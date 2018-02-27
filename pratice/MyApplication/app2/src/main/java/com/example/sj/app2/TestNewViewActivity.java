package com.example.sj.app2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class TestNewViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_new_view);
    }

    public void go1(View view) {

        new Thread() {
            @Override
            public void run() {
                super.run();
                TextView textView = new TextView(TestNewViewActivity.this);
            }
        }.start();
    }

    public void go2(View view) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                Toast toast = new Toast(TestNewViewActivity.this);
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
