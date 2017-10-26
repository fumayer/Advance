package com.quduquxie.base.util;

import android.content.Context;
import android.graphics.Typeface;

import java.util.Hashtable;

/**
 * Created on 17/7/12.
 * Created by crazylei.
 */

public class TypefaceUtil {

    public static final int TYPEFACE_SONG = 0;
    public static final int TYPEFACE_SYSTEM = 1;
    public static final int TYPEFACE_SONG_DEPICT = 2;

    private static final Hashtable<String, Typeface> typefaceTable = new Hashtable<>();

    public static Typeface loadTypeface(Context context, int typeface) {

        if (context == null) {
            return null;
        }

        switch (typeface) {
            case TYPEFACE_SONG:
                return initializeTypeface(context, "fss.ttf");
            case TYPEFACE_SYSTEM:
                return Typeface.DEFAULT;
            case TYPEFACE_SONG_DEPICT:
                return initializeTypeface(context, "fzqksjt.ttf");
            default:
                return Typeface.DEFAULT;
        }
    }

    private static Typeface initializeTypeface(Context context, String name) {
        if (!typefaceTable.containsKey(name)) {

            Typeface typeface;

            try {
                typeface = Typeface.createFromAsset(context.getApplicationContext().getAssets(), "fonts/" + name);
            } catch (RuntimeException runtimeException) {
                runtimeException.printStackTrace();
                return Typeface.DEFAULT;
            }

            if (typeface != null) {
                typefaceTable.put(name, typeface);
            }
        }
        return typefaceTable.get(name);
    }
}