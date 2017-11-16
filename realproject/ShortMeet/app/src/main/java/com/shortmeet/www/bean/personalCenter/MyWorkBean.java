package com.shortmeet.www.bean.personalCenter;

/**
 * Created by Fenglingyue on 2017/10/19.
 */

public class MyWorkBean {
    private String sessionid;
    private int page;
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

}
