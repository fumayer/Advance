package com.quduquxie.module.copyright.view;

import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.quduquxie.R;
import com.quduquxie.base.util.TypefaceUtil;
import com.quduquxie.module.copyright.CopyrightInterface;
import com.quduquxie.module.copyright.presenter.CopyrightPresenter;
import com.quduquxie.view.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created on 16/11/25.
 * Created by crazylei.
 */

public class CopyrightActivity extends BaseActivity implements CopyrightInterface.View {

    @BindView(R.id.common_head_back)
    public ImageView common_head_back;
    @BindView(R.id.common_head_title)
    public TextView common_head_title;
    @BindView(R.id.copyright_content)
    public TextView copyright_content;

    private CopyrightInterface.Presenter copyrightPresenter;

    private Typeface typeface_song_depict;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.layout_activity_copyright);
        } catch (Resources.NotFoundException exception) {
            collectException(exception);
            exception.printStackTrace();
        }

        ButterKnife.bind(this);

        typeface_song_depict = TypefaceUtil.loadTypeface(this, TypefaceUtil.TYPEFACE_SONG_DEPICT);

        copyrightPresenter = new CopyrightPresenter(this);
        copyrightPresenter.init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void setPresenter(CopyrightInterface.Presenter copyrightPresenter) {
        this.copyrightPresenter = copyrightPresenter;
    }

    @Override
    public void initView() {
        if (common_head_title != null) {
            common_head_title.setText(R.string.copyright_notice);
            common_head_title.setTypeface(typeface_song_depict);
        }

        if (copyright_content != null) {
            copyright_content.setText(R.string.copyright_notice_content);
        }
    }

    @OnClick({R.id.common_head_back})
    public void onClickListener(View view) {
        switch (view.getId()) {
            case R.id.common_head_back:
                finish();
                break;
        }
    }
}
