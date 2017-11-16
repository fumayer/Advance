package com.shortmeet.www.ui.PercenalCenter.activity;

import android.app.Dialog;
import android.content.Intent;
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
import com.shortmeet.www.MainActivity;
import com.shortmeet.www.R;
import com.shortmeet.www.bean.personalCenter.PerfectUserInfoBean;
import com.shortmeet.www.entity.percenalCenter.DataEntity;
import com.shortmeet.www.entity.percenalCenter.PerfectUserInfoEntity;
import com.shortmeet.www.mvp.IMVPView;
import com.shortmeet.www.mvp.IMVPrecenter;
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
 *  Fly 注：完善用户信息
 */
public class PerfectUserInfoActivity extends BaseActivity implements IMVPView,View.OnClickListener,TakePhoto.TakeResultListener, InvokeListener {
    private static final String TAG = "PerfectUserInfoActivity";
    // Fly 注：scrollView
    private ScrollView scrollPerfectUserinfoActivity;
    // Fly 注：  上方封面
    private RatioImageView imgvTopPerfectUserinfo;
    // Fly 注：左上角返回
    private FrameLayout frameLeftbackPerfectUserinfo;
    //标题栏标题
    private TextView tvCentertitlePerfectUserinfo;
    // 编辑封面
    private TextView tvEditFengmian;
    //昵称
    private EditText edtNicknamePerfectuserinfo;
    // Fly 注： 女生头像
    private ImageView imgvWomanPerfectuserinfo;
    // Fly 注：女
    private TextView tvWomanPerfectuserinfo;
    // Fly 注：男生头像
    private ImageView imgvManPerfectuserinfo;
    // Fly 注：男
    private TextView tvManPerfectuserinfo;
    // Fly 注：地区
    private TextView tvZone;
    //地区箭头 选择区域
    private ImageView imgvZoneArrowright;
    //地区 linearlayout
    private LinearLayout linearZonePerfectuserinfo;
    // Fly 注： 编辑签名
    private EditText edtSelfsignPerfectuserinfo;
    // Fly 注：立即体验
    private TextView tvInstantTyPerfectuserinfo;
    //记录最终选则 的性别是     1男  2女  （默认女）
    private  int selectedSex=2;
    private IMVPrecenter  mPrecenter;
    //拍照上传
    private TakePhoto takePhoto;
    private InvokeParam invokeParam;
    //剪裁成功 回调 返回的路径
    private String coverImagePath;

    //  判断是否编辑 封面了  ，编辑了  需要上传 封面  否则不需要
    private  boolean hasEditFengmian;


    @Override
    public int setRootView() {
        return R.layout.activity_perfect_user_info;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        //拍照准备 获取TakePhoto实例
        if (getTakePhoto() != null) {
            getTakePhoto().onCreate(savedInstanceState);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        StatusBarUtil.translucentStatusBar(this, false);//设置状态栏透明 内容延伸到状态栏
        scrollPerfectUserinfoActivity = (ScrollView) findViewById(R.id.scroll_perfect_userinfo_activity);
        imgvTopPerfectUserinfo = (RatioImageView) findViewById(R.id.imgv_top_perfect_userinfo);
        frameLeftbackPerfectUserinfo = (FrameLayout) findViewById(R.id.frame_leftback_perfect_userinfo);
        tvCentertitlePerfectUserinfo = (TextView) findViewById(R.id.tv_centertitle_perfect_userinfo);
        tvEditFengmian = (TextView) findViewById(R.id.tv_edit_fengmian);
        edtNicknamePerfectuserinfo = (EditText) findViewById(R.id.edt_nickname_perfectuserinfo);
        imgvWomanPerfectuserinfo = (ImageView) findViewById(R.id.imgv_woman_perfectuserinfo);
        tvWomanPerfectuserinfo = (TextView) findViewById(R.id.tv_woman_perfectuserinfo);
        imgvManPerfectuserinfo = (ImageView) findViewById(R.id.imgv_man_perfectuserinfo);
        tvManPerfectuserinfo = (TextView) findViewById(R.id.tv_man_perfectuserinfo);
        tvZone= (TextView) findViewById(R.id.tv_zone);
        imgvZoneArrowright = (ImageView) findViewById(R.id.imgv_zone_arrowright);
        linearZonePerfectuserinfo = (LinearLayout) findViewById(R.id.linear_zone_perfectuserinfo);
        edtSelfsignPerfectuserinfo = (EditText) findViewById(R.id.edt_selfsign_perfectuserinfo);
        tvInstantTyPerfectuserinfo = (TextView) findViewById(R.id.tv_instantTy_perfectuserinfo);
        mPrecenter=new IMVPrecenter(this,this.getBaseContext());
    }

    @Override
    public void initListener() {
        frameLeftbackPerfectUserinfo.setOnClickListener(this);//返回
        tvEditFengmian.setOnClickListener(this);//编辑封面
        imgvWomanPerfectuserinfo.setOnClickListener(this);//女头像
        imgvManPerfectuserinfo.setOnClickListener(this);//男头像
        linearZonePerfectuserinfo.setOnClickListener(this);//地区
        tvInstantTyPerfectuserinfo.setOnClickListener(this);//立即体验
    }
    @Override
    public void initData() { }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.frame_leftback_perfect_userinfo:
//                DataEntity user = UserUtils.getUser(this);
//                user.setNickname("未设置啊");
//                startActivity(new Intent(this, MainActivity.class));
//                this.finish();
                break;
            case R.id.tv_edit_fengmian:  //编辑封面上传 阿里云
                //編輯封面
                showChooseCameraDialog();
                break;
            case R.id.imgv_woman_perfectuserinfo:  //选择女
                imgvManPerfectuserinfo.setImageResource(R.mipmap.man_n);
                tvManPerfectuserinfo.setTextColor(UiUtils.getColor(R.color.man_n_color));
                imgvWomanPerfectuserinfo.setImageResource(R.mipmap.woman_s);
                tvWomanPerfectuserinfo.setTextColor(UiUtils.getColor(R.color.woman_s_color));
                selectedSex=2;
                break;
            case R.id.imgv_man_perfectuserinfo://选择男
                imgvWomanPerfectuserinfo.setImageResource(R.mipmap.woman_n);
                tvWomanPerfectuserinfo.setTextColor(UiUtils.getColor(R.color.woman_n_color));
                imgvManPerfectuserinfo.setImageResource(R.mipmap.man_s);
                tvManPerfectuserinfo.setTextColor(UiUtils.getColor(R.color.man_s_color));
                selectedSex=1;
                break;
            case R.id.linear_zone_perfectuserinfo:  //选择地区
                showRegionPicker();
                break;

            case R.id.tv_instantTy_perfectuserinfo://立即体验
                String sts=String.valueOf(System.currentTimeMillis());
                String nickname=TextUtils.isEmpty(edtNicknamePerfectuserinfo.getText().toString())?"shortmeet"+sts.substring(sts.length()-6,sts.length()):edtNicknamePerfectuserinfo.getText().toString();//昵称
                int sex=selectedSex;//性别
                String zone=TextUtils.isEmpty(tvZone.getText().toString())?"北京":tvZone.getText().toString();//区域
                String content=TextUtils.isEmpty(edtSelfsignPerfectuserinfo.getText().toString())?"无":edtSelfsignPerfectuserinfo.getText().toString();//签名
                String phone=UserUtils.getUser(PerfectUserInfoActivity.this).getPhone();//手机号
                String sessionId=UserUtils.getSessionId(PerfectUserInfoActivity.this);

                if(!hasEditFengmian){  //没编辑过封面
                    LogUtils.e("PerfectUserInfoActivity----","没编辑过封面 直接调接口");
                    if (nickname.contains(" ")) {
                        showMessage("昵称中不能出现空格");
                        return;
                    }
                    if (nickname.length()>40) {
                        showMessage("请输入1-40长度的任意字符");
                        edtNicknamePerfectuserinfo.setText("");
                        return;
                    }
                    doPerfectIntert(nickname,sex,zone,content,phone,sessionId,TextUtils.isEmpty(coverImagePath)?"无路径":coverImagePath);
                }else{ //编辑过封面  先上传封面
                    LogUtils.e("PerfectUserInfoActivity----","编辑过封面 先上传");
                    this.showLoading("请稍后...");
                    startUpLoadFengMian(coverImagePath,nickname,sex,zone,content,phone,sessionId);
                }
                break;
        }
    }





    /*
     *  Fly 注1：封面选取  相关+++++++++++++++++++++++++++++++++++++++++++++++++++++
     */
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
    /**
     * 获取TakePhoto实例
     */
    public TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }
        return takePhoto;
    }
    //打开相机
    private void openCamera() {
        File file = FileUtil.createCropFile(PerfectUserInfoActivity.this);
        Uri uri = Uri.fromFile(file);
        CropOptions cropOptions = new CropOptions.Builder().setAspectX(1).setAspectY(1).setWithOwnCrop(true).create();
        getTakePhoto().onPickFromCaptureWithCrop(uri, cropOptions);
    }
    //打开相册
    private void openGallery() {
        File file = FileUtil.createCropFile(PerfectUserInfoActivity.this);
        Uri uri = Uri.fromFile(file);
        CropOptions cropOptions = new CropOptions.Builder().setAspectX(1).setAspectY(1).setWithOwnCrop(true).create();
        getTakePhoto().onPickFromGalleryWithCrop(uri, cropOptions);
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
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (getTakePhoto() != null) {
            getTakePhoto().onSaveInstanceState(outState);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (getTakePhoto() != null) {
            getTakePhoto().onActivityResult(requestCode, resultCode, data);
        }
    }
    //选取成功的回调
    @Override
    public void takeSuccess(TResult result) {
        TImage image = result.getImage();
        LogUtils.e(TAG + "_---takeSuccess----: ", "返回原始图片的大小: " + new File(image.getOriginalPath()).length() / 1024 + "K");
        String path=image.getOriginalPath();
        coverImagePath=path;
        GlideLoader.getInstance().loadImg(this,path,true,true,imgvTopPerfectUserinfo);
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
    *  Fly 注2：上传封面图相关+++++++++++++++++++++++++++++++++++++++++++++++++++++
     */
    private OSSAsyncTask<PutObjectResult> myTask;
    private void startUpLoadFengMian(final String photoPath, final String nickname, final int sex, final String zone, final String content, final String phone, final String sessionId) {
        String uuid = getUUID();
        // 构造上传请求
        final String objectKey = "perfectusercover/"+UserUtils.getUser(this).getAccount_id()+"/"+ uuid;
        System.out.println("path--------------->"+photoPath);
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
               LogUtils.e("完善资料封面 上传阿里云成功","完善资料封面 上传阿里云成功");
               doPerfectIntert( nickname, sex, zone, content, phone, sessionId,"http://tlgshortmeeetspace.oss-cn-beijing.aliyuncs.com/"+objectKey );
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                    LogUtils.e("上传阿里云","上传阿里云网络异常");
                    showMessage("设置失败请重新编辑封面");
                }
                if (serviceException != null) {
                    // 服务异常
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                    LogUtils.e("上传阿里云","服务异常");
                    showMessage("设置失败请重新编辑封面");
                }
            }
        });
    }
    public  String getUUID() {
        return UUID.randomUUID().toString().trim().replaceAll("-", "").toUpperCase();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消上传
        cancelUpload();
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
  *  Fly 注：地区选择器 相关+++++++++++++++++++++++++++++++++++++++++++++++++++++
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
                    tvZone.setText(areaName);
                } else {
                    tvZone.setText(provinceName + " " + cityName + " " + areaName);
                }
            }
        });
        task.execute(province, city, county);
    }

    /*
     *  Fly 注：联网完善个人资料
     */
    public void doPerfectIntert(String nickname,int sex,String zone,String content,String phone,String sessionId,String imagePath){
        LogUtils.e("调起完善资料接口","调起完善资料接口 imagePath---------"+imagePath);
        PerfectUserInfoBean  bean=new PerfectUserInfoBean();
        bean.setPhone(phone);
        bean.setImg(TextUtils.isEmpty(imagePath)?"无路径":imagePath);
        bean.setNickname(nickname);
        bean.setArea(zone);
        bean.setContent("无个性签名");
        bean.setSex(sex);
        bean.setSessionid(sessionId);
        mPrecenter.doPerfectUserInfo(bean);
        LogUtils.e("调起完善资料接口","++++++++++传给php后台数据》"+"phone imagepath nickname zone content  sex  sessinid"
                +phone+"@@@"+imagePath+"@@@"+nickname+"@@@"+"@@@"+zone+"@@@"+content+"@@@"+sex+"@@@"+sessionId);
    }

    @Override
    public void setData(Object o, int id) {
        switch (id) {
            case 0://完善用户信息成功
                PerfectUserInfoEntity entity= (PerfectUserInfoEntity) o;
                if(entity.getCode()==0){
                    // Fly 注：完善用户信息成功 保存返回回来的用户信息
                    if(entity.getData()!=null){
                        UserUtils.saveUser(this.getBaseContext(),entity.getData());
                        Log.e("doPerfectUserInfo","完善用户信息++++++++ 保存用戶信息成功！");
                    } else{
                        Log.e("doPerfectUserInfo","完善用户信息 ++++++++保存用戶信息失败！");
                    }
                    UserUtils.setUserIdintify(this,1);//正式用户;
                    this.showMessage("完善信息成功");
                    startActivity(new Intent(this, MainActivity.class));
                    this.hideLoading();
                    this.finish();
                }else  if(entity.getCode()==104){
                    this.showMessage("用户信息错误");
                    this.hideLoading();
                    LogUtils.e("","用户信息错误");
                }else if(entity.getCode()==10707){
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
    public void onBackPressed() {
        DataEntity user = UserUtils.getUser(this);
        String sts=String.valueOf(System.currentTimeMillis());
        String nickname=TextUtils.isEmpty(edtNicknamePerfectuserinfo.getText().toString())?"shortmeet"+sts.substring(sts.length()-6,sts.length()):edtNicknamePerfectuserinfo.getText().toString();//昵称
        user.setNickname(nickname);
        startActivity(new Intent(this, MainActivity.class));
        this.finish();
    }
}
