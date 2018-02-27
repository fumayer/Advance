package com.aiwue.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by Administrator on 2017/1/5 0005.
 */

public class Channel implements MultiItemEntity {
    public static final int TYPE_MY = 1;
    public static final int TYPE_OTHER = 2;
    public static final int TYPE_MY_CHANNEL = 3;
    public static final int TYPE_OTHER_CHANNEL = 4;
    public String Title;
    public int  itemType;

    public Channel(int type, String title) {
        Title = title;
        itemType = type;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
