package com.quduquxie.manager;

import com.dingyueads.sdk.Bean.Novel;
import com.dingyueads.sdk.Native.YQNativeAdInfo;
import com.dingyueads.sdk.NativeInit;
import com.dingyueads.sdk.manager.ADStatisticManager;
import com.quduquxie.util.QGLog;

import java.util.HashMap;

public class StatisticManager {

    public static final String TAG = StatisticManager.class.getSimpleName();

    private static volatile StatisticManager statisticManager = null;

    public static final int TYPE_SHOW = 0x20;
    public static final int TYPE_CLICK = 0x21;
    public static final int TYPE_END = 0x22;

    private HashMap<String, YQNativeAdInfo> nativeAD = new HashMap<>();

    public static StatisticManager getStatisticManager() {

        if (statisticManager == null) {
            synchronized (StatisticManager.class) {
                if (statisticManager == null) {
                    statisticManager = new StatisticManager();
                }
            }
        }
        return statisticManager;
    }

    public void schedulingRequest(YQNativeAdInfo nativeAdInfo, Novel novel, int type, String position, String userDeviceID) {

        if (nativeAdInfo != null) {
            nativeAdInfo.setCreateTime(System.currentTimeMillis());
        }

        //广告位置判断
        if (NativeInit.ad_position[0].equals(position)) {
            switch (type) {
                case TYPE_SHOW:
                    if (nativeAdInfo != null && nativeAdInfo.getAdvertisement() != null && !nativeAdInfo.getAdvertisement().isShowed) {
//                        nativeAdInfo.showedDefaultAD(view, BookApplication.getUdid(), novel, position);
                    }
                    break;
                case TYPE_CLICK:
                    if (nativeAdInfo != null && nativeAdInfo.getAdvertisement() != null && !nativeAdInfo.getAdvertisement().isClicked) {
//                        nativeAdInfo.clickedDefaultAD(view, BookApplication.getUdid(), novel, position);
                    }
                    break;
            }
        } else {
            switch (type) {
                case TYPE_SHOW:
                    if (!nativeAD.containsKey(position)) {
                        if (nativeAdInfo != null && nativeAdInfo.getAdvertisement() != null && !nativeAdInfo.getAdvertisement().isShowed && !nativeAdInfo.getAdvertisement().isClicked) {
                            nativeAD.put(position, nativeAdInfo);
                        }
                    }
                    break;
                case TYPE_CLICK:
                    if (nativeAdInfo != null && nativeAdInfo.getAdvertisement() != null && !nativeAdInfo.getAdvertisement().isClicked) {
//                        nativeAdInfo.clickedDefaultAD(view, BookApplication.getUdid(), novel, position);
                        if (nativeAD.containsKey(position)) {
                            nativeAD.remove(position);
                        }
                    }
                    break;
                case TYPE_END:
                    if (nativeAdInfo != null) {
                        if (nativeAD.containsKey(position)) {
                            if (nativeAdInfo.getAdvertisement() != null && !nativeAdInfo.getAdvertisement().isShowed) {
//                                nativeAdInfo.showedDefaultAD(view, BookApplication.getUdid(), novel, position);
                            }
                            nativeAD.remove(position);
                        }

                    } else {
                        ADStatisticManager adStatisticManager = ADStatisticManager.getADStatisticManager();
                        adStatisticManager.onADShowed(userDeviceID, null, null, novel, position);
                        QGLog.e(TAG, "无广告页小说消费: " + position + " : " + novel);
                    }
                    break;
            }
        }
    }
}
