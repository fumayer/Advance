package com.aiwue.model;

/**
 * 评论model
 * Created by Yibao on 2017年4月12日18:44:01
 * Copyright (c) 2017 aiwue.com All rights reserved
 */
public class Comment {
    private int id;
    private SimpleUserInfo authorInfo;//评论人信息
    private int type = 0; //被评论内容类型
    private int ownerId = 0; //被评论内容id

    private String content; //评论内容
    private int createTime;//评论时间
    private int commentNum = 0; //该评论的评论数
    private int praiseNum = 0;//点赞数
    private int offenseNum = 0;//举报数
    private int praised = 0; //是否点赞
    private int favorited = 0; //当前登录用户是否收藏
    private int readNum = 0; //阅读数
    private int shareNum= 0;//分享数
    private int saveNum= 0; //收藏数

    private String pId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public SimpleUserInfo getAuthorInfo() {
        return authorInfo;
    }

    public void setAuthorInfo(SimpleUserInfo authorInfo) {
        this.authorInfo = authorInfo;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getCreateTime() {
        return createTime;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
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

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }
}
