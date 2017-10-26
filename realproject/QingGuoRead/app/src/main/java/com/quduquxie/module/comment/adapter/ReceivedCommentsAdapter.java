package com.quduquxie.module.comment.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quduquxie.R;
import com.quduquxie.base.util.TypefaceUtil;
import com.quduquxie.model.Review;
import com.quduquxie.module.comment.listener.BookClickedListener;
import com.quduquxie.module.comment.util.RenderUtil;
import com.quduquxie.module.comment.viewholder.ReceivedCommentHolder;
import com.quduquxie.viewholder.ListEndHolder;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created on 17/3/1.
 * Created by crazylei.
 */

public class ReceivedCommentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private WeakReference<Context> contextReference;
    private ArrayList<Review> receivedComments;

    private OnCommentClickedListener onCommentClickedListener;

    public static final int TYPE_LIST_END = 0x61;

    private LayoutInflater layoutInflater;

    private Typeface typeface_song;

    public ReceivedCommentsAdapter(Context context, ArrayList<Review> receivedComments) {
        this.receivedComments = receivedComments;
        this.contextReference = new WeakReference<>(context);
        this.layoutInflater = LayoutInflater.from(contextReference.get());
        this.typeface_song = TypefaceUtil.loadTypeface(contextReference.get(), TypefaceUtil.TYPEFACE_SONG);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_LIST_END:
                return new ListEndHolder(layoutInflater.inflate(R.layout.layout_item_list_end, parent, false));
            default:
                return new ReceivedCommentHolder(layoutInflater.inflate(R.layout.layout_item_received_comment, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        Review review = receivedComments.get(position);
        if (review == null) {
            return;
        }

        if (viewHolder instanceof ReceivedCommentHolder) {

            ((ReceivedCommentHolder) viewHolder).initView(contextReference.get(), review, typeface_song);

            if (review.book != null && review.comments != null) {
                if (((ReceivedCommentHolder) viewHolder).received_comment_view != null) {
                    ((ReceivedCommentHolder) viewHolder).received_comment_view.setTag(R.id.click_object, review);
                    ((ReceivedCommentHolder) viewHolder).received_comment_view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Review clickedReview = (Review) view.getTag(R.id.click_object);
                            if (onCommentClickedListener != null && clickedReview != null && clickedReview.book != null && clickedReview.comments != null) {
                                onCommentClickedListener.startCommentDetailsActivity(clickedReview.book.id, clickedReview.comments.id);
                            }
                        }
                    });
                }
            }

            if (review.book != null) {
                if (((ReceivedCommentHolder) viewHolder).received_comment_detail != null) {
                    ((ReceivedCommentHolder) viewHolder).received_comment_detail.setClickable(true);
                    ((ReceivedCommentHolder) viewHolder).received_comment_detail.setMovementMethod(LinkMovementMethod.getInstance());

                    String content = "《" + review.book.name + "》收到一条新的书评";

                    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(content);
                    RenderUtil.renderBook(content, review.book, spannableStringBuilder, new BookClickedListener() {
                        @Override
                        public void onClickedBookSpan(String id_book) {
                            if (onCommentClickedListener != null) {
                                onCommentClickedListener.startCoverActivity(id_book);
                            }
                        }
                    });

                    ((ReceivedCommentHolder) viewHolder).received_comment_detail.setText(spannableStringBuilder);
                }
            }

            if (((ReceivedCommentHolder) viewHolder).received_comment_reply != null) {
                ((ReceivedCommentHolder) viewHolder).received_comment_reply.setTag(R.id.click_object, review);
                ((ReceivedCommentHolder) viewHolder).received_comment_reply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Review clickedReview = (Review) view.getTag(R.id.click_object);
                        if (onCommentClickedListener != null) {
                            onCommentClickedListener.onClickedCommentReply(clickedReview);
                        }
                    }
                });
            }
        }

    }

    @Override
    public int getItemViewType(int position) {
        return receivedComments.get(position).type;
    }

    @Override
    public int getItemCount() {
        return receivedComments.size();
    }

    public void setOnCommentClickedListener(OnCommentClickedListener onCommentClickedListener) {
        this.onCommentClickedListener = onCommentClickedListener;
    }

    public interface OnCommentClickedListener {
        void startCoverActivity(String id_book);

        void startCommentDetailsActivity(String id_book, String id_comment);

        void onClickedCommentReply(Review review);
    }
}
