package com.quduquxie.application;

import android.content.Context;
import android.content.SharedPreferences;

import com.quduquxie.base.config.BaseConfig;

/**
 * Created on 17/7/13.
 * Created by crazylei.
 */

public class SharedPreferencesUtil {

    private Context context;
    private SharedPreferences sharedPreferences;

    public SharedPreferencesUtil(Context context) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(BaseConfig.FLAG_SHARE_PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public void clear(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(BaseConfig.FLAG_SHARE_PREFERENCE_NAME, Context.MODE_PRIVATE);
        }

        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.clear();
        edit.apply();
    }

    public String loadString(String key, String value) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(BaseConfig.FLAG_SHARE_PREFERENCE_NAME, Context.MODE_PRIVATE);
        }

        return sharedPreferences.getString(key, value);
    }

    public void insertString(String key, String value) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(BaseConfig.FLAG_SHARE_PREFERENCE_NAME, Context.MODE_PRIVATE);
        }

        sharedPreferences.edit().putString(key, value).apply();
    }

    public long loadLong(String key, long value) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(BaseConfig.FLAG_SHARE_PREFERENCE_NAME, Context.MODE_PRIVATE);
        }

        return sharedPreferences.getLong(key, value);
    }

    public void insertLong(String key, long value) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(BaseConfig.FLAG_SHARE_PREFERENCE_NAME, Context.MODE_PRIVATE);
        }

        sharedPreferences.edit().putLong(key, value).apply();
    }

    public int loadInteger(String key, int value) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(BaseConfig.FLAG_SHARE_PREFERENCE_NAME, Context.MODE_PRIVATE);
        }

        return sharedPreferences.getInt(key, value);
    }

    public void insertInteger(String key, int value) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(BaseConfig.FLAG_SHARE_PREFERENCE_NAME, Context.MODE_PRIVATE);
        }

        sharedPreferences.edit().putInt(key, value).apply();
    }

    public boolean loadBoolean(String key, boolean value) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(BaseConfig.FLAG_SHARE_PREFERENCE_NAME, Context.MODE_PRIVATE);
        }

        return sharedPreferences.getBoolean(key, value);
    }

    public void insertBoolean(String key, boolean value) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(BaseConfig.FLAG_SHARE_PREFERENCE_NAME, Context.MODE_PRIVATE);
        }

        sharedPreferences.edit().putBoolean(key, value).apply();
    }

    public float loadFloat(String key, float value) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(BaseConfig.FLAG_SHARE_PREFERENCE_NAME, Context.MODE_PRIVATE);
        }

        return sharedPreferences.getFloat(key, value);
    }

    public void insertFloat(String key, float value) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(BaseConfig.FLAG_SHARE_PREFERENCE_NAME, Context.MODE_PRIVATE);
        }

        sharedPreferences.edit().putFloat(key, value).apply();
    }

    public void remove(String... keys) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(BaseConfig.FLAG_SHARE_PREFERENCE_NAME, Context.MODE_PRIVATE);
        }

        if (keys != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            for (String key : keys) {
                editor.remove(key);
            }
            editor.apply();
        }
    }
}