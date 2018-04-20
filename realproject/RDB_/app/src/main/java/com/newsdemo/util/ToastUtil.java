package com.newsdemo.util;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.newsdemo.R;
import com.newsdemo.app.App;

/**
 * Created by jianqiang.hu on 2017/5/24.
 */

public class ToastUtil {
    static ToastUtil td;
    Context context;
    String msg;
    Toast toast;
    public static final int LENGTH_LONG=Toast.LENGTH_LONG;
    public static final int LENGTH_SHORT=Toast.LENGTH_SHORT;
    public ToastUtil(Context context){
        this.context=context;
    }


    public static void show(int resId){

    }

    public void setText(String msg){
        this.msg=msg;
    }

    public static void show(String msg,int duration){
        if (td==null) {
            td = new ToastUtil(App.getInstance());
        }
        td.setText(msg);
        td.create(duration).show();
    }

    public Toast create(int duration){
        View contentView=View.inflate(context,R.layout.dialog_toast,null);
        TextView tv= (TextView) contentView.findViewById(R.id.tv_toast_msg);
        toast=new Toast(context);
        toast.setView(contentView);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.setDuration(Toast.LENGTH_LONG);
        tv.setText(msg);
        return toast;
    }



}
