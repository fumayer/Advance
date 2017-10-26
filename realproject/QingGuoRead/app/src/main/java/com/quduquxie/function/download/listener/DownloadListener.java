package com.quduquxie.function.download.listener;

import com.quduquxie.base.bean.Book;

/**
 * Created on 17/4/20.
 * Created by crazylei.
 */

public interface DownloadListener {

    void downloadBook(Book book, boolean fromStartIndex);

    void downloadCancelTask(Book book);

    void startDownBookTask(Book book);
}
