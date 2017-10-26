package com.quduquxie.module.comment.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orhanobut.logger.Logger;
import com.quduquxie.R;
import com.quduquxie.base.util.TypefaceUtil;
import com.quduquxie.communal.listener.NoRepeatClickListener;
import com.quduquxie.db.CommentLikeDao;
import com.quduquxie.db.UserDao;
import com.quduquxie.function.comment.list.viewholder.CommentHolder;
import com.quduquxie.function.comment.viewholder.ReplyHolder;
import com.quduquxie.model.Comment;
import com.quduquxie.model.CommentReply;
import com.quduquxie.model.CommentUser;
import com.quduquxie.module.comment.listener.CommentItemListener;
import com.quduquxie.module.comment.listener.UserClickedListener;
import com.quduquxie.module.comment.util.RenderUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created on 17/3/1.
 * Created by crazylei.
 */

public class CommentReplyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<CommentReply> commentReplies;
    private WeakReference<Context> contextReference;

    private Comment comment;
    private CommentItemListener commentItemListener;
    private LayoutInflater layoutInflater;

    private CommentLikeDao commentLikeDao;

    private Typeface typeface_song;

    public static final int TYPE_REPLY_COMMENT = 0x80;

    public CommentReplyAdapter(Context context, ArrayList<CommentReply> commentReplies) {
        this.commentReplies = commentReplies;
        this.contextReference = new WeakReference<>(context);
        this.commentLikeDao = CommentLikeDao.getInstance(contextReference.get());
        this.layoutInflater = LayoutInflater.from(contextReference.get());
        this.typeface_song = TypefaceUtil.loadTypeface(contextReference.get(), TypefaceUtil.TYPEFACE_SONG);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_REPLY_COMMENT:
                return new CommentHolder(layoutInflater.inflate(R.layout.layout_item_comment, parent, false));
            default:
                return new ReplyHolder(layoutInflater.inflate(R.layout.layout_view_comment_reply, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final CommentReply commentReply = commentReplies.get(position);
        if (commentReply == null) {
            return;
        }
        if (viewHolder instanceof CommentHolder) {
            if (comment == null) {
                return;
            }

            ((CommentHolder) viewHolder).initParameter(false);
            ((CommentHolder) viewHolder).setCommentItemListener(commentItemListener);
            ((CommentHolder) viewHolder).initView(contextReference.get(), comment, typeface_song);

            if (((CommentHolder) viewHolder).comment_view != null) {
                ((CommentHolder) viewHolder).comment_view.setTag(R.id.click_object, comment);
                ((CommentHolder) viewHolder).comment_view.setTag(R.id.click_position, position);
                ((CommentHolder) viewHolder).comment_view.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if (UserDao.checkUserLogin()) {
                            int clickedPosition = (int) view.getTag(R.id.click_position);
                            Comment clickedComment = (Comment) view.getTag(R.id.click_object);
                            if (commentItemListener != null && clickedComment != null) {
                                commentItemListener.clickedReview(clickedComment, view, clickedPosition);
                            }
                        } else {
                            if (commentItemListener != null) {
                                commentItemListener.startLoginActivity();
                            }
                        }
                    }
                });
            }

            if (((CommentHolder) viewHolder).comment_commend_view != null) {
                ((CommentHolder) viewHolder).comment_commend_view.setTag(R.id.click_object, comment);
                ((CommentHolder) viewHolder).comment_commend_view.setTag(R.id.click_position, position);

                ((CommentHolder) viewHolder).comment_commend_view.setOnClickListener(new NoRepeatClickListener() {
                    @Override
                    public void onClick(View view) {
                        super.onClick(view);
                    }

                    @Override
                    public void onViewClicked(View view) {
                        if (UserDao.checkUserLogin()) {
                            int clickedPosition = (int) view.getTag(R.id.click_position);
                            Comment clickedComment = (Comment) view.getTag(R.id.click_object);

                            if (commentItemListener != null && clickedComment != null) {
                                if (commentLikeDao.isContainsComment(clickedComment.id)) {
                                    if (commentItemListener != null) {
                                        commentItemListener.commentDislikeAction(clickedComment, clickedPosition);
                                    }
                                } else {
                                    if (commentItemListener != null) {
                                        commentItemListener.commentLikeAction(clickedComment, clickedPosition);
                                    }
                                }
                            }
                        } else {
                            if (commentItemListener != null) {
                                commentItemListener.startLoginActivity();
                            }
                        }
                    }
                });
            }

            if (((CommentHolder) viewHolder).comment_reply != null) {
                ((CommentHolder) viewHolder).comment_reply.setTag(R.id.click_object, comment);
                ((CommentHolder) viewHolder).comment_reply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (UserDao.checkUserLogin()) {
                            Comment clickedComment = (Comment) view.getTag(R.id.click_object);
                            if (commentItemListener != null && clickedComment != null) {
                                commentItemListener.showReviewReplyView(clickedComment);
                            }
                        } else {
                            if (commentItemListener != null) {
                                commentItemListener.startLoginActivity();
                            }
                        }
                    }
                });
            }

        } else if (viewHolder instanceof ReplyHolder) {
            if (((ReplyHolder) viewHolder).comment_reply_content != null) {
                ((ReplyHolder) viewHolder).comment_reply_content.setClickable(true);
                ((ReplyHolder) viewHolder).comment_reply_content.setMovementMethod(LinkMovementMethod.getInstance());

                String content = commentReply.sender.name + "：" + "@" + commentReply.receiver.name + "  " + commentReply.content;

                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(content);
                RenderUtil.renderUser(content, commentReply.sender, commentReply.receiver, spannableStringBuilder, new UserClickedListener() {
                    @Override
                    public void onClickedUserSpan(CommentUser commentUser) {
                        Logger.d("View OnSpanClick");
                    }
                });
                ((ReplyHolder) viewHolder).comment_reply_content.setText(spannableStringBuilder);

                ((ReplyHolder) viewHolder).comment_reply_content.setTag(R.id.click_object, commentReply);
                ((ReplyHolder) viewHolder).comment_reply_content.setTag(R.id.click_position, position);
                ((ReplyHolder) viewHolder).comment_reply_content.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (UserDao.checkUserLogin()) {
                            int clickedPosition = (int) view.getTag(R.id.click_position);
                            CommentReply clickedCommentReply = (CommentReply) view.getTag(R.id.click_object);
                            if (commentItemListener != null && comment != null && clickedCommentReply != null && clickedCommentReply.sender != null) {
                                commentItemListener.clickedCommentReply(comment, clickedCommentReply, view, clickedPosition);
                            }
                        } else {
                            if (commentItemListener != null) {
                                commentItemListener.startLoginActivity();
                            }
                        }
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return commentReplies.size();
    }

    @Override
    public int getItemViewType(int position) {
        return commentReplies.get(position).item_type;
    }

    public void setCommentInformation(Comment comment) {
        this.comment = comment;
    }

    public void setCommentItemListener(CommentItemListener commentItemListener) {
        this.commentItemListener = commentItemListener;
    }
}
