package com.aiwue.model;

/**
 * at @ 用户信息model
 * Created by Yibao on 2017年4月19日14:07:10
 * Copyright (c) 2017 aiwue.com All rights reserved
 */
public class AtUserInfo {
    private Integer id; //用户id
    private String nickName; //昵称
    private Integer sequence; //at的顺序

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

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }
}
