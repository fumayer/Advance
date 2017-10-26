package com.quduquxie.util;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.quduquxie.application.QuApplication;


public final class PreferenceUtils {

	/** 清空数据 */
	public static void reset(final Context context) {
		Editor edit = PreferenceManager.getDefaultSharedPreferences(context).edit();
		edit.clear();
		edit.apply();
	}

	public static String getString(String key, String defValue) {
		return PreferenceManager.getDefaultSharedPreferences(QuApplication.getInstance()).getString(key, defValue);
	}

	public static long getLong(String key, long defValue) {
		return PreferenceManager.getDefaultSharedPreferences(QuApplication.getInstance()).getLong(key, defValue);
	}

	public static float getFloat(String key, float defValue) {
		return PreferenceManager.getDefaultSharedPreferences(QuApplication.getInstance()).getFloat(key, defValue);
	}

	public static void put(String key, String value) {
		putString(key, value);
	}

	public static void put(String key, int value) {
		putInt(key, value);
	}

	public static void put(String key, float value) {
		putFloat(key, value);
	}

	public static void put(String key, boolean value) {
		putBoolean(key, value);
	}

	public static void putFloat(String key, float value) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(QuApplication.getInstance());
		Editor editor = sharedPreferences.edit();
		editor.putFloat(key, value);
		editor.apply();
	}

	public static SharedPreferences getPreferences() {
		return PreferenceManager.getDefaultSharedPreferences(QuApplication.getInstance());
	}

	public static int getInt(String key, int defValue) {
		return PreferenceManager.getDefaultSharedPreferences(QuApplication.getInstance()).getInt(key, defValue);
	}

	public static boolean getBoolean(String key, boolean defValue) {
		return PreferenceManager.getDefaultSharedPreferences(QuApplication.getInstance()).getBoolean(key, defValue);
	}

	public static void putStringProcess(String key, String value) {
		SharedPreferences sharedPreferences = QuApplication.getInstance().getSharedPreferences("preference_mu", Context.MODE_MULTI_PROCESS);
		Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.apply();
	}

	public static String getStringProcess(String key, String defValue) {
		SharedPreferences sharedPreferences = QuApplication.getInstance().getSharedPreferences("preference_mu", Context.MODE_MULTI_PROCESS);
		return sharedPreferences.getString(key, defValue);
	}

	public static boolean hasString(String key) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(QuApplication.getInstance());
		return sharedPreferences.contains(key);
	}

	public static void putString(String key, String value) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(QuApplication.getInstance());
		Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.apply();
	}

	public static void putLong(String key, long value) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(QuApplication.getInstance());
		Editor editor = sharedPreferences.edit();
		editor.putLong(key, value);
		editor.apply();
	}

	public static void putBoolean(String key, boolean value) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(QuApplication.getInstance());
		Editor editor = sharedPreferences.edit();
		editor.putBoolean(key, value);
		editor.apply();
	}

	public static void putInt(String key, int value) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(QuApplication.getInstance());
		Editor editor = sharedPreferences.edit();
		editor.putInt(key, value);
		editor.apply();
	}

	public static void remove(String... keys) {
		if (keys != null) {
			SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(QuApplication.getInstance());
			Editor editor = sharedPreferences.edit();
			for (String key : keys) {
				editor.remove(key);
			}
			editor.apply();
		}
	}
}
