package com.example.dzh.myapplication.tubatu2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.dzh.myapplication.R;
import com.example.dzh.myapplication.tubatu.ScalePageTransformer;

public class TuBaTuActivity extends AppCompatActivity {
    private ClipViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tu_ba_tu);
        mViewPager = (ClipViewPager) findViewById(R.id.viewPager);
        mViewPager.setPageTransformer(true,new ScalePageTransformer());
        mViewPager.setAdapter(new ClipPagerAdapter(this));
    }
}
