package com.quduquxie.module.comment.viewholder;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
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
 * Created on 17/2/28.
 * Created by crazylei.
 */

public class ReceivedReplyHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.received_reply_view)
    public LinearLayout received_reply_view;
    @BindView(R.id.received_reply_avatar)
    public ImageView received_reply_avatar;
    @BindView(R.id.received_reply_user)
    public TextView received_reply_user;
    @BindView(R.id.received_reply_details)
    public TextView received_reply_details;
    @BindView(R.id.received_reply_content)
    public ExpandExpressionTextView received_reply_content;
    @BindView(R.id.received_reply_time)
    public TextView received_reply_time;
    @BindView(R.id.received_reply_discuss)
    public ImageView received_reply_discuss;

    public ReceivedReplyHolder(View view) {
        super(view);

        ButterKnife.bind(this, view);
    }

    public void initView(Context context, Review review, Typeface typeface) {

        if (review.sender != null) {
            if (received_reply_user != null) {
                received_reply_user.setText(TextUtils.isEmpty(review.sender.name) ? "青果阅读" : review.sender.name);
                received_reply_user.setTypeface(typeface);
            }

            if (!TextUtils.isEmpty(review.sender.avatar_url)) {
                if (received_reply_avatar != null) {
                    Glide.with(context)
                            .load(review.sender.avatar_url)
                            .signature(new StringSignature(review.sender.avatar_url))
                            .transform(new GlideCircleTransform(context))
                            .diskCacheStrategy(DiskCacheStrategy.RESULT)
                            .error(R.drawable.icon_avatar_default)
                            .placeholder(R.drawable.icon_avatar_default)
                            .skipMemoryCache(true)
                            .into(received_reply_avatar);
                }
            } else {
                if (received_reply_avatar != null) {
                    received_reply_avatar.setImageResource(R.drawable.icon_avatar_default);
                }
            }
        }

        if (review.replies != null) {
            if (received_reply_content != null) {
                received_reply_content.setContent(TextUtils.isEmpty(review.replies.content) ? "您要找的评论暂时无法查看" : review.replies.content);
            }
        }

        if (received_reply_time != null) {
            received_reply_time.setText(TimeUtils.compareTime(BaseConfig.simpleDateFormat, review.create_time));
        }
    }
}
