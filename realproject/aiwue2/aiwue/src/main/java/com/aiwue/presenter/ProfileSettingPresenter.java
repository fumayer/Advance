package com.aiwue.presenter;

import com.aiwue.base.BasePresenter;
import com.aiwue.iview.ILoginView;
import com.aiwue.iview.IProfileSettingView;
import com.aiwue.model.User;
import com.aiwue.model.VCodeResult;
import com.aiwue.okhttp.AiwueClient;
import com.aiwue.okhttp.SubscriberCallBack;
import com.aiwue.utils.ConstantValue;
import com.orhanobut.logger.Logger;

/**
 *  我的-》设置页面 presenter
 * Created by Yibao on 2017年4月17日 14:26
 * Copyright (c) 2017 aiwue.com All rights reserved
 */
public class ProfileSettingPresenter extends BasePresenter<IProfileSettingView> {
    public ProfileSettingPresenter(IProfileSettingView mvpView) {
        super(mvpView);
    }

}
