package com.newsdemo.model.http.api;

import com.newsdemo.model.bean.VersionBean;
import com.newsdemo.model.http.response.MyHttpResponse;

import io.reactivex.Flowable;
import retrofit2.http.GET;

/**
 * Created by jianqiang.hu on 2017/5/11.
 */

public interface MyApis {

    String HOST="http://codeest.me/api/geeknews/";
    String APK_DOWNLOAD_URL="http://codeest.me/apk/geeknews.apk";


    @GET("version")
    Flowable<MyHttpResponse<VersionBean>> getVersionInfo();

}
