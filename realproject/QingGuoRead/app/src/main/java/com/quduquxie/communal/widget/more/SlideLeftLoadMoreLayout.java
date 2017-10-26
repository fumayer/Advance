package com.quduquxie.communal.widget.more;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.quduquxie.R;

import java.lang.ref.WeakReference;

public class SlideLeftLoadMoreLayout extends CoordinatorLayout {

    private WeakReference<Context> contextReference;

    private int promptViewWidth;

    private int promptViewHeight;

    private int promptTextSize;

    private int promptTextColor;

    private int promptBackground;

    private int promptTextMargin;

    private LoadMorePromptView loadMorePromptView;

    private String[] prompts;

    private ValueAnimator valueAnimator;

    private int translation;

    private TextView textView;

    private View promptView;
    private int viewWidth;
    private boolean skipState = false;
    private boolean animatorState = false;

    private LoadingMoreListener loadingMoreListener;

    public SlideLeftLoadMoreLayout(Context context) {
        this(context, null);
    }

    public SlideLeftLoadMoreLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideLeftLoadMoreLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        contextReference = new WeakReference<>(context);
        Resources resources = context.getResources();

        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SlideLeftLoadMoreLayout);

            int prompt_view_width = typedArray.getResourceId(R.styleable.SlideLeftLoadMoreLayout_slideLeftLoadMoreLayoutPromptViewWidth, R.dimen.width_160);
            promptViewWidth = resources.getDimensionPixelOffset(prompt_view_width);

            int prompt_view_height = typedArray.getResourceId(R.styleable.SlideLeftLoadMoreLayout_slideLeftLoadMoreLayoutPromptViewHeight, R.dimen.height_320);
            promptViewHeight = resources.getDimensionPixelOffset(prompt_view_height);

            int prompt_text_size = typedArray.getResourceId(R.styleable.SlideLeftLoadMoreLayout_slideLeftLoadMoreLayoutPromptTextSize, R.dimen.text_size_28);
            promptTextSize = resources.getDimensionPixelSize(prompt_text_size);

            int prompt_text_margin = typedArray.getResourceId(R.styleable.SlideLeftLoadMoreLayout_slideLeftLoadMoreLayoutPromptTextMargin, R.dimen.width_20);
            promptTextMargin = resources.getDimensionPixelOffset(prompt_text_margin);

            promptTextColor = typedArray.getColor(R.styleable.SlideLeftLoadMoreLayout_slideLeftLoadMoreLayoutPromptTextColor, 0xFF9B9B9B);
            promptBackground = typedArray.getColor(R.styleable.SlideLeftLoadMoreLayout_slideLeftLoadMoreLayoutPromptBackground, 0xFFFFFFFF);

            typedArray.recycle();
        } else {
            promptViewWidth = resources.getDimensionPixelOffset(R.dimen.width_160);
            promptViewHeight = resources.getDimensionPixelOffset(R.dimen.height_320);

            promptTextSize = resources.getDimensionPixelSize(R.dimen.text_size_30);

            promptTextMargin = resources.getDimensionPixelOffset(R.dimen.width_20);

            promptTextColor = Color.parseColor("#9B9B9B");
            promptBackground = Color.parseColor("#FFFFFF");
        }

        if (getChildCount() > 1) {
            throw new RuntimeException("SlideLeftLoadMoreLayout can host only one direct child");
        }

        prompts = new String[]{context.getString(R.string.more_prompt), context.getString(R.string.release_prompt)};

        valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.setDuration(500);
        valueAnimator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                animatorState = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                translation = 0;
                animatorState = false;
                valueAnimator.removeAllUpdateListeners();
            }
        });
    }

    public void addView() {
        LayoutParams promptLayoutParams = new LayoutParams(promptViewWidth, promptViewHeight);
        promptLayoutParams.setMargins(0, promptTextMargin, 0, promptTextMargin);

        loadMorePromptView = new LoadMorePromptView(contextReference.get());
        loadMorePromptView.setColor(promptBackground);
        loadMorePromptView.setId(R.id.release_load_more);
        addView(loadMorePromptView, promptLayoutParams);

        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, promptViewHeight);
        layoutParams.setMargins(0, promptTextMargin, 0, promptTextMargin);

        textView = new TextView(contextReference.get());
        textView.setEms(1);
        textView.setTextColor(promptTextColor);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, promptTextSize);
        textView.setId(R.id.release_load_more_prompt);
        textView.setGravity(Gravity.CENTER);
        addView(textView, layoutParams);

        promptView = getChildAt(0);
    }

    @Override
    public void onLayoutChild(View child, int layoutDirection) {
        super.onLayoutChild(child, layoutDirection);
        if (child == textView) {
            viewWidth = child.getWidth();
            child.offsetLeftAndRight(getWidth() - viewWidth);
            child.offsetTopAndBottom(promptViewHeight / 2 - child.getHeight() / 2);
            child.setTranslationX(viewWidth);

            ((TextView) child).setText(prompts[0]);
        } else if (child == loadMorePromptView) {
            child.offsetLeftAndRight(getWidth() - child.getWidth());
        }
    }


    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_HORIZONTAL) != 0 && !animatorState;
    }

    @Override
    public void onNestedPreScroll(View view, int dx, int dy, int[] consumed) {
        super.onNestedPreScroll(view, dx, dy, consumed);
        if (promptView.getWidth() == getWidth() && dx > 0 && !ViewCompat.canScrollHorizontally(promptView, ViewCompat.SCROLL_INDICATOR_LEFT) || dx < 0 && promptView.getTranslationX() < 0) {
            consumed[0] = dx;
            int distance = dx / 2;

            if (translation - distance <= -promptViewHeight) {
                translation = -promptViewHeight;
            } else {
                translation = translation - distance;
            }
            promptView.setTranslationX(translation);
            translationTxt(translation);
            invalidateLoading(translation);
        } else {
            consumed[0] = 0;
        }
    }

    @Override
    public void onStopNestedScroll(View target) {
        super.onStopNestedScroll(target);
        if (translation != 0) {
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float translationX = (1 - animation.getAnimatedFraction()) * translation;
                    promptView.setTranslationX(translationX);
                    translationTxt(translationX);
                    invalidateLoading(translationX);

                }
            });

            valueAnimator.start();
            if (skipState && loadingMoreListener != null) {
                loadingMoreListener.loadMore();
            }
        }
    }

    private void translationTxt(float translationList) {
        float distance = translationList / 2;
        if (distance <= -viewWidth / 3 * 4) {
            skipState = true;
            textView.setText(prompts[1]);
            textView.setTranslationX(-1 / 3f * viewWidth);
        } else {
            skipState = false;
            textView.setText(prompts[0]);
            textView.setTranslationX(distance + viewWidth);
        }
    }

    private void invalidateLoading(float translationList) {
        float fraction = Math.abs(translationList) / promptViewHeight;
        loadMorePromptView.setFraction(fraction, translationList / 2);
    }


    public void setLoadingMoreListener(LoadingMoreListener loadingMoreListener) {
        this.loadingMoreListener = loadingMoreListener;
    }

    public interface LoadingMoreListener {
        void loadMore();
    }
}
