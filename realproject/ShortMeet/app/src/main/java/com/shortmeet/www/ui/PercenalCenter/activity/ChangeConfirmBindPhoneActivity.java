package com.shortmeet.www.ui.PercenalCenter.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.shortmeet.www.Base.BaseActivity;
import com.shortmeet.www.R;
import com.shortmeet.www.bean.personalCenter.BindPhoneBean;
import com.shortmeet.www.bean.personalCenter.VerifyBean;
import com.shortmeet.www.entity.percenalCenter.BindPhoneEntity;
import com.shortmeet.www.entity.percenalCenter.GetVerifyEntity;
import com.shortmeet.www.mvp.IMVPView;
import com.shortmeet.www.mvp.IMVPrecenter;
import com.shortmeet.www.utilsUsed.LogUtils;
import com.shortmeet.www.utilsUsed.StringUtils;
import com.shortmeet.www.utilsUsed.TimeCountUtil;
import com.shortmeet.www.utilsUsed.UserUtils;

public class ChangeConfirmBindPhoneActivity extends BaseActivity implements IMVPView, View.OnClickListener {
    // 电话
    private EditText edtPhoneChangeconfirm;
    //短信
    private EditText edtDuanxincodeChangeconfirm;
    //获取验证码
    private TextView tvGetverfyChangeconfirm;
    //立即绑定
    private TextView tvLijibindChangeconfirm;
    //precenter
    private IMVPrecenter mPrecenter;
    //传过来的 phone  verfycode
    private String gotphone;
    private int gotcode;
    @Override
    public boolean setUseTitleBar() {
        return true;
    }
    @Override
    public int setRootView() {
     return R.layout.activity_change_confirm_bind_phone;
    }

    @Override
    public void initView() {
     tvCenterTitleBar.setText("更换绑定手机");
     mPrecenter=new IMVPrecenter(this);
     imgvLeftbackTitlebar.setVisibility(View.VISIBLE);
     edtPhoneChangeconfirm = (EditText) findViewById(R.id.edt_phone_changeconfirm);
     edtDuanxincodeChangeconfirm = (EditText) findViewById(R.id.edt_duanxincode_changeconfirm);
     tvGetverfyChangeconfirm = (TextView) findViewById(R.id.tv_getverfy_changeconfirm);
     tvLijibindChangeconfirm = (TextView) findViewById(R.id.tv_lijibind_changeconfirm);
      gotphone=this.getIntent().getStringExtra("prev_phone");
      gotcode=this.getIntent().getIntExtra("prev_code",0);
    }

    @Override
    public void initListener() {
    titleBarLeftframelayout.setOnClickListener(this);
    tvGetverfyChangeconfirm.setOnClickListener(this);
    tvLijibindChangeconfirm.setOnClickListener(this);
    }

    @Override
    public void initData() { }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_bar_leftframelayout:
                this.finish();
                break;
            case R.id.tv_getverfy_changeconfirm:
                String phone1=edtPhoneChangeconfirm.getText().toString();
                if(TextUtils.isEmpty(phone1)){
                    showMessage("请输入您的手机号码");
                    return ;
                }
                VerifyBean bean=new VerifyBean();
                bean.setPhone(phone1);
                bean.setType(1);
                mPrecenter.getVerify(bean);
                break;
            case R.id.tv_lijibind_changeconfirm:
                String phone=edtPhoneChangeconfirm.getText().toString();
                String verfycode=edtDuanxincodeChangeconfirm.getText().toString();
                if(doCheckPhoneAndVerfy(phone,verfycode)){
                    BindPhoneBean bindPhoneBean=new BindPhoneBean();
                    bindPhoneBean.setSessionid(UserUtils.getSessionId(this));
                    bindPhoneBean.setType(2);
                    bindPhoneBean.setPhone(phone);
                    bindPhoneBean.setPassword("0");
                    bindPhoneBean.setCode(Integer.valueOf(verfycode));
                    bindPhoneBean.setPrev_phone(gotphone);
                    bindPhoneBean.setPrev_code(gotcode);
                   mPrecenter.bindPhoneInstant(bindPhoneBean);
                }
                break;
        }
    }
    public boolean doCheckPhoneAndVerfy(String phone,String verfycode){
        if(TextUtils.isEmpty(phone)){
            showMessage("请输入您的手机号码");
            return  false;
        }
        if(!StringUtils.isMobileNO(phone)){
            showMessage("号码格式有误，请重新输入");
            return  false;
        }
        if (TextUtils.isEmpty(verfycode) || verfycode.length() > 6) {
            showMessage("请填写正确验证码");
            return false;
        }
        return  true;
    }
    @Override
    public void setData(Object o, int id) {
        switch (id) {
            case 0:
                GetVerifyEntity verifyEntity= (GetVerifyEntity) o;
              if(verifyEntity.getCode()==0){
               this.showMessage("验证码发送成功");
               new TimeCountUtil(this, 60000, 1000, tvGetverfyChangeconfirm).start();
                }else if(verifyEntity.getCode()==2008){
                 this.showMessage("手机号已经被绑，请使用其他手机号");
                 LogUtils.e("验证码失败---",verifyEntity.getCode());
                }else{
                    this.showMessage("发送失败");
                    LogUtils.e("验证码失败",verifyEntity.getCode());
                }
                break;
            case 1:
                BindPhoneEntity bindPhoneEntity= (BindPhoneEntity) o;
                if(bindPhoneEntity.getCode()==0){
                 this.showMessage("绑定成功");
               //绑定成功后   更新本地存储的手机号
                UserUtils.getUser(this).setPhone(edtPhoneChangeconfirm.getText().toString());
                LogUtils.e("绑定成功","绑定成功后   更新本地存储的手机号");
                }else if(bindPhoneEntity.getCode()==2009){
                  this.showMessage("验证码错误");
                  LogUtils.e("验证码错误---",bindPhoneEntity.getCode());
                }else if(bindPhoneEntity.getCode()==2010){
                    this.showMessage("验证码失效");
                    LogUtils.e("验证码失效---",bindPhoneEntity.getCode());
                } else{
                    this.showMessage("未知错误");
                    LogUtils.e("未知错误----",bindPhoneEntity.getCode());
                }
                break;
        }
    }
}
