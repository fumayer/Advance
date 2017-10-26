package com.quduquxie.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.orhanobut.logger.Logger;
import com.quduquxie.base.util.NetworkUtil;
import com.quduquxie.service.check.CheckNovelUpdateService;
import com.quduquxie.util.QGLog;


public class ConnectionChangeReceiver extends BroadcastReceiver {

	private static final String TAG = ConnectionChangeReceiver.class.getSimpleName();
	private static boolean canReload = true;
	private static boolean canShowAlert = true;

	@Override
	public void onReceive(Context context, Intent intent) {
		Logger.d("ConnectivityReceiver.onReceive()...");
		if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
//			DownloadService downloadService = QuApplication.getDownloadService();
			
			ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

			NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

			if (networkInfo != null) {
				int nType = networkInfo.getType();
				if (nType == ConnectivityManager.TYPE_MOBILE) {
					NetworkUtil.NETWORK_TYPE = NetworkUtil.NETWORK_MOBILE;
					
					mobileType = getMobileType(networkInfo.getSubtype());
					isCanAllowNet(mobileType);
//					if (downloadService != null && canShowAlert) {
//						canShowAlert = false;
//						downloadService.showAlertDialog();
//					}
				} else if (nType == ConnectivityManager.TYPE_WIFI) {
					NetworkUtil.NETWORK_TYPE = NetworkUtil.NETWORK_WIFI;
					
					isCanAllowNet(WIFI);
					mobileType = WIFI;
					canShowAlert = true;
				} else {
					NetworkUtil.NETWORK_TYPE = NetworkUtil.NETWORK_NONE;
					
					isCanAllowNet(G0);
					mobileType = G0;
					canShowAlert = false;
				}

				QGLog.d(TAG, "Network Type  = " + networkInfo.getTypeName());
				QGLog.d(TAG, "Network State = " + networkInfo.getState());
				if (networkInfo.isConnected() && canReload) {
					canReload = false;
					QGLog.e(TAG, "Network connected");
					Intent intent_service = new Intent();	
					intent_service.setClass(context, CheckNovelUpdateService.class);
					intent_service.setAction(CheckNovelUpdateService.ACTION_CHECK_NOVEL_UPDATE);
					context.startService(intent_service);
				}
			} else {
				canReload = true;
				QGLog.e(TAG, "Network unavailable");
				NetworkUtil.NETWORK_TYPE = NetworkUtil.NETWORK_NONE;
				
				isCanAllowNet(G0);
				mobileType = G0;
				canShowAlert = false;
			}
		}
	}
	
	
	private void isCanAllowNet(int netType) {

	}

	private static final int G0 = 0;
	private static final int G2 = 2;
	private static final int G3 = 3;
	private static final int G4 = 4;
	private static final int WIFI = 5;
	public static int mobileType;

	private int getMobileType(int subType) {
		switch (subType) {
		case TelephonyManager.NETWORK_TYPE_1xRTT:
			return G2; // ~ 50-100 kbps
		case TelephonyManager.NETWORK_TYPE_CDMA:
			return G2; // ~ 14-64 kbps
		case TelephonyManager.NETWORK_TYPE_EDGE:
			return G2; // ~ 50-100 kbps
		case TelephonyManager.NETWORK_TYPE_EVDO_0:
			return G3; // ~ 400-1000 kbps
		case TelephonyManager.NETWORK_TYPE_EVDO_A:
			return G3; // ~ 600-1400 kbps
		case TelephonyManager.NETWORK_TYPE_GPRS:
			return G2; // ~ 100 kbps
		case TelephonyManager.NETWORK_TYPE_HSDPA:
			return G3; // ~ 2-14 Mbps
		case TelephonyManager.NETWORK_TYPE_HSPA:
			return G3; // ~ 700-1700 kbps
		case TelephonyManager.NETWORK_TYPE_HSUPA:
			return G3; // ~ 1-23 Mbps
		case TelephonyManager.NETWORK_TYPE_UMTS:
			return G3; // ~ 400-7000 kbps
		case TelephonyManager.NETWORK_TYPE_EHRPD:
			return G3; // ~ 1-2 Mbps
		case TelephonyManager.NETWORK_TYPE_EVDO_B:
			return G3; // ~ 5 Mbps
		case TelephonyManager.NETWORK_TYPE_HSPAP:
			return G3; // ~ 10-20 Mbps
		case TelephonyManager.NETWORK_TYPE_IDEN:
			return G2; // ~25 kbps
		case TelephonyManager.NETWORK_TYPE_LTE:
			return G4; // ~ 10+ Mbps
		case TelephonyManager.NETWORK_TYPE_UNKNOWN:
			return G2;
		default:
			return G2;
		}
	}

}