package com.aiwue.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.aiwue.R;
import com.aiwue.base.BaseMvpActivity;
import com.aiwue.iview.IProfileMypersonalInfoView;
import com.aiwue.model.Notice;
import com.aiwue.presenter.ProfileMypersonalInfoPresenter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by chenhui on 2017/4/14.
 */
public class ProfileMyInfomationActivity extends BaseMvpActivity<ProfileMypersonalInfoPresenter> implements IProfileMypersonalInfoView {

    @BindView(R.id.back_myinfomation)
    ImageView back_myinfomation;

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_profile_mypersonalinfo);
        ButterKnife.bind(this);
    }
    @Override
    protected void bindViews() {
    }
    @Override
    protected ProfileMypersonalInfoPresenter createPresenter() {
        return new ProfileMypersonalInfoPresenter(this);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }
    @Override
    protected void setListener() {
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnEvent(Notice event) {
    }
    @OnClick({R.id.back_myinfomation})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_myinfomation:
                finish();
                break;
        }
    }
}
