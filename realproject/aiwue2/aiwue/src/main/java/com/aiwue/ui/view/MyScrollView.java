package com.aiwue.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.OverScroller;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

/**
 * Created by Administrator on 2016/11/21 0021.
 */
public class MyScrollView extends LinearLayout {

    private float mLastY;
    private LinearLayout mRootView;
    private OverScroller mScroller;
    private int mHeight;

    public MyScrollView(Context context) {
        this(context, null);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mRootView = new LinearLayout(context);
        setOrientation(VERTICAL);
        mScroller = new OverScroller(context);
        mRootView.setOrientation(VERTICAL);
        for (int i = 0; i < 50; i++) {
            TextView view = new TextView(context);
            view.setText("你好" + i);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            mRootView.addView(view);
        }

        addView(mRootView);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        mHeight =getMeasuredHeight();
//        int rootViewHeight =mRootView.getMeasuredHeight();
        int w = MeasureSpec.makeMeasureSpec(mRootView.getMeasuredHeight(),
                View.MeasureSpec.EXACTLY);
        int h = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
//        //重新测量
        mRootView.measure(w, h);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished())
                    mScroller.abortAnimation();
                mLastY = y;
                return true;
            case MotionEvent.ACTION_MOVE:
                float dy = mLastY- mLastY;
                Logger.i("-dy:" + dy);
//                mRootView.scrollBy(0, (int) -dy);
//                scrollBy(0, (int) -dy);
//                smoothScrollBy(0, (int) -dy);
                // Calling overScrollBy will call onOverScrolled, which
                // calls onScrollChanged if applicable.

                mLastY = y;
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

//    @Override
//    protected void measureChild(View child, int parentWidthMeasureSpec,
//                                int parentHeightMeasureSpec) {
//        ViewGroup.LayoutParams lp = child.getLayoutParams();
//
//        int childWidthMeasureSpec;
//        int childHeightMeasureSpec;
//
//        childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec, getPaddingLeft()
//                + getPaddingRight(), lp.width);
//
//        childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
//                MeasureSpec.getSize(parentHeightMeasureSpec), MeasureSpec.UNSPECIFIED);
//
//        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
//    }

    //调用此方法滚动到目标位置
    public void smoothScrollTo(int fx, int fy) {
        int dx = fx - mScroller.getFinalX();
        int dy = fy - mScroller.getFinalY();
        smoothScrollBy(dx, dy);
    }

    //调用此方法设置滚动的相对偏移
    public void smoothScrollBy(int dx, int dy) {
        //设置mScroller的滚动偏移量
        mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(), dx, dy);
        invalidate();//这里必须调用invalidate()才能保证computeScroll()会被调用，否则不一定会刷新界面，看不到滚动效果
    }

//    @Override
//    public void computeScroll() {
//        //先判断mScroller滚动是否完成
//        if (mScroller.computeScrollOffset()) {
//
//            //这里调用View的scrollTo()完成实际的滚动
//            mRootView.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
//            //必须调用该方法，否则不一定能看到滚动效果
//            postInvalidate();
//        }
//        super.computeScroll();
//    }

}
