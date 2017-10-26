package com.quduquxie.wxapi.listener;

import java.io.File;

/**
 * Created on 16/12/6.
 * Created by crazylei.
 */

public interface LandingListener {

    void landing(String telephone_number, String password);

    void showRegisterFragment();

    void showRecoveredFragment();

    void landingWidthWeChat();

    void landingWidthQQ();

    void registerUserInformation(String telephone_number, String verification_code, String password);

    void completeUserInformation(String nickname, File avatar);

    void recoveredUserPassword(String telephone_number, String verification_code, String password);
}
