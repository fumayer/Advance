package com.quduquxie.base;

/**
 * Created on 17/7/12.
 * Created by crazylei.
 */

public interface BaseView {

    void initializeParameter();

    void showPromptMessage(String message);

    void changeNightMode();

    void recycle();
}