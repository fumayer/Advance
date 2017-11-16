package com.shortmeet.www.utilsUsed;

import android.content.Context;
import android.os.Handler;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by zym on 2017/5/3
 */

public class InputMethodUtils {

    private InputMethodUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 切换软键盘的状态
     * 如当前为收起变为弹出,若当前为弹出变为收起
     */
    public static void toggleKeyboard(EditText edittext) {
        InputMethodManager inputMethodManager = (InputMethodManager)
                edittext.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 强制隐藏输入法键盘（延迟），输入框在窗口中可能会和输入法弹出有冲突，需要延迟收起出输入法
     */
    public static void hideKeyboard(final EditText edittext) {
        final InputMethodManager inputMethodManager = (InputMethodManager)
                edittext.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (inputMethodManager.isActive()) {
                    inputMethodManager.hideSoftInputFromWindow(edittext.getWindowToken(), 0);
                }
            }
        }, 100);
    }

    /**
     * 强制显示输入法键盘（延迟）,输入框在窗口中可能会和输入法弹出有冲突，需要延迟弹出输入法
     */
    public static void showKeyboard(final EditText edittext) {
        final InputMethodManager imm = (InputMethodManager) edittext.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        new Handler().postDelayed(new Runnable() {//延迟弹出输入法
            @Override
            public void run() {
                imm.showSoftInput(edittext, InputMethodManager.SHOW_IMPLICIT);
            }
        }, 100);
    }

    /**
     * 输入法是否显示
     */
    public static boolean isActive(EditText edittext) {
        boolean bool = false;
        InputMethodManager inputMethodManager = (InputMethodManager)
                edittext.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive()) {
            bool = true;
        }
        return bool;
    }
}
