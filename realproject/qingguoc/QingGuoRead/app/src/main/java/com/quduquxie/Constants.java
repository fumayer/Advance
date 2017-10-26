package com.quduquxie;

import android.os.Environment;

public class Constants {

    //消息推送功能，注册标识
    public static final String PUSH_DEVICE_REGISTER_FLAG = "push_device_register_flag";

    //阅读页标识
    public static boolean LANDSCAPE = false;
    public static boolean VOLUME_TURNOVER = true;
    public static boolean FULL_WINDOW_READ = true;

    // 反射resourceType
    public static final int DRAWABLE = 1;
    public static final int COLOR = 2;
    public static final int STYLE = 3;
    //baidu增量更新
    public static final int BAIDU_UPDATE_SUCCESS = 9;
    public static final int BAIDU_UPDATE_FAIL = BAIDU_UPDATE_SUCCESS + 1;
    public static final int IYOUQU_UPDATE_SUCCESS = BAIDU_UPDATE_FAIL + 1;
    public static final int OPEN_TOPIC_LIST = IYOUQU_UPDATE_SUCCESS + 1;
    public static final int UPDATE_MIXTURE = OPEN_TOPIC_LIST + 1;
    public static final int READING_PAGE = UPDATE_MIXTURE + 1;
    // FIXME 上线要改成false
    public static final boolean SHOW_LOG = false;
    public static boolean isSdCard = false;

    //书库banner轮播时长
    public static int CAROUSEL_DURATION = 5 * 1000;

    // 阅读页章节首页提示 默认字体大小
    public static int FONT_CHAPTER_DEFAULT = 18;
    //阅读页面章节首页字体
    public static int FONT_CHAPTER_SIZE = 30;
    //阅读页面章节首页首字字体
    public static int FONT_FIRST_CHAPTER_SIZE = 30;
    //landscape 横屏模式
    public static boolean is_wifi_auto_download = false;//默认false
    public static int book_list_sort_type = 0;//0 默认自动书签时间，1 更新时间 ，2 书名
    public static int refreshTime = 5 * 60 * 1000;

    //阅读页标题距顶部距离
    public static int READ_CONTENT_PAGE_CHAPTER_NAME_MARGIN_TOP = 19;//4-20

    public static int ScreenOffTimeout = 5 * 60 * 1000;// TODO屏幕休眠时间调整为5分钟

    public static int readedCount = 0;
    public static int manualReadedCount = 0;

    public static final int PAGE_COUNT = 10;//一次拉取数据条数

    public static final int RESULT_ADD_SHELF_CODE = 9;
    public static int current_book_last_sort = 0;


    public static final String ENABLE = "enable";//章节状态 是否可读
    public static final String DISABLE_DES = "内容审核中,稍候再来看看吧~";
    public static final String ERROR_DES = "请求章节内容失败~";

    public static final String LOCAL_FILE_PATH = "local_file_path";

    public static final int wifi_transport_port = 8888;

    private static String SDCARD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    private static String APP_PATH = SDCARD_PATH + "/quduquxie";
    private static String APP_PATH_CACHE = APP_PATH + "/cache/";

    public static String APP_PATH_BOOK = APP_PATH + "/book/";
    public static String APP_PATH_DOWNLOAD = APP_PATH + "/download/";
    public static String APP_PATH_IMAGE = APP_PATH + "/image/";
    public static String APP_PATH_GLIDE = APP_PATH + "/glide/";
    public static String APP_PATH_LOCAL = APP_PATH + "/local/";
    public static String APP_PATH_AVATAR = APP_PATH + "/avatar/";
    public static String APP_PATH_LOG = APP_PATH + "/log/";

    /**
     * 推荐页总共有多少条数据
     */
    public static int BOOK_RECOMMEND_TOTAL = 0;
    /**
     * 推荐页当前显示的数据
     */
    public static int BOOK_RECOMMEND_CURRENT = 0;
}
