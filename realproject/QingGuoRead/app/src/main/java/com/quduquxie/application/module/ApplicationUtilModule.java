package com.quduquxie.application.module;

import android.content.Context;

import com.quduquxie.application.ApplicationUtil;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created on 17/3/16.
 * Created by crazylei.
 */

@Module
public class ApplicationUtilModule {

    private final ApplicationUtil applicationUtil;

    public ApplicationUtilModule(Context context) {
        this.applicationUtil = new ApplicationUtil(context);
    }

    @Provides
    @Singleton
    ApplicationUtil provideApplicationUtil() {
        return applicationUtil;
    }
}