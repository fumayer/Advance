package com.quduquxie.base.bean;

import java.io.Serializable;

/**
 * Created on 17/7/28.
 * Created by crazylei.
 */

public class Bookmark implements Serializable {
    public int id;
    public int offset = -1;
    public int sequence = -1;

    public String book_id;
    public String chapter_name;
    public String chapter_content;

    public long insert_time;
}