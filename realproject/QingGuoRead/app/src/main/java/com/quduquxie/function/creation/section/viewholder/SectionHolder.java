package com.quduquxie.function.creation.section.viewholder;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quduquxie.R;
import com.quduquxie.base.config.BaseConfig;
import com.quduquxie.model.creation.Section;
import com.quduquxie.util.TimeUtils;

import java.text.MessageFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 17/4/10.
 * Created by crazylei.
 */

public class SectionHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.section_view)
    public RelativeLayout section_view;
    @BindView(R.id.section_serial_number)
    TextView section_serial_number;
    @BindView(R.id.section_name)
    TextView section_name;
    @BindView(R.id.section_word_count)
    TextView section_word_count;
    @BindView(R.id.section_publish_time)
    TextView section_publish_time;
    @BindView(R.id.section_publish_state)
    TextView section_publish_state;
    @BindView(R.id.section_audit_state)
    TextView section_audit_state;

    public SectionHolder(View view) {
        super(view);

        ButterKnife.bind(this, view);
    }
    
    public void initView(Section section, Typeface typeface) {
        if (section != null) {
            section_serial_number.setText(String.valueOf(section.serial_number));

            section_name.setText(TextUtils.isEmpty(section.name) ? "青果阅读" : section.name);
            section_name.setTypeface(typeface);

            section_word_count.setText(MessageFormat.format("字数：{0}", section.word_count));

            section_publish_time.setText(MessageFormat.format("发布时间：{0}", TimeUtils.compareTime(BaseConfig.simpleDateFormat, section.update_time)));

            if ("enable".equals(section.status)) {
                section_publish_state.setText("已上架");
            } else {
                section_publish_state.setText("已下架");
            }
            if ("notcheck".equals(section.check_status)) {
                section_audit_state.setText("审核中");
                section_audit_state.setTextColor(Color.parseColor("#8BC5FB"));
            } else if ("pass".equals(section.check_status)) {
                section_audit_state.setText("已过审");
                section_audit_state.setTextColor(Color.parseColor("#6E6E6E"));
            } else if ("notpass".equals(section.check_status)) {
                section_audit_state.setText("未过审");
                section_audit_state.setTextColor(Color.parseColor("#F44336"));
            }
        }
    }
}
