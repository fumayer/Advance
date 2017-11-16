package com.shortmeet.www.views.recyItemDecoraPart;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;

/*
 *  Fly 注：
 */
public class NoScrollLinearHorzantialLayoutManager extends LinearLayoutManager {
    private boolean isScrollEnabled;

    public NoScrollLinearHorzantialLayoutManager(Context context) {
        super(context);
    }

    public NoScrollLinearHorzantialLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public NoScrollLinearHorzantialLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
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
