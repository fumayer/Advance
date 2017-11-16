package com.shortmeet.www.ui.PercenalCenter.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.shortmeet.www.Base.BaseActivity;
import com.shortmeet.www.Base.CtrollAllactivitys;
import com.shortmeet.www.MainActivity;
import com.shortmeet.www.R;
import com.shortmeet.www.bean.personalCenter.VisitorLoginBean;
import com.shortmeet.www.entity.percenalCenter.VisitorLoginEntity;
import com.shortmeet.www.mvp.IMVPView;
import com.shortmeet.www.mvp.IMVPrecenter;
import com.shortmeet.www.utilsUsed.DiskCatchUtils;
import com.shortmeet.www.utilsUsed.LogUtils;
import com.shortmeet.www.utilsUsed.UserUtils;
import com.shortmeet.www.utilsUsed.CstDialogN;

import java.io.File;

/*
 *  Fly 注：设置界面
 */
public class SettingPageActivity extends BaseActivity implements IMVPView,View.OnClickListener {
     //绑定手机
    private LinearLayout linearBandphoneSettingpage;
    // 隐私
    private LinearLayout linearPrivateSettingpage;
    //通用
    private LinearLayout linearCommenuseSettingpage;
    //问题反馈
    private LinearLayout linearProblembackSettingpage;
    //用户协议
    private LinearLayout linearUserxieyiSettingpage;
    //清理缓存
    private LinearLayout linearClearCatchSettingpage;
    //退出登录
    private TextView tvExitSettingpage;
    //缓存大小
    private TextView tvCatchesize;
    //显示  手机号码   或者  显示请绑定 手机号
    private TextView tvJustphoneSettingpage;
    /*
     *   退出 对话框标题     取消  确认按扭
    */
    AlertDialog dialog;
    private TextView tvTitle;
    private AppCompatButton btnNegative;
    private AppCompatButton btnPositive;
   // IMVPrecenter
    private IMVPrecenter mPrecenter;

  /*
   *  Fly 注：自动登录请求bean
   */
    VisitorLoginBean bean;

    /**
     *清除缓存
     */
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            new DiskCatchUtils(tvCatchesize).execute(new File(getCacheDir(), DiskCache.Factory.DEFAULT_DISK_CACHE_DIR));
        }
    };

    @Override
    public int setRootView() {
        return R.layout.activity_settingpage;
    }

    @Override
    public boolean setUseTitleBar() {
        return true;
    }

    @Override
    public void initView() {
     tvCenterTitleBar.setText("设置");
    imgvLeftbackTitlebar.setVisibility(View.VISIBLE);
    mPrecenter=new IMVPrecenter(this);
    linearBandphoneSettingpage = (LinearLayout) findViewById(R.id.linear_bandphone_settingpage);
    linearPrivateSettingpage = (LinearLayout) findViewById(R.id.linear_private_settingpage);
    linearCommenuseSettingpage = (LinearLayout) findViewById(R.id.linear_commenuse_settingpage);
    linearProblembackSettingpage = (LinearLayout) findViewById(R.id.linear_problemback_settingpage);
    linearUserxieyiSettingpage = (LinearLayout) findViewById(R.id.linear_userxieyi_settingpage);
    linearClearCatchSettingpage = (LinearLayout) findViewById(R.id.linear_clear_catch_settingpage);
    tvCatchesize = (TextView) findViewById(R.id.tv_catchesize);
    tvExitSettingpage = (TextView) findViewById(R.id.tv_exit_settingpage);
    linearClearCatchSettingpage = (LinearLayout) findViewById(R.id.linear_clear_catch_settingpage);
    tvExitSettingpage = (TextView) findViewById(R.id.tv_exit_settingpage);
    tvCatchesize = (TextView) findViewById(R.id.tv_catchesize);
    tvJustphoneSettingpage = (TextView) findViewById(R.id.tv_justphone_settingpage);
    showOrignData();
    initDig();//退出Dialog 初始化
    showRestarDig();// 变为游客  游客自动登录Dialog 初始化
    }
    public void showOrignData(){
    //获取缓存 呈现
     new DiskCatchUtils(tvCatchesize).execute(new File(getCacheDir(), DiskCache.Factory.DEFAULT_DISK_CACHE_DIR));
     if(TextUtils.isEmpty(UserUtils.getUser(this).getPhone())){
         tvJustphoneSettingpage.setText("请绑定手机号");
     } else{
         tvJustphoneSettingpage.setText(UserUtils.getUser(this).getPhone());
     }
    }

    @Override
    public void initListener() {
    titleBarLeftframelayout.setOnClickListener(this);
    linearBandphoneSettingpage.setOnClickListener(this);
    linearPrivateSettingpage.setOnClickListener(this);
    linearCommenuseSettingpage.setOnClickListener(this);
    linearProblembackSettingpage.setOnClickListener(this);
    linearUserxieyiSettingpage.setOnClickListener(this);
    linearClearCatchSettingpage.setOnClickListener(this);
    tvExitSettingpage.setOnClickListener(this);
    }

    @Override
    public void initData() { }
    /*
     *  Fly 注：点击事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_bar_leftframelayout: //返回
                this.finish();
                break;
            case R.id.linear_bandphone_settingpage: //绑定
                if(TextUtils.isEmpty(UserUtils.getUser(this).getPhone())){
                    startActivity(new Intent(this,BindPhoneNumberActivity.class));
                }else{
                    showChangeBindDialog();//更改绑定
                }
                break;
            case R.id.linear_private_settingpage://隐私
                startActivity(new Intent(this,PrivateActivity.class));
                break;
            case R.id.linear_commenuse_settingpage://通用

                break;
            case R.id.linear_problemback_settingpage://问题反馈

                break;
            case R.id.linear_userxieyi_settingpage://用户协议

                break;
            case R.id.linear_clear_catch_settingpage://清理缓存
                Glide.get(this).clearMemory();
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        Glide.get(SettingPageActivity.this).clearDiskCache();
                        mHandler.sendEmptyMessage(1);
                    }
                }.start();
                break;
            case R.id.tv_exit_settingpage://退出登录
                dialog.show();
                break;
        }
    }

    @Override
    public void setData(Object o, int id) {
        switch (id) {
            case 0:
                VisitorLoginEntity entity = (VisitorLoginEntity) o;
                if(entity.getCode()==0){
                    LogUtils.e(" VisitorLogin成功","设置里退出的--------游客自动登录成功");
                    //游客二次登陆成功  保存信息
                    if(entity.getData()!=null){
                        UserUtils.saveVisitor(this,entity.getData());
                        UserUtils.setUserIdintify(this,entity.getData().getUsertype());
                        LogUtils.e("doVisitorLogin","设置里退出的--------游客自动登录成功，并成功保存信息");
                    }else{
                        LogUtils.e("doVisitorLogin","设置里退出的--------游客自动登录成功，保存信息失败--");
                    }
                     //清除之前的mainActivity  开启一个新的
                     CtrollAllactivitys.getActivity(MainActivity.class).finish();
                     startActivity(new Intent(SettingPageActivity.this,MainActivity.class));
                     SettingPageActivity.this.finish();
                }else {
                    LogUtils.e("VisitorLogin 失败 错误码-》",""+entity.getCode());
                    showLoading("");
                }
                break;
        }
    }

        @Override
        public void showLoading(String msg) {
            dialog1.show();
        }


    /*
     *  Fly 注：初始化退出的 dialog
     */
    public void initDig() {
        dialog = new AlertDialog.Builder(this, R.style.Theme_Transparent).create();
        View view =mLayoutInflater.inflate(R.layout.item_showdialog_upload_confirm, null);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        btnNegative = (AppCompatButton)view. findViewById(R.id.btn_negative);
        btnPositive = (AppCompatButton)view. findViewById(R.id.btn_positive);
        tvTitle.setText("确定退出登录？");
        btnNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        btnPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //正式退出，回到主页，变为游客，游客自动登录
                UserUtils.setUserIdintify(SettingPageActivity.this,0);//变为游客
               //游客自动登录
                bean=new VisitorLoginBean();
                bean.setPassword(UserUtils.getVisitor(SettingPageActivity.this).getPassword());
                bean.setUsername(UserUtils.getVisitor(SettingPageActivity.this).getUsername());
                mPrecenter.doVisitorLogin(bean);
                dialog.cancel();
            }
          });
                dialog.setView(view);
    }


   /*
   *  Fly 注：弹出更换绑定电话Dialog
   */
      public void showChangeBindDialog(){
          // 创建一个对象
          View view = View.inflate(this, R.layout.dialog_change_bindphone, null);
          View btnCancel =view.findViewById(R.id.btn_cancel_bindphone);//取消
          //显示对话框
          CstDialogN showBottonDialog = new CstDialogN(this, view, btnCancel);
          final Dialog dialog = showBottonDialog.show();
          Button btnChangeBindphone = (Button) view.findViewById(R.id.btn_change_bindphone);//绑定按钮
          // 设置监听
          btnChangeBindphone.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  startActivity(new Intent(SettingPageActivity.this,ChangeBindPhoneActivity.class));
                  dialog.dismiss();
              }
          });
      }


     /*
      *  Fly 注：  游客自动登录失败 时   重新请求或者退出应用 Dialog
      */
     // 游客自动登录失败 时   重新请求或者退出应用 Dialog
    AlertDialog dialog1;
    private TextView tvTitle1;
    private AppCompatButton btnNegative1;
    private AppCompatButton btnPositive1;
    public void  showRestarDig(){
        dialog1 = new AlertDialog.Builder(this, R.style.Theme_Transparent).create();
        dialog1.setCancelable(false);
        View view =mLayoutInflater.inflate(R.layout.item_showdialog_upload_confirm, null);
        tvTitle1 = (TextView) view.findViewById(R.id.tv_title);
        btnNegative1 = (AppCompatButton)view. findViewById(R.id.btn_negative);
        btnPositive1 = (AppCompatButton)view. findViewById(R.id.btn_positive);
        btnNegative1.setText("退出");
        btnPositive1.setText("重试");
        tvTitle1.setText("请求错误~");
        btnNegative1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingPageActivity.this.finish();
                CtrollAllactivitys.getActivity(MainActivity.class).finish();
                dialog1.cancel();
            }
        });
        btnPositive1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPrecenter.doVisitorLogin(bean);
                dialog1.cancel();
            }
        });
        dialog1.setView(view);
    }

}
