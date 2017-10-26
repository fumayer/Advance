package com.quduquxie.module.read.reading.module;

import com.quduquxie.base.ActivityScope;
import com.quduquxie.module.read.reading.presenter.ReadingPresenter;
import com.quduquxie.module.read.reading.view.ReadingActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created on 17/8/1.
 * Created by crazylei.
 */

@Module
@ActivityScope
public class ReadingModule {

    private ReadingActivity readingActivity;

    public ReadingModule(ReadingActivity readingActivity) {
        this.readingActivity = readingActivity;
    }

    @Provides
    @ActivityScope
    ReadingActivity provideReadingActivity() {
        return readingActivity;
    }

    @Provides
    @ActivityScope
    ReadingPresenter provideReadingPresenter() {
        return new ReadingPresenter(readingActivity);
    }
}
