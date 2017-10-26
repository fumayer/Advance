package com.quduquxie.base.module.catalog.viewholder;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.quduquxie.R;
import com.quduquxie.base.bean.Chapter;
import com.quduquxie.base.config.BaseConfig;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 17/5/5.
 * Created by crazylei.
 */

public class ChapterHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.chapter_content)
    public View chapter_content;
    @BindView(R.id.chapter_name)
    public TextView chapter_name;
    @BindView(R.id.chapter_divider)
    View chapter_divider;

    public ChapterHolder(View view) {
        super(view);

        ButterKnife.bind(this, view);
    }

    public void initializeView(Chapter chapter, String form) {
        if (chapter != null) {
            if (chapter_name != null) {

                if ("ReadingCatalogActivity".equals(form)) {
                    boolean viewState = (BaseConfig.READING_BACKGROUND_MODE == 5 || BaseConfig.READING_BACKGROUND_MODE == 6);

                    chapter_content.setBackgroundResource(viewState ? R.drawable.background_dark_view_holder : R.drawable.background_light_view_holder);

                    chapter_name.setTextColor(viewState ? Color.parseColor("#9B9B9B") : Color.parseColor("#3E3E3E"));

                    chapter_divider.setBackgroundColor(viewState ? Color.parseColor("#464646") : Color.parseColor("#D8D8D8"));
                } else {
                    chapter_content.setBackgroundResource(R.drawable.background_light_view_holder);

                    chapter_name.setTextColor(Color.parseColor("#3E3E3E"));

                    chapter_divider.setBackgroundColor(Color.parseColor("#D8D8D8"));
                }

                chapter_name.setText(TextUtils.isEmpty(chapter.name) ? "青果阅读" : chapter.name);
            }
        }
    }
}