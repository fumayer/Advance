package com.example.sunjie.knowledgepointshareproject.flyweight;

import com.example.sunjie.knowledgepointshareproject.LogUtil;

/**
 * Created by sunjie on 2018/4/23.
 */

public class WhiteChessman extends AbsChessman {
    public WhiteChessman() {
        super("WHITE");
        LogUtil.e("WhiteChessman", "10-----WhiteChessman--->创建一个白子");
    }

    @Override
    protected void point(int x, int y) {
        this.x = x;
        this.y = y;
        show();
    }


}
