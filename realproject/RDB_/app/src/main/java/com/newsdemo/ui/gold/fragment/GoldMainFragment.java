package com.newsdemo.ui.gold.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.newsdemo.R;
import com.newsdemo.app.Constants;
import com.newsdemo.base.BaseFragment;
import com.newsdemo.base.contract.gold.GoldMainContract;
import com.newsdemo.model.bean.GoldManagerBean;
import com.newsdemo.model.bean.GoldManagerItemBean;
import com.newsdemo.presenter.gold.GoldMainPresenter;
import com.newsdemo.ui.gold.activity.GoldManagerActivity;
import com.newsdemo.ui.gold.adapter.GoldPageAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by jianqiang.hu on 2017/5/12.
 */

public class GoldMainFragment extends BaseFragment<GoldMainPresenter> implements GoldMainContract.View{

    @BindView(R.id.tab_gold_main)
    TabLayout mTabLayout;

    @BindView(R.id.vp_gold_main)
    ViewPager mViewPager;


    public static String[] typeStr={"Android", "iOS", "前端", "后端", "设计", "产品", "阅读", "工具资源"};
    public static String[] type = {"android", "ios", "frontend", "backend", "design", "product", "article", "freebie"};

    List<GoldPageFragment> fragments=new ArrayList<>();
    private int currentIndex=0;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_gold;
    }

    @Override
    protected void initEventAndData() {
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mTabLayout.setupWithViewPager(mViewPager);
        mPresentter.initManagerList();
    }

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    public void updateTab(List<GoldManagerItemBean> mList) {
        fragments.clear();
        mTabLayout.removeAllTabs();
        for (GoldManagerItemBean item:mList){
            if (item.getIsSelect()){
                GoldPageFragment fragment=new GoldPageFragment();
                Bundle bundle=new Bundle();
                bundle.putString(Constants.IT_GOLD_TYPE, type[item.getIndex()]);
                bundle.putString(Constants.IT_GOLD_TYPE_STR,typeStr[item.getIndex()]);
                mTabLayout.addTab(mTabLayout.newTab().setText(typeStr[item.getIndex()]));
                fragment.setArguments(bundle);
                fragments.add(fragment);
            }
        }

        GoldPageAdapter mAdapter = new GoldPageAdapter(getChildFragmentManager(),fragments);
        mViewPager.setAdapter(mAdapter);

        for (GoldManagerItemBean itemBean:mList){
            if (itemBean.getIsSelect()){
                mTabLayout.getTabAt(currentIndex++).setText(typeStr[itemBean.getIndex()]);
            }
        }
        currentIndex=0;
    }

    @Override
    public void jumpToManager(GoldManagerBean mBean) {
        Intent intent=new Intent(getActivity(), GoldManagerActivity.class);
        intent.putExtra(Constants.IT_GOLD_MANAGER, mBean);
        mContext.startActivity(intent);
    }

    @OnClick(R.id.iv_gold_menu)
    public void onClick(View view){
        mPresentter.setManagerList();
    }
}
