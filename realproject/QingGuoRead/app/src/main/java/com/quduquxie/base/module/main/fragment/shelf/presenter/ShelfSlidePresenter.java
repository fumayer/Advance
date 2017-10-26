package com.quduquxie.base.module.main.fragment.shelf.presenter;

import com.quduquxie.base.RxPresenter;
import com.quduquxie.base.bean.Book;
import com.quduquxie.base.config.BaseConfig;
import com.quduquxie.base.database.helper.BookDaoHelper;
import com.quduquxie.base.module.main.fragment.shelf.ShelfSlideInterface;
import com.quduquxie.base.module.main.fragment.shelf.view.ShelfSlideFragment;
import com.quduquxie.base.util.CommunalUtil;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created on 17/7/19.
 * Created by crazylei.
 */

public class ShelfSlidePresenter extends RxPresenter implements ShelfSlideInterface.Presenter {

    private ShelfSlideFragment shelfSlideFragment;

    private BookDaoHelper bookDaoHelper;

    public ShelfSlidePresenter(ShelfSlideFragment shelfSlideFragment) {
        this.shelfSlideFragment = shelfSlideFragment;
        this.bookDaoHelper = BookDaoHelper.getInstance(shelfSlideFragment.getContext());
    }

    @Override
    public void initializeData() {
        if (bookDaoHelper == null) {
            this.bookDaoHelper = BookDaoHelper.getInstance(shelfSlideFragment.getContext());
        }

        ArrayList<Book> books = bookDaoHelper.loadAllBooks();

        ArrayList<Book> bookList = new ArrayList<>();

        if (!books.isEmpty()) {
            if (BaseConfig.book_list_sort == 0) {
                Collections.sort(books, new CommunalUtil.MultiComparator());
            }

            for (Book book : books) {
                bookList.add(book);
            }
        }
        shelfSlideFragment.resetBookData(bookList);
    }

    @Override
    public void recycle() {

    }
}