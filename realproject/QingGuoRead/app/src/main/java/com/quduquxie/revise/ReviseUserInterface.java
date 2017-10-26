package com.quduquxie.revise;

import com.quduquxie.BasePresenter;
import com.quduquxie.BaseView;

/**
 * Created on 16/12/5.
 * Created by crazylei.
 */

public interface ReviseUserInterface {

    interface Presenter extends BasePresenter {

        void initParameter();

        void reviseUserTelephoneNumber(String telephone_number, String verification_code, String password);

        void reviseUserPassword(String ancient_password, String telephone_number, String verification_code, String fresh_password);

        void reviseUserQQ(String qq);

        void exitLogin();
    }

    interface View extends BaseView<Presenter> {

        void initView();

        void showUserFragment();

        void finishActivity();

        void showErrorFragment();

        void showToast(String message);

        void refreshTitleView(String title);

        void showProgressDialog();

        void hideProgressDialog();

        void startLoginActivity();

        void refreshPasswordView();

        void refreshQQView();
    }
}