package com.shortmeet.www.ui.PercenalCenter.activity;

import android.content.Intent;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.cunoraz.gifview.library.GifView;
import com.shortmeet.www.Base.BaseActivity;
import com.shortmeet.www.R;
import com.shortmeet.www.bean.personalCenter.RegistBean;
import com.shortmeet.www.bean.personalCenter.VerifyBean;
import com.shortmeet.www.entity.percenalCenter.GetVerifyEntity;
import com.shortmeet.www.entity.percenalCenter.RegistEntity;
import com.shortmeet.www.mvp.IMVPView;
import com.shortmeet.www.mvp.IMVPrecenter;
import com.shortmeet.www.utilsUsed.LogUtils;
import com.shortmeet.www.utilsUsed.PhoneIDUtils;
import com.shortmeet.www.utilsUsed.StringUtils;
import com.shortmeet.www.utilsUsed.TimeCountUtil;
import com.shortmeet.www.utilsUsed.UserUtils;
import com.shortmeet.www.views.widgetPart.RatioImageView;
/*
 *  Fly 注： 注册
 */
public class RegistActivity extends BaseActivity implements IMVPView,View.OnClickListener{
    //srcrollview
    private ScrollView scrollviewRegist;
    //头像
    private RatioImageView imgvHeadRegist;
    // 注册号码
    private EditText edtRegisterPhone;
    //注册 密码
    private EditText edtRegisterPwd;
    //  眼睛是否看见密码
    private ImageView imgvEyeRegist;
    // 短信 验证码输入
    private EditText edtDuanxincodeRegist;
    //  获取验证码
    private TextView tvGetverfyRegist;
    //注册
    private TextView tvRegist;
    //注册后面3个点
    private GifView gifThreePointReg;
    //去登录
    private TextView tvGologin;
    //同意协议
    private CheckBox cbAgreexieyi;
    //眼睛是否可以看见
    private  boolean isOpen;
    //precenter
    private IMVPrecenter  mPrecenter;
    @Override
    public int setRootView() {
        return R.layout.activity_regist;
    }
    @Override
    public boolean setUseTitleBar() {
        return true;
    }
    @Override
    public void initView() {
    tvCenterTitleBar.setText("注册");
    scrollviewRegist = (ScrollView) findViewById(R.id.scrollview_regist);
    imgvHeadRegist = (RatioImageView) findViewById(R.id.imgv_head_regist);
    edtRegisterPhone = (EditText) findViewById(R.id.edt_register_phone);
    edtRegisterPwd = (EditText) findViewById(R.id.edt_register_pwd);
    imgvEyeRegist = (ImageView) findViewById(R.id.imgv_eye_regist);
    edtDuanxincodeRegist = (EditText) findViewById(R.id.edt_duanxincode_regist);
    tvGetverfyRegist = (TextView) findViewById(R.id.tv_getverfy_regist);
    tvRegist = (TextView) findViewById(R.id.tv_regist);
    tvGologin = (TextView) findViewById(R.id.tv_gologin);
    cbAgreexieyi = (CheckBox) findViewById(R.id.cb_agreexieyi);
    gifThreePointReg = (GifView) findViewById(R.id.gif_three_point_reg);
    cbAgreexieyi.setChecked(true);
     mPrecenter =new IMVPrecenter(this);
    }

    @Override
    public void initListener() {
        imgvEyeRegist.setOnClickListener(this);
        tvGetverfyRegist.setOnClickListener(this);
        tvRegist.setOnClickListener(this);
        tvGologin.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        String phone=edtRegisterPhone.getText().toString();
        String pwd=edtRegisterPwd.getText().toString();
        String verify=edtDuanxincodeRegist.getText().toString();
        switch (v.getId()) {
            case R.id.imgv_eye_regist:
                if(isOpen){
               imgvEyeRegist.setImageResource(R.mipmap.yan_close_regist);
               edtRegisterPwd.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }else{
                imgvEyeRegist.setImageResource(R.mipmap.yan_open_regist);
                edtRegisterPwd.setInputType(InputType.TYPE_CLASS_TEXT);
                }
                isOpen=!isOpen;
                break;
            case R.id.tv_getverfy_regist:
                if(checkPhone(phone)){
                    VerifyBean  bean=new VerifyBean();
                    bean.setPhone(phone);
                    bean.setType(0);
                   mPrecenter.getVerify(bean);
                }
                break;
            case R.id.tv_regist:
            doCheckRegist(phone,pwd,verify);
                break;
            case R.id.tv_gologin:
                Intent   intent=new Intent(this,LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
    @Override
    public void initData() {
    }

    /*
     *  Fly 注： 发送手机号码前 检查格式
     */
    public boolean checkPhone(String phone){
        if(TextUtils.isEmpty(phone)){
          showMessage("请输入您的手机号码");
          return  false;
        }
        if(!StringUtils.isMobileNO(phone)){
            showMessage("手机号码格式有误，请重新输入");
            return  false;
        }
        return  true;
    }

    /*
     *  Fly 注： 注册前格式检查
     */
    public void doCheckRegist(String phone,String pwd,String verfy){
        if(!checkPhone(phone)){
            return;
        }
        if(TextUtils.isEmpty(pwd)){
            showMessage("密码不能为空");
            return;
        }
        if(pwd.contains(" ")||pwd.length()<6||pwd.length()>15){
            showMessage("请输入6-15间的任意字符密码");
            return;
        }
        if (TextUtils.isEmpty(verfy) || verfy.length() > 6) {
            showMessage("请填写正确验证码");
            return;
        }
        if(!cbAgreexieyi.isChecked()){
            showMessage("请同意用户协议");
            return;
        }
        RegistBean registBean=new RegistBean();
        registBean.setPhone(phone);
        registBean.setPassword(pwd);
        registBean.setCode(verfy);
        registBean.setDevice_no(PhoneIDUtils.getDeviceId(this));
        registBean.setVisitor_name(UserUtils.getVisitor(this).getUsername());
        registBean.setUser_sub_type(1);
        gifThreePointReg.setVisibility(View.VISIBLE);
        gifThreePointReg.play();
        tvRegist.setText("注册中");
        mPrecenter.doRegist(registBean);
    }

    @Override
    public void setData(Object o, int id) {
        switch (id) {
            case 0://发送验证码成功
                GetVerifyEntity verifyEntity= (GetVerifyEntity) o;
                if(verifyEntity.getCode()==0){
                    this.showMessage("验证码发送成功");
                    new TimeCountUtil(this, 60000, 1000, tvGetverfyRegist).start();
                }else if(verifyEntity.getCode()==10203){
                    this.showMessage("手机号已注册");
                }else{
                    this.showMessage("发送失败");
                    LogUtils.e("验证码失败",verifyEntity.getCode());
                }
                break;
            case 1://注册
                RegistEntity registEntity= (RegistEntity) o;
                if(registEntity.getCode()==0){
                    // Fly 注：注册成功 保存返回回来的用户信息  成为正式用户
                    if(registEntity.getData()!=null){
                        UserUtils.saveUser(this.getBaseContext(),registEntity.getData());
                        Log.e("doRegist","注冊保存用戶信息成功！并升级为正式用户");
                    } else{
                        Log.e("doRegist","注冊保存用戶信息失败！");
                    }
                  this.showMessage("注册成功");
                  UserUtils.setUserIdintify(this,1); //升级为正式
                  Intent intent=new Intent(this, PerfectUserInfoActivity.class);
                  startActivity(intent);
                  finish();
                }else  if(registEntity.getCode()==10305){
                    this.showMessage("用户已存在");
                    resetRegistButton();
                }else if(registEntity.getCode()==10306){
                    this.showMessage("验证码错误");
                    resetRegistButton();
                }else if(registEntity.getCode()==10307){
                    this.showMessage("验证码已过期");
                    resetRegistButton();
                }else{
                    this.showMessage("注册失败");
                    LogUtils.e("注册失败","错误码"+registEntity.getCode());
                    resetRegistButton();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(gifThreePointReg.isPlaying()){
            gifThreePointReg.pause();
        }
        gifThreePointReg.setVisibility(View.GONE);
    }

    public void resetRegistButton(){
        tvRegist.setText("注册");
        gifThreePointReg.pause();
        gifThreePointReg.setVisibility(View.GONE);
    }
}
