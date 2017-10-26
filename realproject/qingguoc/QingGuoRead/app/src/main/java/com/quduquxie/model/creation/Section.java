package com.quduquxie.model.creation;

import com.quduquxie.bean.PublicVO;

import java.io.Serializable;

/**
 * Created on 17/4/10.
 * Created by crazylei.
 */

public class Section extends PublicVO implements Serializable {
    public String id;
    public String name;
    public String status;
    public String book_id;
    public String content;
    public String check_status;
    public String check_message;

    public int serial_number;

    public long word_count;
    public long update_time;

    public int item_type;

}