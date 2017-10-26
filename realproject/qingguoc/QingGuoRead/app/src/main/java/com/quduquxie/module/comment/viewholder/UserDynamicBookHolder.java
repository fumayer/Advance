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
import com.quduquxie.model.Review;
import com.quduquxie.util.TimeUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 17/3/1.
 * Created by crazylei.
 */

public class UserDynamicBookHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.user_dynamic_book_view)
    public LinearLayout user_dynamic_book_view;
    @BindView(R.id.user_dynamic_details)
    public TextView user_dynamic_details;

    @BindView(R.id.user_dynamic_avatar)
    ImageView user_dynamic_avatar;
    @BindView(R.id.user_dynamic_name)
    TextView user_dynamic_name;
    @BindView(R.id.user_dynamic_book_cover)
    ImageView user_dynamic_book_cover;
    @BindView(R.id.user_dynamic_book_name)
    TextView user_dynamic_book_name;
    @BindView(R.id.user_dynamic_book_author)
    TextView user_dynamic_book_author;
    @BindView(R.id.user_dynamic_book_category)
    TextView user_dynamic_book_category;
    @BindView(R.id.user_dynamic_time)
    TextView user_dynamic_time;
    @BindView(R.id.user_dynamic_reply)
    ImageView user_dynamic_reply;

    public UserDynamicBookHolder(View view) {
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

            if (user_dynamic_book_author != null) {
                user_dynamic_book_author.setText(TextUtils.isEmpty(review.sender.name) ? "青果阅读" : review.sender.name);
            }
        }

        if (review.book != null) {
            if (user_dynamic_book_cover != null) {
                if (!TextUtils.isEmpty(review.book.image_url)) {
                    Glide.with(context)
                            .load(review.book.image_url)
                            .signature(new StringSignature(review.book.image_url))
                            .diskCacheStrategy(DiskCacheStrategy.RESULT)
                            .placeholder(R.drawable.icon_cover_default)
                            .error(R.drawable.icon_cover_default)
                            .skipMemoryCache(true)
                            .into(user_dynamic_book_cover);
                } else {
                    user_dynamic_book_cover.setImageResource(R.drawable.icon_cover_default);
                }
            }

            if (user_dynamic_book_name != null) {
                user_dynamic_book_name.setText(TextUtils.isEmpty(review.book.name) ? "青果阅读" : review.book.name);
            }

            if (user_dynamic_book_category != null) {
                if (!TextUtils.isEmpty(review.book.category)) {
                    user_dynamic_book_category.setVisibility(View.VISIBLE);
                    user_dynamic_book_category.setText(review.book.category);
                } else {
                    user_dynamic_book_category.setVisibility(View.GONE);
                }
            }

        }
        if (user_dynamic_time != null) {
            user_dynamic_time.setText(TimeUtils.compareTime(BaseConfig.simpleDateFormat, review.create_time));
        }

        if (user_dynamic_reply != null) {
            user_dynamic_reply.setVisibility(View.GONE);
        }
    }
}
