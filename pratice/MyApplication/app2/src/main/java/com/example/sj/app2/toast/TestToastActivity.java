package com.example.sj.app2.toast;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.sj.app2.R;

public class TestToastActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_toast);
    }

    public void go1(View view) {
        CustomToast.showToast(this, "春眠不觉晓\n处处闻啼鸟\n夜来风雨声\n花落知多少");
    }

    public void go2(View view) {
//        CustomToast toast = new CustomToast(this);
//        toast.setText("我就是我");
//        toast.show();
        CustomToast.showToast(this, "我就是我");

    }

    public void go3(View view) {
//        CustomToast toast = new CustomToast(this);
//        toast.setText("你为什么不说话");
//        toast.show();
        CustomToast.showToast(this, "你为什么不说话");

    }

    public void go4(View view) {
        CustomToast.showToast(this, "春眠不觉晓,处处闻啼鸟,夜来风雨声,花落知多少.春眠不觉晓,处处闻啼鸟,夜来风雨声,花落知多少.春眠不觉晓,处处闻啼鸟,夜来风雨声,花落知多少");

    }

    public void go5(View view) {
//        new ImageToast(this, R.mipmap.ic_launcher_round).show();
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(500, 500);

        ImageToast imageToast = new ImageToast.Builder(this)
                .setDrawable(R.drawable.claw_doll_success_default)
                .setGravity(Gravity.CENTER_HORIZONTAL)
                .setDuration(Toast.LENGTH_LONG)
                .setLayoutParams(lp)
                .create();
        imageToast.show();
    }

    public void go6(View view) {
//        new ImageToast(this, R.mipmap.ic_launcher).show();
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(200, 500);
        lp.setMargins(300, 400, 0, 0);
        ToastImage.build(new ToastImage.Builder(this)
                .setDuration(Toast.LENGTH_LONG)
                .setDrawable(R.drawable.claw_doll_success_default)
                .setGravity(Gravity.CENTER_HORIZONTAL)
                .setLp(lp)).show();

    }

    public void go7(View view) {
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(400, 500);
        lp.setMargins(200, 400, 0, 0);
        ToastImage.build(new ToastImage.Builder(this)
                .setDuration(Toast.LENGTH_LONG)
                .setDrawable(R.drawable.claw_doll_success_default)
                .setGravity(Gravity.CENTER_HORIZONTAL)
                .setLp(lp)).show();

    }

    public void go8(View view) {
        Toast.makeText(this, "触发go8", Toast.LENGTH_SHORT).show();
    }
}
