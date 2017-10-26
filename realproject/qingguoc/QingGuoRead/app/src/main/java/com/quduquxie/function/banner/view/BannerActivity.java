package com.quduquxie.function.banner.view;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.StringSignature;
import com.quduquxie.R;
import com.quduquxie.application.component.ApplicationComponent;
import com.quduquxie.base.bean.Banner;
import com.quduquxie.base.module.main.activity.view.MainActivity;
import com.quduquxie.function.BaseActivity;
import com.quduquxie.function.banner.BannerInterface;
import com.quduquxie.function.banner.component.DaggerBannerComponent;
import com.quduquxie.function.banner.module.BannerModule;
import com.quduquxie.function.banner.presenter.BannerPresenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created on 17/3/24.
 * Created by crazylei.
 */

public class BannerActivity extends BaseActivity implements BannerInterface.View {

    @BindView(R.id.banner_back)
    public ImageView banner_back;
    @BindView(R.id.banner_shelf)
    public ImageView banner_shelf;
    @BindView(R.id.banner_content)
    public FrameLayout banner_content;
    @BindView(R.id.banner_image)
    public ImageView banner_image;
    @BindView(R.id.banner_description)
    public LinearLayout banner_description;

    @Inject
    BannerPresenter bannerPresenter;

    private Banner banner;

    private String[] titles = new String[3];
    private String[] descriptions = new String[3];

    private int[] banner_title = new int[]{R.drawable.icon_banner_title_first, R.drawable.icon_banner_title_second, R.drawable.icon_banner_title_third};

    private static float text_size_30;
    private static float text_size_34;

    private static int margin_16;
    private static int margin_24;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.layout_activity_banner);

            initParameter();

        } catch (Resources.NotFoundException exception) {
            exception.printStackTrace();
            collectException(exception);
        }
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

        if (banner_description != null) {
            banner_description.removeAllViews();
        }

        if (banner_content != null) {
            banner_content.removeAllViews();
        }
    }

    @Override
    protected void setActivityComponent(ApplicationComponent applicationComponent) {
        DaggerBannerComponent.builder()
                .applicationComponent(applicationComponent)
                .bannerModule(new BannerModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(BannerInterface.Presenter bannerPresenter) {
        this.bannerPresenter = (BannerPresenter) bannerPresenter;
    }

    @Override
    public void initParameter() {

        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent != null) {
            banner = (Banner) intent.getSerializableExtra("Banner");
        }

        Resources resources = getQuApplicationContext().getResources();

        text_size_30 = resources.getDimensionPixelSize(R.dimen.text_size_30);
        text_size_34 = resources.getDimensionPixelSize(R.dimen.text_size_34);

        margin_16 = resources.getDimensionPixelOffset(R.dimen.width_32);
        margin_24 = resources.getDimensionPixelOffset(R.dimen.width_28);

        bannerPresenter.initParameter();
    }

    @OnClick({R.id.banner_back, R.id.banner_shelf})
    public void onClickListener(View view) {
        switch (view.getId()) {
            case R.id.banner_back:
                finish();
                break;
            case R.id.banner_shelf:
                Intent mainIntent = new Intent();
                mainIntent.putExtra("position", 0);
                mainIntent.setClass(this, MainActivity.class);
                startActivity(mainIntent);
                break;
        }
    }

    @Override
    public void initView() {
        if (banner != null) {
            if (!TextUtils.isEmpty(banner.image_banner)) {
                Glide.with(this)
                        .load(banner.image_banner)
                        .signature(new StringSignature(banner.image_banner))
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .placeholder(R.drawable.icon_default)
                        .error(R.drawable.icon_default)
                        .skipMemoryCache(true)
                        .fitCenter()
                        .into(banner_image);
            } else {
                banner_image.setImageResource(R.drawable.icon_default);
            }

            titles[0] = banner.title1;
            titles[1] = banner.title2;
            titles[2] = banner.title3;

            descriptions[0] = banner.description1;
            descriptions[1] = banner.description2;
            descriptions[2] = banner.description3;

            setBannerInformation(0);
            setBannerInformation(1);
            setBannerInformation(2);
        }
    }

    private void setBannerInformation(int i) {
        ImageView banner_title_image = new ImageView(this);
        banner_title_image.setImageResource(banner_title[i]);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.bottomMargin = margin_16;
        layoutParams.topMargin = margin_16;
        banner_description.addView(banner_title_image, layoutParams);

        TextView banner_title = new TextView(this);
        banner_title.setText(titles[i]);
        banner_title.setSingleLine(true);
        banner_title.setTextSize(TypedValue.COMPLEX_UNIT_PX, text_size_34);
        banner_title.setTextColor(Color.parseColor("#191919"));
        banner_description.addView(banner_title);

        TextView detailTextView = new TextView(this);
        detailTextView.setText(descriptions[i]);
        detailTextView.setLineSpacing(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14f, getResources().getDisplayMetrics()), 1.0f);
        detailTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, text_size_30);
        detailTextView.setTextColor(Color.parseColor("#191919"));

        layoutParams.gravity = Gravity.CENTER;
        layoutParams.topMargin = margin_24;
        banner_description.addView(detailTextView, layoutParams);
    }
}
