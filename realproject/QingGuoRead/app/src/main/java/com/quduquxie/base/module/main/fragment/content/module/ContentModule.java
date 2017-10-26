package com.quduquxie.base.module.main.fragment.content.module;

import com.quduquxie.base.FragmentScope;
import com.quduquxie.base.module.main.fragment.content.presenter.ContentPresenter;
import com.quduquxie.base.module.main.fragment.content.view.ContentFragment;

import dagger.Module;
import dagger.Provides;

/**
 * Created on 17/7/20.
 * Created by crazylei.
 */

@Module
@FragmentScope
public class ContentModule {

    private ContentFragment contentFragment;

    public ContentModule(ContentFragment contentFragment) {
        this.contentFragment = contentFragment;
    }

    @Provides
    @FragmentScope
    ContentFragment provideSelectedContentFragment() {
        return contentFragment;
    }

    @Provides
    @FragmentScope
    ContentPresenter provideSelectedContentPresenter() {
        return new ContentPresenter(contentFragment);
    }
}