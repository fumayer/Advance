package com.shortmeet.www.Api;


import com.shortmeet.www.Api.RetrofitApi.RetrofitClient;

/**
 * Created by Fenglingyue on 2017/8/20.
 */

public class RequestFactory {
    public static <T> T chooseKindApi(Class<T> clz) {
        if(clz== RetrofitClient.class){
          return (T)RetrofitClient.getRetroClientInstance();
        }
//        if(clz== OkRequestManager.class){
//            return (T) OkRequestManager.getOkhttpIntance();
//        }
         return (T)RetrofitClient.getRetroClientInstance();
    }
}
