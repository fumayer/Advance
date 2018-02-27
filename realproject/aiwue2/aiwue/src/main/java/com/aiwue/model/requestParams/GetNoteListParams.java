package com.aiwue.model.requestParams;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *   获取笔记列表接口参数
 *   created By Yibao on 2017年4月19日14:37:44
 *   Copyright (C) 2017 aiwue.com. All right reserved
 */

public class GetNoteListParams extends BaseParams{
    @SerializedName("pIndex") @Expose
    private  Integer pIndex;
    @SerializedName("pSize") @Expose
    private Integer pSize;
    @SerializedName("plateId") @Expose
    private Integer plateId;
    @SerializedName("tuserId") @Expose
    private Integer tuserId;
    @SerializedName("type") @Expose
    private Integer type;
    @SerializedName("categoryId") @Expose
    private Integer categoryId;
    @SerializedName("isPublic") @Expose
    private Integer isPublic;
    @SerializedName("parentId") @Expose
    private Integer parentId;
    @SerializedName("isGood") @Expose
    private Integer isGood;

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

    public Integer getPlateId() {
        return plateId;
    }

    public void setPlateId(Integer plateId) {
        this.plateId = plateId;
    }

    public Integer getTuserId() {
        return tuserId;
    }

    public void setTuserId(Integer tuserId) {
        this.tuserId = tuserId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Integer isPublic) {
        this.isPublic = isPublic;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getIsGood() {
        return isGood;
    }

    public void setIsGood(Integer isGood) {
        this.isGood = isGood;
    }

}
