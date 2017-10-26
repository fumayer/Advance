package com.quduquxie.module.setting.presenter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.alibaba.sdk.android.feedback.impl.FeedbackAPI;
import com.quduquxie.application.QuApplication;
import com.quduquxie.base.bean.Update;
import com.quduquxie.base.config.BaseConfig;
import com.quduquxie.module.setting.SettingInterface;
import com.quduquxie.util.APIUpdateHelper;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * Created on 17/2/15.
 * Created by crazylei.
 */

public class SettingPresenter implements SettingInterface.Presenter, APIUpdateHelper.CheckUpdateListener {

    private SettingInterface.View settingView;
    private SharedPreferences sharedPreferences;
    private WeakReference<Activity> activityReference;

    private APIUpdateHelper apiUpdateHelper;

    public SettingPresenter(@NonNull SettingInterface.View settingView, Activity activity) {
        this.settingView = settingView;
        this.activityReference = new WeakReference<>(activity);
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
    }

    @Override
    public void init() {

    }

    @Override
    public void initParameter() {
        if (settingView != null) {
            settingView.initView();
        }

        FeedbackAPI.initAnnoy(activityReference.get().getApplication(), BaseConfig.FEEDBACK_APP_KEY);

        Map<String, String> parameterMap = new HashMap<>();
        parameterMap.put("enableAudio",String.valueOf(0));
        parameterMap.put("hideLoginSuccess","true");
        parameterMap.put("sendBtnText","发送");
        parameterMap.put("color","#3a3a3a");
        FeedbackAPI. setUICustomInfo(parameterMap);

        if (sharedPreferences == null) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activityReference.get());
        }

        settingView.initPushState(sharedPreferences.getBoolean("settings_push", true));
        settingView.setVersionName("当前版本V" + QuApplication.getVersionName());
    }

    @Override
    public void savePushState() {
        boolean pushStatus = sharedPreferences.getBoolean("settings_push", true);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("settings_push", !pushStatus);
        editor.apply();

        if (!pushStatus) {
            MiPushClient.resumePush(activityReference.get(), null);
        } else {
            MiPushClient.pausePush(activityReference.get(), null);
        }

        settingView.initPushState(!pushStatus);
    }

    @Override
    public String openFeedbackActivity() {
        return FeedbackAPI.openFeedbackActivity(activityReference.get());
    }

    @Override
    public void checkUpdate() {
        if (settingView != null) {
            settingView.showProgressLoading();
        }

        if (apiUpdateHelper == null) {
            if (activityReference.get() != null) {
                apiUpdateHelper = new APIUpdateHelper(activityReference.get());
            } else {
                settingView.showToastMessage("检测版本失败");
                return;
            }
        }

        apiUpdateHelper.setOnCheckUpdateListener(this);
        apiUpdateHelper.checkApiUpdate();
    }

    @Override
    public void apiUpdateSuccess(Update update) {
        if (settingView != null) {
            settingView.hideProgressLoading();
            settingView.showUpdateInformation(update);
        }
    }

    @Override
    public void apiUpdateHasBeenUpdated() {
        if (settingView != null) {
            settingView.hideProgressLoading();
            settingView.showToastMessage("已是最新版本!");
        }
    }

    @Override
    public void apiUpdateError() {
        if (settingView != null) {
            settingView.showToastMessage("检测版本失败!");
        }
    }
}
