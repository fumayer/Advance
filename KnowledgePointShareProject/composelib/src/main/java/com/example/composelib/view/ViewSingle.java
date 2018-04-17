package com.example.composelib.view;

import com.example.composelib.LogUtil;

/**
 * Created by sunjie on 2018/4/16.
 */

public class ViewSingle extends View {
    public ViewSingle(String name) {
        super(name);
    }

    @Override
    public void showView() {
        LogUtil.e("ViewSingle","14-----showView--->"+name);
    }

    @Override
    public void addView(View view) {

    }

    @Override
    public void removeView(View view) {

    }
}
