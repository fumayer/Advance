package com.quduquxie.read.page;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.quduquxie.R;
import com.quduquxie.base.bean.Chapter;
import com.quduquxie.base.config.BaseConfig;
import com.quduquxie.base.util.TypefaceUtil;
import com.quduquxie.communal.utils.DeviceUtil;
import com.quduquxie.module.read.reading.view.ReadingActivity;
import com.quduquxie.read.DrawTextHelper;
import com.quduquxie.read.IReadDataFactory;
import com.quduquxie.read.NovelHelper;
import com.quduquxie.read.ReadStatus;
import com.quduquxie.read.animation.BitmapManager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class ScrollPageView extends LinearLayout implements PageInterface {

    private static final String TAG = ScrollPageView.class.getSimpleName();

    private RelativeLayout read_scroll_view;
    private CustomListView scroll_list;

    private Context context;
    private Activity activity;

    private CallBack callBack;
    private NovelHelper novelHelper;
    private ReadStatus readStatus;
    private DrawTextHelper drawTextHelper;
    private IReadDataFactory readDataFactory;
    private ScrollPageAdapter scrollPageAdapter;

    private Chapter previousChapter;
    private Chapter currentChapter;

    private Chapter nextChapter;

    public Chapter tempChapter;
    private int previousSize;
    private int currentSize;
    private int nextSize;

    private boolean loadingData = false;

    private int page_width, page_height;

    private FrameLayout.LayoutParams layoutParams;

    private float lastY;
    private int lastVisible = -1;

    /**
     * List滑动监听参数
     **/
    private int totalItemCount;
    private int firstVisibleItem = -1;

    private BitmapManager bitmapManager;

    private final MHandler handler = new MHandler(this);

    private long startTouchTime;
    private int startTouchX;
    private int startTouchY;
    private boolean isNeedToNextPage = false;

    private ArrayList<ArrayList<String>> chapterContent;
    private ArrayList<ArrayList<String>> preChaperConent;
    private ArrayList<ArrayList<String>> currentChaperConent;
    private ArrayList<ArrayList<String>> nextChaperContent;

    private ArrayList<String> chapterNameList = new ArrayList<>();

    public ScrollPageView(Context context) {
        super(context);
        this.context = context;
    }

    public ScrollPageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public ScrollPageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }

    @Override
    public void setReadFactory(IReadDataFactory readDataFactory) {
        this.readDataFactory = readDataFactory;
    }

    @Override
    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    public void init(Activity activity, ReadStatus readStatus, NovelHelper novelHelper) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_read_scroll, this);
        read_scroll_view = (RelativeLayout) view.findViewById(R.id.read_scroll_view);
        scroll_list = (CustomListView) view.findViewById(R.id.scroll_list);

        drawBackground();

        this.novelHelper = novelHelper;
        this.readStatus = readStatus;

        page_width = readStatus.screen_width;
        page_height = readStatus.screen_height - DeviceUtil.dip2px(context, 20);

        chapterContent = new ArrayList<>();

        drawTextHelper = new DrawTextHelper(getResources(), this, activity.getApplication());
        drawTextHelper.setTypeFace(TypefaceUtil.loadTypeface(context, BaseConfig.READING_TYPEFACE));

        scrollPageAdapter = new ScrollPageAdapter();
        scroll_list.setAdapter(scrollPageAdapter);

        layoutParams = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, page_height);
        scroll_list.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                ScrollPageView.this.totalItemCount = totalItemCount;
                ScrollPageView.this.firstVisibleItem = firstVisibleItem;
                int lastVisibleIndex = scroll_list.getLastVisiblePosition();
                if (ScrollPageView.this.lastVisible != lastVisibleIndex) {
                    ScrollPageView.this.lastVisible = lastVisibleIndex;
                    ScrollPageView.this.readStatus.current_page = getCurrentPage(lastVisibleIndex);
                }
            }
        });
    }

    @Override
    protected void onSizeChanged(int width, int height, int old_width, int old_height) {
        super.onSizeChanged(width, height, old_width, old_height);
        page_width = readStatus.screen_width = width;
        page_height = readStatus.screen_height = height;
        bitmapManager = new BitmapManager(readStatus.screen_width, readStatus.screen_height);
        if (callBack != null && (Math.abs(old_height - height) >= DeviceUtil.dip2px(context, 20))) {
            callBack.onResize();
            page_height = readStatus.screen_height - DeviceUtil.dip2px(context, 20);
            layoutParams = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, page_height);
            setBackground();
            if (chapterContent != null) {
                getChapter(true);
            }
        }
    }

    /**
     * 绘制当前页内容
     **/
    @Override
    public void drawCurrentPage() {

    }

    /**
     * 绘制下一页内容
     **/
    @Override
    public void drawNextPage() {

    }

    /**
     * 刷新当前页
     **/
    @Override
    public void refreshCurrentPage() {

    }

    /**
     * 刷新时间
     **/
    @Override
    public void freshTime(CharSequence time) {

    }


    /**
     * 设置文字颜色
     **/
    @Override
    public void setTextColor(int color) {
        if (drawTextHelper != null) {
            drawTextHelper.setTextColor(color);
        }
    }

    /**
     * 设置字体
     **/
    @Override
    public void setTypeFace(Typeface typeFace) {
        if (drawTextHelper != null && typeFace != null) {
            drawTextHelper.setTypeFace(typeFace);
        }
    }

    /**
     * 设置背景颜色
     **/
    @Override
    public void setBackground() {
        drawTextHelper.resetBackBitmap();
        drawBackground();
        if (scrollPageAdapter != null) {
            scrollPageAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 设置动画背景颜色
     **/
    @Override
    public void setPageBackColor(int color) {

    }

    /**
     * 翻到上一页
     **/
    @Override
    public void tryTurnPrePage() {

    }

    /**
     * 动画结束
     **/
    @Override
    public void onAnimationFinish() {

    }

    /**
     * 回收资源
     **/
    @Override
    public void recyclerData() {

        if (bitmapManager != null) {
            bitmapManager.clearBitmap();
        }

        if (drawTextHelper != null) {
            drawTextHelper.recyclerData();
        }

        if (this.activity != null) {
            this.activity = null;
        }

        if (this.context != null) {
            this.context = null;
        }
    }

    @Override
    public boolean setKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP) {
            if (event.getAction() == KeyEvent.ACTION_UP) {
                int position;
                if (android.os.Build.VERSION.SDK_INT < 11) {
                    position = lastVisible - 1;
                } else {
                    position = lastVisible - 2;
                }
                scroll_list.setSelection(position);
                if (!loadingData && lastVisible == 1 && readStatus.current_page == 1) {
                    loadingData = true;
                    isNeedToNextPage = true;
                    boolean result = readDataFactory.previous();
                    if (!result) {
                        handler.sendEmptyMessageDelayed(0, 1000);
                    }
                }
            }
            return true;
        }
        if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN) {
            if (event.getAction() == KeyEvent.ACTION_UP) {
                int position;
                if (android.os.Build.VERSION.SDK_INT < 11) {
                    position = lastVisible + 1;
                } else {
                    position = lastVisible;
                }
                scroll_list.setSelection(position);
                if (!loadingData && (totalItemCount == lastVisible + 1 ||
                        totalItemCount == lastVisible + 2) && readStatus.current_page == readStatus.page_count) {
                    loadingData = true;
                    isNeedToNextPage = true;
                    boolean result = readDataFactory.next();
                    if (!result) {
                        handler.sendEmptyMessageDelayed(0, 1000);
                    }
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 获取章节
     **/
    @Override
    public void getChapter(boolean needSavePage) {
        if (readStatus.line_list == null) {
            return;
        }
        chapterContent.clear();
        previousChapter = null;
        nextChapter = null;
        currentChaperConent = null;
        nextChaperContent = null;
        preChaperConent = null;

        currentChapter = readDataFactory.currentChapter;
        currentChapter.chapterNameList = readStatus.chapter_name_list;
        currentChaperConent = readStatus.line_list;
        chapterContent.addAll(currentChaperConent);
        if (scrollPageAdapter != null) {
            scrollPageAdapter.notifyDataSetChanged();
        }
        loadingData = false;
        previousSize = 0;
        currentSize = 0;
        nextSize = 0;
        getChapterSize();
        int offset = readStatus.offset;
        if (!needSavePage) {
            readStatus.current_page = 1;
        }

        getCurrentPage(readStatus.current_page);
        readStatus.offset = offset;
        final int position = readStatus.current_page - 1;
        if (readStatus.current_page > 1) {
            post(new Runnable() {

                @Override
                public void run() {
                    scroll_list.setSelection(position);
                    readStatus.current_page = position + 1;
                }
            });

        } else if (readStatus.current_page == 1) {
            post(new Runnable() {

                @Override
                public void run() {
                    scroll_list.setSelection(0);
                    readStatus.current_page = 1;
                }
            });
        }
    }

    /**
     * 获取上一章
     **/
    @Override
    public void getPreChapter() {
        boolean canRemove = false;
        ArrayList<ArrayList<String>> temp = nextChaperContent;
        if (preChaperConent != null) {
            nextChaperContent = currentChaperConent;
            currentChaperConent = preChaperConent;

            nextChapter = currentChapter;
            currentChapter = previousChapter;

            canRemove = true;
        }
        previousChapter = readDataFactory.currentChapter;
        previousChapter.chapterNameList = readStatus.chapter_name_list;
        preChaperConent = readStatus.line_list;
        getChapterSize();
        chapterContent.addAll(0, readStatus.line_list);
        if (temp != null && canRemove) {
            chapterContent.clear();
            chapterContent.addAll(preChaperConent);
            chapterContent.addAll(currentChaperConent);
            chapterContent.addAll(nextChaperContent);
        }
        if (scrollPageAdapter != null) {
            scrollPageAdapter.notifyDataSetChanged();
        }
        int position = preChaperConent.size();
        if (isNeedToNextPage) {
            scroll_list.setSelection(position - 1);
        } else {
            scroll_list.setSelection(position);
        }
        loadingData = false;

        if (readStatus.offset == 0) {
            readStatus.offset = 1;
        }
    }

    /**
     * 获取下一章
     **/
    @Override
    public void getNextChapter() {
        ArrayList<ArrayList<String>> temp = preChaperConent;
        boolean canRemove = false;
        if (nextChaperContent != null) {
            preChaperConent = currentChaperConent;
            currentChaperConent = nextChaperContent;

            previousChapter = currentChapter;
            currentChapter = nextChapter;

            canRemove = true;
        }
        nextChapter = readDataFactory.currentChapter;
        nextChapter.chapterNameList = readStatus.chapter_name_list;
        if (nextChapter == null) {
            return;
        }
        nextChaperContent = readStatus.line_list;
        int position = nextChaperContent.size();
        getChapterSize();

        chapterContent.addAll(readStatus.line_list);
        if (temp != null && canRemove) {
            chapterContent.clear();
            chapterContent.addAll(preChaperConent);
            chapterContent.addAll(currentChaperConent);
            chapterContent.addAll(readStatus.line_list);
        }
        if (scrollPageAdapter != null) {
            scrollPageAdapter.notifyDataSetChanged();
        }
        if (isNeedToNextPage) {
            position = chapterContent.size() - position;
        } else {
            position = chapterContent.size() - position - 1;
        }

        scroll_list.setSelection(position);
        loadingData = false;

        if (readStatus.offset == 0) {
            readStatus.offset = 1;
        }
    }

    /**
     * 获取当前章节
     **/
    private int getCurrentPage(int position) {
        String chapter_name = "";
        if (previousSize == 0) {
            if (position <= currentSize) {
                readStatus.page_count = currentSize;
                if (currentChapter != null) {
                    tempChapter = currentChapter;
                    readStatus.sequence = currentChapter.sn - 1;
                    chapter_name = currentChapter.name;
                }

            } else if (position <= currentSize + nextSize) {
                position = position - currentSize;
                readStatus.page_count = nextSize;
                if (nextChapter != null) {
                    tempChapter = nextChapter;
                    readStatus.sequence = nextChapter.sn - 1;
                    chapter_name = nextChapter.name;
                }
            }
        } else if (nextSize == 0) {
            if (position <= previousSize) {
                readStatus.page_count = previousSize;
                if (previousChapter != null) {
                    tempChapter = previousChapter;
                    readStatus.sequence = previousChapter.sn - 1;
                    chapter_name = previousChapter.name;
                }

            } else if (position <= currentSize + previousSize) {
                position = position - previousSize;
                readStatus.page_count = currentSize;
                if (currentChapter != null) {
                    tempChapter = currentChapter;
                    chapter_name = currentChapter.name;
                    readStatus.sequence = currentChapter.sn - 1;
                }
            }
        } else {
            if (position <= previousSize) {
                readStatus.page_count = previousSize;
                if (previousChapter != null) {
                    tempChapter = previousChapter;
                    chapter_name = previousChapter.name;
                    readStatus.sequence = previousChapter.sn - 1;
                }

            } else if (position <= currentSize + previousSize) {
                position = position - previousSize;
                readStatus.page_count = currentSize;
                if (currentChapter != null) {
                    tempChapter = currentChapter;
                    chapter_name = currentChapter.name;
                    readStatus.sequence = currentChapter.sn - 1;
                }
            } else if (position <= nextSize + previousSize + currentSize) {
                position = position - currentSize - previousSize;
                readStatus.page_count = nextSize;
                if (nextChapter != null) {
                    tempChapter = nextChapter;
                    chapter_name = nextChapter.name;
                    readStatus.sequence = nextChapter.sn - 1;
                }

            }
        }
        readStatus.chapter_name = chapter_name;

        if (context instanceof ReadingActivity) {
            ((ReadingActivity) context).freshPage();
        }
        novelHelper.getPageContentScroll();
        return position;
    }

    private void drawBackground() {
        int color;
        if (BaseConfig.READING_BACKGROUND_MODE == 1) {
            color = R.color.read_background_first;
        } else if (BaseConfig.READING_BACKGROUND_MODE == 2) {
            color = R.color.read_background_second;
        } else if (BaseConfig.READING_BACKGROUND_MODE == 3) {
            color = R.color.read_background_third;
        } else if (BaseConfig.READING_BACKGROUND_MODE == 4) {
            color = R.color.read_background_fourth;
        } else if (BaseConfig.READING_BACKGROUND_MODE == 5) {
            color = R.color.read_background_fifth;
        } else if (BaseConfig.READING_BACKGROUND_MODE == 6) {
            color = R.color.read_background_sixth;
        } else {
            color = R.color.read_background_first;
        }
        read_scroll_view.setBackgroundColor(getResources().getColor(color));
        scroll_list.setBackground(color);
    }

    static class MHandler extends Handler {

        private WeakReference<ScrollPageView> weakReference;

        MHandler(ScrollPageView scrollPageView) {
            weakReference = new WeakReference<>(scrollPageView);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ScrollPageView pageView = weakReference.get();
            if (pageView == null) {
                return;
            }
            switch (msg.what) {
                case 0:
                    pageView.changeLoadState();
                    break;
                default:
                    break;
            }
        }
    }

    private class ScrollPageAdapter extends BaseAdapter {

        LayoutInflater layoutInflater;

        ScrollPageAdapter() {
            layoutInflater = LayoutInflater.from(getContext());
        }

        @Override
        public int getCount() {
            return chapterContent.size();
        }

        @Override
        public Object getItem(int position) {
            return chapterContent.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.layout_read_scroll_item, null);
                viewHolder = new ViewHolder();
                viewHolder.customPage = (CustomPage) convertView.findViewById(R.id.read_scroll_item_page);
                convertView.setTag(viewHolder);
                Bitmap currentBitmap = bitmapManager.getBitmap();
                Canvas currentCanvas = new Canvas(currentBitmap);

                viewHolder.customPage.setTag(R.id.tag_bitmap, currentBitmap);
                viewHolder.customPage.setTag(R.id.tag_canvas, currentCanvas);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.customPage.setLayoutParams(layoutParams);

            getCurrentSequence(position + 1);

            Bitmap mCurPageBitmap = (Bitmap) viewHolder.customPage.getTag(R.id.tag_bitmap);
            Canvas mCurrentCanvas = (Canvas) viewHolder.customPage.getTag(R.id.tag_canvas);
            drawTextHelper.drawText(mCurrentCanvas, chapterContent.get(position), chapterNameList);
            viewHolder.customPage.drawPage(mCurPageBitmap);
            return convertView;
        }

        class ViewHolder {
            CustomPage customPage;
        }
    }

    private int getCurrentSequence(int position) {
        if (previousSize == 0) {
            if (position <= currentSize) {
                if (currentChapter != null) {
                    chapterNameList = currentChapter.chapterNameList;
                }

            } else if (position <= currentSize + nextSize) {
                if (nextChapter != null) {
                    chapterNameList = nextChapter.chapterNameList;
                }
            }
        } else if (nextSize == 0) {
            if (position <= previousSize) {
                if (previousChapter != null) {
                    chapterNameList = previousChapter.chapterNameList;
                }

            } else if (position <= currentSize + previousSize) {
                if (currentChapter != null) {
                    chapterNameList = currentChapter.chapterNameList;
                }
            }
        } else {
            if (position <= previousSize) {
                if (previousChapter != null) {
                    chapterNameList = previousChapter.chapterNameList;
                }

            } else if (position <= currentSize + previousSize) {
                if (currentChapter != null) {
                    chapterNameList = currentChapter.chapterNameList;
                }
            } else if (position <= nextSize + previousSize + currentSize) {
                position = position - currentSize - previousSize;
                if (nextChapter != null) {
                    chapterNameList = nextChapter.chapterNameList;
                }

            }
        }

        return position;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (readStatus.menu_show) {
            callBack.onShowMenu(false);
            return false;
        }
        int tmpX = (int) event.getX();
        int tmpY = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startTouchTime = System.currentTimeMillis();
                lastY = event.getY();
                startTouchX = tmpX;
                startTouchY = tmpY;
                break;
            case MotionEvent.ACTION_MOVE:
                if (!loadingData && lastY - event.getY() > 20 && totalItemCount == lastVisible + 1 && readStatus.current_page == readStatus.page_count) {
                    loadingData = true;
                    isNeedToNextPage = false;
                    boolean result = readDataFactory.next();
                    if (!result) {
                        handler.sendEmptyMessageDelayed(0, 1000);
                    }

                } else if (!loadingData && lastY - event.getY() < -20 && firstVisibleItem == 0 && readStatus.current_page == 1) {
                    loadingData = true;
                    if (currentChaperConent != null && currentChaperConent.size() > 0) {
                        ArrayList<String> arrayList = currentChaperConent.get(0);
                        if (arrayList != null && arrayList.size() > 0 && arrayList.get(0).contains("qgnovel_hp")) {
                            loadingData = false;
                            if (scroll_list.getChildCount() > 1 && scroll_list.getChildAt(1) != null &&
                                    Math.abs(scroll_list.getChildAt(1).getTop() - page_height) < 10) {
                                Toast.makeText(context, "当前已经是第一章", Toast.LENGTH_SHORT).show();
                            }
                            return false;
                        }
                    }
                    if (preChaperConent != null && preChaperConent.size() > 0) {
                        ArrayList<String> arrayList = preChaperConent.get(0);
                        if (arrayList != null && arrayList.size() > 0 && arrayList.get(0).contains("qgnovel_hp")) {
                            loadingData = false;
                            if (scroll_list.getChildCount() > 1 && scroll_list.getChildAt(1) != null && Math.abs(scroll_list.getChildAt(1).getTop() - page_height) < 10) {
                                Toast.makeText(context, "当前已经是第一章", Toast.LENGTH_SHORT).show();
                            }
                            return false;
                        }
                    }
                    boolean result = readDataFactory.previous();
                    if (!result) {
                        handler.sendEmptyMessageDelayed(0, 1000);
                    }

                }
                break;
            case MotionEvent.ACTION_UP:
                long touchTime = System.currentTimeMillis() - startTouchTime;
                int distance = (int) Math.sqrt(Math.pow(startTouchX - tmpX, 2) + Math.pow(startTouchY - tmpY, 2));
                if (touchTime < 100 && distance < 30 || distance < 10) {
                    onClick(event);
                }
                startTouchTime = 0;
                break;
            case MotionEvent.ACTION_CANCEL:
                startTouchTime = 0;
                break;
            default:
                break;
        }
        return super.onInterceptTouchEvent(event);
    }

    private void onClick(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        int h4 = page_height / 4;
        int w3 = page_width / 3;
        if (x <= w3) {

        } else if (x >= page_width - w3 || (y >= page_height - h4 && x >= w3)) {

        } else {
            if (callBack != null) {
                callBack.onShowMenu(true);
            }
        }
    }

    public void changeLoadState() {
        loadingData = false;
    }

    private void getChapterSize() {

        if (preChaperConent != null) {
            previousSize = preChaperConent.size();
        } else {
            previousSize = 0;
        }
        if (currentChaperConent != null) {
            currentSize = currentChaperConent.size();
        }
        if (nextChaperContent != null) {
            nextSize = nextChaperContent.size();
        }
    }
}
