package com.quduquxie.base.module.reading.catalog.component;

import com.quduquxie.application.component.ApplicationComponent;
import com.quduquxie.base.FragmentScope;
import com.quduquxie.base.module.reading.catalog.module.BookmarkContentModule;
import com.quduquxie.base.module.reading.catalog.presenter.BookmarkContentPresenter;
import com.quduquxie.base.module.reading.catalog.view.fragment.BookmarkContentFragment;

import dagger.Component;

/**
 * Created on 17/7/27.
 * Created by crazylei.
 */

@FragmentScope
@Component(modules = BookmarkContentModule.class, dependencies = ApplicationComponent.class)
public interface BookmarkContentComponent {

    BookmarkContentFragment inject(BookmarkContentFragment bookmarkContentFragment);

    BookmarkContentPresenter presenter();
}