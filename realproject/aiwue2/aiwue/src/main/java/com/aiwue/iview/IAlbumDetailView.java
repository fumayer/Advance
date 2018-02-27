package com.aiwue.iview;

import com.aiwue.model.Article;

/**
 *  文章详情View 接口
 * Created by Yibao on 2017年4月12日14:36:55
 * Copyright (c) 2017 aiwue.com All rights reserved
 */
public interface IAlbumDetailView extends IBaseDetailView{
    void onGetAlbumDetailSuccess(Boolean success, String err, Article response);
}
