package com.quduquxie.revise;

import com.quduquxie.BasePresenter;
import com.quduquxie.BaseView;

/**
 * Created on 16/12/5.
 * Created by crazylei.
 */

public interface ModifyNumberInterface {

    interface Presenter extends BasePresenter {

        void obtainVerificationCode(String telephone_number);

        boolean verificationInformation(String telephone_number, String verification_code, String password);

        void checkModifyCompleteState(String key, String value);
    }

    interface View extends BaseView<Presenter> {

        void showToast(String message);

        void checkCompleteState(boolean state);

        void showCountdownView(int second);

        void startLoginActivity();

    }
}