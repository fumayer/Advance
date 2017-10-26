package com.quduquxie.communal.listener;

import android.view.View;

import java.util.Calendar;

/**
 * Created on 17/3/13.
 * Created by crazylei.
 */

public abstract class NoRepeatClickListener implements View.OnClickListener {

    private static final int MIN_CLICK_DELAY_TIME = 3000;
    private long lastClickTime = 0;

    @Override
    public void onClick(View view) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            onViewClicked(view);
        }
    }

    public abstract void onViewClicked(View view);
}