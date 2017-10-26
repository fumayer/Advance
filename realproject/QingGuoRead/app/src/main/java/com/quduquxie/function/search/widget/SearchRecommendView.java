package com.quduquxie.function.search.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.quduquxie.R;
import com.quduquxie.function.search.listener.SearchListener;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created on 17/2/9.
 * Created by crazylei.
 */

public class SearchRecommendView extends ViewGroup {

    private List<Line> lines = new ArrayList<>();

    private int horizontalSpace;
    private int verticalSpace;

    private int textSize;
    private int textColor;

    private int maxLines;

    private int paddingTop;
    private int paddingBottom;

    private int paddingLeft;
    private int paddingRight;

    private WeakReference<Context> contextReference;

    private SearchListener searchListener;

    private Random random = new Random();

    private ArrayList<String> recommendList = new ArrayList<>();

    public SearchRecommendView(Context context) {
        this(context, null);
    }

    public SearchRecommendView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchRecommendView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        contextReference = new WeakReference<>(context);

        Resources resources = context.getResources();

        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SearchRecommendView);

            int text_size = typedArray.getResourceId(R.styleable.SearchRecommendView_recommendTextSize, R.dimen.text_size_26);
            textSize = resources.getDimensionPixelSize(text_size);

            textColor = typedArray.getColor(R.styleable.SearchRecommendView_recommendTextColor, 0xFF191919);

            maxLines = typedArray.getInteger(R.styleable.SearchRecommendView_recommendMaxLines, 3);

            int top = typedArray.getResourceId(R.styleable.SearchRecommendView_recommendPaddingTop, R.dimen.width_12);
            paddingTop = resources.getDimensionPixelSize(top);

            int bottom = typedArray.getResourceId(R.styleable.SearchRecommendView_recommendPaddingBottom, R.dimen.width_12);
            paddingBottom = resources.getDimensionPixelSize(bottom);

            int left = typedArray.getResourceId(R.styleable.SearchRecommendView_recommendPaddingLeft, R.dimen.width_24);
            paddingLeft = resources.getDimensionPixelSize(left);

            int right = typedArray.getResourceId(R.styleable.SearchRecommendView_recommendPaddingRight, R.dimen.width_24);
            paddingRight = resources.getDimensionPixelSize(right);

            int vertical = typedArray.getResourceId(R.styleable.SearchRecommendView_recommendVerticalSpace, R.dimen.width_20);
            verticalSpace = resources.getDimensionPixelSize(vertical);

            int horizontal = typedArray.getResourceId(R.styleable.SearchRecommendView_recommendHorizontalSpace, R.dimen.width_20);
            horizontalSpace = resources.getDimensionPixelSize(horizontal);

            typedArray.recycle();
        } else {
            textSize = resources.getDimensionPixelSize(R.dimen.text_size_26);
            textColor = Color.parseColor("#191919");

            maxLines = 3;

            paddingTop = resources.getDimensionPixelOffset(R.dimen.width_12);
            paddingBottom = resources.getDimensionPixelOffset(R.dimen.width_12);

            paddingLeft = resources.getDimensionPixelOffset(R.dimen.width_24);
            paddingRight = resources.getDimensionPixelOffset(R.dimen.width_24);

            verticalSpace = resources.getDimensionPixelOffset(R.dimen.width_20);
            horizontalSpace = resources.getDimensionPixelOffset(R.dimen.width_20);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        lines.clear();

        Line currentLine = null;

        int layoutWidth = MeasureSpec.getSize(widthMeasureSpec);
        int maxLineWidth = layoutWidth - getPaddingLeft() - getPaddingRight();

        int count = getChildCount();

        for (int i = 0; i < count; i++) {

            View view = getChildAt(i);

            if (view.getVisibility() == GONE) {
                continue;
            }

            if (lines.size() >= maxLines && currentLine != null && !currentLine.judgeLineState(view)) {
                break;
            }

            measureChild(view, widthMeasureSpec, heightMeasureSpec);

            if (currentLine == null) {
                currentLine = new Line(maxLineWidth, horizontalSpace);
                lines.add(currentLine);
                currentLine.addView(view);
            } else {
                boolean result = currentLine.judgeLineState(view);
                if (result) {
                    currentLine.addView(view);
                } else {
                    currentLine = new Line(maxLineWidth, horizontalSpace);
                    lines.add(currentLine);
                    currentLine.addView(view);
                }
            }
        }

        float height = 0;
        for (int i = 0; i < lines.size(); i++) {
            float lineHeight = lines.get(i).height;
            height += lineHeight;
            if (i != 0) {
                height += verticalSpace;
            }
        }

        int measuredHeight = (int) (height + getPaddingTop() + getPaddingBottom() + 0.5f);

        setMeasuredDimension(layoutWidth, measuredHeight);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int paddingTop = getPaddingTop();
        int paddingLeft = getPaddingLeft();
        for (int i = 0; i < lines.size(); i++) {
            Line line = lines.get(i);
            line.layout(paddingLeft, paddingTop);
            paddingTop += line.height + verticalSpace;
        }
    }

    public void setRecommendData(ArrayList<String> recommends) {

        recommendList.clear();

        for (int i = 0; i < recommends.size(); i++) {
            int index = random.nextInt(recommends.size() - 1);
            String recommend = recommends.get(index);

            if (!recommendList.contains(recommend)) {

                TextView textView = new TextView(contextReference.get());
                textView.setText(recommend);
                textView.setTextColor(textColor);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                textView.setGravity(Gravity.CENTER);
                textView.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);


                textView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

                textView.setBackgroundResource(R.drawable.background_search_recommend);

                textView.setTag(R.id.click_object, recommend);
                textView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String category = (String) view.getTag(R.id.click_object);
                        if (searchListener != null && !TextUtils.isEmpty(category)) {
                            searchListener.categoryListener(category);
                        }
                    }
                });

                addView(textView);

                recommendList.add(recommend);
            }
        }
    }

    public void setRecommendListener(SearchListener searchListener) {
        this.searchListener = searchListener;
    }

    private class Line {
        private List<View> views = new ArrayList<>();

        private float maxWidth;
        private float usedWidth;
        private float height;
        private float horizontalSpace;

        public Line(int maxWidth, int horizontalSpace) {
            this.maxWidth = maxWidth;
            this.horizontalSpace = horizontalSpace;
        }

        public boolean judgeLineState(View view) {
            int size = views.size();

            if (size == 0) {
                return true;
            }

            int viewWidth = view.getMeasuredWidth();

            float estimate_width = usedWidth + horizontalSpace + viewWidth;
            return estimate_width <= maxWidth;
        }

        public void addView(View view) {
            int size = views.size();
            int viewWidth = view.getMeasuredWidth();
            int viewHeight = view.getMeasuredHeight();
            if (size == 0) {
                if (viewWidth > maxWidth) {
                    usedWidth = maxWidth;
                } else {
                    usedWidth = viewWidth;
                }
                height = viewHeight;
            } else {
                usedWidth += viewWidth + horizontalSpace;
                height = height < viewHeight ? viewHeight : height;
            }
            views.add(view);
        }

        public void layout(int offsetLeft, int offsetTop) {
            int currentLeft = offsetLeft;
            int size = views.size();

            for (int i = 0; i < size; i++) {
                View view = views.get(i);
                int viewWidth = view.getMeasuredWidth();
                int viewHeight = view.getMeasuredHeight();
                int left = currentLeft;
                int top = (int) (offsetTop + (height - viewHeight) / 2 + 0.5f);
                int right = left + viewWidth;
                int bottom = top + viewHeight;

                view.layout(left, top, right, bottom);

                currentLeft += viewWidth + horizontalSpace;
            }
        }

        public void layoutCenter(int offsetLeft, int offsetTop) {
            int currentLeft = offsetLeft;
            int size = views.size();
            float extra = 0;
            float intervalWidth = 0;
            if (maxWidth > usedWidth) {
                extra = maxWidth - usedWidth;
                intervalWidth = extra / size;
            }

            for (int i = 0; i < size; i++) {
                View view = views.get(i);
                int viewWidth = view.getMeasuredWidth();
                int viewHeight = view.getMeasuredHeight();

                if (intervalWidth != 0) {
                    int width = (int) (viewWidth + intervalWidth + 0.5f);
                    int widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
                    int heightMeasureSpec = MeasureSpec.makeMeasureSpec(viewHeight, MeasureSpec.EXACTLY);
                    view.measure(widthMeasureSpec, heightMeasureSpec);

                    viewWidth = view.getMeasuredWidth();
                    viewHeight = view.getMeasuredHeight();
                }

                int left = currentLeft;
                int top = (int) (offsetTop + (height - viewHeight) / 2 + 0.5f);
                int right = left + viewWidth;
                int bottom = top + viewHeight;

                view.layout(left, top, right, bottom);

                currentLeft += viewWidth + horizontalSpace;
            }
        }

    }

}
