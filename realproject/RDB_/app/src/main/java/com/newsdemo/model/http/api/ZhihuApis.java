package com.newsdemo.model.http.api;

import com.newsdemo.model.bean.CommentBean;
import com.newsdemo.model.bean.DailyBeforeListBean;
import com.newsdemo.model.bean.DailyListBean;
import com.newsdemo.model.bean.DetailExtraBean;
import com.newsdemo.model.bean.HotListBean;
import com.newsdemo.model.bean.SectionChildListBean;
import com.newsdemo.model.bean.SectionListBean;
import com.newsdemo.model.bean.ThemeChildListBean;
import com.newsdemo.model.bean.ThemeListBean;
import com.newsdemo.model.bean.WelcomeBean;
import com.newsdemo.model.bean.ZhihuDetailBean;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by jianqiang.hu on 2017/5/11.
 */

public interface ZhihuApis {
    String HOST = "http://news-at.zhihu.com/api/4/";

    /**
     * 启动界面图片
     */
    @GET("start-image/{res}")
    Flowable<WelcomeBean> getWelcomeInfo(@Path("res") String res);


    /**
     * 最新日报
     */
    @GET("news/latest")
    Flowable<DailyListBean> getDailyList();

    /**
     * 往期日报
     */
    @GET("news/before/{date}")
    Flowable<DailyBeforeListBean> getDailyBeforeList(@Path("date") String date);

    /**
     * 主题日报
     */
    @GET("themes")
    Flowable<ThemeListBean> getThemeList();

    /**
     * 主题日报详情
     */
    @GET("theme/{id}")
    Flowable<ThemeChildListBean> getThemeChildList(@Path("id") int id);


    /**
     * 专栏日报
     */
    @GET("sections")
    Flowable<SectionListBean> getSectionList();

    /**
     * 专栏日报详情
     */
    @GET("section/{id}")
    Flowable<SectionChildListBean> getSectionChildList(@Path("id") int id);


    /**
     * 热门日报
     */
    @GET("news/hot")
    Flowable<HotListBean> getHotList();

    /**
     * 日报详情
     */
    @GET("news/{id}")
    Flowable<ZhihuDetailBean> getDetailInfo(@Path("id") int id);

    /**
     * 日报的额外信息
     */
    @GET("story-extra/{id}")
    Flowable<DetailExtraBean> getDetailExtraInfo(@Path("id") int id);

    /**
     * 日报的长评论
     */
    @GET("story/{id}/long-comments")
    Flowable<CommentBean> getLongCommentInfo(@Path("id") int id);

    /**
     * 日报的短评论
     */
    @GET("story/{id}/short-comments")
    Flowable<CommentBean> getShortCommentInfo(@Path("id") int id);
}
