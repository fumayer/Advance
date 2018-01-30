package com.aiwue.model;

/**
 * 收藏笔记模型
 * Created by Yibao on 2017年4月19日14:18:36
 * Copyright (c) 2017 aiwue.com All rights reserved
 */

public class FavoriteNote extends Note {
    private Integer favoriteId = 0;//收藏id
    private Long favoriteTime;//收藏时间

    public Integer getFavoriteId() {
        return favoriteId;
    }

    public void setFavoriteId(Integer favoriteId) {
        this.favoriteId = favoriteId;
    }

    public Long getFavoriteTime() {
        return favoriteTime;
    }

    public void setFavoriteTime(Long favoriteTime) {
        this.favoriteTime = favoriteTime;
    }
}
