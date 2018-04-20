package com.newsdemo.base;

/**
 * Created by jianqiang.hu on 2017/5/11.
 * Presenter基类
 */

public interface BasePresenter<T extends BaseView> {

    void attachView(T view);


    void detachView();
}
