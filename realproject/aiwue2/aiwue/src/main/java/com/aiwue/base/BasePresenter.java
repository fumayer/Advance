package com.aiwue.base;


import com.aiwue.model.Notice;
import com.aiwue.utils.RxBus;

import rx.subscriptions.CompositeSubscription;

/**
 *  Presenter 基类
 * Created by Yibao on 17/4/11
 * Copyright (c) 2017 aiwue.com All rights reserved
 */
public class BasePresenter<V> implements Presenter<V> {
    public V mvpView;
    private CompositeSubscription mCompositeSubscription;

    @Override
    public void attachView(V mvpView) {
        this.mvpView = mvpView;
    }

    public BasePresenter(V mvpView)
    {
        attachView(mvpView);
    }


    @Override
    public void detachView() {
        this.mvpView = null;
//        onUnsubscribe();
    }


//    //RXjava取消注册，以避免内存泄露
//    public void onUnsubscribe() {
//        if (mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) {
//            mCompositeSubscription.unsubscribe();
//        }
//    }
    /**
     * 发送消息
     */
    public void post(Notice msg) {
        RxBus.getDefault().post(msg);
    }


//    public void addSubscription(Observable observable, Subscriber subscriber) {
//        if (mCompositeSubscription == null) {
//            mCompositeSubscription = new CompositeSubscription();
//        }
//        mCompositeSubscription.add(observable
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(subscriber));
//    }
}
