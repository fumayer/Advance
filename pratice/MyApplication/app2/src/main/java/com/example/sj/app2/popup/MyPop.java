package com.example.sj.app2.popup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.SeekBar;

import com.example.sj.app2.LogUtil;
import com.example.sj.app2.R;

public class MyPop extends PopupWindow {
    private SeekBar seekbar;

    public MyPop(Context context) {
        super(context);
        View inflate = LayoutInflater.from(context).inflate(R.layout.my_app, null, false);
        setContentView(inflate);
        setWidth(500);
        setHeight(200);


        seekbar = (SeekBar) inflate.findViewById(R.id.seekbar);

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                LogUtil.e("MyPop","27-----onProgressChanged--->"+progress);
                LogUtil.e("MyPop","29-----onProgressChanged--->"+fromUser);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }
}