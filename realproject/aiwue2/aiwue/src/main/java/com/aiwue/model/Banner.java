package com.aiwue.model;

import java.io.Serializable;
import java.util.List;

/**
 * banner model
 * Created by Yibao on 2017年4月27日18:36:55
 * Copyright (c) 2017 aiwue.com All rights reserved
 */

public class Banner implements Serializable {

    private int id;
    private String picAlt;
    private String picName;
    private int souType;
    private String title;
    private int type;//banner类型 0:WEB首页；100：-APP首页；101-APP课程页；
    private int souId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPicAlt() {
        return picAlt;
    }

    public void setPicAlt(String picAlt) {
        this.picAlt = picAlt;
    }

    public String getPicName() {
        return picName;
    }

    public void setPicName(String picName) {
        this.picName = picName;
    }

    public int getSouType() {
        return souType;
    }

    public void setSouType(int souType) {
        this.souType = souType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSouId() {
        return souId;
    }

    public void setSouId(int souId) {
        this.souId = souId;
    }
}
