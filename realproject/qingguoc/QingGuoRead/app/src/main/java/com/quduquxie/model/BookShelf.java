package com.quduquxie.model;

import java.io.Serializable;

/**
 * Created on 16/12/29.
 * Created by crazylei.
 */

public class BookShelf implements Serializable {
    public Fiction book;
    public int position_number;
    public int serial_number;
    public int word_offset;

    @Override
    public String toString() {
        return "BookShelf{" +
                "book=" + book +
                ", position_number=" + position_number +
                ", serial_number=" + serial_number +
                ", word_offset=" + word_offset +
                '}';
    }
}
