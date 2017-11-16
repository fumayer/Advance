package com.shortmeet.www.ui.Attention.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by zxf on 2017/11/14.
 */

public class AttentionHeadAdapter extends BaseQuickAdapter<String,BaseViewHolder> {

    public AttentionHeadAdapter(@LayoutRes int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {

    }
}
