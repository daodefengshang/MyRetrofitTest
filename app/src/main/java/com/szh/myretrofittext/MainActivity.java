package com.szh.myretrofittext;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.squareup.okhttp.Cache;
import com.squareup.okhttp.CacheControl;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        File mycache = new File(Environment.getExternalStorageDirectory(), "mycache");
        long cacheSize = 10 * 1024 * 1024;
        Cache cache = new Cache(mycache, cacheSize);
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setCache(cache);
        okHttpClient.interceptors().add(new Interceptor() {
            @Override
            public com.squareup.okhttp.Response intercept(Chain chain) throws IOException {
                CacheControl.Builder cacheBuilder = new CacheControl.Builder();
                cacheBuilder.maxAge(0, TimeUnit.SECONDS);//这个是控制缓存的最大生命时间
                cacheBuilder.maxStale(365,TimeUnit.DAYS);//这个是控制缓存的过时时间
                CacheControl cacheControl = cacheBuilder.build();
                Request request = chain.request();
                if(!isNetworkAvailable()){
                    request = request.newBuilder()
                            .cacheControl(cacheControl)
                            .build();
                }
                com.squareup.okhttp.Response originalResponse = chain.proceed(request);
                if(isNetworkAvailable()) {
                    int maxAge =60;// read from cache
                    return originalResponse.newBuilder()
                            .removeHeader("Pragma")
                            .header("Cache-Control","public ,max-age="+ maxAge)
                            .build();
                } else{
                    int maxStale =60*60*24*28;// tolerate 4-weeks stale
                    return originalResponse.newBuilder()
                            .removeHeader("Pragma")
                            .header("Cache-Control","public, only-if-cached, max-stale="+ maxStale)
                            .build();
                }
            }
        });
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.168.102:8080/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GitHubService service = retrofit.create(GitHubService.class);
        Call<Root> call = service.getDiming("diming.json");
        Log.e("TAG","///////////");
        call.enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Response<Root> response, Retrofit retrofit) {
                List<China> chinaList = response.body().getChina();
                for (China china : chinaList) {
                    String province = china.getProvince();
                    Log.e("TAG",province);
                    List<String> stringList = china.getString();
                    for (String string : stringList) {
                        Log.e("TAG",string);
                    }
                }

            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.e("TAG","---失败---=");
            }
        });
    }

    public boolean isNetworkAvailable(){
        Context context = this.getApplicationContext();
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null)
        {
            return false;
        }
        else
        {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0)
            {
                for (int i = 0; i < networkInfo.length; i++)
                {
                    System.out.println(i + "===状态===" + networkInfo[i].getState());
                    System.out.println(i + "===类型===" + networkInfo[i].getTypeName());
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
