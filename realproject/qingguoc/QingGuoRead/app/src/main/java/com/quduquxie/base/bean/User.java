package com.quduquxie.base.bean;

import java.io.Serializable;

/**
 * Created on 17/7/18.
 * Created by crazylei.
 */

public class User implements Serializable {

    public String id;
    public String qq;
    public String name;
    public String token;
    public String avatar;
    public String status;
    public String platform;
    public String user_name;

    public int is_new;
    public int is_sign;
    public int is_uploaded;

    public long register_time;

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", qq='" + qq + '\'' +
                ", name='" + name + '\'' +
                ", token='" + token + '\'' +
                ", avatar='" + avatar + '\'' +
                ", status='" + status + '\'' +
                ", platform='" + platform + '\'' +
                ", user_name='" + user_name + '\'' +
                ", is_new=" + is_new +
                ", is_sign=" + is_sign +
                ", is_uploaded=" + is_uploaded +
                ", register_time=" + register_time +
                '}';
    }
}