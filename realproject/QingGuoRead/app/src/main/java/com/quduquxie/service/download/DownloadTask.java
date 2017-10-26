package com.quduquxie.service.download;

import android.text.TextUtils;

import com.quduquxie.base.bean.Book;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created on 17/4/19.
 * Created by crazylei.
 */

public class DownloadTask {

    public int start;
    public int end;
    public Book book;

    public String book_id;
    public DownloadState downloadState;

    private CompositeDisposable compositeDisposable;

    public DownloadTask(Book book, DownloadState downloadState, int start, int end) {
        if (book == null) {
            throw new IllegalArgumentException("Book may not be null");
        }

        this.book = book;
        this.book_id = book.id;

        this.downloadState = downloadState;

        this.start = start;
        this.end = end;
    }

    public String getBookId() {
        if (TextUtils.isEmpty(book_id)) {
            if (book != null && !TextUtils.isEmpty(book.id)) {
                this.book_id = book.id;
                return book_id;
            } else {
                return "";
            }
        } else {
            return book_id;
        }
    }

    public void addSubscribe(Disposable disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(disposable);
    }

    public void unSubscribe() {
        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }
    }

    @Override
    public boolean equals(Object object) {
        if (object != null && object instanceof DownloadTask) {
            DownloadTask downloadTask = (DownloadTask) object;
            return book.id.equals(downloadTask.book.id);
        }
        return super.equals(object);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((book.id == null) ? 0 : book.id.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
