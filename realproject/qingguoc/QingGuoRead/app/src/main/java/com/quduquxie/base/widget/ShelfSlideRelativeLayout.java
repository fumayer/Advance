package com.quduquxie.base.widget;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.support.annotation.Px;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.orhanobut.logger.Logger;
import com.quduquxie.R;

/**
 * Created on 17/8/10.
 * Created by crazylei.
 */

public class ShelfSlideRelativeLayout extends RelativeLayout {

    private int width;
    private int height;

    private int margin;

    private int content;

    private float enlargeScale = 1.04f;
    private float initializeScale = 1.00f;

    private RecyclerView recyclerView;

    public ShelfSlideRelativeLayout(Context context) {
        super(context);
        initializeParameter(context);
    }

    public ShelfSlideRelativeLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initializeParameter(context);
    }

    public ShelfSlideRelativeLayout(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initializeParameter(context);
    }

    private void initializeParameter(Context context) {
        this.recyclerView = (RecyclerView) this.getParent();

        if (context != null) {
            Resources resources = context.getResources();

            if (resources != null) {
                width = resources.getDimensionPixelOffset(R.dimen.width_152);
                height = (int) (resources.getDisplayMetrics().heightPixels * 0.6);
                margin = resources.getDimensionPixelOffset(R.dimen.width_32);
                content = resources.getDimensionPixelOffset(R.dimen.width_184);
            }
        }
    }


    @Override
    protected void onAttachedToWindow() {

        if (recyclerView == null) {
            recyclerView = (RecyclerView) this.getParent();
        }

        final int count = recyclerView.getChildCount();

        if (count == 1) {
            initializeScale(enlargeScale);
        } else {
            initializeScale(initializeScale);
        }

        super.onAttachedToWindow();
    }

    @Override
    public void offsetLeftAndRight(@Px int offset) {

        int left = this.getLeft();

        if (left <= (content + margin) && left > margin) {
            float scale = initializeScale + ((((content + margin - left) * 4.00f) / content) / 100);
            initializeScale(scale);
        } else if (left <= margin && left > -(width)) {
            float scale = enlargeScale - ((((margin - left) * 4.00f) / content) / 100);
            initializeScale(scale);
        } else {
            initializeScale(initializeScale);
        }

        super.offsetLeftAndRight(offset);
    }

    private void initializeScale(float scale) {
        this.setScaleX(scale);
        this.setScaleY(scale);
        this.setTranslationY(height * ((1 - scale) / 2));
    }
}
