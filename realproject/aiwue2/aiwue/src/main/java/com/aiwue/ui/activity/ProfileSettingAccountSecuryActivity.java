package com.aiwue.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.aiwue.R;
import com.aiwue.base.BaseMvpActivity;
import com.aiwue.iview.IProfileAccountSeuryView;
import com.aiwue.presenter.ProfileAccountSecuryPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/4/14.
 */

public class ProfileSettingAccountSecuryActivity extends BaseMvpActivity<ProfileAccountSecuryPresenter> implements IProfileAccountSeuryView {
    @BindView(R.id.back_account)
    ImageView back_account;
    @BindView(R.id.resetpassword_account)
    RelativeLayout resetpassword_account;
    @BindView(R.id.qq_account)
    RelativeLayout qq_account;
    @BindView(R.id.wechat_account)
    RelativeLayout wechat_account;
    @BindView(R.id.weibo_account)
    RelativeLayout weibo_account;
    @BindView(R.id.phonenuber_account)
    RelativeLayout phonenuber_account;
    @BindView(R.id.email_account)
    RelativeLayout email_account;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }
    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_profilesetting_accountsecurity);
        ButterKnife.bind(this);
    }
    @Override
    protected void bindViews() {
    }
    @Override
    protected ProfileAccountSecuryPresenter createPresenter() {
        return new ProfileAccountSecuryPresenter(this);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }
    @Override
    protected void setListener() {
    }


    @OnClick({R.id.back_account,R.id.resetpassword_account, R.id.qq_account, R.id.wechat_account,R.id.weibo_account,
            R.id.phonenuber_account,R.id.email_account})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_account:
                finish();
                break;
            case R.id.resetpassword_account:
                startActivity(new Intent(ProfileSettingAccountSecuryActivity.this,ProfileResetPsswordActivity.class));
                break;
            case R.id.qq_account:
                showToast("功能暂未开放");
                break;
            case R.id.wechat_account:
                showToast("功能暂未开放");
                break;
            case R.id.weibo_account:
                showToast("功能暂未开放");
                break;
            case R.id.phonenuber_account:
                startActivity(new Intent(ProfileSettingAccountSecuryActivity.this,ProfilePhoneNumberActivity.class));
                break;
            case R.id.email_account:
                showToast("功能暂未开放");
                break;


        }
    }
}
