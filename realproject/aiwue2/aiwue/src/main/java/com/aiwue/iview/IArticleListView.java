package com.aiwue.iview;

import com.aiwue.model.Article;

import java.util.List;

/**
 * Created by Administrator on 2016/11/17 0017.
 */
public interface IArticleListView {
    void onGetArticleListSuccess(Boolean success, String err, List<Article> response);

}
