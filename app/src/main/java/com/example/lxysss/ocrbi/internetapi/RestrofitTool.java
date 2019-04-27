package com.example.lxysss.ocrbi.internetapi;

import java.net.URL;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Lxysss on 2018/10/19.
 */

public class RestrofitTool {
   static Retrofit retrofit = new Retrofit.Builder()
            //设置数据解析器
            .addConverterFactory(GsonConverterFactory.create())
            //设置网络请求的Url地址
            .baseUrl("http://47.101.219.136:80/")
            .build();
    /* 创建网络请求接口的实例 */


    private  static  ApiService mApi = retrofit.create(ApiService.class);

    public static  ApiService getmApi(){
        return mApi;
    }
}
