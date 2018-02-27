package com.aiwue.ui.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.aiwue.R;
import com.aiwue.base.BaseMvpActivity;
import com.aiwue.iview.IProfileBindPhonenumberView;
import com.aiwue.presenter.ProfileBindPhonenumberPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/4/14.
 */

public class ProfileBindPhoneNumberActivity extends BaseMvpActivity<ProfileBindPhonenumberPresenter> implements IProfileBindPhonenumberView {

    @BindView(R.id.back_phonebind)
    ImageView back_phonebind;
    @BindView(R.id.revise_btn)
    EditText revise_btn ;

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
    @OnClick({R.id.back_phonebind, R.id.revise_btn})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_phonebind:
                finish();
                break;
            case R.id.revise_btn:
                finish();
                break;
        }
    }
}
