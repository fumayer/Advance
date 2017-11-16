package com.shortmeet.www.zTakePai.editt.editor.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.shortmeet.www.R;
import com.shortmeet.www.zTakePai.editt.editor.adapter.BaseChooserFragment;
import com.shortmeet.www.zTakePai.editt.effects.control.EffectInfo;
import com.shortmeet.www.zTakePai.editt.effects.control.OnItemClickListener;
import com.shortmeet.www.zTakePai.editt.effects.control.SpaceItemDecoration;
import com.shortmeet.www.zTakePai.editt.effects.control.UIEditorPage;
import com.shortmeet.www.zTakePai.editt.effects.filter.FilterAdapter;
import com.shortmeet.www.zTakePai.editt.util.Common;

/**
 * Created by zxf on 2017/11/3.
 */

public class FilterFragment extends BaseChooserFragment implements OnItemClickListener{
    private RecyclerView mListView;
    private FilterAdapter mFilterAdapter;
    private RelativeLayout mDismissRelative;

    public static FilterFragment newInstance() {
        Bundle args = new Bundle();
        FilterFragment filterfrg = new FilterFragment();
        filterfrg.setArguments(args);
        return filterfrg;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = LayoutInflater.from(getActivity()).inflate(R.layout.filter_view, null);
        mListView = (RecyclerView) mView.findViewById(R.id.effect_list_filter);
        mDismissRelative = (RelativeLayout) mView.findViewById(R.id.effect_list_dismiss);
        mDismissRelative.setVisibility(View.GONE);
      /*  mDismiss = (ImageView) mView.findViewById(R.id.dismiss);
        mDismiss.setOnClickListener(onClickListener);*/
        if(mEditorService != null && mEditorService.isFullScreen()) {
            mListView.setBackgroundColor(getResources().getColor(R.color.aliyun_color_bg));
         //   mDismissRelative.setBackgroundColor(getResources().getColor(R.color.tab_bg_color_50pct));
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
