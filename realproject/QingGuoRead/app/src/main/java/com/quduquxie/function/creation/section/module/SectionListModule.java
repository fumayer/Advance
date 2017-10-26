package com.quduquxie.function.creation.section.module;

import com.quduquxie.base.ActivityScope;
import com.quduquxie.function.creation.section.presenter.SectionListPresenter;
import com.quduquxie.function.creation.section.view.SectionListActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created on 17/4/10.
 * Created by crazylei.
 */

@Module
public class SectionListModule {
    private SectionListActivity sectionListActivity;

    public SectionListModule(SectionListActivity sectionListActivity) {
        this.sectionListActivity = sectionListActivity;
    }

    @Provides
    @ActivityScope
    SectionListActivity provideSectionListActivity() {
        return sectionListActivity;
    }

    @Provides
    @ActivityScope
    SectionListPresenter provideSelectionListPresenter() {
        return new SectionListPresenter(sectionListActivity);
    }
}
