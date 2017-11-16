package com.shortmeet.www.Api.OkhttputilsApi;


import com.zhy.http.okhttp.OkHttpUtils;

import java.io.File;

import okhttp3.MediaType;

/**
 * Created by Fenglingyue on 2017/8/31.
 */

public class OkRequestManager implements IRequestManager {
     private static OkRequestManager okhttpInstance;
     private OkRequestManager(){};

    public  static OkRequestManager getOkhttpIntance(){
        if (okhttpInstance == null) {
            synchronized (OkRequestManager.class) {
                if(okhttpInstance==null){
                 okhttpInstance = new OkRequestManager();
                }
            }
        }
        return okhttpInstance;
    }

    @Override
    public void postString(String url, String requestBody, OnLoadListener onLoadListener) {
        OkHttpUtils.postString()
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .url(url)
                .content(requestBody)
                .build()
                .execute(new MyStringCallback(onLoadListener));
    }

    @Override
    public void postWithToken(String url, String token, String requestBody, OnLoadListener onLoadListener) {
        OkHttpUtils.postString()
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .addHeader("token", token)
                .url(url)
                .content(requestBody)
                .build()
                .execute(new MyStringCallback(onLoadListener));
    }

    @Override
    public void postFile(String url, String token, String uid, File file, OnLoadListener onLoadListener) {
        OkHttpUtils.post()
                .url(url)
                .addHeader("token", token)
                .addParams("userId", uid)
                .addFile("file", file.getName(), file)
                .build()
                .execute(new MyStringCallback(onLoadListener));
    }
}
