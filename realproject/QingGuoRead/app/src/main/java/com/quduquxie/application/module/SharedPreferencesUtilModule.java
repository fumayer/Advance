package com.quduquxie.application.module;

import android.content.Context;

import com.quduquxie.application.SharedPreferencesUtil;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created on 17/7/13.
 * Created by crazylei.
 */

@Module
public class SharedPreferencesUtilModule {
    private final SharedPreferencesUtil sharedPreferencesUtil;

    public SharedPreferencesUtilModule(Context context) {
        this.sharedPreferencesUtil = new SharedPreferencesUtil(context);
    }

    @Provides
    @Singleton
    SharedPreferencesUtil provideSharePreferenceUtil() {
        return sharedPreferencesUtil;
    }
}