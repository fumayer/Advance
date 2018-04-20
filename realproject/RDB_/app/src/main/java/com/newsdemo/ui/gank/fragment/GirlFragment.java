package com.newsdemo.ui.gank.fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.newsdemo.R;
import com.newsdemo.app.Constants;
import com.newsdemo.base.RootFragment;
import com.newsdemo.base.SimpleFragment;
import com.newsdemo.base.contract.gank.GirlConstarct;
import com.newsdemo.model.bean.GankItemBean;
import com.newsdemo.presenter.gank.GirlPresenter;
import com.newsdemo.ui.gank.activity.GirlDetailActivity;
import com.newsdemo.ui.gank.adapter.GirlAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by jianqiang.hu on 2017/5/25.
 */

public class GirlFragment extends RootFragment<GirlPresenter> implements GirlConstarct.View{

    @BindView(R.id.view_main)
    RecyclerView rvGirlContent;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;

    private static final int SPAN_COUNT = 2;
    List<GankItemBean> mList;
    StaggeredGridLayoutManager mStaggeredGridLayoutManager;

    GirlAdapter mAdapter;
    private boolean isLoadingMore = false;
    @Override
    protected int getLayoutId() {
        return R.layout.view_common_list;
    }

    @Override
    protected void initEventAndData() {
        super.initEventAndData();
        mList=new ArrayList<>();
        mAdapter=new GirlAdapter(mContext,mList);
        mStaggeredGridLayoutManager=new StaggeredGridLayoutManager(SPAN_COUNT,StaggeredGridLayoutManager.VERTICAL);
        mStaggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        mStaggeredGridLayoutManager.setItemPrefetchEnabled(false);
        rvGirlContent.setLayoutManager(mStaggeredGridLayoutManager);
        rvGirlContent.setAdapter(mAdapter);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresentter.getGirlData();
            }
        });
        rvGirlContent.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int[] visibleItems = mStaggeredGridLayoutManager.findLastVisibleItemPositions(null);
                int lastItem = Math.max(visibleItems[0],visibleItems[1]);
                if (lastItem > mAdapter.getItemCount() - 5 && !isLoadingMore && dy > 0 ) {
                    isLoadingMore = true;
                    mPresentter.getMoreGirlData();
                }
            }
        });
        mAdapter.setOnItemClickListener(new GirlAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int position, View view) {
                Intent intent=new Intent();
                intent.setClass(mContext, GirlDetailActivity.class);
                intent.putExtra(Constants.IT_GANK_GRIL_URL,mList.get(position).getUrl());
                intent.putExtra(Constants.IT_GANK_GRIL_ID,mList.get(position).get_id());
                ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(mActivity,view,"shareView");
                mContext.startActivity(intent,options.toBundle());
            }
        });
        stateLoading();
        mPresentter.getGirlData();
    }

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    public void showContent(List<GankItemBean> list) {
        if (swipeRefresh.isRefreshing()) {
            swipeRefresh.setRefreshing(false);
        }
        stateMain();
        mList.clear();
        mList.addAll(list);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showMoreContent(List<GankItemBean> list) {
        isLoadingMore = false;
        mList.addAll(list);
        for(int i =mList.size() - GirlPresenter.NUM_OF_PAGE ; i < mList.size(); i++) {    //使用notifyDataSetChanged已加载的图片会有闪烁，遂使用inserted逐个插入
            mAdapter.notifyItemInserted(i);
        }
    }
}
