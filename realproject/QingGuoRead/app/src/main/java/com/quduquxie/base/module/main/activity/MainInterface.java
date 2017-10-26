package com.quduquxie.base.module.main.activity;

import com.quduquxie.base.BasePresenter;
import com.quduquxie.base.BaseView;

/**
 * Created on 17/7/17.
 * Created by crazylei.
 */

public interface MainInterface {

    interface Presenter extends BasePresenter {

        void initLiteratureParameter();

        void updateBookshelf();
    }

    interface View extends BaseView {

        void startLoginActivity();

        void startLiteratureCreateActivity();

        void startLiteratureActivity();

        void showLoadingDialog(String message);

        void hideLoadingDialog();

        void finishActivity();
    }
}