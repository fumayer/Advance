package com.quduquxie.base.viewholder;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
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
 * Created on 17/7/21.
 * Created by crazylei.
 */

public class BookInformationHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.book_information)
    public RelativeLayout book_information;
    @BindView(R.id.book_information_more)
    public ImageView book_information_more;

    @BindView(R.id.book_information_avatar)
    ImageView book_information_avatar;
    @BindView(R.id.book_information_author)
    TextView book_information_author;
    @BindView(R.id.book_information_cover)
    ImageView book_information_cover;
    @BindView(R.id.book_information_state)
    TextView book_information_state;
    @BindView(R.id.book_information_name)
    TextView book_information_name;
    @BindView(R.id.book_information_desc)
    TextView book_information_desc;
    @BindView(R.id.book_information_category)
    TextView book_information_category;
    @BindView(R.id.book_information_read)
    TextView book_information_read;
    @BindView(R.id.book_information_follow)
    TextView book_information_follow;
    @BindView(R.id.book_information_divider)
    View book_information_divider;

    public BookInformationHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

    public void initializeView(Context context, Book book, boolean state, boolean category) {
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
                            .into(book_information_avatar);
                } else {
                    book_information_avatar.setImageResource(R.drawable.icon_avatar_default);
                }

                book_information_author.setText(TextUtils.isEmpty(book.author.name) ? "青果作家" : book.author.name);
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
                        .into(book_information_cover);
            } else {
                book_information_cover.setImageResource(R.drawable.icon_cover_default);
            }

            if ("finish".equals(book.attribute)) {
                book_information_state.setText("完结");
                book_information_state.setTextColor(Color.parseColor("#DA6254"));
            } else {
                if (book.chapter != null) {
                    book_information_state.setText(MessageFormat.format("更新至{0}章", book.chapter.sn));
                    book_information_state.setTextColor(Color.parseColor("#0094D5"));
                }
            }

            book_information_name.setText(TextUtils.isEmpty(book.name) ? "青果作品" : book.name);

            book_information_desc.setText(TextUtils.isEmpty(book.description) ? "我还在努力码字哦~ 暂无章节更新！" : book.description);

            if (category) {
                book_information_category.setVisibility(View.VISIBLE);
                book_information_category.setText(TextUtils.isEmpty(book.category) ? "作品类型" : book.category);
            } else {
                book_information_category.setVisibility(View.GONE);
            }

            book_information_read.setText(MessageFormat.format("阅读{0}", CommunalUtil.formatNumber(book.read_count)));

            book_information_follow.setText(MessageFormat.format("追更{0}", CommunalUtil.formatNumber(book.follow_count)));

            if (state) {
                book_information_divider.setVisibility(View.VISIBLE);
            } else {
                book_information_divider.setVisibility(View.VISIBLE
                );
            }
        }
    }
}