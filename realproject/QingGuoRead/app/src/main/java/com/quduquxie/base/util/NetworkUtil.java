package com.quduquxie.base.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created on 17/7/17.
 * Created by crazylei.
 */

public class NetworkUtil {

    public static final int NETWORK_NONE = -1;
    public static final int NETWORK_MOBILE = 0;
    public static final int NETWORK_WIFI = 1;

    public static int NETWORK_TYPE = NETWORK_MOBILE;

    public static void initNetworkType(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isAvailable()) {
            NETWORK_TYPE = NETWORK_NONE;
            return;
        }

        int networkInfoType = networkInfo.getType();

        if (networkInfoType == ConnectivityManager.TYPE_MOBILE) {
            NETWORK_TYPE = NETWORK_MOBILE;
        } else if (networkInfoType == ConnectivityManager.TYPE_WIFI) {
            NETWORK_TYPE = NETWORK_WIFI;
        }
    }

    public static int loadNetworkType(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isAvailable()) {
            return NETWORK_NONE;
        }

        int networkInfoType = networkInfo.getType();

        if (networkInfoType == ConnectivityManager.TYPE_MOBILE) {
            return NETWORK_MOBILE;
        } else if (networkInfoType == ConnectivityManager.TYPE_WIFI) {
            return NETWORK_WIFI;
        }

        return NETWORK_NONE;
    }

    public static boolean checkNetwork(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isAvailable()) {
            return false;
        }

        int networkInfoType = networkInfo.getType();

        return networkInfoType == ConnectivityManager.TYPE_MOBILE || networkInfoType == ConnectivityManager.TYPE_WIFI;
    }
}