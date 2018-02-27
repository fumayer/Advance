package com.aiwue.ui.activity;

import com.aiwue.R;
import com.aiwue.presenter.VideoDetailPresenter;
import com.aiwue.iview.IVideoDetailView;

/**
 *  视频详情页面
 * Created by Yibao on 2017年4月13日09:00:01
 * Copyright (c) 2017 aiwue.com All rights reserved
 */

public class VideoDetailActivity extends BaseContentActivity<VideoDetailPresenter> implements IVideoDetailView {

    @Override
    protected VideoDetailPresenter createPresenter() {
        return new VideoDetailPresenter(this);
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_video_detail);
        super.loadViewLayout();
    }
}
