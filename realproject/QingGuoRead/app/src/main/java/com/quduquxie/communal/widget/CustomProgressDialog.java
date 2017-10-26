package com.quduquxie.communal.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.InflateException;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.quduquxie.R;

/**
 * Created on 16/12/17.
 * Created by crazylei.
 */

public class CustomProgressDialog extends Dialog {

    public CustomProgressDialog(Context context) {
        this(context, R.style.CustomProgressDialog);
    }

    public CustomProgressDialog(Context context, int theme) {
        super(context, theme);
        try {
            this.setContentView(R.layout.layout_dialog_progress);
        } catch (InflateException exception) {
            exception.printStackTrace();
        }
        Window window = getWindow();

        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.gravity = Gravity.CENTER;
        window.setAttributes(layoutParams);

        setCanceledOnTouchOutside(false);
        setCancelable(false);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (!hasFocus) {
            dismiss();
        }
    }
}