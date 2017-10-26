package com.quduquxie.function.creation.section.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quduquxie.R;
import com.quduquxie.db.DraftDao;
import com.quduquxie.model.creation.Literature;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 17/4/12.
 * Created by crazylei.
 */

public class DraftCountHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.draft_count_view)
    public RelativeLayout draft_count_view;
    @BindView(R.id.draft_count_number)
    TextView draft_count_number;

    private DraftDao draftDao;

    public DraftCountHolder(View view) {
        super(view);

        ButterKnife.bind(this, view);
    }

    public void initView(Context context, Literature literature) {
        if (draft_count_number != null) {
            if (draftDao == null) {
                draftDao = DraftDao.getInstance(context);
            }

            int count = 0;

            if (literature != null && !TextUtils.isEmpty(literature.id)) {
                count = draftDao.getDraftCount(literature.id);
            }

            if (count < 100) {
                draft_count_number.setText(String.valueOf(count));
                draft_count_number.setBackgroundResource(R.drawable.icon_blue_count_circle);
            } else {
                draft_count_number.setText("99+");
                draft_count_number.setBackgroundResource(R.drawable.icon_blue_count_ellipse);
            }
        }
    }
}
