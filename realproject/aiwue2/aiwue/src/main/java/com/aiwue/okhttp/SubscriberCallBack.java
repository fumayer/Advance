package com.aiwue.okhttp;

import com.aiwue.model.response.ResultResponse;

/**
 * Created by Administrator
 * on 2016/5/18.
 */
public abstract class SubscriberCallBack<T> extends BaseCallBack<ResultResponse<T>> {


    @Override
    public void onNext(ResultResponse response) {

        if (response.getEcode().equals(ResultResponse.ECODE_SUCCESS)) {
            onSuccess((T) response.getResult());
        } else if (response.getEcode().equals(ResultResponse.ECODE_NO_RESULT)) {
            onSuccess(null);
        }else {
            onError(response.getEmsg());
        }
    }

    protected abstract void onSuccess(T response);
}
