package com.quduquxie.communal.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.quduquxie.application.QuApplication;

/**
 * Created on 17/2/16.
 * Created by crazylei.
 */

public class GlobalUtil {

    private static SharedPreferences sharedPreferences;


    public static int getKeyboardHeight() {
        if (sharedPreferences == null) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(QuApplication.getInstance());
        }
        return sharedPreferences.getInt("KeyboardHeight", 787);
    }

    public static void saveKeyboardHeight(int keyboardHeight) {
        if (sharedPreferences == null) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(QuApplication.getInstance());
        }
        sharedPreferences.edit().putInt("KeyboardHeight", keyboardHeight).apply();
    }
}
