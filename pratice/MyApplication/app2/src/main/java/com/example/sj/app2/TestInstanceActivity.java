package com.example.sj.app2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class TestInstanceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_instance);
    }


    public void go1(View view) {
        Toast.makeText(this, "触发go1", Toast.LENGTH_SHORT).show();
        int i = 123;
        load(i);

    }

    public void go2(View view) {
        Toast.makeText(this, "触发go2", Toast.LENGTH_SHORT).show();
        String s = "string";
        load(s);
    }

    public void go3(View view) {
        Toast.makeText(this, "触发go3", Toast.LENGTH_SHORT).show();
        boolean b = false;
        load(b);
    }

    public void go4(View view) {
        Toast.makeText(this, "触发go4", Toast.LENGTH_SHORT).show();
        char c = 'A';
        load(c);
    }

    private void load(Object object) {
        if (object instanceof Integer) {
            int i = (int) object;
            LogUtil.e("TestInstanceActivity", "36-----load--->int    " + i);
        } else if (object instanceof String) {
            String s = (String) object;
            LogUtil.e("TestInstanceActivity", "40-----load--->String  " + s);
        } else if (object instanceof Boolean) {
            boolean b = (boolean) object;
            LogUtil.e("TestInstanceActivity", "42-----load--->Boolean   " + b);
        } else {
            LogUtil.e("TestInstanceActivity", "45-----load--->" + object);
        }

    }
}
