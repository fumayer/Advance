package com.quduquxie.function.download.module;

import com.quduquxie.base.ActivityScope;
import com.quduquxie.function.download.presenter.DownloadManagerPresenter;
import com.quduquxie.function.download.view.DownloadManagerActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created on 17/4/19.
 * Created by crazylei.
 */

@Module
public class DownloadManagerModule {

    private DownloadManagerActivity downloadManagerActivity;

    public DownloadManagerModule(DownloadManagerActivity downloadManagerActivity) {
        this.downloadManagerActivity = downloadManagerActivity;
    }

    @Provides
    @ActivityScope
    DownloadManagerActivity provideDownloadManagerActivity() {
        return downloadManagerActivity;
    }

    @Provides
    @ActivityScope
    DownloadManagerPresenter provideDownloadManagerPresenter() {
        return new DownloadManagerPresenter(downloadManagerActivity);
    }
}
