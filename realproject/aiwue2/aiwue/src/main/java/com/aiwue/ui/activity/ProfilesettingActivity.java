package com.aiwue.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.aiwue.R;
import com.aiwue.base.BaseMvpActivity;
import com.aiwue.controller.UserController;
import com.aiwue.iview.IProfileSettingView;
import com.aiwue.model.Notice;
import com.aiwue.model.User;
import com.aiwue.presenter.ProfileSettingPresenter;
import com.aiwue.utils.ConstantValue;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
/**
 * 我的-》setting activity
 * Created by Yibao on 2017年4月17日14:36:39
 * Copyright (c) 2017 aiwue.com All rights reserved
 */
public class ProfilesettingActivity extends BaseMvpActivity<ProfileSettingPresenter> implements IProfileSettingView {

    @BindView(R.id.back_mycollectionsdiv)
    ImageView back_mycollectionsdiv;
    @BindView(R.id.account_security_rela)
    RelativeLayout account_security_rela ;
    @BindView(R.id.clear_cache_rela)
    RelativeLayout clear_cache_rela;
    @BindView(R.id.clear_offlinedown_rela)
    RelativeLayout clear_offlinedown_rela;
    @BindView(R.id.feedback_rela)
    RelativeLayout feedback_rela;
    @BindView(R.id.about_us_rela)
    RelativeLayout about_us_rela;
    @BindView(R.id.app_score_rela)
    RelativeLayout app_score_rela;
    @BindView(R.id.ll_night_mode)
    RelativeLayout ll_night_mode;
    @BindView(R.id.exit_login)
    Button exit_login;
    Context mContext = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
    }
    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_profile_setting);
        ButterKnife.bind(this);
    }
    @Override
    protected void bindViews() {
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        showContent();
    }

    private void showContent() {
        User user = UserController.getInstance().getUser();
        if (user==null){
            account_security_rela.setVisibility(View.GONE);
            exit_login.setVisibility(View.GONE);
        }else{
            account_security_rela.setVisibility(View.VISIBLE);
            exit_login.setVisibility(View.VISIBLE);
        }


    }

    //订阅消息
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnEvent(Notice event) {
        if (event.type == ConstantValue.EVENTBUS_MESSENGE_LOGIN_LOGOUT)
            showContent();
    }
    @Override
    protected void setListener() {
    }
    @Override
    protected ProfileSettingPresenter createPresenter() {
        return new ProfileSettingPresenter(this);
    }

    @OnClick({R.id.back_mycollectionsdiv, R.id.account_security_rela, R.id.clear_cache_rela,R.id.clear_offlinedown_rela,R.id.feedback_rela,
            R.id.about_us_rela,R.id.app_score_rela,R.id.exit_login,R.id.ll_night_mode})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_mycollectionsdiv:
                finish();
                break;
            case R.id.account_security_rela:
                startActivity(new Intent(ProfilesettingActivity.this,ProfileSettingAccountSecuryActivity.class));
                break;
            case R.id.clear_cache_rela:
                showToast("功能暂未开放");
                break;
            case R.id.clear_offlinedown_rela:
                showToast("功能暂未开放");
                break;
            case R.id.feedback_rela:
                startActivity(new Intent(ProfilesettingActivity.this,ProfileFeedBack.class));
                break;
            case R.id.about_us_rela:
                startActivity(new Intent(ProfilesettingActivity.this,ProfileAboutUsActivity.class));
                break;
            case R.id.app_score_rela:
                showToast("功能暂未开放");
                break;
            case R.id.exit_login:
                UserController.getInstance().loginOut();
                //发送登录变化的消息
                Notice msg = new  Notice();
                msg.type = ConstantValue.EVENTBUS_MESSENGE_LOGIN_LOGOUT;
                EventBus.getDefault().post(msg);
                finish();
                break;
            case R.id.ll_night_mode:
           /*     if (SharedPreferencesMgr.getInt(ConstantValue.SP_THEME, ConstantValue.THEME_LIGHT) == ConstantValue.THEME_LIGHT) {
                    SharedPreferencesMgr.setInt(ConstantValue.SP_THEME, ConstantValue.THEME_NIGHT);
                    setTheme(R.style.Theme_Night);
                } else {
                    SharedPreferencesMgr.setInt(ConstantValue.SP_THEME, ConstantValue.THEME_LIGHT);
                    setTheme(R.style.Theme_Light);
                }
                final View rootView = getWindow().getDecorView();
                if (Build.VERSION.SDK_INT >= 14) {
                    rootView.setDrawingCacheEnabled(true);
                    rootView.buildDrawingCache(true);
                    final Bitmap localBitmap = Bitmap.createBitmap(rootView.getDrawingCache());
                    rootView.setDrawingCacheEnabled(false);
                    if (null != localBitmap && rootView instanceof ViewGroup) {
                        final View localView2 = new View(mContext);
                        localView2.setBackgroundDrawable(new BitmapDrawable(getResources(), localBitmap));
                        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                        ((ViewGroup) rootView).addView(localView2, params);
                        localView2.animate().alpha(0).setDuration(400).setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                                post(new Notice(ConstantValue.MSG_TYPE_CHANGE_THEME));
//                                ColorUiUtil.changeTheme(rootView, getTheme());
                            }
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                ((ViewGroup) rootView).removeView(localView2);
                                localBitmap.recycle();
                            }
                            @Override
                            public void onAnimationCancel(Animator animation) {
                            }
                            @Override
                            public void onAnimationRepeat(Animator animation) {
                            }
                        }).start();
                    }
                } else {
                    post(new Notice(ConstantValue.MSG_TYPE_CHANGE_THEME));
                }*/
                break;
        }
    }
}
