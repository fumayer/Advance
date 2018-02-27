package com.aiwue.ui.activity;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aiwue.R;
import com.aiwue.base.BaseMvpActivity;
import com.aiwue.controller.UserController;
import com.aiwue.iview.IProfileSettingView;
import com.aiwue.model.Notice;
import com.aiwue.presenter.ProfileSettingPresenter;
import com.aiwue.utils.ConstantValue;
import com.aiwue.utils.SharedPreferencesMgr;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by chenhui on 2017/4/14.
 */
public class ProfileMyfriendActivity extends BaseMvpActivity<ProfileSettingPresenter> implements IProfileSettingView {
    @BindView(R.id.back_myfriendiv)
    ImageView back_myfriendiv;
    @BindView(R.id.attention_rela)
    RelativeLayout attention_rela;
    @BindView(R.id.fans_rela)
    RelativeLayout fans_rela;
    @BindView(R.id.adduser_iv)
    ImageView adduser_iv;
    @BindView(R.id.attention_txt)
    TextView attention_txt;
    @BindView(R.id.fans_txt)
    TextView fans_txt;
    @BindView(R.id.swipeRefreshlayout_myfriend)
    SwipeRefreshLayout swipeRefreshlayout_myfriend;

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_profiles_myfreiend);
        ButterKnife.bind(this);
    }
    @Override
    protected void bindViews() {
    }
    @Override
    protected ProfileSettingPresenter createPresenter() {
        return new ProfileSettingPresenter(this);
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

    @OnClick({R.id.back_myfriendiv, R.id.attention_rela, R.id.fans_rela,R.id.adduser_iv,})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_myfriendiv:
                finish();
                break;
            case R.id.attention_rela:
                attention_rela.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_profilefriend_attention));
                attention_txt.setTextColor(Color.BLACK);
                fans_rela.setBackgroundDrawable(null);
                fans_txt.setTextColor(Color.WHITE);
                break;
            case R.id.fans_rela:
                fans_rela.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_profilefriend_fans));
                fans_txt.setTextColor(Color.BLACK);
                attention_rela.setBackgroundDrawable(null);
                attention_txt.setTextColor(Color.WHITE);
                break;
            case R.id.adduser_iv:
                startActivity(new Intent(ProfileMyfriendActivity.this,ProfileAddUserActivity.class));
                break;

        }
    }
}
