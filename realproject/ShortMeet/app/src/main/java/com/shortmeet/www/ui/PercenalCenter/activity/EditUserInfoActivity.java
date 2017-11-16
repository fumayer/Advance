package com.shortmeet.www.ui.PercenalCenter.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;
import com.shortmeet.www.Api.ApiConstant;
import com.shortmeet.www.Base.BaseActivity;
import com.shortmeet.www.Base.BaseApplication;
import com.shortmeet.www.R;
import com.shortmeet.www.bean.personalCenter.PerfectUserInfoBean;
import com.shortmeet.www.entity.percenalCenter.DataEntity;
import com.shortmeet.www.entity.percenalCenter.PerfectUserInfoEntity;
import com.shortmeet.www.mvp.IMVPView;
import com.shortmeet.www.mvp.IMVPrecenter;
import com.shortmeet.www.utilsUsed.DialogUtils;
import com.shortmeet.www.utilsUsed.FileUtil;
import com.shortmeet.www.utilsUsed.GlideLoader;
import com.shortmeet.www.utilsUsed.LogUtils;
import com.shortmeet.www.utilsUsed.StatusBarUtil;
import com.shortmeet.www.utilsUsed.UiUtils;
import com.shortmeet.www.utilsUsed.UserUtils;
import com.shortmeet.www.utilsUsed.CstDialogN;
import com.shortmeet.www.views.picker.AddressPickTask;
import com.shortmeet.www.views.widgetPart.RatioImageView;

import java.io.File;
import java.util.UUID;

import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;

/*
 *  Fly 注：  编辑资料界面
 */
public class EditUserInfoActivity extends BaseActivity implements IMVPView,View.OnClickListener,TakePhoto.TakeResultListener, InvokeListener  {
    private static final String TAG = "EditUserInfoActivity";
    //滑动得scrollView
    private ScrollView scrollEditUserinfoActivity;
    //封面背景
    private RatioImageView imgvTopeditUserinfo;
    //左边返回
    private FrameLayout frameLeftbackEditUserinfo;
    //左边返回图标
    private ImageView imgvLeftbackEditUserinfo;
    //中间标题
    private TextView tvCentertitleEditUserinfo;
    //右侧完成
    private TextView tvFinishEditUserinfo;
    //编辑封面
    private TextView tvBianjiuserinfoFengmian;
    // 昵称
    private EditText edtNicknameEdituserinfo;
    //男女布局
    private RelativeLayout relavWomanEdituserinfo;
    private RelativeLayout relavManEdituserinfo;
    // 女图标
    private ImageView imgvWomanEdituserinfo;
    //女
    private TextView tvWomanEdituserinfo;
    //男图标
    private ImageView imgvManEdituserinfo;
    //男
    private TextView tvManEdituserinfo;
    //地区 布局
    private LinearLayout linearZoneEdituserinfo;
    //地区
    private TextView tvZoneEdituserinfo;
    //地区右侧箭头
    private ImageView imgvZonearrowrightEdituserinfo;
    //个性签名
    private EditText edtSelfsignEdituserinfo;

    //记录最终选则 的性别是     1男  2女  （默认女）
    private  int selectedSex=2;
    private IMVPrecenter mPrecenter;
    //拍照上传
    private TakePhoto takePhoto;
    private InvokeParam invokeParam;
    //剪裁成功 回调 返回的路径
    private String coverImagePath;
   //  判断是否编辑 封面了  ，编辑了  需要上传 封面  否则不需要
    private  boolean hasEditFengmian;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        //拍照准备 获取TakePhoto实例
        if (getTakePhoto() != null) {
            getTakePhoto().onCreate(savedInstanceState);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public int setRootView() {
        return R.layout.activity_edit_user_info;
    }
    @Override
    public void initView() {
        StatusBarUtil.translucentStatusBar(this, false);//设置状态栏透明 内容延伸到状态栏
        mPrecenter=new IMVPrecenter(this);
        scrollEditUserinfoActivity = (ScrollView) findViewById(R.id.scroll_edit_userinfo_activity);
        imgvTopeditUserinfo = (RatioImageView) findViewById(R.id.imgv_topedit_userinfo);
        frameLeftbackEditUserinfo = (FrameLayout) findViewById(R.id.frame_leftback_edit_userinfo);
        imgvLeftbackEditUserinfo = (ImageView) findViewById(R.id.imgv_leftback_edit_userinfo);
        tvCentertitleEditUserinfo = (TextView) findViewById(R.id.tv_centertitle_edit_userinfo);
        tvFinishEditUserinfo = (TextView) findViewById(R.id.tv_finish_edit_userinfo);
        tvBianjiuserinfoFengmian = (TextView) findViewById(R.id.tv_bianjiuserinfo_fengmian);
        edtNicknameEdituserinfo = (EditText) findViewById(R.id.edt_nickname_edituserinfo);
        relavWomanEdituserinfo = (RelativeLayout) findViewById(R.id.relav_woman_edituserinfo);
        relavManEdituserinfo = (RelativeLayout) findViewById(R.id.relav_man_edituserinfo);
        imgvWomanEdituserinfo = (ImageView) findViewById(R.id.imgv_woman_edituserinfo);
        tvWomanEdituserinfo = (TextView) findViewById(R.id.tv_woman_edituserinfo);
        imgvManEdituserinfo = (ImageView) findViewById(R.id.imgv_man_edituserinfo);
        tvManEdituserinfo = (TextView) findViewById(R.id.tv_man_edituserinfo);
        linearZoneEdituserinfo = (LinearLayout) findViewById(R.id.linear_zone_edituserinfo);
        tvZoneEdituserinfo = (TextView) findViewById(R.id.tv_zone_edituserinfo);
        imgvZonearrowrightEdituserinfo = (ImageView) findViewById(R.id.imgv_zonearrowright_edituserinfo);
        edtSelfsignEdituserinfo = (EditText) findViewById(R.id.edt_selfsign_edituserinfo);
        showOrignalData();
    }
    //进入界面  展示界面最初数据
   public void  showOrignalData(){
       DataEntity user = UserUtils.getUser(this);
       //背景
       GlideLoader.getInstance().loadImg(this,user.getImg(),R.drawable.ass,imgvTopeditUserinfo);
       //昵称
       edtNicknameEdituserinfo.setText(TextUtils.isEmpty(user.getNickname())?"未设置":user.getNickname());
       //性别
       if("1".equals(user.getSex())){
         showManSelected();
       }else{
         showWomenSelected();
       }
       //地区
       tvZoneEdituserinfo.setText(TextUtils.isEmpty(user.getArea())?"北京":user.getArea());
       //个性签名
       edtSelfsignEdituserinfo.setText(TextUtils.isEmpty(user.getContent())?"未设置":user.getContent());

   }
    @Override
    public void initListener() {
        frameLeftbackEditUserinfo.setOnClickListener(this);//返回
        tvBianjiuserinfoFengmian.setOnClickListener(this);//编辑封面
        relavWomanEdituserinfo.setOnClickListener(this);//女头像
        relavManEdituserinfo.setOnClickListener(this);//男头像
        linearZoneEdituserinfo.setOnClickListener(this);//地区
        tvFinishEditUserinfo.setOnClickListener(this);//完成
    }
    @Override
    public void initData() {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.frame_leftback_edit_userinfo:  //返回
               this.finish();
                break;
            case R.id.relav_woman_edituserinfo: //女头像
                showWomenSelected();
                break;
            case R.id.relav_man_edituserinfo://男头像
                showManSelected();
                break;
            case R.id.linear_zone_edituserinfo://地区
                showRegionPicker();
                break;
            case R.id.tv_bianjiuserinfo_fengmian://编辑封面   并上传阿里云
               //編輯封面
                showChooseCameraDialog();
                break;
            case R.id.tv_finish_edit_userinfo://完成
                String nickname=edtNicknameEdituserinfo.getText().toString();//昵称
                int sex=selectedSex;//性别
                String zone=tvZoneEdituserinfo.getText().toString();//区域
                String content=TextUtils.isEmpty(edtSelfsignEdituserinfo.getText().toString())?"未设置":edtSelfsignEdituserinfo.getText().toString();//签名
                String phone= UserUtils.getUser(EditUserInfoActivity.this).getPhone();//手机号
                String sessionId=UserUtils.getSessionId(EditUserInfoActivity.this);

                if(!hasEditFengmian){  //没编辑过封面
                LogUtils.e("EditUserInfoActivity","没编辑过封面 直接调接口");
                doPerfectIntert(nickname,sex,zone,content,phone,sessionId,UserUtils.getUser(this).getImg());
                }else{ //编辑过封面  先上传封面
                LogUtils.e("EditUserInfoActivity","编辑过封面 先上传");
                this.showLoading("请稍后...");
                startUpLoadFengMian(coverImagePath,nickname,sex,zone,content,phone,sessionId);
                }
                break;
        }
    }





     /*
      *  Fly 注： 封面选取相关---------------------------
      */
     //获取TakePhoto实例
    @Override
    protected void onSaveInstanceState(Bundle outState) {   //生命周期
        super.onSaveInstanceState(outState);
        if (getTakePhoto() != null) {
            getTakePhoto().onSaveInstanceState(outState);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { ////生命周期
        super.onActivityResult(requestCode, resultCode, data);
        if (getTakePhoto() != null) {
            getTakePhoto().onActivityResult(requestCode, resultCode, data);
        }
    }
    public TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }
        return takePhoto;
    }
    // 权限相关
    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }
    // 权限相关
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //以下代码为处理Android6.0、7.0动态权限所需
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(this, type, invokeParam, this);
    }

     public void showChooseCameraDialog(){
         // 创建一个对象
         View view = View.inflate(this, R.layout.dialog_take_photo, null);
         View btnCancel = view.findViewById(R.id.btn_take_photo_cancel);//取消按钮
         //显示对话框
         CstDialogN showBottonDialog = new CstDialogN(this, view, btnCancel);
         final Dialog dialog = showBottonDialog.show();
         //拍照按钮
         Button btnCamera = (Button) view.findViewById(R.id.btn_take_photo_camera);
         //相册获取
         Button btnAlbum = (Button) view.findViewById(R.id.btn_take_photo_album);
         // 设置监听
         btnCamera.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 openCamera();//打开相机
                 dialog.dismiss();
             }
         });
         btnAlbum.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 openGallery();//打开相册
                 dialog.dismiss();
             }
         });
     }

    //打开相机
    private void openCamera() {
        File file = FileUtil.createCropFile(EditUserInfoActivity.this);
        Uri uri = Uri.fromFile(file);
        CropOptions cropOptions = new CropOptions.Builder().setAspectX(1).setAspectY(1).setWithOwnCrop(true).create();
        getTakePhoto().onPickFromCaptureWithCrop(uri, cropOptions);
    }
    //打开相册
    private void openGallery() {
        File file = FileUtil.createCropFile(EditUserInfoActivity.this);
        Uri uri = Uri.fromFile(file);
        CropOptions cropOptions = new CropOptions.Builder().setAspectX(1).setAspectY(1).setWithOwnCrop(true).create();
        getTakePhoto().onPickFromGalleryWithCrop(uri, cropOptions);
    }

    //选取成功的回调
    @Override
    public void takeSuccess(TResult result) {
        TImage image = result.getImage();
        LogUtils.e(TAG + "_---EditUserInfoActivity--takeSuccess----: ", "返回原始图片的大小: " + new File(image.getOriginalPath()).length() / 1024 + "K");
        String path=image.getOriginalPath();
        coverImagePath=path;
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        System.out.println("------------------------------------->"+path+"   "+bitmap);
        GlideLoader.getInstance().loadImg(this,path,true,true,imgvTopeditUserinfo);
        hasEditFengmian=true;
    }
    @Override
    public void takeFail(TResult result, String msg) {
        showMessage("拍照/选择相册失败~");
        hasEditFengmian=false;
    }

    @Override
    public void takeCancel() {
        showMessage("取消拍摄");
        hasEditFengmian=false;
    }




      /*
       *  Fly 注：地区相关
       */
      private void showRegionPicker() {
          String province = "北京市";
          String city = "北京市";
          String county = "东城区";
          AddressPickTask task = new AddressPickTask(this);
          task.setHideProvince(false);
          task.setHideCounty(false);
          task.setCallback(new AddressPickTask.Callback() {
              @Override
              public void onAddressInitFailed() {
                  showMessage("数据初始化失败");
              }
              @Override
              public void onAddressPicked(Province province, City city, County county) {
                  String provinceName = province.getAreaName();
                  String cityName = city.getAreaName();
                  String areaName = county.getAreaName();
                  if (TextUtils.equals("其他", provinceName) && TextUtils.equals("其他", cityName)) {
                      tvZoneEdituserinfo.setText(areaName);
                  } else {
                      tvZoneEdituserinfo.setText(provinceName + " " + cityName + " " + areaName);
                  }
              }
          });
          task.execute(province, city, county);
      }




      /*
       *  Fly 注：上传封面相关
       */
    private OSSAsyncTask<PutObjectResult> myTask;
    private void startUpLoadFengMian(final String photoPath, final String nickname, final int sex, final String zone, final String content, final String phone, final String sessionId) {
        String uuid = getUUID();
        // 构造上传请求
        final String objectKey = "perfectusercover/"+UserUtils.getUser(this).getAccount_id()+"/"+ uuid;
        PutObjectRequest put = new PutObjectRequest(ApiConstant.BUCKETNAME, objectKey, photoPath);
        // 异步上传时可以设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
          LogUtils.e("TAG" + "_uploadTopImg", "currentSize: " + currentSize + " totalSize: " + totalSize);
            }
        });

        //  LogUtils.eNormal(TAG + "当前线程: ", Thread.currentThread().toString());
        // 请求异常
        // 本地异常如网络异常等
        // 服务异常
        myTask = BaseApplication.getOss().asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
//             LogUtils.eNormal(TAG + "当前线程: ", Thread.currentThread().toString());
                LogUtils.e("编辑资料封面 上传阿里云成功","------编辑资料封面 上传成功-----");
                doPerfectIntert( nickname, sex, zone, content, phone, sessionId,"http://tlgshortmeeetspace.oss-cn-beijing.aliyuncs.com/"+objectKey );
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                    LogUtils.e("上传阿里云","上传阿里云网络异常-----");
                    EditUserInfoActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                         showMessage("设置失败请重新编辑封面");
                        }
                    });
                }
                if (serviceException != null) {
                    // 服务异常
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                    LogUtils.e("上传阿里云","服务异常-------");
                    EditUserInfoActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showMessage("设置失败请重新编辑封面");
                        }
                    });
                }
            }
        });
    }
    //获取随机串儿
    public  String getUUID() {
        return UUID.randomUUID().toString().trim().replaceAll("-", "").toUpperCase();
    }
    /**
     * 取消上传操作
     */
    private void cancelUpload() {
        if (myTask != null && !myTask.isCanceled()) {
            myTask.cancel();
        }
    }





    /*
    *  Fly 注：联网完善个人资料  --吊起接口
    */
    public void doPerfectIntert(String nickname,int sex,String zone,String content,String phone,String sessionId,String imagePath){
        LogUtils.e("调起编辑资料接口","调起编辑资料接口---------传给php后台图片路径是》"+imagePath);
        PerfectUserInfoBean bean=new PerfectUserInfoBean();
        bean.setPhone(phone);
        bean.setImg(imagePath);
        bean.setNickname(nickname);
        bean.setArea(zone);
        bean.setContent(content);
        bean.setSex(sex);
        bean.setSessionid(sessionId);
//        LogUtils.e("调起编辑资料接口","--------传给php后台数据》"+"phone imagepath nickname zone content  sex  sessinid"
//            +phone+"  *"+imagePath+"   *"+nickname+"  *"+"  *"+zone+"  *"+content+"  *"+sex+"  *"+sessionId);
        mPrecenter.doPerfectUserInfo(bean);
    }

    @Override
    public void setData(Object o, int id) {
        switch (id) {
            case 0://编辑用户资料成功
                PerfectUserInfoEntity entity= (PerfectUserInfoEntity) o;
                if(entity.getCode()==0){
                    // Fly 注：编辑用户资料成功 保存返回回来的用户信息
                    if(entity.getData()!=null){
                        UserUtils.saveUser(this.getBaseContext(),entity.getData());
                        Log.e("doPerfectUserInfo","编辑用户 保存用戶信息成功！");
                    } else{
                        Log.e("doPerfectUserInfo","编辑用户信息 保存用戶信息失败！");
                    }
                    this.showMessage("编辑信息成功");
                    this.hideLoading();
                    this.finish();
                }else  if(entity.getCode()==104){
                    this.showMessage("用户信息错误");
                    this.hideLoading();
                    LogUtils.e("","用户信息错误");
                DialogUtils.reLoginAct(this, this.getBaseContext(), "您的账号已在另一台设备上登陆，若非本人操作则账号已泄露，请立即找回密码!", false);
                }else if(entity.getCode()==4008){
                    this.showMessage("用户不存在");
                    this.hideLoading();
                    LogUtils.e("","用户不存在");
                } else{
                    LogUtils.e("","参数错误");
                    this.hideLoading();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消上传
        cancelUpload();
    }
    /*
     *  Fly 注：显示性别
     */
    //显示男
    public  void  showManSelected(){
        imgvWomanEdituserinfo.setImageResource(R.mipmap.woman_n);
        tvWomanEdituserinfo.setTextColor(UiUtils.getColor(R.color.woman_n_color));
        imgvManEdituserinfo.setImageResource(R.mipmap.man_s);
        tvManEdituserinfo.setTextColor(UiUtils.getColor(R.color.man_s_color));
        selectedSex=1;
    }
    //显示女
    public void showWomenSelected(){
     imgvManEdituserinfo.setImageResource(R.mipmap.man_n);
     tvManEdituserinfo.setTextColor(UiUtils.getColor(R.color.man_n_color));
     imgvWomanEdituserinfo.setImageResource(R.mipmap.woman_s);
     tvWomanEdituserinfo.setTextColor(UiUtils.getColor(R.color.woman_s_color));
     selectedSex=2;
    }
}
