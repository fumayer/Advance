package com.shortmeet.www.ui.TakePai;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shortmeet.www.Api.ApiConstant;
import com.shortmeet.www.Base.BaseActivity;
import com.shortmeet.www.Base.IBaseView;
import com.shortmeet.www.R;
import com.shortmeet.www.mvp.IMVPrecenter;
import com.shortmeet.www.service.VideoUpLoadService;
import com.shortmeet.www.utilsUsed.FileUtil;
import com.shortmeet.www.utilsUsed.GlideLoader;
import com.shortmeet.www.utilsUsed.LogUtils;
import com.shortmeet.www.utilsUsed.StatusBarUtil;
import com.shortmeet.www.utilsUsed.StringUtils;
import com.shortmeet.www.views.widgetPart.RatioImageView;

public class MyUploadActivity extends BaseActivity implements IBaseView, View.OnClickListener {
    public static final int CROP_IMAGE_CODE=1;
    //跟布局
    private RelativeLayout activityMyUpload;
    //封面
    private RatioImageView imgvFengmianUploadActity;
    // 发布
    private TextView tvCentertitleUploadActity;
    //左侧返回
    private FrameLayout frameLeftbackUploadActity;
   //保存草稿
    private TextView tvSaveDraftsUpload;
    //编辑封面
    private TextView tvEditFengmianUploadActity;
    //编辑标题
    private EditText edtTitleUploadActity;
    //添加活动
    private TextView tvAddactivityTag;
    //删除活动标签
    private ImageView imgvTagDeleteUploadactivty;
    //你在哪里
    private TextView tvWhereyouUploadactivity;
    //微信 布局和图标
    private FrameLayout frameWeixinUploadactivity;
    private ImageView imgvWeixinUploadactivity;
    //微信朋友圈 布局和图标
    private FrameLayout frameFriendsUploadactivity;
    private ImageView imgvFriendsUploadactivity;
    //QQ 布局和图标
    private FrameLayout frameQqUploadactivity;
    private ImageView imgvQqUploadactivity;
    //QQ空间 布局和图标
    private FrameLayout frameSpaceUploadactivity;
    private ImageView imgvSpaceUpload;
    //微博 布局和图标
    private FrameLayout frameWeiboUploadactivity;
    private ImageView imgvWeiboUpload;
    //发送上传
    private TextView tvSendUploadactivity;
    private IMVPrecenter mPrecenter;
    //编辑封面 返回的coverUrl
    private String coverUrlPath;
    //要上传的视频的地址
    private  String upPath;
    //视频 时长
    private long duration;
    //进度条
    private ProgressBar pb_progress;
    //是否存入草稿箱
    private boolean isdraft=false;
    @Override
    public int setRootView() {
        return R.layout.activity_my_upload;
    }
    @Override
    public void initView() {
     upPath=this.getIntent().getStringExtra("upPath");
     duration=this.getIntent().getLongExtra("vedioDuration",0);
     mPrecenter=new IMVPrecenter(this);
     StatusBarUtil.translucentStatusBar(this, false);//设置状态栏透明 内容延伸到状态栏
      activityMyUpload = (RelativeLayout) findViewById(R.id.activity_my_upload);
      imgvFengmianUploadActity = (RatioImageView) findViewById(R.id.imgv_fengmian_upload_actity);
      frameLeftbackUploadActity = (FrameLayout) findViewById(R.id.frame_leftback_upload_actity);
      tvCentertitleUploadActity = (TextView) findViewById(R.id.tv_centertitle_upload_actity);
      tvSaveDraftsUpload = (TextView) findViewById(R.id.tv_save_drafts_upload);
      tvEditFengmianUploadActity = (TextView) findViewById(R.id.tv_edit_fengmian_upload_actity);
      edtTitleUploadActity = (EditText) findViewById(R.id.edt_title_upload_actity);
      tvAddactivityTag = (TextView) findViewById(R.id.tv_addactivity_tag);
      imgvTagDeleteUploadactivty = (ImageView) findViewById(R.id.imgv_tag_delete_uploadactivty);
      tvWhereyouUploadactivity = (TextView) findViewById(R.id.tv_whereyou_uploadactivity);
      frameWeixinUploadactivity = (FrameLayout) findViewById(R.id.frame_weixin_uploadactivity);
      imgvWeixinUploadactivity = (ImageView) findViewById(R.id.imgv_weixin_uploadactivity);
      frameFriendsUploadactivity = (FrameLayout) findViewById(R.id.frame_friends_uploadactivity);
      imgvFriendsUploadactivity = (ImageView) findViewById(R.id.imgv_friends_uploadactivity);
      frameQqUploadactivity = (FrameLayout) findViewById(R.id.frame_qq_uploadactivity);
      imgvQqUploadactivity = (ImageView) findViewById(R.id.imgv_qq_uploadactivity);
      frameSpaceUploadactivity = (FrameLayout) findViewById(R.id.frame_space_uploadactivity);
      imgvSpaceUpload = (ImageView) findViewById(R.id.imgv_space_upload);
      frameWeiboUploadactivity = (FrameLayout) findViewById(R.id.frame_weibo_uploadactivity);
      imgvWeiboUpload = (ImageView) findViewById(R.id.imgv_weibo_upload);
      tvSendUploadactivity = (TextView) findViewById(R.id.tv_send_uploadactivity);
        pb_progress= (ProgressBar) findViewById(R.id.pb_progress);
    }
    @Override
    public void initListener() {
     //返回
      frameLeftbackUploadActity.setOnClickListener(this);
    //存草稿
      tvSaveDraftsUpload.setOnClickListener(this);
    //编辑封面
     tvEditFengmianUploadActity.setOnClickListener(this);
    //添加活动标签
    tvAddactivityTag.setOnClickListener(this);
    //删除活动标签
    imgvTagDeleteUploadactivty.setOnClickListener(this);
   //你在哪里
    tvWhereyouUploadactivity.setOnClickListener(this);
    //微信
    frameWeixinUploadactivity.setOnClickListener(this);
    //朋友圈
    frameFriendsUploadactivity.setOnClickListener(this);
    //qq
    frameQqUploadactivity.setOnClickListener(this);
    //QQ空间
    frameSpaceUploadactivity.setOnClickListener(this);
    //微博
    frameWeiboUploadactivity.setOnClickListener(this);
    //发送视频
    tvSendUploadactivity.setOnClickListener(this);
    }
    @Override
    public void initData() {
        isdraft=false;
    }
    // Fly 注：控件点击监听
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.frame_leftback_upload_actity://返回
                this.finish();
                break;
            case R.id.tv_save_drafts_upload:  //存草稿
                pb_progress.setVisibility(View.VISIBLE);
                //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
                boolean movestate = FileUtil.moveTotherFolders(upPath, ApiConstant.COMPOSE_PAYH_DRAFT);
                if (movestate){
                    pb_progress.setVisibility(View.GONE);
                    showMessage("保存成功");
                    isdraft=true;
                    upPath =ApiConstant.COMPOSE_PAYH_DRAFT+ upPath.substring(upPath.lastIndexOf("/")+1,upPath.length());
                }
                break;
            case R.id.tv_edit_fengmian_upload_actity: //编辑封面
                //showMessage("编辑封面");
                if (!TextUtils.isEmpty(upPath)){
                    Intent intent = new Intent(this,EditeCoverActivity.class);
                    intent.putExtra("path",upPath);
                    intent.putExtra("duration",duration);
                    startActivityForResult(intent, CROP_IMAGE_CODE);
                }
                break;
            case R.id.tv_addactivity_tag:  //添加活动标签
                showMessage("添加活动标签");
                break;
            case R.id.imgv_tag_delete_uploadactivty://删除活动标签
                showMessage("删除活动标签");
                break;
            case R.id.tv_whereyou_uploadactivity://你在哪里
                showMessage("你在哪里");
                break;
            case R.id.frame_weixin_uploadactivity: //微信
                showMessage("微信");
                break;
            case R.id.frame_friends_uploadactivity://朋友圈
                showMessage("朋友圈");
                break;
            case R.id.frame_qq_uploadactivity: //qq
                showMessage("qq");
                break;
            case R.id.frame_space_uploadactivity: //QQ空间
                showMessage("QQ空间");
                break;
            case R.id.frame_weibo_uploadactivity: //微博
                showMessage("微博");
                break;
            case R.id.tv_send_uploadactivity://发送视频  （Service做上传视频和封面的操作）
             showMessage("发送视频");
            int activity_id=1;
            int type=1;
            String tag=tvAddactivityTag.getText().toString();
            String title=edtTitleUploadActity.getText().toString();
            String coverurl=coverUrlPath;
            String descrip ="描述";
            String area=tvWhereyouUploadactivity.getText().toString();
            String createtime= StringUtils.strToTimeA(String.valueOf(System.currentTimeMillis()));

            Intent intent=new Intent(this,VideoUpLoadService.class);
            intent.putExtra("UPLOAD_VIDEO_PATH",upPath);
            intent.putExtra("UPLOAD_ACTIVITY_ID",activity_id);
            intent.putExtra("UPLOAD_TYPE",type);
            intent.putExtra("UPLOAD_TAG",tag);
            intent.putExtra("UPLOAD_TITLE",title);
            intent.putExtra("UPLOAD_COVERURL",coverurl);
            intent.putExtra("UPLOAD_DESCRIP",descrip);
            intent.putExtra("UPLOAD_AREA",area);
            intent.putExtra("UPLOAD_CREATETIME",createtime);
            startService(intent);
            this.finish();
            break;
        }
    }



    @Override
    public void setData(Object o, int id) { }
    // Fly 注：选好的封面地址
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode== Activity.RESULT_OK){
            String path = data.getStringExtra("coverpath");
            switch (requestCode){
            case CROP_IMAGE_CODE:
              LogUtils.e("封面图片",path+"-----");
              coverUrlPath=path;
              GlideLoader.getInstance().loadImg(this,path,true,true,imgvFengmianUploadActity);
             break;
            }
        }
    }
}
