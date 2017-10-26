package com.quduquxie.function.creation.section.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quduquxie.R;
import com.quduquxie.base.util.TypefaceUtil;
import com.quduquxie.function.creation.section.viewholder.DraftCountHolder;
import com.quduquxie.function.creation.section.viewholder.SectionHolder;
import com.quduquxie.model.creation.Literature;
import com.quduquxie.model.creation.Section;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created on 17/4/10.
 * Created by crazylei.
 */

public class SectionListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private WeakReference<Context> contextReference;
    private ArrayList<Section> sectionList;

    private Literature literature;

    private OnSectionClickListener onSectionClickListener;

    private LayoutInflater layoutInflater;

    private Typeface typeface_song;

    public static final int TYPE_DRAFT_COUNT = 0x80;

    public SectionListAdapter(Context context, ArrayList<Section> sectionList) {
        this.contextReference = new WeakReference<>(context);
        this.sectionList = sectionList;
        this.layoutInflater = LayoutInflater.from(contextReference.get());
        this.typeface_song = TypefaceUtil.loadTypeface(contextReference.get(), TypefaceUtil.TYPEFACE_SONG);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_DRAFT_COUNT:
                return new DraftCountHolder(layoutInflater.inflate(R.layout.layout_item_draft_count, parent, false));
            default:
                return new SectionHolder(layoutInflater.inflate(R.layout.layout_item_section, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        Section section = sectionList.get(position);
        if (section == null) {
            return;
        }

        if (viewHolder instanceof DraftCountHolder) {
            ((DraftCountHolder) viewHolder).initView(contextReference.get(), literature);
            ((DraftCountHolder) viewHolder).draft_count_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onSectionClickListener != null) {
                        onSectionClickListener.startDraftListActivity();
                    }
                }
            });
        } else {
            ((SectionHolder) viewHolder).initView(section, typeface_song);

            ((SectionHolder) viewHolder).section_view.setTag(R.id.click_object, section);
            if (!"notcheck".equals(section.check_status)) {
                ((SectionHolder) viewHolder).section_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (onSectionClickListener != null) {
                            Section sectionObject = (Section) view.getTag(R.id.click_object);
                            onSectionClickListener.onSectionClicked(sectionObject);
                        }
                    }
                });
            } else {
                ((SectionHolder) viewHolder).section_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (onSectionClickListener != null) {
                            onSectionClickListener.showToast("当前章节正在审核，暂时无法修改！");
                        }
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return sectionList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return sectionList.get(position).item_type;
    }

    public void setLiteratureData(Literature literature) {
        this.literature = literature;
    }

    public void setOnSectionClickListener(OnSectionClickListener onSectionClickListener) {
        this.onSectionClickListener = onSectionClickListener;
    }

    public interface OnSectionClickListener {
        void onSectionClicked(Section section);

        void showToast(String message);

        void startDraftListActivity();
    }
}
