package com.quduquxie.base.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.text.TextUtils;

import com.quduquxie.Constants;
import com.quduquxie.base.config.BaseConfig;

/**
 * Created on 17/7/26.
 * Created by crazylei.
 */

public class ResourceUtil {

    public static String mode = "";

    public static int loadResourceID(Context context, int type, String name) {

        Resources resources = context.getResources();

        if (TextUtils.isEmpty(mode)) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(BaseConfig.FLAG_SHARE_PREFERENCE_NAME, Context.MODE_PRIVATE);
            mode = sharedPreferences.getString(BaseConfig.FLAG_READING_NIGHT_MODE, "light");
        }

        switch (type) {
            case Constants.DRAWABLE:
                return resources.getIdentifier(mode + name, "drawable", context.getPackageName());
            case Constants.COLOR:
                return resources.getIdentifier(mode + name, "color", context.getPackageName());
            case Constants.STYLE:
                return resources.getIdentifier(mode + name, "style", context.getPackageName());
            default:
                return 0;
        }
    }
}