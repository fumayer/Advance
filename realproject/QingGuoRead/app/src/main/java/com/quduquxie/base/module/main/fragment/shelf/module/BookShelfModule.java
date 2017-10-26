package com.quduquxie.base.module.main.fragment.shelf.module;

import com.quduquxie.base.FragmentScope;
import com.quduquxie.base.module.main.fragment.shelf.presenter.BookShelfPresenter;
import com.quduquxie.base.module.main.fragment.shelf.view.BookShelfFragment;

import dagger.Module;
import dagger.Provides;

/**
 * Created on 17/7/17.
 * Created by crazylei.
 */

@Module
@FragmentScope
public class BookShelfModule {

    private BookShelfFragment bookShelfFragment;

    public BookShelfModule(BookShelfFragment bookShelfFragment) {
        this.bookShelfFragment = bookShelfFragment;
    }

    @Provides
    @FragmentScope
    BookShelfFragment provideBookShelfFragment() {
        return bookShelfFragment;
    }

    @Provides
    @FragmentScope
    BookShelfPresenter provideBookShelfPresenter() {
        return new BookShelfPresenter(bookShelfFragment);
    }
}