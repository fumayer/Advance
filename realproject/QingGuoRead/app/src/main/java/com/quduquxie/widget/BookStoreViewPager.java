package com.quduquxie.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.quduquxie.Constants;
import com.quduquxie.util.QGLog;

/**
 * 创建:陈家辉
 * 时间:2016/7/23 0023 17:58
 * 本类作用:
 * 书城页面，推荐页
 * 最左侧卡片向左滑动提示“进入书架”
 * 最右侧卡片向右滑动提示“进入书库”
 * 在推荐页面只有在最右侧卡片再向右滑动时才进入书库
 */
public class BookStoreViewPager extends ViewPager {

    static class BookStoreViewPagerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }

    /**
     * 是否允许进入书架
     */
    private boolean isEnterShelf = false;

    /**
     * 是否允许进入书库
     */
    private boolean isEnterShuKu = false;

    /**
     * 是否允许回调，只回调一次即可，默认允许
     */
    private boolean isCallBack = true;

    /**
     * 是否允许滑动
     */
    public boolean isScroll = false;

    private int currentPosition = 0;

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
        setCurrentItem(currentPosition);
    }

    public boolean isIntercept = false;

    public BookStoreViewPager(Context context) {
        super(context);
        initData();
    }

    public BookStoreViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        initData();
    }

    private BookStoreViewPagerHandler bookStoreViewPagerHandler;

    private void initData() {
        if (bookStoreViewPagerHandler == null) {
            bookStoreViewPagerHandler = new BookStoreViewPagerHandler();
        }
    }

    private float downX;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                QGLog.e("bookstoreviewpager", "按下操作");
                downX = ev.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                if (getCurrentItem() == 0) {
                    if (currentPosition == 0) {
                        if (Constants.BOOK_RECOMMEND_CURRENT == 0) {
                            //当前是第一页(推荐页的viewpager)
                            if (ev.getX() - downX > 200) {
                                //向右滑动
                                if (onStoreViewPagerListener != null && isCallBack) {
                                    isEnterShelf = true;
                                    isEnterShuKu = false;
                                    onStoreViewPagerListener.showEnterBook();
                                    onStoreViewPagerListener.hideEnterShuKu();

                                    isCallBack = false;
                                    isScroll = false;
                                }
                            }
                        }

                        if (Constants.BOOK_RECOMMEND_CURRENT == Constants.BOOK_RECOMMEND_TOTAL) {
                            //最后一页
                            if (downX == 0) {
                                downX = ev.getX();
                            }
                            if (downX - ev.getX() > 0) {
                                QGLog.e("bookstoreviewpager", "显示进入书架");
                                if (onStoreViewPagerListener != null && isCallBack) {

                                    isEnterShelf = false;
                                    isEnterShuKu = true;
                                    onStoreViewPagerListener.hideEnterBook();
                                    onStoreViewPagerListener.showEnterShuKu();

                                    isCallBack = false;
                                }
                            }
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (getCurrentItem() == 0) {
                    downX = 0;
                    if (currentPosition == 0) {

                        if (isEnterShelf && !isEnterShuKu && onStoreViewPagerListener != null) {
                            //up进入书架
                            onStoreViewPagerListener.enterBook();
                        }

                        if (!isEnterShelf && isEnterShuKu && onStoreViewPagerListener != null) {
                            //up进入书库
                            onStoreViewPagerListener.hideEnterShuKu();
                            isScroll = true;
                            bookStoreViewPagerHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    isEnterShuKu = false;
                                    setCurrentItem(1, true);
                                }
                            });
                        }
                        isCallBack = true;
                    }
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public void scrollTo(int x, int y) {
        if (isScroll) {
            super.scrollTo(x, y);
        }
    }

    public void enterShuKu() {
        onStoreViewPagerListener.hideEnterShuKu();
        isScroll = true;
        bookStoreViewPagerHandler.post(new Runnable() {
            @Override
            public void run() {
                isEnterShuKu = false;
                QGLog.e("BookStoreViewPage", "EnterShuKu");
                setCurrentItem(1, true);
            }
        });
    }

    public void clearMessage() {
        if (bookStoreViewPagerHandler != null) {
            bookStoreViewPagerHandler.removeCallbacksAndMessages(null);
        }
    }

    private OnStoreViewPagerListener onStoreViewPagerListener;

    public void setOnStoreViewPagerListener(OnStoreViewPagerListener l) {
        this.onStoreViewPagerListener = l;
    }

    public interface OnStoreViewPagerListener {
        void showEnterBook();

        void hideEnterBook();

        void showEnterShuKu();

        void hideEnterShuKu();

        void enterBook();

        void enterShuKu();
    }
}
