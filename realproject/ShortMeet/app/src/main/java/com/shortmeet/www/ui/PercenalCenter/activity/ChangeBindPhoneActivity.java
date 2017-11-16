package com.shortmeet.www.ui.PercenalCenter.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.shortmeet.www.Base.BaseActivity;
import com.shortmeet.www.R;
import com.shortmeet.www.bean.personalCenter.CheckVerfyBean;
import com.shortmeet.www.bean.personalCenter.VerifyBean;
import com.shortmeet.www.entity.percenalCenter.CheckVerfyEntity;
import com.shortmeet.www.entity.percenalCenter.GetVerifyEntity;
import com.shortmeet.www.mvp.IMVPView;
import com.shortmeet.www.mvp.IMVPrecenter;
import com.shortmeet.www.utilsUsed.LogUtils;
import com.shortmeet.www.utilsUsed.TimeCountUtil;
import com.shortmeet.www.utilsUsed.UserUtils;

/*
 *  Fly 注：更换 绑定手机
 */
public class ChangeBindPhoneActivity extends BaseActivity implements IMVPView, View.OnClickListener {
    //手机号码
    private TextView tvPhonenumberChangebind;
    //短信
    private EditText edtDuanxincodeChangebind;
    //获取验证码
    private TextView tvGetverfyChangebind;
    // 下一步
    private TextView tvNextChangebind;
   //precenter
    private IMVPrecenter mPrecenter;
    @Override
    public int setRootView() {
        return R.layout.activity_change_bind_phone;
    }

    @Override
    public boolean setUseTitleBar() {
        return true;
    }

    @Override
    public void initView() {
    tvCenterTitleBar.setText("手机验证");
    mPrecenter=new IMVPrecenter(this);
    imgvLeftbackTitlebar.setVisibility(View.VISIBLE);
    tvPhonenumberChangebind = (TextView) findViewById(R.id.tv_phonenumber_changebind);
    edtDuanxincodeChangebind = (EditText) findViewById(R.id.edt_duanxincode_changebind);
    tvGetverfyChangebind = (TextView) findViewById(R.id.tv_getverfy_changebind);
    tvNextChangebind = (TextView) findViewById(R.id.tv_next_changebind);
    tvPhonenumberChangebind.setText("+86-"+ UserUtils.getUser(this).getPhone());
    }
    @Override
    public void initListener() {
    titleBarLeftframelayout.setOnClickListener(this);
    tvGetverfyChangebind.setOnClickListener(this);
    tvNextChangebind.setOnClickListener(this);
    }

    @Override
    public void initData() { }
    /*
     *  Fly 注：点击监听
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_bar_leftframelayout:
               this.finish();
                break;
            case R.id.tv_getverfy_changebind:
             VerifyBean bean=new VerifyBean();
             bean.setPhone(UserUtils.getUser(this).getPhone());
             bean.setType(2);
             mPrecenter.getVerify(bean);
                break;
            case R.id.tv_next_changebind:
                 String verfyCode= edtDuanxincodeChangebind.getText().toString();
                if (TextUtils.isEmpty(verfyCode) || verfyCode.length() > 6) {
                    showMessage("请填写正确验证码");
                    return;
                }
                //做判断验证码  联网访问
                CheckVerfyBean  bean1=new CheckVerfyBean();
                bean1.setPhone(UserUtils.getUser(this).getPhone());
                bean1.setCode(Integer.valueOf(verfyCode));
                bean1.setSessionid(UserUtils.getSessionId(this));
                bean1.setType(2);
                mPrecenter.justVerfyCode(bean1);
                break;
        }
    }
    /*
     *  Fly 注：联网回调
     */
    @Override
    public void setData(Object o, int id) {
        switch (id) {
            case 0:
                GetVerifyEntity verifyEntity= (GetVerifyEntity) o;
                if(verifyEntity.getCode()==0){
                    this.showMessage("验证码发送成功");
                    new TimeCountUtil(this, 60000, 1000, tvGetverfyChangebind).start();
                }else if(verifyEntity.getCode()==10203){
                   // this.showMessage("手机号已注册");
                   LogUtils.e("验证码失败---",verifyEntity.getCode());
                }else{
                    this.showMessage("发送失败");
                    LogUtils.e("验证码失败",verifyEntity.getCode());
                }
                break;
            case 1:
                CheckVerfyEntity entity= (CheckVerfyEntity) o;
                if(entity.getCode()==0){ //判断后   验证成功
                Intent  intent=new Intent(this,ChangeConfirmBindPhoneActivity.class)     ;
                  intent.putExtra("prev_phone",entity.getData().getPrev_phone());
                  intent.putExtra("prev_code",entity.getData().getPrev_code());
                  startActivity(intent);
                  this.finish();
                }else if(entity.getCode()==2009){
                    this.showMessage("验证码错误，请重新输入");
                }else if(entity.getCode()==2010){
                    this.showMessage("验证码已失效");
                    LogUtils.e("验证码已失效","验证码已失效"+entity.getCode());
                }else{
                    showMessage("未知错误");
                    LogUtils.e("验证码已失效","验证码已失效"+entity.getCode());
                }
                break;
        }
    }
}
