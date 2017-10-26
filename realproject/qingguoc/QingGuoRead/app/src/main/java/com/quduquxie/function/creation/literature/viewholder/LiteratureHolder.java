package com.quduquxie.function.creation.literature.viewholder;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.StringSignature;
import com.quduquxie.R;
import com.quduquxie.model.creation.Literature;

import java.text.MessageFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 17/4/10.
 * Created by crazylei.
 */

public class LiteratureHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.literature_detailed)
    public RelativeLayout literature_detailed;
    @BindView(R.id.literature_create_section)
    public TextView literature_create_section;
    @BindView(R.id.literature_section_manager)
    public TextView literature_section_manager;
    @BindView(R.id.literature_revise)
    public TextView literature_revise;
    @BindView(R.id.literature_delete)
    public TextView literature_delete;
    @BindView(R.id.literature_cover)
    ImageView literature_cover;
    @BindView(R.id.literature_sign_mark)
    ImageView literature_sign_mark;
    @BindView(R.id.literature_name)
    TextView literature_name;
    @BindView(R.id.literature_category)
    TextView literature_category;
    @BindView(R.id.literature_attribute)
    TextView literature_attribute;
    @BindView(R.id.literature_chapter_count)
    TextView literature_chapter_count;
    @BindView(R.id.literature_list_chapter)
    TextView literature_list_chapter;

    private static final String[] attributes = new String[]{"serialize", "finish"};

    public LiteratureHolder(View view) {
        super(view);

        ButterKnife.bind(this, view);

    }

    public void initView(Context context, Literature literature, Typeface typeface) {
        if (literature != null) {

            if (!TextUtils.isEmpty(literature.image_url)) {
                Glide.with(context)
                        .load(literature.image_url)
                        .signature(new StringSignature(literature.image_url))
                        .skipMemoryCache(true)
                        .error(R.drawable.icon_default)
                        .placeholder(R.drawable.icon_default)
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .fitCenter()
                        .into(literature_cover);
            } else {
                literature_cover.setImageResource(R.drawable.icon_default);
            }

            if (literature.is_sign == 1) {
                literature_sign_mark.setVisibility(View.VISIBLE);
            } else {
                literature_sign_mark.setVisibility(View.INVISIBLE);
            }

            literature_name.setText(TextUtils.isEmpty(literature.name) ? "青果阅读" : literature.name);
            literature_name.setTypeface(typeface);

            if (literature.chapter != null) {
                literature_chapter_count.setText(MessageFormat.format("{0}章", literature.chapter.serial_number));
                literature_list_chapter.setText(MessageFormat.format("最新章节：{0}", literature.chapter.name));
            } else {
                literature_chapter_count.setText("0章");
                literature_list_chapter.setText("暂无最新章节！");
            }

            literature_category.setText(TextUtils.isEmpty(literature.category) ? "暂无分类" : literature.category);
            if (!TextUtils.isEmpty(literature.attribute)) {
                if (literature.attribute.equals(attributes[1])) {
                    literature_attribute.setText("完结");
                } else {
                    literature_attribute.setText("连载");
                }
            }

            if (literature.chapter == null) {
                literature_delete.setEnabled(true);
            } else {
                literature_delete.setEnabled(false);
            }
        }
    }
}
