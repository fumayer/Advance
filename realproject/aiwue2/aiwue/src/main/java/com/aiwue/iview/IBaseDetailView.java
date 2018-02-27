package com.aiwue.iview;

import com.aiwue.model.Comment;

import java.util.List;

/**
 *  内容详情View 接口
 * Created by Yibao on 2017年4月12日17:07:19
 * Copyright (c) 2017 aiwue.com All rights reserved
 */
public interface IBaseDetailView {
    void onGetCommentListSuccess(Boolean success, String err, List<Comment> response);
}
