package com.quduquxie.module.setting;

import com.quduquxie.BasePresenter;
import com.quduquxie.BaseView;
import com.quduquxie.base.bean.Update;

/**
 * Created on 17/2/15.
 * Created by crazylei.
 */

public interface SettingInterface {

    interface Presenter extends BasePresenter {

        void initParameter();

        void savePushState();

        String openFeedbackActivity();

        void checkUpdate();

    }

    interface View extends BaseView<Presenter> {

        void initView();

        void initPushState(boolean check);

        void setVersionName(String version);

        void showProgressLoading();

        void hideProgressLoading();

        void showToastMessage(String message);

        void showUpdateInformation(Update update);
    }
}
