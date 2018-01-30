package com.aiwue.ui.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.ImageView;

import com.aiwue.R;
import com.aiwue.base.BaseMvpActivity;
import com.aiwue.iview.IProfileServiceAndPricacyView;
import com.aiwue.presenter.ProfileServiceAndPrivacyPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/4/19
 */

public class ProfileServiceAndPrivaceActivity extends BaseMvpActivity<ProfileServiceAndPrivacyPresenter> implements IProfileServiceAndPricacyView {

    @BindView(R.id.back_service_privacy)
    ImageView back_service_privacy;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }
    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_profiles_service_privacy);
        ButterKnife.bind(this);
    }
    @Override
    protected void bindViews() {
    }
    @Override
    protected ProfileServiceAndPrivacyPresenter createPresenter() {
        return new ProfileServiceAndPrivacyPresenter(this);
    }
    @Override
    protected void processLogic(Bundle savedInstanceState) {
    }
    @Override
    protected void setListener() {
    }
    @OnClick({R.id.back_service_privacy})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_service_privacy:
                finish();
                break;
        }
    }
}