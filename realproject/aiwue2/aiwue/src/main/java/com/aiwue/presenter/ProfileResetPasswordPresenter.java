package com.aiwue.presenter;

import com.aiwue.base.BasePresenter;
import com.aiwue.iview.IProfileResetPasswordView;
import com.aiwue.iview.IProfileSettingView;
import com.aiwue.model.NullResult;
import com.aiwue.model.User;
import com.aiwue.model.VCodeResult;
import com.aiwue.okhttp.AiwueClient;
import com.aiwue.okhttp.SubscriberCallBack;
import com.aiwue.utils.ConstantValue;
import com.orhanobut.logger.Logger;

/**
 *  我的-》ResetPassword presenter
 * Created by Yibao on 2017年4月18日 14:26
 * Copyright (c) 2017 aiwue.com All rights reserved
 */
public class ProfileResetPasswordPresenter extends BasePresenter<IProfileResetPasswordView> {
    public ProfileResetPasswordPresenter(IProfileResetPasswordView mvpView) {
        super(mvpView);
    }
    //发送验证码
    public void resetPassword(String newpwd){
        AiwueClient.resetPassword(newpwd, new SubscriberCallBack<NullResult>() {
            @Override
            protected void onSuccess(NullResult response) {
                mvpView.onResetPasswordSuccess(true,null);
            }
            @Override
            protected void onError(String err) {
                mvpView.onResetPasswordSuccess(false,err);
            }
        });


    }



}
