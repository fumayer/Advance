package com.shortmeet.www.utilsCommen;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.shortmeet.www.MainActivity;
import com.shortmeet.www.R;


/**
 * Fragment切换工具类
 */
public class FragmentSwitchUtil {

    private static Fragment currentFragment = new Fragment();


    /**
     * 警告:  需要切换的Fragment 需要用单例的方式引用
     *
     * 1. 创建一个新的fragment  判断当前Fragment是否被添加
     * 2.hide之前的fragment
     *
     * @param mainActivity
     * @param targetFragment
     */
    public static void switchFragment(MainActivity mainActivity, Fragment targetFragment) {
        FragmentTransaction transaction = mainActivity.getSupportFragmentManager()
                .beginTransaction();
        if (!targetFragment.isAdded()) {
            transaction
                     .hide(currentFragment)
                     .add(R.id.main_fagcontent, targetFragment)
                     .commit();
        } else {
            transaction
                    .hide(currentFragment)
                    .show(targetFragment)
                    .commit();
        }
        currentFragment = targetFragment;
    }
}
