package com.quduquxie.base.module.main.activity.component;

import com.quduquxie.application.component.ApplicationComponent;
import com.quduquxie.base.ActivityScope;
import com.quduquxie.base.module.main.activity.module.MainModule;
import com.quduquxie.base.module.main.activity.presenter.MainPresenter;
import com.quduquxie.base.module.main.activity.view.MainActivity;

import dagger.Component;

/**
 * Created on 17/7/17.
 * Created by crazylei.
 */

@ActivityScope
@Component(modules = MainModule.class, dependencies = ApplicationComponent.class)
public interface MainComponent {

    MainActivity inject(MainActivity mainActivity);

    MainPresenter presenter();
}