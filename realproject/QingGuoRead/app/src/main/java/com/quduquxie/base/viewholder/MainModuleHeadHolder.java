package com.quduquxie.base.viewholder;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quduquxie.R;
import com.quduquxie.base.bean.MainContentItem;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 17/7/24.
 * Created by crazylei.
 */

public class MainModuleHeadHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.module_head)
    public RelativeLayout module_head;
    @BindView(R.id.module_head_title)
    TextView module_head_title;
    @BindView(R.id.module_head_more)
    TextView module_head_more;

    public MainModuleHeadHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

    public void initializeView(MainContentItem mainContentItem) {
        module_head_title.setText(TextUtils.isEmpty(mainContentItem.title) ? "编辑推荐" : mainContentItem.title);

        if (TextUtils.isEmpty(mainContentItem.uri)) {
            module_head_more.setVisibility(View.GONE);
        } else {
            module_head_more.setVisibility(View.VISIBLE);
        }
    }
}