package com.quduquxie.function.creation.draft.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quduquxie.R;
import com.quduquxie.base.util.TypefaceUtil;
import com.quduquxie.function.creation.draft.viewholder.DraftHolder;
import com.quduquxie.model.creation.Draft;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created on 17/4/10.
 * Created by crazylei.
 */

public class DraftListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private WeakReference<Context> contextReference;
    private ArrayList<Draft> drafts;

    private Typeface typeface_song;

    private OnDrafterClickListener onDrafterClickListener;

    public DraftListAdapter(Context context, ArrayList<Draft> drafts) {
        this.contextReference = new WeakReference<>(context);
        this.drafts = drafts;
        this.typeface_song = TypefaceUtil.loadTypeface(contextReference.get(), TypefaceUtil.TYPEFACE_SONG);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DraftHolder(LayoutInflater.from(contextReference.get()).inflate(R.layout.layout_item_draft, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        Draft draft = drafts.get(position);
        if (draft == null) {
            return;
        }

        ((DraftHolder) viewHolder).initView(draft, typeface_song);

        ((DraftHolder) viewHolder).draft_content.setTag(R.id.click_object, draft);
        ((DraftHolder) viewHolder).draft_content.setTag(R.id.click_position, position);
        ((DraftHolder) viewHolder).draft_content.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (onDrafterClickListener != null) {
                    int index = (int) view.getTag(R.id.click_position);
                    Draft clickedObject = (Draft) view.getTag(R.id.click_object);
                    onDrafterClickListener.onDraftLongClick(clickedObject, index);
                    return true;
                }
                return false;
            }
        });

        ((DraftHolder) viewHolder).draft_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onDrafterClickListener != null) {
                    int index = (int) view.getTag(R.id.click_position);
                    Draft clickedObject = (Draft) view.getTag(R.id.click_object);
                    onDrafterClickListener.onDraftClick(clickedObject, index);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return drafts.size();
    }

    public void setOnDrafterClickListener(OnDrafterClickListener onDrafterClickListener) {
        this.onDrafterClickListener = onDrafterClickListener;
    }

    public interface OnDrafterClickListener {
        void onDraftLongClick(Draft draft, int position);

        void onDraftClick(Draft draft, int position);
    }
}
