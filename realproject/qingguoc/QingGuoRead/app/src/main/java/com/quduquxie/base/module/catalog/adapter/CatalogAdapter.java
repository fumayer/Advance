package com.quduquxie.base.module.catalog.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quduquxie.R;
import com.quduquxie.base.bean.Chapter;
import com.quduquxie.base.config.BaseConfig;
import com.quduquxie.base.listener.ChapterListener;
import com.quduquxie.base.module.catalog.viewholder.ChapterHolder;
import com.quduquxie.base.util.TypefaceUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created on 17/5/5.
 * Created by crazylei.
 */

public class CatalogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private WeakReference<Context> contextReference;

    private ArrayList<Chapter> chapters;

    private LayoutInflater layoutInflater;

    private int selected_position = 0;

    private ChapterListener chapterListener;

    private String form;

    public CatalogAdapter(Context context, ArrayList<Chapter> chapters) {
        this.contextReference = new WeakReference<>(context);
        this.chapters = chapters;
        this.layoutInflater = LayoutInflater.from(contextReference.get());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ChapterHolder(layoutInflater.inflate(R.layout.layout_item_chapter, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        Chapter chapter = chapters.get(position);

        if (chapter == null) {
            return;
        }

        ((ChapterHolder) viewHolder).initializeView(chapter, form);

        if (position == selected_position) {
            ((ChapterHolder) viewHolder).chapter_name.setTextColor(Color.parseColor("#0094D5"));
        }

        ((ChapterHolder) viewHolder).chapter_content.setTag(R.id.click_object, chapter);
        ((ChapterHolder) viewHolder).chapter_content.setTag(R.id.click_position, position);
        ((ChapterHolder) viewHolder).chapter_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chapterListener != null) {
                    int index = (int) view.getTag(R.id.click_position);
                    Chapter clickedChapter = (Chapter) view.getTag(R.id.click_object);
                    if (clickedChapter != null && !TextUtils.isEmpty(clickedChapter.id)) {
                        chapterListener.onClickedChapter(clickedChapter, index);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (chapters == null || chapters.isEmpty()) {
            return 0;
        } else {
            return chapters.size();
        }
    }

    public void setSelectedPosition(int position) {
        if (position >= chapters.size()) {
            position = chapters.size() - 1;
        }
        selected_position = position;
    }

    public void setChapterListener(ChapterListener chapterListener) {
        this.chapterListener = chapterListener;
    }

    public void setActivityForm(String form) {
        this.form = form;
    }
}