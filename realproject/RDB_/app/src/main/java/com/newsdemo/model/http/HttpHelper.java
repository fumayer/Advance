package com.newsdemo.model.http;

import com.newsdemo.model.bean.CommentBean;
import com.newsdemo.model.bean.DailyBeforeListBean;
import com.newsdemo.model.bean.DailyListBean;
import com.newsdemo.model.bean.DetailExtraBean;
import com.newsdemo.model.bean.GankItemBean;
import com.newsdemo.model.bean.GankSearchItemBean;
import com.newsdemo.model.bean.GoldListBean;
import com.newsdemo.model.bean.HotListBean;
import com.newsdemo.model.bean.NodeBean;
import com.newsdemo.model.bean.NodeListBean;
import com.newsdemo.model.bean.RepliesListBean;
import com.newsdemo.model.bean.SectionChildListBean;
import com.newsdemo.model.bean.SectionListBean;
import com.newsdemo.model.bean.ThemeChildListBean;
import com.newsdemo.model.bean.ThemeListBean;
import com.newsdemo.model.bean.VersionBean;
import com.newsdemo.model.bean.WXItemBean;
import com.newsdemo.model.bean.WelcomeBean;
import com.newsdemo.model.bean.ZhihuDetailBean;
import com.newsdemo.model.http.response.GankHttpResponse;
import com.newsdemo.model.http.response.GoldHttpResponse;
import com.newsdemo.model.http.response.MyHttpResponse;
import com.newsdemo.model.http.response.WXHttpResponse;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by jianqiang.hu on 2017/5/11.
 */

public interface HttpHelper {
    Flowable<DailyListBean> fetchDailyListInfo();

    Flowable<DailyBeforeListBean> fetchDailyBeforeListInfo(String date);

    Flowable<ThemeListBean> fetchDailyThemeListInfo();

    Flowable<ThemeChildListBean> fetchThemeChildListInfo(int id);

    Flowable<SectionListBean> fetchSectionListInfo();

    Flowable<SectionChildListBean> fetchSectionChildListInfo(int id);

    Flowable<ZhihuDetailBean> fetchDetailInfo(int id);

    Flowable<DetailExtraBean> fetchDetailExtraInfo(int id);

    Flowable<WelcomeBean> fetchWelcomeInfo(String res);

    Flowable<CommentBean> fetchLongCommentInfo(int id);

    Flowable<CommentBean> fetchShortCommentInfo(int id);

    Flowable<HotListBean> fetchHotListInfo();

    Flowable<GankHttpResponse<List<GankItemBean>>> fetchTechList(String tech, int num, int page);

    Flowable<GankHttpResponse<List<GankItemBean>>> fetchGirlList(int num, int page);

    Flowable<GankHttpResponse<List<GankItemBean>>> fetchRandomGirl(int num);

    Flowable<GankHttpResponse<List<GankSearchItemBean>>> fetchGankSearchList(String query, String type, int num, int page);

    Flowable<WXHttpResponse<List<WXItemBean>>> fetchWechatListInfo(int num, int page);

    Flowable<WXHttpResponse<List<WXItemBean>>> fetchWechatSearchListInfo(int num, int page, String word);

    Flowable<MyHttpResponse<VersionBean>> fetchVersionInfo();

    Flowable<GoldHttpResponse<List<GoldListBean>>> fetchGoldList(String type, int num, int page);

    Flowable<GoldHttpResponse<List<GoldListBean>>> fetchGoldHotList(String type, String dataTime, int limit);

    Flowable<NodeBean> fetchNodeInfo(String name);

    Flowable<List<NodeListBean>> fetchTopicList(String name);

    Flowable<List<NodeListBean>> fetchTopicInfo(String id);

    Flowable<List<RepliesListBean>> fetchRepliesList(String id);
}
