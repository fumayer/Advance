package com.quduquxie.revise.listener;

/**
 * Created on 16/12/5.
 * Created by crazylei.
 */

public interface ReviseUserListener {

    void showModifyUserFragment();

    void showModifyNumberFragment();

    void showModifyBindingFragment();

    void showModifyPasswordFragment();

    void showModifyQQFragment();

    void modifyTelephoneNumber(String telephone_number, String verification_code, String password);

    void modifyPassword(String ancient_password, String telephone_number, String verification_code, String fresh_password);

    void modifyQQ(String qq);

    void exitLogin();
}
