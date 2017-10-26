package com.quduquxie.local.presenter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.quduquxie.local.LocalFilesInterface;
import com.quduquxie.local.adapter.LocalViewPagerAdapter;
import com.quduquxie.local.view.LocalFilesDirectoryFragment;
import com.quduquxie.local.view.LocalFilesScanFragment;

import java.lang.ref.WeakReference;

public class LocalFilesPresenter implements LocalFilesInterface.Presenter {

    private LocalFilesInterface.View localFilesView;
    private WeakReference<Context> contextReference;

    public LocalFilesPresenter(@NonNull LocalFilesInterface.View localFilesView, Context context) {
        this.localFilesView = localFilesView;
        this.localFilesView.setPresenter(this);
        this.contextReference = new WeakReference<>(context);
    }

    @Override
    public void init() {

    }

    @Override
    public void initParameter(LocalViewPagerAdapter localViewPagerAdapter) {
        String[] titles = new String[]{"智能导入书籍", "浏览本机书籍"};

        Bundle localScanBundle = new Bundle();
        localViewPagerAdapter.addTab(titles[0], titles[0], LocalFilesScanFragment.class, localScanBundle);
        Bundle localDirectoryBundle = new Bundle();
        localViewPagerAdapter.addTab(titles[1], titles[1], LocalFilesDirectoryFragment.class, localDirectoryBundle);
    }
}