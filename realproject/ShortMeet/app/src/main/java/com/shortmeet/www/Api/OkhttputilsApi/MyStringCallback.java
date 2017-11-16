package com.shortmeet.www.Api.OkhttputilsApi;

import android.text.TextUtils;

import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

/**
 * 封装基本的数据返回判断
 */
public class MyStringCallback extends StringCallback {
    private OnLoadListener onLoadListener;
    private boolean success;
    private String message;
    private String errorMessage;

    MyStringCallback(OnLoadListener onLoadListener) {
        this.onLoadListener = onLoadListener;
    }

    @Override
    public void onError(Call call, Exception e, int id) {
        onLoadListener.OnFailed("请求失败");
    }

    @Override
    public void onResponse(String response, int id) {
        if (!TextUtils.isEmpty(response) && isJson(response)) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                success = jsonObject.getBoolean("success");
                message = jsonObject.getString("message");
                errorMessage = jsonObject.getString("errorMessage");
                if (success) {
                    onLoadListener.OnSuccess(response);
                } else {
                    onLoadListener.OnFailed(TextUtils.isEmpty(errorMessage) ? message : errorMessage);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                onLoadListener.OnFailed("请求失败");
            }

        } else {
            onLoadListener.OnFailed("请求失败");
        }

    }


    /*
     *  Fly 注： 判断是否是正确json 串儿
     */
    public  boolean isJson(String str) {
     return str.trim().startsWith("{") && str.trim().endsWith("}");
    }

}
