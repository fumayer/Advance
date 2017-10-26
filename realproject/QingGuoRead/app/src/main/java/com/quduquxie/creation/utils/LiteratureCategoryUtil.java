package com.quduquxie.creation.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.quduquxie.bean.Category;

import java.util.ArrayList;

/**
 * Created on 16/11/24.
 * Created by crazylei.
 */

public class LiteratureCategoryUtil {

    public static ArrayList<Category> getCategoryMan(Context context) {
        ArrayList<Category> categoryList = new ArrayList<>();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String categoryString = sharedPreferences.getString("category_man", "");

        if (TextUtils.isEmpty(categoryString)) {
            categoryString = "玄幻奇幻|都市小说|悬疑惊悚|游戏体育|乡村生活|热血青春|科幻灵异|武侠仙侠|官场职场|历史军事|二次元";
            sharedPreferences.edit().putString("category_man", categoryString).apply();
        }

        if (categoryString.length() > 0) {
            String[] categories = categoryString.split("\\|");
            Category category;
            for (String label : categories) {
                category = new Category();
                category.label = label;
                category.check = false;
                categoryList.add(category);
            }
        }
        return categoryList;
    }

    public static ArrayList<Category> getCategoryWoman(Context context) {
        ArrayList<Category> categoryList = new ArrayList<>();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String categoryString = sharedPreferences.getString("category_woman", "");

        if (TextUtils.isEmpty(categoryString)) {
            categoryString = "总裁豪门|穿越重生|古典架空|宫斗宅斗|都市高干|女尊王朝|仙侠种田|幻想灵异|青春校园|婚恋职场|同人衍生|耽美小说|二次元";
            sharedPreferences.edit().putString("category_woman", categoryString).apply();
        }

        if (categoryString.length() > 0) {
            String[] categories = categoryString.split("\\|");
            Category category;
            for (String label : categories) {
                category = new Category();
                category.label = label;
                category.check = false;
                categoryList.add(category);
            }
        }
        return categoryList;
    }

    public static void setCategoryMan(Context context, ArrayList<String> categories, String category_key) {
        if (categories == null) {
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (String category : categories) {
            stringBuilder.append(category);
            stringBuilder.append("|");
        }
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putString(category_key, stringBuilder.toString()).apply();
    }
}
