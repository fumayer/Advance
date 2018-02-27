package com.aiwue.model;

/**
 *  笔记内容的段落模型
 * Created by Yibao on 2017年4月19日14:18:36
 * Copyright (c) 2017 aiwue.com All rights reserved
 */

public class NoteParagraph {
    private String type = "img";// 媒体类型 "img"：图片； "video"：视频； "audio"：音频；
    private String title;//标题
    private String cover;//封面
    private String media;//媒体文件名
    private Integer length=0; //媒体的大小， 视频、音频：长度为秒；图片：无用
    private Float aspectRatio=Float.valueOf((float)16.0/9); //视频的宽高比
    private String content; // 文本内容

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Float getAspectRatio() {
        return aspectRatio;
    }

    public void setAspectRatio(Float aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
