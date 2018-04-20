package com.newsdemo.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.newsdemo.app.App;
import com.newsdemo.dagger.component.DaggerFragmentComponent;
import com.newsdemo.dagger.component.FragmentComponent;
import com.newsdemo.dagger.module.FragmentModule;
import com.newsdemo.util.SnackbarUtil;


import javax.inject.Inject;

/**
 * Created by jianqiang.hu on 2017/5/15.
 */

public abstract  class BaseFragment<T extends BasePresenter> extends SimpleFragment implements BaseView{

    @Inject
    protected T mPresentter;


    protected FragmentComponent getFragmentComponent(){
        return DaggerFragmentComponent.builder()
                .appComponent(App.getAppComponent())
                .fragmentModule(getFragmentModule())
                .build();
    }

    protected FragmentModule getFragmentModule(){
        return new FragmentModule(this);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initInject();
        mPresentter.attachView(this);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        if (mPresentter!=null)
            mPresentter.detachView();
        super.onDestroyView();
    }


    @Override
    public void showErrorMsg(String msg) {
        SnackbarUtil.show(((ViewGroup)getActivity().findViewById(android.R.id.content)).getChildAt(0),msg);
    }

    @Override
    public void useNightMode(boolean isNight) {

    }

    @Override
    public void stateError() {

    }

    @Override
    public void stateLoading() {

    }

    @Override
    public void stateMain() {

    }

    protected abstract void initInject();
}
