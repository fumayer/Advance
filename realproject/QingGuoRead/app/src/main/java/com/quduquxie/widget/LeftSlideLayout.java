package com.quduquxie.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Scroller;

import com.quduquxie.R;

import java.util.LinkedList;
import java.util.List;

/**
 * Created on 17/1/11.
 * Created by crazylei.
 */

public class LeftSlideLayout extends LinearLayout {
    private View content_view;
    private int touch_threshold;
    private int downX;
    private int downY;
    private Scroller scroller;
    private int view_width;
    private boolean is_sliding;
    private boolean is_finish;
    private Drawable drawable;
    private Activity activity;

    private int currentX;

    private List<ViewPager> viewPagers = new LinkedList<>();

    private boolean white_background = false;

    private LeftSlideListener leftSlideListener;

    public LeftSlideLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LeftSlideLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        scroller = new Scroller(context);
        drawable = getResources().getDrawable(R.drawable.icon_shadow_left);
        touch_threshold = ViewConfiguration.get(context).getScaledTouchSlop();
    }


    public void attachToActivity(Activity activity) {
        this.activity = activity;
        TypedArray typedArray = activity.getTheme().obtainStyledAttributes(new int[]{android.R.attr.windowBackground});
        int background = typedArray.getResourceId(0, 0);
        typedArray.recycle();

        ViewGroup decor_view = (ViewGroup) activity.getWindow().getDecorView();
        ViewGroup decor_child = (ViewGroup) decor_view.getChildAt(0);
        decor_child.setBackgroundResource(background);
        decor_view.removeView(decor_child);
        addView(decor_child);
        setContentView(decor_child);
        decor_view.addView(this);
    }

    private void setContentView(View decorChild) {
        content_view = (View) decorChild.getParent();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        ViewPager viewPager = getTouchViewPager(viewPagers, motionEvent);

        if (viewPager != null && viewPager.getCurrentItem() != 0) {
            return super.onInterceptTouchEvent(motionEvent);
        }

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = currentX = (int) motionEvent.getRawX();
                downY = (int) motionEvent.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) motionEvent.getRawX();
                if (moveX < downX && moveX - downX < -touch_threshold && Math.abs((int) motionEvent.getRawY() - downY) < touch_threshold) {
                    return true;
                }
                break;
        }

        return super.onInterceptTouchEvent(motionEvent);
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) motionEvent.getRawX();
                int deltaX = moveX - currentX;
                currentX = moveX;
                if (moveX < downX && moveX - downX < -touch_threshold && Math.abs((int) motionEvent.getRawY() - downY) < touch_threshold) {
                    is_sliding = true;
                }
                if (moveX - downX < 0 && is_sliding) {
                    if (leftSlideListener != null && !white_background) {
                        white_background = true;
                        leftSlideListener.closeSlide();
                    }
                    content_view.scrollBy(-deltaX, 0);
                }
                break;
            case MotionEvent.ACTION_UP:
                is_sliding = false;
                white_background = false;
                if (content_view.getScrollX() * 2 >= 100) {
                    is_finish = true;
                    scrollRight();
                } else {
                    if (leftSlideListener != null) {
                        leftSlideListener.restoreBackground();
                    }
                    scrollOrigin();
                    is_finish = false;
                }
                break;
        }

        return true;
    }

    private void getAllViewPager(List<ViewPager> viewPagers, ViewGroup parent) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            if (child instanceof ViewPager) {
                viewPagers.add((ViewPager) child);
            } else if (child instanceof ViewGroup) {
                getAllViewPager(viewPagers, (ViewGroup) child);
            }
        }
    }

    private ViewPager getTouchViewPager(List<ViewPager> viewPagers, MotionEvent motionEvent) {
        if (viewPagers == null || viewPagers.size() == 0) {
            return null;
        }
        Rect rect = new Rect();
        for (ViewPager viewPager : viewPagers) {
            viewPager.getHitRect(rect);

            if (rect.contains((int) motionEvent.getX(), (int) motionEvent.getY())) {
                return viewPager;
            }
        }
        return null;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            view_width = this.getWidth();

            getAllViewPager(viewPagers, this);
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (drawable != null && content_view != null) {
            int left = content_view.getLeft() - drawable.getIntrinsicWidth();
            int right = left + drawable.getIntrinsicWidth();
            int top = content_view.getTop();
            int bottom = content_view.getBottom();
            drawable.setBounds(left, top, right, bottom);
            drawable.draw(canvas);
        }

    }

    private void scrollRight() {
        final int delta = (view_width + content_view.getScrollX());
        scroller.startScroll(content_view.getScrollX(), 0, delta, 0, 300);
        postInvalidate();
    }

    private void scrollOrigin() {
        int delta = content_view.getScrollX();
        scroller.startScroll(content_view.getScrollX(), 0, -delta, 0, Math.abs(delta));
        postInvalidate();
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            content_view.scrollTo(scroller.getCurrX(), scroller.getCurrY());
            postInvalidate();

            if (scroller.isFinished() && is_finish) {
                activity.finish();
            }
        }
    }

    public void setOnLeftSlideLayoutListener(LeftSlideListener leftSlideListener) {
        this.leftSlideListener = leftSlideListener;
    }

    public interface LeftSlideListener {
        void closeSlide();

        void restoreBackground();
    }
}
