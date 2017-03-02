package com.szh.myretrofittext;

import java.util.List;

/**
 * Created by szh on 2016/12/15.
 */
public class China {
    private String province;

    private List<String> city ;

    public void setProvince(String province){
        this.province = province;
    }
    public String getProvince(){
        return this.province;
    }
    public void setString(List<String> city){
        this.city = city;
    }
    public List<String> getString(){
        return this.city;
    }


    @Override
    public String toString() {
        return "China{" +
                "province='" + province + '\'' +
                ", city=" + city +
                '}';
    }
}
