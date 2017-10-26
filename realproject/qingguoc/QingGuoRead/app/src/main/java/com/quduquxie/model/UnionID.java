package com.quduquxie.model;

import java.io.Serializable;

/**
 * Created on 16/12/21.
 * Created by crazylei.
 */

public class UnionID implements Serializable {
    public String client_id;
    public String openid;
    public String union_id;

    @Override
    public String toString() {
        return "UnionID{" +
                "client_id='" + client_id + '\'' +
                ", openid='" + openid + '\'' +
                ", union_id='" + union_id + '\'' +
                '}';
    }
}
