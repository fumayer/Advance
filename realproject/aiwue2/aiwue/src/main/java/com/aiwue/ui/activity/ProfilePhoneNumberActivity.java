package com.aiwue.ui.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.ImageView;

import com.aiwue.R;
import com.aiwue.base.BaseMvpActivity;
import com.aiwue.iview.IProfileBindPhonenumberView;
import com.aiwue.iview.IProfileResetPasswordView;
import com.aiwue.presenter.ProfileBindPhonenumberPresenter;
import com.aiwue.presenter.ProfileResetPasswordPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/4/18.
 */

public class ProfilePhoneNumberActivity extends BaseMvpActivity<ProfileBindPhonenumberPresenter> implements IProfileBindPhonenumberView {

    @BindView(R.id.back_phonebind)
    ImageView back_phonebind;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

    }
    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_profilesetting_account_bindphonenum);
        ButterKnife.bind(this);
    }
    @Override
    protected void bindViews() {
    }
    @Override
    protected ProfileBindPhonenumberPresenter createPresenter() {
        return new ProfileBindPhonenumberPresenter(this);
    }
    @Override
    protected void processLogic(Bundle savedInstanceState) {
    }
    @Override
    protected void setListener() {
    }

    @OnClick({R.id.back_phonebind})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_phonebind:
                finish();
                break;

        }
    }

}
