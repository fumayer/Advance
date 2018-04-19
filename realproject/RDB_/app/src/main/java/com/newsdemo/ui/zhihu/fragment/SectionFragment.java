package com.newsdemo.ui.zhihu.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.newsdemo.R;
import com.newsdemo.base.RootFragment;
import com.newsdemo.base.contract.zhihu.SectionContract;
import com.newsdemo.model.bean.SectionListBean;
import com.newsdemo.presenter.zhihu.SectionPresent;
import com.newsdemo.ui.zhihu.adapter.SectionAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by jianqiang.hu on 2017/5/15.
 */

public class SectionFragment extends RootFragment<SectionPresent> implements SectionContract.View{
    @BindView(R.id.view_main)
    RecyclerView rvSectionList;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;

    List<SectionListBean.DataBean> mList;
    SectionAdapter mAdapter;




    @Override
    protected int getLayoutId() {
        return R.layout.view_common_list;
    }

    @Override
    protected void initEventAndData() {
        super.initEventAndData();
        mList = new ArrayList<>();
        mAdapter = new SectionAdapter(mContext,mList);
        rvSectionList.setLayoutManager(new GridLayoutManager(mContext, 2));
        rvSectionList.setAdapter(mAdapter);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresentter.getSectionData();
            }
        });
        mPresentter.getSectionData();
        stateLoading();

    }

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    public void showContent(SectionListBean sectionListBean) {
        if(swipeRefresh.isRefreshing()) {
            swipeRefresh.setRefreshing(false);
        }
        stateMain();
        rvSectionList.setVisibility(View.VISIBLE);
        mList.clear();
        mList.addAll(sectionListBean.getData());
        mAdapter.notifyDataSetChanged();
    }
}
