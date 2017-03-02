package com.szh.myretrofittext;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Path;

/**
 * Created by szh on 2016/12/14.
 */
public interface GitHubService {
    @Headers("Cache-Control: public, max-age=3600")
    @GET("{json}")
    Call<Root> getDiming(@Path("json") String json);
}
