package com.quduquxie.model;

import java.io.Serializable;

/**
 * Created on 16/12/7.
 * Created by crazylei.
 */

public class Avatar implements Serializable {
    public String avatar_url;

    @Override
    public String toString() {
        return "Avatar{" +
                "avatar_url='" + avatar_url + '\'' +
                '}';
    }
}
