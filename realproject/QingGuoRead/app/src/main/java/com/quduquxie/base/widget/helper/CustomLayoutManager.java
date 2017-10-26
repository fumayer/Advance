package com.quduquxie.base.widget.helper;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.quduquxie.R;

import static android.support.v7.widget.RecyclerView.NO_POSITION;

/**
 * Created on 17/7/30.
 * Created by crazylei.
 */

public class CustomLayoutManager extends LinearLayoutManager {

    private int width;
    private int height;

    private int totalX = 0;

    private int offset32;

    private float defaultScale = 0.94f;
    private float initializeScale = 1.00f;

    public CustomLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);

        width = context.getResources().getDimensionPixelOffset(R.dimen.width_174);
        height = context.getResources().getDimensionPixelOffset(R.dimen.height_638);

        offset32 = context.getResources().getDimensionPixelOffset(R.dimen.width_32);
    }

    @Override
    public void addView(View child) {

        int first = findFirstVisibleItemPosition();

        int position = this.getPosition(child);

        float scale = child.getScaleX();

        if (first != NO_POSITION) {
            if (position == first) {
                if (scale != initializeScale) {
                    initializeScale(child, initializeScale);
                }
            } else {
                if (scale != defaultScale) {
                    initializeScale(child, defaultScale);
                }
            }
        } else {
            if (scale != initializeScale) {
                initializeScale(child, initializeScale);
            }
        }

        super.addView(child);
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {

        totalX += dx;

        if (totalX <= 0) {
            totalX = 0;
        }

        int position = findFirstVisibleItemPosition();

        if (position == this.getItemCount() - 1) {
            totalX = (width + offset32) * (this.getItemCount() - 1);
        }

        scaleFirstView(position);

        if (position + 1 < getItemCount()) {
            scaleSecondView(position);
        }

        return super.scrollHorizontallyBy(dx, recycler, state);
    }

    private void scaleFirstView(int position) {

        View view = this.findViewByPosition(position);

        int distance = totalX - (width * position) - loadViewOffset(position);

        float scale = (((width * 10 * 1.00f) - distance) / (width * 10));

        if (scale <= defaultScale) {
            scale = defaultScale;
        } else if (scale >= initializeScale) {
            scale = initializeScale;
        }

        if (view != null && view.getScaleX() != scale) {
            view.setScaleX(scale);
            view.setScaleY(scale);
            view.setTranslationY(height * ((1 - scale) / 2));
        }
    }

    private void scaleSecondView(int position) {

        View view = this.findViewByPosition(position + 1);

        int distance = totalX - (width * position) - loadViewOffset(position);

        float scale = (((width * 9 * 1.00f) + distance) / (width * 10));

        if (scale >= defaultScale) {

            if (scale >= initializeScale) {
                scale = initializeScale;
            }

            if (view != null && view.getScaleX() != scale) {
                view.setScaleX(scale);
                view.setScaleY(scale);
                view.setTranslationY(height * ((1 - scale) / 2));
            }
        }
    }

    private int loadViewOffset(int position) {
        int offset = offset32;

        offset += offset32 * position;

        return offset;
    }

    private void initializeScale(int position, float scale) {

        View view = this.findViewByPosition(position);

        if (view != null && view.getScaleX() != scale) {
            view.setScaleX(scale);
            view.setScaleY(scale);
            view.setTranslationY(height * ((1 - scale) / 2));
        }
    }

    private void initializeScale(View view, float scale) {
        if (view != null) {
            view.setScaleX(scale);
            view.setScaleY(scale);
            view.setTranslationY(height * ((1 - scale) / 2));
        }
    }
}