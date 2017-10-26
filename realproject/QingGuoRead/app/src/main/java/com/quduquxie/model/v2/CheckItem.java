package com.quduquxie.model.v2;

import java.io.Serializable;

/**
 * Created on 17/4/26.
 * Created by crazylei.
 */

public class CheckItem implements Serializable {
    public String book_id;
    public int serial_number;

    public CheckItem(String book_id, int serial_number) {
        this.book_id = book_id;
        this.serial_number = serial_number;
    }
}
