package com.newsdemo.base.contract.main;

import com.newsdemo.base.BasePresenter;
import com.newsdemo.base.BaseView;
import com.newsdemo.model.bean.WelcomeBean;

/**
 * Created by jianqiang.hu on 2017/5/11.
 */

public interface WelcomeContract {
    interface View extends BaseView{
        void showContent(WelcomeBean welcomeBean);
        void jumpToMain();
    }
    interface Presenter extends BasePresenter<View>{
        void getWelcomeData();
    }
}
