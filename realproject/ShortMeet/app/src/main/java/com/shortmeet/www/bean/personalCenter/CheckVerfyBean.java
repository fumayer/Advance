package com.shortmeet.www.bean.personalCenter;

/**
 * Created by Fenglingyue on 2017/10/31.
 */

public class CheckVerfyBean {
    private String sessionid;
    private String phone ;
    private int code;
    private int type;

    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
