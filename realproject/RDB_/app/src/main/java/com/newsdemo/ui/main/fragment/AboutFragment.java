package com.newsdemo.ui.main.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.newsdemo.app.App;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by jianqiang.hu on 2017/5/12.
 */

public class AboutFragment extends SupportFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TextView textView = new TextView(App.getInstance().getApplicationContext());
        textView.setText("about");
        return textView;

    }
}
