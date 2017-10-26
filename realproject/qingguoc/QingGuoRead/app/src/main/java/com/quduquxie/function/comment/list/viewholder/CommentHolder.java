package com.quduquxie.function.comment.list.viewholder;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.StringSignature;
import com.quduquxie.R;
import com.quduquxie.base.config.BaseConfig;
import com.quduquxie.communal.widget.GlideCircleTransform;
import com.quduquxie.communal.widget.expand.ExpandExpressionTextView;
import com.quduquxie.db.CommentLikeDao;
import com.quduquxie.function.comment.list.util.CommentReplyHelper;
import com.quduquxie.model.Comment;
import com.quduquxie.module.comment.listener.CommentItemListener;
import com.quduquxie.util.TimeUtils;

import java.text.MessageFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 17/2/17.
 * Created by crazylei.
 */

public class CommentHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.comment_view)
    public LinearLayout comment_view;
    @BindView(R.id.comment_commend_view)
    public LinearLayout comment_commend_view;
    @BindView(R.id.comment_reply)
    public ImageView comment_reply;

    @BindView(R.id.comment_avatar)
    ImageView comment_avatar;
    @BindView(R.id.comment_signed_mark)
    ImageView comment_signed_mark;
    @BindView(R.id.comment_user_name)
    TextView comment_user_name;
    @BindView(R.id.comment_author_mark)
    TextView comment_author_mark;
    @BindView(R.id.comment_commend_image)
    ImageView comment_commend_image;
    @BindView(R.id.comment_commend_number)
    TextView comment_commend_number;
    @BindView(R.id.comment_floor)
    TextView comment_floor;
    @BindView(R.id.comment_time)
    TextView comment_time;
    @BindView(R.id.comment_content)
    ExpandExpressionTextView comment_content;
    @BindView(R.id.comment_reply_list)
    ViewGroup comment_reply_list;

    private boolean spread = true;

    private  CommentReplyHelper commentReplyHelper;

    private CommentLikeDao commentLikeDao;

    private CommentItemListener commentItemListener;

    public CommentHolder(View view) {
        super(view);

        ButterKnife.bind(this, view);
    }

    public void initParameter(boolean spread) {
        this.spread = spread;
    }

    public void initView(Context context, Comment comment, Typeface typeface) {
        if (comment.sender != null) {
            if (comment_avatar != null) {
                if (!TextUtils.isEmpty(comment.sender.avatar_url)) {
                    Glide.with(context)
                            .load(comment.sender.avatar_url)
                            .signature(new StringSignature(comment.sender.avatar_url))
                            .transform(new GlideCircleTransform(context))
                            .diskCacheStrategy(DiskCacheStrategy.RESULT)
                            .placeholder(R.drawable.icon_avatar_default)
                            .error(R.drawable.icon_avatar_default)
                            .skipMemoryCache(true)
                            .into(comment_avatar);
                } else {
                    comment_avatar.setImageResource(R.drawable.icon_avatar_default);
                }
            }

            if (comment_user_name != null) {
                comment_user_name.setText(TextUtils.isEmpty(comment.sender.name) ? "青果阅读" : comment.sender.name);
                comment_user_name.setTypeface(typeface);
            }

            if (comment_signed_mark != null) {
                if (comment.sender.is_sign == 1) {
                    comment_signed_mark.setVisibility(View.VISIBLE);
                } else {
                    comment_signed_mark.setVisibility(View.INVISIBLE);
                }
            }
        }

        if (comment_author_mark != null) {
            if (comment.is_author == 1) {
                comment_author_mark.setVisibility(View.VISIBLE);
            } else {
                comment_author_mark.setVisibility(View.GONE);
            }
        }

        if (comment_floor != null) {
            comment_floor.setText(MessageFormat.format("{0}楼", comment.sn));
        }

        if (comment_time != null) {
            comment_time.setText(TimeUtils.compareTime(BaseConfig.simpleDateFormat, comment.create_time));
        }

        if (comment_content != null) {
            comment_content.setContent(TextUtils.isEmpty(comment.content) ? "您要找的书评暂时无法查看" : comment.content);
        }

        if (comment_commend_image != null) {
            if (commentLikeDao == null) {
                commentLikeDao = CommentLikeDao.getInstance(context);
            }
            if (!TextUtils.isEmpty(comment.id)) {
                comment_commend_image.setVisibility(View.VISIBLE);
                if (comment_commend_number != null) {
                    comment_commend_number.setVisibility(View.VISIBLE);
                }

                if (commentLikeDao.isContainsComment(comment.id)) {

                    comment_commend_image.setImageResource(R.drawable.icon_commend_selected);

                    if (comment_commend_number != null) {
                        if (comment.like_count == 0) {
                            comment.like_count = 1;
                        }
                        comment_commend_number.setText(String.valueOf(comment.like_count));
                    }
                } else {
                    comment_commend_image.setImageResource(R.drawable.icon_commend_normal);
                    if (comment_commend_number != null) {
                        comment_commend_number.setText(String.valueOf(comment.like_count));
                    }
                }

            } else {

                comment_commend_image.setVisibility(View.GONE);

                if (comment_commend_number != null) {
                    comment_commend_number.setVisibility(View.GONE);
                }
            }
        }

        if (spread) {
            if (comment.replies != null && comment.replies.size() > 0) {
                comment_reply_list.setVisibility(View.VISIBLE);
                commentReplyHelper = new CommentReplyHelper(comment_reply_list, comment, commentItemListener);
                commentReplyHelper.refreshView();
            } else {
                comment_reply_list.setVisibility(View.GONE);
            }
        } else {
            comment_reply_list.setVisibility(View.GONE);
        }

    }

    public void setCommentItemListener(CommentItemListener commentItemListener) {
        this.commentItemListener = commentItemListener;
    }
}
