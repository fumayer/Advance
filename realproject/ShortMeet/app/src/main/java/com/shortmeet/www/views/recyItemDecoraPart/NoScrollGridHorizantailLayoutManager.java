package com.shortmeet.www.views.recyItemDecoraPart;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.util.AttributeSet;

/*
 *  Fly 注：
 */
public class NoScrollGridHorizantailLayoutManager extends GridLayoutManager {

    private boolean isScrollEnabled;

    public NoScrollGridHorizantailLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public NoScrollGridHorizantailLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public NoScrollGridHorizantailLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }

    @Override
    public boolean canScrollHorizontally() {
        //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
        return isScrollEnabled && super.canScrollHorizontally();
    }
}
