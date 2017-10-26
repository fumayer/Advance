package com.quduquxie.function.creation.draft.viewholder;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quduquxie.R;
import com.quduquxie.base.config.BaseConfig;
import com.quduquxie.model.creation.Draft;
import com.quduquxie.util.TimeUtils;

import java.text.MessageFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 17/4/10.
 * Created by crazylei.
 */

public class DraftHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.draft_content)
    public RelativeLayout draft_content;
    @BindView(R.id.draft_name)
    TextView draft_name;
    @BindView(R.id.draft_word_count)
    TextView draft_word_count;
    @BindView(R.id.draft_create_time)
    TextView draft_create_time;

    public DraftHolder(View view) {
        super(view);

        ButterKnife.bind(this, view);
    }

    public void initView(Draft draft, Typeface typeface) {
        if (draft != null) {
            draft_name.setText(TextUtils.isEmpty(draft.name) ? "青果阅读" : draft.name);
            draft_name.setTypeface(typeface);

            draft_word_count.setText(MessageFormat.format("字数：{0}", draft.word_count + "字"));

            if (draft.update_time != 0) {
                draft_create_time.setText(MessageFormat.format("创建时间：{0}", TimeUtils.compareTime(BaseConfig.simpleDateFormat, draft.update_time)));
            }
        }
    }
}
