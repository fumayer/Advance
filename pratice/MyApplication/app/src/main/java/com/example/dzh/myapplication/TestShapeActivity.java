package com.example.dzh.myapplication;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
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
        GradientDrawable myGrad = (GradientDrawable) mTv.getBackground();
        myGrad.setColor(getResources().getColor(R.color.red));
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void go3(View view) {
        Toast.makeText(this, "触发go3", Toast.LENGTH_SHORT).show();
        GradientDrawable myGrad = (GradientDrawable) mTv.getBackground();
//         生成的是混合色
        myGrad.setColors(new int[]{R.color.blue, R.color.red});
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)   /* 16 */
    public void go4(View view) {
        Toast.makeText(this, "触发go4", Toast.LENGTH_SHORT).show();
        int colors[] = {0xff255779, 0xff3e7492, 0xffa6c0cd};//分别为开始颜色，中间夜色，结束颜色

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


    public void go5(View view) {
        Toast.makeText(this, "触发go5", Toast.LENGTH_SHORT).show();

        GradientDrawable normalDraw = getDrawable(R.color.blue, 100f, 0, -1);
        GradientDrawable pressDraw = getDrawable(R.color.yellow, 500f, 0, -1);
        StateListDrawable selector = getSelector(normalDraw, pressDraw);
        Button bt = (Button) view;
        bt.setBackgroundDrawable(selector);

    }

    public void go6(View view) {
        Toast.makeText(this, "触发go6", Toast.LENGTH_SHORT).show();
    }

    public void go7(View view) {
        Toast.makeText(this, "触发go7", Toast.LENGTH_SHORT).show();
    }

    public void go8(View view) {
        Toast.makeText(this, "触发go8", Toast.LENGTH_SHORT).show();
    }


    public GradientDrawable getDrawable(@ColorRes int rgb, float radius, int stroke, @ColorRes int strokeColor) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(getResources().getColor(rgb));//设置颜色
//        gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);//设置显示的样式
        gradientDrawable.setGradientType(GradientDrawable.SWEEP_GRADIENT);//设置显示的样式
        gradientDrawable.setCornerRadius(radius);//设置圆角的半径
        if (stroke != 0 && strokeColor != -1) {
            gradientDrawable.setStroke(stroke, getResources().getColor(strokeColor));//描边
        }
        return gradientDrawable;
    }


    public StateListDrawable getSelector(Drawable normalDrawable, Drawable pressDrawable) {
        StateListDrawable stateListDrawable = new StateListDrawable();
//给当前的颜色选择器添加选中图片指向状态，未选中图片指向状态
        stateListDrawable.addState(new int[]{android.R.attr.state_enabled, android.R.attr.state_pressed}, pressDrawable);
//设置默认状态
        stateListDrawable.addState(new int[]{}, normalDrawable);
        return stateListDrawable;
    }
            }
