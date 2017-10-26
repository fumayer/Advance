package com.quduquxie.base.bean;

import java.io.Serializable;

/**
 * Created on 17/7/28.
 * Created by crazylei.
 */

public class Update implements Serializable {

    public int code;

    public String url;
    public String name;
    public String desc;

    public boolean force;
    public boolean update;

    @Override
    public String toString() {
        return "Update{" +
                "name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", update=" + update +
                ", force=" + force +
                ", code=" + code +
                ", url='" + url + '\'' +
                '}';
    }
}
