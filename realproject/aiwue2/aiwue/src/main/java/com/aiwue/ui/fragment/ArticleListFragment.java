package com.aiwue.ui.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aiwue.R;
import com.aiwue.base.BaseMvpFragment;
import com.aiwue.iview.IArticleListView;
import com.aiwue.model.Article;
import com.aiwue.presenter.ArticleListPresenter;
import com.aiwue.ui.activity.ArticleDetailActivity;
import com.aiwue.ui.adapter.ArticleListAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *  主页中的文章列表页面
 * Created by Yibao on 2017年4月12日14:36:55
 * Copyright (c) 2017 aiwue.com All rights reserved
 */
public class ArticleListFragment extends BaseMvpFragment<ArticleListPresenter> implements IArticleListView {

    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;
    protected List<Article> mDatas = new ArrayList<>();
    protected BaseQuickAdapter mAdapter;

    private  int pIndex = 1;
    private int pSize = 10;

    @Override
    protected ArticleListPresenter createPresenter() {
        return new ArticleListPresenter(this);
    }

    @Override
    protected View loadViewLayout(LayoutInflater inflater, ViewGroup container) {
        View v = inflater.inflate(R.layout.layout_recyclerview, null);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    protected void bindViews(View view) {
    }

    /**
     * 获取配置好的recyclerview
     */
    @Override
    protected void processLogic() {
        initCommonRecyclerView(createAdapter(), null);
        //mTitleCode = getArguments().getString(ConstantValue.DATA);
        srl.measure(0, 0);
        srl.setRefreshing(true);
    }

    protected BaseQuickAdapter createAdapter() {
        mAdapter = new ArticleListAdapter(mDatas);
        mAdapter.setEnableLoadMore(true);
        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        return mAdapter;
    }


    @Override
    protected void lazyLoad() {
        super.lazyLoad();
        pIndex = 1;
        mvpPresenter.getArticleList(pSize);
    }

    @Override
    protected void setListener() {
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pIndex = 1;
                mvpPresenter.getArticleList(pSize);
            }
        });
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter var1, View var2, int i){

                Article article = (Article)mAdapter.getData().get(i);
                Intent intent = new Intent(mContext, ArticleDetailActivity.class);
                intent.putExtra("articleId",article.getId());
                startActivity(intent);
            }
        });
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override public void onLoadMoreRequested() {
                ++pIndex;
                mvpPresenter.getArticleList(pSize);
            }
        },recyclerView);
    }

    @Override
    public void onGetArticleListSuccess(Boolean success, String err, List<Article> response) {
        srl.setRefreshing(false);
        mAdapter.loadMoreComplete();
        if (success) {
            if (response == null) { //如果返回null，表明到末尾了
                if (pIndex > 1)
                    mAdapter.loadMoreEnd();
                return;
            }
            if (response.size() < pSize) { //如果返回的记录数比期望的小，则表明到末尾了
                mAdapter.loadMoreEnd();
            }
            if (pIndex == 1) {
                mAdapter.getData().clear();
            }

            mAdapter.addData(response);
            mAdapter.notifyDataSetChanged();

        }else {
            mAdapter.loadMoreFail();
        }
    }
}
