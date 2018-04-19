package com.newsdemo.ui.zhihu.fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.newsdemo.R;
import com.newsdemo.app.Constants;
import com.newsdemo.base.RootFragment;
import com.newsdemo.base.contract.zhihu.HotContract;
import com.newsdemo.model.bean.HotListBean;
import com.newsdemo.presenter.zhihu.HotPresent;
import com.newsdemo.ui.zhihu.activity.ZhihuDetailActivity;
import com.newsdemo.ui.zhihu.adapter.HotAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by jianqiang.hu on 2017/5/15.
 */

public class HotFragment extends RootFragment<HotPresent> implements HotContract.View{

    @BindView(R.id.view_main)
    RecyclerView rvHotContent;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;

    HotAdapter mAdapter;
    List<HotListBean.RecentBean> mList;




    @Override
    protected int getLayoutId() {
        return R.layout.view_common_list;
    }

    @Override
    protected void initEventAndData() {
        super.initEventAndData();
        mList=new ArrayList<>();
        stateLoading();
        mAdapter=new HotAdapter(mContext,mList);
        rvHotContent.setVisibility(View.VISIBLE);
        rvHotContent.setLayoutManager(new LinearLayoutManager(mContext));
        rvHotContent.setAdapter(mAdapter);
        mPresentter.getHotData();

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresentter.getHotData();
            }
        });

        mAdapter.setOnItemClickListener(new HotAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int poition, View shareView) {
                mPresentter.inserReadToDB(mList.get(poition).getNews_id());
                mAdapter.setReadState(poition,true);
                mAdapter.notifyItemChanged(poition);

                Intent intent=new Intent();
                intent.setClass(mContext, ZhihuDetailActivity.class);
                intent.putExtra(Constants.IT_ZHIHU_DETAIL_ID,mList.get(poition).getNews_id());
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(mActivity, shareView, "shareView");
                mContext.startActivity(intent,options.toBundle());
            }
        });
    }

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    public void showContent(HotListBean hotListBean) {
        if (swipeRefresh.isRefreshing()){
            swipeRefresh.setRefreshing(false);
        }
        stateMain();
        rvHotContent.setVisibility(View.VISIBLE);
        mList.clear();
        mList.addAll(hotListBean.getRecent());
       mAdapter.notifyDataSetChanged();
    }
}
