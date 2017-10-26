package com.quduquxie.model;

import java.io.Serializable;

/**
 * Created on 16/12/29.
 * Created by crazylei.
 */

public class Fiction implements Serializable {
    public String attribute;
    public String category;
    public Chapter chapter;
    public String description;
    public String ending;
    public String id;
    public String image_url;
    public String name;
    public String status;
    public String style;
    public long word_count;
    public User user;

    public int is_sign;

    public String fenpin;

    @Override
    public String toString() {
        return "BookShelf{" +
                "attribute='" + attribute + '\'' +
                ", category='" + category + '\'' +
                ", chapter=" + chapter +
                ", description='" + description + '\'' +
                ", ending='" + ending + '\'' +
                ", id='" + id + '\'' +
                ", image_url='" + image_url + '\'' +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", style='" + style + '\'' +
                ", word_count=" + word_count +
                ", user=" + user +
                '}';
    }
}
