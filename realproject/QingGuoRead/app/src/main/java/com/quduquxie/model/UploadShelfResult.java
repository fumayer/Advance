package com.quduquxie.model;

import java.io.Serializable;

/**
 * Created on 16/12/28.
 * Created by crazylei.
 */

public class UploadShelfResult implements Serializable {
    public int count;

    @Override
    public String toString() {
        return "UploadShelfResult{" +
                "count=" + count +
                '}';
    }
}
