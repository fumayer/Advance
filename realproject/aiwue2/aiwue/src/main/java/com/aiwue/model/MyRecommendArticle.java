package com.aiwue.model;

import com.aiwue.R;
import com.aiwue.base.AiwueApplication;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/9/21.
 */
public class MyRecommendArticle  extends Article {

    private Integer[] statusList = {R.string.my_recommendation_status_wait_for_process,
            R.string.my_recommendation_status_in_processing,
            R.string.my_recommendation_status_process_failure,
            R.string.my_recommendation_status_audit_failure,
            R.string.my_recommendation_status_published,
            R.string.my_recommendation_status_deleted};

    private Integer status;// //用于我的推荐列表
    private String  recommendNote; //推荐理由
    private Long createTime;

    private String recommendTime;
    private String statusStr;


    public String getRecommendNote() {
        return recommendNote;
    }

    public void setRecommendNote(String recommendNote) {
        this.recommendNote = recommendNote;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getRecommendTime() {
        if (createTime != null) {
            SimpleDateFormat my = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
            return my.format(new Date(createTime));
        }
        return "";
    }

    public String getStatusStr() {
        if (status < 0 || status >= statusList.length)
            return AiwueApplication.getAppContext().getString(statusList[0]);
        return AiwueApplication.getAppContext().getString(statusList[status]);
    }
}
