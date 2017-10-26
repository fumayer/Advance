package com.quduquxie.module.agreement.view;

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
import com.quduquxie.module.agreement.AgreementInterface;
import com.quduquxie.module.agreement.presenter.AgreementPresenter;
import com.quduquxie.view.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created on 16/11/25.
 * Created by crazylei.
 */

public class AgreementActivity extends BaseActivity implements AgreementInterface.View {

    @BindView(R.id.common_head_back)
    public ImageView common_head_back;
    @BindView(R.id.common_head_title)
    public TextView common_head_title;
    @BindView(R.id.agreement_content)
    public TextView agreement_content;

    private AgreementInterface.Presenter agreementPresenter;

    private Typeface typeface_song_depict;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.layout_activity_agreement);
        } catch (Resources.NotFoundException exception) {
            collectException(exception);
            exception.printStackTrace();
        }

        ButterKnife.bind(this);

        typeface_song_depict = TypefaceUtil.loadTypeface(this, TypefaceUtil.TYPEFACE_SONG_DEPICT);

        agreementPresenter = new AgreementPresenter(this);
        agreementPresenter.init();
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

    @OnClick({R.id.common_head_back})
    public void onClickListener(View view) {
        switch (view.getId()) {
            case R.id.common_head_back:
                finish();
                break;
        }
    }

    @Override
    public void setPresenter(AgreementInterface.Presenter agreementPresenter) {
        this.agreementPresenter = agreementPresenter;
    }

    @Override
    public void initView() {
        if (common_head_title != null) {
            common_head_title.setText(R.string.agreement);
            common_head_title.setTypeface(typeface_song_depict);
        }

        if (agreement_content != null) {
            agreement_content.setText(R.string.agreement_content);
        }
    }
}
