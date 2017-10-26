package com.quduquxie.module.comment.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quduquxie.R;
import com.quduquxie.base.util.TypefaceUtil;
import com.quduquxie.model.Review;
import com.quduquxie.module.comment.util.RenderUtil;
import com.quduquxie.module.comment.listener.BookClickedListener;
import com.quduquxie.module.comment.viewholder.UserDynamicBookHolder;
import com.quduquxie.module.comment.viewholder.UserDynamicReplyHolder;
import com.quduquxie.module.comment.viewholder.UserDynamicCommentHolder;
import com.quduquxie.viewholder.ListEndHolder;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created on 17/3/1.
 * Created by crazylei.
 */

public class UserDynamicAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private WeakReference<Context> contextReference;
    private ArrayList<Review> dynamics;

    private OnDynamicClickListener onDynamicClickListener;

    public static final int TYPE_LIST_END = 0x90;

    private LayoutInflater layoutInflater;
    private Typeface typeface_song;

    public UserDynamicAdapter(Context context, ArrayList<Review> dynamics) {
        this.dynamics = dynamics;
        this.contextReference = new WeakReference<>(context);
        this.layoutInflater = LayoutInflater.from(contextReference.get());
        this.typeface_song = TypefaceUtil.loadTypeface(contextReference.get(), TypefaceUtil.TYPEFACE_SONG);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_LIST_END:
                return new ListEndHolder(layoutInflater.inflate(R.layout.layout_item_list_end, parent, false));
            case 0:
                return new UserDynamicBookHolder(layoutInflater.inflate(R.layout.layout_item_dynamic_book, parent, false));
            case 1:
                return new UserDynamicCommentHolder(layoutInflater.inflate(R.layout.layout_item_dynamic_comment, parent, false));
            case 2:
                return new UserDynamicReplyHolder(layoutInflater.inflate(R.layout.layout_item_dynamic_reply, parent, false));
            default:
                return new UserDynamicReplyHolder(layoutInflater.inflate(R.layout.layout_item_dynamic_reply, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final Review dynamic = dynamics.get(position);
        if (dynamic == null) {
            return;
        }
        if (viewHolder instanceof UserDynamicBookHolder) {
            ((UserDynamicBookHolder) viewHolder).initView(contextReference.get(), dynamic, typeface_song);
            if (dynamic.book != null) {
                if (((UserDynamicBookHolder) viewHolder).user_dynamic_book_view != null) {
                    if (!TextUtils.isEmpty(dynamic.book.id)) {
                        ((UserDynamicBookHolder) viewHolder).user_dynamic_book_view.setTag(R.id.click_object, dynamic.book.id);
                        ((UserDynamicBookHolder) viewHolder).user_dynamic_book_view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String id = (String) view.getTag(R.id.click_object);
                                if (onDynamicClickListener != null) {
                                    onDynamicClickListener.startCoverActivity(id);
                                }
                            }
                        });
                    }
                }

                if (((UserDynamicBookHolder) viewHolder).user_dynamic_details != null) {
                    ((UserDynamicBookHolder) viewHolder).user_dynamic_details.setClickable(true);
                    ((UserDynamicBookHolder) viewHolder).user_dynamic_details.setMovementMethod(LinkMovementMethod.getInstance());

                    String content = "发布了新书《" + dynamic.book.name + "》";

                    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(content);
                    RenderUtil.renderBook(content, dynamic.book, spannableStringBuilder, new BookClickedListener() {
                        @Override
                        public void onClickedBookSpan(String id_book) {
                            if (onDynamicClickListener != null) {
                                onDynamicClickListener.startCoverActivity(id_book);
                            }
                        }
                    });

                    ((UserDynamicBookHolder) viewHolder).user_dynamic_details.setText(spannableStringBuilder);
                }
            }
        } else if (viewHolder instanceof UserDynamicCommentHolder) {
            ((UserDynamicCommentHolder) viewHolder).initView(contextReference.get(), dynamic, typeface_song);

            if (dynamic.book != null) {
                if (((UserDynamicCommentHolder) viewHolder).user_dynamic_details != null) {
                    ((UserDynamicCommentHolder) viewHolder).user_dynamic_details.setClickable(true);
                    ((UserDynamicCommentHolder) viewHolder).user_dynamic_details.setMovementMethod(LinkMovementMethod.getInstance());

                    String content = "对《" + dynamic.book.name + "》发表了书评";

                    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(content);
                    RenderUtil.renderBook(content, dynamic.book, spannableStringBuilder, new BookClickedListener() {
                        @Override
                        public void onClickedBookSpan(String id_book) {
                            if (onDynamicClickListener != null) {
                                onDynamicClickListener.startCoverActivity(id_book);
                            }
                        }
                    });

                    ((UserDynamicCommentHolder) viewHolder).user_dynamic_details.setText(spannableStringBuilder);
                }

                if (((UserDynamicCommentHolder) viewHolder).user_dynamic_comment_view != null) {
                    ((UserDynamicCommentHolder) viewHolder).user_dynamic_comment_view.setTag(R.id.click_object, dynamic);
                    ((UserDynamicCommentHolder) viewHolder).user_dynamic_comment_view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Review review = (Review) view.getTag(R.id.click_object);
                            if (onDynamicClickListener != null && review.book != null && review.comments != null) {
                                onDynamicClickListener.startCommentDetailsActivity(review.book.id, review.comments.id);
                            }
                        }
                    });
                }
            }

            if (((UserDynamicCommentHolder) viewHolder).user_dynamic_reply != null) {
                ((UserDynamicCommentHolder) viewHolder).user_dynamic_reply.setVisibility(View.VISIBLE);
                ((UserDynamicCommentHolder) viewHolder).user_dynamic_reply.setTag(R.id.click_object, dynamic);
                ((UserDynamicCommentHolder) viewHolder).user_dynamic_reply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Review review = (Review) view.getTag(R.id.click_object);
                        if (onDynamicClickListener != null && review != null) {
                            onDynamicClickListener.onClickedDynamic(dynamic);
                        }
                    }
                });
            }
        } else if (viewHolder instanceof UserDynamicReplyHolder) {
            ((UserDynamicReplyHolder) viewHolder).initView(contextReference.get(), dynamic, typeface_song);
            if (dynamic.book != null && dynamic.comments != null) {
                if (((UserDynamicReplyHolder) viewHolder).user_dynamic_reply_view != null) {
                    ((UserDynamicReplyHolder) viewHolder).user_dynamic_reply_view.setTag(R.id.click_object, dynamic);
                    ((UserDynamicReplyHolder) viewHolder).user_dynamic_reply_view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Review review = (Review) view.getTag(R.id.click_object);
                            if (onDynamicClickListener != null && review.book != null && review.comments != null) {
                                onDynamicClickListener.startCommentDetailsActivity(review.book.id, review.comments.id);
                            }
                        }
                    });
                }
            }

            if (dynamic.book != null) {
                if (((UserDynamicReplyHolder) viewHolder).user_dynamic_details != null) {
                    ((UserDynamicReplyHolder) viewHolder).user_dynamic_details.setClickable(true);
                    ((UserDynamicReplyHolder) viewHolder).user_dynamic_details.setMovementMethod(LinkMovementMethod.getInstance());

                    String content = "对《" + dynamic.book.name + "》发表了评论";

                    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(content);
                    RenderUtil.renderBook(content, dynamic.book, spannableStringBuilder, new BookClickedListener() {
                        @Override
                        public void onClickedBookSpan(String id_book) {
                            if (onDynamicClickListener != null) {
                                onDynamicClickListener.startCoverActivity(id_book);
                            }
                        }
                    });

                    ((UserDynamicReplyHolder) viewHolder).user_dynamic_details.setText(spannableStringBuilder);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return dynamics.size();
    }

    @Override
    public int getItemViewType(int position) {
        return dynamics.get(position).type;
    }

    public void setDynamicClickListener(OnDynamicClickListener onDynamicClickListener) {
        this.onDynamicClickListener = onDynamicClickListener;
    }

    public interface OnDynamicClickListener {
        void startCoverActivity(String id_book);

        void startCommentDetailsActivity(String id_book, String id_comment);

        void onClickedDynamic(Review review);
    }
}
