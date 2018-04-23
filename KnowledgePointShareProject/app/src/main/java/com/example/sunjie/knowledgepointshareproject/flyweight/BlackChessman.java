package com.example.sunjie.knowledgepointshareproject.flyweight;

import android.util.Log;

import com.example.sunjie.knowledgepointshareproject.LogUtil;

/**
 * Created by sunjie on 2018/4/23.
 */

public class BlackChessman extends AbsChessman {
    public BlackChessman() {
        super("BLACK");
        LogUtil.e("BlackChessman","12-----BlackChessman--->创建了一个黑子");
    }

    @Override
    protected void point(int x, int y) {
        this.x=x;
        this.y=y;
        show();
    }
}
