package com.shortmeet.www.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.alibaba.sdk.android.vod.upload.VODUploadCallback;
import com.alibaba.sdk.android.vod.upload.VODUploadClient;
import com.alibaba.sdk.android.vod.upload.VODUploadClientImpl;
import com.alibaba.sdk.android.vod.upload.model.UploadFileInfo;
import com.alibaba.sdk.android.vod.upload.model.VodInfo;
import com.shortmeet.www.Api.ApiConstant;
import com.shortmeet.www.Base.BaseApplication;
import com.shortmeet.www.bean.takePai.RequestUpLoadBean;
import com.shortmeet.www.entity.takepai.RequestUpLoadEntity;
import com.shortmeet.www.event.UpVideoEvent;
import com.shortmeet.www.mvp.IMVPView;
import com.shortmeet.www.mvp.IMVPrecenter;
import com.shortmeet.www.utilsUsed.LogUtils;
import com.shortmeet.www.utilsUsed.RxBus;
import com.shortmeet.www.utilsUsed.StringUtils;
import com.shortmeet.www.utilsUsed.UserUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class VideoUpLoadService extends Service implements IMVPView{
    private IMVPrecenter  mPrecenter;
    //标题
    private  String  title;
    //视频路径
    private   String   videopath;
    //activity_id;
    private int  activity_id;
    //type频道
    private  int type;
    //标签
    private  String  tag;
    //描述
    private String  descrip;
    //创建时间
    private String  createtime;
    //区域
    private String  area;

    private String UploadAddress;
    private   String UploadAuth;
    private String aliCoverUrlPath;
    @Override
    public void onCreate() {
        super.onCreate();
        mPrecenter=new IMVPrecenter(this);
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
         videopath=intent.getStringExtra("UPLOAD_VIDEO_PATH");
         activity_id=intent.getIntExtra("UPLOAD_ACTIVITY_ID",1);
         type=intent.getIntExtra("UPLOAD_TYPE",1);
         tag=intent.getStringExtra("UPLOAD_TAG");
         title=intent.getStringExtra("UPLOAD_TITLE");
         descrip=intent.getStringExtra("UPLOAD_DESCRIP");
         area=intent.getStringExtra("UPLOAD_AREA");
         createtime=intent.getStringExtra("UPLOAD_CREATETIME");
         String  coverurl=intent.getStringExtra("UPLOAD_COVERURL");

        //上传给阿里云封面   上传封面成功后 -》掉请求上传接口  返给  上传地址和凭证
       LogUtils.e("","传给service的---》"+"videopath=>"+videopath+"activity_id=>"+activity_id+"type=>"+type+"tag=>"+tag+"title=>"+title+"coverurl=>"+coverurl
          +"descrip=>"+descrip+"area=>"+area+"createtime=>"+createtime);
        startUpLoadFengMian(coverurl);
        return super.onStartCommand(intent, flags,startId);
    }
    @Override
    public IBinder onBind(Intent intent) {
    return  null;
    }

  /*
   *  Fly 注：上传封面-----------》阿里云
   */
    private OSSAsyncTask<PutObjectResult> myCoverTask;
    private void startUpLoadFengMian(final String coverUrl) {
        String uuid = getUUID();
        // 构造上传请求
        final String objectKey = "editcover/"+UserUtils.getUser(this).getAccount_id()+"/"+ uuid;
        PutObjectRequest put = new PutObjectRequest(ApiConstant.BUCKETNAME, objectKey,coverUrl);
        // 异步上传时可以设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
           LogUtils.e("TAG" + "_uploadTopImg==========>", "currentSize: " + currentSize + " totalSize: " + totalSize);
            }
        });
        myCoverTask = BaseApplication.getOss().asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
        @Override
        public void onSuccess(PutObjectRequest request, PutObjectResult result) {
            aliCoverUrlPath="http://"+ApiConstant.BUCKETNAME+".oss-cn-beijing.aliyuncs.com/"+objectKey;
          //做访问  提交 拼接好的阿里封面路径
            LogUtils.e("","封面阿里云上传成功---------------------------------");
            doRequestUpLoadAct(aliCoverUrlPath);
            }
            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                    LogUtils.e("上传封面阿里云","上传封面阿里云网络异常");
                }
                if (serviceException != null) {
                    // 服务异常
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                    LogUtils.e("上传封面阿里云","服务异常");
                }
            }
        });
    }
    // Fly 注：获取随机串
    public  String getUUID() {
        return UUID.randomUUID().toString().trim().replaceAll("-", "").toUpperCase();
    }



   //联网动作---------------------------------------------------------------
   /*
   *  Fly 注：请求上传视频接口
   */
    public void doRequestUpLoadAct(String coverPath){
        RequestUpLoadBean  bean=new RequestUpLoadBean();
        bean.setSessionid(UserUtils.getSessionId(this));
        bean.setCover_url(coverPath);
        bean.setTitle(TextUtils.isEmpty(title)?"无标题":title);
        bean.setEncrypt(1);
        bean.setType_id(type);
        bean.setArea(TextUtils.isEmpty(area)?"无地区":area);
        bean.setDescribe(TextUtils.isEmpty(descrip)?"无描述":descrip);
        bean.setCreate_time(TextUtils.isEmpty(createtime)? StringUtils.strToTimeA(String.valueOf(System.currentTimeMillis())):createtime);
        bean.setName(TextUtils.isEmpty(UserUtils.getUser(this).getNickname())?"aaa.mp4":UserUtils.getUser(this).getNickname()+".mp4");
        bean.setTag(TextUtils.isEmpty(tag)?"无标签":tag);
        mPrecenter.doRequestUpLoadAct(bean);
    }

   /*
    *  Fly 注：拿到服务器结果
    */
    @Override
    public void setData(Object o, int id) {
        switch (id) {
          case 0://请求上传接口 将封面 地址给php成功 返回视频地址和凭证
              RequestUpLoadEntity  entity= (RequestUpLoadEntity) o;
              if(entity.getCode()==0){
              LogUtils.e("将封面地址给php成功","并拿到视频地址和凭证");
              if(entity.getData()==null){
              LogUtils.e("将封面地址给php失败entity.getData()==null","");
               return;
              }
               if(TextUtils.isEmpty(entity.getData().getUploadAddress())||TextUtils.isEmpty(entity.getData().getUploadAuth())){
                   LogUtils.e("将封面地址给php失败","地址或凭证==null");
                   return;
               }
                  UploadAddress=entity.getData().getUploadAddress();
                  UploadAuth=entity.getData().getUploadAuth();
                   doVodUpLoadVideos();
              }else {
             LogUtils.e("将封面地址给php失败","未取到视频地址和凭证");
              }
                break;
            default:
                break;
        }
    }

    /*
     *  Fly 注：VOD 模式上传视频
     */
    private VODUploadClient uploader;
    UpVideoEvent mUpVideoEvent=new UpVideoEvent();
    public  void  doVodUpLoadVideos(){
    uploader = new VODUploadClientImpl(getApplicationContext());
    VODUploadCallback callback = new VODUploadCallback() {
      @Override
      public void onUploadSucceed(UploadFileInfo info) {
          mUpVideoEvent.setCode(1);
          RxBus.getDefault().post(mUpVideoEvent);// 1 上传完成
          LogUtils.e("VOD************>","onsucceed 上传成功------------------" + info.getFilePath());
       }
            @Override
       public void onUploadFailed(UploadFileInfo info, String code, String message) {
                mUpVideoEvent.setCode(2);
                RxBus.getDefault().post(mUpVideoEvent);// 2 上传失败
                LogUtils.e("VOD************>","onsucceed 上传失败啦------------------" + info.getFilePath());
       }
        @Override
        public void onUploadProgress(UploadFileInfo info, long uploadedSize, long totalSize) {
            LogUtils.e("VOD  onProgress************>",info.getFilePath() + " VOD 已上传" + uploadedSize + " VOD 总大小" + totalSize);
            mUpVideoEvent.setCurrentSize(uploadedSize);
            mUpVideoEvent.setTotalSize(totalSize);
            mUpVideoEvent.setCode(0);
            RxBus.getDefault().post(mUpVideoEvent);// 0上传中
         }
            @Override
            public void onUploadTokenExpired() {
                LogUtils.e("VOD","onExpired ------------- ");
                // 实现时，从新获取UploadAuth
                // TODO: 2017/11/10   重新获取 地址和凭证
                doRequestUpLoadAct(aliCoverUrlPath);
                uploader.resumeWithAuth(UploadAuth);
            }
            @Override
            public void onUploadRetry(String code, String message) {
                LogUtils.e("VOD","onUploadRetry ------------- ");
                uploader.resume();
            }
            @Override
            public void onUploadRetryResume() {
                LogUtils.e("VOD","onUploadRetryResume ------------- ");

            }
            @Override
            public void onUploadStarted(UploadFileInfo uploadFileInfo) {
             LogUtils.e("VOD","onUploadStarted ------------- ");
             uploader.setUploadAuthAndAddress(uploadFileInfo, UploadAuth, UploadAddress);
                LogUtils.e("VOD","file path:" + uploadFileInfo.getFilePath() +
                        ", endpoint: " + uploadFileInfo.getEndpoint() +
                        ", bucket:" + uploadFileInfo.getBucket() +
                        ", object:" + uploadFileInfo.getObject() +
                        ", status:" + uploadFileInfo.getStatus());
            }
        };
    // 点播上传。每次上传都是独立的鉴权，所以初始化时，不需要设置鉴权
     uploader.init(callback);
     uploader.addFile(videopath,getVodInfo());
    uploader.start();
    }
    private VodInfo getVodInfo() {
        VodInfo vodInfo = new VodInfo();
        vodInfo.setTitle(TextUtils.isEmpty(title)?"无标题":title);
        vodInfo.setDesc(TextUtils.isEmpty(descrip)?"无描述":descrip);
        vodInfo.setCateId(1);
        vodInfo.setIsProcess(true);
        vodInfo.setCoverUrl(aliCoverUrlPath+".jpg");
        List<String> tags = new ArrayList<>();
        tags.add(TextUtils.isEmpty(tag)?"无标签":tag);
        vodInfo.setTags(tags);
        vodInfo.setIsShowWaterMark(false);
        vodInfo.setPriority(7);
        return vodInfo;
     }
    @Override
    public void showLoading(String msg) {}
    @Override
    public void hideLoading() {}
    @Override
    public void showMessage(String msg) {}
    @Override
    public void onDestroy() {
     super.onDestroy();
    }
}






