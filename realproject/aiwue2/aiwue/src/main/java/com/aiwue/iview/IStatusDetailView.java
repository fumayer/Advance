package com.aiwue.iview;

import com.aiwue.model.Article;
import com.aiwue.model.Note;

/** 我的-》setting View 接口
 * Created by chenhui on 2017年4月19日15:44:53
 * Copyright (c) 2017 aiwue.com All rights reserved
 */
public interface IStatusDetailView extends IBaseDetailView {
    void onGetStatusDetailSuccess(Boolean success, String err, Note response);
}
