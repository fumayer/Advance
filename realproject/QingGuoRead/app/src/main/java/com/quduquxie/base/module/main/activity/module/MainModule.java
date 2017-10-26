package com.quduquxie.base.module.main.activity.module;

import com.quduquxie.base.ActivityScope;
import com.quduquxie.base.module.main.activity.presenter.MainPresenter;
import com.quduquxie.base.module.main.activity.view.MainActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created on 17/7/17.
 * Created by crazylei.
 */

@Module
@ActivityScope
public class MainModule {
    private MainActivity mainActivity;

    public MainModule(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Provides
    @ActivityScope
    MainActivity provideMainActivity() {
        return mainActivity;
    }


    @Provides
    @ActivityScope
    MainPresenter provideMainPresenter(MainActivity mainActivity) {
        return new MainPresenter(mainActivity);
    }
}