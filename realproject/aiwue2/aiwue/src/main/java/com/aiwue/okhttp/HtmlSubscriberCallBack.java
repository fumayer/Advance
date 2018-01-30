package com.aiwue.okhttp;

/**
 * Created by Administrator on 2017/2/9 0009.
 */

public abstract class HtmlSubscriberCallBack extends BaseCallBack<String> {
    @Override
    public void onNext(String response) {
        onSuccess(response);
    }

    protected abstract void onSuccess(String response);
}
