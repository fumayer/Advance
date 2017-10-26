package com.quduquxie.revise.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;
import com.quduquxie.model.Verification;
import com.quduquxie.retrofit.DataRequestService;
import com.quduquxie.retrofit.ServiceGenerator;
import com.quduquxie.retrofit.model.CommunalResult;
import com.quduquxie.revise.ModifyBindingInterface;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created on 16/12/5.
 * Created by crazylei.
 */

public class ModifyBindingPresenter implements ModifyBindingInterface.Presenter {

    private ModifyBindingInterface.View modifyBindingView;
    private WeakReference<Context> contextReference;

    private Map<String, String> parameter = new HashMap<>();

    public ModifyBindingPresenter(@NonNull ModifyBindingInterface.View modifyBindingView, Context context) {
        this.modifyBindingView = modifyBindingView;
        this.contextReference = new WeakReference<>(context);
    }

    @Override
    public void init() {

    }

    @Override
    public void obtainVerificationCode(String telephone_number) {
        if (telephone_number.length() != 11 || !telephone_number.startsWith("1")) {
            modifyBindingView.showToast("手机号有误");
        } else {
            requestVerificationCode(telephone_number);
        }
    }

    @Override
    public boolean verificationInformation(String telephone_number, String verification_code, String password) {
        StringBuilder stringBuilder = new StringBuilder();
        if (telephone_number.length() != 11 || !telephone_number.startsWith("1")) {
            stringBuilder.append("手机号有误");
        }

        if (TextUtils.isEmpty(verification_code)) {
            if (stringBuilder.length() != 0) {
                stringBuilder.append("\n");
            }
            stringBuilder.append("请输入验证码！");
        }

        if (password.length() < 6 || password.length() > 18) {
            if (stringBuilder.length() != 0) {
                stringBuilder.append("\n");
            }
            stringBuilder.append("输入6-18位密码");
        }

        if (stringBuilder.length() > 0) {
            modifyBindingView.showToast(stringBuilder.toString());
            return false;
        }
        return true;
    }

    @Override
    public void checkModifyCompleteState(String key, String value) {
        parameter.put(key, value);

        if (parameter.containsKey("telephone_number") && !TextUtils.isEmpty(parameter.get("telephone_number")) && parameter.containsKey("verification_code") && !TextUtils.isEmpty(parameter.get("verification_code")) && parameter.containsKey("password") && !TextUtils.isEmpty(parameter.get("password"))) {
            modifyBindingView.checkCompleteState(true);
        } else {
            modifyBindingView.checkCompleteState(false);
        }
    }

    private void requestVerificationCode(String telephone_number) {

        DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, false);
        dataRequestService.loadVerificationCode(telephone_number, "1")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResourceSubscriber<CommunalResult<Verification>>() {
                    @Override
                    public void onNext(CommunalResult<Verification> verificationResult) {
                        Logger.d("LoadVerificationCode onNext");
                        if (verificationResult != null) {
                            if (verificationResult.getCode() == 0 && verificationResult.getModel() != null) {
                                modifyBindingView.showToast("验证码已发送！");
                                showCountdownView(verificationResult.getModel().expire_sec);
                            } else if (!TextUtils.isEmpty(verificationResult.getMessage())) {
                                modifyBindingView.showToast(verificationResult.getMessage());
                            }
                        } else {
                            modifyBindingView.showToast("请检查网络");
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Logger.d("LoadVerificationCode onError: " + throwable.toString());
                        modifyBindingView.showToast("请检查网络");
                    }

                    @Override
                    public void onComplete() {
                        Logger.d("LoadVerificationCode onComplete");
                    }
                });
    }

    private void showCountdownView(int second) {
        modifyBindingView.showCountdownView(second);
    }
}
