package com.quduquxie.revise;

import com.quduquxie.BasePresenter;
import com.quduquxie.BaseView;

import java.io.File;

/**
 * Created on 16/12/5.
 * Created by crazylei.
 */

public interface ModifyUserInterface {

    interface Presenter extends BasePresenter {

        void changePenName(String fresh_name, boolean finish);

        void changeAvatar(File file);
    }

    interface View extends BaseView<Presenter> {

        void showAvatarImage(String url);

        void showPenName(String pen_name);

        void showBindingNumber(String telephone_number);

        void showQQNumber(String qq);

        void showToast(String message);

        void showProgressDialog();

        void hideProgressDialog();

        void startLoginActivity();

        void showLoadingPage();

        void hideLoadingPage();

        void showLoadingError();

        void destroyActivity();
    }
}