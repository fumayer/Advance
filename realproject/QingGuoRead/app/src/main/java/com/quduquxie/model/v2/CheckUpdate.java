package com.quduquxie.model.v2;

import com.quduquxie.base.bean.Chapter;

import java.io.Serializable;

/**
 * Created on 17/4/25.
 * Created by crazylei.
 */

public class CheckUpdate implements Serializable {
    public String id;
    public String name;
    public String description;
    public String attribute;
    public int is_sign;
    public int is_copyright;
    public String category;
    public String style;
    public String ending;
    public String image;
    public String channel;
    public int word_count;
    public int read_count;
    public int click_count;

    public User author;
    public Chapter chapter;
}
