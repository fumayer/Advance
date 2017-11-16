package com.shortmeet.www.bean.video;

/**
 * Created by Fenglingyue on 2017/11/7.
 */
/*
 *  Fly 注：点赞状态  关注状态 点赞总数  评论总数
 */
public class VideoDetailStatusShowBean {
    private  String  vod_id;
    private String sessionid;

    public String getVod_id() {
        return vod_id;
    }

    public void setVod_id(String vod_id) {
        this.vod_id = vod_id;
    }

    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }
}
