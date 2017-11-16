package com.shortmeet.www.Api.OkhttputilsApi;

import java.io.File;

/**
 * Created by Fenglingyue on 2017/8/31.
 */
 // Fly 注：  定义我们的模板  okhttputils  功能
public interface IRequestManager {
    /**
     * post上传json数据
     *
     * @param url
     * @param requestBody
     * @param onLoadListener
     */
    void postString(String url, String requestBody, OnLoadListener onLoadListener);

    /**
     * 携带token, post上传json数据
     *
     * @param url
     * @param token
     * @param requestBody
     * @param onLoadListener
     */
    void postWithToken(String url, String token, String requestBody, OnLoadListener onLoadListener);

    /**
     * 携带uid 上传文件
     *
     * @param uid
     * @param file
     * @param onLoadListener
     */
    void postFile(String url, String token, String uid, File file, OnLoadListener onLoadListener);
}
