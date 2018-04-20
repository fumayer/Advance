package com.newsdemo.ui.zhihu.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.newsdemo.R;
import com.newsdemo.app.Constants;
import com.newsdemo.base.RootActivity;
import com.newsdemo.base.contract.zhihu.SectionChildContract;
import com.newsdemo.model.bean.SectionChildListBean;
import com.newsdemo.presenter.zhihu.SectionChildPresent;
import com.newsdemo.ui.zhihu.adapter.SectionChildAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.newsdemo.app.Constants.IT_ZHIHU_SECTION_ID;
import static com.newsdemo.app.Constants.IT_ZHIHU_SECTION_TITLE;

/**
 * Created by jianqiang.hu on 2017/5/24.
 */

public class SectionActivity extends RootActivity<SectionChildPresent> implements SectionChildContract.View{

    @BindView(R.id.view_main)
    RecyclerView rvSectionContent;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.tool_bar)
    Toolbar mToolBar;

    int id;
    String title;
    List<SectionChildListBean.StoriesBean> mList;
    SectionChildAdapter mAdapter;

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_section;
    }

    @Override
    public void showContent(SectionChildListBean sectionChildListBean) {
        if(swipeRefresh.isRefreshing()) {
            swipeRefresh.setRefreshing(false);
        }
        stateMain();
        mList.clear();
        mList.addAll(sectionChildListBean.getStories());
        mAdapter.notifyDataSetChanged();
    }
    @Override
    public void stateError() {
        super.stateError();
        if(swipeRefresh.isRefreshing()) {
            swipeRefresh.setRefreshing(false);
        }
    }
    @Override
    protected void initEventAndData() {
        super.initEventAndData();
        Intent intent = getIntent();
        id = intent.getIntExtra(IT_ZHIHU_SECTION_ID, 0);
        title = intent.getStringExtra(IT_ZHIHU_SECTION_TITLE);
        setToolBar(mToolBar,title);
        mList = new ArrayList<>();
        mAdapter = new SectionChildAdapter(mContext, mList);
        rvSectionContent.setLayoutManager(new LinearLayoutManager(mContext));
        rvSectionContent.setAdapter(mAdapter);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getThemeChildData(id);
            }
        });

        mAdapter.setOnItemClickListener(new SectionChildAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int position, View shareView) {
                mPresenter.insertReadToDB(mList.get(position).getId());
                mAdapter.setReadState(position, true);
                mAdapter.notifyItemChanged(position);
                Intent intent = new Intent();
                intent.setClass(mContext, ZhihuDetailActivity.class);
                intent.putExtra(Constants.IT_ZHIHU_DETAIL_ID, mList.get(position).getId());
                if (shareView != null) {
                    mContext.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(mContext, shareView, "shareView").toBundle());
                } else {
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(mContext).toBundle());
                }
            }
        });
        mPresenter.getThemeChildData(id);
        stateLoading();

    }
}
