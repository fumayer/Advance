package com.shortmeet.www.ui.PercenalCenter.activity;

import android.content.Intent;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cunoraz.gifview.library.GifView;
import com.shortmeet.www.Base.BaseActivity;
import com.shortmeet.www.MainActivity;
import com.shortmeet.www.R;
import com.shortmeet.www.bean.personalCenter.LoginBean;
import com.shortmeet.www.entity.percenalCenter.LoginEntity;
import com.shortmeet.www.mvp.IMVPView;
import com.shortmeet.www.mvp.IMVPrecenter;
import com.shortmeet.www.utilsUsed.StringUtils;
import com.shortmeet.www.utilsUsed.UserUtils;
import com.shortmeet.www.views.widgetPart.RatioImageView;
/*
 *  Fly 注：登录
 */
public class LoginActivity extends BaseActivity implements IMVPView,View.OnClickListener {
    //头像
    private RatioImageView imgvHeadLogin;
    //登陆号码
    private EditText edtLoginPhone;
    //登录密码
    private EditText edtLoginPwd;
    //  imgv  眼睛图标
    private ImageView imgvEyechooseLogin;
    // 登陆
    private TextView tvLogin;
    //登录后面3个白点
    private GifView gifThreePointLogin;
    //忘记密码
    private TextView tvForgotpwdLoginfrag;
    //去注册
    private TextView tvGoregistLoginfrag;
    //微信登录
    private ImageView imgvWeixinLogin;
    //QQ登录
    private ImageView imgvQqLogin;
    //微博登录
    private ImageView imgvWeibologin;
    //眼睛是否可以看见
    private  boolean isOpen;
    //precenter
    private IMVPrecenter mPrecenter;

    @Override
    public boolean setUseTitleBar() {
        return true;
    }
    @Override
    public int setRootView() {
        return R.layout.activity_login;
    }
    @Override
    public void initView() {
     tvCenterTitleBar.setText("登录");
     imgvHeadLogin = (RatioImageView) findViewById(R.id.imgv_head_login);
     edtLoginPhone = (EditText) findViewById(R.id.edt_login_phone);
     edtLoginPwd = (EditText) findViewById(R.id.edt_login_pwd);
     imgvEyechooseLogin = (ImageView) findViewById(R.id.imgv_eyechoose_login);
     tvLogin = (TextView)findViewById(R.id.tv_login);
     tvForgotpwdLoginfrag = (TextView) findViewById(R.id.tv_forgotpwd_loginfrag);
     tvGoregistLoginfrag = (TextView) findViewById(R.id.tv_goregist_loginfrag);
     imgvWeixinLogin = (ImageView) findViewById(R.id.imgv_weixin_login);
     imgvQqLogin = (ImageView) findViewById(R.id.imgv_qq_login);
     imgvWeibologin = (ImageView) findViewById(R.id.imgv_weibologin);
     gifThreePointLogin = (GifView) findViewById(R.id.gif_three_point_login);
     mPrecenter=new IMVPrecenter(this,this.getBaseContext());
    }
    @Override
    public void initData() {

    }
    @Override
    public void initListener() {
        tvLogin.setOnClickListener(this);
        imgvEyechooseLogin.setOnClickListener(this);
        tvForgotpwdLoginfrag.setOnClickListener(this);
        tvGoregistLoginfrag.setOnClickListener(this);
        imgvWeixinLogin.setOnClickListener(this);
        imgvQqLogin.setOnClickListener(this);
        imgvWeibologin.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        String phone=edtLoginPhone.getText().toString();
        String pwd=edtLoginPwd.getText().toString();
        switch (v.getId()) {
            case R.id.tv_login:
                if(checkPhoneAndPwdForm(phone,pwd)){
                    LoginBean bean=new LoginBean();
                    bean.setPhone(phone);
                    bean.setPassword(pwd);
                    gifThreePointLogin.setVisibility(View.VISIBLE);
                    gifThreePointLogin.play();
                    tvLogin.setText("登录中");
                    mPrecenter.doLoginInter(bean);
                }
                break;
            case R.id.imgv_eyechoose_login:
                if(isOpen){
                    imgvEyechooseLogin.setImageResource(R.mipmap.yan_close_regist);
                    edtLoginPwd.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }else{
                    imgvEyechooseLogin.setImageResource(R.mipmap.yan_open_regist);
                    edtLoginPwd.setInputType(InputType.TYPE_CLASS_TEXT);
                }
                isOpen=!isOpen;
                break;
            case R.id.tv_forgotpwd_loginfrag:

                break;
            case R.id.tv_goregist_loginfrag:
                Intent  intent=new Intent(this,RegistActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.imgv_weixin_login:

                break;
            case R.id.imgv_weibologin:

                break;
            case R.id.imgv_qq_login:

                break;
        }
    }
    /*
     *  Fly 注：检查 手机和密码格式
     */
    public boolean checkPhoneAndPwdForm(String phone,String pwd){
        if(TextUtils.isEmpty(phone)){
            showMessage("请输入您的手机号码");
            return  false;
        }
        if(!StringUtils.isMobileNO(phone)){
            showMessage("号码格式有误，请重新输入");
            return  false;
        }
        if(TextUtils.isEmpty(pwd)){
            showMessage("请输入密码");
            return false;
        }
        return true;
    }

    @Override
    public void setData(Object o, int id) {
        switch (id) {
            case 0:
                LoginEntity loginEntity = (LoginEntity) o;
                if (loginEntity.getCode() == 0) {
                    // Fly 注：登录成功 保存返回回来的用户信息
                    if (loginEntity.getData() != null) {
                        UserUtils.saveUser(this, loginEntity.getData());
                        Log.e("doLoginInter", "登录保存用戶信息成功！并且变为正式用户");
                    } else {
                        Log.e("doLoginInter", "登录保存用戶信息失败！");
                    }
                    this.showMessage("登录成功");
                    resetLoginButton();
                    UserUtils.setUserIdintify(this, 1);//升级为正式
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    this.finish();
                } else if (loginEntity.getCode() == 10003) {
                    this.showMessage("用户不存在");
                    resetLoginButton();
                } else if (loginEntity.getCode() == 10004) {
                    this.showMessage("密码错误，请重新输入");
                    resetLoginButton();
                } else {
                    this.showMessage("登录失败");
                    resetLoginButton();
                }
        }
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(gifThreePointLogin.isPlaying()){
            gifThreePointLogin.pause();
        }
        gifThreePointLogin.setVisibility(View.GONE);
    }

    public void resetLoginButton(){
        tvLogin.setText("登录");
        gifThreePointLogin.pause();
        gifThreePointLogin.setVisibility(View.GONE);
    }

}
