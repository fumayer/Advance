package com.quduquxie.base.module.catalog.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.quduquxie.R;
import com.quduquxie.application.component.ApplicationComponent;
import com.quduquxie.base.BaseActivity;
import com.quduquxie.base.bean.Book;
import com.quduquxie.base.module.catalog.CatalogInterface;
import com.quduquxie.base.module.catalog.component.DaggerCatalogComponent;
import com.quduquxie.base.module.catalog.module.CatalogModule;
import com.quduquxie.base.module.catalog.presenter.CatalogPresenter;
import com.quduquxie.base.module.catalog.view.fragment.CatalogContentFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created on 17/7/27.
 * Created by crazylei.
 */

public class CatalogActivity extends BaseActivity<CatalogPresenter> implements CatalogInterface.View {

    @BindView(R.id.common_head_back)
    ImageView common_head_back;
    @BindView(R.id.common_head_title)
    TextView common_head_title;
    @BindView(R.id.catalog_content)
    FrameLayout catalog_content;

    @Inject
    CatalogPresenter catalogPresenter;

    private Book book;

    private CatalogContentFragment catalogContentFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_catalog);
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
        DaggerCatalogComponent.builder()
                .applicationComponent(applicationComponent)
                .catalogModule(new CatalogModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void initializeParameter() {
        ButterKnife.bind(this);

        common_head_title.setText("目录");
        common_head_title.setTypeface(typeface_song_depict);

        Intent intent = this.getIntent();

        if (intent != null && intent.getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            book = (Book) bundle.getSerializable("book");
        }

        FragmentManager fragmentManager = this.getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


        Bundle bundle = new Bundle();
        bundle.putSerializable("book", book);
        bundle.putString("form", "CatalogActivity");

        if (catalogContentFragment == null) {
            catalogContentFragment = new CatalogContentFragment();
            catalogContentFragment.setArguments(bundle);
            fragmentTransaction.add(R.id.catalog_content, catalogContentFragment);
        } else {
            fragmentTransaction.show(catalogContentFragment);
        }

        if (!isFinishing()) {
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    @Override
    public void recycle() {

    }

    @OnClick({R.id.common_head_back})
    public void onClickListener(View view) {
        switch (view.getId()) {
            case R.id.common_head_back:
                finish();
                break;
        }
    }
}
