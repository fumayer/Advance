package com.newsdemo.ui.gank.fragment;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.newsdemo.R;
import com.newsdemo.app.Constants;
import com.newsdemo.base.SimpleFragment;
import com.newsdemo.component.RxBus;
import com.newsdemo.model.event.SearchEvent;
import com.newsdemo.ui.gank.adapter.GankMainAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by jianqiang.hu on 2017/5/12.
 */

public class GankMainFragment extends SimpleFragment{
    public static   String[] tabTitle = new String[]{"Android","iOS","前端","福利"};
    @BindView(R.id.tab_zhihu_main)
    TabLayout tabLayout;

    @BindView(R.id.vp_zhihu_main)
    ViewPager mViewPager;

    List<Fragment> fragments=new ArrayList<>();

    GankMainAdapter mAdapter;
    TechFragment androidFragment;
    TechFragment iosFragment;
    TechFragment webFragment;
    GirlFragment girlFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_zhihu_main;
    }

    @Override
    protected void initEventAndData() {
        androidFragment=new TechFragment();
        iosFragment=new TechFragment();
        webFragment=new TechFragment();
        girlFragment=new GirlFragment();
        //android
        Bundle androidBundle=new Bundle();
        androidBundle.putString(Constants.IT_GANK_TYPE,tabTitle[0]);
        androidBundle.putInt(Constants.IT_GANK_TYPE_CODE,Constants.TYPE_ANDROID);
        androidFragment.setArguments(androidBundle);

        //ios
        Bundle iosBundle = new Bundle();
        iosBundle.putString(Constants.IT_GANK_TYPE, tabTitle[1]);
        iosBundle.putInt(Constants.IT_GANK_TYPE_CODE, Constants.TYPE_IOS);
        iosFragment.setArguments(iosBundle);

        //web
        Bundle webBundle = new Bundle();
        webBundle.putString(Constants.IT_GANK_TYPE, tabTitle[2]);
        webBundle.putInt(Constants.IT_GANK_TYPE_CODE, Constants.TYPE_WEB);
        webFragment.setArguments(webBundle);

        fragments.add(androidFragment);
        fragments.add(iosFragment);
        fragments.add(webFragment);
        fragments.add(girlFragment);

        mAdapter=new GankMainAdapter(getChildFragmentManager(),fragments);
        mViewPager.setAdapter(mAdapter);


        tabLayout.addTab(tabLayout.newTab().setText(tabTitle[0]));
        tabLayout.addTab(tabLayout.newTab().setText(tabTitle[1]));
        tabLayout.addTab(tabLayout.newTab().setText(tabTitle[2]));
        tabLayout.addTab(tabLayout.newTab().setText(tabTitle[3]));
        tabLayout.setupWithViewPager(mViewPager);

    }

    public void doSearch(String query){
        switch (mViewPager.getCurrentItem()){
            case 0:
                RxBus.getDefault().post(new SearchEvent(query,Constants.TYPE_ANDROID));
                break;
            case 1:
                RxBus.getDefault().post(new SearchEvent(query,Constants.TYPE_IOS));
                break;
            case 2:
                RxBus.getDefault().post(new SearchEvent(query,Constants.TYPE_WEB));
                break;
            case 3:
                RxBus.getDefault().post(new SearchEvent(query,Constants.TYPE_GIRL));
                break;
        }
    }
}
