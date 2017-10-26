package com.quduquxie.base.module.tabulation.module;

import com.quduquxie.base.ActivityScope;
import com.quduquxie.base.module.tabulation.presenter.TabulationPresenter;
import com.quduquxie.base.module.tabulation.view.TabulationActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created on 17/7/25.
 * Created by crazylei.
 */

@Module
@ActivityScope
public class TabulationModule {

    private TabulationActivity tabulationActivity;

    public TabulationModule(TabulationActivity tabulationActivity) {
        this.tabulationActivity = tabulationActivity;
    }

    @Provides
    @ActivityScope
    TabulationActivity provideTabulationActivity() {
        return tabulationActivity;
    }

    @Provides
    @ActivityScope
    TabulationPresenter provideTabulationPresenter() {
        return new TabulationPresenter(tabulationActivity);
    }
}