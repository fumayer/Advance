package com.newsdemo.base;

import android.support.v7.app.AppCompatDelegate;
import android.view.ViewGroup;

import com.newsdemo.R;
import com.newsdemo.app.App;
import com.newsdemo.dagger.component.ActivityComponent;
import com.newsdemo.dagger.component.DaggerActivityComponent;
import com.newsdemo.dagger.module.ActivityModule;
import com.newsdemo.util.SnackbarUtil;

import javax.inject.Inject;



/**
 * Created by jianqiang.hu on 2017/5/11.
 */

public abstract class BaseActivity<T extends BasePresenter> extends SimpleActivity  implements BaseView{

    @Inject
    protected T mPresenter;

    @Override
    protected void onViewCreated() {
        super.onViewCreated();
        initInject();
        if (mPresenter!=null){
            mPresenter.attachView(this);
        }

    }


    protected ActivityComponent getActivityComponent(){
        return DaggerActivityComponent.builder()
                .appComponent(App.getAppComponent())
                .activityModule(getActivityModule())
                .build();
    }


    protected ActivityModule getActivityModule(){
        return new ActivityModule(this);
    }

    @Override
    protected void onDestroy() {
        if (mPresenter!=null){
            mPresenter.detachView();
        }
        super.onDestroy();
    }

    @Override
    public void showErrorMsg(String msg) {
        SnackbarUtil.show(((ViewGroup)findViewById(android.R.id.content)).getChildAt(0),msg);
    }

    @Override
    public void useNightMode(boolean isNight) {
        if (isNight){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        recreate();
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
