package com.quduquxie.wxapi;

import com.quduquxie.BasePresenter;
import com.quduquxie.BaseView;

/**
 * Created on 16/12/6.
 * Created by crazylei.
 */

public interface LoginInterface {

    interface Presenter extends BasePresenter {

        boolean verificationInformation(String telephone_number, String password);

        void checkLandingState(String key, String value);

    }

    interface View extends BaseView<Presenter> {

        void showToast(String message);

        void changeLoginViewState(boolean state);

    }
}
