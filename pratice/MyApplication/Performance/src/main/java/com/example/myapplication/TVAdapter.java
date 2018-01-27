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

public class TVAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


    public TVAdapter( @Nullable List<String> data ) {
        super(R.layout.item_main, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final String item) {
        TextView view = helper.getView(R.id.tv_item);
        if (view != null) {
            view.setText(item);
        }
    }
}
