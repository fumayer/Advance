package com.shortmeet.www.views.refreshPart;

import android.content.Context;
import android.util.AttributeSet;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;

/*
 *  Fly 注：头部刷新控件
 */
public class AutoSwipeRefresh extends SwipeToLoadLayout {

    public AutoSwipeRefresh(Context context) {
        super(context);
    }

    public AutoSwipeRefresh(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoSwipeRefresh(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void refreshComplete() {
        setRefreshing(false);
    }

    public void autoRefresh(long time) {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                setRefreshing(true);
            }
        }, time);

    }

    /**
     * 自动刷新
     */
    public void autoRefresh() {
        setRefreshing(true);
    }
}
