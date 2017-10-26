package com.quduquxie.model;

import java.io.Serializable;

/**
 * Created on 16/12/30.
 * Created by crazylei.
 */

public class Alias implements Serializable {
    public String alias;

    @Override
    public String toString() {
        return "Alias{" +
                "alias='" + alias + '\'' +
                '}';
    }
}
