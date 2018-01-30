package com.aiwue.model;
import java.util.List;

/**
 *  笔记模型
 * Created by Yibao on 2017年4月19日14:18:36
 * Copyright (c) 2017 aiwue.com All rights reserved
 */

public class Note {
    private Integer id;
    private Integer type = 1;// 帖子类型 0：帖子； 1：动态； 2：提问； 3：视频； 4：图集'
    private SimpleUserInfo authorInfo;//作者信息
    private Integer bbsPlateId;// 板块id
    private LocationInfo locationInfo; ///地点信息
    private List<NoteParagraph> pgraphList; /// Note 媒体（章节）模型数组
    private List<AtUserInfo> atUserList;// 被at的用户模型数组
    private String clientType;// app客户端 "iphone 7" ...
    private String headPicName; //题图
    private String title;//标题
    private Integer readNum = 0;/// 浏览量
    private Integer commentNum = 0;// 评论数量
    private Integer praiseNum = 0;/// 赞数量
    private Integer praised = 0;/// 当前浏览者是否已经赞, 0:没有 ，1：有
    private Integer favorited = 0;   //当前浏览者是否已经收藏
    private String pId; ///pageId，也是静态页面的文件名
    private Long createTime; /// 发布时间 - 时间戳

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public SimpleUserInfo getAuthorInfo() {
        return authorInfo;
    }

    public void setAuthorInfo(SimpleUserInfo authorInfo) {
        this.authorInfo = authorInfo;
    }

    public Integer getBbsPlateId() {
        return bbsPlateId;
    }

    public void setBbsPlateId(Integer bbsPlateId) {
        this.bbsPlateId = bbsPlateId;
    }

    public LocationInfo getLocationInfo() {
        return locationInfo;
    }

    public void setLocationInfo(LocationInfo locationInfo) {
        this.locationInfo = locationInfo;
    }

    public List<NoteParagraph> getPgraphList() {
        return pgraphList;
    }

    public void setPgraphList(List<NoteParagraph> pgraphList) {
        this.pgraphList = pgraphList;
    }

    public List<AtUserInfo> getAtUserList() {
        return atUserList;
    }

    public void setAtUserList(List<AtUserInfo> atUserList) {
        this.atUserList = atUserList;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getHeadPicName() {
        return headPicName;
    }

    public void setHeadPicName(String headPicName) {
        this.headPicName = headPicName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getReadNum() {
        return readNum;
    }

    public void setReadNum(Integer readNum) {
        this.readNum = readNum;
    }

    public Integer getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(Integer commentNum) {
        this.commentNum = commentNum;
    }

    public Integer getPraiseNum() {
        return praiseNum;
    }

    public void setPraiseNum(Integer praiseNum) {
        this.praiseNum = praiseNum;
    }

    public Integer getPraised() {
        return praised;
    }

    public void setPraised(Integer praised) {
        this.praised = praised;
    }

    public Integer getFavorited() {
        return favorited;
    }

    public void setFavorited(Integer favorited) {
        this.favorited = favorited;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
}
