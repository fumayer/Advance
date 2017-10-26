package com.quduquxie.manager;

import android.app.Activity;
import com.dingyueads.sdk.NativeInit;
import com.quduquxie.util.QGLog;

/**
 * 自有广告平台
 */
public class OwnNativeAdManager {
	private static final String TAG = "OwnNativeAdManager";
	private static OwnNativeAdManager mInstance;
	private NativeInit nativeInit;


	public OwnNativeAdManager(Activity activity) {
		nativeInit = new NativeInit(activity);
	}

	public static synchronized OwnNativeAdManager getInstance(Activity mActivity) {
		if (mInstance == null) {
			mInstance = new OwnNativeAdManager(mActivity);
			QGLog.e(TAG, "OwnNativeAdManager mInstance new instance");
		}else {
			QGLog.e(TAG,"OwnNativeAdManager mInstance already exist");
		}
		return mInstance;
	}
}
