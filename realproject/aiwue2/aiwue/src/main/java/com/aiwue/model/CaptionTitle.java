package com.aiwue.model;
/**
 *  分类模型
 * Created by Yibao on 2017年4月19日12:20:47
 * Copyright (c) 2017 aiwue.com All rights reserved
 */
public class CaptionTitle {
    private int id; //分类id
    private String name; //分类名称
    private Boolean show; //是否显示
    private Boolean isSelected; //是否选中

    public CaptionTitle(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getShow() {
        return show;
    }

    public void setShow(Boolean show) {
        this.show = show;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }
}
