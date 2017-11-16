package com.shortmeet.www.utilsUsed;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by \(^o^)/~ on 2016/7/11.
 */
public class PreferenceUtil {
    private static SharedPreferences mSharedPreferences;

    private static synchronized SharedPreferences getPreferneces(Context context) {
        if (mSharedPreferences == null) {
            mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        }
        return mSharedPreferences;
    }

    /**
     * 打印所有
     */
    public static void print(Context context) {
        System.out.println(getPreferneces(context).getAll());
    }

    /**
     * 清空保存在默认SharePreference下的所有数据
     */
    public static void clear(Context context) {
        getPreferneces(context).edit().clear().apply();
    }

    /**
     * 保存字符串
     *
     * @return
     */
    public static void putString(Context context, String key, String value) {
        getPreferneces(context).edit().putString(key, value).apply();
    }

    /**
     * 读取字符串
     *
     * @param key
     * @return
     */
    public static String getString(Context context, String key) {
        return getPreferneces(context).getString(key, "");

    }

    /**
     * 保存整型值
     *
     * @return
     */
    public static void putInt(Context context, String key, int value) {
        getPreferneces(context).edit().putInt(key, value).apply();
    }

    /**
     * 读取整型值
     *
     * @param key
     * @return
     */
    public static int getInt(Context context, String key) {
        return getPreferneces(context).getInt(key, 0);
    }

    /**
     * 保存布尔值
     *
     * @return
     */
    public static void putBoolean(Context context, String key, Boolean value) {
        getPreferneces(context).edit().putBoolean(key, value).apply();
    }

    /**
     * 保存long数据
     *
     * @param context
     * @param key
     * @param value
     */
    public static void putLong(Context context, String key, long value) {
        getPreferneces(context).edit().putLong(key, value).apply();
    }

    /**
     * 读取Long数据
     *
     * @param context
     * @param key
     * @return
     */
    public static long getLong(Context context, String key) {
        return getPreferneces(context).getLong(key, 0);
    }

    /**
     * t 读取布尔值
     *
     * @param key
     * @return
     */
    public static boolean getBoolean(Context context, String key, boolean defValue) {
        return getPreferneces(context).getBoolean(key, defValue);

    }

    /**
     * 移除字段
     *
     * @return
     */
    public static void removeString(Context context, String key) {
        getPreferneces(context).edit().remove(key).apply();
    }


}
