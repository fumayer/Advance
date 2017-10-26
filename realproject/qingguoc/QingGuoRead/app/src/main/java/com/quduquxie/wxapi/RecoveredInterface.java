package com.quduquxie.wxapi;

import com.quduquxie.BasePresenter;
import com.quduquxie.BaseView;

/**
 * Created on 16/11/30.
 * Created by crazylei.
 */

public interface RecoveredInterface {

    interface Presenter extends BasePresenter {

        void obtainVerificationCode(String telephone_number);

        boolean verificationInformation(String telephone_number, String verification_code, String password);

        void checkRecoveredNextStepState(String key, String value);

    }

    interface View extends BaseView<Presenter> {

        void showToast(String message);

        void checkCompleteState(boolean state);

        void showCountdownView(int second);

        void startLoginActivity();
    }
}
