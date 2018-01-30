package com.aiwue.utils;

/**
 *  一些常量定义
 * Created by Yibao on 17/4/11
 * Copyright (c) 2017 aiwue.com All rights reserved
 */

public interface ConstantValue {
    String DATA = "data";
    public static final String ARTICLE_GENRE_GALLERY = "gallery";
    public static final String ARTICLE_GENRE_VIDEO = "video";
    public static final String ARTICLE_GENRE_ARTICLE = "article";


    String URL = "url";
    String SP_THEME = "theme";
    int THEME_LIGHT = 1;
    int THEME_NIGHT = 2;
    /**
     * 修改主题
     */
    int MSG_TYPE_CHANGE_THEME = 100;

    //=========================================================
    //文章类型定义
    public static final int ARTICLE_TYPE_NORMAL= 0;
    public static final int ARTICLE_TYPE_IMG= 2;
    public static final int ARTICLE_TYPE_VIDEO= 2;

    //内容类型定义
    public static final int CONTENT_TYPE_ARTICLE = 0;  //文章
    public static final int CONTENT_TYPE_NOTE = 10;     //笔记
    public static final int CONTENT_TYPE_COURSE = 20;  //教程
    public static final int CONTENT_TYPE_SPECIAL_TOPICS = 21; //专题
    public static final int CONTENT_TYPE_GOODS = 30;  //商品
    public static final int CONTENT_TYPE_USER = 40;  //用户
    public static final int CONTENT_TYPE_ORGANIZATION = 50; //社团
    public static final int CONTENT_TYPE_COMMENT = 60; //评论
    public static final int CONTENT_TYPE_ACTIVITY = 70; //活动
    public static final int CONTENT_TYPE_PANEL = 80; //板块
    public static final int CONTENT_TYPE_TOPICS = 90; //话题
    public static final int CONTENT_TYPE_TAG = 120; //标签
    public static final int CONTENT_TYPE_HONOR = 130; //荣誉
    public static final int CONTENT_TYPE_COACH = 140; //教练
    public static final int CONTENT_TYPE_MASTER_RELATION = 150; //师承
    public static final int CONTENT_TYPE_USER_ORG_RELATIOM = 160; //用户-社团关系
    public static final int CONTENT_TYPE_ORG_ORG_RELATION = 170; //社团-社团关系
    public static final int CONTENT_TYPE_BULLETIN = 180; //公告
    public static final int CONTENT_TYPE_ADVERTISE = 190; //广告
    public static final int CONTENT_TYPE_HELP = 200; //帮助

    //第三方登录方式
    public static final int THIRD_PARTY_LOGIN_WEIXIN = 0;//微信
    public static final int THIRD_PARTY_LOGIN_WEIBO = 1;//微博
    public static final int THIRD_PARTY_LOGIN_QQ = 2;//微信
    //login
    public static final String LOGIN_TO_WHERE = "login_to_where";

    //验证码方式
    public static final int VCODE_OPERATION_REGISTER = 0; //手机注册
    public static final int VCODE_OPERATION_MODI_MOBLIE_EMAIL = 1; //修改邮箱/手机号
    public static final int VCODE_OPERATION_MODI_PWD = 2; //修改密码时验证

    //消息类型
    public static final int EVENTBUS_MESSENGE_LOGIN_LOGOUT = 0; // 登录或者登出发送的消息

    //笔记类型
    public static final int NOTE_TYPE_POST = 0; // 论坛帖子
    public static final int NOTE_TYPE_STATUS = 1; // 动态
    public static final int NOTE_TYPE_QUESTION = 2; // 问答
    public static final int NOTE_TYPE_VIDEO = 3; // 视频
    public static final int NOTE_TYPE_ALBUM = 4; // 图集
}
