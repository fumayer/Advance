package com.quduquxie.base.listener;

import android.view.View;

import com.quduquxie.base.bean.Book;

/**
 * Created on 17/8/2.
 * Created by crazylei.
 */

public interface MainContentListener {

    void startTabulationActivity(String title, String uri);

    void startSearchActivity();

    void showPromptView(View view, Book book);

}