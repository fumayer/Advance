package com.aiwue.model.requestParams;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *   获取笔记列表接口参数
 *   created By Yibao on 2017年4月19日14:37:44
 *   Copyright (C) 2017 aiwue.com. All right reserved
 */

public class GetMyCollectionNoteListParams extends BaseParams{
    @SerializedName("pIndex") @Expose
    private  Integer pIndex;
    @SerializedName("pSize") @Expose
    private Integer pSize;
    @SerializedName("type") @Expose
    private Integer type;

    public Integer getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Integer isPublic) {
        this.isPublic = isPublic;
    }

    @SerializedName("isPublic") @Expose
    private Integer isPublic;


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

    public Integer getType() {return type;}

    public void setType(Integer type) {this.type = type;}

}
