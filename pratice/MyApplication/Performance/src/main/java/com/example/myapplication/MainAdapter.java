package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by sunjie on 2018/1/24.
 */

public class MainAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    private Context context;

    public MainAdapter(int layoutResId, @Nullable List<String> data, Context context) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, final String item) {
        TextView view = helper.getView(R.id.tv_item);
        if (view != null) {
            view.setText(item);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (item) {
                        case "for循环性能测试":
                            context.startActivity(new Intent(context, ForCicleActivity.class));
                            break;
                        default:
                            break;
                    }
                }
            });
        }
    }
}
