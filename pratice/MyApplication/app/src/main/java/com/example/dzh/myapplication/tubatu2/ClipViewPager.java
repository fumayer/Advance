package com.example.dzh.myapplication.tubatu2;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by dzh on 2017/10/25.
 */

public class ClipViewPager extends ViewPager {
    public ClipViewPager(Context context) {
        super(context);
    }

    public ClipViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 重写点击事件，当用户抬起的时候 判断用户点击的区域是否在viewPager的区域中
     * 如果是，在判断是在哪个子view上，然后设置当前页为该view
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP){
            View view = viewOfClickOnScreen(ev);
            if (view != null){
                setCurrentItem(indexOfChild(view));
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private View viewOfClickOnScreen(MotionEvent ev) {
        int childCount = getChildCount();
        int[] location = new int[2];
        for (int i = 0; i < childCount; i++) {
            View v = getChildAt(i);
            v.getLocationOnScreen(location);
            int minX = location[0];
            int minY = getTop();

            int maxX = location[0] + v.getWidth();
            int maxY = getBottom();

            float x = ev.getX();
            float y = ev.getY();

            if ((x > minX && x < maxX) && (y > minY && y < maxY)) {
                return v;
            }
        }
        return null;
    }
}