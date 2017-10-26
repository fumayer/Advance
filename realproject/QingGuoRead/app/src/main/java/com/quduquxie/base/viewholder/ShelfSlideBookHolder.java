package com.quduquxie.base.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.StringSignature;
import com.orhanobut.logger.Logger;
import com.quduquxie.R;
import com.quduquxie.base.bean.Book;
import com.quduquxie.base.database.helper.BookDaoHelper;
import com.quduquxie.base.widget.ShelfSlideRelativeLayout;
import com.quduquxie.communal.widget.GlideCircleTransform;
import com.quduquxie.communal.widget.GlideRoundTransform;

import java.text.MessageFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 17/7/19.
 * Created by crazylei.
 */

public class ShelfSlideBookHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.shelf_slide_book)
    public ShelfSlideRelativeLayout shelf_slide_book;
    @BindView(R.id.shelf_slide_book_check)
    public ImageView shelf_slide_book_check;
    @BindView(R.id.shelf_slide_book_state)
    ImageView shelf_slide_book_state;
    @BindView(R.id.shelf_slide_book_cover)
    ImageView shelf_slide_book_cover;
    @BindView(R.id.shelf_slide_book_name)
    TextView shelf_slide_book_name;
    @BindView(R.id.shelf_slide_book_progress)
    TextView shelf_slide_book_progress;
    @BindView(R.id.shelf_slide_book_author_avatar)
    ImageView shelf_slide_book_author_avatar;
    @BindView(R.id.shelf_slide_book_author)
    TextView shelf_slide_book_author;

    private static final String[] attributes = new String[]{"serialize", "finish"};
    private static final Integer[] drawables = new Integer[]{R.drawable.icon_local_cover_1, R.drawable.icon_local_cover_2, R.drawable.icon_local_cover_3, R.drawable.icon_local_cover_4, R.drawable.icon_local_cover_5, R.drawable.icon_local_cover_6};

    public ShelfSlideBookHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

    public void initializeView(Context context, Book book, BookDaoHelper bookDaoHelper) {
        if (book != null) {

            if (book.book_type == Book.TYPE_ONLINE) {
                if (!TextUtils.isEmpty(book.image)) {
                    Glide.with(context)
                            .load(book.image)
                            .transform(new GlideRoundTransform(context))
                            .signature(new StringSignature(book.image))
                            .diskCacheStrategy(DiskCacheStrategy.RESULT)
                            .placeholder(R.drawable.icon_cover_default)
                            .error(R.drawable.icon_cover_default)
                            .skipMemoryCache(true)
                            .into(shelf_slide_book_cover);
                } else {
                    shelf_slide_book_cover.setImageResource(R.drawable.icon_cover_default);
                }

            } else {
                int flag = Integer.valueOf(book.image);
                if (flag > -1 && flag < 6) {
                    shelf_slide_book_cover.setImageResource(drawables[flag]);
                } else {
                    int index = (int) (Math.random() * 100) % 6;
                    shelf_slide_book_cover.setImageResource(drawables[index]);
                    book.image = String.valueOf(index);
                    bookDaoHelper.updateBook(book);
                }
            }

            shelf_slide_book_name.setText(TextUtils.isEmpty(book.name) ? "青果作品" : book.name);

            if (book.chapter != null) {
                int update_count = (book.sequence + 1);
                if (book.chapter.sn == 0) {
                    shelf_slide_book_progress.setText(MessageFormat.format("已读{0}%", 0));
                } else {
                    Logger.e("update_count: " + update_count + " : " + book.chapter.sn);
                    int progress = (int) ((update_count * 1.00f / book.chapter.sn) * 100);
                    shelf_slide_book_progress.setText(MessageFormat.format("已读{0}%", progress));
                }
            }

            if (book.author != null) {
                if (!TextUtils.isEmpty(book.author.avatar)) {
                    Glide.with(context)
                            .load(book.author.avatar)
                            .signature(new StringSignature(book.author.avatar))
                            .transform(new GlideCircleTransform(context))
                            .diskCacheStrategy(DiskCacheStrategy.RESULT)
                            .error(R.drawable.icon_avatar_default)
                            .placeholder(R.drawable.icon_avatar_default)
                            .skipMemoryCache(true)
                            .into(shelf_slide_book_author_avatar);
                } else {
                    shelf_slide_book_author_avatar.setImageResource(R.drawable.icon_avatar_default);
                }

                shelf_slide_book_author.setText(TextUtils.isEmpty(book.author.name) ? "青果作家" : book.author.name);
            }


            if (!TextUtils.isEmpty(book.attribute) && book.attribute.equals(attributes[1])) {
                if (shelf_slide_book_state.getVisibility() != View.VISIBLE) {
                    shelf_slide_book_state.setVisibility(View.VISIBLE);
                }
                shelf_slide_book_state.setImageResource(R.drawable.icon_book_finish);
            } else {
                if (book.update_status == 1) {
                    if (shelf_slide_book_state.getVisibility() != View.VISIBLE) {
                        shelf_slide_book_state.setVisibility(View.VISIBLE);
                    }
                    shelf_slide_book_state.setImageResource(R.drawable.icon_book_serizlize);
                } else {
                    shelf_slide_book_state.setVisibility(View.GONE);
                }
            }
        }

    }
}