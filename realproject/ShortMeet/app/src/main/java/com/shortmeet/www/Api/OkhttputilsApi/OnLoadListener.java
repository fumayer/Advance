package com.shortmeet.www.Api.OkhttputilsApi;

/**
 * Created by Fenglingyue on 2017/8/31.
 */

public interface OnLoadListener<T> {
    void OnSuccess(T t);
    void OnFailed(String msg);
}
