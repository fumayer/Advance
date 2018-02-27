package com.aiwue.okhttp;

import android.os.Handler;
import android.os.Looper;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import rx.Subscriber;
import timber.log.Timber;

/**
 * Created by Administrator on 2017/2/9 0009.
 */

public abstract class BaseCallBack<T> extends Subscriber<T> {
    private Handler mDelivery;
    private long startTime = 0l; // 计算时间
    protected String t_className; // 模板T的类

    private String getTagName(){
        String name = null;
        if (t_className != null) {
            name = t_className;
        } else {
            name = getClass().getName()+'^';
        }
        return name;
    }
    public BaseCallBack() {
        mDelivery = new Handler(Looper.getMainLooper());
    }

    @Override
    public void onStart() {
        super.onStart();
        startTime = System.currentTimeMillis();
    }

    @Override
    public void onCompleted() {
        mDelivery = null;
    }

    @Override
    public void onError(final Throwable e) {
        Timber.e(e);
        e.printStackTrace();
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                String errStr = "出错啦，请稍后再试!";
                if (e instanceof SocketTimeoutException) {
                    errStr = "网络连接超时";
                } else if (e instanceof SocketException) {
                    if (e instanceof ConnectException) {
                        errStr = "网络未连接";
                    } else {
                        errStr = "网络错误";
                    }
                }
                onError(errStr);
            }
        });
    }

    protected void onError(String err) {
    }

    protected void onFailure(String error) {
    }
}
