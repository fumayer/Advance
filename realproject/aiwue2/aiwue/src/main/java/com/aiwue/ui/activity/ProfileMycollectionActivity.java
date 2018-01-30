package com.aiwue.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aiwue.R;
import com.aiwue.base.BaseMvpActivity;
import com.aiwue.iview.IProfileMycollectionView;
import com.aiwue.iview.IProfileSettingView;
import com.aiwue.model.Notice;
import com.aiwue.presenter.ProfileMycollectionPresenter;
import com.aiwue.presenter.ProfileSettingPresenter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/4/14.
 */

public class ProfileMycollectionActivity extends BaseMvpActivity<ProfileMycollectionPresenter> implements IProfileMycollectionView {


    @BindView(R.id.back_mycollectionsdiv)
    ImageView back_mycollectionsdiv;
    @BindView(R.id.collection_article_rela)
    RelativeLayout collection_article_rela;
    @BindView(R.id.collection__track_rela)
    RelativeLayout collection__track_rela;
    @BindView(R.id.collection_video_rela)
    RelativeLayout collection_video_rela;
    @BindView(R.id.collection_album_rela)
    RelativeLayout collection_album_rela;
    @BindView(R.id.collection__course_rela)
    RelativeLayout collection__course_rela;



    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_profiles_mycollections);
        ButterKnife.bind(this);
    }
    @Override
    protected void bindViews() {
    }
    @Override
    protected ProfileMycollectionPresenter createPresenter() {
        return new ProfileMycollectionPresenter(this);
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

    @OnClick({R.id.back_mycollectionsdiv, R.id.collection_article_rela, R.id.collection__track_rela, R.id.collection_video_rela, R.id.collection_album_rela, R.id.collection__course_rela
    })
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_mycollectionsdiv:
                finish();
                break;
            case R.id.collection_article_rela:
                showToast("功能暂未开放");
                break;
            case R.id.collection__track_rela:
                showToast("功能暂未开放");
                break;
            case R.id.collection_video_rela:
                showToast("功能暂未开放");
                break;
            case R.id.collection_album_rela:
                showToast("功能暂未开放");
                break;
            case R.id.collection__course_rela:
                break;


        }
    }
}