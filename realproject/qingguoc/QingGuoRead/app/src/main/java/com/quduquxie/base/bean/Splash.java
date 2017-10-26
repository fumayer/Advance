package com.quduquxie.base.bean;

import java.io.Serializable;

/**
 * Created on 17/7/12.
 * Created by crazylei.
 */

public class Splash implements Serializable {

    public String type;
    public Book book;

    @Override
    public String toString() {
        return "Splash{" +
                "type='" + type + '\'' +
                ", book=" + book +
                '}';
    }
}