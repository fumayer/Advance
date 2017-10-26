package com.quduquxie.base.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quduquxie.R;
import com.quduquxie.widget.BookStoreViewPager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SlidePagerTabStrip extends HorizontalScrollView implements View.OnClickListener {
    // 当前位置
    private int currentPosition;

    private int lastOffset;
    private int lastScrollX = 0;

    // 当前位置偏移量
    private float currentPositionOffset;

    private boolean start;

    // 内容宽度无法充满时，允许自动调整Item的宽度以充满
    private boolean tabsAllowWidthFull;

    // 禁用ViewPager
    private boolean disableViewPager;

    // 当前标题项
    private View currentSelectedView;

    // 滑块
    private Drawable slideBlockDrawable;

    // ViewPager
    private ViewPager viewPager;

    // 标题项布局
    private ViewGroup tabsLayout;

    // 页面改变监听器
    private OnPageChangeListener onPageChangeListener;

    private OnPageChangedListener onPageChangedListener;

    private OnClickTabListener onClickTabListener;

    private List<View> tabsView;

    private LinearLayout.LayoutParams defaultTabLayoutParams;
    private LinearLayout.LayoutParams expandedTabLayoutParams;

    private Paint dividerPaint;

    private boolean shouldExpand = false;

    private float dividerWidth = 0.5f;

    private int dividerColor;

    private int slideBlockHeight;

    private int slideBlockMargin;


    public SlidePagerTabStrip(Context context) {
        this(context, null);
    }

    public SlidePagerTabStrip(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidePagerTabStrip(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //隐藏横向滑动提示条
        setHorizontalScrollBarEnabled(false);

        Resources resources = context.getResources();

        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SlidePagerTabStrip);

            slideBlockDrawable = typedArray.getDrawable(R.styleable.SlidePagerTabStrip_tabSlideBlock);

            disableViewPager = typedArray.getBoolean(R.styleable.SlidePagerTabStrip_viewPagerSlideDisable, false);

            int height = typedArray.getResourceId(R.styleable.SlidePagerTabStrip_slideBlockHeight, R.dimen.height_2);
            slideBlockHeight = resources.getDimensionPixelOffset(height);

            int margin = typedArray.getResourceId(R.styleable.SlidePagerTabStrip_slideBlockMargin, R.dimen.width_10);
            slideBlockMargin = resources.getDimensionPixelOffset(margin);

            int color = typedArray.getResourceId(R.styleable.SlidePagerTabStrip_dividerColor, R.color.color_transparent);
            dividerColor = resources.getColor(color);

            tabsAllowWidthFull = typedArray.getBoolean(R.styleable.SlidePagerTabStrip_tabsAllowWidthFull, false);

            typedArray.recycle();
        } else {
            slideBlockDrawable = resources.getDrawable(R.color.theme_primary);

            disableViewPager = false;

            slideBlockHeight = resources.getDimensionPixelOffset(R.dimen.height_2);

            slideBlockMargin = resources.getDimensionPixelOffset(R.dimen.width_10);

            tabsAllowWidthFull = false;

            dividerColor = resources.getColor(R.color.color_transparent);
        }

        dividerPaint = new Paint();
        dividerPaint.setAntiAlias(true);
        dividerPaint.setStrokeWidth(dividerWidth);

        defaultTabLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        expandedTabLayoutParams = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (!tabsAllowWidthFull)
            return;
        ViewGroup tabsLayout = getTabsLayout();
        if (tabsLayout == null || tabsLayout.getMeasuredWidth() >= getMeasuredWidth())
            return;
        if (tabsLayout.getChildCount() <= 0)
            return;

        if (tabsView == null) {
            tabsView = new ArrayList<>();
        } else {
            tabsView.clear();
        }
        for (int w = 0; w < tabsLayout.getChildCount(); w++) {
            tabsView.add(tabsLayout.getChildAt(w));
        }

        adjustChildWidthWithParent(tabsView, getMeasuredWidth() - tabsLayout.getPaddingLeft() - tabsLayout.getPaddingRight(), widthMeasureSpec, heightMeasureSpec);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 调整views集合中的View，让所有View的宽度加起来正好等于parentViewWidth
     */
    private void adjustChildWidthWithParent(List<View> views, int parentViewWidth, int parentWidthMeasureSpec, int parentHeightMeasureSpec) {
        // 先去掉所有子View的外边距
        for (View view : views) {
            if (view.getLayoutParams() instanceof MarginLayoutParams) {
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
                parentViewWidth -= layoutParams.leftMargin + layoutParams.rightMargin;
            }
        }

        // 去掉宽度大于平均宽度的View后再次计算平均宽度
        int averageWidth = parentViewWidth / views.size();
        int bigTabCount = views.size();
        while (true) {
            Iterator<View> iterator = views.iterator();
            while (iterator.hasNext()) {
                View view = iterator.next();
                if (view.getMeasuredWidth() > averageWidth) {
                    parentViewWidth -= view.getMeasuredWidth();
                    bigTabCount--;
                    iterator.remove();
                }
            }
            averageWidth = parentViewWidth / bigTabCount;
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

        // 修改宽度小于新的平均宽度的View的宽度
        for (View view : views) {
            if (view.getMeasuredWidth() < averageWidth) {
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
                layoutParams.width = averageWidth;
                view.setLayoutParams(layoutParams);
                // 再次测量让新宽度生效
                if (layoutParams instanceof MarginLayoutParams) {
                    measureChildWithMargins(view, parentWidthMeasureSpec, 0, parentHeightMeasureSpec, 0);
                } else {
                    measureChild(view, parentWidthMeasureSpec, parentHeightMeasureSpec);
                }
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        ViewGroup tabViewGroup = getTabsLayout();
        if (tabViewGroup != null) {
            // 初始化滑块位置以及选中状态
            currentPosition = viewPager != null ? viewPager.getCurrentItem() : 0;
            if (!disableViewPager) {
                scrollToChild(currentPosition, 0); // 移动滑块到指定位置
                selectedTab(currentPosition); // 选中指定位置的TAB
            }

            // 给每一个tab设置点击事件，当点击的时候切换Pager
            for (int w = 0; w < tabViewGroup.getChildCount(); w++) {
                View itemView = tabViewGroup.getChildAt(w);
                itemView.setTag(w);
                itemView.setOnClickListener(this);
            }
        }
    }

    @Override
    public void onClick(View v) {
        int index = (Integer) v.getTag();
        if (onClickTabListener != null) {
            onClickTabListener.onClickTab(v, index);
        }
        if (viewPager != null) {
            if (viewPager instanceof BookStoreViewPager) {
                ((BookStoreViewPager) viewPager).isScroll = true;
            }
            viewPager.setCurrentItem(index, true);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (disableViewPager)
            return;
        /* 绘制滑块 */
        ViewGroup tabsLayout = getTabsLayout();
        if (tabsLayout != null && tabsLayout.getChildCount() > 0 && slideBlockDrawable != null) {
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
                slideBlockDrawable.setBounds((int) slidingBlockLeft + slideBlockMargin, getHeight() - slideBlockHeight, (int) slidingBlockRight - slideBlockMargin, getHeight());
                slideBlockDrawable.draw(canvas);
            }

            dividerPaint.setColor(dividerColor);

            for (int i = 0; i < tabsLayout.getChildCount() - 1; i++) {
                View tab = tabsLayout.getChildAt(i);
                canvas.drawLine(tab.getRight(), slideBlockMargin, tab.getRight(), getHeight() - slideBlockMargin, dividerPaint);
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
                // 计算新的X坐标
                int newScrollX = view.getLeft() + offset;
                if (position > 0 || offset > 0) {
                    newScrollX -= 240 - getOffset(view.getWidth()) / 2;
                }

                // 如果同上次X坐标不一样就执行滚动
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
            if (currentSelectedView != null) {
                currentSelectedView.setSelected(false);
            }
            currentSelectedView = tabsLayout.getChildAt(currentSelectedTabPosition);
            if (currentSelectedView != null) {
                currentSelectedView.setSelected(true);
            }
        }
    }

    private void addTextTab(final int position, String title) {

        TextView tab = new TextView(getContext());
        tab.setText(title);
        tab.setGravity(Gravity.CENTER);
        tab.setSingleLine();

        addTab(position, tab);
    }

    private void addTab(final int position, View tab) {
        tab.setFocusable(true);
    }

    /**
     * 添加Tab
     */
    public void addTab(View tabView, int index) {
        if (tabView != null) {
            getTabsLayout().addView(tabView, index, shouldExpand ? expandedTabLayoutParams : defaultTabLayoutParams);
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

    /**
     * 移除一个tab
     */
    public void removeTab(int index) {
        removeTab(index, 1);
    }

    /**
     * 移除tab
     */
    public void removeTab(int start, int count) {
        int tabCount = getTabCount();
        if (start < 0 || start > tabCount) {
            start = 0;
        }
        if (count < 0 || count > tabCount) {
            count = 1;
        }
        if (count - start > tabCount) {
            count = tabCount - start;
        }

        getTabsLayout().removeViews(start, count);
        requestLayout();
    }

    /**
     * 移除所有
     */
    public void removeAllTab() {
        getTabsLayout().removeAllViews();
        requestLayout();
    }

    public View getChild(int idx) {
        return getTabsLayout().getChildAt(idx);
    }

    /**
     * 设置ViewPager
     */
    public void setViewPager(final ViewPager viewPager) {
        if (disableViewPager)
            return;
        this.viewPager = viewPager;
        this.viewPager.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {

                selectedTab(position);
                if (onPageChangeListener != null) {
                    onPageChangeListener.onPageSelected(position);
                }
                if (onPageChangedListener != null)
                    onPageChangedListener.onChanged(position);
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
            public void onPageScrollStateChanged(int arg0) {

                if (onPageChangeListener != null) {
                    onPageChangeListener.onPageScrollStateChanged(arg0);
                }
            }
        });
        requestLayout();
    }

    /**
     * 设置Page切换监听器
     */
    public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
    }

    /**
     * 设置是否充满屏幕
     */
    public void setTabsAllowWidthFull(boolean tabsAllowWidthFull) {
        this.tabsAllowWidthFull = tabsAllowWidthFull;
        requestLayout();
    }

    /**
     * 设置滑块图片
     */
    public void setSlideBlockDrawable(Drawable slideBlockDrawable) {
        this.slideBlockDrawable = slideBlockDrawable;
        requestLayout();
    }

    /**
     * 获取Tab总数
     */
    public int getTabCount() {
        ViewGroup tabsLayout = getTabsLayout();
        return tabsLayout != null ? tabsLayout.getChildCount() : 0;
    }

    /**
     * 设置Tab点击监听器
     */
    public void setOnClickTabListener(OnClickTabListener onClickTabListener) {
        this.onClickTabListener = onClickTabListener;
    }

    /**
     * 设置不使用ViewPager
     */
    public void setDisableViewPager(boolean disableViewPager) {
        this.disableViewPager = disableViewPager;
        if (viewPager != null) {
            viewPager.addOnPageChangeListener(onPageChangeListener);
            viewPager = null;
        }
        requestLayout();
    }

    public void setDividerColor(int dividerColor) {
        this.dividerColor = dividerColor;
        invalidate();
    }

    public interface OnClickTabListener {
        void onClickTab(View view, int index);
    }

    public void setOnPageChangedListener(OnPageChangedListener onPageChangedListener) {
        this.onPageChangedListener = onPageChangedListener;
    }

    public interface OnPageChangedListener {
        void onChanged(int page);
    }
}