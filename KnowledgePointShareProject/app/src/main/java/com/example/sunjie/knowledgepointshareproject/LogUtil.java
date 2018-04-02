package com.example.sunjie.knowledgepointshareproject;

import android.util.Log;

public class LogUtil {
	public static boolean enabled = true;

	public static void i(String tag, String msg) {
		if (enabled) {
			if (msg != null) {
				Log.i(tag, msg);
			}

		}
	}

	public static void d(String tag, String msg) {
		if (enabled) {
			if (msg != null) {
				Log.d(tag, msg);
			}

		}
	}

	public static void e(String tag, String msg) {
		if (enabled) {
			if (msg != null) {
				Log.e(tag, msg);
			}

		}
	}

	public static void v(String tag, String msg) {
		if (enabled) {
			if (msg != null) {
				Log.v(tag, msg);
			}

		}
	}

	public static void w(String tag, String msg) {
		if (enabled) {
			if (msg != null) {
				Log.w(tag, msg);
			}

		}
	}
}
