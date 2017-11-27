package com.example.dzh.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TestTimeFormatActivity extends AppCompatActivity {
    private EditText mEt;
    private long time = 44555;
    private long oneDay = 86400000;  //  一天的毫秒值

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_time_format);
        mEt = (EditText) findViewById(R.id.et);
    }

    public void go1(View view) {
        String trim = mEt.getText().toString().trim();
        if (trim != null && !trim.equals("")) {
            time = Long.parseLong(trim);
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String d = format.format(time);
        Date date = null;
        try {
            date = format.parse(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.e("TestTimeFormatActivity", "38-----go1--->" + time);
        Log.e("TestTimeFormatActivity", "37-----go1--->" + d);
        Log.e("TestTimeFormatActivity", "38-----go1--->" + date);
        boolean b = false;
        if (b = true) {

        }
    }

    public void go2(View view) {
        String trim = mEt.getText().toString().trim();
        if (trim != null && !trim.equals("")) {
            time = Long.parseLong(trim) - oneDay;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String d = format.format(time);
        Date date = null;
        try {
            date = format.parse(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.e("TestTimeFormatActivity", "38-----go1--->" + time);
        Log.e("TestTimeFormatActivity", "37-----go1--->" + d);
        Log.e("TestTimeFormatActivity", "38-----go1--->" + date);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -1);
        String currentTime = format.format(calendar.getTime());
        Log.e("TestTimeFormatActivity", "66-----go2--->" + currentTime);

    }

    public void go3(View view) {
        Toast.makeText(this, "触发go3", Toast.LENGTH_SHORT).show();
        long l = System.currentTimeMillis();
        Log.e("TestTimeFormatActivity", "66-----go3--->" + l);

    }

    public void go4(View view) {
        Toast.makeText(this, "触发go4", Toast.LENGTH_SHORT).show();
    }
}
