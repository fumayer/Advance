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
import com.quduquxie.base.database.helper.BookDaoHelper;
import com.quduquxie.communal.widget.GlideRoundTransform;

import java.text.MessageFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 17/7/17.
 * Created by crazylei.
 */

public class ShelfGridBookHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.shelf_book)
    public LinearLayout shelf_book;
    @BindView(R.id.shelf_book_check)
    public ImageView shelf_book_check;
    @BindView(R.id.shelf_book_cover)
    ImageView shelf_book_cover;
    @BindView(R.id.shelf_book_state)
    ImageView shelf_book_state;
    @BindView(R.id.shelf_book_name)
    TextView shelf_book_name;
    @BindView(R.id.shelf_book_chapter)
    TextView shelf_book_chapter;

    private static final String[] attributes = new String[]{"serialize", "finish"};
    private static final Integer[] drawables = new Integer[]{R.drawable.icon_local_cover_1, R.drawable.icon_local_cover_2, R.drawable.icon_local_cover_3, R.drawable.icon_local_cover_4, R.drawable.icon_local_cover_5, R.drawable.icon_local_cover_6};

    public ShelfGridBookHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

    public void initializeView(Context context, Book book, BookDaoHelper bookDaoHelper) {
        if (book != null) {

            if (shelf_book_name != null) {
                shelf_book_name.setText(TextUtils.isEmpty(book.name) ? "青果作品" : book.name);
            }

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
                            .into(shelf_book_cover);
                } else {
                    shelf_book_cover.setImageResource(R.drawable.icon_cover_default);
                }

            } else {
                int flag = Integer.valueOf(book.image);
                if (flag > -1 && flag < 6) {
                    shelf_book_cover.setImageResource(drawables[flag]);
                } else {
                    int index = (int) (Math.random() * 100) % 6;
                    shelf_book_cover.setImageResource(drawables[index]);
                    book.image = String.valueOf(index);
                    bookDaoHelper.updateBook(book);
                }
            }

            if (book.chapter != null) {
                int update_count = (book.sequence + 1);
                if (book.chapter.sn == 0) {
                    shelf_book_chapter.setText(MessageFormat.format("已读{0}%", 0));
                } else {
                    int progress = (int) ((update_count * 1.00f / book.chapter.sn) * 100);
                    shelf_book_chapter.setText(MessageFormat.format("已读{0}%", progress));
                }
            }


            if (!TextUtils.isEmpty(book.attribute) && book.attribute.equals(attributes[1])) {
                if (shelf_book_state.getVisibility() != View.VISIBLE) {
                    shelf_book_state.setVisibility(View.VISIBLE);
                }
                shelf_book_state.setImageResource(R.drawable.icon_book_finish);
            } else {
                if (book.update_status == 1) {
                    if (shelf_book_state .getVisibility() != View.VISIBLE) {
                        shelf_book_state.setVisibility(View.VISIBLE);
                    }
                    shelf_book_state.setImageResource(R.drawable.icon_book_serizlize);
                } else {
                    shelf_book_state.setVisibility(View.GONE);
                }
            }
        }

    }
}