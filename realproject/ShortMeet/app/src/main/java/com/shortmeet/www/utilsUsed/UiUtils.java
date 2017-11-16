package com.shortmeet.www.utilsUsed;

/**
 * Created by SHM on 2016/09/23.
 */

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;

import com.shortmeet.www.Base.BaseApplication;

/**
 * UI工具类,封装了常用的UI操作
 */
public class UiUtils {


    /**
     * 让runnable的run方法中代码到UI线程中执行的方法
     */
    public static void runOnSubThread(Runnable r) {
        new Thread(r).start();
    }


    /**
     * 获取Resources对象
     */
    public static Resources getResources() {
        return BaseApplication.getInstance().getResources();
    }

    /**
     * dp转换成dx
     */
    public static int dp2px(float dp) {
        float density = getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5);
    }

    /**
     * 根据id获取vulues/strings.xml中的字符串
     */
    public static String getString(int id) {
        return getResources().getString(id);
    }

    /**
     * 根据资源id获取res下的图片资源
     */
    public static Drawable getDrawable(int id) {
        return getResources().getDrawable(id);
    }

    /**
     * 根据资源id获取res/values/colors.xml文件中的Color
     */
    public static int getColor(int id) {
        return getResources().getColor(id);
    }

    /**
     * 弹出popupWindow变暗
     */
    public static void turnDark(Activity context) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = 0.6f;
        context.getWindow().setAttributes(lp);
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    /**
     * 弹出popupWindow变暗, 可自定义暗度
     */
    public static void turnDark(Activity context, float dark) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = dark;
        context.getWindow().setAttributes(lp);
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    /**
     * popupWindow消失的时候恢复
     */
    public static void recover(Activity context) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = 1.0f;
        context.getWindow().setAttributes(lp);
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    @SuppressWarnings("static-access")
    public static float dp2Px(Context context, float value) {
        if (context == null) {
            return 0;
        }
        TypedValue typedValue = new TypedValue();
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return typedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, metrics);
    }

    @SuppressWarnings("static-access")
    public static float sp2Px(Context context, float value) {
        TypedValue typedValue = new TypedValue();
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return typedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, value, metrics);
    }

    public static int dpToPx(float dp, Resources resources) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
        return (int) px;
    }

}
