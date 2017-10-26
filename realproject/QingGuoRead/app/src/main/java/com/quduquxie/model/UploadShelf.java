package com.quduquxie.model;

import java.io.Serializable;

/**
 * Created on 16/12/28.
 * Created by crazylei.
 */

public class UploadShelf implements Serializable {
    public int is_self;
    public String book_id;
    public int word_offset;
    public String book_name;
    public int serial_number;

    @Override
    public String toString() {
        return "UploadShelf{" +
                "is_self=" + is_self +
                ", book_id='" + book_id + '\'' +
                ", word_offset=" + word_offset +
                ", book_name='" + book_name + '\'' +
                ", serial_number=" + serial_number +
                '}';
    }
}
