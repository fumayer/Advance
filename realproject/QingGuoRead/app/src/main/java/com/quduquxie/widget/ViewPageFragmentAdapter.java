package com.quduquxie.widget;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.quduquxie.R;
import com.quduquxie.bean.ViewPageInfo;

import java.util.ArrayList;

public class ViewPageFragmentAdapter extends FragmentStatePagerAdapter {

    private final Context content;
    protected PagerSlidingTabStrip pagerSlidingTabStrip;
    private final ViewPager viewPager;
    private final ArrayList<ViewPageInfo> tabs = new ArrayList<>();

    private VerticalViewPager verticalViewPager;

    public ViewPageFragmentAdapter(FragmentManager fragmentManager, PagerSlidingTabStrip pageStrip, ViewPager viewPager) {
        super(fragmentManager);
        this.content = viewPager.getContext();
        this.pagerSlidingTabStrip = pageStrip;
        this.viewPager = viewPager;
        this.viewPager.setAdapter(this);
        pagerSlidingTabStrip.setViewPager(viewPager);
    }

    public ViewPageFragmentAdapter(FragmentManager fragmentManager, ViewPager viewPager) {
        super(fragmentManager);
        this.content = viewPager.getContext();
        this.viewPager = viewPager;
        this.viewPager.setAdapter(this);
    }

    public ViewPageFragmentAdapter(FragmentManager fragmentManager, VerticalViewPager viewPager) {
        super(fragmentManager);
        this.content = viewPager.getContext();
        this.viewPager = null;
        this.verticalViewPager = viewPager;
        this.verticalViewPager.setAdapter(this);
    }


    public void addTab(String title, String tag, Class<?> className, Bundle args) {
        ViewPageInfo viewPageInfo = new ViewPageInfo(title, tag, className, args);
        addFragment(viewPageInfo);
    }

    private void addFragment(ViewPageInfo info) {
        if (info == null) {
            return;
        }

        TextView title = (TextView) LayoutInflater.from(content).inflate(R.layout.layout_textview, null, false);
        title.setGravity(Gravity.CENTER);
        title.setText(info.title);

        if (pagerSlidingTabStrip != null) {
            pagerSlidingTabStrip.addTab(title);
        }

        tabs.add(info);
        notifyDataSetChanged();
    }

    /**
     * 移除第一次
     */
    public void remove() {
        remove(0);
    }

    /**
     * 移除一个tab
     * @param index 备注：如果index小于0，则从第一个开始删 如果大于tab的数量值则从最后一个开始删除
     */
    public void remove(int index) {
        if (tabs.isEmpty()) {
            return;
        }
        if (index < 0) {
            index = 0;
        }
        if (index >= tabs.size()) {
            index = tabs.size() - 1;
        }
        tabs.remove(index);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return tabs.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public Fragment getItem(int position) {
        ViewPageInfo viewPageInfo = tabs.get(position);
        return Fragment.instantiate(content, viewPageInfo.className.getName(), viewPageInfo.bundle);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs.get(position).title;
    }
}