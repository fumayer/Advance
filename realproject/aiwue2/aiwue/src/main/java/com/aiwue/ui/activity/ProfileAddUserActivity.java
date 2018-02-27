package com.aiwue.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.aiwue.R;
import com.aiwue.base.BaseMvpActivity;
import com.aiwue.iview.IProfileAddUserView;
import com.aiwue.presenter.ProfileAddUserPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/4/14.
 */

public class ProfileAddUserActivity extends BaseMvpActivity<ProfileAddUserPresenter>implements IProfileAddUserView{
    @BindView(R.id.back_adduser_iv)
    ImageView back_adduser_iv;
    @Override
    protected ProfileAddUserPresenter createPresenter() {
        return new ProfileAddUserPresenter(this);
    }
    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_profilesmyfreiend_adduser);
        ButterKnife.bind(this);
    }
    @Override
    protected void bindViews() {
    }
    @Override
    protected void processLogic(Bundle savedInstanceState) {
    }
    @Override
    protected void setListener() {
    }

    @OnClick(R.id.back_adduser_iv)
    public void onClick(View v){
        switch (v.getId()){
            case R.id.back_adduser_iv:
                finish();
                break;
        }
    }




}
