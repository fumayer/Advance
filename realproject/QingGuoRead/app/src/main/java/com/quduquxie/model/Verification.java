package com.quduquxie.model;

import java.io.Serializable;

/**
 * Created on 16/12/1.
 * Created by crazylei.
 */

public class Verification implements Serializable {

    public int expire_sec;
    public String sms;

    @Override
    public String toString() {
        return "Verification{" +
                "expire_sec=" + expire_sec +
                ", sms='" + sms + '\'' +
                '}';
    }
}
