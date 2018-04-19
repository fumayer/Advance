package com.newsdemo.base;

/**
 * Created by jianqiang.hu on 2017/5/11.
 */

public interface BaseView {
    void showErrorMsg(String msg);
    void useNightMode(boolean isNight);

    //=====State=============
    void stateError();
    void stateLoading();
    void stateMain();
}
