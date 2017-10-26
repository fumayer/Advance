package com.quduquxie.wxapi;

import com.quduquxie.BasePresenter;
import com.quduquxie.BaseView;

/**
 * Created on 16/11/30.
 * Created by crazylei.
 */

public interface RegisterInterface {

    interface Presenter extends BasePresenter {

        void obtainVerificationCode(String telephone_number);

        boolean verificationInformation(String telephone_number, String verification_code, String password);

        void checkRegisterNextStepState(String key, String value);
    }

    interface View extends BaseView<Presenter> {

        void showToast(String message);

        void checkNextStepState(boolean state);

        void showCountdownView(int second);
    }
}