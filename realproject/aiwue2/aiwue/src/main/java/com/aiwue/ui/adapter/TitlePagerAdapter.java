package com.aiwue.ui.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.aiwue.base.BaseFragment;
import com.aiwue.model.CaptionTitle;

import java.util.List;

/**
 * Created by Administrator on 2016/3/30.
 */
public class TitlePagerAdapter extends FragmentPagerAdapter {

    private List<BaseFragment> fragments;
    private CaptionTitle[] categoryList;

    public TitlePagerAdapter(FragmentManager fm, List<BaseFragment> fragments, CaptionTitle[] categoryList) {
        super(fm);
        this.fragments = fragments;
        this.categoryList = categoryList;
    }

    @Override
    public BaseFragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return categoryList == null ? "" : categoryList[position].getName();
    }

}
