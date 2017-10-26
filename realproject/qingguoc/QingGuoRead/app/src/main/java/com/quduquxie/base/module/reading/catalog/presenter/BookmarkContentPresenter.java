package com.quduquxie.base.module.reading.catalog.presenter;

import android.text.TextUtils;

import com.quduquxie.base.RxPresenter;
import com.quduquxie.base.bean.Book;
import com.quduquxie.base.bean.Bookmark;
import com.quduquxie.base.database.helper.BookDaoHelper;
import com.quduquxie.base.module.reading.catalog.BookmarkContentInterface;
import com.quduquxie.base.module.reading.catalog.view.fragment.BookmarkContentFragment;

import java.util.ArrayList;

/**
 * Created on 17/7/27.
 * Created by crazylei.
 */

public class BookmarkContentPresenter extends RxPresenter implements BookmarkContentInterface.Presenter {

    private BookmarkContentFragment bookmarkContentFragment;

    private BookDaoHelper bookDaoHelper;

    private ArrayList<Bookmark> bookmarkList = new ArrayList<>();

    public BookmarkContentPresenter(BookmarkContentFragment bookmarkContentFragment) {
        this.bookmarkContentFragment = bookmarkContentFragment;
        this.bookDaoHelper = BookDaoHelper.getInstance(bookmarkContentFragment.getContext());
    }

    @Override
    public void initializeBookmarks(Book book) {

        bookmarkContentFragment.showLoadingPage();

        if (book != null && !TextUtils.isEmpty(book.id)) {

            if (bookDaoHelper == null) {
                bookDaoHelper = BookDaoHelper.getInstance(bookmarkContentFragment.getContext());
            }

            if (bookmarkList == null) {
                bookmarkList = new ArrayList<>();
            } else {
                bookmarkList.clear();
            }

            ArrayList<Bookmark> bookmarks = bookDaoHelper.loadBookmarks(book.id);
            if (bookmarks != null && bookmarks.size() > 0) {
                for (Bookmark bookmark : bookmarks) {
                    bookmarkList.add(bookmark);
                }
            }

            bookmarkContentFragment.initializeBookmarks(bookmarkList);
            bookmarkContentFragment.hideLoadingPage();
        } else {
            bookmarkContentFragment.showLoadingError();
        }
    }

    @Override
    public void recycle() {

    }
}