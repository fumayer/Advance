package com.quduquxie.base.module.main.fragment.shelf;

import com.quduquxie.base.BasePresenter;
import com.quduquxie.base.BaseView;
import com.quduquxie.base.bean.Book;

import java.util.ArrayList;

/**
 * Created on 17/7/19.
 * Created by crazylei.
 */

public interface ShelfGridInterface {

    interface Presenter extends BasePresenter {

        void initializeData();
    }

    interface View extends BaseView {

        void resetBookData(ArrayList<Book> books);
    }
}