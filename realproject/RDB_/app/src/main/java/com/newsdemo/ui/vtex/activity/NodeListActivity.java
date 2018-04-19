package com.newsdemo.ui.vtex.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.newsdemo.R;
import com.newsdemo.app.Constants;
import com.newsdemo.base.RootActivity;
import com.newsdemo.base.contract.vtex.NodeContract;
import com.newsdemo.model.bean.NodeBean;
import com.newsdemo.model.bean.NodeListBean;
import com.newsdemo.presenter.vtex.NodePresenter;
import com.newsdemo.ui.vtex.adapter.NodeListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by jianqiang.hu on 2017/5/31.
 */

public class NodeListActivity extends RootActivity<NodePresenter> implements NodeContract.View{

    @BindView(R.id.tool_bar)
   Toolbar toolBar;

    @BindView(R.id.view_main)
   RecyclerView rvContent;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;


    private NodeListAdapter mAdapter;
    private String nodeName;
    private String nodeValue;
    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_replies;
    }

    @Override
    protected void initEventAndData() {
        super.initEventAndData();
        nodeName=getIntent().getStringExtra(Constants.IT_VTEX_NODE_NAME);
        nodeValue=getIntent().getStringExtra(Constants.IT_VTEX_NODE_VALUE);
        setToolBar(toolBar,nodeValue);
        mAdapter=new NodeListAdapter(mContext,new ArrayList<NodeListBean>());
        rvContent.setLayoutManager(new LinearLayoutManager(mContext));
        rvContent.setAdapter(mAdapter);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getContent(nodeName);
            }
        });
        stateLoading();
        mPresenter.getContent(nodeName);
        mPresenter.getTopInfo(nodeName);
    }

    @Override
    public void stateError() {
        super.stateError();
        if (swipeRefresh.isRefreshing()){
            swipeRefresh.setRefreshing(false);
        }
    }

    @Override
    public void showContent(List<NodeListBean> mList) {
        if (swipeRefresh.isRefreshing()){
            swipeRefresh.setRefreshing(false);
        }
        stateMain();
        mAdapter.setContentData(mList);
    }

    @Override
    public void showTopInfo(NodeBean mTopIngo) {
        mAdapter.setTopData(mTopIngo);
    }
}
