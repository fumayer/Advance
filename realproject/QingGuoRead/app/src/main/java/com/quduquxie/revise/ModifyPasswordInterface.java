package com.quduquxie.revise;

import com.quduquxie.BasePresenter;
import com.quduquxie.BaseView;

/**
 * Created on 16/12/5.
 * Created by crazylei.
 */

public interface ModifyPasswordInterface {

    interface Presenter extends BasePresenter {

        void obtainVerificationCode(String telephone_number);

        boolean verificationInformation(String ancient_password, String telephone_number, String verification_code, String fresh_password);

        void checkModifyCompleteState(String key, String value);
    }

    interface View extends BaseView<Presenter> {

        void showToast(String message);

        void checkCompleteState(boolean state);

        void showCountdownView(int second);

        void startLoginActivity();

    }
}