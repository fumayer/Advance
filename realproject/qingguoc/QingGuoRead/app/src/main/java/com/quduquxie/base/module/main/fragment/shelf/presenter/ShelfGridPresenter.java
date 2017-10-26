package com.quduquxie.base.module.main.fragment.shelf.presenter;

import com.quduquxie.base.RxPresenter;
import com.quduquxie.base.bean.Book;
import com.quduquxie.base.config.BaseConfig;
import com.quduquxie.base.database.helper.BookDaoHelper;
import com.quduquxie.base.module.main.fragment.shelf.ShelfGridInterface;
import com.quduquxie.base.module.main.fragment.shelf.view.ShelfGridFragment;
import com.quduquxie.base.util.CommunalUtil;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created on 17/7/19.
 * Created by crazylei.
 */

public class ShelfGridPresenter extends RxPresenter implements ShelfGridInterface.Presenter {

    private ShelfGridFragment shelfGridFragment;

    private BookDaoHelper bookDaoHelper;

    public ShelfGridPresenter(ShelfGridFragment shelfGridFragment) {
        this.shelfGridFragment = shelfGridFragment;
        this.bookDaoHelper = BookDaoHelper.getInstance(shelfGridFragment.getContext());
    }

    @Override
    public void initializeData() {
        if (bookDaoHelper == null) {
            this.bookDaoHelper = BookDaoHelper.getInstance(shelfGridFragment.getContext());
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
        shelfGridFragment.resetBookData(bookList);
    }

    @Override
    public void recycle() {

    }
}