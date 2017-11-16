package com.shortmeet.www.bean.personalCenter;

/**
 * Created by Fenglingyue on 2017/10/31.
 */

public class BindPhoneBean {
    private  String sessionid;
    private  int type;
    private  String phone;
    private  String password;
    private int code;
    private String prev_phone;
    private int prev_code;

    public String getPrev_phone() {
        return prev_phone;
    }

    public void setPrev_phone(String prev_phone) {
        this.prev_phone = prev_phone;
    }

    public int getPrev_code() {
        return prev_code;
    }

    public void setPrev_code(int prev_code) {
        this.prev_code = prev_code;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
