package com.quduquxie.function.search.listener;

import com.quduquxie.base.bean.Book;

/**
 * Created on 16/10/20.
 * Created by crazylei.
 */

public interface SearchListener {

    void feedbackListener();

    void recommendMoreListener();

    void searchResultListener(Book book);

    void categoryListener(String keyword);

    void deleteHistory();

    void suggestItemClicked(String suggest);
}
