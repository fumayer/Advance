package com.quduquxie.util;

import android.content.Context;

import com.baidu.mobstat.StatService;

public class StatServiceUtils {

    /**
     * 书城精选banner点击
     */
    public static void statBookStoreBannerClick(Context context, String position) {
        StatService.onEvent(context, "v_Bookstore_Banner", "位置:" + position);
    }

    /**
     * 书城精选重磅推荐点击
     */
    public static void statFeaturedRecommendClick(Context context, String position) {
        StatService.onEvent(context, "v_Featured_Recommend_click", "位置:" + position);
    }

    /**
     * 书城精选模块点击
     * **/
    public static void statFeaturedHeadClick(Context context, String name) {
        StatService.onEvent(context, "v_Featured_Head_click", "名称:" + name);
    }

    /**
     * 书城精选模块中书籍点击数量
     * **/
    public static void statFeaturedBookClick(Context context, String name) {
        StatService.onEvent(context, "v_Featured_Book_click", name);
    }

    /**
     * 书城发现分类点击
     * **/
    public static void statFindCategoryClick(Context context, String name) {
        StatService.onEvent(context, "v_Find_Category_click", name);
    }

    /**
     * 书城发现榜单模块头部点击
     * **/
    public static void statFindToppedHeadClick(Context context, String name) {
        StatService.onEvent(context, "v_Find_Topped_Head_click", name);
    }

    /**
     * 书城发现榜单模块书籍点击
     * **/
    public static void statFindToppedBookClick(Context context, String name, int position) {
        StatService.onEvent(context, "v_Find_Topped_Book_click", name + ":" + position);
    }

    /**
     * 书城发现榜单模块更多书籍点击
     * **/
    public static void statFindToppedMoreClick(Context context, String name) {
        StatService.onEvent(context, "v_Find_Topped_More_click", name);
    }

    /**
     * 书城推荐卡片点击
     */
    public static void statPartClick(Context mContext, String position) {
        StatService.onEvent(mContext, "v_Part_click", "位置:" + position);
    }

    /**
     * 分类页item点击
     * **/
    public static void statCategoryClick(Context context, String message) {
        StatService.onEvent(context, "v_Category_click", message);
    }

    /**
     * 书架删除书籍
     */
    public static void statDeleteBook(Context mContext) {
        StatService.onEvent(mContext, "v_Bookshelf_Deletebook", "书架删除书籍");
    }

    /**
     * 点击书籍详情加入书架
     */
    public static void statBookDetailsAddBook(Context context) {
        StatService.onEvent(context, "v_BookDetails_Addbook", "点击书籍详情加入书架");
    }

    /**
     * 点击书籍详情阅读
     */
    public static void statBookDetailsRead(Context context) {
        StatService.onEvent(context, "v_BookDetails_Read", "点击书籍详情阅读");
    }

    /**
     * 点击返回书架
     */
    public static void statBackShelf(Context context) {
        StatService.onEvent(context, "v_Backshelf", "点击返回书架");
    }

    /**
     * 阅读页菜单点击目录
     */
    public static void statReadPageCatalog(Context context) {
        StatService.onEvent(context, "v_Readpage_Catalog", "阅读页菜单点击目录");
    }

    /**
     * 阅读页菜单点击字体
     */
    public static void statReadPageFont(Context context, String fontType) {
        StatService.onEvent(context, "v_Readpage_Font", fontType);
    }

    /**
     * 阅读页行距点击
     */
    public static void statReadPageSpacing(Context context, String spaceType) {
        StatService.onEvent(context, "v_Readpage_Spacing", spaceType);
    }

    /**
     * 阅读页点击添加书签
     */
    public static void statReadPageMark(Context context) {
        StatService.onEvent(context, "v_Readpage_Mark", "阅读页点击添加书签");
    }

    /**
     * 阅读页背景颜色设置
     */
    public static void statReadPageBgColor(Context context, String colorId) {
        StatService.onEvent(context, "v_Readpage_Bgcolor", colorId);
    }

    /**
     * 阅读页点击亮度调节
     */
    public static void statReadPageBrightness(Context context) {
        StatService.onEvent(context, "v_Readpage_Brightness", "阅读页点击亮度调节");
    }

    /**
     * 阅读页点击跟随系统
     */
    public static void statReadPageFollowSystem(Context context) {
        StatService.onEvent(context, "v_Readpage_Followsystem", "阅读页点击跟随系统");
    }

    /**
     * 阅读页点击夜间模式按钮
     */
    public static void statReadPageNightMode(Context context) {
        StatService.onEvent(context, "v_Readpage_Nightmode", "阅读页点击夜间模式按钮");
    }

    /**
     * 阅读页点击下一章节按钮
     */
    public static void statReadPageFlipBtn(Context context, String content) {
        StatService.onEvent(context, "v_Readpage_Flipbtn", content);
    }

    /**
     * 阅读页点击滑块反翻章节
     */
    public static void statReadPageSlideChapter(Context context) {
        StatService.onEvent(context, "v_Readpage_Slidechapter", "阅读页点击滑块反翻章节");
    }

    /**
     * 书籍列表页添加书籍
     */
    public static void statBookListAddBook(Context context) {
        StatService.onEvent(context, "v_Booklist_Addbook", "书籍列表页添加书籍");
    }

    /**
     * 书籍下载
     */
    public static void statBookShelfDownload(Context context) {
        StatService.onEvent(context, "v_Download_book", "书架页下载书籍");
    }

    /**
     * 书架页点击WiFi传书
     */
    public static void statBookShelfWIFI(Context context) {
        StatService.onEvent(context, "v_Bookshelf_Wifi", "书架页点击Wifi传书");
    }

    /**
     * 书架页点击导入本地书籍
     */
    public static void statBookShelfImportBook(Context context) {
        StatService.onEvent(context, "v_Bookshelf_Importbook", "书架页点击导入本地书籍");
    }

    /**
     * 书架页点击下载管理
     */
    public static void statBookShelfDownloadManager(Context context) {
        StatService.onEvent(context, "v_Bookshelf_Download", "书架页点击下载管理");
    }

    /**
     * 阅读页点击翻页模式
     */
    public static void statReadPageFlip(Context context, String flipMode) {
        StatService.onEvent(context, "v_Readpage_flip", "翻页模式:" + flipMode);
    }

    /**
     * WIFI传书
     */
    public static void statWiFiTransport(Context context, String name) {
        StatService.onEvent(context, "v_Wifi_Transport", "WIFI传书《" + name + "》");

    }

    /**
     * 本地导入
     */
    public static void statLocalFileImport(Context context, String name) {
        StatService.onEvent(context, "v_Local_File_Import", "本地导入《" + name + "》");
    }

    public static void statReadCharge(Context context) {
        StatService.onEvent(context, "v_Start_BookCharge", "书籍收费章节提醒");
    }

    public static void statReadWeb(Context context) {
        StatService.onEvent(context, "v_Start_BookWeb", "浏览器阅读");
    }
}
