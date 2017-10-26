package com.quduquxie.base.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.StringSignature;
import com.quduquxie.R;
import com.quduquxie.base.bean.Book;
import com.quduquxie.base.util.CommunalUtil;
import com.quduquxie.communal.widget.GlideCircleTransform;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * Created on 17/7/14.
 * Created by crazylei.
 */

public class CoverDetailed extends LinearLayout {

    private WeakReference<Context> contextReference;

    @BindView(R.id.cover_detailed_image)
    ImageView cover_detailed_image;
    @BindView(R.id.cover_detailed_name)
    TextView cover_detailed_name;
    @BindView(R.id.cover_detailed_author_image)
    ImageView cover_detailed_author_image;
    @BindView(R.id.cover_detailed_author_name)
    TextView cover_detailed_author_name;
    @BindView(R.id.cover_detailed_category)
    TextView cover_detailed_category;
    @BindView(R.id.cover_detailed_style)
    TextView cover_detailed_style;
    @BindView(R.id.cover_detailed_ending)
    TextView cover_detailed_ending;
    @BindView(R.id.cover_detailed_attribute)
    TextView cover_detailed_attribute;
    @BindView(R.id.book_word_count)
    TextView cover_detailed_word_count;
    @BindView(R.id.book_word_unit)
    TextView cover_detailed_word_unit;
    @BindView(R.id.book_read_count)
    TextView cover_detailed_read_count;
    @BindView(R.id.book_read_unit)
    TextView cover_detailed_read_unit;
    @BindView(R.id.book_follow_count)
    TextView cover_detailed_follow_count;
    @BindView(R.id.book_follow_unit)
    TextView cover_detailed_follow_unit;
    @BindView(R.id.cover_detailed_description)
    ExpandInformation cover_detailed_description;
    @BindView(R.id.cover_detailed_other)
    LinearLayout cover_detailed_other;
    @BindView(R.id.cover_detailed_author_message)
    ExpandInformation cover_detailed_author_message;

    public CoverDetailed(Context context) {
        this(context, null);
    }

    public CoverDetailed(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CoverDetailed(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.contextReference = new WeakReference<>(context);

        initializeView();
    }

    public void initializeView() {
        LayoutInflater.from(contextReference.get()).inflate(R.layout.layout_view_cover_detailed, this, true);

        ButterKnife.bind(this);
    }

    public void initializeParameter(Book book) {
        if (book != null) {
            if (cover_detailed_image != null) {
                if (!TextUtils.isEmpty(book.image) && contextReference.get() != null) {
                    Glide.with(contextReference.get())
                            .load(book.image)
                            .diskCacheStrategy(DiskCacheStrategy.RESULT)
                            .placeholder(R.drawable.icon_default)
                            .error(R.drawable.icon_default)
                            .fitCenter()
                            .into(cover_detailed_image);
                } else {
                    cover_detailed_image.setImageResource(R.drawable.icon_default);
                }
            }

            if (cover_detailed_name != null) {
                cover_detailed_name.setText(TextUtils.isEmpty(book.name) ? "青果作品" : book.name);
            }

            if (book.author != null) {
                if (cover_detailed_author_image != null) {
                    if (!TextUtils.isEmpty(book.author.avatar) && contextReference.get() != null) {
                        Glide.with(contextReference.get())
                                .load(book.author.avatar)
                                .signature(new StringSignature(book.author.avatar))
                                .transform(new GlideCircleTransform(contextReference.get()))
                                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                                .placeholder(R.drawable.icon_default)
                                .error(R.drawable.icon_default)
                                .fitCenter()
                                .into(cover_detailed_author_image);
                    } else {
                        cover_detailed_author_image.setImageResource(R.drawable.icon_default);
                    }
                }

                if (cover_detailed_author_name != null) {
                    cover_detailed_author_name.setText(TextUtils.isEmpty(book.author.name) ? "青果作家" : book.author.name);
                }
            }

            if (cover_detailed_category != null) {
                cover_detailed_category.setText(TextUtils.isEmpty(book.category) ? "作品类型" : book.category);
            }

            if (cover_detailed_style != null) {
                cover_detailed_style.setText(TextUtils.isEmpty(book.style) ? "作品风格" : book.style);
            }

            if (cover_detailed_ending != null) {
                cover_detailed_ending.setText(TextUtils.isEmpty(book.ending) ? "结局类型" : book.ending);
            }

            if (cover_detailed_attribute != null) {
                cover_detailed_attribute.setText(book.attribute.equals("finish") ? "完结" : "连载中");
            }

            if (cover_detailed_word_count != null && cover_detailed_word_unit != null) {
                CommunalUtil.formatNumber(cover_detailed_word_count, cover_detailed_word_unit, book.word_count);
            }

            if (cover_detailed_read_count != null && cover_detailed_read_unit != null) {
                CommunalUtil.formatNumber(cover_detailed_read_count, cover_detailed_read_unit, book.read_count);
            }

            if (cover_detailed_follow_count != null && cover_detailed_follow_unit != null) {
                CommunalUtil.formatNumber(cover_detailed_follow_count, cover_detailed_follow_unit, book.follow_count);
            }

            if (cover_detailed_description != null) {
                cover_detailed_description.initializeContent(TextUtils.isEmpty(book.description) ? "我还在努力码字哦~ 暂无章节更新！" : book.description);
            }

            if (TextUtils.isEmpty(book.description)) {
                cover_detailed_other.setVisibility(GONE);
            } else {
                cover_detailed_other.setVisibility(VISIBLE);
                cover_detailed_author_message.initializeContent(book.description);
            }
        }
    }
}