package com.quduquxie.modular.cover.module;

import com.quduquxie.base.ActivityScope;
import com.quduquxie.modular.cover.presenter.CoverPresenter;
import com.quduquxie.modular.cover.view.CoverActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created on 17/4/13.
 * Created by crazylei.
 */

@Module
public class CoverModule {

    private CoverActivity coverActivity;

    public CoverModule(CoverActivity coverActivity) {
        this.coverActivity = coverActivity;
    }

    @Provides
    @ActivityScope
    CoverActivity provideCoverActivity() {
        return coverActivity;
    }

    @Provides
    @ActivityScope
    CoverPresenter provideCoverPresenter() {
        return new CoverPresenter(coverActivity);
    }
}
