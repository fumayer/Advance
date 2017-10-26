package com.quduquxie.base.retrofit;

import com.orhanobut.logger.Logger;
import com.quduquxie.application.QuApplication;
import com.quduquxie.base.config.BaseConfig;
import com.quduquxie.db.UserDao;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created on 17/7/12.
 * Created by crazylei.
 */

public class DataRequestFactory {

    public static final int TYPE_CDN = 0x80;
    public static final int TYPE_HTTP = 0x81;
    public static final int TYPE_HTTPS = 0x82;

    private static final String API_HTTP_FORMAL_URL = "http://api.qingoo.cn:9090";
    private static final String API_HTTP_DEVELOP_URL = "http://t.api.qgread.com:9090";

    private static final String API_HTTPS_FORMAL_URL = "https://apis.qingoo.cn";
    private static final String API_HTTPS_DEVELOP_URL = "http://t.api.qgread.com:9090";

    private static final String API_CDN_FORMAL_URL = "s.apis.qingoo.cn";
    private static final String API_CDN_DEVELOP_URL = "http://t.api.qgread.com:9090";

    private static final String API_DOWNLOAD_FORMAL_URL = "http://oss.qingoo.cn";

    private static final String API_AUTHORIZATION_FORMAL_URL = "https://graph.qq.com";

    public static <S> S instantiation(Class<S> serviceClass, final int type, final boolean status) {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(BaseConfig.DEFAULT_CONNECT_TIMEOUT, TimeUnit.SECONDS);
        httpClientBuilder.readTimeout(BaseConfig.DEFAULT_READ_TIMEOUT, TimeUnit.SECONDS);

        httpClientBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {

                Request request = chain.request();

                HttpUrl.Builder httpUrlBuilder = request.url()
                        .newBuilder()
                        .addQueryParameter("packagename", QuApplication.getQuPackageName())
                        .addQueryParameter("os", "android")
                        .addQueryParameter("udid", QuApplication.getUserDeviceID())
                        .addQueryParameter("appversion", String.valueOf(QuApplication.getVersionCode()))
                        .addQueryParameter("ch", QuApplication.getChannelID());

                if (status) {
                    httpUrlBuilder.addQueryParameter("token", UserDao.getToken(QuApplication.getInstance()));
                }

                Request requestBuilder = request.newBuilder()
                        .method(request.method(), request.body())
                        .url(httpUrlBuilder.build())
                        .tag(httpUrlBuilder.build())
                        .build();

                Logger.e("Instantiation: " + requestBuilder.toString());

                return chain.proceed(requestBuilder);
            }
        });

        Retrofit.Builder retrofitBuilder = new Retrofit.Builder();

        retrofitBuilder.client(httpClientBuilder.build());
        retrofitBuilder.addConverterFactory(GsonConverterFactory.create());
        retrofitBuilder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());

        switch (type) {
            case TYPE_CDN:
                retrofitBuilder.baseUrl(API_CDN_FORMAL_URL);
                break;
            case TYPE_HTTP:
                retrofitBuilder.baseUrl(API_HTTP_FORMAL_URL);
                break;
            case TYPE_HTTPS:
                retrofitBuilder.baseUrl(API_HTTPS_FORMAL_URL);
                break;
        }

        Retrofit retrofit = retrofitBuilder.build();

        return retrofit.create(serviceClass);
    }

    public static <S> S instantiationDownload(Class<S> serviceClass) {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(BaseConfig.DEFAULT_CONNECT_TIMEOUT, TimeUnit.SECONDS);
        httpClientBuilder.readTimeout(BaseConfig.DEFAULT_READ_TIMEOUT, TimeUnit.SECONDS);

        httpClientBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {

                Request request = chain.request();

                HttpUrl.Builder httpUrlBuilder = request.url().newBuilder();

                Request requestBuilder = request.newBuilder()
                        .method(request.method(), request.body())
                        .url(httpUrlBuilder.build())
                        .tag(httpUrlBuilder.build())
                        .build();

                Logger.e("InstantiationDownload: " + requestBuilder.toString());

                return chain.proceed(requestBuilder);
            }
        });

        Retrofit retrofit = new Retrofit.Builder()
                .client(httpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(API_DOWNLOAD_FORMAL_URL)
                .build();

        return retrofit.create(serviceClass);
    }

    public static <S> S instantiationAuthorization(Class<S> serviceClass) {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(BaseConfig.DEFAULT_CONNECT_TIMEOUT, TimeUnit.SECONDS);
        httpClientBuilder.readTimeout(BaseConfig.DEFAULT_READ_TIMEOUT, TimeUnit.SECONDS);

        httpClientBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {

                Request request = chain.request();

                HttpUrl.Builder httpUrlBuilder = request.url()
                        .newBuilder()
                        .addQueryParameter("unionid", "1");

                Request requestBuilder = request.newBuilder()
                        .method(request.method(), request.body())
                        .url(httpUrlBuilder.build())
                        .build();

                Logger.e("InstantiationAuthorization: " + requestBuilder.toString());

                return chain.proceed(requestBuilder);
            }
        });

        Retrofit retrofit = new Retrofit.Builder()
                .client(httpClientBuilder.build())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(API_AUTHORIZATION_FORMAL_URL)
                .build();

        return retrofit.create(serviceClass);
    }
}