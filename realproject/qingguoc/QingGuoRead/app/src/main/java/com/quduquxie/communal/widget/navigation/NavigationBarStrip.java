package com.quduquxie.communal.widget.navigation;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Scroller;

import com.quduquxie.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NavigationBarStrip extends View implements ViewPager.OnPageChangeListener {

    private final static int HIGH_QUALITY_FLAGS = Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG;

    private final RectF bounds = new RectF();
    private final RectF stripBounds = new RectF();
    private final Rect titleBounds = new Rect();

    private final ValueAnimator valueAnimator = new ValueAnimator();
    private final ArgbEvaluator argbEvaluator = new ArgbEvaluator();

    private final ResizeInterpolator resizeInterpolator = new ResizeInterpolator();

    private List<String> titles = new ArrayList<>();

    private ViewPager navigationViewPager;
    private ViewPager.OnPageChangeListener onPageChangeListener;
    private int scrollState;

    private float mTabSize;

    private float mFraction;

    private float mStartStripX;
    private float mEndStripX;
    private float mStripLeft;
    private float mStripRight;

    private boolean mIsViewPagerMode;
    private boolean mIsResizeIn;
    private boolean mIsActionDown;
    private boolean mIsTabActionDown;
    private boolean mIsSetIndexFromTabBar;

    private int indicatorColor;

    private float indicatorTextSize;
    private float indicatorWeight;

    private float indicatorRadius;

    private float indicatorPadding;

    private int indicatorAnimationDuration;

    private int indicatorActiveColor;
    private int indicatorInactiveColor;


    private StripType stripType;
    private StripGravity stripGravity;

    private final static int INVALID_INDEX = -1;

    private final static float MIN_FRACTION = 0.0F;
    private final static float MAX_FRACTION = 1.0F;

    private int mLastIndex = INVALID_INDEX;
    private int mIndex = INVALID_INDEX;

    private final Paint stripPaint = new Paint(HIGH_QUALITY_FLAGS) {
        {
            setStyle(Style.FILL);
        }
    };

    private final Paint titlePaint = new TextPaint(HIGH_QUALITY_FLAGS) {
        {
            setTextAlign(Align.CENTER);
        }
    };


    public NavigationBarStrip(final Context context) {
        this(context, null);
    }

    public NavigationBarStrip(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NavigationBarStrip(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setWillNotDraw(false);

        ViewCompat.setLayerType(this, ViewCompat.LAYER_TYPE_SOFTWARE, null);

        setLayerType(LAYER_TYPE_SOFTWARE, null);

        Resources resources = context.getResources();

        int indicatorType;
        int indicatorGravity;

        float indicatorFactor;

        if (attrs != null) {
            final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NavigationBarStrip);

            int indicator_color = typedArray.getResourceId(R.styleable.NavigationBarStrip_indicatorColor, R.color.theme_primary);
            indicatorColor = resources.getColor(indicator_color);

            int indicator_text_size = typedArray.getResourceId(R.styleable.NavigationBarStrip_indicatorTextSize, R.dimen.text_size_30);
            indicatorTextSize = resources.getDimension(indicator_text_size);

            int indicator_weight = typedArray.getResourceId(R.styleable.NavigationBarStrip_indicatorWidth, R.dimen.height_4);
            indicatorWeight = resources.getDimension(indicator_weight);

            int indicator_padding = typedArray.getResourceId(R.styleable.NavigationBarStrip_indicatorPadding, R.dimen.width_46);
            indicatorPadding = resources.getDimension(indicator_padding);

            indicatorFactor = typedArray.getFloat(R.styleable.NavigationBarStrip_indicatorFactor, 2.5F);

            indicatorType = typedArray.getInt(R.styleable.NavigationBarStrip_indicatorType, StripType.LINE_INDEX);

            indicatorGravity = typedArray.getInt(R.styleable.NavigationBarStrip_indicatorGravity, StripGravity.BOTTOM_INDEX);

            int indicator_radius = typedArray.getResourceId(R.styleable.NavigationBarStrip_indicatorRadius, R.dimen.radius_2);
            indicatorRadius = resources.getDimension(indicator_radius);

            indicatorAnimationDuration = typedArray.getInteger(R.styleable.NavigationBarStrip_indicatorAnimationDuration, 350);

            int indicator_active_color = typedArray.getResourceId(R.styleable.NavigationBarStrip_indicatorActiveColor, R.color.color_black_191919);
            indicatorActiveColor = resources.getColor(indicator_active_color);

            int indicator_inactive_color = typedArray.getResourceId(R.styleable.NavigationBarStrip_indicatorInactiveColor, R.color.color_gray_9b9b9b);
            indicatorInactiveColor = resources.getColor(indicator_inactive_color);

            typedArray.recycle();
        } else {
            indicatorColor = resources.getColor(R.color.theme_primary);
            indicatorTextSize = resources.getDimension(R.dimen.text_size_30);
            indicatorWeight = resources.getDimension(R.dimen.height_4);
            indicatorPadding = resources.getDimension(R.dimen.width_46);

            indicatorFactor = 2.5F;

            indicatorType = StripType.LINE_INDEX;
            indicatorGravity = StripGravity.BOTTOM_INDEX;

            indicatorRadius = resources.getDimension(R.dimen.radius_2);

            indicatorAnimationDuration = 350;

            indicatorActiveColor = resources.getColor(R.color.color_black_191919);
            indicatorInactiveColor = resources.getColor(R.color.color_gray_9b9b9b);
        }
        setStripColor(indicatorColor);
        setTitleSize(indicatorTextSize);
        setStripWeight(indicatorWeight);
        setStripFactor(indicatorFactor);
        setStripType(indicatorType);
        setStripGravity(indicatorGravity);

        setCornersRadius(indicatorRadius);
        setAnimationDuration(indicatorAnimationDuration);

        setActiveColor(indicatorActiveColor);
        setInactiveColor(indicatorInactiveColor);

        valueAnimator.setFloatValues(MIN_FRACTION, MAX_FRACTION);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(final ValueAnimator animation) {
                updateIndicatorPosition((Float) animation.getAnimatedValue());
            }
        });
    }


    public void addTitle(String title) {
        if (!TextUtils.isEmpty(title)) {
            titles.add(title);
            requestLayout();
        }
    }

    public void setStripColor(final int color) {
        stripPaint.setColor(color);
        postInvalidate();
    }

    public void setStripWeight(final float stripWeight) {
        indicatorWeight = stripWeight;
        requestLayout();
    }

    private void setStripGravity(final int index) {
        switch (index) {
            case StripGravity.TOP_INDEX:
                setStripGravity(StripGravity.TOP);
                break;
            case StripGravity.BOTTOM_INDEX:
            default:
                setStripGravity(StripGravity.BOTTOM);
                break;
        }
    }

    public void setStripGravity(final StripGravity stripGravity) {
        this.stripGravity = stripGravity;
        requestLayout();
    }

    private void setStripType(final int index) {
        switch (index) {
            case StripType.POINT_INDEX:
                setStripType(StripType.POINT);
                break;
            case StripType.LINE_INDEX:
            default:
                setStripType(StripType.LINE);
                break;
        }
    }

    public void setStripType(final StripType stripType) {
        this.stripType = stripType;
        requestLayout();
    }

    public void setStripFactor(final float factor) {
        resizeInterpolator.setFactor(factor);
    }

    public void setActiveColor(final int activeColor) {
        indicatorActiveColor = activeColor;
        postInvalidate();
    }

    public void setInactiveColor(final int inactiveColor) {
        indicatorInactiveColor = inactiveColor;
        postInvalidate();
    }

    public void setCornersRadius(final float cornersRadius) {
        indicatorRadius = cornersRadius;
        postInvalidate();
    }

    public void setAnimationDuration(final int animationDuration) {
        this.indicatorAnimationDuration = animationDuration;
        valueAnimator.setDuration(animationDuration);
        resetScroller();
    }

    public void setTitleSize(final float titleSize) {
        titlePaint.setTextSize(titleSize);
        postInvalidate();
    }

    public void setViewPager(final ViewPager viewPager) {
        if (viewPager == null) {
            mIsViewPagerMode = false;
            return;
        }

        if (viewPager.equals(navigationViewPager)) {
            return;
        }
        if (navigationViewPager != null) {
            navigationViewPager.setOnPageChangeListener(null);
        }
        if (viewPager.getAdapter() == null)
            throw new IllegalStateException("ViewPager does not provide adapter instance.");

        mIsViewPagerMode = true;
        navigationViewPager = viewPager;
        navigationViewPager.addOnPageChangeListener(this);

        resetScroller();
        postInvalidate();
    }

    private void resetScroller() {
        if (navigationViewPager == null) {
            return;
        }
        try {
            final Field scrollerField = ViewPager.class.getDeclaredField("mScroller");
            scrollerField.setAccessible(true);
            final ResizeViewPagerScroller scroller = new ResizeViewPagerScroller(getContext());
            scrollerField.set(navigationViewPager, scroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setOnPageChangeListener(final ViewPager.OnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
    }

    public void setTabIndex(int index) {
        setTabIndex(index, false);
    }

    public void setTabIndex(int tabIndex, boolean isForce) {
        if (valueAnimator.isRunning()) {
            return;
        }
        if (titles.size() == 0) {
            return;
        }

        int index = tabIndex;
        boolean force = isForce;

        if (mIndex == INVALID_INDEX) {
            force = true;
        }

        if (index == mIndex) {
            return;
        }

        index = Math.max(0, Math.min(index, titles.size() - 1));

        mIsResizeIn = index < mIndex;
        mLastIndex = mIndex;
        mIndex = index;

        mIsSetIndexFromTabBar = true;
        if (mIsViewPagerMode) {
            if (navigationViewPager == null) {
                throw new IllegalStateException("ViewPager is null.");
            }
            navigationViewPager.setCurrentItem(index, !force);
        }

        mStartStripX = mStripLeft;
        mEndStripX = (mIndex * mTabSize) + (stripType == StripType.POINT ? mTabSize * 0.5F : 0.0F);

        if (force) {
            updateIndicatorPosition(MAX_FRACTION);
            if (mIsViewPagerMode) {
                if (!navigationViewPager.isFakeDragging()) {
                    navigationViewPager.beginFakeDrag();
                }
                if (navigationViewPager.isFakeDragging()) {
                    navigationViewPager.fakeDragBy(0.0F);
                    navigationViewPager.endFakeDrag();
                }
            }
        } else {
            valueAnimator.start();
        }
    }

    public void deselect() {
        mLastIndex = INVALID_INDEX;
        mIndex = INVALID_INDEX;
        mStartStripX = INVALID_INDEX * mTabSize;
        mEndStripX = mStartStripX;
        updateIndicatorPosition(MIN_FRACTION);
    }

    private void updateIndicatorPosition(final float fraction) {
        mFraction = fraction;

        mStripLeft = mStartStripX + (resizeInterpolator.getResizeInterpolation(fraction, mIsResizeIn) * (mEndStripX - mStartStripX));

        mStripRight = (mStartStripX + (stripType == StripType.LINE ? mTabSize : indicatorWeight)) + (resizeInterpolator.getResizeInterpolation(fraction, !mIsResizeIn) * (mEndStripX - mStartStripX));

        postInvalidate();
    }

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        if (valueAnimator.isRunning()) {
            return true;
        }
        if (scrollState != ViewPager.SCROLL_STATE_IDLE) {
            return true;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mIsActionDown = true;
                if (!mIsViewPagerMode) {
                    break;
                }
                mIsTabActionDown = (int) (event.getX() / mTabSize) == mIndex;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mIsTabActionDown) {
                    navigationViewPager.setCurrentItem((int) (event.getX() / mTabSize), true);
                    break;
                }
                if (mIsActionDown) break;
            case MotionEvent.ACTION_UP:
                if (mIsActionDown) setTabIndex((int) (event.getX() / mTabSize));
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_OUTSIDE:
            default:
                mIsTabActionDown = false;
                mIsActionDown = false;
                break;
        }

        return true;
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        final float width = MeasureSpec.getSize(widthMeasureSpec);
        final float height = MeasureSpec.getSize(heightMeasureSpec);

        bounds.set(0.0F, 0.0F, width, height);

        if (titles.size() == 0 || width == 0 || height == 0) return;

        mTabSize = width / (float) titles.size();

        if (isInEditMode() || !mIsViewPagerMode) {
            mIsSetIndexFromTabBar = true;

            if (isInEditMode()) mIndex = new Random().nextInt(titles.size());

            mStartStripX = (mIndex * mTabSize) + (stripType == StripType.POINT ? mTabSize * 0.5F : 0.0F);
            mEndStripX = mStartStripX;
            updateIndicatorPosition(MAX_FRACTION);
        }
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        stripBounds.set(mStripLeft - (stripType == StripType.POINT ? indicatorWeight * 0.5F : 0.0F) + indicatorPadding, stripGravity == StripGravity.BOTTOM ? bounds.height() - indicatorWeight : 0.0F, mStripRight - (stripType == StripType.POINT ? indicatorWeight * 0.5F : 0.0F) - indicatorPadding, stripGravity == StripGravity.BOTTOM ? bounds.height() : indicatorWeight);

        if (indicatorRadius == 0) {
            canvas.drawRect(stripBounds, stripPaint);
        } else {
            canvas.drawRoundRect(stripBounds, indicatorRadius, indicatorRadius, stripPaint);
        }

        for (int i = 0; i < titles.size(); i++) {
            final String title = titles.get(i);

            final float leftTitleOffset = (mTabSize * i) + (mTabSize * 0.5F);

            titlePaint.getTextBounds(title, 0, title.length(), titleBounds);
            final float topTitleOffset = (bounds.height() - indicatorWeight) * 0.5F + titleBounds.height() * 0.5F - titleBounds.bottom;

            final float interpolation = resizeInterpolator.getResizeInterpolation(mFraction, true);
            final float lastInterpolation = resizeInterpolator.getResizeInterpolation(mFraction, false);

            if (mIsSetIndexFromTabBar) {
                if (mIndex == i) {
                    updateCurrentTitle(interpolation);
                } else if (mLastIndex == i) {
                    updateLastTitle(lastInterpolation);
                } else {
                    updateInactiveTitle();
                }
            } else {
                if (i != mIndex && i != mIndex + 1) {
                    updateInactiveTitle();
                } else if (i == mIndex + 1) {
                    updateCurrentTitle(interpolation);
                } else if (i == mIndex) {
                    updateLastTitle(lastInterpolation);
                }
            }

            canvas.drawText(title, leftTitleOffset, topTitleOffset + (stripGravity == StripGravity.TOP ? indicatorWeight : 0.0F), titlePaint);
        }
    }

    private void updateCurrentTitle(final float interpolation) {
        titlePaint.setColor((int) argbEvaluator.evaluate(interpolation, indicatorInactiveColor, indicatorActiveColor));
    }

    private void updateLastTitle(final float lastInterpolation) {
        titlePaint.setColor((int) argbEvaluator.evaluate(lastInterpolation, indicatorActiveColor, indicatorInactiveColor));
    }

    private void updateInactiveTitle() {
        titlePaint.setColor(indicatorInactiveColor);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, final int positionOffsetPixels) {
        if (onPageChangeListener != null) {
            onPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }

        if (!mIsSetIndexFromTabBar) {
            mIsResizeIn = position < mIndex;
            mLastIndex = mIndex;
            mIndex = position;

            mStartStripX = (position * mTabSize) + (stripType == StripType.POINT ? mTabSize * 0.5F : 0.0F);
            mEndStripX = mStartStripX + mTabSize;
            updateIndicatorPosition(positionOffset);
        }

        if (!valueAnimator.isRunning() && mIsSetIndexFromTabBar) {
            mFraction = MIN_FRACTION;
            mIsSetIndexFromTabBar = false;
        }
    }

    @Override
    public void onPageSelected(final int position) {

    }

    @Override
    public void onPageScrollStateChanged(final int state) {
        scrollState = state;
        if (state == ViewPager.SCROLL_STATE_IDLE) {
            if (onPageChangeListener != null) {
                onPageChangeListener.onPageSelected(mIndex);
            }
        }

        if (onPageChangeListener != null) {
            onPageChangeListener.onPageScrollStateChanged(state);
        }
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        final SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        mIndex = savedState.index;
        requestLayout();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();
        final SavedState savedState = new SavedState(superState);
        savedState.index = mIndex;
        return savedState;
    }

    private static class SavedState extends BaseSavedState {

        private int index;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            index = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(index);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    @Override
    protected void onConfigurationChanged(final Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        requestLayout();
        final int tempIndex = mIndex;
        deselect();
        post(new Runnable() {
            @Override
            public void run() {
                setTabIndex(tempIndex, true);
            }
        });
    }

    private class ResizeViewPagerScroller extends Scroller {

        ResizeViewPagerScroller(Context context) {
            super(context, new AccelerateDecelerateInterpolator());
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, indicatorAnimationDuration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            super.startScroll(startX, startY, dx, dy, indicatorAnimationDuration);
        }
    }

    private static class ResizeInterpolator implements Interpolator {
        private float mFactor;
        private boolean mResizeIn;

        void setFactor(final float factor) {
            mFactor = factor;
        }

        @Override
        public float getInterpolation(final float input) {
            if (mResizeIn) return (float) (1.0F - Math.pow((1.0F - input), 2.0F * mFactor));
            else return (float) (Math.pow(input, 2.0F * mFactor));
        }

        float getResizeInterpolation(final float input, final boolean resizeIn) {
            mResizeIn = resizeIn;
            return getInterpolation(input);
        }
    }

    private enum StripType {
        LINE, POINT;

        private final static int LINE_INDEX = 0;
        private final static int POINT_INDEX = 1;
    }

    private enum StripGravity {
        BOTTOM, TOP;

        private final static int BOTTOM_INDEX = 0;
        private final static int TOP_INDEX = 1;
    }

    interface OnTabStripSelectedIndexListener {
        void onStartTabSelected(final String title, final int index);

        void onEndTabSelected(final String title, final int index);
    }
}
