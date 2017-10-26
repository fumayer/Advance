package com.quduquxie.base.module.billboard.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.quduquxie.R;
import com.quduquxie.base.bean.ViewPagerInformation;
import com.quduquxie.base.module.billboard.view.fragment.BillboardContentFragment;
import com.quduquxie.base.widget.SlidePagerTabStrip;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created on 17/7/25.
 * Created by crazylei.
 */

public class BillboardAdapter extends FragmentStatePagerAdapter {

    private WeakReference<Context> contextReference;

    private final ViewPager viewPager;
    private SparseArray<Fragment> fragments = new SparseArray<>();

    private final ArrayList<ViewPagerInformation> informationList = new ArrayList<>();

    protected SlidePagerTabStrip slidePagerTabStrip;

    public BillboardAdapter(Context context, FragmentManager fragmentManager, SlidePagerTabStrip slidePagerTabStrip, ViewPager viewPager) {
        super(fragmentManager);
        this.contextReference = new WeakReference<>(context);
        this.slidePagerTabStrip = slidePagerTabStrip;
        this.viewPager = viewPager;
        this.viewPager.setAdapter(this);

        slidePagerTabStrip.setViewPager(viewPager);
    }

    public void insetTab(String title, String flag, Class<?> className, Bundle bundle) {
        ViewPagerInformation viewPagerInformation = new ViewPagerInformation(title, flag, className, bundle);
        insertFragment(viewPagerInformation);
    }

    private void insertFragment(ViewPagerInformation viewPagerInformation) {
        if (viewPagerInformation == null) {
            return;
        }

        TextView title = (TextView) LayoutInflater.from(contextReference.get()).inflate(R.layout.layout_view_billboard_navigation, null, false);
        title.setGravity(Gravity.CENTER);
        title.setText(viewPagerInformation.title);

        if (slidePagerTabStrip != null) {
            slidePagerTabStrip.addTab(title);
        }

        informationList.add(viewPagerInformation);
        notifyDataSetChanged();
    }

    public void remove() {
        remove(0);
    }


    public void remove(int index) {
        if (informationList.isEmpty()) {
            return;
        }
        if (index < 0) {
            index = 0;
        }
        if (index >= informationList.size()) {
            index = informationList.size() - 1;
        }
        informationList.remove(index);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return informationList.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public Fragment getItem(int position) {
        ViewPagerInformation viewPagerInformation = informationList.get(position);

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
        return informationList.get(position).title;
    }

    public void changeFragmentBundle(String date) {

        try {
            if (fragments != null) {
                final int size = fragments.size();

                for (int i = 0; i < size; i++) {
                    BillboardContentFragment billboardContentFragment = (BillboardContentFragment) fragments.get(i);
                    if (billboardContentFragment != null) {
                        billboardContentFragment.changeData(date);
                    }
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
