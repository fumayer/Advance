package com.quduquxie.base.viewholder;

import android.content.Context;
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

public class BookSelectedHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.book_selected)
    public LinearLayout book_selected;
    @BindView(R.id.book_selected_avatar)
    ImageView book_selected_avatar;
    @BindView(R.id.book_selected_author)
    TextView book_selected_author;
    @BindView(R.id.book_selected_desc)
    TextView book_selected_desc;
    @BindView(R.id.book_selected_banner)
    ImageView book_selected_banner;
    @BindView(R.id.book_selected_name)
    TextView book_selected_name;
    @BindView(R.id.book_selected_read)
    TextView book_selected_read;
    @BindView(R.id.book_selected_follow)
    TextView book_selected_follow;

    public BookSelectedHolder(View view) {
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
                            .into(book_selected_avatar);
                } else {
                    book_selected_avatar.setImageResource(R.drawable.icon_avatar_default);
                }

                book_selected_author.setText(TextUtils.isEmpty(book.author.name) ? "青果作家" : book.author.name);
            }

            if (!TextUtils.isEmpty(book.bannerImage)) {
                Glide.with(context)
                        .load(book.bannerImage)
                        .skipMemoryCache(true)
                        .signature(new StringSignature(book.bannerImage))
                        .transform(new GlideRoundTransform(context))
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .error(R.drawable.icon_banner_default)
                        .placeholder(R.drawable.icon_banner_default)
                        .into(book_selected_banner);
            } else {
                book_selected_banner.setImageResource(R.drawable.icon_banner_default);
            }

            book_selected_name.setText(TextUtils.isEmpty(book.name) ? "青果作品" : book.name);
            book_selected_desc.setText(TextUtils.isEmpty(book.description) ? "我还在努力码字哦~ 暂无章节更新！" : book.description);

            book_selected_read.setText(MessageFormat.format("阅读{0}", CommunalUtil.formatNumber(book.read_count)));

            book_selected_follow.setText(MessageFormat.format("追更{0}", CommunalUtil.formatNumber(book.follow_count)));
        }
    }
}