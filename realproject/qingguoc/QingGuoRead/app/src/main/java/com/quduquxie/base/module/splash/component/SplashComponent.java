package com.quduquxie.base.module.splash.component;

import com.quduquxie.application.component.ApplicationComponent;
import com.quduquxie.base.ActivityScope;
import com.quduquxie.base.module.splash.module.SplashModule;
import com.quduquxie.base.module.splash.presenter.SplashPresenter;
import com.quduquxie.base.module.splash.view.SplashActivity;

import dagger.Component;

/**
 * Created on 17/7/12.
 * Created by crazylei.
 */

@ActivityScope
@Component(modules = SplashModule.class, dependencies = ApplicationComponent.class)
public interface SplashComponent {

    SplashActivity inject(SplashActivity splashActivity);

    SplashPresenter presenter();
}