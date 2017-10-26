package com.example.dzh.myapplication.tubatu;

import android.os.Build;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by dzh on 2017/10/25.
 */

public class ScalePageTransformer implements ViewPager.PageTransformer {
    public static final float MAX_SCALE = 1.2f;
    public static final float MIN_SCALE = 0.6f;

    /**
     * 当处于最中间的view往左边滑动时，它的position值是小于0的，并且是越来越小,它右边的view的position是从1逐渐减小到0的。
     *
     * @param page
     * @param position
     */
    @Override
    public void transformPage(View page, float position) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            page.getParent().requestLayout();
        }
        if (position < -1) {
            position = -1;
        } else if (position > 1) {
            position = 1;
        }

        float tempScale = position < 0 ? 1 + position : 1 - position;

        float slope = (MAX_SCALE - MIN_SCALE) / 1;
        float scaleValue = MIN_SCALE + tempScale * slope;
        page.setScaleX(scaleValue);
        page.setScaleY(scaleValue);
    }
}
