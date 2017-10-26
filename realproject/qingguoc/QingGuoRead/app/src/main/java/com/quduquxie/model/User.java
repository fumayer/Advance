package com.quduquxie.model;

import java.io.Serializable;

/**
 * Created on 16/12/2.
 * Created by crazylei.
 */

public class User implements Serializable {

    public String id;
    public int is_new;
    public int is_sign;
    public String token;
    public String status;
    public String penname;
    public String platform;
    public int is_uploaded;
    public String user_name;
    public String avatar_url;
    public long register_time;
    public String qq;

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", is_new=" + is_new +
                ", is_sign=" + is_sign +
                ", token='" + token + '\'' +
                ", status='" + status + '\'' +
                ", penname='" + penname + '\'' +
                ", platform='" + platform + '\'' +
                ", is_uploaded=" + is_uploaded +
                ", user_name='" + user_name + '\'' +
                ", avatar_url='" + avatar_url + '\'' +
                ", register_time=" + register_time +
                ", qq='" + qq + '\'' +
                '}';
    }
}
