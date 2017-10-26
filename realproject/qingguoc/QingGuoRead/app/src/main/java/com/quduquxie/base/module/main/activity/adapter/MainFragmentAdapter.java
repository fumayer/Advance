package com.quduquxie.base.module.main.activity.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;

import com.quduquxie.base.bean.ViewPagerInformation;
import com.quduquxie.base.widget.NavigationBarStrip;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created on 17/7/20.
 * Created by crazylei.
 */

public class MainFragmentAdapter extends FragmentStatePagerAdapter {

    private WeakReference<Context> contextReference;

    private ViewPager viewPager;

    private ArrayList<ViewPagerInformation> viewPageInfoList = new ArrayList<>();

    private SparseArray<Fragment> fragments = new SparseArray<>();

    private NavigationBarStrip navigationBarStrip;

    public MainFragmentAdapter(Context context, FragmentManager fragmentManager, NavigationBarStrip navigationBarStrip, ViewPager viewPager) {
        super(fragmentManager);
        this.contextReference = new WeakReference<>(context);
        this.navigationBarStrip = navigationBarStrip;
        this.viewPager = viewPager;
        this.viewPager.setAdapter(this);

        navigationBarStrip.setViewPager(viewPager);
    }

    public void addTable(String title, String flag, Class<?> className, Bundle bundle) {
        ViewPagerInformation viewPagerInformation = new ViewPagerInformation(title, flag, className, bundle);
        addFragment(viewPagerInformation);
    }

    private void addFragment(ViewPagerInformation viewPagerInformation) {
        if (viewPagerInformation == null) {
            return;
        }

        if (navigationBarStrip != null) {
            navigationBarStrip.insertTitle(viewPagerInformation.title);
        }
        viewPageInfoList.add(viewPagerInformation);
        notifyDataSetChanged();
    }

    public void remove() {
        remove(0);
    }

    public void remove(int index) {
        if (viewPageInfoList.isEmpty()) {
            return;
        }
        if (index < 0) {
            index = 0;
        }
        if (index >= viewPageInfoList.size()) {
            index = viewPageInfoList.size() - 1;
        }
        viewPageInfoList.remove(index);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return viewPageInfoList.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public Fragment getItem(int position) {
        ViewPagerInformation viewPagerInformation = viewPageInfoList.get(position);

        if (fragments.get(position) != null) {
            return fragments.get(position);
        } else {
            Fragment fragment = Fragment.instantiate(contextReference.get(), viewPagerInformation.className.getName(), viewPagerInformation.bundle);
            fragments.put(position, fragment);
            return fragment;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return viewPageInfoList.get(position).title;
    }

    public void recycle() {
        if (fragments != null) {
            fragments.clear();
            fragments = null;
        }

        if (viewPager != null) {
            viewPager.removeAllViews();
            viewPager = null;
        }

        if (viewPageInfoList != null) {
            viewPageInfoList.clear();
            viewPageInfoList = null;
        }

        if (navigationBarStrip != null) {
            navigationBarStrip = null;
        }

        if (contextReference != null && contextReference.get() != null) {
            contextReference = new WeakReference<>(null);
        }
    }
}