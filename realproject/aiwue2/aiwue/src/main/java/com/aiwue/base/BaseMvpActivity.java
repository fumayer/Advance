package com.aiwue.base;

import android.os.Bundle;

/**
 *  MvpActivity 基类
 * Created by Yibao on 17/4/11
 * Copyright (c) 2017 aiwue.com All rights reserved
 */
public abstract class BaseMvpActivity<P extends BasePresenter> extends BaseActivity {
    protected P mvpPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mvpPresenter = createPresenter();
        super.onCreate(savedInstanceState);
    }

    protected abstract P createPresenter();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mvpPresenter != null) {
            mvpPresenter.detachView();
        }
    }

//    protected UserInfo user;




//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        user = AiwueApplication.getInstance().getUserInfo();
//        super.onCreate(savedInstanceState);
//    }
//
//    @Override
//    protected void onResume() {
//        user = AiwueApplication.getInstance().getUserInfo();
//        super.onResume();
//    }

//    public boolean checkLogin() {
//        if (user == null) {
//            intent2Activity(LoginActivity.class);
//            return false;
//        }
//        return true;
//    }

}
