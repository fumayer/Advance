package com.quduquxie.function.read.end.module;

import com.quduquxie.base.ActivityScope;
import com.quduquxie.function.read.end.presenter.ReadEndPresenter;
import com.quduquxie.function.read.end.view.ReadEndActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created on 17/4/16.
 * Created by crazylei.
 */

@Module
public class ReadEndModule {

    private ReadEndActivity readEndActivity;

    public ReadEndModule(ReadEndActivity readEndActivity) {
        this.readEndActivity = readEndActivity;
    }

    @Provides
    @ActivityScope
    ReadEndActivity provideReadEndActivity() {
        return readEndActivity;
    }

    @Provides
    @ActivityScope
    ReadEndPresenter provideReadEndPresenter() {
        return new ReadEndPresenter(readEndActivity);
    }
}