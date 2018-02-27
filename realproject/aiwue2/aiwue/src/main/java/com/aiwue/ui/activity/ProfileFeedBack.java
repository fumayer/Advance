package com.aiwue.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.aiwue.R;
import com.aiwue.base.BaseMvpActivity;
import com.aiwue.iview.IProfileFeedBackView;
import com.aiwue.presenter.ProfileFeedBackPresenter;
import com.aiwue.presenter.ProfileSettingPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/4/14.
 */

public class ProfileFeedBack  extends BaseMvpActivity<ProfileFeedBackPresenter> implements IProfileFeedBackView{

    @BindView(R.id.feedInput)
    EditText feedInput;
    @BindView(R.id.contactIput)
    EditText contactIput ;
    @BindView(R.id.commit_btn)
    Button commit_btn;



    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

    }
    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_profiles_feedback);
        ButterKnife.bind(this);
    }
    @Override
    protected void bindViews() {
    }
    @Override
    protected ProfileFeedBackPresenter createPresenter() {
        return new ProfileFeedBackPresenter(this);
    }
    @Override
    protected void processLogic(Bundle savedInstanceState) {
    }
    @Override
    protected void setListener() {
    }
    @OnClick({R.id.back_myfeedback,R.id.feedInput, R.id.contactIput, R.id.commit_btn})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_myfeedback:
                finish();
                break;
            case R.id.feedInput:

                break;
            case R.id.contactIput:
                break;
            case R.id.commit_btn:
                finish();
                break;
        }
    }
}
