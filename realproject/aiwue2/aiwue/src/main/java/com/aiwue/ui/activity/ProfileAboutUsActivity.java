package com.aiwue.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwue.R;
import com.aiwue.base.BaseMvpActivity;
import com.aiwue.iview.IProfileAboutUsView;
import com.aiwue.presenter.ProfileAboutUsPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/4/14.
 */

public class ProfileAboutUsActivity extends BaseMvpActivity<ProfileAboutUsPresenter> implements IProfileAboutUsView {

    @BindView(R.id.back_aboutus)
    ImageView back_aboutus;
    @BindView(R.id.tipservice_aboutas)
    TextView tipservice_aboutas ;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

    }
    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_profiles_aboutus);
        ButterKnife.bind(this);
    }
    @Override
    protected void bindViews() {
    }
    @Override
    protected ProfileAboutUsPresenter createPresenter() {
        return new ProfileAboutUsPresenter(this);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }
    @Override
    protected void setListener() {
    }

    @OnClick({R.id.back_aboutus, R.id.tipservice_aboutas})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_aboutus:
                finish();
                break;
            case R.id.tipservice_aboutas:
               startActivity(new Intent(ProfileAboutUsActivity.this, ProfileServiceAndPrivaceActivity.class));
                break;
        }
    }
}
