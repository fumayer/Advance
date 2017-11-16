package com.shortmeet.www.ui.PercenalCenter.activity;

import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.shortmeet.www.Base.BaseActivity;
import com.shortmeet.www.R;
import com.shortmeet.www.mvp.IMVPView;
 /*
  *  Fly 注：立即綁定手机
  */
public class BindConfirmPhoneActivity extends BaseActivity implements IMVPView, View.OnClickListener {
    //密码框
    private EditText edtPwdBindconfirmpage;
    //密码是否可见
    private ImageView imgvEyechooseBindconfirmpage;
    //立即绑定
    private TextView tvInstantbindBindconfirmpage;
    //眼睛是否可以看见
    private  boolean isOpen;

    @Override
    public boolean setUseTitleBar() {
        return true;
    }
    @Override
    public int setRootView() {
    return R.layout.activity_bind_confirm_phone;
    }

    @Override
    public void initView() {
    tvCenterTitleBar.setText("绑定手机号码");
    imgvLeftbackTitlebar.setVisibility(View.VISIBLE);
    edtPwdBindconfirmpage = (EditText) findViewById(R.id.edt_pwd_bindconfirmpage);
    imgvEyechooseBindconfirmpage = (ImageView) findViewById(R.id.imgv_eyechoose_bindconfirmpage);
    tvInstantbindBindconfirmpage = (TextView) findViewById(R.id.tv_instantbind_bindconfirmpage);
    }

    @Override
    public void initListener() {
      titleBarLeftframelayout.setOnClickListener(this);
      imgvEyechooseBindconfirmpage.setOnClickListener(this);
      tvInstantbindBindconfirmpage.setOnClickListener(this);
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
            case R.id.imgv_eyechoose_bindconfirmpage:
                if(isOpen){
                    imgvEyechooseBindconfirmpage.setImageResource(R.mipmap.yan_close_regist);
                    edtPwdBindconfirmpage.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }else{
                    imgvEyechooseBindconfirmpage.setImageResource(R.mipmap.yan_open_regist);
                    edtPwdBindconfirmpage.setInputType(InputType.TYPE_CLASS_TEXT);
                }
                isOpen=!isOpen;
                break;
            case R.id.tv_instantbind_bindconfirmpage:
                showMessage("立即绑定");
                break;
        }
    }
}
