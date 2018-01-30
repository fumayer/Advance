package com.aiwue.model.requestParams;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 获取热点banner接口参数
 * Created by Administrator on 2017/4/28.
 */

public class GetBannerParams extends BaseParams{
    @SerializedName("types") @Expose
    private Integer  types;

    public Integer getTypes() {
        return types;
    }

    public void setTypes(Integer types) {
        this.types = types;
    }
}
