package com.quduquxie.base.module.splash;

import com.quduquxie.base.BasePresenter;
import com.quduquxie.base.BaseView;
import com.quduquxie.base.bean.Splash;

/**
 * Created on 17/7/12.
 * Created by crazylei.
 */

public interface SplashInterface {

    interface Presenter extends BasePresenter {

        void loadSplashRecommend();

        void checkDefaultBook();

        void initializePresenter();

        void checkStartActivity();
    }

    interface View extends BaseView {

        void showRecommend(Splash splash);

        void startMainActivity();
    }
}