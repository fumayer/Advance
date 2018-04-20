package com.newsdemo.ui.vtex.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.newsdemo.R;
import com.newsdemo.app.Constants;
import com.newsdemo.base.RootFragment;
import com.newsdemo.base.contract.vtex.VtexContract;
import com.newsdemo.model.bean.TopicListBean;
import com.newsdemo.presenter.vtex.VtexPresenter;
import com.newsdemo.ui.vtex.adapter.TopicAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * Created by jianqiang.hu on 2017/5/27.
 */

public class VtexPagerFragment extends RootFragment<VtexPresenter> implements VtexContract.View {
   @BindView(R.id.view_main)
   RecyclerView rvContent;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;

    TopicAdapter mAdapter;

   private String mType;

    @Override
    protected int getLayoutId() {
        return R.layout.view_common_list;
    }

    @Override
    protected void initEventAndData() {
        super.initEventAndData();
        mType=getArguments().getString(Constants.IT_VTEX_TYPE);
        mAdapter=new TopicAdapter(mContext,new ArrayList<TopicListBean>());
        rvContent.setLayoutManager(new LinearLayoutManager(mContext));
        rvContent.setAdapter(mAdapter);

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresentter.getContent(mType);
            }
        });
        stateLoading();
        mPresentter.getContent(mType);
    }


    @Override
    public void stateError() {
        super.stateError();
        if (swipeRefresh.isRefreshing()){
            swipeRefresh.setRefreshing(false);
        }
    }

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    public void showContent(List<TopicListBean> mList) {
        if (swipeRefresh.isRefreshing()){
            swipeRefresh.setRefreshing(false);
        }
        stateMain();
        mAdapter.updateData(mList);
    }
}
