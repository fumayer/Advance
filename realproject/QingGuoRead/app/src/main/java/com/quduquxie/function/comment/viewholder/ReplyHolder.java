package com.quduquxie.function.comment.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.quduquxie.R;
import com.quduquxie.communal.widget.expression.ExpressionTextView;

/**
 * Created on 17/2/23.
 * Created by crazylei.
 */

public class ReplyHolder extends RecyclerView.ViewHolder {

    public LinearLayout comment_reply_view;
    public ExpressionTextView comment_reply_content;

    public ReplyHolder(View view) {
        super(view);
        comment_reply_view = (LinearLayout) view.findViewById(R.id.comment_reply_view);
        comment_reply_content = (ExpressionTextView) view.findViewById(R.id.comment_reply_content);
    }
}
