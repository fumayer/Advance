package com.newsdemo.ui.vtex.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.newsdemo.ui.vtex.fragment.VtexMainFragment;
import com.newsdemo.ui.vtex.fragment.VtexPagerFragment;

import java.util.List;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

/**
 * Created by jianqiang.hu on 2017/5/27.
 */

public class VtexPagerAdapter extends FragmentPagerAdapter{
    private List<VtexPagerFragment> fragments;
    public VtexPagerAdapter(FragmentManager fm, List<VtexPagerFragment> fragments){
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

    @Override
    public CharSequence getPageTitle(int position) {
        return VtexMainFragment.typeStr[position];
    }
}
