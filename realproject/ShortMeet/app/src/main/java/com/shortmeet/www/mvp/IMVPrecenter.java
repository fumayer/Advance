package com.shortmeet.www.mvp;


import android.content.Context;

import com.google.gson.Gson;
import com.shortmeet.www.Api.RequestFactory;
import com.shortmeet.www.Api.RetrofitApi.RetrofitClient;
import com.shortmeet.www.Base.IBasePrecenter;
import com.shortmeet.www.Base.IBaseView;
import com.shortmeet.www.bean.home.GetRecomandVideoBean;
import com.shortmeet.www.bean.personalCenter.BindPhoneBean;
import com.shortmeet.www.bean.personalCenter.CheckVerfyBean;
import com.shortmeet.www.bean.personalCenter.CreateVisitorBean;
import com.shortmeet.www.bean.personalCenter.LoginBean;
import com.shortmeet.www.bean.personalCenter.MyWorkBean;
import com.shortmeet.www.bean.personalCenter.PerfectUserInfoBean;
import com.shortmeet.www.bean.personalCenter.RegistBean;
import com.shortmeet.www.bean.personalCenter.VerifyBean;
import com.shortmeet.www.bean.personalCenter.VisitorLoginBean;
import com.shortmeet.www.bean.takePai.RequestUpLoadBean;
import com.shortmeet.www.bean.video.AddCancelCareBean;
import com.shortmeet.www.bean.video.ClickZanBean;
import com.shortmeet.www.bean.video.DeleteCommenBean;
import com.shortmeet.www.bean.video.GetCommenListBean;
import com.shortmeet.www.bean.video.ReplyBean;
import com.shortmeet.www.bean.video.SendCommenBean;
import com.shortmeet.www.bean.video.ShowZanHeadsBean;
import com.shortmeet.www.bean.video.VideoDetailStatusShowBean;
import com.shortmeet.www.entity.home.RecomandEntity;
import com.shortmeet.www.entity.percenalCenter.BindPhoneEntity;
import com.shortmeet.www.entity.percenalCenter.CheckVerfyEntity;
import com.shortmeet.www.entity.percenalCenter.CreateVisitorEntity;
import com.shortmeet.www.entity.percenalCenter.GetVerifyEntity;
import com.shortmeet.www.entity.percenalCenter.LoginEntity;
import com.shortmeet.www.entity.percenalCenter.MyWorkEntity;
import com.shortmeet.www.entity.percenalCenter.PerfectUserInfoEntity;
import com.shortmeet.www.entity.percenalCenter.RegistEntity;
import com.shortmeet.www.entity.percenalCenter.VisitorLoginEntity;
import com.shortmeet.www.entity.takepai.RequestUpLoadEntity;
import com.shortmeet.www.entity.video.AddCancelCareEntity;
import com.shortmeet.www.entity.video.ClickZanEntity;
import com.shortmeet.www.entity.video.DeleteCommenEntity;
import com.shortmeet.www.entity.video.GetCommenListEntity;
import com.shortmeet.www.entity.video.ReplyEntity;
import com.shortmeet.www.entity.video.SendCommenEntity;
import com.shortmeet.www.entity.video.ShowZanHeadsEntity;
import com.shortmeet.www.entity.video.VideoDetailStatusEntity;
import com.shortmeet.www.utilsUsed.AesEncryptionUtil;
import com.shortmeet.www.utilsUsed.LogUtils;
import com.shortmeet.www.utilsUsed.MD5UtilShotM;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Fenglingyue on 2017/8/31.
 * 控制 所有的逻辑操作
 */

public class IMVPrecenter implements IBasePrecenter {
    private IBaseView view;
    private Context mContext;
    public  IMVPrecenter(IBaseView view ){
        this.view=view;
    }
    public  IMVPrecenter(IBaseView view,Context mContext){
        this.view=view;
        this.mContext=mContext;
    }

  // ****************** Home 首页**********************************************
   // Fly 注：  首页 推荐
   public  void  doGetRecomandInter(GetRecomandVideoBean bean){
       String strstime=String.valueOf(System.currentTimeMillis()).substring(0,10);
       String token= MD5UtilShotM.getToken(strstime);
       String secretJson=AesEncryptionUtil.getSecreJson(new Gson().toJson(bean));
       // view.showLoading("");
       RequestFactory.chooseKindApi(RetrofitClient.class).createRetroApi().dogetRecomandData(strstime,token,secretJson).enqueue(new Callback<ResponseBody>() {
           @Override
           public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
               String  sts= null;
               if(response.body()==null){
                   LogUtils.e("","null====>doGetRecomandInter response.body()");
                   view.hideLoading();
                   return;
               }
               try {
                   sts = AesEncryptionUtil.getJieMiJson(response.body().string());
                 LogUtils.e("doGetRecomandInter","doGetRecomandInter-------=======>sts"+sts);
               } catch (IOException e) {
                   e.printStackTrace();
               }
               RecomandEntity entity=new Gson().fromJson(sts,RecomandEntity.class);
               if(entity==null){
                   LogUtils.e("","null====>doGetRecomandInter entity");
                view.hideLoading();
                   return;
               }else{
                view.setData(entity,0);
                 //view.hideLoading();
               }
           }
           @Override
           public void onFailure(Call<ResponseBody> call, Throwable t) {
               System.out.println("doGetRecomandInter"+t);
               view.hideLoading();
               view.showMessage("网络错误");
               LogUtils.e("doGetRecomandInter==》","访问错误");
           }
       });
   }





 // ******************* Video视频********************************************
  // Fly 注：视频详情 获取点赞数  状态   关注状态
  public void  doVideoDetailStatus(VideoDetailStatusShowBean bean){
      String strstime=String.valueOf(System.currentTimeMillis()).substring(0,10);
      String token= MD5UtilShotM.getToken(strstime);
      String secretJson=AesEncryptionUtil.getSecreJson(new Gson().toJson(bean));
      RequestFactory.chooseKindApi(RetrofitClient.class).createRetroApi().doShowVideoDetailStus(strstime,token,secretJson).enqueue(new Callback<ResponseBody>() {
          @Override
          public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
              String  sts= null;
              if(response.body()==null){
                  LogUtils.e("","null====>doVideoDetailStatus response.body()");
                  return;
              }
              try {
                  sts = AesEncryptionUtil.getJieMiJson(response.body().string());

                  LogUtils.e("doVideoDetailStatus","doVideoDetailStatus-------=======>sts"+sts);
              } catch (IOException e) {
                  e.printStackTrace();
              }
              VideoDetailStatusEntity entity=new Gson().fromJson(sts,VideoDetailStatusEntity.class);
              if(entity==null){
                  LogUtils.e("","null====>doVideoDetailStatus entity");
                  view.hideLoading();
                  return;
              }else{
                  view.setData(entity,0);
              }
          }
          @Override
          public void onFailure(Call<ResponseBody> call, Throwable t) {
              System.out.println("doVideoDetailStatus"+t);
              view.hideLoading();
              view.showMessage("网络错误");
              LogUtils.e("doVideoDetailStatus==》","访问错误");
          }
      });
  }
  // Fly 注：视频详情  点赞  取消赞
   public void  doClickZanCancelVideoDetail(ClickZanBean bean){
       String strstime=String.valueOf(System.currentTimeMillis()).substring(0,10);
       String token= MD5UtilShotM.getToken(strstime);
       String secretJson=AesEncryptionUtil.getSecreJson(new Gson().toJson(bean));
       RequestFactory.chooseKindApi(RetrofitClient.class).createRetroApi().clickZanCancel(strstime,token,secretJson).enqueue(new Callback<ResponseBody>() {
           @Override
           public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
               String  sts= null;
               if(response.body()==null){
                   LogUtils.e("","null====>doClickZanCancelVideoDetail response.body()");
                   return;
               }
               try {
                   sts = AesEncryptionUtil.getJieMiJson(response.body().string());
                   LogUtils.e("doClickZanCancelVideoDetail","doClickZanCancelVideoDetail-------=======>sts"+sts);
               } catch (IOException e) {
                   e.printStackTrace();
               }
               ClickZanEntity entity=new Gson().fromJson(sts,ClickZanEntity.class);
               if(entity==null){
                   LogUtils.e("","null====>doClickZanCancelVideoDetail entity");
                  // view.hideLoading();
                   return;
               }else{
                   view.setData(entity,1);
               }
           }
           @Override
           public void onFailure(Call<ResponseBody> call, Throwable t) {
               System.out.println("doClickZanCancelVideoDetail"+t);
               view.hideLoading();
               view.showMessage("网络错误");
               LogUtils.e("doClickZanCancelVideoDetail==》","访问错误");
           }
       });
   }

    // Fly 注：视频详情  关注  取消关注
    public void  doAddCancelCare(AddCancelCareBean bean){
        String strstime=String.valueOf(System.currentTimeMillis()).substring(0,10);
        String token= MD5UtilShotM.getToken(strstime);
        String secretJson=AesEncryptionUtil.getSecreJson(new Gson().toJson(bean));
        RequestFactory.chooseKindApi(RetrofitClient.class).createRetroApi().addCancelCare(strstime,token,secretJson).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String  sts= null;
                if(response.body()==null){
                    LogUtils.e("","null====>doAddCancelCare response.body()");
                    return;
                }
                try {
                    sts = AesEncryptionUtil.getJieMiJson(response.body().string());
                    LogUtils.e("doAddCancelCare","doAddCancelCare-------=======>sts"+sts);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                 AddCancelCareEntity entity=new Gson().fromJson(sts,AddCancelCareEntity.class);
                if(entity==null){
                    LogUtils.e("","null====>doAddCancelCare entity");
                    view.hideLoading();
                    return;
                }else{
                    view.setData(entity,11);
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("doAddCancelCare"+t);
                view.hideLoading();
                view.showMessage("网络错误");
                LogUtils.e("doAddCancelCare==》","访问错误");
            }
        });
    }

   //Fly 注：6个点赞的头像
   public void  doShowAllZanHeads(ShowZanHeadsBean bean){
       String strstime=String.valueOf(System.currentTimeMillis()).substring(0,10);
       String token= MD5UtilShotM.getToken(strstime);
       String secretJson=AesEncryptionUtil.getSecreJson(new Gson().toJson(bean));
       RequestFactory.chooseKindApi(RetrofitClient.class).createRetroApi().showZanHeads(strstime,token,secretJson).enqueue(new Callback<ResponseBody>() {
           @Override
           public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
               String  sts= null;
               if(response.body()==null){
                   LogUtils.e("","null====>doShowAllZanHeads response.body()");
                   return;
               }
               try {
                   sts = AesEncryptionUtil.getJieMiJson(response.body().string());
                   LogUtils.e("doShowAllZanHeads","doShowAllZanHeads-------=======>sts"+sts);
               } catch (IOException e) {
                   e.printStackTrace();
               }
                ShowZanHeadsEntity entity=new Gson().fromJson(sts,ShowZanHeadsEntity.class);
                if(entity==null){
                    LogUtils.e("","null====>doShowAllZanHeads entity");
                    // view.hideLoading();
                    return;
                }else{
                    view.setData(entity,2);
                }
           }
           @Override
           public void onFailure(Call<ResponseBody> call, Throwable t) {
               System.out.println("doShowAllZanHeads"+t);
               view.hideLoading();
               view.showMessage("网络错误");
               LogUtils.e("doShowAllZanHeads==》","访问错误");
           }
       });
   }

   // Fly 注：发评论
   public void  doSendCommen(SendCommenBean bean){
       String strstime=String.valueOf(System.currentTimeMillis()).substring(0,10);
       String token= MD5UtilShotM.getToken(strstime);
       String secretJson=AesEncryptionUtil.getSecreJson(new Gson().toJson(bean));
       RequestFactory.chooseKindApi(RetrofitClient.class).createRetroApi().sendCommen(strstime,token,secretJson).enqueue(new Callback<ResponseBody>() {
           @Override
           public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
               String  sts= null;
               if(response.body()==null){
                   LogUtils.e("","null====>doSendCommen response.body()");
                   return;
               }
               try {
                   sts = AesEncryptionUtil.getJieMiJson(response.body().string());
               LogUtils.e("doSendCommen","doSendCommen-------=======>sts"+sts);
               } catch (IOException e) {
                   e.printStackTrace();
               }
               SendCommenEntity entity=new Gson().fromJson(sts,SendCommenEntity.class);
               if(entity==null){
                   LogUtils.e("","null====>doSendCommen entity");
                   // view.hideLoading();
                   return;
               }else{
                   view.setData(entity,3);
               }
           }
           @Override
           public void onFailure(Call<ResponseBody> call, Throwable t) {
               System.out.println("doSendCommen"+t);
               view.hideLoading();
               view.showMessage("网络错误");
               LogUtils.e("doSendCommen==》","访问错误");
           }
       });
   }

    //Fly 注：获取评论列表  doGetCommenList
   public void  doGetCommenList(GetCommenListBean bean){
       String strstime=String.valueOf(System.currentTimeMillis()).substring(0,10);
       String token= MD5UtilShotM.getToken(strstime);
       String secretJson=AesEncryptionUtil.getSecreJson(new Gson().toJson(bean));
       RequestFactory.chooseKindApi(RetrofitClient.class).createRetroApi().getCommenList(strstime,token,secretJson).enqueue(new Callback<ResponseBody>() {
           @Override
           public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
               String  sts= null;
               if(response.body()==null){
                   LogUtils.e("","null====>doGetCommenList response.body()");
                   return;
               }
               try {
                   sts = AesEncryptionUtil.getJieMiJson(response.body().string());
                   LogUtils.e("doGetCommenList","doGetCommenList-------=======>sts"+sts);
               } catch (IOException e) {
                   e.printStackTrace();
               }
               GetCommenListEntity entity=new Gson().fromJson(sts,GetCommenListEntity.class);
               if(entity==null){
                   LogUtils.e("","null====>doGetCommenList entity");
                   // view.hideLoading();
                   return;
               }else{
                   view.setData(entity,4);
               }
           }
           @Override
           public void onFailure(Call<ResponseBody> call, Throwable t) {
               System.out.println("doGetCommenList"+t);
               view.hideLoading();
               view.showMessage("网络错误");
               LogUtils.e("doGetCommenList==》","访问错误");
           }
       });
   }

   // Fly 注：回复评论 或者回复回复
    public void  doReplyAct(ReplyBean bean){
        String strstime=String.valueOf(System.currentTimeMillis()).substring(0,10);
        String token= MD5UtilShotM.getToken(strstime);
        String secretJson=AesEncryptionUtil.getSecreJson(new Gson().toJson(bean));
        RequestFactory.chooseKindApi(RetrofitClient.class).createRetroApi().doReply(strstime,token,secretJson).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String  sts= null;
                if(response.body()==null){
                    LogUtils.e("","null====>doReplyAct response.body()");
                    return;
                }
                try {
                    sts = AesEncryptionUtil.getJieMiJson(response.body().string());
                    LogUtils.e("doReplyAct","doReplyAct-------=======>sts"+sts);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ReplyEntity entity=new Gson().fromJson(sts,ReplyEntity.class);
                if(entity==null){
                    LogUtils.e("","null====>doReplyAct entity");
                    // view.hideLoading();
                    return;
                }else{
                    view.setData(entity,5);
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("doReplyAct"+t);
                view.hideLoading();
                view.showMessage("网络错误");
                LogUtils.e("doReplyAct==》","访问错误");
            }
        });
    }
   //删除评论
   public void  doDeleteCommenAct(DeleteCommenBean bean){
       String strstime=String.valueOf(System.currentTimeMillis()).substring(0,10);
       String token= MD5UtilShotM.getToken(strstime);
       String secretJson=AesEncryptionUtil.getSecreJson(new Gson().toJson(bean));
       RequestFactory.chooseKindApi(RetrofitClient.class).createRetroApi().doDeleteCommen(strstime,token,secretJson).enqueue(new Callback<ResponseBody>() {
           @Override
           public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
               String  sts= null;
               if(response.body()==null){
                   LogUtils.e("","null====>doDeleteCommenAct response.body()");
                   return;
               }
               try {
                   sts = AesEncryptionUtil.getJieMiJson(response.body().string());
                   LogUtils.e("doDeleteCommenAct","doDeleteCommenAct-------=======>sts"+sts);
               } catch (IOException e) {
                   e.printStackTrace();
               }
                DeleteCommenEntity entity=new Gson().fromJson(sts,DeleteCommenEntity.class);
                if(entity==null){
                    LogUtils.e("","null====>doDeleteCommenAct entity");
                    // view.hideLoading();
                    return;
                }else{
                    view.setData(entity,6);
                }
           }
           @Override
           public void onFailure(Call<ResponseBody> call, Throwable t) {
               System.out.println("doDeleteCommenAct"+t);
               view.hideLoading();
               view.showMessage("网络错误");
               LogUtils.e("doDeleteCommenAct==》","访问错误");
           }
       });
   }



// *******************  Discover发现********************************************






 // ************** TakePai拍摄*************
    //上传请求
 // Fly 注：上传请求
 public  void  doRequestUpLoadAct(RequestUpLoadBean bean){
     String strstime=String.valueOf(System.currentTimeMillis()).substring(0,10);
     String token= MD5UtilShotM.getToken(strstime);
     String secretJson=AesEncryptionUtil.getSecreJson(new Gson().toJson(bean));
     RequestFactory.chooseKindApi(RetrofitClient.class).createRetroApi().doRequestUpLode(strstime,token,secretJson).enqueue(new Callback<ResponseBody>() {
         @Override
         public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
             String  sts= null;
             if(response.body()==null){
                 LogUtils.e("","null====>doRequestUpLoadAct response.body()");
                 return;
             }
             try {
                 sts = AesEncryptionUtil.getJieMiJson(response.body().string());
                 LogUtils.e("doRequestUpLoadAct","doRequestUpLoadAct==========>sts"+sts);
             } catch (IOException e) {
                 e.printStackTrace();
             }
             RequestUpLoadEntity entity=new Gson().fromJson(sts,RequestUpLoadEntity.class);
             if(entity==null){
                 LogUtils.e("","null====>doUpLoadVideo ");
                 return;
             }else{
                 view.setData(entity,0);//请求上传接口 将封面 地址给php成功 返回视频地址和凭证
             }
         }
         @Override
         public void onFailure(Call<ResponseBody> call, Throwable t) {
             System.out.println("doRequestUpLoadAct"+t);
             view.hideLoading();
             view.showMessage("网络错误");
             LogUtils.e("","访问错误");
         }
     });
 }



    // ********************** Group群组*********************






    // ******************** Percenal个人中心********************
    // Fly 注： 创建游客 接口
     public  void  createYk(CreateVisitorBean bean){
     String strstime=String.valueOf(System.currentTimeMillis()).substring(0,10);
     String token= MD5UtilShotM.getToken(strstime);
     String secretJson=AesEncryptionUtil.getSecreJson(new Gson().toJson(bean));
     RequestFactory.chooseKindApi(RetrofitClient.class).createRetroApi().createVisit(strstime,token,secretJson).enqueue(new Callback<ResponseBody>() {
             @Override
             public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                 String  sts= null;
                 if(response.body()==null){
                     LogUtils.e("","null====>createYk response.body()");
                     view.showLoading("");
                     return;
                 }
                 try {
                     sts = AesEncryptionUtil.getJieMiJson(response.body().string());
                     LogUtils.e("createYk","stststst==============>"+sts);
                 } catch (IOException e) {
                     e.printStackTrace();
                 }
                 try {
                     CreateVisitorEntity entity= new Gson().fromJson(sts,CreateVisitorEntity.class);
                     if(entity==null){
                         LogUtils.e("createYk","createYk entity==null");
                         view.showLoading("");
                         return;
                     }else{
                         view.setData(entity,0);
                     }
                 } catch ( Exception e) {
                     e.printStackTrace();
                 }
             }
             @Override
             public void onFailure(Call<ResponseBody> call, Throwable t) {
                LogUtils.e("createYk","createYk错误"+t);
                 view.showLoading("");
             }
         });
     }

   // Fly 注： 游客二次登录
    public void doVisitorLogin(VisitorLoginBean  bean){
        String strstime=String.valueOf(System.currentTimeMillis()).substring(0,10);
        String token= MD5UtilShotM.getToken(strstime);
        String secretJson=AesEncryptionUtil.getSecreJson(new Gson().toJson(bean));
        RequestFactory.chooseKindApi(RetrofitClient.class).createRetroApi().doVisitorLoginInter(strstime,token,secretJson).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String  sts= null;
                if(response.body()==null){
                    LogUtils.e("","null====>doVisitorLogin response.body()");
                    view.showLoading("");
                    return;
                }
                try {
                    sts = AesEncryptionUtil.getJieMiJson(response.body().string());
                    LogUtils.e("doVisitorLogin","stststst==============>"+sts);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    VisitorLoginEntity entity= new Gson().fromJson(sts,VisitorLoginEntity.class);
                    if(entity==null){
                        LogUtils.e("doVisitorLogin","doVisitorLogin entity==null");
                        view.showLoading("");
                        return;
                    }else{
                        view.setData(entity,0);
                    }
                } catch ( Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                LogUtils.e("doVisitorLogin","doVisitorLogin错误"+t);
                view.showLoading("");
            }
        });
    }

   // Fly 注：获取验证码
     public  void  getVerify(VerifyBean bean){
         String strstime=String.valueOf(System.currentTimeMillis()).substring(0,10);
         String token= MD5UtilShotM.getToken(strstime);
         String secretJson=AesEncryptionUtil.getSecreJson(new Gson().toJson(bean));
         RequestFactory.chooseKindApi(RetrofitClient.class).createRetroApi().getVerify(strstime,token,secretJson).enqueue(new Callback<ResponseBody>() {
             @Override
             public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                 String  sts= null;
                 if(response.body()==null){
                    LogUtils.e("","null====>getVerify response.body()");
                     return;
                 }
                 try {
                     sts = AesEncryptionUtil.getJieMiJson(response.body().string());
              LogUtils.e("getVerify","getVerify sts=================>"+sts);
                 } catch (IOException e) {
                     e.printStackTrace();
                 }
                 try {
                     GetVerifyEntity entity= new Gson().fromJson(sts,GetVerifyEntity.class);
                     if(entity==null){
                         LogUtils.e("","getVerify entity==null");
                         return;
                     }else{
                         view.setData(entity,0);
                     }
                 } catch ( Exception e) {
                     e.printStackTrace();
                 }
             }
             @Override
             public void onFailure(Call<ResponseBody> call, Throwable t) {
                 System.out.println("getVerify错误"+t);
                view.showMessage("网络错误");
             }
         });
     }

  // Fly 注：绑定手机   或者 更换绑定手机  判断验证码
    public  void  justVerfyCode(CheckVerfyBean bean){
        String strstime=String.valueOf(System.currentTimeMillis()).substring(0,10);
        String token= MD5UtilShotM.getToken(strstime);
        String secretJson=AesEncryptionUtil.getSecreJson(new Gson().toJson(bean));
        RequestFactory.chooseKindApi(RetrofitClient.class).createRetroApi().checkVerfyCode(strstime,token,secretJson).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String  sts= null;
                if(response.body()==null){
                    LogUtils.e("","null====>justVerfyCode response.body()");
                    return;
                }
                try {
                    sts = AesEncryptionUtil.getJieMiJson(response.body().string());
                    LogUtils.e("justVerfyCode","justVerfyCode sts=================>"+sts);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    CheckVerfyEntity entity= new Gson().fromJson(sts,CheckVerfyEntity.class);
                    if(entity==null){
                        LogUtils.e("justVerfyCode","justVerfyCode entity==null");
                        return;
                    }else{
                        view.setData(entity,1);
                    }
                } catch ( Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("justVerfyCode错误"+t);
                view.showMessage("网络错误");
            }
        });
    }

   // Fly 注：立即绑定手机
    public  void  bindPhoneInstant(BindPhoneBean bean){
        String strstime=String.valueOf(System.currentTimeMillis()).substring(0,10);
        String token= MD5UtilShotM.getToken(strstime);
        String secretJson=AesEncryptionUtil.getSecreJson(new Gson().toJson(bean));
        RequestFactory.chooseKindApi(RetrofitClient.class).createRetroApi().bindPhone(strstime,token,secretJson).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String  sts= null;
                if(response.body()==null){
                    LogUtils.e("","null====>bindPhoneInstant response.body()");
                    return;
                }
                try {
                    sts = AesEncryptionUtil.getJieMiJson(response.body().string());
                    LogUtils.e("bindPhoneInstant","bindPhoneInstant sts=================>"+sts);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    BindPhoneEntity entity= new Gson().fromJson(sts,BindPhoneEntity.class);
                    if(entity==null){
                        LogUtils.e("bindPhoneInstant","bindPhoneInstant entity==null");
                        return;
                    }else{
                        view.setData(entity,1);
                    }
                } catch ( Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("bindPhoneInstant错误"+t);
                view.showMessage("网络错误");
            }
        });
    }

     // Fly 注： 注册
     public  void  doRegist(RegistBean bean){
         String strstime=String.valueOf(System.currentTimeMillis()).substring(0,10);
         String token= MD5UtilShotM.getToken(strstime);
         String secretJson=AesEncryptionUtil.getSecreJson(new Gson().toJson(bean));
         RequestFactory.chooseKindApi(RetrofitClient.class).createRetroApi().goRegist(strstime,token,secretJson).enqueue(new Callback<ResponseBody>() {
             @Override
             public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                 String  sts= null;
                 if(response.body()==null){
                     LogUtils.e("","null====>doRegist response.body()");
                     return;
                 }
                 try {
                     sts = AesEncryptionUtil.getJieMiJson(response.body().string());
                     System.out.println("sts----------------------------------------->zhuce"+sts);
                 } catch (IOException e) {
                     e.printStackTrace();
                 }
                 RegistEntity entity=new Gson().fromJson(sts,RegistEntity.class);
                 if(entity==null){
                     LogUtils.e("","doRegist entity==null");
                     return;
                 }else{
                     view.setData(entity,1);
                 }
             }
             @Override
             public void onFailure(Call<ResponseBody> call, Throwable t) {
                   LogUtils.e("","doRegist错误"+t);
                  view.showMessage("网络错误");
             }
         });
     }

    // Fly 注：登录
    public  void  doLoginInter(LoginBean bean){
        String strstime=String.valueOf(System.currentTimeMillis()).substring(0,10);
        String token= MD5UtilShotM.getToken(strstime);
        String secretJson=AesEncryptionUtil.getSecreJson(new Gson().toJson(bean));
        RequestFactory.chooseKindApi(RetrofitClient.class).createRetroApi().doLogin(strstime,token,secretJson).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String  sts= null;
                if(response.body()==null){
                    LogUtils.e("","null====>doLoginInter response.body()");
                    return;
                }
                try {
                    sts = AesEncryptionUtil.getJieMiJson(response.body().string());
                    LogUtils.e("","doLoginInter================>"+sts);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                 LoginEntity entity=new Gson().fromJson(sts,LoginEntity.class);
                if(entity==null){
                    LogUtils.e(" ","doLoginInter entity==null");
                    return;
                }else{
                    view.setData(entity,0);//登录成功
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                LogUtils.e("","doLoginInter"+t);
                view.showMessage("网络错误");
            }
        });
    }

  // Fly 注：完善用户信息
  public  void  doPerfectUserInfo(PerfectUserInfoBean bean){
      String strstime=String.valueOf(System.currentTimeMillis()).substring(0,10);
      String token= MD5UtilShotM.getToken(strstime);
      String secretJson=AesEncryptionUtil.getSecreJson(new Gson().toJson(bean));
      RequestFactory.chooseKindApi(RetrofitClient.class).createRetroApi().doPerfectUserinfo(strstime,token,secretJson).enqueue(new Callback<ResponseBody>() {
          @Override
          public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
              String  sts= null;
              if(response.body()==null){
                LogUtils.e("","null========>doPerfectUserInfo response.body()");
                  return;
              }
              try {
                  sts = AesEncryptionUtil.getJieMiJson(response.body().string());
                  LogUtils.e("doPerfectUserInfo完善资料","sts-=================>"+sts);
              } catch (IOException e) {
                  e.printStackTrace();
              }
              PerfectUserInfoEntity entity=new Gson().fromJson(sts,PerfectUserInfoEntity.class);
              if(entity==null){
              LogUtils.e("","null====>doPerfectUserInfo entity");
                 return;
              }else{
                  view.setData(entity,0);
              }
          }
          @Override
          public void onFailure(Call<ResponseBody> call, Throwable t) {
              System.out.println("doPerfectUserInfo错误"+t);
              view.showMessage("网络错误");
              LogUtils.e("","访问错误");
          }
      });
  }

   // Fly 注：用户个人中心列表  -----作品
   public  void  doGetMyWorks(MyWorkBean bean){
       String strstime=String.valueOf(System.currentTimeMillis()).substring(0,10);
       String token= MD5UtilShotM.getToken(strstime);
       String secretJson=AesEncryptionUtil.getSecreJson(new Gson().toJson(bean));
       RequestFactory.chooseKindApi(RetrofitClient.class).createRetroApi().doGetMyWorkList(strstime,token,secretJson).enqueue(new Callback<ResponseBody>() {
           @Override
           public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
               String  sts= null;
               if(response.body()==null){
                   LogUtils.e("","null====>doGetMyWorks response.body()");
                   return;
               }
               try {
                   sts = AesEncryptionUtil.getJieMiJson(response.body().string());
                   LogUtils.e("doGetMyWorksList","doGetMyWorks +++++++++++++++++++==>sts"+sts);
               } catch (IOException e) {
                   e.printStackTrace();
               }
               MyWorkEntity workEntity=new Gson().fromJson(sts,MyWorkEntity.class);
               if(workEntity==null){
                   LogUtils.e("","null====>doGetMyWorks workEntity");
                   view.hideLoading();
                   return;
               }else{
                   view.setData(workEntity,0);
               }
           }
           @Override
           public void onFailure(Call<ResponseBody> call, Throwable t) {
               System.out.println("doGetMyWorks"+t);
               view.hideLoading();
               view.showMessage("网络错误");
               LogUtils.e("","访问错误");
           }
       });
   }

    // Fly 注：用户个人中心列表  -----点赞作品
    public  void  doGetMyZanWorks(MyWorkBean bean){
        String strstime=String.valueOf(System.currentTimeMillis()).substring(0,10);
        String token= MD5UtilShotM.getToken(strstime);
        String secretJson=AesEncryptionUtil.getSecreJson(new Gson().toJson(bean));
        RequestFactory.chooseKindApi(RetrofitClient.class).createRetroApi().doGetMyWorkList(strstime,token,secretJson).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String  sts= null;
                if(response.body()==null){
                    LogUtils.e("","null====>doGetMyWorks response.body()");
                    return;
                }
                try {
                    sts = AesEncryptionUtil.getJieMiJson(response.body().string());
                    System.out.println("doGetMyWorks    ==>sts"+sts);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                MyWorkEntity workEntity=new Gson().fromJson(sts,MyWorkEntity.class);
                if(workEntity==null){
                    LogUtils.e("","null====>doGetMyWorks workEntity");
                    view.hideLoading();
                    return;
                }else{
                    view.setData(workEntity,1);
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("doGetMyWorks"+t);
                view.hideLoading();
                view.showMessage("网络错误");
                LogUtils.e("","访问错误");
            }
        });
    }


}















//    //Fly 注：  retrofit + rxjava方式
//    public  void  dologin1(String phone, final String pwd, int type){
//        view.showLoading("正在登录");
//        RequestFactory.chooseKindApi(RetrofitClient.class).createRetroApi().login1(phone,pwd,type)
//                .map(new Func1<LoginEntity, String>() {
//                    @Override
//                    public String call(LoginEntity loginEntity) {
//                        if(loginEntity.getCode()==10000){
//                            view.showMessage("Func1里登陆成功");
//                            //做数据保存等操作  将实体传给  activity
//                            view.setData(loginEntity,2);
//                            return "登陆成功";
//                        }else{
//                            view.showMessage("Func1里登陆失败");
//                            return "登陆失败";
//                        }
//                    }
//                })
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(new Action1<String>() {
//                    @Override
//                    public void call(String s) {
//                        if("登陆成功".equals(s)){
//                            view.hideLoading();
//                            view.showMessage("登录成功啦");
//                        }else{
//                            view.hideLoading();
//                            view.showMessage("登录失败啦");
//                        }
//                    }
//                }, new Action1<Throwable>() {
//                    @Override
//                    public void call(Throwable throwable) {
//                        view.hideLoading();
//                        view.showMessage("登录请求失败error");
//                    }
//                });
//    }

/*    *//**
     * 数据判空，获取bean实体对象
     * @param response
     * @param message
     * @param clazz
     * @return
     *//*
 Object checkEmpty(Response<ResponseBody> response,String message,Class clazz){
      String  sts= null;
      if(response.body()==null){
          LogUtils.e("","null====>doPerfectUserInfo response.body()");
          return null;
      }
      try {
          sts = AesEncryptionUtil.getJieMiJson(response.body().string());
      } catch (IOException e) {
          e.printStackTrace();
      }
      Object object=new Gson().fromJson(sts,clazz);
      return  object;
    }*/




