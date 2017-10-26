package com.quduquxie.base.module.splash.module;

import com.quduquxie.base.ActivityScope;
import com.quduquxie.base.module.splash.presenter.SplashPresenter;
import com.quduquxie.base.module.splash.view.SplashActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created on 17/7/12.
 * Created by crazylei.
 */

@Module
@ActivityScope
public class SplashModule {

    private SplashActivity splashActivity;

    public SplashModule(SplashActivity splashActivity) {
        this.splashActivity = splashActivity;
    }

    @Provides
    @ActivityScope
    SplashActivity provideSplashActivity(SplashActivity splashActivity) {
        return splashActivity;
    }

    @Provides
    @ActivityScope
    SplashPresenter provideSplashPresenter() {
        return new SplashPresenter(splashActivity);
    }
}