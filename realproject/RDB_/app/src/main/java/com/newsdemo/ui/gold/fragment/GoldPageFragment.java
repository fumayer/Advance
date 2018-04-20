package com.newsdemo.ui.gold.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.newsdemo.R;
import com.newsdemo.app.Constants;
import com.newsdemo.base.RootFragment;
import com.newsdemo.base.contract.gold.GoldContract;
import com.newsdemo.model.bean.GoldListBean;
import com.newsdemo.presenter.gold.GoldPresenter;
import com.newsdemo.ui.gold.adapter.GoldListAdapter;
import com.newsdemo.widget.GoldItemDection;
import com.newsdemo.widget.TouchSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by jianqiang.hu on 2017/5/26.
 */

public class GoldPageFragment extends RootFragment<GoldPresenter> implements GoldContract.View {

    @BindView(R.id.view_main)
    RecyclerView rvGoldList;
    @BindView(R.id.swipe_refresh)
    TouchSwipeRefreshLayout swipeRefresh;

    private GoldListAdapter mAdapter;
    private GoldItemDection mDecoration;

    private boolean isLoadingMore=false;

    private String mType;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_gold_page;
    }

    @Override
    protected void initEventAndData() {
        super.initEventAndData();
        mType=getArguments().getString(Constants.IT_GOLD_TYPE);
        mDecoration=new GoldItemDection();
        mAdapter=new GoldListAdapter(mContext,new ArrayList<GoldListBean>(),getArguments().getString(Constants.IT_GOLD_TYPE_STR));
        rvGoldList.setLayoutManager(new LinearLayoutManager(mContext));
        rvGoldList.setAdapter(mAdapter);
        rvGoldList.addItemDecoration(mDecoration);


        rvGoldList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisableItem=((LinearLayoutManager)rvGoldList.getLayoutManager()).findLastVisibleItemPosition();
                int totalItemCount=rvGoldList.getLayoutManager().getItemCount();
                if (lastVisableItem>totalItemCount-2 && dy>2){
                    if (!isLoadingMore){
                        isLoadingMore=true;
                        mPresentter.getModeGoldData();
                    }
                }
            }
        });

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!mAdapter.getHotFlag()){
                    rvGoldList.addItemDecoration(mDecoration);
                }
                mAdapter.setHotFlag(true);
                mPresentter.getGoldData(mType);
            }
        });
        mAdapter.setOnHotCloseListener(new GoldListAdapter.onHotCloseListener() {
            @Override
            public void onClose() {
                rvGoldList.removeItemDecoration(mDecoration);
            }
        });
        stateLoading();
        mPresentter.getGoldData(mType);
    }

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    public void showContent(List<GoldListBean> goldListBeen) {
        if (swipeRefresh.isRefreshing()){
            swipeRefresh.setRefreshing(false);
        }
        stateMain();
        mAdapter.updateData(goldListBeen);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showMoreContent(List<GoldListBean> goldListBeen, int start, int end) {
        mAdapter.updateData(goldListBeen);
        mAdapter.notifyItemRangeInserted(start,end);
        isLoadingMore=false;
    }

    @Override
    public void stateError() {
        super.stateError();
        if (swipeRefresh.isRefreshing()){
            swipeRefresh.setRefreshing(false);
        }
    }
}
