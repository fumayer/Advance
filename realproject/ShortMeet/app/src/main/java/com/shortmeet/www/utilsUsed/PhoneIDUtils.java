package com.shortmeet.www.utilsUsed;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * Created by Fenglingyue on 2017/10/9.
 */

public class PhoneIDUtils {
    /**
     * 获取设备的唯一标识，deviceId
     *
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = tm.getDeviceId();
        if (deviceId == null) {
            return "-";
        } else {
            return deviceId;
        }
    }
}
