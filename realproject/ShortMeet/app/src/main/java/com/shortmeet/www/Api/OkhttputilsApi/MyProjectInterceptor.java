package com.shortmeet.www.Api.OkhttputilsApi;

import android.text.TextUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * Created by Fenglingyue on 2017/9/2.
 *  okhttputils 添加拦截器
 *  post 访问的时候默认 传递一些参数
 */

public class MyProjectInterceptor implements Interceptor {
    Map<String, String> getParamsMap = new HashMap<>();

    @Override
     public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder requestBuilder = request.newBuilder();
//
//        if (TextUtils.equals(request.method(), "POST")) {
//            if (canInjectIntoBody(request)) {
//                long time = System.currentTimeMillis();
//                String userPid = LoginUtil.getUserPid();
//                String token = LoginUtil.getToken();
//                String sign = MD5Utils.getMD5String(userPid + token + time).trim().toUpperCase();
//                FormBody.Builder formBodyBuilder = new FormBody.Builder();
//                formBodyBuilder.add("loginUserPid", String.valueOf(userPid));
//                formBodyBuilder.add("timestamp", String.valueOf(time));
//                formBodyBuilder.add("sign", sign);
//
//                RequestBody formBody = formBodyBuilder.build();
//                String postBodyString = bodyToString(request.body());
//                postBodyString += ((postBodyString.length() > 0) ? "&" : "") + bodyToString(formBody);
//                requestBuilder.post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded;charset=UTF-8"), postBodyString));
//                Log.e("aa","我的天呀穿的数据是===>>>>"+"loginUserPid"+String.valueOf(userPid)+ "   #timestamp"+String.valueOf(time)+"   #sign"+sign);
//            }
//        }
//
//
        request = requestBuilder.build();
        return chain.proceed(request);
    }

    private boolean canInjectIntoBody(Request request) {
        if (request == null) {
            return false;
        }
        if (!TextUtils.equals(request.method(), "POST")) {
            return false;
        }
        RequestBody body = request.body();
        if (body == null) {
            return false;
        }
//        MediaType mediaType = body.contentType();
//        if (mediaType == null) {
//            return false;
//        }
//        if (!TextUtils.equals(mediaType.subtype(), "x-www-form-urlencoded")) {
//            return false;
//        }
        return true;
    }

    // func to inject params into url
    private Request injectParamsIntoUrl(HttpUrl.Builder httpUrlBuilder, Request.Builder requestBuilder, Map<String, String> paramsMap) {
        if (paramsMap.size() > 0) {
            Iterator iterator = paramsMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                httpUrlBuilder.addQueryParameter((String) entry.getKey(), (String) entry.getValue());
            }
            requestBuilder.url(httpUrlBuilder.build());
            return requestBuilder.build();
        }

        return null;
    }

    private static String bodyToString(final RequestBody request) {
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if (copy != null)
                copy.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }

    public static class Builder {

        MyProjectInterceptor interceptor;

        public Builder() {
            interceptor = new MyProjectInterceptor();
        }

        public MyProjectInterceptor build() {
            return interceptor;
        }

    }
}
