package com.newsdemo.ui.zhihu.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.newsdemo.R;
import com.newsdemo.base.SimpleFragment;
import com.newsdemo.ui.zhihu.adapter.ZhihuMainAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by jianqiang.hu on 2017/5/12.
 */

public class ZhihuMainFragment extends SimpleFragment{

    @BindView(R.id.tab_zhihu_main)
    TabLayout mTabLayout;

    @BindView(R.id.vp_zhihu_main)
    ViewPager mViewPager;



    List<Fragment> fragments =new ArrayList<Fragment>();

    ZhihuMainAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_zhihu_main;
    }

    @Override
    protected void initEventAndData() {
        fragments.add(new DailyFragment());
        fragments.add(new Themefragment());
        fragments.add(new SectionFragment());
        fragments.add(new HotFragment());
        mAdapter=new ZhihuMainAdapter(getChildFragmentManager(),fragments);
        mViewPager.setAdapter(mAdapter);


        //tab的标题在adapter中设置，因为setupWithViewPager-->setPagerAdapter-->populateFromPagerAdapter中调用了removeAllTabs，
        //所以调用setupWithViewPager以后会将之前添加的title清空，自己又new新的title,所以我们之前添加的标题都不见了。
        /**
         * 解决方案2种
         * 第一种：
         * 可以执行完setupWithViewPager（）后，再添加标题
         * tabLayout.getTabAt(0).setText("日报");
           tabLayout.getTabAt(1).setText("主题");
           tabLayout.getTabAt(2).setText("专栏");
         第二种：
         在adapter中实现getPageTitle
             @Override
             public CharSequence getPageTitle(int position) {
             return titles[position];
             }
         */
        mTabLayout.addTab(mTabLayout.newTab());
        mTabLayout.addTab(mTabLayout.newTab());
        mTabLayout.addTab(mTabLayout.newTab());
        mTabLayout.addTab(mTabLayout.newTab());
        mTabLayout.setupWithViewPager(mViewPager);


    }
}
