package com.newsdemo.widget;

import android.text.TextUtils;

import com.newsdemo.base.BaseView;
import com.newsdemo.model.http.exception.APIException;
import com.newsdemo.util.LogUtil;

import io.reactivex.subscribers.ResourceSubscriber;
import retrofit2.HttpException;


/**
 * Created by jianqiang.hu on 2017/5/12.
 */

public abstract class CommonSubscriber<T> extends ResourceSubscriber<T>{
    private BaseView mView;
    private String mErrorMsg;
    private boolean isShowErrorState=true;

    protected CommonSubscriber(BaseView mView){
        this.mView=mView;
    }

    protected CommonSubscriber(BaseView mView,String mErrorMsg){
        this.mView=mView;
        this.mErrorMsg=mErrorMsg;
    }


    protected CommonSubscriber(BaseView view, boolean isShowErrorState){
        this.mView = view;
        this.isShowErrorState = isShowErrorState;
    }

    protected CommonSubscriber(BaseView view, String errorMsg, boolean isShowErrorState){
        this.mView = view;
        this.mErrorMsg = errorMsg;
        this.isShowErrorState = isShowErrorState;
    }

    @Override
    public void onComplete() {

    }
    @Override
    public void onError(Throwable t) {
        if (mView==null){
            return;
        }
        if (mErrorMsg!=null&& !TextUtils.isEmpty(mErrorMsg)){
            mView.showErrorMsg(mErrorMsg);
        }else if(t instanceof APIException){
            mView.showErrorMsg(t.toString());
        }else if(t instanceof HttpException){
            mView.showErrorMsg("数据加载失败ヽ(≧Д≦)ノ");
        }else{
            mView.showErrorMsg("未知错误ヽ(≧Д≦)ノ");
            LogUtil.d(t.toString());
        }

        if (isShowErrorState){
            mView.stateError();
        }
    }
}
