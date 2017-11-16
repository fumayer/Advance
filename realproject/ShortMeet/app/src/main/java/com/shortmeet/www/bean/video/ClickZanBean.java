package com.shortmeet.www.bean.video;

/**
 * Created by Fenglingyue on 2017/11/7.
 */
/*
 *  Fly 注：点赞  取消赞
 */
public class ClickZanBean {
  private  String sessionid;
  private  String vod_id;
  private  int type;
  private  String table_name;
  private  String   thumb_up_id ;

    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }

    public String getVod_id() {
        return vod_id;
    }

    public void setVod_id(String vod_id) {
        this.vod_id = vod_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTable_name() {
        return table_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }

    public String getThumb_up_id() {
        return thumb_up_id;
    }

    public void setThumb_up_id(String thumb_up_id) {
        this.thumb_up_id = thumb_up_id;
    }
}
