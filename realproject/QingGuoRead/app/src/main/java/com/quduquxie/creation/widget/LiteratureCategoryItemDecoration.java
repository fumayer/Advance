package com.quduquxie.creation.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created on 16/11/24.
 * Created by crazylei.
 */

public class LiteratureCategoryItemDecoration extends RecyclerView.ItemDecoration {

    private float scaledDensity;
    private int line_max;

    public LiteratureCategoryItemDecoration(float scaledDensity, int size) {
        this.scaledDensity = scaledDensity;
        this.line_max = size / 4;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        int top = 0;
        int bottom = 0;

        if (parent.getChildAdapterPosition(view) / 4  == 0) {
            top = (int) (11 * scaledDensity);
            bottom = (int) (6.5 * scaledDensity);
        } else if (parent.getChildAdapterPosition(view) / 4 == line_max) {
            top = (int) (6.5 * scaledDensity);
            bottom = (int) (11 * scaledDensity);
        } else {
            top = (int) (6.5 * scaledDensity);
            bottom = (int) (6.5 * scaledDensity);
        }

        if (parent.getChildAdapterPosition(view) % 4 == 0) {
//            outRect.left = (int) (16 * scaledDensity);
//            outRect.right = (int) (8 * scaledDensity);
            outRect.top = top;
            outRect.bottom = bottom;
        } else if (parent.getChildAdapterPosition(view) % 4 == 3) {
//            outRect.left = (int) (8 * scaledDensity);
//            outRect.right = (int) (16 * scaledDensity);
            outRect.top = top;
            outRect.bottom = bottom;
        } else {
//            outRect.left = (int) (8 * scaledDensity);
//            outRect.right = (int) (8 * scaledDensity);
            outRect.top = top;
            outRect.bottom = bottom;
        }
    }
}
