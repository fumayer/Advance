package com.quduquxie.function.comment.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quduquxie.R;

/**
 * Created on 17/2/23.
 * Created by crazylei.
 */

public class ReplySpreadHolder extends RecyclerView.ViewHolder {

    public RelativeLayout comment_reply_spread_view;
    public TextView comment_reply_spread;

    public ReplySpreadHolder(View view) {
        super(view);
        comment_reply_spread_view = (RelativeLayout) view.findViewById(R.id.comment_reply_spread_view);
        comment_reply_spread = (TextView) view.findViewById(R.id.comment_reply_spread);
    }
}
