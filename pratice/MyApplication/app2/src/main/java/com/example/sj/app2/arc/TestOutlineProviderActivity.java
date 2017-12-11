package com.example.sj.app2.arc;

import android.graphics.Outline;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;

import com.example.sj.app2.R;

public class TestOutlineProviderActivity extends AppCompatActivity {

    private ImageView imageView;
    //1. 自定义一个轮廓提供者
    private ViewOutlineProvider viewOutlineProvider = new ViewOutlineProvider() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void getOutline(View view, Outline outline) {
            //裁剪成一个圆形
            outline.setOval(0, 0, view.getWidth(), view.getHeight());
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_outline_provider);

        imageView = (ImageView) findViewById(R.id.iv);
        imageView.setOutlineProvider(viewOutlineProvider);//把自定义的轮廓提供者设置给imageView
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void btnClick(View view) {
        if (imageView.getClipToOutline()) {
            imageView.setClipToOutline(false);//关闭裁剪
        } else {
            imageView.setClipToOutline(true);//开启裁剪
        }

    }
}
