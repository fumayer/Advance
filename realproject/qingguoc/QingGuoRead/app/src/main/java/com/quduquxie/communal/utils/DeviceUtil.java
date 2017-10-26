package com.quduquxie.communal.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;
import com.quduquxie.Constants;
import com.quduquxie.application.QuApplication;
import com.quduquxie.db.UserDao;
import com.quduquxie.model.Alias;
import com.quduquxie.model.User;
import com.quduquxie.retrofit.DataRequestService;
import com.quduquxie.retrofit.ServiceGenerator;
import com.quduquxie.retrofit.model.CommunalResult;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created on 16/12/28.
 * Created by crazylei.
 */

public class DeviceUtil {

    public static float density = 1;

    static {
        density = QuApplication.getInstance().getResources().getDisplayMetrics().density;
    }

    public static int dp(float value) {
        if (value == 0) {
            return 0;
        }
        return (int) Math.ceil(density * value);
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int dip2px(int dipValue) {
        float reSize = QuApplication.getInstance().getResources().getDisplayMetrics().density;
        return (int) ((dipValue * reSize) + 0.5);
    }

    public static int dip2px(double dipValue) {
        float reSize = QuApplication.getInstance().getResources().getDisplayMetrics().density;
        return (int) ((dipValue * reSize));
    }

    public static String getMacAddress() {
        String address = null;
        WifiManager wifiManager = (WifiManager) QuApplication.getInstance().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null && wifiManager.getConnectionInfo() != null) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            if (wifiInfo != null && !TextUtils.isEmpty(wifiInfo.getMacAddress())) {
                address = wifiInfo.getMacAddress();
            }
        }
        return address;
    }

    public static void pushDevice(String register) {
        Map<String, String> information = new HashMap<>();
        if (!TextUtils.isEmpty(register)) {
            information.put("regId", register);
        } else {
            return;
        }
        String address = getMacAddress();
        if (!TextUtils.isEmpty(address)) {
            information.put("mac", address);
        }

        UserDao userDao = UserDao.getInstance(QuApplication.getInstance());
        User user = userDao.getUser();
        if (user != null && !TextUtils.isEmpty(user.token)) {
            information.put("token", user.token);
        }
        pushRegisterDevice(information);
    }

    private static void pushActiveDevice(Map<String, String> information) {
        DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, false);
        dataRequestService.pushActiveDevice(information)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .subscribe(new ResourceSubscriber<CommunalResult>() {

                    @Override
                    public void onNext(CommunalResult communalResult) {
                        Logger.d("PushActiveDevice onNext: " + communalResult.getCode());
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Logger.d("PushActiveDevice onError: " + throwable.toString());
                    }

                    @Override
                    public void onComplete() {
                        Logger.d("PushActiveDevice onComplete");
                    }
                });
    }

    private static void pushRegisterDevice(final Map<String, String> information) {
        DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, false);
        dataRequestService.pushRegisterDevice(information)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .subscribe(new ResourceSubscriber<CommunalResult<Alias>>() {

                    @Override
                    public void onNext(CommunalResult<Alias> aliasResult) {
                        if (aliasResult != null && aliasResult.getCode() == 0 && aliasResult.getModel() != null) {
                            Logger.d("PushRegisterDevice onNext: " + aliasResult.getCode());
                            String alias = aliasResult.getModel().alias;
                            if (!TextUtils.isEmpty(alias)) {
                                Logger.d("PushRegisterDevice onNext setAlias: " + alias);
                                MiPushClient.setAlias(QuApplication.getInstance(), alias, null);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Logger.d("PushRegisterDevice onError: " + throwable.toString());
                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(QuApplication.getInstance());
                        sharedPreferences.edit().putBoolean(Constants.PUSH_DEVICE_REGISTER_FLAG + QuApplication.getVersionCode(), false).apply();
                    }

                    @Override
                    public void onComplete() {
                        Logger.d("PushRegisterDevice onComplete");
                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(QuApplication.getInstance());
                        sharedPreferences.edit().putBoolean(Constants.PUSH_DEVICE_REGISTER_FLAG + QuApplication.getVersionCode(), true).apply();
                    }
                });
    }

    public static void pushBindingDevice(String register, String token) {
        Map<String, String> information = new HashMap<>();
        if (!TextUtils.isEmpty(register)) {
            information.put("regId", register);
        } else {
            return;
        }
        String address = getMacAddress();
        if (!TextUtils.isEmpty(address)) {
            information.put("mac", address);
        }

        if (!TextUtils.isEmpty(token)) {
            information.put("token", token);
        }

        Logger.d("pushBindingDevice: " + information.toString());
        DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, false);
        dataRequestService.pushBindingDevice(information)
                .subscribeOn(Schedulers.trampoline())
                .observeOn(Schedulers.trampoline())
                .subscribe(new ResourceSubscriber<CommunalResult<Alias>>() {

                    @Override
                    public void onNext(CommunalResult<Alias> aliasResult) {
                        if (aliasResult != null && aliasResult.getCode() == 0 && aliasResult.getModel() != null) {
                            Logger.d("PushBindingDevice onNext: " + aliasResult.getCode());
                            String alias = aliasResult.getModel().alias;
                            if (!TextUtils.isEmpty(alias)) {
                                Logger.d("PushBindingDevice onNext setAlias: " + alias);
                                MiPushClient.setAlias(QuApplication.getInstance(), alias, null);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Logger.d("PushBindingDevice onError: " + throwable.toString());
                    }

                    @Override
                    public void onComplete() {
                        Logger.d("PushBindingDevice onComplete");
                    }
                });
    }


    public static void runOnUIThread(Runnable runnable) {
        runOnUIThread(runnable, 0);
    }

    public static void runOnUIThread(Runnable runnable, long delay) {
        if (delay == 0) {
            QuApplication.applicationHandler.post(runnable);
        } else {
            QuApplication.applicationHandler.postDelayed(runnable, delay);
        }
    }
}
