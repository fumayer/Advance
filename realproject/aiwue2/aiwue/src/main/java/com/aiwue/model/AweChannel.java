package com.aiwue.model;

import java.io.Serializable;

/**
 * Created by 44548 on 2016/5/29.
 */
public class AweChannel implements Serializable {
    private String name;
    private String id;

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public AweChannel(String title, String s) {
        this.name = title;
        id = s;
    }



    @Override
    public String toString() {
        return "Channel{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                '}';
    }

}
