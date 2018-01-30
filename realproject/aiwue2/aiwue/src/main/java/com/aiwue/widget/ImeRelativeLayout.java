package com.aiwue.widget;

/**
 * Created by liaixiong on 16-8-31.
 * 这个类主要为了更好的监听输入法的展开和隐藏事件。可参见网上的一些帖子
 * http://blog.csdn.net/top_code/article/details/9187481
 */

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.orhanobut.logger.Logger;


public class ImeRelativeLayout extends RelativeLayout {
    private static final String TAG = ImeRelativeLayout.class.getSimpleName();
    public static final byte KEYBOARD_STATE_SHOW = -3;
    public static final byte KEYBOARD_STATE_HIDE = -2;
    public static final byte KEYBOARD_STATE_INIT = -1;
    private boolean mHasInit;
    private boolean mHasKeybord;
    private int mHeight;
    private onImeChangeListener mListener;

    public ImeRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ImeRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImeRelativeLayout(Context context) {
        super(context);
    }

    /**
     * set keyboard state listener
     */
    public void setImeStateListener(onImeChangeListener listener) {
        mListener = listener;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (!mHasInit) {
            mHasInit = true;
            mHeight = b;
            if (mListener != null) {
                mListener.onKeyBoardStateChange(KEYBOARD_STATE_INIT);
            }
        } else {
            mHeight = mHeight < b ? b : mHeight;
        }
        if (mHasInit && mHeight > b) {
            mHasKeybord = true;
            if (mListener != null) {
                mListener.onKeyBoardStateChange(KEYBOARD_STATE_SHOW);
            }
            Logger.d(TAG, "输入法软键盘展示.......");
        }
        if (mHasInit && mHasKeybord && mHeight == b) {
            mHasKeybord = false;
            if (mListener != null) {
                mListener.onKeyBoardStateChange(KEYBOARD_STATE_HIDE);
            }
            Logger.d(TAG, "输入法软键盘隐藏.......");
        }
    }

    public interface onImeChangeListener {
        public void onKeyBoardStateChange(int state);
    }
}
