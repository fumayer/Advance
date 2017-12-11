package com.example.sj.app2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class TestSwitchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_switch);
    }

    private int i = 4;

    public void go1(View view) {
        Toast.makeText(this, "触发go1", Toast.LENGTH_SHORT).show();

        switch (i) {
            default:
                LogUtil.e("TestSwitchActivity", "34-----go1--->" + "break");
                break;
            case 1:
                LogUtil.e("TestSwitchActivity", "23-----go1--->" + 1);
                break;
            case 2:
                LogUtil.e("TestSwitchActivity", "23-----go1--->" + 2);
                break;
            case 3:
                LogUtil.e("TestSwitchActivity", "23-----go1--->" + 3);
                break;

        }
    }

    public void go2(View view) {
        Toast.makeText(this, "触发go2", Toast.LENGTH_SHORT).show();
    }

    public void go3(View view) {
        Toast.makeText(this, "触发go3", Toast.LENGTH_SHORT).show();
    }

    public void go4(View view) {
        Toast.makeText(this, "触发go4", Toast.LENGTH_SHORT).show();
    }
}
