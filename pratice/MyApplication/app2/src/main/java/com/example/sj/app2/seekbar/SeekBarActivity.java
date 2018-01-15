package com.example.sj.app2.seekbar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.sj.app2.R;

public class SeekBarActivity extends AppCompatActivity {
    private MSeekbar mSeekbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seek_bar);
        mSeekbar = (MSeekbar) findViewById(R.id.m_seekbar);
    }
}
