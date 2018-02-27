package com.aiwue.utils;

import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by liaixiong on 16-8-25.
 */
public class SharedPrefUtil {

    private static boolean DEBUG = false;
    public static void applyToEditor(SharedPreferences.Editor editor) {
        if (DEBUG)
            Log.d("show", "SDK_INT  = " + android.os.Build.VERSION.SDK_INT);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.GINGERBREAD) {
            editor.apply();
        } else {
            editor.commit();
        }
    }

    public static void commitToEditor(SharedPreferences.Editor editor) {
        if (DEBUG)
            Log.d("show", "SDK_INT  = " + android.os.Build.VERSION.SDK_INT);

        editor.commit();
    }

}
