package com.example.sj.app2.toast;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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
        Toast.makeText(this, "触发go5", Toast.LENGTH_SHORT).show();
    }

    public void go6(View view) {
        Toast.makeText(this, "触发go6", Toast.LENGTH_SHORT).show();
    }

    public void go7(View view) {
        Toast.makeText(this, "触发go7", Toast.LENGTH_SHORT).show();
    }

    public void go8(View view) {
        Toast.makeText(this, "触发go8", Toast.LENGTH_SHORT).show();
    }
}
