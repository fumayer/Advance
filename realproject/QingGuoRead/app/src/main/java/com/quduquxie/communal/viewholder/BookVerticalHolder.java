package com.quduquxie.communal.viewholder;

import android.content.Context;
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

/**
 * Created on 16/10/18.
 * Created by WangLei.
 */

public class BookVerticalHolder extends RecyclerView.ViewHolder {

    public RelativeLayout book_vertical;
    private ImageView book_vertical_cover;
    private TextView book_vertical_name;
    private TextView book_vertical_author;

    public BookVerticalHolder(View view) {
        super(view);
        book_vertical = (RelativeLayout) view.findViewById(R.id.book_vertical);
        book_vertical_cover = (ImageView) view.findViewById(R.id.book_vertical_cover);
        book_vertical_name = (TextView) view.findViewById(R.id.book_vertical_name);
        book_vertical_author = (TextView) view.findViewById(R.id.book_vertical_author);
    }

    public void initView(Context context, Book book) {
        if (book != null) {
            if (book_vertical_cover != null) {
                if (!TextUtils.isEmpty(book.image)) {
                    Glide.with(context)
                            .load(book.image)
                            .signature(new StringSignature(book.image))
                            .skipMemoryCache(true)
                            .error(R.drawable.icon_default)
                            .placeholder(R.drawable.icon_default)
                            .diskCacheStrategy(DiskCacheStrategy.RESULT)
                            .fitCenter()
                            .into(book_vertical_cover);
                } else {
                    book_vertical_cover.setImageResource(R.drawable.icon_default);
                }
            }


            if (book_vertical_name != null) {
                book_vertical_name.setText(TextUtils.isEmpty(book.name) ? "青果阅读" : book.name);
            }

            if (book.author != null) {
                if (book_vertical_author != null) {
                    book_vertical_author.setText(TextUtils.isEmpty(book.author.name) ? "青果作家" : book.author.name);
                }
            }
        }
    }
}
