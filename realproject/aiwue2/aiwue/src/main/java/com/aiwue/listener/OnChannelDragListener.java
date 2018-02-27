package com.aiwue.listener;

import com.chad.library.adapter.base.BaseViewHolder;

/**
 * Created by Administrator on 2017/1/5 0005.
 */

public interface OnChannelDragListener {
    void onStarDrag(BaseViewHolder baseViewHolder);
    void onItemMove(int starPos, int endPos);
}
