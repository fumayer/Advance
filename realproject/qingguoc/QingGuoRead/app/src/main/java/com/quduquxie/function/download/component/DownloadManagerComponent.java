package com.quduquxie.function.download.component;

import com.quduquxie.application.component.ApplicationComponent;
import com.quduquxie.base.ActivityScope;
import com.quduquxie.function.download.module.DownloadManagerModule;
import com.quduquxie.function.download.presenter.DownloadManagerPresenter;
import com.quduquxie.function.download.view.DownloadManagerActivity;

import dagger.Component;

/**
 * Created on 17/4/19.
 * Created by crazylei.
 */

@ActivityScope
@Component(modules = DownloadManagerModule.class, dependencies = ApplicationComponent.class)
public interface DownloadManagerComponent {

    DownloadManagerActivity inject(DownloadManagerActivity downloadManagerActivity);

    DownloadManagerPresenter presenter();
}
