package com.aiwue.iview;

import com.aiwue.model.Article;
import com.aiwue.model.Banner;
import com.aiwue.model.RecommendFriends;

import java.util.List;

/**
 * Created by Administrator on 2017/4/28.
 */

public interface IHotspotView  {
    void onGetBannerListSuccess(Boolean success, String err, List<Banner> response);
    void onGetArticleListSuccess(Boolean success, String err, List<Article> response);
    void onGetRecFriendsListSuccess(Boolean success, String err, List<RecommendFriends> response);
}
