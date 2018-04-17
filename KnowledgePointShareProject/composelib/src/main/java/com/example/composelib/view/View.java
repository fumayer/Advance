package com.example.composelib.view;

/**
 * Created by sunjie on 2018/4/16.
 */

public abstract class View {
    String name;

    public View(String name) {
        this.name = name;
    }

    public abstract void showView();

    public abstract void addView(View view);

    public abstract void removeView(View view);
}
