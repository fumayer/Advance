package com.quduquxie.communal.widget;

import android.app.Activity;
import android.app.Dialog;
import android.view.InflateException;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.quduquxie.R;


public class CustomDialog extends Dialog {

    public CustomDialog(Activity activity, int layout) {
        super(activity, R.style.update_dialog);
        try {
            setContentView(layout);
        } catch (InflateException e) {
            e.printStackTrace();
        }
        setCanceledOnTouchOutside(false);
    }

    public CustomDialog(Activity activity, int layout, int gravity) {
        this(activity, layout, gravity, true, true);
    }

    public CustomDialog(Activity activity, int layout, int gravity, boolean isShow, boolean isBack) {
        super(activity, R.style.update_dialog);
        try {
            setContentView(layout);
        } catch (InflateException e) {
            e.printStackTrace();
        }
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.gravity = gravity;
        window.setAttributes(params);
        setCanceledOnTouchOutside(isShow);
        setCancelable(isBack);
    }
}
