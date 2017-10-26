package com.quduquxie.base.module.main.fragment.shelf.presenter;

import com.quduquxie.base.RxPresenter;
import com.quduquxie.base.module.main.fragment.shelf.BookShelfInterface;
import com.quduquxie.base.module.main.fragment.shelf.view.BookShelfFragment;

/**
 * Created on 17/7/17.
 * Created by crazylei.
 */

public class BookShelfPresenter extends RxPresenter implements BookShelfInterface.Presenter {

    private BookShelfFragment bookShelfFragment;

    public BookShelfPresenter(BookShelfFragment bookShelfFragment) {
        this.bookShelfFragment = bookShelfFragment;
    }

    @Override
    public void recycle() {
        if (bookShelfFragment != null) {
            bookShelfFragment = null;
        }
    }
}