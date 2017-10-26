package com.quduquxie.wxapi;

import com.quduquxie.BasePresenter;
import com.quduquxie.BaseView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * Created on 16/12/6.
 * Created by crazylei.
 */

public interface WXEntryInterface {

    interface Presenter extends BasePresenter {

        void initQQAccessToken(JSONObject jsonObject) throws JSONException;

        void updateUserInformationQQ(JSONObject jsonObject) throws JSONException;

        void registerUserInformationWeChat(String code);

        void registerUserInformation(String telephone_number, String verification_code, String password);

        void landingUserInformation(String telephone_number, String password);

        void recoveredUserPassword(String telephone_number, String verification_code, String password);

        void completeUserInformation(String nickname, File avatar);
    }

    interface View extends BaseView<Presenter> {

        void initView();

        void refreshViewTitle(String title);

        void showLoginFragment();

        void showCompleteInformationFragment();

        void finishActivity();

        void finishActivityLiteratureCheck();

        void setQQAccessToken(String access_token, String openid, String expires_in);

        void updateUserInformationQQ();

        void showToast(String message);

        void showCompleteMessage(String message);

        void showProgressDialog();

        void hideProgressDialog();

        void startLoginActivity();
    }
}
