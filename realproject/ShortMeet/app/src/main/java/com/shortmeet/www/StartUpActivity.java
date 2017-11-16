package com.shortmeet.www;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.TextView;

import com.aliyun.common.utils.ToastUtil;
import com.shortmeet.www.Base.BaseActivity;
import com.shortmeet.www.bean.personalCenter.VisitorLoginBean;
import com.shortmeet.www.entity.percenalCenter.VisitorLoginEntity;
import com.shortmeet.www.mvp.IMVPView;
import com.shortmeet.www.mvp.IMVPrecenter;
import com.shortmeet.www.utilsUsed.LogUtils;
import com.shortmeet.www.utilsUsed.UserUtils;

import java.util.ArrayList;
import java.util.List;

public class StartUpActivity extends BaseActivity implements IMVPView {
    /*
     *  Fly 注：申请权限
     */
    private static final String[] PERMISSIONS = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE };

    private static final  int PERMISSION_CODES = 1001;
    private boolean permissionGranted = true;
    /*
     *  Fly 注：IMVPrecenter
     */
     IMVPrecenter  mPrecenter;
    /*
     *  Fly 注： VisitorLoginBean  请求bean
     */
    VisitorLoginBean  bean;
    @Override
    public int setRootView() {
        return R.layout.activity_start_up;
    }
    @Override
    public void initView() {
        requestPerm(); //申请权限
        mPrecenter=new IMVPrecenter(this);
        showRestarDig();
    }
    @Override
    public void initListener() { }

    @Override
    public void initData() { }

    // Fly 注： 判断申请权限
    public void requestPerm(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermission();
        }else {
            skipActivity();
        }
    }
    @TargetApi(Build.VERSION_CODES.M)
    private void requestPermission(){
        List<String> p = new ArrayList<>();
        for(String permission :PERMISSIONS){
            if(ContextCompat.checkSelfPermission(this,permission) != PackageManager.PERMISSION_GRANTED){
                p.add(permission);
            }
        }
        if(p.size() > 0){
            requestPermissions(p.toArray(new String[p.size()]),PERMISSION_CODES);
        }else {
            skipActivity();
        }
    }
    //权限申请回掉
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_CODES:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED){
                    ToastUtil.showToast(this,"请给予相应的权限");
                    permissionGranted = false;
                }else {
                    permissionGranted = true;
                }
                break;
        }
        skipActivity();
    }
    /*
     *  Fly 注：跳转splash 页面  或者   main主页   或者login 页面
     */
    public void skipActivity(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(UserUtils.GetIsFirst(StartUpActivity.this)){
                    startActivity(new Intent(StartUpActivity.this,GuideActivity.class));
                    StartUpActivity.this.finish();
                }else{
                    if(UserUtils.getUserIdintify(StartUpActivity.this)==0){  //游客     （自动登录）  如果登录失败 重试 ，总之覆盖就得sessionid
                        LogUtils.e("StartUpActivity 游客身份自动登录","游客身份自动登录");
                        bean=new VisitorLoginBean();
                        bean.setPassword(UserUtils.getVisitor(StartUpActivity.this).getPassword());
                        bean.setUsername(UserUtils.getVisitor(StartUpActivity.this).getUsername());
                        mPrecenter.doVisitorLogin(bean);
                    }else if(UserUtils.getUserIdintify(StartUpActivity.this)==1){ //正式用户
                        LogUtils.e("您是正式用户哇","直接到主页喽~");
                        startActivity(new Intent(StartUpActivity.this, MainActivity.class));
                        StartUpActivity.this.finish();
                    }
                }

            }
        },1500);
    }



    @Override
    public void setData(Object o, int id) {
        switch (id) {
            case 0:
                VisitorLoginEntity entity = (VisitorLoginEntity) o;
               if(entity.getCode()==0){
                   LogUtils.e(" VisitorLogin成功","游客自动登录成功");
                   //游客二次登陆成功  保存信息
                   if(entity.getData()!=null){
                       UserUtils.saveVisitor(this,entity.getData());
                       UserUtils.setUserIdintify(this,entity.getData().getUsertype());
                       LogUtils.e("doVisitorLogin","游客自动登录成功，并成功保存信息");
                   }else{
                       LogUtils.e("doVisitorLogin","游客自动登录成功，保存信息失败--");
                   }
                   startActivity(new Intent(StartUpActivity.this, MainActivity.class));
                   StartUpActivity.this.finish();
               }else {
                   LogUtils.e("VisitorLogin 失败 错误码-》",""+entity.getCode());
                   showLoading("");
               }
                break;
        }
    }

    @Override
    public void showLoading(String msg) {
        dialog.show();
    }
    // 游客自动登录失败 时   重新请求或者退出
    AlertDialog dialog;
    private TextView tvTitle;
    private AppCompatButton btnNegative;
    private AppCompatButton btnPositive;
    public void  showRestarDig(){
        dialog = new AlertDialog.Builder(this, R.style.Theme_Transparent).create();
        dialog.setCancelable(false);
        View view =mLayoutInflater.inflate(R.layout.item_showdialog_upload_confirm, null);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        btnNegative = (AppCompatButton)view. findViewById(R.id.btn_negative);
        btnPositive = (AppCompatButton)view. findViewById(R.id.btn_positive);
        btnNegative.setText("退出");
        btnPositive.setText("重试");
        tvTitle.setText("请求错误~");
        btnNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartUpActivity.this.finish();
                dialog.cancel();
            }
        });
        btnPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPrecenter.doVisitorLogin(bean);
                dialog.cancel();
            }
        });
        dialog.setView(view);
    }

}
