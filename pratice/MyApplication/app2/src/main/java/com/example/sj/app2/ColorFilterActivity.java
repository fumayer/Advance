package com.example.sj.app2;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class ColorFilterActivity extends AppCompatActivity implements View.OnTouchListener {
    private ImageView img1;
    private ImageView img2;
    private ImageView img3;
    private ImageView img4;
    private ImageView img;

    private SJText tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_filter);
        img = (ImageView) findViewById(R.id.img);
        img2 = (ImageView) findViewById(R.id.img2);
        img3 = (ImageView) findViewById(R.id.img3);
        img4 = (ImageView) findViewById(R.id.img4);
        tv = (SJText) findViewById(R.id.tv);
        tv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                LogUtil.e("SJText", "35-----onTouch--->" + event.getAction());
                return false;
            }
        });
        img.setOnTouchListener(this);
    }

    public void go1(View view) {
        Toast.makeText(this, "触发go1", Toast.LENGTH_SHORT).show();
        img.setColorFilter(getResources().getColor(R.color.colorPrimary));
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


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        LogUtil.e("ColorFilterActivity", "48-----onTouch--->" + event.getAction());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

//                img.setColorFilter(getResources().getColor(R.color.colorPrimary));
//                img.setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.XOR);
//                img.setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
//                img.setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC);
//                img.setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.DST);
//                img.setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.DARKEN);
//                img.setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.MULTIPLY);
                img.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
//                img.setColorFilter(getResources().getColor(R.color.colorAccent));
//                img.setColorFilter(Color.GRAY, PorterDuff.Mode.LIGHTEN);
                img.clearColorFilter();
                break;
            default:
                break;

        }
        return false;
    }
}
