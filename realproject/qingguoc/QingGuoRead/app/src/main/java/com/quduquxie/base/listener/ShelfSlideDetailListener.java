package com.quduquxie.base.listener;

import com.quduquxie.base.bean.Book;

/**
 * Created on 17/7/29.
 * Created by crazylei.
 */

public interface ShelfSlideDetailListener {

    void insertBook();

    void onClickedOnlineBook(Book book, String type);

    void onClickedLocalBook(Book book);
}