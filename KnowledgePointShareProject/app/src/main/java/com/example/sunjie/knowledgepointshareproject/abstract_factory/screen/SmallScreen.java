package com.example.sunjie.knowledgepointshareproject.abstract_factory.screen;

import com.example.sunjie.knowledgepointshareproject.LogUtil;

/**
 * Created by sunjie on 2018/4/2.
 */

public class SmallScreen implements Screen {
    @Override
    public void look() {
        LogUtil.e("SmallScreen","10-----look--->经典小屏");
    }
}
