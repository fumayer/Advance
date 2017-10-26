package com.quduquxie.base.listener;

import android.view.View;

import com.quduquxie.base.bean.Book;

/**
 * Created on 17/8/2.
 * Created by crazylei.
 */

public interface TabulationListener {

    void startCoverActivity(Book book);

    void showOptionMore(View view, Book book, int position);
}