package com.quduquxie.base.util;

import android.content.Context;

import com.baidu.mobstat.StatService;

/**
 * Created on 17/7/12.
 * Created by crazylei.
 */

public class StatServiceUtil {

    /**
     * 启动间隔
     * **/
    public static void statStartInterval(Context context, String time) {
        StatService.onEvent(context, "Start_Application_Interval", "应用启动间隔: " + time);
    }

    /**
     * 点击启动推荐书籍
     * **/
    public static void statSplashRecommend(Context context, String id) {
        StatService.onEvent(context, "Splash_Recommend", "书籍: " + id);
    }

    /**
     * 书架点击添加书籍
     * **/
    public static void statBookShelfInsertBook(Context context) {
        StatService.onEvent(context, "BookShelf_Insert_Book", "书架点击添加书籍");
    }

    /**
     * 书架删除书籍
     * **/
    public static void statBookshelfDeleteBook(Context context) {
        StatService.onEvent(context, "Bookshelf_Delete_Book", "书架删除书籍");
    }

    /**
     * 书城点击Banner
     * **/
    public static void statBookStoreBanner(Context context, int position) {
        StatService.onEvent(context, "BookStore_Banner", "位置：" + position);
    }

    /**
     * 书城分类点击
     * **/
    public static void statBookStoreCategory(Context context, String channel, String category) {
        StatService.onEvent(context, "BookShelf_Insert_Book", channel + " : " + category);
    }

    /**
     * 书城排行点击
     * **/
    public static void statBookStoreRanking(Context context, String ranking) {
        StatService.onEvent(context, "BookStore_Ranking", ranking);
    }

    /**
     * 书城排行书籍点击
     * **/
    public static void statBookStoreRankingBook(Context context, String ranking, int position) {
        StatService.onEvent(context, "BookStore_Ranking_Book", ranking + " : " + position);
    }

    /**
     * 书城模块头部点击
     * **/
    public static void statBookStoreModuleHead(Context context, String title) {
        StatService.onEvent(context, "BookStore_Module_Head", title);
    }

    /**
     * 封面点击立即阅读
     * **/
    public static void statCoverReadImmediately(Context context) {
        StatService.onEvent(context, "Cover_Read_Immediately", "封面点击立即阅读");
    }

    /**
     * 封面点击加入书架
     * **/
    public static void statCoverInsertBookshelf(Context context) {
        StatService.onEvent(context, "Cover_Insert_Bookshelf", "封面点击加入书架");
    }

    /**
     * 封面点击目录
     * **/
    public static void statCoverCatalog(Context context) {
        StatService.onEvent(context, "Cover_Catalog", "封面点击目录");
    }

    /**
     * 封面页推荐点击
     * **/
    public static void statCoverRecommend(Context context, int position) {
        StatService.onEvent(context, "Cover_Recommend", "封面页推荐点击: " + position);
    }

    /**
     * 列表页加入书架
     * **/
    public static void statTabulationInsertBook(Context context) {
        StatService.onEvent(context, "Tabulation_Insert_Book", "列表页加入书架");
    }

    /**
     * 阅读页跳转复杂目录页
     * **/
    public static void statReadingComplicatedCatalog(Context context) {
        StatService.onEvent(context, "Reading_Complicated_Catalog", "阅读页跳转复杂目录页");
    }

    /**
     * 阅读页详细设置
     * **/
    public static void statReadingSettingDetail(Context context) {
        StatService.onEvent(context, "Reading_Setting_Detail", "阅读页详细设置");
    }

    /**
     * 阅读页其他设置
     * **/
    public static void statReadingSettingOther(Context context) {
        StatService.onEvent(context, "Reading_Setting_Detail", "阅读页其他设置");
    }

    /**
     * 阅读页跳转下一章
     * **/
    public static void statReadingNext(Context context) {
        StatService.onEvent(context, "Reading_Next", "阅读页跳转下一章");
    }

    /**
     * 阅读页跳转上一章
     * **/
    public static void statReadingPrevious(Context context) {
        StatService.onEvent(context, "Reading_Previous", "阅读页跳转上一章");
    }

    /**
     * 阅读页滑动切换章节
     * **/
    public static void statReadingChangeChapter(Context context) {
        StatService.onEvent(context, "Reading_Change_Chapter", "阅读页滑动切换章节");
    }

    /**
     * 阅读页改变字体大小
     * **/
    public static void statReadingChangeTextSize(Context context, int size) {
        StatService.onEvent(context, "Reading_Change_Text_Size", "阅读页改变字体大小: " + size);
    }

    /**
     * 阅读页改变行距
     * **/
    public static void statReadingChangeSpace(Context context, int size) {
        StatService.onEvent(context, "Reading_Change_Space", "阅读页改变行距: " + size);
    }

    /**
     * 阅读页改变字体
     * **/
    public static void statReadingChangeTypeface(Context context, String type) {
        StatService.onEvent(context, "Reading_Change_Typeface", "阅读页改变字体: " + type);
    }

    /**
     * 阅读页改变翻页模式
     * **/
    public static void statReadingChangeFlip(Context context, String type) {
        StatService.onEvent(context, "Reading_Change_Flip", "阅读页改变翻页模式: " + type);
    }

    /**
     * 阅读页改变翻页模式
     * **/
    public static void statReadingBrightness(Context context, int size) {
        StatService.onEvent(context, "Reading_Brightness", "阅读页滑动改变亮度: " + size);
    }

    /**
     * 阅读页亮度跟随系统点击
     * **/
    public static void statReadingBrightnessSystem(Context context, boolean state) {
        StatService.onEvent(context, "Reading_Brightness_System", "阅读页亮度跟随系统点击: " + state);
    }

    /**
     * 阅读页改变背景颜色
     * **/
    public static void statReadingBackground(Context context, int index) {
        StatService.onEvent(context, "Reading_Background", "阅读页改变背景颜色: " + index);
    }

    /**
     * 阅读页改变夜间模式
     * **/
    public static void statReadingChangeMode(Context context, int index) {
        StatService.onEvent(context, "Reading_Change_Mode", "阅读页改变夜间模式: " + index);
    }

    /**
     * 阅读完结页推荐点击
     * **/
    public static void statFinishRecommend(Context context, int index) {
        StatService.onEvent(context, "Finish_Recommend", "阅读完结页推荐点击: " + index);
    }

    /**
     * 阅读完结页推荐点击
     * **/
    public static void statComplicatedCatalog(Context context) {
        StatService.onEvent(context, "Complicated_Catalog", "详细目录页目录展示");
    }

    /**
     * 详细目录页书签展示
     * **/
    public static void statComplicatedCatalogBookmark(Context context) {
        StatService.onEvent(context, "Complicated_Catalog_Bookmark", "详细目录页书签展示");
    }

    /**
     * 详细目录页跳转封面页
     * **/
    public static void statComplicatedCatalogCover(Context context) {
        StatService.onEvent(context, "Complicated_Catalog_Cover", "详细目录页跳转封面页");
    }
}