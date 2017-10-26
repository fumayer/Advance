package com.quduquxie.function.search.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;

/**
 * Created on 16/10/19.
 * Created by WangLei.
 */

public class SearchUtil {

    /**
     * 获取搜索历史词
     **/
    public static ArrayList<String> getHistoryWord(Context context) {
        ArrayList<String> list = new ArrayList<>();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String historyString = sharedPreferences.getString("search_history", "");
        if (historyString.length() > 0) {
            String[] historyKeys = historyString.split("\\|");
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
     * 保存搜索历史词
     **/
    public static void saveHistoryWord(Context context, ArrayList<String> list) {
        if (list == null) {
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (String historyWord : list) {
            stringBuilder.append(historyWord);
            stringBuilder.append("|");
        }
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putString("search_history", stringBuilder.toString()).apply();
    }

    /**
     * 删除搜索历史
     **/
    public static void deleteHistoryWord(Context mContext) {
        saveHistoryWord(mContext, new ArrayList<String>());
    }
}
