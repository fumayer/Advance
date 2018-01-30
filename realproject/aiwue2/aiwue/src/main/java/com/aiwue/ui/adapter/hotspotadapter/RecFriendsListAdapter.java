package com.aiwue.ui.adapter.hotspotadapter;

import android.widget.ImageView;

import com.aiwue.R;
import com.aiwue.base.AiwueConfig;
import com.aiwue.model.RecommendFriends;
import com.aiwue.theme.colorUi.util.ColorUiUtil;
import com.aiwue.utils.ImageLoaderUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 获取推荐武友的adapter
 * Created by Administrator on 2017/5/2.
 */

public class RecFriendsListAdapter extends BaseQuickAdapter<RecommendFriends,BaseViewHolder>{
    public RecFriendsListAdapter(List<RecommendFriends> data) {
        super(R.layout.item_recommend_friends, data);
    }



    @Override
    protected void convert(BaseViewHolder baseViewHolder, RecommendFriends recommendFriends) {
        ColorUiUtil.changeTheme(baseViewHolder.convertView, mContext.getTheme());
        ImageLoaderUtils.displayImage(AiwueConfig.AIWUE_API_PIC_URL + recommendFriends.getHeadPicName(), (ImageView) baseViewHolder.getView(R.id.imgv_recommend_friends));
        baseViewHolder.setText(R.id.tv_recommend_friends, recommendFriends.getNickName());
    }
}
