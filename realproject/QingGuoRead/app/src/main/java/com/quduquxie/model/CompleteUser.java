package com.quduquxie.model;

import java.io.Serializable;

/**
 * Created on 17/1/3.
 * Created by crazylei.
 */

public class CompleteUser implements Serializable {
    public String penname;
    public String avatar_url;

    @Override
    public String toString() {
        return "CompleteUser{" +
                "penname='" + penname + '\'' +
                ", avatar_url='" + avatar_url + '\'' +
                '}';
    }
}
