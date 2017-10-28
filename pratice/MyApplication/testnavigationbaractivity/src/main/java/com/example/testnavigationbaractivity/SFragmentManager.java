package com.example.testnavigationbaractivity;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.TreeMap;

/**
 * Created by dzh on 2017/10/28.
 * 使用多个Fragment进行切换的Manager
 */

public class SFragmentManager {

    private final int mContainer;
    private final FragmentManager mFragmentManager;
    private TreeMap<Integer, Fragment> mFragments = new TreeMap<>();  //使用TreeMap应该高效,无需遍历
    private int mLastFragment;

    public SFragmentManager(@IdRes int layout, FragmentActivity context) {
        mContainer = layout;
        mFragmentManager = context.getSupportFragmentManager();
    }

    public SFragmentManager addFragment(Fragment fragment) {
        if (mFragments != null && fragment != null) {
            mFragments.put(mFragments.size(), fragment);
        }
        return this;
    }

    public void show() {
        show(0);
    }

    public void show(int position) {
        if (mFragmentManager == null || mFragments == null || position < 0
                || mLastFragment < 0 || mFragments.size() < (mLastFragment + 1)
                || mFragments.size() < (position + 1) || !mFragments.containsKey(position)) {
            return;
        }
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if (transaction == null) {
            return;
        }

        for (Integer key : mFragments.keySet()) {
            Fragment fragment = mFragments.get(key);
            transaction.add(mContainer, fragment);
            if (key != position) {
                transaction.hide(fragment);
            }
        }
        transaction.commitAllowingStateLoss();
        mLastFragment = position;

    }


    public void changeCurrentFragment(int position) {
        /*如果position==last 说明是当前正显示，直接打断*/
        if (position == mLastFragment || mFragmentManager == null || mFragments == null || position < 0
                || mLastFragment < 0 || mFragments.size() < (mLastFragment + 1)
                || mFragments.size() < (position + 1) || !mFragments.containsKey(mLastFragment)
                || !mFragments.containsKey(position)) {
            return;
        }

        Fragment hideFragment = mFragments.get(mLastFragment);
        Fragment showFragment = mFragments.get(position);

        if (hideFragment == null || showFragment == null || hideFragment.isDetached() || showFragment.isDetached() || !hideFragment.isAdded() || !showFragment.isAdded()) {
            return;
        }
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if (transaction == null) {
            return;
        }
        if (!hideFragment.isHidden()) {
            transaction.hide(hideFragment);
        }
        transaction.show(showFragment);
        transaction.commitAllowingStateLoss();
        mLastFragment = position;
    }


}
