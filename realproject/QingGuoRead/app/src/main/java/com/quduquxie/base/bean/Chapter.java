package com.quduquxie.base.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created on 17/7/18.
 * Created by crazylei.
 */

public class Chapter implements Serializable {

    public String id;
    public String name;
    public String status;
    public String content;
    public String book_id;

    public int sn;
    public int sequence = -1;
    public int index_start;

    public long create_time;
    public long word_count;

    public ArrayList<String> chapterNameList;

    @Override
    public String toString() {
        return "Chapter{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", content='" + content + '\'' +
                ", book_id='" + book_id + '\'' +
                ", sn=" + sn +
                ", sequence=" + sequence +
                ", index_start=" + index_start +
                ", create_time=" + create_time +
                ", word_count=" + word_count +
                '}';
    }
}