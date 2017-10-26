package com.quduquxie.util;

import android.content.SharedPreferences;

public class SharedPreferencesUtils {
	private SharedPreferences sp;

	public SharedPreferencesUtils(SharedPreferences sp) {
		this.sp = sp;
	}

	public void putBoolean(String key, boolean value) {
		sp.edit().putBoolean(key, value).apply();
	}

	public void putFloat(String key, float value) {
		sp.edit().putFloat(key, value).apply();
	}

	public void putInt(String key, int value) {
		sp.edit().putInt(key, value).apply();
	}

	public void putLong(String key, long value) {
		sp.edit().putLong(key, value).apply();
	}

	public void putString(String key, String value) {
		sp.edit().putString(key, value).apply();
	}

	public boolean getBoolean(String key) {
		return sp.getBoolean(key, false);
	}

	public boolean getBoolean(String key,boolean default_boolean) {
		return sp.getBoolean(key, default_boolean);
	}

	public float getFloat(String key) {
		return sp.getFloat(key, 0);
	}

	public int getInt(String key) {
		return sp.getInt(key, 0);
	}

	public long getLong(String key) {
		return sp.getLong(key, 0);
	}

	public String getString(String key) {
		return sp.getString(key, "");
	}

	public void removeValue(String key) {
		sp.edit().remove(key).apply();
	}
}

