package com.aiwue.model;

import java.io.Serializable;

/**
 * created by Yibao on 2016年9月18日12:16:56
 */
public class ArticleChannels implements Serializable {
    private String name;
    private Integer check;
    private String id;

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCheck() {
        return check;
    }

    public void setCheck(Integer check) {
        this.check = check;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArticleChannels() {
    }
    public ArticleChannels(String title, Integer check, String s) {
        this.name = title;
        this.check = check;
        this.id = s;
    }


    @Override
    public String toString() {
        return "Channel{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", check='" + check + '\'' +
                '}';
    }

}
