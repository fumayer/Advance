package com.quduquxie.module.about.presenter;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.quduquxie.application.QuApplication;
import com.quduquxie.module.about.AboutInterface;

/**
 * Created on 16/11/25.
 * Created by crazylei.
 */

public class AboutPresenter implements AboutInterface.Presenter {

    private AboutInterface.View aboutView;

    public AboutPresenter(@Nullable AboutInterface.View aboutView) {
        this.aboutView = aboutView;
    }

    @Override
    public void init() {
        if (aboutView != null) {
            aboutView.initView();
        }
    }

    @Override
    public void initData() {
        if (aboutView != null) {
            aboutView.initVersion("青果App" + (TextUtils.isEmpty(QuApplication.getVersionName()) ? "" : QuApplication.getVersionName()));
        }
    }
}
