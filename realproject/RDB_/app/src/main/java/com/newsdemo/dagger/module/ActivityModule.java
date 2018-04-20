package com.newsdemo.dagger.module;

import android.app.Activity;

import com.newsdemo.dagger.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by jianqiang.hu on 2017/5/11.
 */

@Module
public class ActivityModule {
    private Activity mActivity;

    public ActivityModule(Activity activity){
        this.mActivity=activity;
    }


    @Provides
    @ActivityScope
    public Activity provideActivity(){
        return mActivity;
    }
}
