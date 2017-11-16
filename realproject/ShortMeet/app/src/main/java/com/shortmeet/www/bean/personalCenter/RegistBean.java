package com.shortmeet.www.bean.personalCenter;

/**
 * Created by Fenglingyue on 2017/9/2.
 */

public class RegistBean {

    /**用户手机号**/
    private String phone;

    /**用户密码**/
    private String password;

    /**验证码**/
    private String code;

    /**设备信息**/
    private String device_no;

    /**设备类型**/
    private int user_sub_type;

    /**游客用户名**/
    private String visitor_name;


    public int getUser_sub_type() {
        return user_sub_type;
    }

    public void setUser_sub_type(int user_sub_type) {
        this.user_sub_type = user_sub_type;
    }

    public String getVisitor_name() {
        return visitor_name;
    }

    public void setVisitor_name(String visitor_name) {
        this.visitor_name = visitor_name;
    }

    public String getDevice_no() {
        return device_no;
    }

    public void setDevice_no(String device_no) {
        this.device_no = device_no;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
