package com.quduquxie.communal.widget.expression;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;

import com.orhanobut.logger.Logger;
import com.quduquxie.bean.ViewPageInfo;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class ExpressionViewAdapter extends FragmentStatePagerAdapter {

    private ViewPager viewPager;

    private ExpressionListener expressionListener;

    private WeakReference<Context> contextReference;

    private SparseArray<Fragment> expressionGridFragments;

    private final ArrayList<ViewPageInfo> viewPageInfoList = new ArrayList<>();

    public ExpressionViewAdapter(FragmentManager fragmentManager, ViewPager viewPager, ExpressionListener expressionListener) {
        super(fragmentManager);
        this.viewPager = viewPager;
        this.expressionListener = expressionListener;
        this.contextReference = new WeakReference<>(viewPager.getContext());
        this.expressionGridFragments = new SparseArray<>();
        this.viewPager.setAdapter(this);
    }

    public void addTab(String title, String tag, Class<?> className, Bundle bundle) {
        ViewPageInfo viewPageInfo = new ViewPageInfo(title, tag, className, bundle);
        addFragment(viewPageInfo);
    }

    private void addFragment(ViewPageInfo info) {
        if (info == null) {
            return;
        }

        viewPageInfoList.add(info);
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
        ViewPageInfo info = viewPageInfoList.get(position);
        Logger.d(expressionGridFragments.indexOfKey(position));
        if (expressionGridFragments.indexOfKey(position) >= 0) {
            return expressionGridFragments.get(position);
        } else {
            ExpressionGridFragment expressionGridFragment = (ExpressionGridFragment) Fragment.instantiate(contextReference.get(), info.className.getName(), info.bundle);
            expressionGridFragment.setExpressionListener(expressionListener);

            expressionGridFragments.put(position, expressionGridFragment);
            return expressionGridFragment;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return viewPageInfoList.get(position).title;
    }
}
