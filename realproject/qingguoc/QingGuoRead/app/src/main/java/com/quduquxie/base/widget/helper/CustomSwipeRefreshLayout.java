package com.quduquxie.base.widget.helper;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/**
 * Created on 17/8/4.
 * Created by crazylei.
 */

public class CustomSwipeRefreshLayout extends SwipeRefreshLayout {

    private int touchSlop;

    private float eventY;

    public CustomSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                eventY = event.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                final float y = event.getY();

                float offset = Math.abs(eventY - y);

                if (offset < touchSlop + 200) {
                    return false;
                }
        }
        return super.onInterceptTouchEvent(event);
    }
}