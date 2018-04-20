package com.newsdemo.model.http;


import com.newsdemo.app.Constants;
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
import com.newsdemo.model.http.api.GankApis;
import com.newsdemo.model.http.api.GoldApis;
import com.newsdemo.model.http.api.MyApis;
import com.newsdemo.model.http.api.VtexApis;
import com.newsdemo.model.http.api.WeChatApis;
import com.newsdemo.model.http.api.ZhihuApis;
import com.newsdemo.model.http.response.GankHttpResponse;
import com.newsdemo.model.http.response.GoldHttpResponse;
import com.newsdemo.model.http.response.MyHttpResponse;
import com.newsdemo.model.http.response.WXHttpResponse;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;

/**
 * Created by jianqiang.hu on 2017/5/11.
 */

public class RetrofitHelper implements HttpHelper{
    private ZhihuApis mZhihuApiService;
    private GankApis mGankApiService;
    private WeChatApis mWechatApiService;
    private MyApis mMyApiService;
    private GoldApis mGoldApiService;
    private VtexApis mVtexApiService;


    /**
     * apis在HttpModule中实例化，所以这里可以用inject直接注入
     * @param zhihuApiService
     * @param gankApiService
     * @param wechatApiService
     * @param myApiService
     * @param goldApiService
     * @param vtexApiService
     */
    @Inject
    public RetrofitHelper(ZhihuApis zhihuApiService, GankApis gankApiService, WeChatApis wechatApiService,
                          MyApis myApiService, GoldApis goldApiService, VtexApis vtexApiService){
        this.mZhihuApiService = zhihuApiService;
        this.mGankApiService = gankApiService;
        this.mWechatApiService = wechatApiService;
        this.mMyApiService = myApiService;
        this.mGoldApiService = goldApiService;
        this.mVtexApiService = vtexApiService;
    }

    @Override
    public Flowable<DailyListBean> fetchDailyListInfo() {
        return mZhihuApiService.getDailyList();
    }

    @Override
    public Flowable<DailyBeforeListBean> fetchDailyBeforeListInfo(String date) {
        return mZhihuApiService.getDailyBeforeList(date);
    }

    @Override
    public Flowable<ThemeListBean> fetchDailyThemeListInfo() {
        return mZhihuApiService.getThemeList();
    }

    @Override
    public Flowable<ThemeChildListBean> fetchThemeChildListInfo(int id) {
        return mZhihuApiService.getThemeChildList(id);
    }

    @Override
    public Flowable<SectionListBean> fetchSectionListInfo() {
        return mZhihuApiService.getSectionList();
    }

    @Override
    public Flowable<SectionChildListBean> fetchSectionChildListInfo(int id) {
        return mZhihuApiService.getSectionChildList(id);
    }

    @Override
    public Flowable<ZhihuDetailBean> fetchDetailInfo(int id) {
        return mZhihuApiService.getDetailInfo(id);
    }

    @Override
    public Flowable<DetailExtraBean> fetchDetailExtraInfo(int id) {
        return mZhihuApiService.getDetailExtraInfo(id);
    }

    @Override
    public Flowable<WelcomeBean> fetchWelcomeInfo(String res) {
        return mZhihuApiService.getWelcomeInfo(res);
    }

    @Override
    public Flowable<CommentBean> fetchLongCommentInfo(int id) {
        return mZhihuApiService.getLongCommentInfo(id);
    }

    @Override
    public Flowable<CommentBean> fetchShortCommentInfo(int id) {
        return mZhihuApiService.getShortCommentInfo(id);
    }

    @Override
    public Flowable<HotListBean> fetchHotListInfo() {
        return mZhihuApiService.getHotList();
    }

    @Override
    public Flowable<GankHttpResponse<List<GankItemBean>>> fetchTechList(String tech, int num, int page) {
        return mGankApiService.getTechList(tech, num, page);
    }

    @Override
    public Flowable<GankHttpResponse<List<GankItemBean>>> fetchGirlList(int num, int page) {
        return mGankApiService.getGirlList(num, page);
    }

    @Override
    public Flowable<GankHttpResponse<List<GankItemBean>>> fetchRandomGirl(int num) {
        return mGankApiService.getRandomGirl(num);
    }

    @Override
    public Flowable<GankHttpResponse<List<GankSearchItemBean>>> fetchGankSearchList(String query, String type, int num, int page) {
        return mGankApiService.getSearchList(query,type,num,page);
    }

    @Override
    public Flowable<WXHttpResponse<List<WXItemBean>>> fetchWechatListInfo(int num, int page) {
        return mWechatApiService.getWXHot(Constants.KEY_API, num, page);
    }

    @Override
    public Flowable<WXHttpResponse<List<WXItemBean>>> fetchWechatSearchListInfo(int num, int page, String word) {
        return mWechatApiService.getWXHotSearch(Constants.KEY_API, num, page, word);
    }

    @Override
    public Flowable<MyHttpResponse<VersionBean>> fetchVersionInfo() {
        return mMyApiService.getVersionInfo();
    }

    @Override
    public Flowable<GoldHttpResponse<List<GoldListBean>>> fetchGoldList(String type, int num, int page) {
        return mGoldApiService.getGoldList(Constants.LEANCLOUD_ID, Constants.LEANCLOUD_SIGN,
                "{\"category\":\"" + type + "\"}", "-createdAt", "user,user.installation", num, page * num);
    }

    @Override
    public Flowable<GoldHttpResponse<List<GoldListBean>>> fetchGoldHotList(String type, String dataTime, int limit) {
        return mGoldApiService.getGoldHot(Constants.LEANCLOUD_ID, Constants.LEANCLOUD_SIGN,
                "{\"category\":\"" + type + "\",\"createdAt\":{\"$gt\":{\"__type\":\"Date\",\"iso\":\"" + dataTime + "T00:00:00.000Z\"}},\"objectId\":{\"$nin\":[\"58362f160ce463005890753e\",\"583659fcc59e0d005775cc8c\",\"5836b7358ac2470065d3df62\"]}}",
                "-hotIndex", "user,user.installation", limit);
    }

    @Override
    public Flowable<NodeBean> fetchNodeInfo(String name) {
        return mVtexApiService.getNodeInfo(name);
    }

    @Override
    public Flowable<List<NodeListBean>> fetchTopicList(String name) {
        return mVtexApiService.getTopicList(name);
    }

    @Override
    public Flowable<List<NodeListBean>> fetchTopicInfo(String id) {
        return mVtexApiService.getTopicInfo(id);
    }

    @Override
    public Flowable<List<RepliesListBean>> fetchRepliesList(String id) {
        return mVtexApiService.getRepliesList(id);
    }
}
