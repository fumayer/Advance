package com.shortmeet.www.ui.PercenalCenter.activity;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.shortmeet.www.Base.BaseActivity;
import com.shortmeet.www.R;
import com.shortmeet.www.mvp.IMVPView;

/*
 *  Fly 注：绑定手机
 */
public class BindPhoneNumberActivity extends BaseActivity implements IMVPView, View.OnClickListener {
   // 手机号码
    private EditText edtPhoneBindphonepage;
    //输入短信验证码
    private EditText edtDuanxincodeBindphonepage;
    //获取验证码
    private TextView tvGetverfyBindphonepage;
    //下一步
    private TextView tvNextBindphonepage;

    @Override
    public int setRootView() {
        return R.layout.activity_bind_phone_number;
    }

    @Override
    public boolean setUseTitleBar() {
        return true;
    }

    @Override
    public void initView() {
     tvCenterTitleBar.setText("绑定手机号码");
     imgvLeftbackTitlebar.setVisibility(View.VISIBLE);
     edtPhoneBindphonepage = (EditText) findViewById(R.id.edt_phone_bindphonepage);
     edtDuanxincodeBindphonepage = (EditText) findViewById(R.id.edt_duanxincode_bindphonepage);
     tvGetverfyBindphonepage = (TextView) findViewById(R.id.tv_getverfy_bindphonepage);
     tvNextBindphonepage = (TextView) findViewById(R.id.tv_next_bindphonepage);
    }

    @Override
    public void initListener() {
    titleBarLeftframelayout.setOnClickListener(this);
    tvGetverfyBindphonepage.setOnClickListener(this);
    tvNextBindphonepage.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void setData(Object o, int id) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_bar_leftframelayout:
               this.finish();
                break;
            case R.id.tv_getverfy_bindphonepage:

                break;
            case R.id.tv_next_bindphonepage:
              //去confirm 前判断
              startActivity(new Intent(this,BindConfirmPhoneActivity.class));
                break;
        }
    }
}
