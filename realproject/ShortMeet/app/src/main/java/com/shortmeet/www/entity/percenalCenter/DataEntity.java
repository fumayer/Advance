package com.shortmeet.www.entity.percenalCenter;

/**
 * Created by Fenglingyue on 2017/10/10.
 */

public class DataEntity {

    private String phone;
    private String img;
    private String sex;
    private String area;
    private String nickname;
    private String birthday;
    private String content;
    private String reg_time;
    private String sessionid;
    private String session_time;


    public String getSession_time() {
        return session_time;
    }
    public void setSession_time(String session_time) {
        this.session_time = session_time;
    }


    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setReg_time(String reg_time) {
        this.reg_time = reg_time;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }

    public String getPhone() {
        return phone;
    }

    public String getImg() {
        return img;
    }

    public String getSex() {
        return sex;
    }

    public String getArea() {
        return area;
    }

    public String getNickname() {
        return nickname;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getContent() {
        return content;
    }

    public String getReg_time() {
        return reg_time;
    }

    public String getSessionid() {
        return sessionid;
    }




    /**
     * account_id : 100000016
     * username : sm100000017
     * usertype : null
     * password : 03a5712fbb9ea503e18244ee23f21a44
     * create_time : 2017-10-26 14:29:13
     */

    private long account_id;
    private String username;
    private int usertype;
    private String password;
    private String create_time;

    public void setAccount_id(long account_id) {
        this.account_id = account_id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUsertype(int usertype) {
        this.usertype = usertype;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public long getAccount_id() {
        return account_id;
    }

    public String getUsername() {
        return username;
    }

    public int getUsertype() {
        return usertype;
    }

    public String getPassword() {
        return password;
    }

    public String getCreate_time() {
        return create_time;
    }
}
