package com.quduquxie.model;

import java.io.Serializable;

/**
 * Created on 16/12/6.
 * Created by crazylei.
 */

public class ReviseUser implements Serializable {
    public String token;
    public String user_name;

    @Override
    public String toString() {
        return "ReviseUser{" +
                "token='" + token + '\'' +
                ", user_name='" + user_name + '\'' +
                '}';
    }
}
