package com.quduquxie.base.viewholder;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.StringSignature;
import com.quduquxie.R;
import com.quduquxie.base.bean.Book;
import com.quduquxie.base.util.CommunalUtil;
import com.quduquxie.communal.widget.GlideCircleTransform;
import com.quduquxie.communal.widget.GlideRoundTransform;

import java.text.MessageFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 17/7/25.
 * Created by crazylei.
 */

public class BookRecommendHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.book_recommend)
    public LinearLayout book_recommend;

    @BindView(R.id.book_recommend_avatar)
    ImageView book_recommend_avatar;
    @BindView(R.id.book_recommend_author)
    TextView book_recommend_author;
    @BindView(R.id.book_recommend_desc)
    TextView book_recommend_desc;
    @BindView(R.id.book_recommend_cover)
    ImageView book_recommend_cover;
    @BindView(R.id.book_recommend_name)
    TextView book_recommend_name;
    @BindView(R.id.book_recommend_category)
    TextView book_recommend_category;
    @BindView(R.id.book_recommend_style)
    TextView book_recommend_style;
    @BindView(R.id.book_recommend_ending)
    TextView book_recommend_ending;
    @BindView(R.id.book_recommend_read)
    TextView book_recommend_read;
    @BindView(R.id.book_recommend_follow)
    TextView book_recommend_follow;

    public BookRecommendHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

    public void initializeView(Context context, Book book) {
        if (book != null) {
            if (book.author != null) {
                if (!TextUtils.isEmpty(book.author.avatar)) {
                    Glide.with(context)
                            .load(book.author.avatar)
                            .skipMemoryCache(true)
                            .signature(new StringSignature(book.author.avatar))
                            .transform(new GlideCircleTransform(context))
                            .diskCacheStrategy(DiskCacheStrategy.RESULT)
                            .error(R.drawable.icon_avatar_default)
                            .placeholder(R.drawable.icon_avatar_default)
                            .into(book_recommend_avatar);
                } else {
                    book_recommend_avatar.setImageResource(R.drawable.icon_avatar_default);
                }

                book_recommend_author.setText(TextUtils.isEmpty(book.author.name) ? "青果作家" : book.author.name);
            }

            if (!TextUtils.isEmpty(book.image)) {
                Glide.with(context)
                        .load(book.image)
                        .skipMemoryCache(true)
                        .signature(new StringSignature(book.image))
                        .transform(new GlideRoundTransform(context))
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .error(R.drawable.icon_cover_default)
                        .placeholder(R.drawable.icon_cover_default)
                        .into(book_recommend_cover);
            } else {
                book_recommend_cover.setImageResource(R.drawable.icon_cover_default);
            }

            book_recommend_name.setText(TextUtils.isEmpty(book.name) ? "青果作品" : book.name);
            book_recommend_desc.setText(TextUtils.isEmpty(book.description) ? "我还在努力码字哦~ 暂无章节更新！" : book.description);

            book_recommend_category.setText(TextUtils.isEmpty(book.category) ? "作品类型" : book.category);
            book_recommend_style.setText(TextUtils.isEmpty(book.category) ? "作品风格" : book.style);
            book_recommend_ending.setText(TextUtils.isEmpty(book.category) ? "结局类型" : book.ending);

            book_recommend_read.setText(MessageFormat.format("阅读{0}", CommunalUtil.formatNumber(book.read_count)));

            book_recommend_follow.setText(MessageFormat.format("追更{0}", CommunalUtil.formatNumber(book.follow_count)));
        }
    }
}