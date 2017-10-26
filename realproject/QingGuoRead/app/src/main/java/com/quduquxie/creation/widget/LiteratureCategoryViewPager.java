package com.quduquxie.creation.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created on 16/11/24.
 * Created by crazylei.
 */

public class LiteratureCategoryViewPager extends ViewPager {

    public LiteratureCategoryViewPager(Context context) {
        super(context);
    }

    public LiteratureCategoryViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int height = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int childMeasuredHeight = child.getMeasuredHeight();
            if (childMeasuredHeight > height)
                height = childMeasuredHeight;
        }

        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}
