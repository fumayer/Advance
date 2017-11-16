/*
 * Copyright (C) 2010-2017 Alibaba Group Holding Limited.
 */

package com.shortmeet.www.zTakePai.editt.effects.filter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.shortmeet.www.R;
import com.shortmeet.www.zTakePai.editt.effects.control.BaseChooser;
import com.shortmeet.www.zTakePai.editt.effects.control.EffectInfo;
import com.shortmeet.www.zTakePai.editt.effects.control.OnItemClickListener;
import com.shortmeet.www.zTakePai.editt.effects.control.SpaceItemDecoration;
import com.shortmeet.www.zTakePai.editt.effects.control.UIEditorPage;
import com.shortmeet.www.zTakePai.editt.util.Common;


public class FilterChooserMediator extends BaseChooser implements OnItemClickListener {

    private RecyclerView mListView;
    private FilterAdapter mFilterAdapter;

    private RelativeLayout mDismissRelative;

    public static FilterChooserMediator newInstance(){
        FilterChooserMediator dialog = new FilterChooserMediator();
        Bundle args = new Bundle();
//        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = LayoutInflater.from(getActivity()).inflate(R.layout.filter_view, container);
        mListView = (RecyclerView) mView.findViewById(R.id.effect_list_filter);
        mDismissRelative = (RelativeLayout) mView.findViewById(R.id.effect_list_dismiss);
        mDismiss = (ImageView) mView.findViewById(R.id.dismiss);
        mDismiss.setOnClickListener(onClickListener);
        if(mEditorService != null && mEditorService.isFullScreen()) {
            mListView.setBackgroundColor(getResources().getColor(R.color.action_bar_bg_50pct));
            mDismissRelative.setBackgroundColor(getResources().getColor(R.color.tab_bg_color_50pct));
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mListView.setLayoutManager(layoutManager);
        mFilterAdapter = new FilterAdapter(getContext());
        mFilterAdapter.setOnItemClickListener(this);
        mFilterAdapter.setDataList(Common.getFilterList());
        mFilterAdapter.setSelectedPos(mEditorService.getEffectIndex(UIEditorPage.FILTER_EFFECT));
        mListView.setAdapter(mFilterAdapter);
        mListView.addItemDecoration(new SpaceItemDecoration(getContext().getResources().getDimensionPixelSize(R.dimen.list_item_space)));
        mListView.scrollToPosition(mEditorService.getEffectIndex(UIEditorPage.FILTER_EFFECT));
        return mView;
    }

    @Override
    public boolean onItemClick(EffectInfo effectInfo, int index) {
        if(mOnEffectChangeListener != null) {
            mEditorService.addTabEffect(UIEditorPage.FILTER_EFFECT, index);
            mOnEffectChangeListener.onEffectChange(effectInfo);
        }
        return true;
    }
}
