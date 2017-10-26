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
 * Created on 17/3/1.
 * Created by crazylei.
 */

public class UserDynamicCommentHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.user_dynamic_comment_view)
    public LinearLayout user_dynamic_comment_view;
    @BindView(R.id.user_dynamic_details)
    public TextView user_dynamic_details;
    @BindView(R.id.user_dynamic_reply)
    public ImageView user_dynamic_reply;

    @BindView(R.id.user_dynamic_avatar)
    ImageView user_dynamic_avatar;
    @BindView(R.id.user_dynamic_name)
    TextView user_dynamic_name;
    @BindView(R.id.user_dynamic_comment_content)
    ExpandExpressionTextView user_dynamic_comment_content;
    @BindView(R.id.user_dynamic_time)
    TextView user_dynamic_time;

    public UserDynamicCommentHolder(View view) {
        super(view);

        ButterKnife.bind(this, view);

    }

    public void initView(Context context, Review review, Typeface typeface) {
        if (review.sender != null) {
            if (user_dynamic_avatar != null) {
                if (!TextUtils.isEmpty(review.sender.avatar_url)) {
                    Glide.with(context)
                            .load(review.sender.avatar_url)
                            .signature(new StringSignature(review.sender.avatar_url))
                            .transform(new GlideCircleTransform(context))
                            .diskCacheStrategy(DiskCacheStrategy.RESULT)
                            .placeholder(R.drawable.icon_avatar_default)
                            .error(R.drawable.icon_avatar_default)
                            .skipMemoryCache(true)
                            .into(user_dynamic_avatar);
                } else {
                    user_dynamic_avatar.setImageResource(R.drawable.icon_avatar_default);
                }
            }

            if (user_dynamic_name != null) {
                user_dynamic_name.setText(TextUtils.isEmpty(review.sender.name) ? "青果阅读" : review.sender.name);
                user_dynamic_name.setTypeface(typeface);
            }
        }

        if (review.comments != null) {
            if (user_dynamic_comment_content != null) {
                user_dynamic_comment_content.setContent(TextUtils.isEmpty(review.comments.content) ? "您要找的书评暂时无法查看" : review.comments.content);
            }
        }

        if (user_dynamic_time != null) {
            user_dynamic_time.setText(TimeUtils.compareTime(BaseConfig.simpleDateFormat, review.create_time));
        }
    }
}
