package com.quduquxie.function.creation.write.module;

import com.quduquxie.base.ActivityScope;
import com.quduquxie.function.creation.write.presenter.SectionWritePresenter;
import com.quduquxie.function.creation.write.view.SectionWriteActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created on 17/4/10.
 * Created by crazylei.
 */

@Module
public class SectionWriteModule {

    private SectionWriteActivity sectionWriteActivity;

    public SectionWriteModule(SectionWriteActivity sectionWriteActivity) {
        this.sectionWriteActivity = sectionWriteActivity;
    }

    @Provides
    @ActivityScope
    SectionWriteActivity provideSectionWriteActivity() {
        return sectionWriteActivity;
    }

    @Provides
    @ActivityScope
    SectionWritePresenter provideSectionWritePresenter() {
        return new SectionWritePresenter(sectionWriteActivity);
    }
}
