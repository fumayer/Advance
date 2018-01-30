package com.aiwue.okhttp;

import com.aiwue.model.Article;
import com.aiwue.model.Banner;
import com.aiwue.model.Comment;
import com.aiwue.model.Note;
import com.aiwue.model.NullResult;
import com.aiwue.model.RecommendFriends;
import com.aiwue.model.User;
import com.aiwue.model.VCodeResult;
import com.aiwue.model.response.ResultResponse;

import java.util.List;
import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 *  okhttp服务器接口
 */
public interface AiwueService {

    /*爱武艺代码 start*/

    //发送验证码
    @GET("/v1/vcode/sendvcode")
    Observable<ResultResponse<VCodeResult>> sendVCode(@Query("destination") String destination, @Query("operation") Integer operation);

    //验证码登录
    @GET("/v1/user/login/vcode")
    Observable<ResultResponse<User>> loginWithVCode(@Query("account") String account, @Query("verifyId") Integer verifyId, @Query("verifyCode") String verifyCode, @Query("deviceName") String deviceName);

    //账号密码登录
    @GET("/v1/user/login/normal")
    Observable<ResultResponse<User>> loginWithNormal(@Query("account") String account, @Query("password") String password, @Query("ipAddr") String ipAddr);

    //修改密码
    @POST("/v1/user/login/normal")
    Observable<ResultResponse<NullResult>> resetPassword(@Query("newpwd") String newpwd);

    //第三方登录
    @GET("/v1/user/tlogin")
    Observable<ResultResponse<User>> thirdPartyLogin(@Query("openId") String openId, @Query("type") Integer type, @Query("openAccessToken") String openAccessToken, @Query("ipAddr") String ipAddr);

    //返回登录用户信息
    @GET("/v1/user/info")
    Observable<ResultResponse<User>> getUserInfo();

    //返回其它用户信息
    @GET("/v1/user/other/info")
    Observable<ResultResponse<User>> getOtherUserInfo(@Query("oUserId") Integer oUserId, @Query("nickName") String nickName);

    //随机获取文章列表
    @GET("/v1/article/rlist")
    Observable<ResultResponse<List<Article>>> getRandomArticleList(@QueryMap Map<String, String> params);

    //获取文章详情
    @GET("/v1/article/detail/{articleId}")
    Observable<ResultResponse<Article>> getArticleDetail(@Path("articleId") Integer articleId);

    //获取动态（帖子）详情
    @GET("/feed/{pId}")
    Observable<ResultResponse<Article>> getDynaimicDetail(@Path("articleId") Integer articleId);


    //获取评论列表
    @GET("/v1/commens/list/{type}/{id}")
    Observable<ResultResponse<List<Comment>>> getCommentList(@Path("type") Integer type, @Path("id") Integer id, @Query("pIndex") Integer pIndex, @Query("pSize") Integer pSize );

    //获取笔记列表
    @POST("/v1/bbs/note/list")
    Observable<ResultResponse<List<Note>>> getNoteList(@QueryMap Map<String, String> params);
    //获取banner列表
    @POST("/v1/banner/list/{type}")
    Observable<ResultResponse<List<Banner>>> getBannerList(@Path("type") Integer type);

    //获取推荐武友列表
    @GET("/v1/fans/recommend/list")
    Observable<ResultResponse<List<RecommendFriends>>> getRecFriendsList(@QueryMap Map<String, String> params);


    /*爱武艺代码 end*/
}
