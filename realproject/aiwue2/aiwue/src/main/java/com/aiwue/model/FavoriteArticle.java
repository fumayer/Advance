package com.aiwue.model;

/**
 * Created by 44548 on 2016/5/29.
 */
public class FavoriteArticle extends Article {

    private int favoriteId; //收藏id
    private long favoriteTime; //收藏时间

    public int getFavoriteId() {
        return favoriteId;
    }

    public void setFavoriteId(int favoriteId) {
        this.favoriteId = favoriteId;
    }

    public long getFavoriteTime() {
        return favoriteTime;
    }

    public void setFavoriteTime(long favoriteTime) {
        this.favoriteTime = favoriteTime;
    }

}
