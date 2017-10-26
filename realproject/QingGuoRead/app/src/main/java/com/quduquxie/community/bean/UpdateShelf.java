package com.quduquxie.community.bean;

import java.io.Serializable;

/**
 * Created on 17/7/28.
 * Created by crazylei.
 */

public class UpdateShelf implements Serializable {
    public int count;

    @Override
    public String toString() {
        return "UpdateShelf{" +
                "count=" + count +
                '}';
    }
}