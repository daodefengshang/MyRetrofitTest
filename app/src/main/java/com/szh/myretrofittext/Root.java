package com.szh.myretrofittext;

import java.util.List;

/**
 * Created by szh on 2016/12/15.
 */
public class Root {
    private List<China> china ;

    public void setChina(List<China> china){
        this.china = china;
    }
    public List<China> getChina(){
        return this.china;
    }

    @Override
    public String toString() {
        return "Root{" +
                "china=" + china +
                '}';
    }
}
