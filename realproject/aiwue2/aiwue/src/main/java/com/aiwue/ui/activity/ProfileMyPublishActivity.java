package com.aiwue.ui.activity;

import android.os.Bundle;

import android.view.View;
import android.widget.ImageView;

import com.aiwue.R;
import com.aiwue.base.BaseMvpActivity;
import com.aiwue.iview.IProfileMyissueView;

import com.aiwue.model.Notice;
import com.aiwue.presenter.ProfileMyissuePresenter;

import com.aiwue.utils.MyIssueDialog;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 *  我的发布页面
 * Created by Chenhui on 2017年4月20日
 * Copyright (c) 2017 aiwue.com All rights reserved
 */
public class ProfileMyPublishActivity extends BaseMvpActivity<ProfileMyissuePresenter> implements IProfileMyissueView {
    @BindView(R.id.back_myissue)
    ImageView back_myissue;

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_profile_myissue);
        ButterKnife.bind(this);
    }
    @Override
    protected void bindViews() {
    }
    @Override
    protected ProfileMyissuePresenter createPresenter() {
        return new ProfileMyissuePresenter(this);
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
    @OnClick({R.id.back_myissue,R.id.share_myissue})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_myissue:
                finish();
                break;
            case R.id.share_myissue:
                MyIssueDialog dialog = new MyIssueDialog(this);
                dialog.show();;
        }
    }
}
