package com.quduquxie.base.module.main.fragment.selected.presenter;

import android.os.Bundle;

import com.quduquxie.base.RxPresenter;
import com.quduquxie.base.module.main.activity.adapter.MainFragmentAdapter;
import com.quduquxie.base.module.main.fragment.content.view.ContentFragment;
import com.quduquxie.base.module.main.fragment.selected.SelectedInterface;
import com.quduquxie.base.module.main.fragment.selected.view.SelectedFragment;

/**
 * Created on 17/7/17.
 * Created by crazylei.
 */

public class SelectedPresenter extends RxPresenter implements SelectedInterface.Presenter {

    private SelectedFragment selectedFragment;

    public SelectedPresenter(SelectedFragment selectedFragment) {
        this.selectedFragment = selectedFragment;
    }

    @Override
    public void initializeView(MainFragmentAdapter mainFragmentAdapter) {
        String[] title = new String[]{"男频", "今日精选", "女频"};

        Bundle firstBundle = new Bundle();
        firstBundle.putString("key", "boy");
        firstBundle.putString("title", title[0]);
        firstBundle.putInt("type", ContentFragment.TYPE_SELECTED);
        mainFragmentAdapter.addTable(title[0], title[0], ContentFragment.class, firstBundle);

        Bundle secondBundle = new Bundle();
        secondBundle.putString("key", "index");
        secondBundle.putString("title", title[1]);
        secondBundle.putInt("type", ContentFragment.TYPE_SELECTED);
        mainFragmentAdapter.addTable(title[1], title[1], ContentFragment.class, secondBundle);

        Bundle thirdBundle = new Bundle();
        thirdBundle.putString("key", "girl");
        thirdBundle.putString("title", title[2]);
        thirdBundle.putInt("type", ContentFragment.TYPE_SELECTED);
        mainFragmentAdapter.addTable(title[2], title[2], ContentFragment.class, thirdBundle);

        selectedFragment.refreshView();
    }

    @Override
    public void recycle() {
        if (selectedFragment != null) {
            selectedFragment = null;
        }
    }
}