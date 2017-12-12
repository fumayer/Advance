package com.example.sj.app2.sync;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.sj.app2.LogUtil;
import com.example.sj.app2.R;

public class TestSyncActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_sync);
    }

    int i = 0;
    public static final String tag = "";
    Object o = new Object();

    public void go1(View view) {
        synchronized (o) {
            LogUtil.e("TestSyncActivity", "19-----go1--->" + i++);
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        LogUtil.e("TestSyncActivity", "31-----run--->");

                        sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }


    private Thread mThread;

    public void go2(View view) {
        Toast.makeText(this, "触发go2", Toast.LENGTH_SHORT).show();
        if (mThread == null || !mThread.isAlive()) {
            mThread = new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        sleep(5000);
                        LogUtil.e("TestSyncActivity", "51-----run--->" + i++);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            };
            mThread.start();
        }

    }

    public void go3(View view) {
        Toast.makeText(this, "触发go3", Toast.LENGTH_SHORT).show();
    }

    public void go4(View view) {
        Toast.makeText(this, "触发go4", Toast.LENGTH_SHORT).show();
    }
}
