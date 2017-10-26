package com.quduquxie.base.widget.helper;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.orhanobut.logger.Logger;
import com.quduquxie.R;

/**
 * Created on 17/7/30.
 * Created by crazylei.
 */

public class ShelfSlideLinearLayoutManager extends LinearLayoutManager {

    private int width;
    private float height;

    private int totalX = 0;

    private int margin;

    private float offset;

    private boolean initialize = false;

    public ShelfSlideLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);

        width = context.getResources().getDimensionPixelOffset(R.dimen.width_152);
        height = context.getResources().getDisplayMetrics().heightPixels * 0.6f;

        margin = context.getResources().getDimensionPixelOffset(R.dimen.width_32);

        offset = width + margin;
    }

    @Override
    public void addView(View child) {
        super.addView(child);
    }

    @Override
    public void addView(View child, int index) {
        if (!initialize) {
            child.setScaleX(1.04f);
            child.setScaleY(1.04f);
            child.setTranslationY(-(height * ((0.04f) / 2)));
            initialize = true;
        }

        super.addView(child, index);
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {

        int x = super.scrollHorizontallyBy(dx, recycler, state);

        int position = findFirstVisibleItemPosition();

        totalX += dx;

        if (totalX <= 0) {
            totalX = 0;
        }

        if (position == this.getItemCount() - 1) {
            totalX = (width + margin) * (this.getItemCount() - 1);
        }

        int distance = totalX - ((width + margin) * position);

        float scale = (distance * 4 / offset) / 100 ;

        scaleFirstView(position, scale);

        if (position + 1 < getItemCount()) {
            scaleSecondView(position, scale);
        }

//        Logger.e("TotalX: " + totalX + " : " + x);

        return x;
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
    }

    private void scaleFirstView(int position, float scale) {
        View view = this.findViewByPosition(position);

        float resize = 1.04f - scale;

        if (resize <= 1.00f) {
            resize = 1.00f;
        }

        if (view != null && view.getScaleX() != resize) {
            view.setScaleX(resize);
            view.setScaleY(resize);
            view.setTranslationY(- (height * ((resize - 1) / 2)));
        }
    }

    private void scaleSecondView(int position, float scale) {

        View view = this.findViewByPosition(position + 1);

        float resize = 1.00f + scale;

        if (resize >= 1.04f) {
            resize = 1.04f;
        }

        if (view != null && view.getScaleX() != resize) {
            view.setScaleX(resize);
            view.setScaleY(resize);
            view.setTranslationY(-(height * ((resize - 1) / 2)));
        }
    }

    @Override
    public void onItemsChanged(RecyclerView recyclerView) {
        int position = this.findFirstVisibleItemPosition();

        View view = this.findViewByPosition(position);

        if (view != null) {
            view.setScaleX(1.04f);
            view.setScaleY(1.04f);
            view.setTranslationY(-(height * ((0.04f) / 2)));
        }

        super.onItemsChanged(recyclerView);
    }
}