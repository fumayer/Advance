package com.shortmeet.www.ui.Video.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shortmeet.www.entity.video.ShowZanHeadsEntity;

import java.util.List;

/**
 * Created by Fenglingyue on 2017/10/30.
 */

public class VedoDetaiRecyAllHeadAdapter extends BaseQuickAdapter<ShowZanHeadsEntity.DataEntity.ListEntity,BaseViewHolder> {

    public VedoDetaiRecyAllHeadAdapter(@LayoutRes int layoutResId, @Nullable List<ShowZanHeadsEntity.DataEntity.ListEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ShowZanHeadsEntity.DataEntity.ListEntity item) {

    }
}
