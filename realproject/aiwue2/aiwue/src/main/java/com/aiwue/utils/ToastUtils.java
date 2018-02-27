package com.aiwue.utils;

import android.widget.Toast;

import com.aiwue.base.AiwueApplication;

public class ToastUtils {

    private static Toast mToast;

    /**
     * 显示Toast
     */
    public static void showToast( CharSequence text) {
        if (mToast == null) {
            mToast = Toast.makeText(AiwueApplication.getInstance(), text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
        }
        mToast.show();
    }


}
