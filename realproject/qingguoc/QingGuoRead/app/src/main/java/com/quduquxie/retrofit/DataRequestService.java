package com.quduquxie.retrofit;

import com.quduquxie.base.bean.Book;
import com.quduquxie.base.bean.Chapter;
import com.quduquxie.model.creation.DraftCreate;
import com.quduquxie.model.creation.DraftDelete;
import com.quduquxie.model.creation.DraftList;
import com.quduquxie.model.creation.DraftPublish;
import com.quduquxie.model.creation.LiteratureDelete;
import com.quduquxie.model.creation.LiteratureRevise;
import com.quduquxie.model.creation.LiteratureTag;
import com.quduquxie.model.creation.SectionList;
import com.quduquxie.model.creation.SectionPublish;
import com.quduquxie.model.creation.LiteratureList;
import com.quduquxie.model.QQ;
import com.quduquxie.model.Review;
import com.quduquxie.model.Alias;
import com.quduquxie.model.Avatar;
import com.quduquxie.model.Comment;
import com.quduquxie.model.CommentList;
import com.quduquxie.model.CompleteUser;
import com.quduquxie.model.DownloadShelfResult;
import com.quduquxie.model.PenName;
import com.quduquxie.model.ReviewResult;
import com.quduquxie.model.RecoveredPassword;
import com.quduquxie.model.RegisterUser;
import com.quduquxie.model.ReviseUser;
import com.quduquxie.model.SearchResult;
import com.quduquxie.model.Token;
import com.quduquxie.model.UploadShelfResult;
import com.quduquxie.model.UserInformation;
import com.quduquxie.model.Verification;
import com.quduquxie.model.v2.SensitiveWord;
import com.quduquxie.retrofit.model.BlanketResult;
import com.quduquxie.retrofit.model.CommunalResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created on 16/12/13.
 * Created by crazylei.
 */

public interface DataRequestService {

    //获取验证码
    @GET("/v1/sns/get_sms")
    Flowable<CommunalResult<Verification>> loadVerificationCode(@Query("user_name") String user_name);

    //获取验证码
    @GET("/v1/sns/get_sms")
    Flowable<CommunalResult<Verification>> loadVerificationCode(@Query("user_name") String user_name, @Query("is_check") String check);

    //用户注册
    @FormUrlEncoded
    @POST("/v1/sns/create_user")
    Flowable<CommunalResult<RegisterUser>> registerUser(@Field("info_sign") String sign);

    //用户登陆
    @FormUrlEncoded
    @POST("/v1/sns/create_token")
    Flowable<CommunalResult<RegisterUser>> landingUser(@Field("info_sign") String sign);

    //用户登陆：微信
    @FormUrlEncoded
    @POST("/v1/sns/create_token/wechat")
    Flowable<CommunalResult<RegisterUser>> landingUserWeChat(@Field("code") String code);

    //用户登陆：获取QQUnionID
    @GET("/oauth2.0/me")
    Flowable<String> loadQQUnionID(@Query("access_token") String access_token);

    //用户登陆：QQ
    @FormUrlEncoded
    @POST("/v1/sns/create_token/qq")
    Flowable<CommunalResult<RegisterUser>> landingUserQQ(@FieldMap Map<String, String> information);

    //补充用户信息
    @Multipart
    @POST("/v1/sns/auth/add_profile")
    Flowable<CommunalResult<CompleteUser>> completeUser(@Part MultipartBody.Part penname, @Part MultipartBody.Part file);

    //获取用户信息
    @GET("/v1/sns/auth/get_user_info")
    Flowable<CommunalResult<UserInformation>> loadUserInformation();

    //绑定电话号码，重新绑定
    @FormUrlEncoded
    @POST("/v1/sns/auth/reset_uname")
    Flowable<CommunalResult<ReviseUser>> reviseTelephoneNumber(@Field("info_sign") String sign);

    //修改密码
    @FormUrlEncoded
    @POST("/v1/sns/auth/reset_pwd")
    Flowable<CommunalResult<Token>> reviserPassword(@Field("info_sign") String sign);

    //绑定QQ号，重新绑定
    @FormUrlEncoded
    @PUT("/v1/sns/auth/user/qq")
    Flowable<CommunalResult<QQ>> reviserQQ(@Field("qq") String qq);

    //找回密码
    @FormUrlEncoded
    @POST("/v1/sns/find_user")
    Flowable<CommunalResult<RecoveredPassword>> recoveredPassword(@Field("info_sign") String sign);

    //修改笔名
    @FormUrlEncoded
    @POST("/v1/sns/auth/update_pname")
    Flowable<CommunalResult<PenName>> reviserPenName(@Field("penname") String penname);

    //修改头像
    @Multipart
    @POST("/v1/sns/auth/update_avatar")
    Flowable<CommunalResult<Avatar>> reviserAvatar(@Part MultipartBody.Part file);

    //刷新Token
    @GET("/v1/sns/auth/load_token")
    Flowable<CommunalResult<Token>> reviserToken();

    //用户退出登陆
    @GET("/v1/sns/auth/invalid_token")
    Flowable<CommunalResult<Alias>> exitLanding();

    //上传用户书架信息
    @POST("/v1/sns/auth/up_shelf")
    @Headers("Content-Type: application/json;charset=UTF-8")
    Flowable<CommunalResult<UploadShelfResult>> uploadUserBookShelf(@Body RequestBody requestBody);

    //下载用户书架信息
    @GET("/v1/sns/auth/down_shelf")
    Flowable<CommunalResult<DownloadShelfResult>> downloadUserBookShelf();

    //小米推送：注册设备
    @FormUrlEncoded
    @POST("/v1/service/register_device")
    Flowable<CommunalResult<Alias>> pushRegisterDevice(@FieldMap Map<String, String> information);

    //小米推送：绑定设备
    @FormUrlEncoded
    @POST("/v1/service/bind_device")
    Flowable<CommunalResult<Alias>> pushBindingDevice(@FieldMap Map<String, String> information);

    //小米推送：激活设备
    @FormUrlEncoded
    @POST("/v1/service/active_device")
    Flowable<CommunalResult> pushActiveDevice(@FieldMap Map<String, String> information);

    //获取书籍评论
    @GET("/v1/sns/books/{id}/comments")
    Flowable<CommunalResult<CommentList>> loadBookComments(@Path("id") String id, @Query("page") int page);

    //获取书籍评论数量
    @GET("/v1/sns/books/{id}/comments")
    Flowable<CommunalResult<CommentList>> loadBookCommentsCount(@Path("id") String id, @Query("page") int page, @Query("fl") String count);

    //发布书籍评论
    @FormUrlEncoded
    @POST("/v1/sns/books/{id}/comments")
    Flowable<CommunalResult<Comment>> publishBookComment(@Path("id") String id, @Field("user_id") String user_id, @Field("content") String content);

    //发布评论回复
    @FormUrlEncoded
    @POST("/v1/sns/books/{id_book}/comments/{id_comment}/replies/")
    Flowable<CommunalResult<Comment>> publishCommentReply(@Path("id_book") String id_book, @Path("id_comment") String id_comment, @Field("receiver_id") String receiver_id, @Field("content") String content);

    //获取评论详情
    @GET("/v1/sns/books/{id_book}/comments/{id_comment}")
    Flowable<CommunalResult<Comment>> loadCommentDetails(@Path("id_book") String id_book, @Path("id_comment") String id_comment);

    //收获的书评
    @GET("/v1/sns/users/actions")
    Flowable<CommunalResult<ReviewResult<Review>>> loadReceivedBookReview(@Query("is_own") int is_own, @Query("type") int type, @Query("page") int page);

    //收获的评论
    @GET("/v1/sns/users/actions")
    Flowable<CommunalResult<ReviewResult<Review>>> loadReceivedComments(@Query("is_own") int is_own, @Query("type") int type, @Query("page") int page);

    //用户动态
    @GET("/v1/sns/users/actions")
    Flowable<CommunalResult<ReviewResult<Review>>> loadUserDynamic(@Query("is_own") int is_own, @Query("page") int page);

    //书评点赞
    @POST("/v1/sns/books/{id_book}/comments/{id_comment}/likes")
    Flowable<CommunalResult> commentCommendLike(@Path("id_book") String id_book, @Path("id_comment") String id_comment);

    //书评取消点赞
    @DELETE("/v1/sns/books/{id_book}/comments/{id_comment}/likes")
    Flowable<Response<ResponseBody>> commentCommendDislike(@Path("id_book") String id_book, @Path("id_comment") String id_comment);

    //评论举报
    @FormUrlEncoded
    @POST("/v1/sns/complaints/comments")
    Flowable<CommunalResult> reportComment(@Field("comments_id") String comment_id);

    //评论举报
    @FormUrlEncoded
    @POST("/v1/sns/complaints/comments")
    Flowable<CommunalResult> reportCommentReply(@Field("comments_id") String comment_id, @Field("reply_sn") int reply_sn);

    //删除书评
    @DELETE("/v1/sns/books/{id_book}/comments/{id_comment}")
    Flowable<Response<ResponseBody>> deleteComment(@Path("id_book") String id_book, @Path("id_comment") String id_comment);

    //删除评论
    @DELETE("/v1/sns/books/{id_book}/comments/{id_comment}/replies/{sn}")
    Flowable<Response<ResponseBody>> deleteCommentReply(@Path("id_book") String id_book, @Path("id_comment") String id_comment, @Path("sn") int sn);

    @GET("{uri}")
    Flowable<Response<ResponseBody>> downloadApplication(@Path(value = "uri", encoded = true) String uri);


    /*************
     * 创作接口
     *************/
    //获取创作列表
    @GET("/v1/ugc/books")
    Flowable<CommunalResult<LiteratureList>> loadLiteratureList();

    //删除作品
    @FormUrlEncoded
    @POST("/v1/ugc/delete_book")
    Flowable<CommunalResult<LiteratureDelete>> deleteLiterature(@Field("book_id") String id);

    //草稿列表
    @GET("/v1/ugc/books/{id}/drafts")
    Flowable<CommunalResult<DraftList>> loadDraftList(@Path("id") String id, @Query("page") int page, @Query("limit") int limit);

    //删除草稿
    @GET("/v1/ugc/delete_draft")
    Flowable<CommunalResult<DraftDelete>> deleteDraft(@Query("draft_id") String id);

    //创建草稿
    @FormUrlEncoded
    @POST("/v1/ugc/create_draft")
    Flowable<CommunalResult<DraftCreate>> createDraft(@FieldMap Map<String, String> parameter);

    //修改草稿
    @FormUrlEncoded
    @POST("/v1/ugc/update_draft")
    Flowable<CommunalResult<DraftCreate>> updateDraft(@FieldMap Map<String, String> parameter);

    //获取草稿信息
    @GET("/v1/ugc/drafts/{id}")
    Flowable<CommunalResult<DraftCreate>> loadDraft(@Path("id") String id);

    //发布草稿
    @FormUrlEncoded
    @POST("/v1/ugc/publish_draft")
    Flowable<CommunalResult<DraftPublish>> publishDraft(@FieldMap Map<String, String> parameter);

    //章节列表
    @GET("/v1/ugc/books/{id}/chapters")
    Flowable<CommunalResult<SectionList>> loadSectionList(@Path("id") String id, @Query("page") int page, @Query("limit") int limit);

    //发布章节
    @FormUrlEncoded
    @POST("/v1/ugc/create_chapter")
    Flowable<CommunalResult<SectionPublish>> publishSection(@FieldMap Map<String, String> parameter);

    //获取书籍标签
    @GET("/v1/ugc/books/tags")
    Flowable<CommunalResult<LiteratureTag>> loadLiteratureTag();

    //创建作品
    @Multipart
    @POST("/v1/ugc/create_book")
    Flowable<CommunalResult<LiteratureRevise>> createLiterature(@PartMap Map<String, RequestBody> parameter, @Part MultipartBody.Part file);

    //修改作品信息
    @Multipart
    @POST("/v1/ugc/update_book")
    Flowable<CommunalResult<LiteratureRevise>> updateLiterature(@PartMap Map<String, RequestBody> parameter, @Part MultipartBody.Part file);

    //修改章节内容
    @FormUrlEncoded
    @POST("/v1/ugc/update_chapter")
    Flowable<CommunalResult<SectionPublish>> updateSection(@FieldMap Map<String, String> parameter);

    //获取章节内容
    @GET("/v1/ugc/chapters/{id}")
    Flowable<CommunalResult<SectionPublish>> loadSection(@Path("id") String id);






    /*************
     * V2版本接口
     *************/
    //获取书籍信息
    @GET("/v2/books/{book_id}")
    Flowable<CommunalResult<Book>> loadBookInformation(@Path("book_id") String book_id);

    //获取搜索结果
    @GET("v2/books/routings")
    Flowable<CommunalResult<SearchResult>> loadSearchResult(@Query(value = "word", encoded = true) String word, @Query("page") int page);

    //获取搜索联想词
    @GET("v2/books/guess")
    Flowable<CommunalResult<ArrayList<String>>> loadSearchGuess(@Query(value = "word", encoded = true) String word, @Query("page") int page);

    //获取搜索推荐词
    @GET("v2/books/advice")
    Flowable<CommunalResult<List<String>>> loadSearchSuggest(@Query("page") int page);

    //阅读完结页推荐
    @GET("/v2/books/{id}/advice")
    Flowable<CommunalResult<List<Book>>> loadReadEndRecommendList(@Path("id") String id);

    //获取书籍目录
    @GET("/v2/books/{id}/chapters")
    Flowable<CommunalResult<ArrayList<Chapter>>> loadBookCatalog(@Path("id") String id, @Query("start") int start);

    //批量获取章节内容
    @GET("/v2/books/{id}/chapters/items")
    Flowable<CommunalResult<ArrayList<Chapter>>> loadChapterBatch(@Path("id") String id, @Query("start") int start);

    //获取单章内容
    @GET("/v2/books/chapters/{id}")
    Flowable<CommunalResult<Chapter>> loadChapter(@Path("id") String id);

    //检查更新
    @POST("/v2/bookshelves")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Flowable<CommunalResult<ArrayList<Book>>> checkBookUpdate(@Query("is_all") int all, @Body RequestBody json);

    //敏感词检查
    @FormUrlEncoded
    @POST("/v2/verifier/content")
    Flowable<CommunalResult<ArrayList<SensitiveWord>>> filterSensitiveWord(@Field("content") String content);

    //封面页推荐
    @GET("/v2/chakra/cover")
    Flowable<CommunalResult<ArrayList<Book>>> loadCoverRecommend(@Query("book_id") String book_id);
}