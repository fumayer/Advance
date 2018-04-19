package com.newsdemo.dagger.module;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.newsdemo.dagger.scope.FragmentScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by jianqiang.hu on 2017/5/15.
 */

@Module
public class FragmentModule {
    private Fragment fragment;

    public FragmentModule(Fragment fragment){
        this.fragment=fragment;
    }

    @Provides
    @FragmentScope
    public Activity provideActivity(){
        return fragment.getActivity();
    }

}
