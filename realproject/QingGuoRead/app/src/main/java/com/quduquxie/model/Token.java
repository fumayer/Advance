package com.quduquxie.model;

import java.io.Serializable;

/**
 * Created on 16/12/6.
 * Created by crazylei.
 */

public class Token implements Serializable {
    public int is_refresh;
    public String token;

    @Override
    public String toString() {
        return "Token{" +
                "is_refresh=" + is_refresh +
                ", token='" + token + '\'' +
                '}';
    }
}