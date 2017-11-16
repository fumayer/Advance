package com.shortmeet.www.utilsUsed;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.cunoraz.gifview.library.GifView;
import com.shortmeet.www.Base.CtrollAllactivitys;
import com.shortmeet.www.Listener.DialogCallBack;
import com.shortmeet.www.MainActivity;
import com.shortmeet.www.R;
import com.shortmeet.www.ui.PercenalCenter.activity.LoginActivity;

import static com.shortmeet.www.R.id.tv_title;


/**
 * Created by Fenglingyue on 2017/6/26.
 *  控制 显示隐藏 进度Dialog
 */

public class DialogUtils {
    public static AlertDialog mDialog;
    public static GifView gifView1;
    public  static void showProgress(Context context,String msg){
        showProgress(context,msg,false);
    }

    /**
     *对话框
     */
    public static void showAlertDialog(Context context, String msg, final DialogCallBack dialogCallBack){
        mDialog = new AlertDialog.Builder(context, R.style.Theme_Transparent).create();
        //给对话框去标题栏，记得次句要写在setContentView之前。
        // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = LayoutInflater.from(context).inflate(R.layout.item_showdialog_upload_confirm, null);
        AppCompatButton  btnNegative = (AppCompatButton) view.findViewById(R.id.btn_negative);
        AppCompatButton btnPositive = (AppCompatButton) view.findViewById(R.id.btn_positive);
        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_title.setText(msg);
        btnNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogCallBack.OnFailed(mDialog);
            }
        });
        btnPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogCallBack.OnSuccess(mDialog);
            }
        });
        mDialog.setView(view);
        mDialog.show();
    }


    // Fenglingyue 注： 显示 文字 ，点击外部是否可以取消
    public static void showProgress(Context context,String msg,boolean isCancel){
        View view= LayoutInflater.from(context).inflate(R.layout.item_img_dialog_loading,null);
        TextView textView = (TextView) view.findViewById(R.id.tv_loading);
        gifView1 = (GifView) view.findViewById(R.id.gif1);
        gifView1.setGifResource(R.drawable.bigload);
        gifView1.play();
        textView.setText(msg);
        if(mDialog==null){
           mDialog=new AlertDialog.Builder(context, R.style.AlertTrans)
                   .setView(view)
                   //.setTitle(msg)
                   .setCancelable(isCancel)
                   .setOnDismissListener(new DialogInterface.OnDismissListener() {
                       @Override
                       public void onDismiss(DialogInterface dialogInterface) {
                            mDialog=null;
                       }
                   })
                   .setOnCancelListener(new DialogInterface.OnCancelListener() {
                       @Override
                       public void onCancel(DialogInterface dialogInterface) {
                           mDialog=null;
                       }
                   })
                   .show();
       }
    }

    public static void showLoading(Context context){
        View view= LayoutInflater.from(context).inflate(R.layout.item_img_dialog_loading,null);
        mDialog=new AlertDialog.Builder(context,R.style.AlertTrans)
                .setView(view)
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        mDialog=null;
                    }
                })
                .show();
    }

    public static  void  closeProgress(){
        if(gifView1==null){
            return;
        }
        if (gifView1.isPlaying()) {
            gifView1.pause();
        }
        if(mDialog!=null&&mDialog.isShowing()){
            try {
                mDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
            mDialog=null;
        }
    }


//
//    /*
//     *  Fly 注：提示用户登录身份过期
//     */
    public static void reLoginAct(final Fragment fragment, final Context context, String msg, boolean isCancel){
          final AlertDialog mDiag;
          TextView tvTitle;
          TextView tvTopTitle;
          View viewdivider;
          AppCompatButton btnNegative;
          AppCompatButton btnPositive;
        View view = LayoutInflater.from(context).inflate(R.layout.item_showdialog_upload_confirm, null);
        tvTitle = (TextView) view.findViewById(tv_title);
        tvTopTitle= (TextView) view.findViewById(R.id.tv_toptitle);
        viewdivider=view.findViewById(R.id.line);
        tvTitle.setText(msg);
        tvTitle.setTextSize(12.0f);
        tvTopTitle.setVisibility(View.VISIBLE);
        viewdivider.setVisibility(View.GONE);
        tvTitle.setTextColor(UiUtils.getColor(R.color.black));
        btnNegative = (AppCompatButton) view.findViewById(R.id.btn_negative);
        btnPositive = (AppCompatButton) view.findViewById(R.id.btn_positive);
        btnNegative.setVisibility(View.GONE);
        mDiag = new AlertDialog.Builder(context, R.style.Theme_Transparent).create();
        mDiag .setCancelable(isCancel);
        mDiag .setView(view);
        mDiag .show();
        btnPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserUtils.clearUser(context);
                fragment.startActivity(new Intent(context, LoginActivity.class));
                mDiag.cancel();
                CtrollAllactivitys.getActivity(MainActivity.class).finish();
            }
        });
    }

    public static void reLoginAct(final Activity activity, final Context context, String msg, boolean isCancel){
        final AlertDialog mDiag;
        TextView tvTitle;
        TextView tvTopTitle;
        View viewdivider;
        AppCompatButton btnNegative;
        AppCompatButton btnPositive;
        View view = LayoutInflater.from(context).inflate(R.layout.item_showdialog_upload_confirm, null);
        tvTitle = (TextView) view.findViewById(tv_title);
        tvTopTitle= (TextView) view.findViewById(R.id.tv_toptitle);
        viewdivider=view.findViewById(R.id.line);
        tvTitle.setText(msg);
        tvTitle.setTextSize(12.0f);
        tvTopTitle.setVisibility(View.VISIBLE);
        viewdivider.setVisibility(View.GONE);
        tvTitle.setTextColor(UiUtils.getColor(R.color.black));
        btnNegative = (AppCompatButton) view.findViewById(R.id.btn_negative);
        btnPositive = (AppCompatButton) view.findViewById(R.id.btn_positive);
        btnNegative.setVisibility(View.GONE);
        mDiag = new AlertDialog.Builder(context, R.style.Theme_Transparent).create();
        mDiag .setCancelable(isCancel);
        mDiag .setView(view);
        mDiag .show();
        btnPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserUtils.clearUser(context);
                activity.startActivity(new Intent(context, LoginActivity.class));
                mDiag.cancel();
                CtrollAllactivitys.getActivity(MainActivity.class).finish();
            }
        });
    }



}
