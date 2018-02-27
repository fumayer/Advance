package com.aiwue.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;


import java.io.Serializable;


/**
 *  文章model
 * Created by Yibao on 2017年4月12日14:36:55
 * Copyright (c) 2017 aiwue.com All rights reserved
 */
public class Article extends BaseObservable implements Serializable {
    private String pId;
    private int id;
    private String title;//标题
    private String author;//推荐者名称
    private int authorId;//推荐者ID
    private String summary;//文章简介
    private String headPicName;//题图
    private int type;//类型： 0：文章； 1：图片；2：视频；3：话题
    private int commentNum = 0; //该评论的评论数
    private int praiseNum = 0;//点赞数
    private int offenseNum = 0;//举报数
    private int praised = 0; //是否点赞
    private int favorited = 0; //当前登录用户是否收藏
    private int readNum = 0; //阅读数
    private int shareNum= 0;//分享数
    private int saveNum= 0; //收藏数

    private long visibleTime;//

    private int status;//状态



    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getHeadPicName() {
        return headPicName;
    }

    public void setHeadPicName(String headPicName) {
        this.headPicName = headPicName;

    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    public int getPraiseNum() {
        return praiseNum;
    }

    public void setPraiseNum(int praiseNum) {
        this.praiseNum = praiseNum;
    }

    public int getOffenseNum() {
        return offenseNum;
    }

    public void setOffenseNum(int offenseNum) {
        this.offenseNum = offenseNum;
    }

    public int getPraised() {
        return praised;
    }

    public void setPraised(int praised) {
        this.praised = praised;
    }

    public int getFavorited() {
        return favorited;
    }

    public void setFavorited(int favorited) {
        this.favorited = favorited;
    }

    public int getReadNum() {
        return readNum;
    }

    public void setReadNum(int readNum) {
        this.readNum = readNum;
    }

    public int getShareNum() {
        return shareNum;
    }

    public void setShareNum(int shareNum) {
        this.shareNum = shareNum;
    }

    public int getSaveNum() {
        return saveNum;
    }

    public void setSaveNum(int saveNum) {
        this.saveNum = saveNum;
    }

    public long getVisibleTime() {
        return visibleTime;
    }

    public void setVisibleTime(long visibleTime) {
        this.visibleTime = visibleTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
