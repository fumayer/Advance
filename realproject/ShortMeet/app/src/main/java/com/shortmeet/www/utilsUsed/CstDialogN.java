package com.shortmeet.www.utilsUsed;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;

import com.shortmeet.www.R;


public class CstDialogN {

    private Activity activity;
    private View view, cancelview;

    public CstDialogN(Activity activity, View view) {
        this.activity = activity;
        this.view = view;
    }

    public CstDialogN(Activity activity, View view, View cancelview) {
        this.activity = activity;
        this.view = view;
        this.cancelview = cancelview;
    }

    public Dialog show() {
        final Dialog dialog = new Dialog(activity, R.style.CustomDialog );
        dialog.setContentView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        // 设置显示动画
        window.setWindowAnimations(R.style.bottom_menu_anim_style);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = activity.getWindowManager().getDefaultDisplay().getHeight();
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = LayoutParams.MATCH_PARENT;
        wl.height = LayoutParams.WRAP_CONTENT;

        // 设置显示位置
        dialog.onWindowAttributesChanged(wl);
        // 设置点击外围解散
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        if (cancelview != null) {
            cancelview.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }
        return dialog;
    }


}
