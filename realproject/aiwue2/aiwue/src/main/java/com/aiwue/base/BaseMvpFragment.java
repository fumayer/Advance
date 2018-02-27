package com.aiwue.base;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;

import com.aiwue.R;

/**
 *  MvpFragment 基类
 * Created by Yibao on 17/4/11
 * Copyright (c) 2017 aiwue.com All rights reserved
 */
public abstract class BaseMvpFragment<P extends BasePresenter> extends BaseFragment {
    protected P mvpPresenter;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        if (mvpPresenter == null) mvpPresenter = createPresenter();
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    protected void lazyLoad() {
        if (mvpPresenter == null) mvpPresenter = createPresenter();
        super.lazyLoad();
    }
    protected abstract P createPresenter();


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mvpPresenter != null) {
            mvpPresenter.detachView();
        }
    }

//    protected UserInfo user;

    public RecyclerView initCommonRecyclerView(BaseQuickAdapter adapter, RecyclerView.ItemDecoration decoration) {
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (decoration != null) {
            recyclerView.addItemDecoration(decoration);
        }
        recyclerView.setAdapter(adapter);
        return recyclerView;
    }

    public RecyclerView initHorizontalRecyclerView(BaseQuickAdapter adapter, RecyclerView.ItemDecoration decoration) {
        return initHorizontalRecyclerView(null, adapter, decoration);
    }

    public RecyclerView initHorizontalRecyclerView(RecyclerView recyclerView, BaseQuickAdapter adapter, RecyclerView.ItemDecoration decoration) {
        return initHorizontalRecyclerView(recyclerView, adapter, decoration, false);
    }

    public RecyclerView initHorizontalRecyclerView(RecyclerView recyclerView, BaseQuickAdapter adapter, RecyclerView.ItemDecoration decoration, boolean reverseLayout) {
        if (recyclerView == null)
            recyclerView = (RecyclerView) rootView.findViewById(R.id.horizontal_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, reverseLayout));
        if (decoration != null) {
            recyclerView.addItemDecoration(decoration);
        }
        recyclerView.setAdapter(adapter);
        return recyclerView;
    }

    public RecyclerView initGridRecyclerView(BaseQuickAdapter adapter, RecyclerView.ItemDecoration decoration, int spanCount) {
        return initGridRecyclerView((RecyclerView) rootView.findViewById(R.id.recyclerView), adapter, decoration, spanCount);
    }

    public RecyclerView initGridRecyclerView(RecyclerView recyclerView, BaseQuickAdapter adapter, RecyclerView.ItemDecoration decoration, int spanCount) {
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), spanCount));
        if (decoration != null) {
            recyclerView.addItemDecoration(decoration);
        }
        recyclerView.setAdapter(adapter);
        return recyclerView;
    }

//    @Override
//    public void onViewCreated(View view, Bundle savedInstanceState) {
//        user = AiwueApplication.getInstance().getUserInfo();
//        super.onViewCreated(view, savedInstanceState);
//    }
//
//    @Override
//    public void onResume() {
//        user = AiwueApplication.getInstance().getUserInfo();
//        super.onResume();
//    }
//    public boolean checkLogin() {
//        if (user == null) {
//            intent2Activity(LoginActivity.class);
//            return false;
//        }
//        return true;
//    }

}
