package com.newsdemo.ui.main.activity;


import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

import com.newsdemo.R;
import com.newsdemo.base.BaseActivity;
import com.newsdemo.base.contract.main.WelcomeContract;
import com.newsdemo.component.GlidUtils;
import com.newsdemo.model.bean.WelcomeBean;
import com.newsdemo.presenter.main.WelcomePresenter;

import butterknife.BindView;


/**
 * Created by jianqiang.hu on 2017/5/11.
 */

public class WelcomeActivity extends BaseActivity<WelcomePresenter> implements WelcomeContract.View {
    @BindView(R.id.iv_welcome_bg)
    ImageView iv_welcome_bg;

    @BindView(R.id.tv_welcome_author)
    TextView tv_welcome_author;



    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void initEventAndData() {
        mPresenter.getWelcomeData();
    }

    @Override
    public void showContent(WelcomeBean welcomeBean) {
        GlidUtils.load(this,welcomeBean.getImg(),iv_welcome_bg);
        iv_welcome_bg.animate().scaleX(1.12f).scaleY(1.12f).setDuration(2000).setStartDelay(100).start();
        tv_welcome_author.setText(welcomeBean.getText());
    }

    @Override
    public void jumpToMain() {
        Intent intent=new Intent();
        intent.setClass(this,MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }
}
