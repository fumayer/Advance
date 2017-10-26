package com.quduquxie.module.comment.util;

import android.view.View;

import com.quduquxie.module.comment.listener.BookClickedListener;

/**
 * Created on 17/2/28.
 * Created by crazylei.
 */

public class BookClickableSpan extends BasicClickableSpan {

    private String id_book;
    private BookClickedListener bookClickedListener;

    public BookClickableSpan(String id_book, BookClickedListener bookClickedListener) {
        this.id_book = id_book;
        this.bookClickedListener = bookClickedListener;
    }

    @Override
    public void onClick(View widget) {
        if (bookClickedListener != null) {
            bookClickedListener.onClickedBookSpan(id_book);
        }
    }
}
