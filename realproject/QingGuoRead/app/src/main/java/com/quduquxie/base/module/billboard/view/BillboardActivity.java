package com.quduquxie.base.module.billboard.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.InflateException;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.quduquxie.R;
import com.quduquxie.application.component.ApplicationComponent;
import com.quduquxie.base.BaseActivity;
import com.quduquxie.base.listener.BillboardListener;
import com.quduquxie.base.module.billboard.adapter.BillboardAdapter;
import com.quduquxie.base.module.billboard.BillboardInterface;
import com.quduquxie.base.module.billboard.component.DaggerBillboardComponent;
import com.quduquxie.base.module.billboard.module.BillboardModule;
import com.quduquxie.base.module.billboard.presenter.BillboardPresenter;
import com.quduquxie.base.widget.DropWindow;
import com.quduquxie.base.widget.SlidePagerTabStrip;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created on 17/7/25.
 * Created by crazylei.
 */

public class BillboardActivity extends BaseActivity<BillboardPresenter> implements BillboardInterface.View, BillboardListener {

    @BindView(R.id.billboard_back)
    ImageView billboard_back;
    @BindView(R.id.billboard_title)
    TextView billboard_title;
    @BindView(R.id.billboard_date)
    LinearLayout billboard_date;
    @BindView(R.id.billboard_date_option)
    TextView billboard_date_option;
    @BindView(R.id.billboard_date_flag)
    ImageView billboard_date_flag;

    @BindView(R.id.billboard_navigation)
    SlidePagerTabStrip billboard_navigation;
    @BindView(R.id.billboard_result)
    ViewPager billboard_result;

    @Inject
    BillboardPresenter billboardPresenter;

    private BillboardAdapter billboardAdapter;

    private String uri;
    private String date;
    private String title;

    private DropWindow dropWindow;
    private View dropView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_billboard);
        initializeParameter();
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
    protected void setActivityComponent(ApplicationComponent applicationComponent) {
        DaggerBillboardComponent.builder()
                .applicationComponent(applicationComponent)
                .billboardModule(new BillboardModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void initializeParameter() {
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent != null) {
            this.uri = intent.getStringExtra("uri");
            this.title = intent.getStringExtra("title");
        }

        this.date = billboardPresenter.initializeDate(uri);

        changeDateState(date);

        billboard_title.setText(TextUtils.isEmpty(title) ? "榜单" : title);

        billboard_result.setOffscreenPageLimit(1);

        billboardAdapter = new BillboardAdapter(this, this.getSupportFragmentManager(), billboard_navigation, billboard_result);

        billboardPresenter.initializeParameter(billboardAdapter, title, uri, date);

        billboardAdapter.notifyDataSetChanged();
    }

    @Override
    public void recycle() {

    }

    @OnClick({R.id.billboard_back, R.id.billboard_date})
    public void onClickListener(View view) {
        switch (view.getId()) {
            case R.id.billboard_back:
                finish();
                break;
            case R.id.billboard_date:
                initPopupWindow();
                break;
        }
    }

    private void changeDateState(String date) {
        this.date = date;
        if ("week".equals(date)) {
            billboard_date_option.setText("周榜");
        } else if ("month".equals(date)) {
            billboard_date_option.setText("总榜");
        } else if ("year".equals(date)) {
            billboard_date_option.setText("总榜");
        }

        billboard_date_flag.setImageResource(R.drawable.selector_open);
    }

    private void initPopupWindow() {
        if (dropWindow == null) {
            try {
                dropView = View.inflate(this, R.layout.layout_view_billboard_date_option, null);
            } catch (InflateException inflateException) {
                collectException(inflateException);
                inflateException.printStackTrace();
                return;
            }
            TextView billboard_date_option_week = (TextView) dropView.findViewById(R.id.billboard_date_option_week);
            TextView billboard_date_option_year = (TextView) dropView.findViewById(R.id.billboard_date_option_year);

            billboard_date_option_week.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismissPopupWindow();
                    changeDateState("week");
                    billboardAdapter.changeFragmentBundle("week");
                }
            });

            billboard_date_option_year.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismissPopupWindow();
                    changeDateState("year");
                    billboardAdapter.changeFragmentBundle("year");
                }
            });

            dropWindow = new DropWindow(this, dropView);

            dropWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    refreshDateFlagDrawable(false);
                }
            });
        }
        showPopupWindow(billboard_date);
    }

    public void showPopupWindow(View root) {
        if (dropWindow != null && !dropWindow.isShowing()) {
            if (root != null && dropView != null && dropView.getParent() != null) {
                ((ViewGroup) dropView.getParent()).removeAllViews();
            }

            refreshDateFlagDrawable(true);

            int x = this.getResources().getDimensionPixelOffset(R.dimen.width_260);

            if (root != null) {
                dropWindow.showAsDropDown(root, -x, 0);
            }
        }
    }

    private void dismissPopupWindow() {
        if (dropWindow != null && dropWindow.isShowing()) {
            dropWindow.dismiss();
        }
    }


    private void refreshDateFlagDrawable(boolean state) {
        if (state) {
            billboard_date_flag.setImageResource(R.drawable.selector_close);
        } else {
            billboard_date_flag.setImageResource(R.drawable.selector_open);
        }
    }

    @Override
    public String loadBillboardDate() {
        return date;
    }
}