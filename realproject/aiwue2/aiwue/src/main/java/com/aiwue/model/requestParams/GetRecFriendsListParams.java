package com.aiwue.model.requestParams;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * 获取推荐武友参数
 * Created by Administrator on 2017/5/2.
 */

public class GetRecFriendsListParams extends BaseParams{
    @SerializedName("pIndex") @Expose
    private Integer pIndex;
    @SerializedName("pSize") @Expose
    private  Integer pSize;



    public GetRecFriendsListParams(Integer pIndex,Integer pSize) {
        this.pIndex = pIndex;
        this.pSize = pSize;
    }

    public Integer getpIndex() {
        return pIndex;
    }

    public void setpIndex(Integer pIndex) {
        this.pIndex = pIndex;
    }

    public Integer getpSize() {
        return pSize;
    }

    public void setpSize(Integer pSize) {
        this.pSize = pSize;
    }
}
