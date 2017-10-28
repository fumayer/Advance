package com.example.dzh.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.dzh.myapplication.tubatu.ScalePageTransformer;
import com.example.dzh.myapplication.tubatu.VpAdapter;
import com.example.dzh.myapplication.tubatu2.TuBaTuActivity;

public class MainActivity extends AppCompatActivity {
    private ViewPager mVp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mVp = (ViewPager) findViewById(R.id.vp);
        mVp.setAdapter(new VpAdapter(this));
        mVp.setPageTransformer(true, new ScalePageTransformer());

    }

    public void go1(View view) {
        Toast.makeText(this, "触发go1", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, TuBaTuActivity.class));

    }

    public void go2(View view) {
        Toast.makeText(this, "触发go2", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, TestFinishActivity.class));

    }

    public void go3(View view) {
        Toast.makeText(this, "触发go3", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, TestShapeActivity.class));

    }

    public void go4(View view) {
        Toast.makeText(this, "触发go4", Toast.LENGTH_SHORT).show();
    }
}
