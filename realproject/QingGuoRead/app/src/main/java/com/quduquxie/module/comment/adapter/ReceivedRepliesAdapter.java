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
import com.quduquxie.module.comment.viewholder.ReceivedReplyHolder;
import com.quduquxie.viewholder.ListEndHolder;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created on 17/2/28.
 * Created by crazylei.
 */

public class ReceivedRepliesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private WeakReference<Context> contextReference;

    private ArrayList<Review> receivedReplies;

    private OnReplyClickedListener onReplyClickedListener;

    public static final int TYPE_LIST_END = 0x61;

    private LayoutInflater layoutInflater;

    private Typeface typeface_song;

    public ReceivedRepliesAdapter(Context context, ArrayList<Review> receivedReplies) {
        this.receivedReplies = receivedReplies;
        this.contextReference = new WeakReference<>(context);
        this.layoutInflater = LayoutInflater.from(contextReference.get());
        this.typeface_song = TypefaceUtil.loadTypeface(contextReference.get(), TypefaceUtil.TYPEFACE_SONG);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_LIST_END:
                return new ListEndHolder(LayoutInflater.from(contextReference.get()).inflate(R.layout.layout_item_list_end, parent, false));
            default:
                return new ReceivedReplyHolder(LayoutInflater.from(contextReference.get()).inflate(R.layout.layout_item_received_reply, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        Review review = receivedReplies.get(position);
        if (review == null) {
            return;
        }

        if (viewHolder instanceof ReceivedReplyHolder) {

            ((ReceivedReplyHolder) viewHolder).initView(contextReference.get(), review, typeface_song);

            if (review.book != null && review.comments != null) {
                if (((ReceivedReplyHolder) viewHolder).received_reply_view != null) {
                    ((ReceivedReplyHolder) viewHolder).received_reply_view.setTag(R.id.click_object, review);
                    ((ReceivedReplyHolder) viewHolder).received_reply_view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Review clickedReview = (Review) view.getTag(R.id.click_object);
                            if (onReplyClickedListener != null && clickedReview != null && clickedReview.book != null && clickedReview.comments != null) {
                                onReplyClickedListener.startCommentDetailsActivity(clickedReview.book.id, clickedReview.comments.id);
                            }
                        }
                    });
                }
            }

            if (review.book != null) {
                ((ReceivedReplyHolder) viewHolder).received_reply_details.setClickable(true);
                ((ReceivedReplyHolder) viewHolder).received_reply_details.setMovementMethod(LinkMovementMethod.getInstance());

                String content = "在《" + review.book.name + "》的评论中回复了你";

                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(content);
                RenderUtil.renderBook(content, review.book, spannableStringBuilder, new BookClickedListener() {
                    @Override
                    public void onClickedBookSpan(String id_book) {
                        if (onReplyClickedListener != null) {
                            onReplyClickedListener.startCoverActivity(id_book);
                        }
                    }
                });

                ((ReceivedReplyHolder) viewHolder).received_reply_details.setText(spannableStringBuilder);
            }

            if (((ReceivedReplyHolder) viewHolder).received_reply_discuss != null) {
                ((ReceivedReplyHolder) viewHolder).received_reply_discuss.setTag(R.id.click_object, review);
                ((ReceivedReplyHolder) viewHolder).received_reply_discuss.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Review clickedReview = (Review) view.getTag(R.id.click_object);
                        if (onReplyClickedListener != null) {
                            onReplyClickedListener.onClickedReceivedComment(clickedReview);
                        }
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return receivedReplies.size();
    }

    @Override
    public int getItemViewType(int position) {
        return receivedReplies.get(position).type;
    }

    public void setOnReplyClickedListener(OnReplyClickedListener onReplyClickedListener) {
        this.onReplyClickedListener = onReplyClickedListener;
    }

    public interface OnReplyClickedListener {
        void startCoverActivity(String id_book);

        void startCommentDetailsActivity(String id_book, String id_comment);

        void onClickedReceivedComment(Review review);
    }
}
