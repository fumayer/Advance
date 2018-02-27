package com.aiwue.model;
/**
 *  EventBus 消息类
 * Created by Yibao on 17/4/11
 * Copyright (c) 2017 aiwue.com All rights reserved
 */
public class Notice {
    public int type;
    public Object content;

    public Notice(int type) {
        this.type = type;
    }

    public Notice(int type, Object content) {
        this.type = type;
        this.content = content;
    }

    public Notice() {
    }
}
