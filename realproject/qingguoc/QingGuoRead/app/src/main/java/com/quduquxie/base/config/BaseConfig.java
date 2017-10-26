package com.quduquxie.base.config;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created on 17/7/12.
 * Created by crazylei.
 */

public class BaseConfig {

    //开发模式开关
    public static final boolean DEVELOP_MODE = true;

    //Log标志
    public static final String TAG = "QuDuQuXie";

    //请求读取时长
    public static final int DEFAULT_READ_TIMEOUT = 15000;

    //请求连接时长
    public static final int DEFAULT_CONNECT_TIMEOUT = 20000;

    //书架排列方式：0 阅读时间，1 更新时间 ，2 书名
    public static int book_list_sort = 0;

    //阅读页背景模式
    public static int READING_BACKGROUND_MODE = 1;
    //阅读页行距
    public static float READING_INTERLINEAR_SPACE = 0.5f;
    //阅读页段间距
    public static float READING_PARAGRAPH_SPACE = 1.0f;
    //阅读页内容上下页边距
    public static int READING_CONTENT_TOP_SPACE = 45;
    //阅读页内容页左右边距
    public static int READING_CONTENT_LEFT_SPACE = 16;
    //阅读页字体
    public static int READING_TYPEFACE = 0;
    //阅读页翻页动画
    public static int READING_FLIP_MODE = 1;
    //阅读页翻页效果是否为上下翻页
    public static boolean READING_FLIP_UP_DOWN = true;
    // 阅读页默认字体大小
    public static int READING_TEXT_SIZE = 15;

    //列表加载偏移量
    public static final int LIST_PAGINATION_COUNT = 20;

    //标识
    public static final int SHELF_DISPLAY_GRID = 0x60;
    public static final int SHELF_DISPLAY_SLIDE = 0x61;


    //阿里百川反馈APP_KEY
    public static final String FEEDBACK_APP_KEY = "23369540";

    //微信登陆ID
    public static final String WX_APP_ID = "wx35fa1b996cb804f7";

    //QQ登陆ID
    public static final String QQ_APP_ID = "1105362624";

    //小米推送
    public static final String MI_PUSH_APP_KEY = "5871749959726";
    public static final String MI_PUSH_APP_ID = "2882303761517499726";

    //默认渠道
    public static final String DEFAULT_CHANNEL = "qg0000_001";

    //默认定时任务时间
    public static final int DEFAULT_CLOCKED_TIME = 5 * 60 * 1000;

    //时间格式化
    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.CHINA);


    //常量
    public static final String ENABLE = "enable";
    public static final String DISABLE = "disable";

    public static final String ERROR = "请求章节内容失败~";
    public static final String DISABLE_MESSAGE = "内容审核中,稍候再来看看吧~";

    public static final int DEFAULT_CODE = "青果阅读".hashCode();





    //私有SharePreference名称
    public static final String FLAG_SHARE_PREFERENCE_NAME = "flag_share_preference_name";
    //应用启动时间
    public static final String FLAG_START_APPLICATION_TIME = "flag_start_application_time";
    //应用自动亮度
    public static final String FLAG_BRIGHTNESS_FOLLOW_SYSTEM = "flag_brightness_follow_system";
    //应用自定义屏幕亮度
    public static final String FLAG_SCREEN_BRIGHTNESS = "flag_screen_brightness";
    //应用推送开关
    public static final String FLAG_PUSH_NOTIFICATION = "flag_push_notification";
    //应用书架排列方式
    public static
    final String FLAG_BOOK_LIST_SORT = "flag_book_list_sort";
    //应用版本引导
    public static final String FLAG_VERSION_GUIDE = "flag_version_guide";
    //应用检查更新时间
    public static final String FLAG_CHECK_VERSION_TIME = "flag_check_version_time";
    //应用书籍检查时间
    public static final String FLAG_SHELF_CHECK_BOOK_TIME = "flag_shelf_check_book_time";
    //应用书架展示方式
    public static final String FLAG_SHELF_DISPLAY_MODE = "flag_shelf_display_mode";



    //阅读页背景模式
    public static final String FLAG_READING_BACKGROUND_MODE = "flag_reading_background_mode";
    //阅读页行距
    public static final String FLAG_READING_INTERLINEAR_SPACE = "flag_reading_interlinear_space";
    //阅读页字体
    public static final String FLAG_READING_TYPEFACE = "flag_reading_typeface";
    //阅读页翻页模式
    public static final String FLAG_READING_FLIP_MODE = "flag_reading_flip_mode";
    //阅读页夜间模式
    public static final String FLAG_READING_NIGHT_MODE= "flag_reading_night_mode";
    //阅读页字体大小
    public static final String FLAG_READING_TEXT_SIZE= "flag_reading_text_size";
    //阅读页夜间模式切换
    public static final String FLAG_READING_NIGHT_MODE_FROM = "flag_reading_night_mode_from";
    //阅读页全屏模式
    public static final String FLAG_READING_FULL_WINDOW = "flag_reading_full_window";
    //阅读页屏幕方向
    public static final String FLAG_READING_SCREEN_ORIENTATION = "flag_reading_screen_orientation";



    //书架功能引导
    public static final String FLAG_SHELF_GUIDE = "flag_shelf_guide";
    public static final String FLAG_SHELF_SLIDE_MORE_GUIDE = "flag_shelf_slide_more_guide";

    //阅读页引导
    public static final String FLAG_READING_GUIDE = "flag_reading_guide";
    public static final String FLAG_READING_CATEGORY_GUIDE = "flag_reading_category_guide";
    public static final String FLAG_READING_BOOKMARK_GUIDE = "flag_reading_bookmark_guide";
}