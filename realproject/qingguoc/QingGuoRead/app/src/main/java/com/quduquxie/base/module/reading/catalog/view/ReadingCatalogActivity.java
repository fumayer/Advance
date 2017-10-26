package com.quduquxie.base.module.reading.catalog.view;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.quduquxie.R;
import com.quduquxie.application.component.ApplicationComponent;
import com.quduquxie.base.BaseActivity;
import com.quduquxie.base.bean.Book;
import com.quduquxie.base.config.BaseConfig;
import com.quduquxie.base.module.catalog.view.fragment.CatalogContentFragment;
import com.quduquxie.base.module.reading.catalog.ReadingCatalogInterface;
import com.quduquxie.base.module.reading.catalog.component.DaggerReadingCatalogComponent;
import com.quduquxie.base.module.reading.catalog.module.ReadingCatalogModule;
import com.quduquxie.base.module.reading.catalog.presenter.ReadingCatalogPresenter;
import com.quduquxie.base.module.reading.catalog.view.fragment.BookmarkContentFragment;
import com.quduquxie.widget.LeftSlideLayout;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created on 17/7/27.
 * Created by crazylei.
 */

public class ReadingCatalogActivity extends BaseActivity<ReadingCatalogPresenter> implements ReadingCatalogInterface.View {

    @BindView(R.id.reading_catalog_head)
    LinearLayout reading_catalog_head;
    @BindView(R.id.reading_catalog)
    TextView reading_catalog;
    @BindView(R.id.reading_bookmark)
    TextView reading_bookmark;
    @BindView(R.id.reading_catalog_divider)
    View reading_catalog_divider;
    @BindView(R.id.reading_catalog_content)
    FrameLayout reading_catalog_content;

    @Inject
    ReadingCatalogPresenter readingCatalogPresenter;

    private Book book;

    private FragmentManager fragmentManager;

    private CatalogContentFragment catalogContentFragment;
    private BookmarkContentFragment bookmarkContentFragment;

    private int position = 0;

    private ColorStateList darkColorStateList;
    private ColorStateList lightColorStateList;

    protected LeftSlideLayout left_slide_layout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        left_slide_layout = (LeftSlideLayout) LayoutInflater.from(this).inflate(R.layout.layout_activity_swipe_back, null);
        left_slide_layout.attachToActivity(this);

        setContentView(R.layout.layout_activity_reading_catalog);

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
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.activity_left_in, R.anim.activity_left_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_left_in, R.anim.activity_left_out);
    }

    @Override
    protected void setActivityComponent(ApplicationComponent applicationComponent) {
        DaggerReadingCatalogComponent.builder()
                .applicationComponent(applicationComponent)
                .readingCatalogModule(new ReadingCatalogModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void initializeParameter() {
        ButterKnife.bind(this);

        initializeWindow();

        reading_catalog.setTypeface(typeface_song_depict);
        reading_bookmark.setTypeface(typeface_song_depict);

        fragmentManager = getSupportFragmentManager();

        Intent intent = this.getIntent();

        if (intent != null && intent.getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            book = (Book) bundle.getSerializable("book");
        }

        int[] darkColors = new int[]{Color.parseColor("#0094D5"), Color.parseColor("#0094D5"), Color.parseColor("#0094D5"), Color.parseColor("#9B9B9B")};
        int[] lightColors = new int[]{Color.parseColor("#0094D5"), Color.parseColor("#0094D5"), Color.parseColor("#0094D5"), Color.parseColor("#191919")};

        int[][] states = new int[4][];
        states[0] = new int[]{android.R.attr.state_pressed};
        states[1] = new int[]{android.R.attr.state_checked};
        states[2] = new int[]{android.R.attr.state_selected};
        states[3] = new int[]{};

        darkColorStateList = new ColorStateList(states, darkColors);
        lightColorStateList = new ColorStateList(states, lightColors);

        initializeContentView();

        showFragment(position);
    }

    @Override
    public void recycle() {

    }

    @OnClick({R.id.reading_catalog, R.id.reading_bookmark})
    public void onClickListener(View view) {
        switch (view.getId()) {
            case R.id.reading_catalog:
                if (position != 0) {
                    showFragment(0);
                }
                break;
            case R.id.reading_bookmark:
                if (position != 1) {
                    showFragment(1);
                }
                break;
        }
    }

    private void initializeWindow() {
        if (this.getWindow() != null) {
            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    public void showFragment(int position) {

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        hideFragment(fragmentTransaction);

        Bundle bundle = new Bundle();
        bundle.putSerializable("book", book);
        bundle.putString("form", "ReadingCatalogActivity");

        switch (position) {
            case 0:
                if (catalogContentFragment == null) {
                    catalogContentFragment = new CatalogContentFragment();
                    catalogContentFragment.setArguments(bundle);
                    fragmentTransaction.add(R.id.reading_catalog_content, catalogContentFragment);
                } else {
                    fragmentTransaction.show(catalogContentFragment);
                }

                switchNavigationState(position);
                break;
            case 1:
                if (bookmarkContentFragment == null) {
                    bookmarkContentFragment = new BookmarkContentFragment();
                    bookmarkContentFragment.setArguments(bundle);
                    fragmentTransaction.add(R.id.reading_catalog_content, bookmarkContentFragment);
                } else {
                    fragmentTransaction.show(bookmarkContentFragment);
                }

                switchNavigationState(position);
                break;
        }

        this.position = position;

        if (!isFinishing()) {
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    private void hideFragment(FragmentTransaction fragmentTransaction) {
        if (catalogContentFragment != null) {
            fragmentTransaction.hide(catalogContentFragment);
        }

        if (bookmarkContentFragment != null) {
            fragmentTransaction.hide(bookmarkContentFragment);
        }
    }

    private void switchNavigationState(int position) {
        if (reading_catalog != null) {
            reading_catalog.setSelected(position == 0);
        }

        if (reading_bookmark != null) {
            reading_bookmark.setSelected(position == 1);
        }
    }

    private void initializeContentView() {

        boolean viewState = (BaseConfig.READING_BACKGROUND_MODE == 5 || BaseConfig.READING_BACKGROUND_MODE == 6);

        reading_catalog_head.setBackgroundColor(viewState ? Color.parseColor("#252424") : Color.parseColor("#FFFFFF"));

        reading_catalog_content.setBackgroundColor(viewState ? Color.parseColor("#252424") : Color.parseColor("#FFFFFF"));

        reading_catalog.setTextColor(viewState ? darkColorStateList : lightColorStateList);

        reading_bookmark.setTextColor(viewState ? darkColorStateList : lightColorStateList);

        reading_catalog_divider.setBackgroundColor(viewState ? Color.parseColor("#464646") : Color.parseColor("#D8D8D8"));
    }
}