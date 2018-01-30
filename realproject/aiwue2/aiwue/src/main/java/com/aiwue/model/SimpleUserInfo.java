package com.aiwue.model;

/**
 * 简单用户信息model
 * Created by Yibao on 2017年4月12日18:44:01
 * Copyright (c) 2017 aiwue.com All rights reserved
 */
public class SimpleUserInfo {
    private Integer id; //用户id
    private String nickName; //昵称
    private String headPicName; //头像
    private Integer gendar; //性别

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadPicName() {
        return headPicName;
    }

    public void setHeadPicName(String headPicName) {
        this.headPicName = headPicName;
    }

    public Integer getGendar() {
        return gendar;
    }

    public void setGendar(Integer gendar) {
        this.gendar = gendar;
    }
}
