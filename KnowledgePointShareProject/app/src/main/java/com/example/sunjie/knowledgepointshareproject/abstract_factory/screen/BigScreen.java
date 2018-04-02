package com.example.sunjie.knowledgepointshareproject.abstract_factory.screen;

import com.example.sunjie.knowledgepointshareproject.LogUtil;

/**
 * Created by sunjie on 2018/4/2.
 */

public class BigScreen implements Screen {
    @Override
    public void look() {
        LogUtil.e("BigScreen","10-----look--->霸气大屏幕");
    }
}
