package com.example.sj.app2.toast;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sj.app2.R;

import org.w3c.dom.Text;

/**
 * Created by sunjie on 2018/1/22.
 */

public class CustomToast extends Toast {

    private static CustomToast customToast;
    private TextView textView;

    /**
     * Construct an empty Toast object.  You must call {@link #setView} before you
     * can call {@link #show}.
     *
     * @param context The context to use.  Usually your {@link Application}
     *                or {@link Activity} object.
     */
    private CustomToast(Context context) {
        super(context);
          WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();

        View view = LayoutInflater.from(context).inflate(R.layout.toast, null, false);
        textView = view.findViewById(R.id.tv);
        setView(view);
        setDuration(Toast.LENGTH_LONG);
        setGravity(Gravity.CENTER, 0, 0);
    }

    @Override
    public void setText(CharSequence s) {
        textView.setText(s);
    }



    public static void showToast(Context context, CharSequence sequence) {
        if (customToast != null) {
            customToast.cancel();
            customToast = new CustomToast(context);
        }else{
            customToast = new CustomToast(context);
        }
        customToast.setText(sequence);
        customToast.show();

    }
}
