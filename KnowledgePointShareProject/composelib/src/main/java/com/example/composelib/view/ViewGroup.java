package com.example.composelib.view;

import com.example.composelib.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunjie on 2018/4/16.
 */

public class ViewGroup extends View {
    private List<View> views = new ArrayList<>();

    public ViewGroup(String name) {
        super(name);
    }

    @Override
    public void showView() {
        LogUtil.e("ViewGroup", "14-----showView--->" + name);
        for (View view : views) {
            view.showView();
        }
    }

    @Override
    public void addView(View view) {
        views.add(view);
    }

    @Override
    public void removeView(View view) {
        views.remove(view);
    }

}
