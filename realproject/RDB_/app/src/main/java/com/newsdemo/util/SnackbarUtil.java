package com.newsdemo.util;

import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by jianqiang.hu on 2017/5/11.
 */

public class SnackbarUtil {
    public static void show(View view ,String msg){
        Snackbar.make(view,msg,Snackbar.LENGTH_LONG).show();
    }


    public static void showShort(View view, String msg) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show();
    }
}
