package com.quduquxie.application.component;

import com.quduquxie.application.QuApplication;
import com.quduquxie.application.module.ApplicationModule;
import com.quduquxie.application.module.ApplicationUtilModule;
import com.quduquxie.application.ApplicationUtil;
import com.quduquxie.application.module.SharedPreferencesUtilModule;
import com.quduquxie.application.SharedPreferencesUtil;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created on 16/12/27.
 * Created by crazylei.
 */

@Singleton
@Component(modules = {ApplicationModule.class, ApplicationUtilModule.class, SharedPreferencesUtilModule.class})
public interface ApplicationComponent {

    QuApplication getQuApplication();

    ApplicationUtil getApplicationUtil();

    SharedPreferencesUtil getSharedPreferencesUtil();
}