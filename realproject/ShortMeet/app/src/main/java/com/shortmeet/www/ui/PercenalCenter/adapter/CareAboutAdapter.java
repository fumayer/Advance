package com.shortmeet.www.ui.PercenalCenter.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Fenglingyue on 2017/10/30.
 */

public class CareAboutAdapter extends BaseQuickAdapter<String,BaseViewHolder> {
    public CareAboutAdapter(@LayoutRes int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {

    }
}
