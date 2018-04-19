package com.newsdemo.ui.gold.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.newsdemo.ui.gold.fragment.GoldPageFragment;

import java.util.List;

/**
 * Created by jianqiang.hu on 2017/5/26.
 */

public class GoldPageAdapter extends FragmentPagerAdapter {
    private List<GoldPageFragment> fragments;

    public GoldPageAdapter(FragmentManager fm,List<GoldPageFragment> fragments) {
        super(fm);
        this.fragments=fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
