package com.quduquxie.util;

import com.quduquxie.application.QuApplication;

import android.widget.Toast;

public class ToastUtils {
	private static Toast mToast;
	/**
	 * 解决Toast重复显示的问题
	 * */
	public static void showToastNoRepeat(String text) {
		if (mToast == null) {
			mToast = Toast.makeText(QuApplication.getInstance(), text, Toast.LENGTH_SHORT);
		} else {
			mToast.setText(text);
			mToast.setDuration(Toast.LENGTH_SHORT);
		}
		mToast.show();
	}
	public static void showToastNoRepeat(int source) {
		if (mToast == null) {
			mToast = Toast.makeText(QuApplication.getInstance(), source, Toast.LENGTH_SHORT);
		} else {
			mToast.setText(source);
			mToast.setDuration(Toast.LENGTH_SHORT);
		}
		mToast.show();
	}
}
