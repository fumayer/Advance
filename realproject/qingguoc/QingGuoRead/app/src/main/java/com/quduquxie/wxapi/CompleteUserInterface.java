package com.quduquxie.wxapi;

import com.quduquxie.BasePresenter;
import com.quduquxie.BaseView;

import java.io.File;

/**
 * Created on 16/11/30.
 * Created by crazylei.
 */

public interface CompleteUserInterface {

    interface Presenter extends BasePresenter {

        boolean verificationInformation(String nickname, File file);

    }

    interface View extends BaseView<Presenter> {

        void showToast(String message);
    }
}
