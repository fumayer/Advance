package com.newsdemo.model.http.api;

import com.newsdemo.model.bean.GankItemBean;
import com.newsdemo.model.bean.GankSearchItemBean;
import com.newsdemo.model.http.response.GankHttpResponse;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by jianqiang.hu on 2017/5/11.
 */

public interface GankApis {
    String HOST="http://gank.io/api/";

    /**
     * 技术文章列表
     * @param tech
     * @param num
     * @param page
     * @return
     */
    @GET("data/{tech}/{num}/{page}")
    Flowable<GankHttpResponse<List<GankItemBean>>> getTechList(@Path("tech") String tech, @Path("num") int num, @Path("page") int page);

    /**
     * 妹纸列表
     * @param num
     * @param page
     * @return
     */
    @GET("data/福利/{num}/{page}")
    Flowable<GankHttpResponse<List<GankItemBean>>>  getGirlList(@Path("num") int num,@Path("page") int page);

    /**
     * 随机妹纸图
     * @param num
     * @return
     */
    @GET("random/data/福利/{num}")
    Flowable<GankHttpResponse<List<GankItemBean>>> getRandomGirl(@Path("num") int num);

    /**
     * 搜索
     * @param query
     * @param type
     * @param num
     * @param page
     * @return
     */
    @GET("search/query/{query}/category/{type}/count/{count}/page/{page}")
    Flowable<GankHttpResponse<List<GankSearchItemBean>>> getSearchList(@Path("query") String query,@Path("type") String type,@Path("count") int num,@Path("page") int page);


}
