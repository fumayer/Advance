package com.quduquxie.base.widget;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;

import com.quduquxie.R;
import com.quduquxie.application.component.ApplicationComponent;
import com.quduquxie.base.BaseActivity;
import com.quduquxie.base.BasePresenter;
import com.quduquxie.widget.LeftSlideLayout;

import javax.inject.Inject;

/**
 * Created on 17/8/1.
 * Created by crazylei.
 */

public abstract class SlideBackActivity<T extends BasePresenter> extends BaseActivity<T> {

    @Inject
    protected T presenter;

    protected LeftSlideLayout left_slide_layout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        left_slide_layout = (LeftSlideLayout) LayoutInflater.from(this).inflate(R.layout.layout_activity_swipe_back, null);
        left_slide_layout.attachToActivity(this);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.activity_left_in, R.anim.activity_left_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_left_in, R.anim.activity_left_out);
    }

    @Override
    protected void setActivityComponent(ApplicationComponent applicationComponent) {

    }

    @Override
    public void initializeParameter() {

    }

    @Override
    public void recycle() {

    }
}
