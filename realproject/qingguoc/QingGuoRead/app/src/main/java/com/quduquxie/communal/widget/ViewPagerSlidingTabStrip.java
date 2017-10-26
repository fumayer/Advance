package com.quduquxie.communal.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.quduquxie.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ViewPagerSlidingTabStrip extends HorizontalScrollView implements View.OnClickListener {

    private int slidingBlockHeight;

    private boolean allowWidthFull;

    private Drawable slidingBlock;

    private int slidingBlockWidth;

    private float dividerWidth;

    private int dividerColor;

    private int dividerPadding;

    private boolean showDivider;

    private Paint dividerPaint;

    private LinearLayout.LayoutParams layoutParams;

    private List<View> tabs;

    private boolean start;
    private int lastOffset;
    private int currentPosition;
    private int lastScrollX = 0;
    private float currentPositionOffset;

    private View currentSelectedTabView;

    private ViewPager viewPager;
    private ViewGroup tabsLayout;

    private OnClickTabListener onClickTabListener;
    private ViewPager.OnPageChangeListener onPageChangeListener;

    public ViewPagerSlidingTabStrip(Context context) {
        this(context, null);
    }

    public ViewPagerSlidingTabStrip(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewPagerSlidingTabStrip(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setHorizontalScrollBarEnabled(false);

        Resources resources = context.getResources();

        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ViewPagerSlidingTabStrip);

            int sliding_block_height = typedArray.getResourceId(R.styleable.ViewPagerSlidingTabStrip_viewPagerSlidingTabStripSlidingBlockHeight, R.dimen.height_4);
            slidingBlockHeight = resources.getDimensionPixelOffset(sliding_block_height);

            allowWidthFull = typedArray.getBoolean(R.styleable.ViewPagerSlidingTabStrip_viewPagerSlidingTabStripAllowWidthFull, false);

            int sliding_block = typedArray.getResourceId(R.styleable.ViewPagerSlidingTabStrip_viewPagerSlidingTabStripSlidingBlock, R.drawable.icon_bookstore_navigation);
            slidingBlock = resources.getDrawable(sliding_block);

            int sliding_block_width = typedArray.getResourceId(R.styleable.ViewPagerSlidingTabStrip_viewPagerSlidingTabStripSlidingBlockWidth, R.dimen.width_32);
            slidingBlockWidth = resources.getDimensionPixelOffset(sliding_block_width);

            int divider_width = typedArray.getResourceId(R.styleable.ViewPagerSlidingTabStrip_viewPagerSlidingTabStripDividerWidth, R.dimen.width_1);
            dividerWidth = resources.getDimension(divider_width);

            dividerColor = typedArray.getColor(R.styleable.ViewPagerSlidingTabStrip_viewPagerSlidingTabStripDividerColor, 0xFFD8D8D8);

            int divider_padding = typedArray.getResourceId(R.styleable.ViewPagerSlidingTabStrip_viewPagerSlidingTabStripDividerPadding, R.dimen.width_10);
            dividerPadding = resources.getDimensionPixelOffset(divider_padding);

            showDivider = typedArray.getBoolean(R.styleable.ViewPagerSlidingTabStrip_viewPagerSlidingTabStripShowDivider, false);

            typedArray.recycle();
        } else {
            slidingBlockHeight = resources.getDimensionPixelOffset(R.dimen.height_4);

            allowWidthFull = false;

            slidingBlock = resources.getDrawable(R.drawable.icon_bookstore_navigation);

            slidingBlockWidth = resources.getDimensionPixelOffset(R.dimen.width_32);

            dividerWidth = resources.getDimension(R.dimen.width_1);

            dividerColor = Color.parseColor("#D8D8D8");

            dividerPadding = resources.getDimensionPixelOffset(R.dimen.width_10);

            showDivider = false;
        }

        dividerPaint = new Paint();
        dividerPaint.setAntiAlias(true);
        dividerPaint.setStrokeWidth(dividerWidth);

        layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (!allowWidthFull) {
            return;
        }

        ViewGroup viewGroup = getTabsLayout();
        if (viewGroup == null || viewGroup.getMeasuredWidth() >= getMeasuredWidth())
            return;
        if (viewGroup.getChildCount() <= 0)
            return;

        if (tabs == null) {
            tabs = new ArrayList<>();
        } else {
            tabs.clear();
        }
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            tabs.add(viewGroup.getChildAt(i));
        }

        adjustChildWidthWithParent(tabs, getMeasuredWidth() - viewGroup.getPaddingLeft() - viewGroup.getPaddingRight(), widthMeasureSpec, heightMeasureSpec);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void adjustChildWidthWithParent(List<View> views, int parentViewWidth, int parentWidthMeasureSpec, int parentHeightMeasureSpec) {
        for (View view : views) {
            if (view.getLayoutParams() instanceof MarginLayoutParams) {
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
                parentViewWidth -= layoutParams.leftMargin + layoutParams.rightMargin;
            }
        }

        int averageWidth = parentViewWidth / views.size();
        int count = views.size();
        while (true) {
            Iterator<View> iterator = views.iterator();
            while (iterator.hasNext()) {
                View view = iterator.next();
                if (view.getMeasuredWidth() > averageWidth) {
                    parentViewWidth -= view.getMeasuredWidth();
                    count--;
                    iterator.remove();
                }
            }
            averageWidth = parentViewWidth / count;
            boolean end = true;
            for (View view : views) {
                if (view.getMeasuredWidth() > averageWidth) {
                    end = false;
                }
            }
            if (end) {
                break;
            }
        }

        for (View view : views) {
            if (view.getMeasuredWidth() < averageWidth) {
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
                layoutParams.width = averageWidth;
                view.setLayoutParams(layoutParams);
                measureChildWithMargins(view, parentWidthMeasureSpec, 0, parentHeightMeasureSpec, 0);
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        ViewGroup tabViewGroup = getTabsLayout();
        if (tabViewGroup != null) {
            currentPosition = viewPager != null ? viewPager.getCurrentItem() : 0;

            scrollToChild(currentPosition, 0);
            selectedTab(currentPosition);

            for (int i = 0; i < tabViewGroup.getChildCount(); i++) {
                View itemView = tabViewGroup.getChildAt(i);
                itemView.setTag(i);
                itemView.setOnClickListener(this);
            }
        }
    }

    @Override
    public void onClick(View view) {
        int index = (Integer) view.getTag();
        if (onClickTabListener != null) {
            onClickTabListener.onClickTab(view, index);
        }
        if (viewPager != null) {
            viewPager.setCurrentItem(index, true);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        ViewGroup tabsLayout = getTabsLayout();
        if (tabsLayout != null && tabsLayout.getChildCount() > 0 && slidingBlock != null) {
            View currentTab = tabsLayout.getChildAt(currentPosition);
            if (currentTab != null) {
                float slidingBlockLeft = currentTab.getLeft();
                float slidingBlockRight = currentTab.getRight();
                if (currentPositionOffset > 0f && currentPosition < tabsLayout.getChildCount() - 1) {
                    View nextTab = tabsLayout.getChildAt(currentPosition + 1);
                    if (nextTab != null) {
                        final float nextTabLeft = nextTab.getLeft();
                        final float nextTabRight = nextTab.getRight();
                        slidingBlockLeft = (currentPositionOffset * nextTabLeft + (1f - currentPositionOffset) * slidingBlockLeft);
                        slidingBlockRight = (currentPositionOffset * nextTabRight + (1f - currentPositionOffset) * slidingBlockRight);
                    }
                }

                int padding = (int) ((slidingBlockRight - slidingBlockLeft - slidingBlockWidth) / 2);

                slidingBlock.setBounds((int) slidingBlockLeft + padding, getHeight() - slidingBlockHeight, (int) slidingBlockRight - padding, getHeight());
                slidingBlock.draw(canvas);
            }

            if (showDivider) {
                dividerPaint.setColor(dividerColor);
                for (int i = 0; i < tabsLayout.getChildCount() - 1; i++) {
                    View tab = tabsLayout.getChildAt(i);
                    canvas.drawLine(tab.getRight(), dividerPadding, tab.getRight(), getHeight() - dividerPadding, dividerPaint);
                }
            }
        }
    }

    /**
     * 获取布局
     */
    private ViewGroup getTabsLayout() {
        if (tabsLayout == null) {
            if (getChildCount() > 0) {
                tabsLayout = (ViewGroup) getChildAt(0);
            } else {
                removeAllViews();
                tabsLayout = new LinearLayout(getContext());
                addView(tabsLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
            }
        }
        return tabsLayout;
    }

    /**
     * 滚动到指定的位置
     */
    private void scrollToChild(int position, int offset) {
        ViewGroup tabsLayout = getTabsLayout();
        if (tabsLayout != null && tabsLayout.getChildCount() > 0 && position < tabsLayout.getChildCount()) {
            View view = tabsLayout.getChildAt(position);
            if (view != null) {
                int newScrollX = view.getLeft() + offset;
                if (position > 0 || offset > 0) {
                    newScrollX -= 240 - getOffset(view.getWidth()) / 2;
                }

                if (newScrollX != lastScrollX) {
                    lastScrollX = newScrollX;
                    scrollTo(newScrollX, 0);
                }
            }
        }
    }

    /**
     * 获取偏移量
     */
    private int getOffset(int newOffset) {
        if (lastOffset < newOffset) {
            if (start) {
                lastOffset += 1;
                return lastOffset;
            } else {
                start = true;
                lastOffset += 1;
                return lastOffset;
            }
        }
        if (lastOffset > newOffset) {
            if (start) {
                lastOffset -= 1;
                return lastOffset;
            } else {
                start = true;
                lastOffset -= 1;
                return lastOffset;
            }
        } else {
            start = true;
            lastOffset = newOffset;
            return lastOffset;
        }
    }

    /**
     * 选中指定位置的TAB
     */
    public void selectedTab(int currentSelectedTabPosition) {
        ViewGroup tabsLayout = getTabsLayout();
        if (currentSelectedTabPosition > -1 && tabsLayout != null && currentSelectedTabPosition < tabsLayout.getChildCount()) {
            if (currentSelectedTabView != null) {
                currentSelectedTabView.setSelected(false);
            }
            currentSelectedTabView = tabsLayout.getChildAt(currentSelectedTabPosition);
            if (currentSelectedTabView != null) {
                currentSelectedTabView.setSelected(true);
            }
        }
    }

    /**
     * 添加Tab
     */
    public void addTab(View tabView, int index) {
        if (tabView != null) {
            getTabsLayout().addView(tabView, index, layoutParams);
            requestLayout();
        }
    }

    /**
     * 添加Tab
     */
    public void addTab(View tabView) {
        addTab(tabView, -1);
    }

    /**
     * 添加Tab
     */
    public void addTab(View... tabViews) {
        if (tabViews != null) {
            for (View view : tabViews) {
                getTabsLayout().addView(view);
            }
            requestLayout();
        }
    }

    /**
     * 添加Tab
     */
    public void addTab(List<View> tabViews) {
        if (tabViews != null) {
            for (View view : tabViews) {
                getTabsLayout().addView(view);
            }
            requestLayout();
        }
    }

    public void setViewPager(final ViewPager viewPager) {
        this.viewPager = viewPager;
        this.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                selectedTab(position);

                if (onPageChangeListener != null) {
                    onPageChangeListener.onPageSelected(position);
                }
            }

            @Override
            public void onPageScrolled(int nextPagePosition, float positionOffset, int positionOffsetPixels) {

                ViewGroup tabsLayout = getTabsLayout();
                if (nextPagePosition < tabsLayout.getChildCount()) {
                    View view = tabsLayout.getChildAt(nextPagePosition);
                    if (view != null) {
                        currentPosition = nextPagePosition;
                        currentPositionOffset = positionOffset;
                        scrollToChild(nextPagePosition, (int) (positionOffset * view.getWidth()));
                        invalidate();
                    }
                }
                if (onPageChangeListener != null) {
                    onPageChangeListener.onPageScrolled(nextPagePosition, positionOffset, positionOffsetPixels);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

                if (onPageChangeListener != null) {
                    onPageChangeListener.onPageScrollStateChanged(state);
                }
            }
        });
        requestLayout();
    }

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
    }

    public void setOnClickTabListener(OnClickTabListener onClickTabListener) {
        this.onClickTabListener = onClickTabListener;
    }

    public interface OnClickTabListener {
        void onClickTab(View tab, int index);
    }
}