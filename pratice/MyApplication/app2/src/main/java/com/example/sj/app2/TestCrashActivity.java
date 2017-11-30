package com.example.sj.app2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class TestCrashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_crash);
    }

    public void go1(View view) {
        Toast.makeText(this, "触发go1", Toast.LENGTH_SHORT).show();
        new Thread() {
            @Override
            public void run() {
                super.run();
                LogUtil.e("TestCrashActivity", "19-----go1--->" + 1 / 0);
            }
        }.start();
//        LogUtil.e("TestCrashActivity", "19-----go1--->" + 1 / 0);

    }

    public void go2(View view) {
        Toast.makeText(this, "触发go2", Toast.LENGTH_SHORT).show();
        new Thread() {
            @Override
            public void run() {
                super.run();
//                Looper.prepare();
                Toast.makeText(TestCrashActivity.this, "就是要弹出", Toast.LENGTH_SHORT).show();
//                Looper.loop();
            }
        }.start();
    }

    public void go3(View view) {
        Toast.makeText(this, "触发go3", Toast.LENGTH_SHORT).show();
    }

    public void go4(View view) {
        Toast.makeText(this, "触发go4", Toast.LENGTH_SHORT).show();
    }
}
