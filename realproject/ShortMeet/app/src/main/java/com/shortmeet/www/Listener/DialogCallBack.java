package com.shortmeet.www.Listener;

/**
 * Created by zxf on 2017/11/13.
 * 对话框的回调
 */

public interface DialogCallBack<T> {
    void OnSuccess(T t);
    void OnFailed(T t);
}
