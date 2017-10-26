package com.quduquxie.read.page;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.quduquxie.R;


public class CustomListView extends ListView {

    private View read_scroll_foot;

    public CustomListView(Context context) {
        super(context);
        initView(context);
    }

    public CustomListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public CustomListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        read_scroll_foot = inflater.inflate(R.layout.layout_read_scroll_foot, null);
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        addFooterView(read_scroll_foot, null, true);
        super.setAdapter(adapter);
    }

    public void setBackground(int color) {
        read_scroll_foot.setBackgroundColor(getResources().getColor(color));
    }
}
