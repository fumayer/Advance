package com.quduquxie.function.creation.draft.module;

import com.quduquxie.base.ActivityScope;
import com.quduquxie.function.creation.draft.presenter.DraftListPresenter;
import com.quduquxie.function.creation.draft.view.DraftListActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created on 17/4/10.
 * Created by crazylei.
 */

@Module
public class DraftListModule {

    private DraftListActivity draftListActivity;

    public DraftListModule(DraftListActivity draftListActivity) {
        this.draftListActivity = draftListActivity;
    }

    @Provides
    @ActivityScope
    DraftListActivity provideDraftListActivity() {
        return draftListActivity;
    }

    @Provides
    @ActivityScope
    DraftListPresenter provideDraftListPresenter() {
        return new DraftListPresenter(draftListActivity);
    }
}
