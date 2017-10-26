package com.quduquxie.function.download.presenter;

import com.quduquxie.base.bean.Book;
import com.quduquxie.base.database.helper.BookDaoHelper;
import com.quduquxie.function.download.DownloadManagerInterface;
import com.quduquxie.function.download.view.DownloadManagerActivity;

import java.util.ArrayList;

/**
 * Created on 17/4/19.
 * Created by crazylei.
 */

public class DownloadManagerPresenter implements DownloadManagerInterface.Presenter {

    private DownloadManagerActivity downloadManagerActivity;

    private BookDaoHelper bookDaoHelper;
    private ArrayList<Book> bookList;

    public DownloadManagerPresenter(DownloadManagerActivity downloadManagerActivity) {
        this.downloadManagerActivity = downloadManagerActivity;
    }

    @Override
    public void initParameter() {
        if (bookDaoHelper == null) {
            bookDaoHelper = BookDaoHelper.getInstance(downloadManagerActivity);
        }

        if (bookList == null) {
            bookList = new ArrayList<>();
        } else {
            bookList.clear();
        }

        bookList = bookDaoHelper.loadOnlineBooks();

        downloadManagerActivity.setDownloadResource(bookList);
    }
}
