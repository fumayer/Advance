package com.aiwue.presenter;

import com.aiwue.base.BasePresenter;
import com.aiwue.iview.ILoginView;
import com.aiwue.model.User;
import com.aiwue.model.VCodeResult;
import com.aiwue.okhttp.AiwueClient;
import com.aiwue.okhttp.SubscriberCallBack;
import com.aiwue.utils.ConstantValue;
import com.orhanobut.logger.Logger;

/**
 *  登录页面 presenter
 * Created by Yibao on 2017年4月13日10:11:36
 * Copyright (c) 2017 aiwue.com All rights reserved
 */
public class LoginPresenter extends BasePresenter<ILoginView> {
    public LoginPresenter(ILoginView mvpView) {
        super(mvpView);
    }
    //发送验证码
    public void sendVCode(String destination) {
        AiwueClient.sendVCode(destination, ConstantValue.VCODE_OPERATION_REGISTER, new SubscriberCallBack<VCodeResult>() {

            @Override
            protected void onSuccess(VCodeResult response) {
                Logger.i(response.toString());
                mvpView.onSendVCodeSuccess(true, null,response.id);

            }
            @Override
            protected void onError(String err) {
                mvpView.onSendVCodeSuccess(false, err,null);
            }
        });
    }
    //验证码登录
    public void LoginWithVCode(String mobile, int verifyId, String verifyCode, String deviceName) {
        AiwueClient.loginWithVCode(mobile, verifyId, verifyCode, deviceName, new SubscriberCallBack<User>(){
            @Override
            protected void onSuccess(User response) {
                Logger.i(response.toString());
                mvpView.onLoginSuccess(true, null,response);
            }
            @Override
            protected void onError(String err) {
                mvpView.onLoginSuccess(false, err,null);
            }
        });
    }
    //账号密码登录
    public void LoginWithNormal(String account, String password, String ipAddr) {
        AiwueClient.loginWithNormal(account, password, ipAddr, new SubscriberCallBack<User>(){
            @Override
            protected void onSuccess(User response) {
                Logger.i(response.toString());
                mvpView.onLoginSuccess(true, null,response);
            }
            @Override
            protected void onError(String err) {
                mvpView.onLoginSuccess(false, err,null);
            }
        });
    }
    //三方登录
    public void thirdPartyLogin(String openId, String openAccessToken, Integer type, String ipAddr) {
        AiwueClient.thirdPartyLogin(openId, openAccessToken, type, ipAddr, new SubscriberCallBack<User>(){
            @Override
            protected void onSuccess(User response) {
                Logger.i(response.toString());
                mvpView.onLoginSuccess(true, null,response);
            }
            @Override
            protected void onError(String err) {
                mvpView.onLoginSuccess(false, err,null);
            }
        });
    }
}
