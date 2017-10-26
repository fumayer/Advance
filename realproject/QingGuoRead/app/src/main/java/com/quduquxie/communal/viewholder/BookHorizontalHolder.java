package com.quduquxie.communal.viewholder;

import android.content.Context;
import android.graphics.Typeface;
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

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 16/10/18.
 * Created by WangLei.
 */

public class BookHorizontalHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.book_horizontal)
    public RelativeLayout book_horizontal;
    @BindView(R.id.book_horizontal_add_shelf)
    public ImageView book_horizontal_add_shelf;
    @BindView(R.id.book_horizontal_image)
    ImageView book_horizontal_image;
    @BindView(R.id.book_horizontal_signed)
    ImageView book_horizontal_signed;
    @BindView(R.id.book_horizontal_ranking)
    ImageView book_horizontal_ranking;
    @BindView(R.id.book_horizontal_name)
    TextView book_horizontal_name;
    @BindView(R.id.book_horizontal_author)
    TextView book_horizontal_author;
    @BindView(R.id.book_horizontal_description)
    TextView book_horizontal_description;

    public BookHorizontalHolder(View view) {
        super(view);

        ButterKnife.bind(this, view);

    }

    public void initView(Context context, Book book, Typeface typeface, int position, String type) {
        if (book != null) {
            if (book_horizontal_image != null) {
                if (!TextUtils.isEmpty(book.image)) {
                    Glide.with(context)
                            .load(book.image)
                            .signature(new StringSignature(book.image))
                            .skipMemoryCache(true)
                            .error(R.drawable.icon_default)
                            .placeholder(R.drawable.icon_default)
                            .diskCacheStrategy(DiskCacheStrategy.RESULT)
                            .fitCenter()
                            .into(book_horizontal_image);
                } else {
                    book_horizontal_image.setImageResource(R.drawable.icon_default);
                }
            }

            if (book_horizontal_ranking != null) {
                if ("Tabulation".equals(type)) {
                    if (position > 0 && position < 4) {
                        book_horizontal_ranking.setVisibility(View.VISIBLE);
                    } else {
                        book_horizontal_ranking.setVisibility(View.GONE);
                    }
                    if (position == 1) {
                        book_horizontal_ranking.setImageResource(R.drawable.icon_ranking_first);
                    } else if (position == 2) {
                        book_horizontal_ranking.setImageResource(R.drawable.icon_ranking_second);
                    } else if (position == 3) {
                        book_horizontal_ranking.setImageResource(R.drawable.icon_ranking_third);
                    }
                } else {
                    book_horizontal_ranking.setVisibility(View.GONE);
                }
            }

            if (book_horizontal_signed != null) {
                if (book.is_sign == 1) {
                    book_horizontal_signed.setVisibility(View.VISIBLE);
                } else {
                    book_horizontal_signed.setVisibility(View.GONE);
                }
            }

            if (book_horizontal_name != null) {
                book_horizontal_name.setText(TextUtils.isEmpty(book.name) ? "青果阅读" : book.name);
                book_horizontal_name.setTypeface(typeface);
            }

            if (book.author != null) {
                if (book_horizontal_author != null) {
                    book_horizontal_author.setText(TextUtils.isEmpty(book.author.name) ? "青果作家" : book.author.name);
                }
            }

            if (book_horizontal_description != null) {
                book_horizontal_description.setText(TextUtils.isEmpty(book.description) ? "我还在努力码字哦~ 暂无章节更新！" : book.description);
            }
        }
    }
}
