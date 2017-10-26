package com.quduquxie.model.creation;

import java.io.Serializable;

/**
 * Created on 17/4/10.
 * Created by crazylei.
 */
public class Literature implements Serializable {

    public String id;
    public String name;
    public String style;
    public String status;
    public String ending;
    public String fenpin;
    public String category;
    public String image_url;
    public String attribute;
    public String description;

    public int is_sign;
    public int serial_number;

    public long word_count;

    public Section chapter;

    //展示用临时字段
    public int item_type;
}
