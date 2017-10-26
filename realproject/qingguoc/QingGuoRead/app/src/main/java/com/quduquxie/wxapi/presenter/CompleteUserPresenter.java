package com.quduquxie.wxapi.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.quduquxie.wxapi.CompleteUserInterface;

import java.io.File;
import java.lang.ref.WeakReference;

/**
 * Created on 16/11/30.
 * Created by crazylei.
 */

public class CompleteUserPresenter implements CompleteUserInterface.Presenter {

    private static final String TAG = CompleteUserPresenter.class.getSimpleName();

    private CompleteUserInterface.View completeUserView;
    private WeakReference<Context> contextReference;

    public CompleteUserPresenter(@NonNull CompleteUserInterface.View completeUserView, Context context) {
        this.completeUserView = completeUserView;
        this.contextReference = new WeakReference<>(context);
    }

    @Override
    public void init() {

    }

    @Override
    public boolean verificationInformation(String nickname, File file) {
        if (TextUtils.isEmpty(nickname)) {
            completeUserView.showToast("请输入用户名");
            return false;
        }
        return true;
    }
}
