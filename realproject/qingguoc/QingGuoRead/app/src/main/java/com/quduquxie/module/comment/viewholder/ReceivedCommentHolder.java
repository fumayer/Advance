package com.quduquxie.module.comment.viewholder;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.StringSignature;
import com.quduquxie.R;
import com.quduquxie.base.config.BaseConfig;
import com.quduquxie.communal.widget.GlideCircleTransform;
import com.quduquxie.communal.widget.expand.ExpandExpressionTextView;
import com.quduquxie.model.Review;
import com.quduquxie.util.TimeUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 17/3/1.
 * Created by crazylei.
 */

public class ReceivedCommentHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.received_comment_view)
    public LinearLayout received_comment_view;
    @BindView(R.id.received_comment_detail)
    public TextView received_comment_detail;
    @BindView(R.id.received_comment_avatar)
    public ImageView received_comment_avatar;
    @BindView(R.id.received_comment_user)
    public TextView received_comment_user;
    @BindView(R.id.received_comment_time)
    public TextView received_comment_time;
    @BindView(R.id.received_comment_reply)
    public ImageView received_comment_reply;
    @BindView(R.id.received_comment_content)
    public ExpandExpressionTextView received_comment_content;

    public ReceivedCommentHolder(View view) {
        super(view);

        ButterKnife.bind(this, view);

    }

    public void initView(Context context, Review review, Typeface typeface) {
        if (review.sender != null) {
            if (received_comment_avatar != null) {
                if (!TextUtils.isEmpty(review.sender.avatar_url)) {
                    Glide.with(context)
                            .load(review.sender.avatar_url)
                            .signature(new StringSignature(review.sender.avatar_url))
                            .transform(new GlideCircleTransform(context))
                            .diskCacheStrategy(DiskCacheStrategy.RESULT)
                            .error(R.drawable.icon_avatar_default)
                            .placeholder(R.drawable.icon_avatar_default)
                            .skipMemoryCache(true)
                            .into(received_comment_avatar);
                } else {
                    received_comment_avatar.setImageResource(R.drawable.icon_avatar_default);
                }
            }

            if (received_comment_user != null) {
                received_comment_user.setText(TextUtils.isEmpty(review.sender.name) ? "青果阅读" : review.sender.name);
                received_comment_user.setTypeface(typeface);
            }
        }

        if (received_comment_time != null) {
            received_comment_time.setText(TimeUtils.compareTime(BaseConfig.simpleDateFormat, review.create_time));
        }

        if (review.comments != null) {
            if (received_comment_content != null) {
                received_comment_content.setContent(TextUtils.isEmpty(review.comments.content) ? "您要找的书评暂时无法查看" : review.comments.content);
            }
        }
    }
}
