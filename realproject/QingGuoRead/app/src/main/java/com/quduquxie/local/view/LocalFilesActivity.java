package com.quduquxie.local.view;

import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.quduquxie.R;
import com.quduquxie.base.util.TypefaceUtil;
import com.quduquxie.local.LocalFilesInterface;
import com.quduquxie.local.adapter.LocalViewPagerAdapter;
import com.quduquxie.local.presenter.LocalFilesPresenter;
import com.quduquxie.view.BaseActivity;
import com.quduquxie.widget.PagerSlidingTabStrip;

public class LocalFilesActivity extends BaseActivity implements LocalFilesInterface.View, View.OnClickListener {

    private LocalFilesPresenter localFilesPresenter;

    public ImageView common_head_back;
    public TextView common_head_title;
    public PagerSlidingTabStrip local_files_tab_strip;
    public ViewPager local_files_view_pager;

    public LocalViewPagerAdapter localViewPagerAdapter;

    private Typeface typeface_song_depict;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.layout_local_files);
        } catch (Resources.NotFoundException exception) {
            exception.printStackTrace();
        }

        localFilesPresenter = new LocalFilesPresenter(this, this);

        typeface_song_depict = TypefaceUtil.loadTypeface(this, TypefaceUtil.TYPEFACE_SONG_DEPICT);

        initView();
    }

    private void initView() {
        common_head_back = (ImageView) findViewById(R.id.common_head_back);
        common_head_title = (TextView) findViewById(R.id.common_head_title);
        local_files_tab_strip = (PagerSlidingTabStrip) findViewById(R.id.local_files_tab_strip);
        local_files_view_pager = (ViewPager) findViewById(R.id.local_files_view_pager);

        if (common_head_back != null) {
            common_head_back.setOnClickListener(this);
        }

        if (common_head_title != null) {
            common_head_title.setText(R.string.local_files);
            common_head_title.setTypeface(typeface_song_depict);
        }

        localViewPagerAdapter = new LocalViewPagerAdapter(getSupportFragmentManager(), local_files_tab_strip, local_files_view_pager);
        localFilesPresenter.initParameter(localViewPagerAdapter);
        localViewPagerAdapter.notifyDataSetChanged();
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.common_head_back:
                finish();
                break;
        }
    }

    @Override
    public void setPresenter(LocalFilesInterface.Presenter localFilesPresenter) {
        this.localFilesPresenter = (LocalFilesPresenter) localFilesPresenter;
    }
}
