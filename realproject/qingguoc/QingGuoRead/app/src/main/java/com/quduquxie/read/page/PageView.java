package com.quduquxie.read.page;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import com.quduquxie.Constants;
import com.quduquxie.base.config.BaseConfig;
import com.quduquxie.base.util.TypefaceUtil;
import com.quduquxie.communal.utils.DeviceUtil;
import com.quduquxie.read.DrawTextHelper;
import com.quduquxie.read.IReadDataFactory;
import com.quduquxie.read.NovelHelper;
import com.quduquxie.read.ReadStatus;
import com.quduquxie.read.animation.AnimationProvider;
import com.quduquxie.read.animation.BitmapManager;
import com.quduquxie.read.animation.CurlAnimationProvider;
import com.quduquxie.read.animation.ShiftAnimationProvider;
import com.quduquxie.read.animation.SlideAnimationProvider;
import com.quduquxie.util.QGLog;

import java.util.List;

/**
 * 阅读页View
 */
public class PageView extends View implements PageInterface {

    private static final String TAG = PageView.class.getSimpleName();

    private Context context;
    private Activity activity;
    private CallBack callBack;
    private Scroller scroller;

    private BitmapManager bitmapManager;

    private Canvas currentPageCanvas;
    private Canvas nextPageCanvas;

    /**
     * 默认的滑动、翻页阀值
     **/
    private static final int move_threshold_dip = 20;
    private static final int turn_threshold_dip = 10;

    /**
     * 默认的向下滑动的阀值
     **/
    private static final int down_move_threshold_dip = 10;
    private static final int down_stop_threshold_dip = 120;

    /**
     * 屏幕宽、高
     **/
    private int page_width, page_height;

    private int move_threshold;
    private int turn_threshold;

    private int down_move_threshold;
    private int down_stop_threshold;

    private AnimationProvider animationProvider;

    private List<String> pageLines;

    /**
     * 仿真翻页背面颜色
     **/
    private int backColor;

    private ReadStatus readStatus;
    private NovelHelper novelHelper;
    private DrawTextHelper drawTextHelper;
    private IReadDataFactory readDataFactory;

    private boolean animation_curl_type = false;

    private static final int ANIMATION_SIMULATION_NORMAL = 0;
    private static final int ANIMATION_SIMULATION_UP_DOWN = 1;
    private static final int ANIMATION_SIMULATION_LEFT_RIGHT = 2;
    private static final int ANIMATION_SIMULATION_CLICK = 3;

    private int animation_simulation_type = ANIMATION_SIMULATION_NORMAL;

    private List<String> currentPageContent;
    private List<String> nextPageContent;

    /**
     * 滑动事件相关参数
     **/
    private int touchStartX;
    private int touchStartY;
    private long touchStartTime;

    private int currentMoveStateStartX;
    private int currentMoveStateLastX;

    private boolean is_moving = false;
    private boolean is_move_other = false;
    private boolean is_move_bottom = false;

    private boolean can_consume_move_bottom = false;
    private boolean is_move_two = false;

    private MotionState motionState = MotionState.kNone;
    private MotionState initMoveState = MotionState.kNone;
    private MotionState validMoveState = MotionState.kNone;

    public PageView(Context context) {
        super(context);
        this.context = context;
    }

    public PageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public PageView(Context context, AttributeSet attrs, int defStyle) {
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

    @SuppressLint("NewApi")
    public void init(Activity activity, ReadStatus readStatus, NovelHelper novelHelper) {
        this.context = activity;
        this.readStatus = readStatus;
        this.novelHelper = novelHelper;

        drawTextHelper = new DrawTextHelper(getResources(), this, activity.getApplication());

        scroller = new Scroller(context);

        bitmapManager = new BitmapManager(readStatus.screen_width, readStatus.screen_height);

        Bitmap currentPageBitmap = bitmapManager.getBitmap(0);
        Bitmap nextPageBitmap = bitmapManager.getBitmap(1);

        currentPageCanvas = new Canvas(currentPageBitmap);
        nextPageCanvas = new Canvas(nextPageBitmap);

        move_threshold = DeviceUtil.dip2px(context, move_threshold_dip);
        turn_threshold = DeviceUtil.dip2px(context, turn_threshold_dip);

        down_move_threshold = DeviceUtil.dip2px(context, down_move_threshold_dip);
        down_stop_threshold = DeviceUtil.dip2px(context, down_stop_threshold_dip);

        page_width = readStatus.screen_width;
        page_height = readStatus.screen_height;

        drawTextHelper.drawText(currentPageCanvas, pageLines);

        postInvalidate();
    }

    @Override
    protected void onSizeChanged(int width, int height, int old_width, int old_height) {
        super.onSizeChanged(width, height, old_width, old_height);
        page_width = readStatus.screen_width = width;
        page_height = readStatus.screen_height = height;
        if (animationProvider != null) {
            animationProvider.setWidthHeight(width, height);
        }
        if (callBack != null && (Math.abs(old_height - height) > DeviceUtil.dip2px(context, 26))) {
            getAnimationProvider();
            callBack.onResize();
            if (!is_moving) {
                drawCurrentPage();
            }
            drawNextPage();
        }
        setBackground();
    }

    @Override
    public void computeScroll() {
        super.computeScroll();

        if (animation_curl_type) {
            if (animation_simulation_type == ANIMATION_SIMULATION_UP_DOWN) {
                if (scroller.computeScrollOffset()) {
                    offsetTopAndBottom(scroller.getCurrY() - getTop());
                    invalidate();
                }
            } else if (animation_simulation_type == ANIMATION_SIMULATION_LEFT_RIGHT || animation_simulation_type == ANIMATION_SIMULATION_CLICK) {
                ((CurlAnimationProvider) animationProvider).dealComputerScroller();
            }
        } else {
            if (scroller.computeScrollOffset()) {
                offsetTopAndBottom(scroller.getCurrY() - getTop());
                invalidate();
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        animationProvider = getAnimationProvider();
        animationProvider.drawInternal(canvas);
    }

    /**
     * 获取动画
     **/
    private AnimationProvider getAnimationProvider() {
        if (animationProvider == null || animationProvider.pageMode != BaseConfig.READING_FLIP_MODE) {
            switch (BaseConfig.READING_FLIP_MODE) {
                case AnimationProvider.CURL_MODE:
                    animation_curl_type = true;
                    BaseConfig.READING_FLIP_UP_DOWN = false;
                    animationProvider = new CurlAnimationProvider(bitmapManager, readStatus);
                    break;
                case AnimationProvider.SHIFT_MODE:
                    animation_curl_type = false;
                    BaseConfig.READING_FLIP_UP_DOWN = false;
                    animationProvider = new ShiftAnimationProvider(bitmapManager, readStatus);
                    break;
                case AnimationProvider.SLIDE_MODE:
                    animation_curl_type = false;
                    BaseConfig.READING_FLIP_UP_DOWN = false;
                    animationProvider = new SlideAnimationProvider(bitmapManager, readStatus);
                    break;
                default:
                    animation_curl_type = false;
                    animationProvider = new ShiftAnimationProvider(bitmapManager, readStatus);
                    break;
            }
            callBack.onResize();
            animationProvider.clear();
            animationProvider.backColor = this.backColor;
            animationProvider.pageMode = BaseConfig.READING_FLIP_MODE;
            animationProvider.setScroller(scroller);
            animationProvider.setCallBack(callBack);
            animationProvider.setPageView(this);
            animationProvider.getDivider(getResources());
        }
        return animationProvider;
    }

    /**
     * 绘制当前页内容
     **/
    @Override
    public void drawCurrentPage() {
        currentPageContent = pageLines = novelHelper.getPageContent();
        refreshCurrentPage();

        if (callBack != null) {
            callBack.refreshBookMark();
        }
    }

    /**
     * 绘制下一页内容
     **/
    @Override
    public void drawNextPage() {
        nextPageContent = pageLines = novelHelper.getPageContent();
        drawTextHelper.drawText(nextPageCanvas, pageLines);
        postInvalidate();

        if (callBack != null) {
            callBack.refreshBookMark();
        }
    }

    /**
     * 刷新当前页
     **/
    @Override
    public void refreshCurrentPage() {
        drawTextHelper.setTypeFace(TypefaceUtil.loadTypeface(context, BaseConfig.READING_TYPEFACE));
        drawTextHelper.drawText(currentPageCanvas, pageLines);
    }

    /**
     * 刷新时间
     **/
    @Override
    public void freshTime(CharSequence time) {
        if (time != null && time.length() > 0) {
            drawTextHelper.setTimeText(time.toString());
        }
        if (currentPageCanvas != null) {
            drawTextHelper.drawCanvasFoot(currentPageCanvas);
        }
        if (nextPageCanvas != null) {
            drawTextHelper.drawCanvasFoot(nextPageCanvas);
        }
        postInvalidate();
    }

    /**
     * 设置文字颜色
     **/
    @Override
    public void setTextColor(int color) {
        drawTextHelper.setTextColor(color);
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
        drawTextHelper.drawText(currentPageCanvas, pageLines);
        drawTextHelper.drawText(nextPageCanvas, pageLines);
        invalidate();
    }

    /**
     * 设置动画背景颜色
     **/
    @Override
    public void setPageBackColor(int color) {
        this.backColor = color;
        if (animationProvider != null) {
            animationProvider.backColor = color;
        }
    }

    /**
     * 翻到上一页
     **/
    @Override
    public void tryTurnPrePage() {
        QGLog.e(TAG, "tryTurnPrePage");
        if (animation_curl_type) {
            drawNextPage();
        }
        if (prepareTurnPrePage()) {
            animationProvider.setTouchStartPosition(0, page_height, false);
            animationProvider.startTurnAnimation(false);
            if (animation_curl_type) {
                drawCurrentPage();
            } else {
                drawNextPage();
            }
        }
    }

    /**
     * 翻到下一页
     **/
    private void tryTurnNextPage(MotionEvent event) {
        QGLog.e(TAG, "tryTurnNextPage");
        drawCurrentPage();
        if (prepareTurnNextPage()) {
            int pix = DeviceUtil.dip2px(getContext(), 50);
            if (null == event) {
                animationProvider.setTouchStartPosition(page_width - pix, page_height - pix, true);
            } else {
                animationProvider.setTouchStartPosition((int) event.getX(), (int) event.getY(), true);
            }
            animationProvider.startTurnAnimation(true);
            drawNextPage();
        }
    }

    /**
     * 动画结束
     **/
    @Override
    public void onAnimationFinish() {
        if (cancelState && callBack != null) {
            callBack.onCancelPage();
        }
        cancelState = false;
        drawCurrentPage();
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

        if (animationProvider != null) {
            animationProvider.clear();
        }

        if (this.activity != null) {
            this.activity = null;
        }

        if (this.context != null) {
            this.context = null;
        }

        if (this.readStatus != null) {
            this.readStatus = null;
        }

        if (this.readDataFactory != null) {
            this.readDataFactory = null;
        }

        if (this.pageLines != null) {
            this.pageLines.clear();
            this.pageLines = null;
        }

        if (this.nextPageContent != null) {
            this.nextPageContent.clear();
            this.nextPageContent = null;
        }

        if (this.currentPageContent != null) {
            this.currentPageContent.clear();
            this.currentPageContent = null;
        }
        System.gc();
    }

    @Override
    public boolean setKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP) {
            if (event.getAction() == KeyEvent.ACTION_UP) {
                tryTurnPrePage();
            }
            return true;
        }
        if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN) {
            if (event.getAction() == KeyEvent.ACTION_UP) {
                tryTurnNextPage(null);
            }
            return true;
        }
        return false;
    }

    /**
     * 动画相关
     **/
    @Override
    public boolean onTouchEvent(MotionEvent e) {

        if (readStatus != null && readStatus.menu_show) {
            callBack.onShowMenu(false);
            return false;
        }
        return onTouchEventSlide(e);

    }

    private enum MotionState {
        kWaiting, kMoveToLeft, kMoveToRight, kNone, kMoveBottom,
    }

    private boolean onTouchEventSlide(MotionEvent event) {
        int tmpX = getStartPointX(event);
        int tmpY = getStartPointY(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchStartX = tmpX;
                touchStartY = tmpY;

                touchStartTime = System.currentTimeMillis();

                QGLog.e(TAG, "ACTION_DOWN事件数量: " + event.getPointerCount());

                motionState = MotionState.kWaiting;
                initMoveState = MotionState.kNone;
                validMoveState = MotionState.kNone;
                animationProvider = getAnimationProvider();

                is_move_two = false;

                if (!animation_curl_type && scroller.computeScrollOffset())
                    scroller.forceFinished(true);

                return true;
            case MotionEvent.ACTION_MOVE:
                is_moving = true;
                QGLog.e(TAG, "ACTION_MOVE事件数量: " + event.getPointerCount() + " : " + is_move_other);

                if (event.getPointerCount() == 2) {
                    QGLog.e(TAG, "双指滑动");

                    if (System.currentTimeMillis() - touchStartTime < 300) {
                        is_move_two = true;
                        return true;
                    } else {
                        is_move_two = false;
                        return false;
                    }

                } else if (event.getPointerCount() == 1 && !is_move_two) {
                    QGLog.e(TAG, "单指滑动");
                    if (System.currentTimeMillis() - touchStartTime > 100) {
                        is_move_two = false;
                        int offsetX = Math.abs(tmpX - touchStartX);
                        int offsetY = Math.abs(tmpY - touchStartY);

                        if (!is_move_other && !is_move_bottom) {
                            QGLog.e(TAG, "检查滑动方向：" + offsetX + " : " + offsetY);
                            if (offsetX > offsetY) {
                                QGLog.e(TAG, "检查滑动方向为左右");
                                is_move_other = true;
                                if (animation_curl_type) {
                                    animation_simulation_type = ANIMATION_SIMULATION_LEFT_RIGHT;
                                }
                                return handleTouchEventMove(event);
                            } else {
                                QGLog.e(TAG, "检查滑动方向为上下");
                                is_move_bottom = true;
                                if (animation_curl_type) {
                                    animation_simulation_type = ANIMATION_SIMULATION_UP_DOWN;
                                }
                                return handleTouchEventMoveBottom(event);
                            }
                        } else if (is_move_other) {
                            QGLog.e(TAG, "左右滑动");
                            return handleTouchEventMove(event);
                        } else {
                            QGLog.e(TAG, "上下滑动");
                            return handleTouchEventMoveBottom(event);
                        }
                    } else {
                        return true;
                    }
                } else {
                    return true;
                }

            case MotionEvent.ACTION_CANCEL:
                QGLog.e(TAG, "ACTION_CANCEL事件数量: " + event.getPointerCount());
                is_moving = false;
                is_move_other = false;
                is_move_bottom = false;
                is_move_two = false;
                motionState = MotionState.kNone;
                return true;
            case MotionEvent.ACTION_UP:
                QGLog.e(TAG, "ACTION_UP事件数量: " + event.getPointerCount());
                is_moving = false;
                if (is_move_two) {
                    QGLog.e(TAG, "ACTION_UP事件两指: " + is_move_other + " : " + is_move_bottom);
                    return handleTwoTouchEventUp(event);
                } else {
                    if (is_move_other) {
                        QGLog.e(TAG, "左右滑动事件消费");
                        return handleTouchEventUp(event);
                    } else if (is_move_bottom) {
                        QGLog.e(TAG, "上下滑动事件消费");
                        return handleTouchEventUpBottom();
                    } else {
                        QGLog.e(TAG, "点击事件消费");
                        if (touchStartX - tmpX < 10 && touchStartY - tmpY < 10) {
                            if (animation_curl_type) {
                                animation_simulation_type = ANIMATION_SIMULATION_CLICK;
                            }
                            return handleTouchEventUp(event);
                        }
                    }
                }

        }
        return super.onTouchEvent(event);
    }

    /**
     * 获取事件点击X轴位置
     **/
    private int getStartPointX(MotionEvent event) {
        return (int) event.getX();
    }

    /**
     * 获取事件点击Y轴位置
     **/
    private int getStartPointY(MotionEvent event) {
        return (int) event.getY();
    }

    /**
     * ACTION_MOVE：左右滑动事件处理
     **/
    private boolean handleTouchEventMove(MotionEvent event) {
        int tmpX = getStartPointX(event);
        if (MotionState.kNone == motionState) {
            return false;
        }

        if (event.getPointerCount() > 1) {
            return true;
        }
        if (MotionState.kWaiting == motionState) {
            if (Math.abs(tmpX - touchStartX) < move_threshold) {
                return true;
            }
            QGLog.e(TAG, "左右滑动事件消费");
            animationProvider.finishAnimation();
            if (tmpX > touchStartX) {
                if (animation_curl_type) {
                    drawNextPage();
                }
                if (!prepareTurnPrePage()) {
                    motionState = MotionState.kNone;
                    return false;
                }
                if (animation_curl_type) {
                    drawCurrentPage();
                } else {
                    drawNextPage();
                }
                motionState = MotionState.kMoveToRight;
            } else {
                if (!prepareTurnNextPage()) {
                    motionState = MotionState.kNone;
                    return false;
                }
                drawNextPage();
                motionState = MotionState.kMoveToLeft;
            }
            initMoveState = motionState;
            currentMoveStateStartX = touchStartX;
            currentMoveStateLastX = touchStartX;
            animationProvider.setTouchStartPosition(touchStartX, touchStartY, MotionState.kMoveToLeft == motionState);
        }
        updateMoveState(tmpX);
        animationProvider.moveEvent(event);
        postInvalidate();
        return true;
    }

    /**
     * ACTION_MOVE：上下滑动事件处理
     **/
    private boolean handleTouchEventMoveBottom(MotionEvent event) {
        int tmpY = getStartPointY(event);
        if (MotionState.kNone == motionState) {
            return false;
        }

        if (event.getPointerCount() > 1) {
            return true;
        }

        if (animation_curl_type && animationProvider != null && !scroller.isFinished()) {
            animationProvider.finishAnimation();
            return false;
        }

        if (MotionState.kWaiting == motionState) {
            int offsetY = tmpY - touchStartY;
            if (offsetY > down_move_threshold && getTop() <= down_stop_threshold) {
                can_consume_move_bottom = true;
                if (callBack != null) {
                    callBack.refreshHideView(getTop());
                }
                offsetTopAndBottom(offsetY);
            }
        }
        return true;
    }

    /**
     * ACTION_UP:左右滑动事件消费
     **/
    private boolean handleTouchEventUp(MotionEvent event) {
        if (animationProvider != null) {
            animationProvider.upEvent();
        }
        if (motionState == MotionState.kNone)
            return false;
        if (motionState == MotionState.kWaiting) {
            onClick(event);
        } else {
            if (initMoveState == validMoveState && animationProvider != null) {
                if (MotionState.kMoveToRight == validMoveState) {
                    animationProvider.startTurnAnimation(false);
                } else {
                    animationProvider.startTurnAnimation(true);
                }
            } else if (animationProvider != null) {
                animationProvider.startTurnAnimation(MotionState.kMoveToRight == initMoveState);
            }
        }
        motionState = MotionState.kNone;
        is_move_other = false;
        return true;
    }

    /**
     * ACTION_UP:上下滑动事件消费
     **/
    private boolean handleTouchEventUpBottom() {
        scroller.startScroll(getLeft(), getTop(), -getLeft(), -getTop(), 300);
        invalidate();
        is_move_bottom = false;

        QGLog.e(TAG, "handleTouchEventUpBottom: " + can_consume_move_bottom);

        if (can_consume_move_bottom && callBack != null) {
            callBack.changeBookMarkDB();
            can_consume_move_bottom = false;
        }

        return true;
    }

    /**
     * ACTION_UP:双指滑动事件消费
     **/
    private boolean handleTwoTouchEventUp(MotionEvent event) {

        int offsetX = (int) (event.getX() - touchStartX);

        if (!scroller.isFinished()) {
            if (animationProvider != null) {
                animationProvider.finishAnimation();
                return true;
            }
        } else {
            if (offsetX > move_threshold) {
                if (callBack != null) {
                    callBack.openCatalog();
                }
            }
        }
        is_move_two = false;
        return true;
    }

    private void onClick(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        int h4 = page_height / 4;
        int w3 = page_width / 3;
        int w2 = page_width / 2;
        if (x <= w3 && y <= page_height - h4) {
            tryTurnPrePage();
        } else if (x >= page_width - w3 && y <= page_height - h4) {
            tryTurnNextPage(event);
        } else if (y >= page_height - h4 && x <= w2) {
            tryTurnPrePage();
        } else if (y >= page_height - h4 && x > w2) {
            tryTurnNextPage(event);
        } else {
            computeScroll();
            if (callBack != null) {
                callBack.onShowMenu(true);
            }
        }
    }

    private boolean prepareTurnPrePage() {
        Constants.manualReadedCount++;
        return readDataFactory.previous();
    }

    private boolean prepareTurnNextPage() {
        Constants.manualReadedCount++;
        return readDataFactory.next();
    }

    private boolean cancelState = false;
    private boolean canRestore = false;

    private void updateMoveState(int currentX) {
        if (MotionState.kMoveToRight == motionState) {
            if (canRestore && !animation_curl_type && (currentX > touchStartX) && callBack != null) {
                canRestore = false;
            }

            if (currentX < currentMoveStateLastX - move_threshold) {
                motionState = MotionState.kMoveToLeft;
                currentMoveStateStartX = currentMoveStateLastX;
                cancelState = !cancelState;
                canRestore = true;
            }

            currentMoveStateLastX = currentX;
        } else if (MotionState.kMoveToLeft == motionState) {
            if (canRestore && !animation_curl_type && (currentX < touchStartX) && callBack != null) {
                canRestore = false;
            }
            if (currentX > currentMoveStateLastX + move_threshold) {
                motionState = MotionState.kMoveToRight;
                currentMoveStateStartX = currentMoveStateLastX;
                cancelState = !cancelState;
                canRestore = true;
            }

            currentMoveStateLastX = currentX;
        } else {
            return;
        }
        if (Math.abs(currentX - currentMoveStateStartX) > turn_threshold) {
            if (currentX > currentMoveStateStartX) {
                validMoveState = MotionState.kMoveToRight;
            } else {
                validMoveState = MotionState.kMoveToLeft;
            }
        }
    }

    @Override
    public void getChapter(boolean needSavePage) {
        // TODO Auto-generated method stub

    }

    @Override
    public void getPreChapter() {
        // TODO Auto-generated method stub

    }

    @Override
    public void getNextChapter() {
        // TODO Auto-generated method stub
    }
}
