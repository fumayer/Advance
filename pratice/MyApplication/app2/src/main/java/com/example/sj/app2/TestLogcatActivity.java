package com.example.sj.app2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class TestLogcatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_logcat);
        LogUtil.v("TestTag", "14-----onCreate--->");
        LogUtil.d("TestTag", "14-----onCreate--->");
        LogUtil.i("TestTag", "14-----onCreate--->");
        LogUtil.w("TestTag", "14-----onCreate--->");
        LogUtil.e("TestTag", "14-----onCreate--->");
        LogUtil.v("NewTestTag", "14-----onCreate--->");
        LogUtil.d("NewTestTag", "14-----onCreate--->");
        LogUtil.i("NewTestTag", "14-----onCreate--->");
        LogUtil.w("NewTestTag", "14-----onCreate--->");
        LogUtil.e("NewTestTag", "14-----onCreate--->");
    }

    public void go1(View view) {
        Toast.makeText(this, "触发go1", Toast.LENGTH_SHORT).show();
        LogUtil.v("TestTag", "23-----go1--->");

    }

    public void go2(View view) {
        Toast.makeText(this, "触发go2", Toast.LENGTH_SHORT).show();
        LogUtil.d("TestTag", "29-----go2--->");

    }

    public void go3(View view) {
        Toast.makeText(this, "触发go3", Toast.LENGTH_SHORT).show();
        LogUtil.i("TestTag", "35-----go3--->");
    }

    public void go4(View view) {
        Toast.makeText(this, "触发go4", Toast.LENGTH_SHORT).show();
        LogUtil.w("TestTag", "40-----go4--->");
    }

    public void go5(View view) {
        Toast.makeText(this, "触发go5", Toast.LENGTH_SHORT).show();
        LogUtil.e("TestTag", "45-----go5--->");

    }

    public void go6(View view) {
        Toast.makeText(this, "触发go6", Toast.LENGTH_SHORT).show();
        LogUtil.v("NewTestTag", "51-----go6--->");

    }

    public void go7(View view) {
        Toast.makeText(this, "触发go7", Toast.LENGTH_SHORT).show();
        LogUtil.d("NewTestTag", "57-----go7--->");
    }

    public void go8(View view) {
        Toast.makeText(this, "触发go8", Toast.LENGTH_SHORT).show();
        LogUtil.i("NewTestTag", "62-----go8--->");
    }

    public void go9(View view) {
        Toast.makeText(this, "触发go9", Toast.LENGTH_SHORT).show();
        LogUtil.w("NewTestTag", "67-----go9--->");
    }

    public void go10(View view) {
        Toast.makeText(this, "触发go10", Toast.LENGTH_SHORT).show();
        LogUtil.e("NewTestTag", "72-----go10--->");
    }

    public void go11(View view) {
        Toast.makeText(this, "触发go11", Toast.LENGTH_SHORT).show();
    }

    public void go12(View view) {
        Toast.makeText(this, "触发go12", Toast.LENGTH_SHORT).show();
    }

    public void go13(View view) {
        Toast.makeText(this, "触发go13", Toast.LENGTH_SHORT).show();
    }

    public void go14(View view) {
        Toast.makeText(this, "触发go14", Toast.LENGTH_SHORT).show();
    }

    public void go15(View view) {
        Toast.makeText(this, "触发go15", Toast.LENGTH_SHORT).show();

    }

    public void go16(View view) {
        Toast.makeText(this, "触发go16", Toast.LENGTH_SHORT).show();

    }

    public void go17(View view) {
        Toast.makeText(this, "触发go17", Toast.LENGTH_SHORT).show();
    }

    public void go18(View view) {
        Toast.makeText(this, "触发go18", Toast.LENGTH_SHORT).show();
    }

    public void go19(View view) {
        Toast.makeText(this, "触发go19", Toast.LENGTH_SHORT).show();
    }

    public void go20(View view) {
        Toast.makeText(this, "触发go20", Toast.LENGTH_SHORT).show();
    }

    public void go21(View view) {
        Toast.makeText(this, "触发go21", Toast.LENGTH_SHORT).show();
    }

    public void go22(View view) {
        Toast.makeText(this, "触发go22", Toast.LENGTH_SHORT).show();
    }

    public void go23(View view) {
        Toast.makeText(this, "触发go23", Toast.LENGTH_SHORT).show();
    }

    public void go24(View view) {
        Toast.makeText(this, "触发go24", Toast.LENGTH_SHORT).show();
    }
}
