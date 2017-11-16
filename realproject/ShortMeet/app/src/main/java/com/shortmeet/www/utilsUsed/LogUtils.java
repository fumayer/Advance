package com.shortmeet.www.utilsUsed;


import android.util.Log;
/*
 *  Fly 注： 快捷打日志
 */
public class LogUtils {
    private static final boolean ENABLE = true;

    /**
     * 打印info级别的log
     */
    public static void i(Object objTag, Object objMsg) {
        if (ENABLE) {
            Log.i(getTag(objTag), getMsg(objMsg));
        }
    }

    /**
     * 打印error级别的log
     */
    public static void e(Object objTag, Object objMsg) {
        if (ENABLE) {
            Log.e(getTag(objTag), getMsg(objMsg));
        }
    }

    /**
     * 打印错误信息的log
     */
    public static void e(Object objTag, Object objMsg, Throwable e) {
        if (ENABLE) {
            Log.e(getTag(objTag), getMsg(objMsg), e);
        }
    }

    /**
     * 获取Tag
     */
    private static String getTag(Object objTag) {
        String tag;
        if (objTag == null) {
            tag = "null";
        } else if (objTag instanceof Class<?>) {
            tag = ((Class<?>) objTag).getSimpleName();
        } else if (objTag instanceof String) {
            tag = (String) objTag;
        } else {
            tag = objTag.getClass().getSimpleName();
        }
        return "yxl" + tag + "_";
    }

    /**
     * 获取要打印的信息
     */
    private static String getMsg(Object objMsg) {
        String msg;
        if (objMsg == null) {
            msg = (String) objMsg;
        } else {
            msg = String.valueOf(objMsg);
        }
        return msg;
    }
}
