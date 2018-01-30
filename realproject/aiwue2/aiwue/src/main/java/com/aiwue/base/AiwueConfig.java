package com.aiwue.base;

/**
 *  整个程序配置文件
 * Created by Yibao on 2017年4月14日12:32:50
 * Copyright (c) 2017 aiwue.com All rights reserved
 */
public class AiwueConfig {

    public static final String AIWUE_BASE_URL = "https://www.aiwue.com/";
//    //图片服务器地址
    public static final String AIWUE_API_PIC_URL = "http://pic.aiwue.com/";
    //短视频服务器地址
    public static final String AIWUE_API_SHORT_VIDEO_URL = "http://video.aiwue.com/short-video/";
    //课程视频服务器地址
    public static final String AIWUE_API_COURSE_VIDEO_URL = "http://video.aiwue.com/course/";


//    public static final String AIWUE_BASE_URL = "http://192.168.0.11:8080/";
//    //图片服务器地址
//    //public static final String AIWUE_API_PIC_URL = "http://pic.aiwue.com/test/";
//    //短视频服务器地址
//    public static final String AIWUE_API_SHORT_VIDEO_URL = "http://video.aiwue.com/short-video_test/";
//    //课程视频服务器地址
//    public static final String AIWUE_API_COURSE_VIDEO_URL = "http://video.aiwue.com/course_test/";


    //----------------静态页面URL--------------------------------------
    public static final String AIWUE_STATIC_URL = "http://www.aiwue.com/";
    //article URL
    public static final String AIWUE_ARTICLE_URL = AIWUE_STATIC_URL+"article/";
    //NOTE URL
    public static final String AIWUE_NOTE_URL = AIWUE_STATIC_URL+"static/";
    //course URL
    public static final String AIWUE_COURSE_URL = AIWUE_STATIC_URL+"static/";
    //user URL
    public static final String AIWUE_USER_URL = AIWUE_STATIC_URL+"user/";
    //comment URL
    public static final String AIWUE_COMMENT_URL = AIWUE_STATIC_URL+"comment/";



    public static final String APP_KEY = "SYKw4emQyl4yVX71nkN8SRqXszUN64Dg";

    //密码上传前要加盐后md5
    public static final String PASSWORD_SALT = "SYKw4emQyl4yVX71nkN8SRqXszUN64Dg";

}
