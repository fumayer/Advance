package com.newsdemo.model.bean;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by jianqiang.hu on 2017/5/11.
 */

public class ReadStateBean extends RealmObject{
    @PrimaryKey
    private int id;

    public ReadStateBean() { }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
