package com.example.sunjie.knowledgepointshareproject.flyweight;

import com.example.sunjie.knowledgepointshareproject.LogUtil;

/**
 * Created by sunjie on 2018/4/23.
 */

public abstract class AbsChessman {
    protected String color;
    protected int x, y;

    public AbsChessman(String color) {
        this.color = color;
    }

    protected abstract void point(int x, int y);

    public void show() {
        LogUtil.e("AbsChessman", "color:" + color + "      x:" + x + "    y:" + y);
    }
}
