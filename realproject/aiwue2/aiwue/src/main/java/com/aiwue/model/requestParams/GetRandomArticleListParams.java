package com.aiwue.model.requestParams;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *   获取随机文章列表接口参数
 *   created By Yibao on 2017年4月11日19:32:02
 *   Copyright (C) 2017 aiwue.com. All right reserved
 */

public class GetRandomArticleListParams extends BaseParams{
    @SerializedName("channel") @Expose
    private  Integer channel;
    @SerializedName("pSize") @Expose
    private  Integer pSize;

    @SerializedName("lastVisitTime") @Expose
    private Long lastVisitTime;

    public GetRandomArticleListParams(Integer channel,Integer pSize, Long lastVisitTime) {
        this.channel = channel;
        this.pSize = pSize;
        this.lastVisitTime = lastVisitTime;
    }

    public Integer getChannel() {
        return channel;
    }

    public void setChannel(Integer channel) {
        this.channel = channel;
    }

    public Long getLastVisitTime() {
        return lastVisitTime;
    }

    public void setLastVisitTime(Long lastVisitTime) {
        this.lastVisitTime = lastVisitTime;
    }

    public Integer getpSize() {
        return pSize;
    }

    public void setpSize(Integer pSize) {
        this.pSize = pSize;
    }
}
