package com.shortmeet.www.Api.RetrofitApi;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Fenglingyue on 2017/8/20.
 */

public interface NetInterface {

                                   //  *********************首页//
  /*
   *  Fly 注：首页推荐
   */
     @FormUrlEncoded
     // @POST("vod_list")
       @POST("Video/vod_list")
     Call<ResponseBody> dogetRecomandData(@Field("time") String time,@Field("token") String token, @Field("data") String json);







                                    //  *********************视频详情//
    /*
     *  Fly 注：获取 点赞状态  点赞总数  关注状态
     */
     @FormUrlEncoded
     @POST("Video/getVodThumbUpCount")
     Call<ResponseBody> doShowVideoDetailStus(@Field("time") String time,@Field("token") String token, @Field("data") String json);

    /*
     *  Fly 注：视频详情 点赞  取消赞
     */
    @FormUrlEncoded
    @POST("Video/thumbUp")
    Call<ResponseBody> clickZanCancel(@Field("time") String time,@Field("token") String token, @Field("data") String json);

    /*
    *  Fly 注：视频详情 加关注  取消关注
    */
    @FormUrlEncoded
    @POST("Video/userFollower")
    Call<ResponseBody> addCancelCare(@Field("time") String time,@Field("token") String token, @Field("data") String json);

    /*
     *  Fly 注：视频详情   赞列表   一排六个头像，或  数字点进去（赞过的用户）
     */
    @FormUrlEncoded
    @POST("Video/getVodThumbUpList")
    Call<ResponseBody> showZanHeads(@Field("time") String time,@Field("token") String token, @Field("data") String json);
    /*
     *  Fly 注：视频详情  发评论
     */
    @FormUrlEncoded
    @POST("Video/vodComment")
    Call<ResponseBody> sendCommen(@Field("time") String time,@Field("token") String token, @Field("data") String json);
    /*
     *  Fly 注：视频详情    评论列表
     */
    @FormUrlEncoded
    @POST("Video/getVodCommentList")
    Call<ResponseBody> getCommenList(@Field("time") String time,@Field("token") String token, @Field("data") String json);
   /*
    *  Fly 注：回复评论 或   回复回复
    */
   @FormUrlEncoded
   @POST("Video/vodCommentReply")
   Call<ResponseBody> doReply(@Field("time") String time,@Field("token") String token, @Field("data") String json);
    /*
       *  Fly 注：删除评论
       */
    @FormUrlEncoded
    @POST("Video/delComment")
    Call<ResponseBody> doDeleteCommen(@Field("time") String time,@Field("token") String token, @Field("data") String json);








                                  //  *********************发现//










                                  //  *********************拍摄//
     /*
      *  Fly 注：上传请求
      */
    @FormUrlEncoded
    @POST("Video/request_aliyun_upload")
    Call<ResponseBody> doRequestUpLode(@Field("time") String time,@Field("token") String token, @Field("data") String json);







                                  //  *********************消息//









                                  //  *********************个人//
    /*
     *  Fly 注：创建游客
    */

   @FormUrlEncoded
  //@POST("createVisitor")
  @POST("User/createVisitor")
   Call<ResponseBody> createVisit(@Field("time") String time, @Field("token") String token, @Field("data") String json );

   /*
    *  Fly 注：游客登录
    */

   @FormUrlEncoded
  // @POST("visitorLogin")
  @POST("User/visitorLogin")
   Call<ResponseBody> doVisitorLoginInter(@Field("time") String time, @Field("token") String token, @Field("data") String json );

   /*
    *  Fly 注：获取验证码
    */

    @FormUrlEncoded
  //  @POST("sendsms")
    @POST("User/sendsms")
    Call<ResponseBody> getVerify(@Field("time") String time, @Field("token") String token, @Field("data") String json );

    /*
        *  Fly 注：绑定手机   或者 更换绑定手机  判断验证码
        */

    @FormUrlEncoded
   // @POST("checkPhoneCode")
    @POST("UserSet/checkPhoneCode")
    Call<ResponseBody> checkVerfyCode(@Field("time") String time, @Field("token") String token, @Field("data") String json );

    /*
     *  Fly 注：立即绑定
     */

    @FormUrlEncoded
  // @POST("bindPhone")
   @POST("UserSet/bindPhone")
    Call<ResponseBody> bindPhone(@Field("time") String time, @Field("token") String token, @Field("data") String json );

    /*
     *  Fly 注： 注册
     */

    @FormUrlEncoded
    @POST("reg")
    Call<ResponseBody> goRegist(@Field("time") String time,@Field("token") String token, @Field("data") String json );


   /*
   *  Fly 注：登录
   */
    @FormUrlEncoded
    //@POST("login")
    @POST("User/login")
    Call<ResponseBody> doLogin(@Field("time") String time,@Field("token") String token, @Field("data") String json );

   /*
    *  Fly 注：完善用户信息
    */
   @FormUrlEncoded
   @POST("userinfo")
   Call<ResponseBody> doPerfectUserinfo(@Field("time") String time,@Field("token") String token, @Field("data") String json );

    /*
      *  Fly 注：个人中心 作品列表
      */
    @FormUrlEncoded
    //@POST("user_vod")
    @POST("Video/user_vod")
    Call<ResponseBody> doGetMyWorkList(@Field("time") String time,@Field("token") String token, @Field("data") String json);


    /*
     *  Fly 注：个人中心 获赞列表
    */
    @FormUrlEncoded
    @POST("User/getMyPraiseList")
    Call<ResponseBody> doHuoZanList(@Field("time") String time,@Field("token") String token, @Field("data") String json);

}
