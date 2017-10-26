package com.quduquxie.base.viewholder;

import android.content.Context;
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
import com.quduquxie.base.bean.Book;
import com.quduquxie.communal.widget.GlideCircleTransform;
import com.quduquxie.communal.widget.GlideRoundTransform;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 17/7/24.
 * Created by crazylei.
 */

public class AuthorRecommendHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.author_recommend)
    public LinearLayout author_recommend;
    @BindView(R.id.author_recommend_avatar)
    ImageView author_recommend_avatar;
    @BindView(R.id.author_recommend_name)
    TextView author_recommend_name;
    @BindView(R.id.author_recommend_message)
    TextView author_recommend_message;
    @BindView(R.id.author_recommend_cover)
    ImageView author_recommend_cover;
    @BindView(R.id.author_recommend_title)
    TextView author_recommend_title;
    @BindView(R.id.author_recommend_author)
    TextView author_recommend_author;
    @BindView(R.id.author_recommend_desc)
    TextView author_recommend_desc;

    public AuthorRecommendHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

    public void initializeView(Context context, Book book) {
        if (book != null) {
            if (!TextUtils.isEmpty(book.image)) {
                Glide.with(context)
                        .load(book.image)
                        .signature(new StringSignature(book.image))
                        .transform(new GlideRoundTransform(context))
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .error(R.drawable.icon_cover_default)
                        .placeholder(R.drawable.icon_cover_default)
                        .into(author_recommend_cover);
            } else {
                author_recommend_cover.setImageResource(R.drawable.icon_cover_default);
            }

            author_recommend_title.setText(TextUtils.isEmpty(book.name) ? "青果作品" : book.name);

            if (book.bigGodUser != null) {

                if (!TextUtils.isEmpty(book.bigGodUser.avatar)) {
                    Glide.with(context)
                            .load(book.bigGodUser.avatar)
                            .signature(new StringSignature(book.bigGodUser.avatar))
                            .transform(new GlideCircleTransform(context))
                            .skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.RESULT)
                            .error(R.drawable.icon_avatar_default)
                            .placeholder(R.drawable.icon_avatar_default)
                            .into(author_recommend_avatar);
                } else {
                    author_recommend_avatar.setImageResource(R.drawable.icon_avatar_default);
                }

                author_recommend_name.setText(TextUtils.isEmpty(book.bigGodUser.name) ? "青果作家" : book.bigGodUser.name);
            }

            if (book.author != null) {
                author_recommend_author.setText(TextUtils.isEmpty(book.author.name) ? "青果作家" : book.author.name);
            }

            author_recommend_desc.setText(TextUtils.isEmpty(book.description) ? "我还在努力码字哦~ 暂无章节更新！" : book.description);
        }
    }
}