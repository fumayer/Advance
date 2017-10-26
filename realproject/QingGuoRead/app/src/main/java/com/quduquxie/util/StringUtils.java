package com.quduquxie.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by q on 2016/4/22.
 */
public class StringUtils {

    private static final String text_discard = "（该网页已经技术转换）";

    public static String deleteTextPoint(String text) {
        text = text.trim();

        int index = text.indexOf(text_discard);
        if (index != -1) {
            text = text.substring(index + text_discard.length()).trim();
        }

        while (text.indexOf("　") == 0 || text.indexOf(" ") == 0
                || text.indexOf("	") == 0) {
            text = text.substring(1);
        }
        try {
            Pattern P = Pattern.compile("^\\p{Punct}", Pattern.UNIX_LINES);
            Matcher M = P.matcher(text);
            while (M.find()) {
                text = text.substring(1);
                M = P.matcher(text);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        while (text.indexOf("　") == 0 || text.indexOf(" ") == 0
                || text.indexOf("	") == 0) {
            text = text.substring(1);
        }
        return text.trim();
    }

    /*
	 * 去掉章节名中的序相关字符,用于判断是否有重复章节
	 */
    public static String getPatterName(String name) {
        String m_patternString = "第[\\d廿两零一二三四五六七八九十百千]+[篇章节集部张卷回]";
        Pattern pattern = Pattern.compile(m_patternString);
        Matcher matcher = pattern.matcher(name);
        while (matcher.find()) {
            name = matcher.replaceAll("").trim();
        }
        return name;
    }

    /**
     * 字符是否为中文
     *
     * c
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS;
    }

    /**
     * 获取热词
     *
     * list
     */
    public static void getHotWord(Context mContext, ArrayList<String> list) {
        try {
            list.clear();
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
            String hotWordStr = sp.getString("hotWord", "");
            if (hotWordStr.length() > 0) {
                String[] hotWords = hotWordStr.split("\\|");
                for (String hotWord : hotWords) {
                    list.add(hotWord);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取历史词
     *
     * list
     */
    public static ArrayList<String> getHistoryWord(Context mContext) {
        ArrayList<String> list = new ArrayList<String>();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
        String historyKeyStr = sp.getString("search_history", "");
        if (historyKeyStr.length() > 0) {
            String[] historyKeys = historyKeyStr.split("\\|");
            int index = 0;
            for (String historyKey : historyKeys) {
                list.add(historyKey);
                index++;
                if (index > 9) {
                    break;
                }
            }
        }
        return list;
    }

    /**
     * 保存历史词
     *
     * list
     */
    public static void saveHistoryWord(Context mContext, ArrayList<String> mList) {
        if (mList == null) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (String historyWord : mList) {
            sb.append(historyWord);
            sb.append("|");
        }
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
        sp.edit().putString("search_history", sb.toString()).apply();
    }

    /**
     * 保存热词
     *
     * list
     */
    public static void saveHotWord(Context mContext, ArrayList<String> mList) {
        if (mList == null || mList.size() < 1) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (String hotWord : mList) {
            sb.append(hotWord);
            sb.append("|");
        }
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
        sp.edit().putString("hotWord", sb.toString()).apply();
//        AppLog.e("lq,","hotword:"+sb.toString());
    }

    /**
     * 判断服务是否启动
     *
     * context
     * serviceName
     */
    public static boolean isServiceRunning(Context context, String serviceName) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        boolean isServiceRunning = false;
        for (ActivityManager.RunningServiceInfo service : activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceName.equals(service.service.getClassName())) {
                isServiceRunning = true;
            }
        }
        return isServiceRunning;
    }

    public static String compareTime(SimpleDateFormat formatter, long time) {
        String date = "";
        long l = System.currentTimeMillis() - time;
        if (l < 0) {
            return "刚刚";
        }
        long day = l / (24 * 60 * 60 * 1000);
        long hour = (l / (60 * 60 * 1000) - day * 24);
        long minute = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);

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
