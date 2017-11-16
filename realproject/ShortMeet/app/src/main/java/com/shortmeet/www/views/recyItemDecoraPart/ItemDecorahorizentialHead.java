package com.shortmeet.www.views.recyItemDecoraPart;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/*
 *  Fly 注：  横向头像   详情页面 一排评论头像
 */
public class ItemDecorahorizentialHead extends RecyclerView.ItemDecoration {
    private int space;

    public ItemDecorahorizentialHead(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        // Add top margin only for the first item to avoid double space between items
        outRect.right = space;
    }
}
