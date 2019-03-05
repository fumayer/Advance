package com.example.animation.frame;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.animation.R;

public class FrameAnimationActivity extends AppCompatActivity {
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame_animation);
        img = (ImageView) findViewById(R.id.img);
        AnimationDrawable animDraw = (AnimationDrawable) img.getDrawable();
        animDraw.start();
    }
}
