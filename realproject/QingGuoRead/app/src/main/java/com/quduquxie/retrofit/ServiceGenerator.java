package com.quduquxie.retrofit;

import com.orhanobut.logger.Logger;
import com.quduquxie.application.QuApplication;
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

public class ServiceGenerator {

    private static int DEFAULT_READ_TIMEOUT = 15000;
    private static int DEFAULT_CONNECT_TIMEOUT = 20000;

    private static final String API_FORMAL_BASE_URL = "http://api.qingoo.cn:9090";
    private static final String API_DEVELOP_BASE_URL = "http://t.api.qgread.com:9090";

    private static final String API_HTTPS_FORMAL_BASE_URL = "https://apis.qingoo.cn";
    private static final String API_HTTPS_DEVELOP_BASE_URL = "http://t.api.qgread.com:9090";

    private static final String API_FORMAL_DOWNLOAD_APPLICATION = "http://oss.qingoo.cn";

    private static final String API_FORMAL_QQ = "https://graph.qq.com";

    public static <S> S getInstance(Class<S> serviceClass, final boolean status) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_CONNECT_TIMEOUT, TimeUnit.SECONDS);
        builder.readTimeout(DEFAULT_READ_TIMEOUT, TimeUnit.SECONDS);

        builder.addInterceptor(new Interceptor() {
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

                Logger.e(requestBuilder.toString());

                return chain.proceed(requestBuilder);
            }
        });

        Retrofit retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(API_FORMAL_BASE_URL)
                .build();

        return retrofit.create(serviceClass);
    }

    public static <S> S getHttpsInstance(Class<S> serviceClass, final boolean status) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_CONNECT_TIMEOUT, TimeUnit.SECONDS);
        builder.readTimeout(DEFAULT_READ_TIMEOUT, TimeUnit.SECONDS);

        builder.addInterceptor(new Interceptor() {
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

                Logger.e(requestBuilder.toString());

                return chain.proceed(requestBuilder);
            }
        });

        Retrofit retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(API_HTTPS_FORMAL_BASE_URL)
                .build();

        return retrofit.create(serviceClass);
    }

    //应用检查更新
    public static <S> S getInstance(Class<S> serviceClass) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_CONNECT_TIMEOUT, TimeUnit.SECONDS);
        builder.readTimeout(DEFAULT_READ_TIMEOUT, TimeUnit.SECONDS);

        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {

                Request request = chain.request();

                HttpUrl.Builder httpUrlBuilder = request.url().newBuilder();

                Request requestBuilder = request.newBuilder()
                        .method(request.method(), request.body())
                        .url(httpUrlBuilder.build())
                        .tag(httpUrlBuilder.build())
                        .build();

                Logger.e(requestBuilder.toString());

                return chain.proceed(requestBuilder);
            }
        });

        Retrofit retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(API_FORMAL_DOWNLOAD_APPLICATION)
                .build();

        return retrofit.create(serviceClass);
    }

    //QQ授权
    public static <S> S getSpecialGenerator(Class<S> serviceClass) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_CONNECT_TIMEOUT, TimeUnit.SECONDS);
        builder.readTimeout(DEFAULT_READ_TIMEOUT, TimeUnit.SECONDS);

        builder.addInterceptor(new Interceptor() {
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

                Logger.e(requestBuilder.toString());

                return chain.proceed(requestBuilder);
            }
        });

        Retrofit retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(API_FORMAL_QQ)
                .build();

        return retrofit.create(serviceClass);
    }
}
