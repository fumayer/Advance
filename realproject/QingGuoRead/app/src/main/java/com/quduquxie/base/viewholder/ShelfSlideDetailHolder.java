package com.quduquxie.base.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.StringSignature;
import com.orhanobut.logger.Logger;
import com.quduquxie.R;
import com.quduquxie.base.bean.Book;
import com.quduquxie.base.util.CommunalUtil;
import com.quduquxie.base.widget.AutoSetTextView;
import com.quduquxie.base.widget.ExpandInformation;
import com.quduquxie.communal.widget.GlideCircleTransform;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 17/7/29.
 * Created by crazylei.
 */

public class ShelfSlideDetailHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.shelf_slide_detail)
    public RelativeLayout shelf_slide_detail;

    @BindView(R.id.shelf_slide_detail_name)
    TextView shelf_slide_detail_name;
    @BindView(R.id.shelf_slide_detail_author)
    TextView shelf_slide_detail_author;
    @BindView(R.id.book_word_count)
    TextView book_word_count;
    @BindView(R.id.book_word_unit)
    TextView book_word_unit;
    @BindView(R.id.book_read_count)
    TextView book_read_count;
    @BindView(R.id.book_read_unit)
    TextView book_read_unit;
    @BindView(R.id.book_follow_count)
    TextView book_follow_count;
    @BindView(R.id.book_follow_unit)
    TextView book_follow_unit;
    @BindView(R.id.book_detail_user)
    View book_detail_user;
    @BindView(R.id.book_detail_user_avatar)
    ImageView book_detail_user_avatar;
    @BindView(R.id.book_detail_user_name)
    TextView book_detail_user_name;
    @BindView(R.id.book_detail_user_message)
    TextView book_detail_user_message;
    @BindView(R.id.book_detail_chapter)
    View book_detail_chapter;
    @BindView(R.id.book_detail_chapter_update)
    LinearLayout book_detail_chapter_update;
    @BindView(R.id.book_detail_chapter_update_count)
    TextView book_detail_chapter_update_count;
    @BindView(R.id.book_detail_chapter_message)
    TextView book_detail_chapter_message;
    @BindView(R.id.book_detail_chapter_name)
    TextView book_detail_chapter_name;
    @BindView(R.id.shelf_slide_detail_message)
    TextView shelf_slide_detail_message;
    @BindView(R.id.shelf_slide_detail_prompt)
    TextView shelf_slide_detail_prompt;

    public ShelfSlideDetailHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

    public void initializeBook(Context context, Book book) {
        if (book != null) {

            shelf_slide_detail_name.setText(TextUtils.isEmpty(book.name) ? "青果作品" : book.name);

            if (book.author != null) {
                shelf_slide_detail_author.setText(TextUtils.isEmpty(book.author.name) ? "青果作家" : book.author.name);
            }

            CommunalUtil.formatNumber(book_word_count, book_word_unit, book.word_count);

            CommunalUtil.formatNumber(book_read_count, book_read_unit, book.read_count);

            CommunalUtil.formatNumber(book_follow_count, book_follow_unit, book.follow_count);

            if (book.update_status == 1) {
                Logger.e("initializeBook: " + book.update_status + " : " + book.update_count);

                shelf_slide_detail.setTag(R.id.click_type, "Chapter");

                book_detail_user.setVisibility(View.GONE);
                book_detail_chapter.setVisibility(View.VISIBLE);

                if (book.update_count > 1) {
                    book_detail_chapter_update.setVisibility(View.VISIBLE);

                    book_detail_chapter_update_count.setText(String.valueOf(book.update_count));
                    book_detail_chapter_message.setText(R.string.chapter_newest);
                    book_detail_chapter_message.setBackgroundResource(R.drawable.icon_book_detailed_message);

                } else {
                    book_detail_chapter_update.setVisibility(View.GONE);

                    book_detail_chapter_message.setText(R.string.chapter_new);
                    book_detail_chapter_message.setBackgroundResource(R.drawable.icon_book_detailed_message);
                }

                if (book.chapter != null) {
                    book_detail_chapter_name.setText(TextUtils.isEmpty(book.chapter.name) ? "青果作品最新章节" : book.chapter.name);
                    shelf_slide_detail_message.setText(TextUtils.isEmpty(book.chapter.content) ? "我还在努力码字哦~ 暂无章节更新！" : book.chapter.content);
                }

                shelf_slide_detail_prompt.setText(R.string.read_immediately);

            } else {
                book_detail_user.setVisibility(View.VISIBLE);
                book_detail_chapter.setVisibility(View.GONE);

                if (!TextUtils.isEmpty(book.authorTalk)) {

                    shelf_slide_detail.setTag(R.id.click_type, "AuthorTalk");

                    if (book.author != null) {
                        if (!TextUtils.isEmpty(book.author.avatar)) {
                            Glide.with(context)
                                    .load(book.author.avatar)
                                    .signature(new StringSignature(book.author.avatar))
                                    .transform(new GlideCircleTransform(context))
                                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                                    .skipMemoryCache(true)
                                    .error(R.drawable.icon_avatar_default)
                                    .placeholder(R.drawable.icon_avatar_default)
                                    .into(book_detail_user_avatar);
                        } else {
                            book_detail_user_avatar.setImageResource(R.drawable.icon_avatar_default);
                        }

                        book_detail_user_name.setText(TextUtils.isEmpty(book.author.name) ? "青果作家" : book.author.name);

                        book_detail_user_message.setVisibility(View.VISIBLE);
                        book_detail_user_message.setText(R.string.author_say_something);

                        shelf_slide_detail_message.setText(book.authorTalk);

                        shelf_slide_detail_prompt.setText(R.string.check_more);
                    }
                } else {

                    if (book.hotComment != null) {

                        shelf_slide_detail.setTag(R.id.click_type, "Comment");

                        if (book.hotComment.sender != null) {
                            if (!TextUtils.isEmpty(book.hotComment.sender.avatar_url)) {
                                Glide.with(context)
                                        .load(book.hotComment.sender.avatar_url)
                                        .signature(new StringSignature(book.hotComment.sender.avatar_url))
                                        .transform(new GlideCircleTransform(context))
                                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                                        .skipMemoryCache(true)
                                        .error(R.drawable.icon_avatar_default)
                                        .placeholder(R.drawable.icon_avatar_default)
                                        .into(book_detail_user_avatar);
                            } else {
                                book_detail_user_avatar.setImageResource(R.drawable.icon_avatar_default);
                            }

                            book_detail_user_name.setText(TextUtils.isEmpty(book.hotComment.sender.name) ? "青果作家" : book.hotComment.sender.name);

                            book_detail_user_message.setVisibility(View.VISIBLE);
                            book_detail_user_message.setText(R.string.newest_comment);

                            shelf_slide_detail_message.setText(TextUtils.isEmpty(book.hotComment.content) ? "作者大大赶紧更新，实在等不急了！" : book.hotComment.content);

                            shelf_slide_detail_prompt.setText(R.string.participate_discussion);
                        }
                    } else {
                        book_detail_user.setVisibility(View.GONE);
                        book_detail_chapter.setVisibility(View.VISIBLE);

                        shelf_slide_detail.setTag(R.id.click_type, "Chapter");

                        if (book.chapter != null) {
                            book_detail_chapter_update.setVisibility(View.GONE);

                            book_detail_chapter_message.setText(R.string.chapter_new);
                            book_detail_chapter_message.setBackgroundResource(R.drawable.icon_book_update_state);

                            book_detail_chapter_name.setText(TextUtils.isEmpty(book.chapter.name) ? "青果作品最新章节" : book.chapter.name);
                            shelf_slide_detail_message.setText(TextUtils.isEmpty(book.chapter.content) ? "我还在努力码字哦~ 暂无章节更新！" : book.chapter.content);

                            shelf_slide_detail_prompt.setText(R.string.read_immediately);
                        }
                    }
                }
            }
        }
    }
}