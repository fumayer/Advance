package com.example.dzh.myapplication;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class TestShapeActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_shape);
        mTv = (TextView) findViewById(R.id.tv);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void go1(View view) {
        Toast.makeText(this, "触发go1", Toast.LENGTH_SHORT).show();
        int[][] states = new int[][]{
                new int[]{-android.R.attr.state_checked},
                new int[]{android.R.attr.state_checked}
        };

        int[] colors = new int[]{getResources().getColor(R.color.colorAccent),
                getResources().getColor(R.color.colorPrimary)
        };
        ColorStateList csl = new ColorStateList(states, colors);

        mTv.setBackgroundTintList(csl);
    }


    private TextView mTv;

    public void go2(View view) {
        Toast.makeText(this, "触发go2", Toast.LENGTH_SHORT).show();
        GradientDrawable myGrad = (GradientDrawable)mTv.getBackground();
        myGrad.setColor(getResources().getColor(R.color.red));
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void go3(View view) {
        Toast.makeText(this, "触发go3", Toast.LENGTH_SHORT).show();
        GradientDrawable myGrad = (GradientDrawable)mTv.getBackground();
//         生成的是混合色
        myGrad.setColors(new int[]{R.color.blue,R.color.red});
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)   /* 16 */
    public void go4(View view) {
        Toast.makeText(this, "触发go4", Toast.LENGTH_SHORT).show();
        int colors[] = { 0xff255779 , 0xff3e7492, 0xffa6c0cd };//分别为开始颜色，中间夜色，结束颜色

        GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors);

        int strokeWidth = 5; // 3dp 边框宽度
        int roundRadius = 15; // 8dp 圆角半径
        int strokeColor = Color.parseColor("#2E3135");//边框颜色
        int fillColor = Color.parseColor("#DFDFE0");//内部填充颜色

//        gd.setColor(fillColor);
        gd.setCornerRadius(roundRadius);
        gd.setStroke(strokeWidth, strokeColor);
        mTv.setBackground(gd);
    }
}
