package com.quduquxie.base.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.quduquxie.R;

public class DropWindow extends PopupWindow {

    private Context context;
    private View contentView;

    public DropWindow(Context context, View menu) {
        super(context);
        if (menu != null) {
            contentView = menu;
        }
        this.context = context;
        initSelectPopUpMenu(-1);
    }


    private void initSelectPopUpMenu(int menuViewID) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (menuViewID != -1) {
            contentView = layoutInflater.inflate(menuViewID, null);
        }

        this.setContentView(contentView);
        this.setWidth(context.getResources().getDimensionPixelOffset(R.dimen.width_360));
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        this.setOutsideTouchable(true);

        this.setFocusable(true);
        this.setTouchable(true);

        contentView.setFocusableInTouchMode(true);

        this.setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(R.color.color_transparent)));

        this.update();

        contentView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_MENU) && (isShowing())) {
                    dismiss();
                    return true;
                }
                return false;
            }
        });
    }

    public void recycle() {
        if (context != null) {
            context = null;
        }

        if (contentView != null) {
            contentView = null;
        }
    }
}