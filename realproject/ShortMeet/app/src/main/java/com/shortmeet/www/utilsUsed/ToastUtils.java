package com.shortmeet.www.utilsUsed;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by Fenglingyue on 2017/6/26.
 */
   /*
    *  Fenglingyue 注： 土司工具类
    */
public class ToastUtils {
    private static Toast mToast=null;

   /*
    *  Fenglingyue 注：方法一
     *  当前没有提示信息时正常执行；当前有提示信息时新信息覆盖原来的信息。
    */
   // 短时间显示的工具类此时设置的是中间弹出
    public static void showShort(Context context,CharSequence message){
        if(null==mToast){ //用于判断是否已有Toast执行
            mToast=Toast.makeText(context,message,Toast.LENGTH_SHORT);//正常执行
          //  mToast.setGravity(Gravity.BOTTOM,0,0);
        }else{
            mToast.setText(message); //用于覆盖前面未消失的提示信息
        }
           mToast.show();
    }
    // 长时间显示的工具类此时设置的是中间弹出
    public static  void showLong(Context context,CharSequence message){
         if(null==mToast){
             mToast=Toast.makeText(context,message,Toast.LENGTH_LONG);
             mToast.setGravity(Gravity.CENTER,0,0);
         }else{
             mToast.setText(message);
         }
          mToast.show();
    }

    // 自定义Toast的 显示 时间
    public static void show(Context context, CharSequence message, int duration) {
        if (null == mToast) {
            mToast = Toast.makeText(context, message, duration);
            mToast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            mToast.setText(message);
        }
        mToast.show();
    }
    // Fenglingyue 注： 取消吐司通知
    public static void hideToast() {
        if (null != mToast) {
            mToast.cancel();
        }
    }

    /*
     * Fenglingyue 注：方法二
     *  当前没有提示信息时正常执行；当前有提示信息时新信息覆盖原来的信息。
     */
    private static String oldMsg;
    private static long oneTime = 0;
    private static long twoTime = 0;
    public static void showToast(Context context, String s) {
        if (mToast == null) {
            mToast = Toast.makeText(context, s, Toast.LENGTH_SHORT);
            mToast.show();
            oneTime = System.currentTimeMillis();
        } else {
            twoTime = System.currentTimeMillis();
            if (s.equals(oldMsg)) {
                if (twoTime - oneTime > Toast.LENGTH_SHORT) {
                    mToast.show();
                }
            } else {
                oldMsg = s;
                mToast.setText(s);
                mToast.show();
            }
        }
        oneTime = twoTime;
    }
    public static void showToast(Context context, int resId) {
        showToast(context, context.getString(resId));
    }
}
