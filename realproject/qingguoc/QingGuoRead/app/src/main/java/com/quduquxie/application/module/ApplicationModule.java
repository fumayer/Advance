package com.quduquxie.application.module;

import com.quduquxie.application.QuApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created on 16/12/27.
 * Created by crazylei.
 */

@Module
public class ApplicationModule {
    private QuApplication application;

    public ApplicationModule(QuApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    QuApplication provideApplicationContext() {
        return this.application;
    }
}