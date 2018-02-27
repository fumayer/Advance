package com.aiwue.model;

import java.io.Serializable;
import java.util.List;

/**
 * 推荐用户model
 * Created by Administrator on 2017/5/2.
 */

public class RecommendFriends implements Serializable {
        private int gendar;
        private String nickName;
        private String headPicName;
        private int userId;
        private int statusNum;
        private int grade;
        private String slogen;

    public int getGendar() {
        return gendar;
    }

    public void setGendar(int gendar) {
        this.gendar = gendar;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getStatusNum() {
        return statusNum;
    }

    public void setStatusNum(int statusNum) {
        this.statusNum = statusNum;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getSlogen() {
        return slogen;
    }

    public void setSlogen(String slogen) {
        this.slogen = slogen;
    }
}
