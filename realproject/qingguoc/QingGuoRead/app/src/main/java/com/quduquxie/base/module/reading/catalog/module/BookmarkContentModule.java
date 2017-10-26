package com.quduquxie.base.module.reading.catalog.module;

import com.quduquxie.base.FragmentScope;
import com.quduquxie.base.module.reading.catalog.presenter.BookmarkContentPresenter;
import com.quduquxie.base.module.reading.catalog.view.fragment.BookmarkContentFragment;

import dagger.Module;
import dagger.Provides;

/**
 * Created on 17/7/27.
 * Created by crazylei.
 */

@Module
@FragmentScope
public class BookmarkContentModule {

    private BookmarkContentFragment bookmarkContentFragment;

    public BookmarkContentModule(BookmarkContentFragment bookmarkContentFragment) {
        this.bookmarkContentFragment = bookmarkContentFragment;
    }

    @Provides
    @FragmentScope
    BookmarkContentFragment provideBookmarkContentFragment() {
        return bookmarkContentFragment;
    }

    @Provides
    @FragmentScope
    BookmarkContentPresenter provideBookmarkContentPresenter() {
        return new BookmarkContentPresenter(bookmarkContentFragment);
    }
}