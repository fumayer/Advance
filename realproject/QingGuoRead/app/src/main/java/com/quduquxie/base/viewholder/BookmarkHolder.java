package com.quduquxie.base.viewholder;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quduquxie.R;
import com.quduquxie.base.bean.Bookmark;
import com.quduquxie.base.config.BaseConfig;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 17/7/28.
 * Created by crazylei.
 */

public class BookmarkHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.bookmark_content)
    public LinearLayout bookmark_content;

    @BindView(R.id.bookmark_name)
    TextView bookmark_name;
    @BindView(R.id.bookmark_desc)
    TextView bookmark_desc;
    @BindView(R.id.bookmark_divider)
    View bookmark_divider;

    public BookmarkHolder(View view) {
        super(view);

        ButterKnife.bind(this, view);
    }

    public void initializeView(Bookmark bookmark, String form) {
        if (bookmark != null) {

            if ("ReadingCatalogActivity".equals(form)) {
                boolean viewState = (BaseConfig.READING_BACKGROUND_MODE == 5 || BaseConfig.READING_BACKGROUND_MODE == 6);

                bookmark_content.setBackgroundResource(viewState ? R.drawable.background_dark_view_holder : R.drawable.background_light_view_holder);

                bookmark_name.setTextColor(viewState ? Color.parseColor("#9B9B9B") : Color.parseColor("#444444"));
                bookmark_desc.setTextColor(viewState ? Color.parseColor("#9B9B9B") : Color.parseColor("#3E3E3E"));
                bookmark_divider.setBackgroundColor(viewState ? Color.parseColor("#464646") : Color.parseColor("#D8D8D8"));
            } else {
                bookmark_content.setBackgroundResource(R.drawable.background_light_view_holder);

                bookmark_name.setTextColor(Color.parseColor("#9B9B9B"));
                bookmark_desc.setTextColor(Color.parseColor("#9B9B9B"));
                bookmark_divider.setBackgroundColor(Color.parseColor("#464646"));
            }


            bookmark_name.setText(TextUtils.isEmpty(bookmark.chapter_name) ? "青果阅读" : bookmark.chapter_name);

            String content = handleBookmarkContent(bookmark.chapter_content);
            bookmark_desc.setText(TextUtils.isEmpty(content) ? "青果阅读" : bookmark.chapter_content);
        }
    }

    private String handleBookmarkContent(String content) {
        if (content.endsWith(",") || content.endsWith("，") || content.endsWith(".") || content.endsWith("。") || content.endsWith("!") || content.endsWith("！")) {
            content = content.substring(0, content.length() - 1);
        }
        return content + "……";
    }
}