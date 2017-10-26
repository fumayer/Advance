package com.quduquxie.function.comment.list.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quduquxie.R;
import com.quduquxie.base.listener.BookListener;
import com.quduquxie.base.util.TypefaceUtil;
import com.quduquxie.base.viewholder.FillerHolder;
import com.quduquxie.common.viewholder.CoverDetailsHolder;
import com.quduquxie.common.viewholder.RecommendCardHolder;
import com.quduquxie.communal.listener.NoRepeatClickListener;
import com.quduquxie.db.CommentLikeDao;
import com.quduquxie.db.UserDao;
import com.quduquxie.function.comment.list.viewholder.CommentEmptyHolder;
import com.quduquxie.function.comment.list.viewholder.CommentFlagHolder;
import com.quduquxie.function.comment.list.viewholder.CommentHolder;
import com.quduquxie.function.comment.list.viewholder.CommentCountHolder;
import com.quduquxie.function.comment.list.viewholder.CommentMoreHolder;
import com.quduquxie.model.Comment;
import com.quduquxie.model.CommentItem;
import com.quduquxie.module.comment.listener.CommentItemListener;
import com.quduquxie.viewholder.ListEndHolder;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created on 17/4/13.
 * Created by crazylei.
 */

public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private WeakReference<Context> contextReference;

    private ArrayList<CommentItem> commentItems;

    private CommentLikeDao commentLikeDao;

    private LayoutInflater layoutInflater;

    private Typeface typeface_song;

    private BookListener bookListener;
    private CommentItemListener commentItemListener;

    private int count = 0;

    public static final int TYPE_COVER_DETAILS = 0x80;
    public static final int TYPE_COVER_RECOMMEND = 0x81;
    public static final int TYPE_COMMENT_NUMBER = 0x82;
    public static final int TYPE_COMMENT_HOT = 0x83;
    public static final int TYPE_COMMENT_HOT_FLAG = 0x84;
    public static final int TYPE_COMMENT_NEW = 0x85;
    public static final int TYPE_COMMENT_NEW_FLAG = 0x86;
    public static final int TYPE_COMMENT_END = 0x87;
    public static final int TYPE_COMMENT_EMPTY = 0x88;
    public static final int TYPE_COMMENT_MORE = 0x89;
    public static final int TYPE_COMMENT_FILL = 0x90;

    public CommentAdapter(Context context, ArrayList<CommentItem> commentItems) {
        this.contextReference = new WeakReference<>(context);
        this.commentItems = commentItems;
        this.commentLikeDao = CommentLikeDao.getInstance(contextReference.get());
        this.layoutInflater = LayoutInflater.from(contextReference.get());
        this.typeface_song = TypefaceUtil.loadTypeface(contextReference.get(), TypefaceUtil.TYPEFACE_SONG);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_COVER_DETAILS:
                return new CoverDetailsHolder(layoutInflater.inflate(R.layout.layout_item_cover_details, parent, false));
            case TYPE_COVER_RECOMMEND:
                return new RecommendCardHolder(layoutInflater.inflate(R.layout.layout_item_recommend_card, parent, false));
            case TYPE_COMMENT_HOT:
                return new CommentHolder(layoutInflater.inflate(R.layout.layout_item_comment, parent, false));
            case TYPE_COMMENT_HOT_FLAG:
                return new CommentFlagHolder(layoutInflater.inflate(R.layout.layout_item_comment_flag, parent, false));
            case TYPE_COMMENT_NEW:
                return new CommentHolder(layoutInflater.inflate(R.layout.layout_item_comment, parent, false));
            case TYPE_COMMENT_NEW_FLAG:
                return new CommentFlagHolder(layoutInflater.inflate(R.layout.layout_item_comment_flag, parent, false));
            case TYPE_COMMENT_NUMBER:
                return new CommentCountHolder(layoutInflater.inflate(R.layout.layout_item_comment_count, parent, false));
            case TYPE_COMMENT_END:
                return new ListEndHolder(layoutInflater.inflate(R.layout.layout_item_list_end, parent, false));
            case TYPE_COMMENT_EMPTY:
                return new CommentEmptyHolder(layoutInflater.inflate(R.layout.layout_item_comment_empty, parent, false));
            case TYPE_COMMENT_MORE:
                return new CommentMoreHolder(layoutInflater.inflate(R.layout.layout_item_comment_more, parent, false));
            case TYPE_COMMENT_FILL:
                return new FillerHolder(layoutInflater.inflate(R.layout.layout_view_filler_shadow, parent, false));
            default:
                return new FillerHolder(layoutInflater.inflate(R.layout.layout_view_filler, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        CommentItem commentItem = commentItems.get(position);
        if (commentItem == null) {
            return;
        }

        if (viewHolder instanceof CoverDetailsHolder) {
            if (commentItem.book != null) {
                ((CoverDetailsHolder) viewHolder).initView(contextReference.get(), commentItem.book);
                ((CoverDetailsHolder) viewHolder).cover_detailed_catalog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (commentItemListener != null) {
                            commentItemListener.startCatalogActivity();
                        }
                    }
                });
            }
        } else if (viewHolder instanceof RecommendCardHolder) {
            if (commentItem.books != null && commentItem.books.size() > 0) {
                ((RecommendCardHolder) viewHolder).initView(contextReference.get(), "精彩推荐", commentItem.books, bookListener);
            }
        } else if (viewHolder instanceof CommentCountHolder) {

            this.count = commentItem.count;

            ((CommentCountHolder) viewHolder).initView(count, typeface_song);
            ((CommentCountHolder) viewHolder).comment_write.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (UserDao.checkUserLogin()) {
                        if (commentItemListener != null) {
                            commentItemListener.showReviewWriteView();
                        }
                    } else {
                        if (commentItemListener != null) {
                            commentItemListener.startLoginActivity();
                        }
                    }
                }
            });
        } else if (viewHolder instanceof CommentFlagHolder) {
            if (commentItem.type == CommentAdapter.TYPE_COMMENT_NEW_FLAG) {
                ((CommentFlagHolder) viewHolder).comment_flat_image.setImageResource(R.drawable.icon_comment_new);
            } else {
                ((CommentFlagHolder) viewHolder).comment_flat_image.setImageResource(R.drawable.icon_comment_hot);
            }
        } else if (viewHolder instanceof CommentHolder) {
            if (commentItem.comment == null) {
                return;
            }

            Comment comment = commentItem.comment;

            ((CommentHolder) viewHolder).initParameter(true);
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
        } else if (viewHolder instanceof CommentMoreHolder) {
            ((CommentMoreHolder) viewHolder).comment_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (commentItemListener != null) {
                        commentItemListener.startCommentListActivity();
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return commentItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position > -1 && position < commentItems.size()) {
            return commentItems.get(position).type;
        } else {
            return TYPE_COMMENT_EMPTY;
        }
    }

    public void setCommentItemListener(CommentItemListener commentItemListener) {
        this.commentItemListener = commentItemListener;
    }

    public void setBookListener(BookListener bookListener) {
        this.bookListener = bookListener;
    }

    public void recyclerResource() {
        if (commentItems != null) {
            commentItems.clear();
            commentItems = null;
        }

        if (contextReference.get() != null) {
            contextReference.clear();
        }

        if (commentItemListener != null) {
            commentItemListener = null;
        }
    }
}
