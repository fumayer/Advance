package com.quduquxie.function.creation.create.util;

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

public class LiteratureCreateUtil {

    //频道
    public static ArrayList<String> getLiteratureChannel(Context context) {
        ArrayList<String> channelList = new ArrayList<>();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String channelString = sharedPreferences.getString("literature_create_channel", "");

        if (TextUtils.isEmpty(channelString)) {
            channelString = "男频|女频";
            sharedPreferences.edit().putString("literature_create_channel", channelString).apply();
        }

        if (channelString.length() > 0) {
            String[] channels = channelString.split("\\|");
            for (String label : channels) {
                channelList.add(label);
            }
        }
        return channelList;
    }

    public static void setLiteratureChannel(Context context, ArrayList<String> channels) {
        if (channels == null) {
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (String label : channels) {
            stringBuilder.append(label);
            stringBuilder.append("|");
        }
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putString("literature_create_channel", stringBuilder.toString()).apply();
    }

    //风格
    public static ArrayList<String> getLiteratureStyle(Context context) {
        ArrayList<String> styleList = new ArrayList<>();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String styleString = sharedPreferences.getString("literature_create_style", "");

        if (TextUtils.isEmpty(styleString)) {
            styleString = "轻松|虐心|正剧";
            sharedPreferences.edit().putString("literature_create_style", styleString).apply();
        }

        if (styleString.length() > 0) {
            String[] styles = styleString.split("\\|");
            for (String label : styles) {
                styleList.add(label);
            }
        }
        return styleList;
    }

    public static void setLiteratureStyle(Context context, ArrayList<String> styles) {
        if (styles == null) {
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (String label : styles) {
            stringBuilder.append(label);
            stringBuilder.append("|");
        }
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putString("literature_create_style", stringBuilder.toString()).apply();
    }

    //结局
    public static ArrayList<String> getLiteratureEnding(Context context) {
        ArrayList<String> endingList = new ArrayList<>();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String endingString = sharedPreferences.getString("literature_create_ending", "");

        if (TextUtils.isEmpty(endingString)) {
            endingString = "悲情|大团圆|开放式";
            sharedPreferences.edit().putString("literature_create_ending", endingString).apply();
        }

        if (endingString.length() > 0) {
            String[] endings = endingString.split("\\|");
            for (String label : endings) {
                endingList.add(label);
            }
        }
        return endingList;
    }

    public static void setLiteratureEnding(Context context, ArrayList<String> ending) {
        if (ending == null) {
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (String label : ending) {
            stringBuilder.append(label);
            stringBuilder.append("|");
        }
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putString("literature_create_ending", stringBuilder.toString()).apply();
    }

    //状态
    public static ArrayList<String> getLiteratureAttributes(Context context) {
        ArrayList<String> attributesList = new ArrayList<>();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String attributesString = sharedPreferences.getString("literature_create_attributes", "");

        if (TextUtils.isEmpty(attributesString)) {
            attributesString = "serialize|finish";
            sharedPreferences.edit().putString("literature_create_attributes", attributesString).apply();
        }

        if (attributesString.length() > 0) {
            String[] endings = attributesString.split("\\|");
            for (String label : endings) {
                attributesList.add(label);
            }
        }
        return attributesList;
    }

    public static void setLiteratureAttributes(Context context, ArrayList<String> attributes) {
        if (attributes == null) {
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (String label : attributes) {
            stringBuilder.append(label);
            stringBuilder.append("|");
        }
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putString("literature_create_attributes", stringBuilder.toString()).apply();
    }

    //男频分类
    public static ArrayList<Category> getLiteratureCategoriesMan(Context context) {
        ArrayList<Category> categoryList = new ArrayList<>();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String categoryString = sharedPreferences.getString("literature_create_categories_man", "");

        if (TextUtils.isEmpty(categoryString)) {
            categoryString = "玄幻奇幻|都市小说|悬疑惊悚|游戏体育|乡村生活|热血青春|科幻灵异|武侠仙侠|官场职场|历史军事|二次元";
            sharedPreferences.edit().putString("literature_create_categories_man", categoryString).apply();
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

    public static void setLiteratureCategoriesMan(Context context, ArrayList<String> categories) {
        if (categories == null) {
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (String label : categories) {
            stringBuilder.append(label);
            stringBuilder.append("|");
        }
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putString("literature_create_categories_man", stringBuilder.toString()).apply();
    }

    //女频分类
    public static ArrayList<Category> getLiteratureCategoriesWoman(Context context) {
        ArrayList<Category> categoryList = new ArrayList<>();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String categoryString = sharedPreferences.getString("literature_create_categories_woman", "");

        if (TextUtils.isEmpty(categoryString)) {
            categoryString = "总裁豪门|穿越重生|古典架空|宫斗宅斗|都市高干|女尊王朝|仙侠种田|幻想灵异|青春校园|婚恋职场|同人衍生|耽美小说|二次元";
            sharedPreferences.edit().putString("literature_create_categories_woman", categoryString).apply();
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

    public static void setLiteratureCategoriesWoman(Context context, ArrayList<String> categories) {
        if (categories == null) {
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (String label : categories) {
            stringBuilder.append(label);
            stringBuilder.append("|");
        }
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putString("literature_create_categories_woman", stringBuilder.toString()).apply();
    }
}
