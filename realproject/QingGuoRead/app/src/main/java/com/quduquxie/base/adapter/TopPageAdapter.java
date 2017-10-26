package com.quduquxie.base.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.StringSignature;
import com.quduquxie.R;
import com.quduquxie.base.bean.Book;
import com.quduquxie.base.listener.BookListener;
import com.quduquxie.base.util.CommunalUtil;
import com.quduquxie.communal.widget.GlideRoundTransform;

import java.lang.ref.WeakReference;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on 17/7/24.
 * Created by crazylei.
 */

public class TopPageAdapter extends PagerAdapter {

    private WeakReference<Context> contextReference;

    private SparseArray<List<Book>> books;

    private LayoutInflater layoutInflater;

    private SparseArray<View> views = new SparseArray<>();

    private BookListener bookListener;

    public TopPageAdapter(Context context, SparseArray<List<Book>> books, BookListener bookListener) {
        this.contextReference = new WeakReference<>(context);
        this.books = books;
        this.bookListener = bookListener;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return books.size();
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        views.remove(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View view = views.get(position);

        if (view == null) {
            List<Book> bookList = books.get(position);

            view = layoutInflater.inflate(R.layout.layout_view_top_item, null, false);

            if (view != null && bookList != null && bookList.size() > 0) {
                initializeView(view, bookList, position == 0);
            }

            views.put(position, view);
        }

        container.addView(view);
        return view;
    }

    public void recyclerData() {
        if (views != null && views.size() > 0) {
            for (int i = 0; i < views.size(); i++) {
                views.get(i).destroyDrawingCache();
            }
        }
        if (views != null) {
            views.clear();
        }
    }

    private void initializeView(View view, List<Book> books, boolean state) {
        if (view != null && books != null && books.size() > 2) {

            Book first = books.get(0);

            RelativeLayout top_first = (RelativeLayout) view.findViewById(R.id.top_first);

            if (first != null) {

                ImageView top_first_cover = (ImageView) view.findViewById(R.id.top_first_cover);

                ImageView top_first_flag = (ImageView) view.findViewById(R.id.top_first_flag);

                TextView top_first_name = (TextView) view.findViewById(R.id.top_first_name);
                TextView top_first_author = (TextView) view.findViewById(R.id.top_first_author);
                TextView top_first_read = (TextView) view.findViewById(R.id.top_first_read);
                TextView top_first_follow = (TextView) view.findViewById(R.id.top_first_follow);
                TextView top_first_state = (TextView) view.findViewById(R.id.top_first_state);

                top_first.setTag(R.id.click_object, first);
                top_first.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (bookListener != null) {
                            Book clickedBook = (Book) view.getTag(R.id.click_object);
                            bookListener.onClickedBook(clickedBook);
                        }
                    }
                });

                if (state) {
                    top_first_flag.setVisibility(View.VISIBLE);
                } else {
                    top_first_flag.setVisibility(View.GONE);
                }

                if (!TextUtils.isEmpty(first.image)) {
                    Glide.with(contextReference.get())
                            .load(first.image)
                            .signature(new StringSignature(first.image))
                            .transform(new GlideRoundTransform(contextReference.get()))
                            .diskCacheStrategy(DiskCacheStrategy.RESULT)
                            .skipMemoryCache(true)
                            .error(R.drawable.icon_cover_default)
                            .placeholder(R.drawable.icon_cover_default)
                            .into(top_first_cover);
                } else {
                    top_first_cover.setImageResource(R.drawable.icon_cover_default);
                }

                top_first_name.setText(TextUtils.isEmpty(first.name) ? "青果作品" : first.name);

                if (first.author != null) {
                    top_first_author.setText(TextUtils.isEmpty(first.author.name) ? "青果作家" : first.author.name);
                }

                top_first_read.setText(MessageFormat.format("阅读 {0}", CommunalUtil.formatNumber(first.read_count)));

                top_first_follow.setText(MessageFormat.format("追更{0}", CommunalUtil.formatNumber(first.follow_count)));

                if ("finish".equals(first.attribute)) {
                    top_first_state.setText("完结");
                    top_first_state.setTextColor(Color.parseColor("#DA6254"));
                } else {
                    if (first.chapter != null) {
                        top_first_state.setText(MessageFormat.format("更新至{0}章", first.chapter.sn));
                        top_first_state.setTextColor(Color.parseColor("#0094D5"));
                    }
                }
            } else {
                top_first.setVisibility(View.GONE);
            }

            Book second = books.get(1);

            RelativeLayout top_second = (RelativeLayout) view.findViewById(R.id.top_second);

            if (second != null) {

                ImageView top_second_cover = (ImageView) view.findViewById(R.id.top_second_cover);

                ImageView top_second_flag = (ImageView) view.findViewById(R.id.top_second_flag);

                TextView top_second_name = (TextView) view.findViewById(R.id.top_second_name);
                TextView top_second_author = (TextView) view.findViewById(R.id.top_second_author);
                TextView top_second_read = (TextView) view.findViewById(R.id.top_second_read);
                TextView top_second_follow = (TextView) view.findViewById(R.id.top_second_follow);
                TextView top_second_state = (TextView) view.findViewById(R.id.top_second_state);

                top_second.setTag(R.id.click_object, second);
                top_second.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (bookListener != null) {
                            Book clickedBook = (Book) view.getTag(R.id.click_object);
                            bookListener.onClickedBook(clickedBook);
                        }
                    }
                });

                if (!TextUtils.isEmpty(second.image)) {
                    Glide.with(contextReference.get())
                            .load(second.image)
                            .signature(new StringSignature(second.image))
                            .transform(new GlideRoundTransform(contextReference.get()))
                            .diskCacheStrategy(DiskCacheStrategy.RESULT)
                            .skipMemoryCache(true)
                            .error(R.drawable.icon_cover_default)
                            .placeholder(R.drawable.icon_cover_default)
                            .into(top_second_cover);
                } else {
                    top_second_cover.setImageResource(R.drawable.icon_cover_default);
                }

                if (state) {
                    top_second_flag.setVisibility(View.VISIBLE);
                } else {
                    top_second_flag.setVisibility(View.GONE);
                }

                top_second_name.setText(TextUtils.isEmpty(second.name) ? "青果作品" : second.name);

                if (second.author != null) {
                    top_second_author.setText(TextUtils.isEmpty(second.author.name) ? "青果作家" : second.author.name);
                }

                top_second_read.setText(MessageFormat.format("阅读 {0}", CommunalUtil.formatNumber(second.read_count)));

                top_second_follow.setText(MessageFormat.format("追更{0}", CommunalUtil.formatNumber(second.follow_count)));

                if ("finish".equals(second.attribute)) {
                    top_second_state.setText("完结");
                    top_second_state.setTextColor(Color.parseColor("#DA6254"));
                } else {
                    if (second.chapter != null) {
                        top_second_state.setText(MessageFormat.format("更新至{0}章", second.chapter.sn));
                        top_second_state.setTextColor(Color.parseColor("#0094D5"));
                    }
                }
            } else {
                top_second.setVisibility(View.GONE);
            }

            Book third = books.get(2);

            RelativeLayout top_third = (RelativeLayout) view.findViewById(R.id.top_third);

            if (third != null) {

                ImageView top_third_cover = (ImageView) view.findViewById(R.id.top_third_cover);

                ImageView top_third_flag = (ImageView) view.findViewById(R.id.top_third_flag);

                TextView top_third_name = (TextView) view.findViewById(R.id.top_third_name);
                TextView top_third_author = (TextView) view.findViewById(R.id.top_third_author);
                TextView top_third_read = (TextView) view.findViewById(R.id.top_third_read);
                TextView top_third_follow = (TextView) view.findViewById(R.id.top_third_follow);
                TextView top_third_state = (TextView) view.findViewById(R.id.top_third_state);

                top_third.setTag(R.id.click_object, third);
                top_third.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (bookListener != null) {
                            Book clickedBook = (Book) view.getTag(R.id.click_object);
                            bookListener.onClickedBook(clickedBook);
                        }
                    }
                });

                if (!TextUtils.isEmpty(third.image)) {
                    Glide.with(contextReference.get())
                            .load(third.image)
                            .signature(new StringSignature(third.image))
                            .transform(new GlideRoundTransform(contextReference.get()))
                            .diskCacheStrategy(DiskCacheStrategy.RESULT)
                            .skipMemoryCache(true)
                            .error(R.drawable.icon_cover_default)
                            .placeholder(R.drawable.icon_cover_default)
                            .into(top_third_cover);
                } else {
                    top_third_cover.setImageResource(R.drawable.icon_cover_default);
                }

                if (state) {
                    top_third_flag.setVisibility(View.VISIBLE);
                } else {
                    top_third_flag.setVisibility(View.GONE);
                }

                top_third_name.setText(TextUtils.isEmpty(third.name) ? "青果作品" : third.name);

                if (third.author != null) {
                    top_third_author.setText(TextUtils.isEmpty(third.author.name) ? "青果作家" : third.author.name);
                }

                top_third_read.setText(MessageFormat.format("阅读 {0}", CommunalUtil.formatNumber(third.read_count)));

                top_third_follow.setText(MessageFormat.format("追更{0}", CommunalUtil.formatNumber(third.follow_count)));

                if ("finish".equals(third.attribute)) {
                    top_third_state.setText("完结");
                    top_third_state.setTextColor(Color.parseColor("#DA6254"));
                } else {
                    if (third.chapter != null) {
                        top_third_state.setText(MessageFormat.format("更新至{0}章", third.chapter.sn));
                        top_third_state.setTextColor(Color.parseColor("#0094D5"));
                    }
                }
            } else {
                top_third.setVisibility(View.GONE);
            }
        }
    }
}