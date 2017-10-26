package com.quduquxie.base.module.main.fragment.library.presenter;

import android.os.Bundle;

import com.quduquxie.base.RxPresenter;
import com.quduquxie.base.module.main.activity.adapter.MainFragmentAdapter;
import com.quduquxie.base.module.main.fragment.content.view.ContentFragment;
import com.quduquxie.base.module.main.fragment.library.LibraryInterface;
import com.quduquxie.base.module.main.fragment.library.view.LibraryFragment;

/**
 * Created on 17/7/17.
 * Created by crazylei.
 */

public class LibraryPresenter extends RxPresenter implements LibraryInterface.Presenter {

    private LibraryFragment libraryFragment;

    public LibraryPresenter(LibraryFragment libraryFragment) {
        this.libraryFragment = libraryFragment;
    }

    @Override
    public void initializeView(MainFragmentAdapter mainFragmentAdapter) {
        String[] title = new String[]{"男频", "女频"};

        Bundle firstBundle = new Bundle();
        firstBundle.putString("key", "boy");
        firstBundle.putString("title", title[0]);
        firstBundle.putInt("type", ContentFragment.TYPE_LIBRARY);
        mainFragmentAdapter.addTable(title[0], title[0], ContentFragment.class, firstBundle);

        Bundle secondBundle = new Bundle();
        secondBundle.putString("key", "girl");
        secondBundle.putString("title", title[1]);
        secondBundle.putInt("type", ContentFragment.TYPE_LIBRARY);
        mainFragmentAdapter.addTable(title[1], title[1], ContentFragment.class, secondBundle);

        libraryFragment.refreshView();
    }

    @Override
    public void recycle() {

    }
}