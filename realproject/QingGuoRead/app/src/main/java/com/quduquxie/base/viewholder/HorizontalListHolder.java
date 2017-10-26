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
import com.quduquxie.communal.widget.GlideRoundTransform;

import java.text.MessageFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 17/7/21.
 * Created by crazylei.
 */

public class HorizontalListHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.horizontal_list)
    public RelativeLayout horizontal_list;
    @BindView(R.id.horizontal_list_cover)
    ImageView horizontal_list_cover;
    @BindView(R.id.horizontal_list_name)
    TextView horizontal_list_name;
    @BindView(R.id.horizontal_list_author)
    TextView horizontal_list_author;
    @BindView(R.id.horizontal_list_progress)
    TextView horizontal_list_progress;

    public HorizontalListHolder(View view) {
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
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .skipMemoryCache(true)
                        .error(R.drawable.icon_cover_default)
                        .placeholder(R.drawable.icon_cover_default)
                        .into(horizontal_list_cover);
            } else {
                horizontal_list_cover.setImageResource(R.drawable.icon_cover_default);
            }

            horizontal_list_name.setText(TextUtils.isEmpty(book.name) ? "青果作品" : book.name);

            if (book.author != null) {
                horizontal_list_author.setText(TextUtils.isEmpty(book.author.name) ? "青果作家" : book.author.name);
            }

            if ("finish".equals(book.attribute)) {
                horizontal_list_progress.setText("完结");
                horizontal_list_progress.setTextColor(Color.parseColor("#DA6254"));
            } else {
                if (book.chapter != null) {
                    horizontal_list_progress.setText(MessageFormat.format("更新至{0}章", book.chapter.sn));
                    horizontal_list_progress.setTextColor(Color.parseColor("#0094D5"));
                }
            }

        }
    }
}