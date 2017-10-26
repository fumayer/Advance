package com.quduquxie.base.listener;

import com.quduquxie.base.bean.Book;

/**
 * Created on 17/7/19.
 * Created by crazylei.
 */

public interface ShelfListener {

    void insertBook();

    void clickedBook(Book book, boolean delete);

    void longClickedBook(Book book);

}