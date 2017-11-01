package com.example.testnavigationbaractivity;

import android.os.Bundle;
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
    private Bundle mSavedInstanceState;
    private final FragmentManager mFragmentManager;
    private TreeMap<Integer, Fragment> mFragments = new TreeMap<>();  //使用TreeMap应该高效,无需遍历
    private int mLastFragment = -1;

    public SFragmentManager(@IdRes int layout, FragmentActivity context) {
        mContainer = layout;
        mFragmentManager = context.getSupportFragmentManager();
    }

    public SFragmentManager setBundle(Bundle savedInstanceState) {
        mSavedInstanceState = savedInstanceState;
        return this;
    }

    public SFragmentManager addFragment(Fragment fragment) {
        Fragment fragmentByTag = null;
        if (mSavedInstanceState != null && mFragmentManager != null && mFragments != null) {
            fragmentByTag = mFragmentManager.findFragmentByTag(Integer.toString(mFragments.size()));
        }
        if (fragmentByTag != null) {
            mLastFragment = mFragments.size();
            fragment = fragmentByTag;
        }

        if (fragment != null) {
            mFragments.put(mFragments.size(), fragment);
        }
        return this;
    }

    public void showSelectFrag() {
        showSelectFrag(0);
    }


    public void showSelectFrag(int position) {
           /*如果position==last 说明是当前正显示，直接打断*/
        if (mLastFragment == position || mFragmentManager == null || mFragments == null || position < 0
                || mFragments.size() < (position + 1)
                || !mFragments.containsKey(position)) {
            return;
        }
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        Fragment showCurrentFrag = mFragments.get(position);
        Fragment lastFrag = null;
        if (mLastFragment >= 0 && mFragments.containsKey(mLastFragment)) {
            lastFrag = mFragments.get(mLastFragment);
        }


        if (showCurrentFrag == null || transaction == null) {
            return;
        }


        if (showCurrentFrag.isAdded()) {
            transaction.show(showCurrentFrag);
        } else {
            transaction.add(mContainer, showCurrentFrag, Integer.toString(position));
        }
        if (lastFrag != null && lastFrag.isAdded()) {
            if (!lastFrag.isHidden()) {
                transaction.hide(lastFrag);
            }
        }
        transaction.commitAllowingStateLoss();

        mLastFragment = position;
    }

}
