package com.quduquxie.module.about.view;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.quduquxie.R;
import com.quduquxie.module.about.AboutInterface;
import com.quduquxie.module.about.presenter.AboutPresenter;
import com.quduquxie.view.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created on 16/11/25.
 * Created by crazylei.
 */

public class AboutActivity extends BaseActivity implements AboutInterface.View {

    @BindView(R.id.about_back)
    public ImageView about_back;
    @BindView(R.id.about_version)
    public TextView about_version;
    @BindView(R.id.about_email)
    public TextView about_email;
    @BindView(R.id.about_feedback)
    public TextView about_feedback;

    private AboutInterface.Presenter aboutPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.layout_activity_about);
        } catch (Resources.NotFoundException exception) {
            collectException(exception);
            exception.printStackTrace();
        }

        ButterKnife.bind(this);

        aboutPresenter = new AboutPresenter(this);
        aboutPresenter.init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void setPresenter(AboutInterface.Presenter aboutPresenter) {
        this.aboutPresenter = aboutPresenter;
    }

    @Override
    public void initView() {
        aboutPresenter.initData();
    }

    @Override
    public void initVersion(String message) {
        if (about_version != null) {
            about_version.setText(message);
        }
    }

    @OnClick({R.id.about_back})
    public void onClickListener(View view) {
        switch (view.getId()) {
            case R.id.about_back:
                finish();
                break;
        }
    }
}
