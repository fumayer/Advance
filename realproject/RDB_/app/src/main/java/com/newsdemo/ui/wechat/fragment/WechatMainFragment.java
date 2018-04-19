package com.newsdemo.ui.wechat.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.newsdemo.R;
import com.newsdemo.base.RootFragment;
import com.newsdemo.base.contract.wechat.WechatContract;
import com.newsdemo.model.bean.WXItemBean;
import com.newsdemo.presenter.wechat.WeChatPresent;
import com.newsdemo.ui.wechat.adapter.WechatAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by jianqiang.hu on 2017/5/12.
 */

public class WechatMainFragment extends RootFragment<WeChatPresent> implements WechatContract.View{

    @BindView(R.id.view_main)
    RecyclerView rvWeChatList;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;


    List<WXItemBean> mList;

    WechatAdapter mAdapter;

    boolean isLoadingMore=false;

    @Override
    protected int getLayoutId() {
        return R.layout.view_common_list;
    }

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    public void showContent(List<WXItemBean> list) {
        if (swipeRefresh.isRefreshing()){
            swipeRefresh.setRefreshing(false);
        }
        stateMain();
        mList.clear();
        mList.addAll(list);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void initEventAndData() {
        super.initEventAndData();
        mList=new ArrayList<>();
        mAdapter=new WechatAdapter(mContext,mList);
        rvWeChatList.setLayoutManager(new LinearLayoutManager(mContext));
        rvWeChatList.setAdapter(mAdapter);
        stateLoading();
        mPresentter.getWeChatData();

        rvWeChatList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItem=((LinearLayoutManager)rvWeChatList.getLayoutManager()).findLastVisibleItemPosition();
                int totalItemCount = rvWeChatList.getLayoutManager().getItemCount();

                if (lastVisibleItem>=totalItemCount-2 && dy>0){//还剩2个Item时加载更多
                    if (!isLoadingMore){
                        isLoadingMore=true;
                        mPresentter.getMoreWechatData();
                    }
                }
            }
        });

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresentter.getWeChatData();
            }
        });

    }

    @Override
    public void showMoreContent(List<WXItemBean> list) {
        stateMain();
        mList.addAll(list);
        mAdapter.notifyDataSetChanged();
        isLoadingMore=false;
    }


    @Override
    public void stateError() {
        super.stateError();
        if (swipeRefresh.isRefreshing())
                swipeRefresh.setRefreshing(false);

    }
}
