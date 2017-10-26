package com.quduquxie.util;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by q on 2016/4/22.
 */
public class TimeUtils {
    private static final String TAG = "TimeUtils";


    /**
     * 获取当前系统的时间格式
     *
     * context
     */
    public static String getTimeFormat(Context context) {
        String strTimeFormat = null;
        try {
            ContentResolver cv = context.getContentResolver();
            strTimeFormat = android.provider.Settings.System.getString(cv,
                    android.provider.Settings.System.TIME_12_24);
            if (isStrEmpty(strTimeFormat)) {
                strTimeFormat = nullStrToEmpty("24");
            }
            QGLog.d(TAG, "getTimeFormat " + strTimeFormat);
        } catch (Exception e) {
            QGLog.e(TAG, "getTimeFormat error");
            e.printStackTrace();
        }
        return strTimeFormat;
    }

    public static boolean isStrEmpty(String str) {
        return (str == null || str.length() == 0);
    }

    public static String nullStrToEmpty(String str) {
        return (str == null ? "" : str);
    }

    public static String timeFormat(long ms) {
        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;

        long day = ms / dd;
        long hour = (ms - day * dd) / hh;
        long minute = (ms - day * dd - hour * hh) / mi;
        long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        long milliSecond = ms - day * dd - hour * hh - minute * mi - second
                * ss;

        String strDay = day < 10 ? "0" + day : "" + day;
        String strHour = hour < 10 ? "0" + hour : "" + hour;
        String strMinute = minute < 10 ? "0" + minute : "" + minute;
        String strSecond = second < 10 ? "0" + second : "" + second;
        String strMilliSecond = milliSecond < 10 ? "0" + milliSecond : "" + milliSecond;
        strMilliSecond = milliSecond < 100 ? "0" + strMilliSecond : ""
                + strMilliSecond;
        return strDay + " 天" + strHour + " 小时" + strMinute + " 分" + strSecond
                + " 秒" + strMilliSecond + " 毫秒";
    }

    public static boolean isLaterDay(long time) {
        Date date = new Date();
        if (time >= 0) {
            long l = date.getTime() - time;
            long hour = (l / (60 * 60 * 1000));
            return hour > 24;
        }
        return false;
    }

    public static boolean getBooleanPreferences(Context context,
                                                String preName, boolean defaultValue) {
        SharedPreferences defaultPf = PreferenceManager
                .getDefaultSharedPreferences(context);
        return defaultPf.getBoolean(preName, defaultValue);
    }

    public static Date addDay(Date date, int num) {
        Calendar startDT = Calendar.getInstance();
        startDT.setTime(date);
        startDT.add(Calendar.DAY_OF_MONTH, num);
        return startDT.getTime();
    }

    public static String compareTime(SimpleDateFormat formatter, long time) {
        String date;
        long interval = System.currentTimeMillis() - time;
        if (interval < 0) {
            date = "刚刚";
        }
        long day = interval / (24 * 60 * 60 * 1000);
        long hour = (interval / (60 * 60 * 1000) - day * 24);
        long minute = ((interval / (60 * 1000)) - day * 24 * 60 - hour * 60);

        if (day > 0) {
            if (day >= 1 && day <= 7) {
                date = day + "天前";
            } else {
                date = formatter.format(time);
            }
        } else if (hour > 0) {
            date = hour + "小时前";
        } else if (minute > 0) {
            date = minute + "分钟前";
        } else {
            date = "刚刚";
        }
        return date;
    }
}
