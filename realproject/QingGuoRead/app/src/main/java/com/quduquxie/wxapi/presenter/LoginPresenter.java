package com.quduquxie.wxapi.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.quduquxie.util.QGLog;
import com.quduquxie.wxapi.LoginInterface;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * Created on 16/12/6.
 * Created by crazylei.
 */

public class LoginPresenter implements LoginInterface.Presenter {

    private static final String TAG = LoginPresenter.class.getSimpleName();

    private LoginInterface.View loginView;
    private WeakReference<Context> contextReference;

    private Map<String, String> parameter = new HashMap<>();

    public LoginPresenter(@NonNull LoginInterface.View loginView, Context context) {
        this.loginView = loginView;
        this.contextReference = new WeakReference<>(context);
    }


    @Override
    public void init() {

    }

    @Override
    public boolean verificationInformation(String telephone_number, String password) {
        StringBuilder stringBuilder = new StringBuilder();
        if (telephone_number.length() != 11 || !telephone_number.startsWith("1")) {
            stringBuilder.append("手机号有误");
        }

        if (password.length() < 6 || password.length() > 18) {
            if (stringBuilder.length() != 0) {
                stringBuilder.append("\n");
            }
            stringBuilder.append("输入6-18位密码");
        }

        if (stringBuilder.length() > 0) {
            loginView.showToast(stringBuilder.toString());
            return false;
        }
        return true;
    }

    @Override
    public void checkLandingState(String key, String value) {
        parameter.put(key, value);
        if (parameter.containsKey("telephone_number") && !TextUtils.isEmpty(parameter.get("telephone_number")) && parameter.containsKey("password") && !TextUtils.isEmpty(parameter.get("password"))) {
            QGLog.e(TAG, "checkLandingState: true");
            loginView.changeLoginViewState(true);
        } else {
            QGLog.e(TAG, "checkLandingState: false");
            loginView.changeLoginViewState(false);
        }
    }
}
