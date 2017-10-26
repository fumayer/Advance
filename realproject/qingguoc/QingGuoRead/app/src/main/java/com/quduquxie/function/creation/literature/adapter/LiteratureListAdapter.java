package com.quduquxie.function.creation.literature.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quduquxie.R;
import com.quduquxie.base.util.TypefaceUtil;
import com.quduquxie.communal.viewholder.ListEmptyFillHolder;
import com.quduquxie.function.creation.literature.listener.LiteratureListListener;
import com.quduquxie.function.creation.literature.viewholder.LiteratureCompleteHolder;
import com.quduquxie.function.creation.literature.viewholder.LiteratureHolder;
import com.quduquxie.function.creation.literature.viewholder.LiteraturePromptHolder;
import com.quduquxie.model.creation.Literature;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created on 17/4/10.
 * Created by crazylei.
 */

public class LiteratureListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private WeakReference<Context> contextReference;
    private ArrayList<Literature> literatureList;

    private LiteratureListListener literatureListListener;

    private Typeface typeface_song;

    private LayoutInflater layoutInflater;

    public static final int TYPE_PROMPT = 0x80;
    public static final int TYPE_COMPLETE_INFORMATION = 0x81;
    public static final int TYPE_EMPTY_FILL_EIGHT = 0x82;

    public LiteratureListAdapter(Context context, ArrayList<Literature> literatureList) {
        this.contextReference = new WeakReference<>(context);
        this.literatureList = literatureList;
        this.layoutInflater = LayoutInflater.from(contextReference.get());
        this.typeface_song = TypefaceUtil.loadTypeface(contextReference.get(), TypefaceUtil.TYPEFACE_SONG);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_PROMPT:
                return new LiteraturePromptHolder(layoutInflater.inflate(R.layout.layout_item_literature_prompt, parent, false));
            case TYPE_COMPLETE_INFORMATION:
                return new LiteratureCompleteHolder(layoutInflater.inflate(R.layout.layout_item_literature_complete, parent, false));
            case TYPE_EMPTY_FILL_EIGHT:
                return new ListEmptyFillHolder(layoutInflater.inflate(R.layout.layout_view_empty_fill_16, parent, false));
            default:
                return new LiteratureHolder(layoutInflater.inflate(R.layout.layout_item_literature, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        Literature literature = literatureList.get(position);
        if (literature == null) {
            return;
        }

        if (viewHolder instanceof LiteratureCompleteHolder) {
            ((LiteratureCompleteHolder) viewHolder).literature_complete_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (literatureListListener != null) {
                        literatureListListener.showReviserUser();
                    }
                }
            });
        } else if (viewHolder instanceof LiteratureHolder) {

            ((LiteratureHolder) viewHolder).initView(contextReference.get(), literature, typeface_song);

            ((LiteratureHolder) viewHolder).literature_detailed.setTag(R.id.click_object, literature);
            ((LiteratureHolder) viewHolder).literature_detailed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (literatureListListener != null) {
                        Literature clickedObject = (Literature) view.getTag(R.id.click_object);
                        if (clickedObject.chapter != null) {
                            literatureListListener.startLiteratureDetailed(clickedObject);
                        } else {
                            literatureListListener.showToast("当前作品暂无章节，请先创建章节！");
                        }
                    }
                }
            });

            ((LiteratureHolder) viewHolder).literature_create_section.setTag(R.id.click_object, literature);
            ((LiteratureHolder) viewHolder).literature_create_section.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (literatureListListener != null) {
                        Literature clickedObject = (Literature) view.getTag(R.id.click_object);
                        literatureListListener.startLiteratureChapterCreate(clickedObject);
                    }
                }
            });

            ((LiteratureHolder) viewHolder).literature_section_manager.setTag(R.id.click_object, literature);
            ((LiteratureHolder) viewHolder).literature_section_manager.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (literatureListListener != null) {
                        Literature clickedObject = (Literature) view.getTag(R.id.click_object);
                        literatureListListener.startLiteratureChapterManager(clickedObject);
                    }
                }
            });

            ((LiteratureHolder) viewHolder).literature_revise.setTag(R.id.click_object, literature);
            ((LiteratureHolder) viewHolder).literature_revise.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (literatureListListener != null) {
                        Literature clickedObject = (Literature) view.getTag(R.id.click_object);
                        literatureListListener.startLiteratureRevise(clickedObject);
                    }
                }
            });

            if (literature.chapter == null) {
                ((LiteratureHolder) viewHolder).literature_delete.setTag(R.id.click_object, literature);
                ((LiteratureHolder) viewHolder).literature_delete.setTag(R.id.click_position, position);
                ((LiteratureHolder) viewHolder).literature_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (literatureListListener != null) {
                            int position = (int) view.getTag(R.id.click_position);
                            Literature clickedObject = (Literature) view.getTag(R.id.click_object);
                            literatureListListener.deleteLiterature(position, clickedObject);
                        }
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return literatureList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return literatureList.get(position).item_type;
    }

    public void setLiteratureListListener(LiteratureListListener literatureListListener) {
        this.literatureListListener = literatureListListener;
    }
}