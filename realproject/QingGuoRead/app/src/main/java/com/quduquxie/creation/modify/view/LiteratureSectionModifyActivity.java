package com.quduquxie.creation.modify.view;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.quduquxie.R;
import com.quduquxie.communal.dialog.CustomDialogFragment;
import com.quduquxie.communal.dialog.CustomDialogListener;
import com.quduquxie.communal.utils.UserUtil;
import com.quduquxie.creation.modify.LiteratureSectionModifyInterface;
import com.quduquxie.creation.modify.presenter.LiteratureSectionModifyPresenter;
import com.quduquxie.creation.modify.widget.LiteratureSectionModifyContentView;
import com.quduquxie.creation.modify.widget.utils.LiteratureSectionModifyContentListener;
import com.quduquxie.model.creation.Literature;
import com.quduquxie.model.creation.Section;
import com.quduquxie.model.v2.SensitiveWord;
import com.quduquxie.view.BaseActivity;
import com.quduquxie.wxapi.WXEntryActivity;

import java.text.MessageFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created on 16/11/17.
 * Created by crazylei.
 */

public class LiteratureSectionModifyActivity extends BaseActivity implements LiteratureSectionModifyInterface.View, LiteratureSectionModifyContentListener {

    private LiteratureSectionModifyPresenter literatureSectionModifyPresenter;

    @BindView(R.id.literature_section_revise_back)
    public ImageView literature_section_revise_back;
    @BindView(R.id.literature_section_revise_content_length)
    public TextView literature_section_revise_content_length;
    @BindView(R.id.literature_section_revise_publish)
    public TextView literature_section_revise_publish;
    @BindView(R.id.literature_section_revise_chapter)
    public TextView literature_section_revise_chapter;
    @BindView(R.id.literature_section_revise_content_view)
    public LiteratureSectionModifyContentView literature_section_revise_content_view;

    private Handler handler = new Handler();

    private Section section;
    private Literature literature;

    private CustomDialogFragment customDialogFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.layout_activity_literature_section_revise);
        } catch (Resources.NotFoundException exception) {
            collectException(exception);
            exception.printStackTrace();
        }

        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent != null) {
            section = (Section) intent.getSerializableExtra("section");
            literature = (Literature) intent.getSerializableExtra("literature");
        }


        literatureSectionModifyPresenter = new LiteratureSectionModifyPresenter(this, getApplicationContext());
        literatureSectionModifyPresenter.initParameter(section);
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(this);

        if (section != null) {
            if (literatureSectionModifyPresenter == null) {
                literatureSectionModifyPresenter = new LiteratureSectionModifyPresenter(this, getApplicationContext());
            }
            literatureSectionModifyPresenter.initSection(section);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (literature_section_revise_content_view != null) {
            literature_section_revise_content_view.recycleData();
        }
    }

    @Override
    public void setPresenter(LiteratureSectionModifyInterface.Presenter writeNovelPresenter) {
        this.literatureSectionModifyPresenter = (LiteratureSectionModifyPresenter) writeNovelPresenter;
    }

    @Override
    public void initView(int limit, String title) {
        if (literature_section_revise_chapter != null) {
            literature_section_revise_chapter.setText(title);
        }

        if (literature_section_revise_content_view != null) {
            literature_section_revise_content_view.setLiteratureSectionModifyContentListener(this);
            literature_section_revise_content_view.setTitleLimit(limit);
        }
    }

    @Override
    public void setChapterInformation(String title, String content) {
        if (literature_section_revise_content_view != null) {
            literature_section_revise_content_view.initChapterInformation(title, content);
        }
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void finishActivity() {
        finish();
    }

    @Override
    public void startLoginActivity() {
        UserUtil.deleteUser(getApplicationContext());
        Intent intent = new Intent();
        intent.putExtra("exit_login", true);
        intent.setClass(this, WXEntryActivity.class);
        startActivity(intent);
    }

    @Override
    public void refreshPublishButtonState(boolean state) {
        if (literature_section_revise_publish != null) {
            literature_section_revise_publish.setClickable(true);
        }
    }

    @Override
    public void highlightSensitiveWord(ArrayList<SensitiveWord> sensitiveWords) {
        refreshPublishButtonState(true);
        if (literature_section_revise_content_view != null) {
            literature_section_revise_content_view.setSensitiveWord(sensitiveWords);
        }
    }

    @Override
    public void showPromptDialog(String message) {
        if (customDialogFragment == null) {
            customDialogFragment = new CustomDialogFragment();
        }

        customDialogFragment.setPrompt(message);
        customDialogFragment.setFirstOption("取消");
        customDialogFragment.setSecondOption("继续");
        customDialogFragment.setCustomDialogListener(new CustomDialogListener() {
            @Override
            public void onFirstOptionClicked() {
                refreshPublishButtonState(true);
                hideCustomDeleteDialogFragment();
            }

            @Override
            public void onSecondOptionClicked() {
                hideCustomDeleteDialogFragment();
                publishChapter();
            }
        });

        if (!isFinishing()) {
            if (customDialogFragment.isAdded()) {
                customDialogFragment.setShowsDialog(true);
            } else {
                customDialogFragment.show(getSupportFragmentManager(), "CustomDialogFragment");
            }
        }
    }

    public void hideCustomDeleteDialogFragment() {
        if (!isFinishing() && customDialogFragment != null && customDialogFragment.getShowsDialog()) {
            customDialogFragment.dismiss();
        }
    }

    @Override
    public void publishChapter() {
        refreshPublishButtonState(true);
        literatureSectionModifyPresenter.publishChapter(section, literature_section_revise_content_view.getTitle(), literature_section_revise_content_view.getContent());
    }

    @OnClick({R.id.literature_section_revise_back, R.id.literature_section_revise_publish})
    public void onClickListener(View view) {
        switch (view.getId()) {
            case R.id.literature_section_revise_back:
                finish();
                break;
            case R.id.literature_section_revise_publish:
                refreshPublishButtonState(false);
                hideKeyboard(view);
                literatureSectionModifyPresenter.filterSensitiveWord(literature_section_revise_content_view.getContent());
                break;
        }
    }


    @Override
    public void setContentLength(int length) {
        if (literature_section_revise_content_length != null) {
            literature_section_revise_content_length.setText(MessageFormat.format("{0}字", String.valueOf(length)));
        }
    }

    @Override
    public void showKeyboard(final View view) {
        if (handler == null) {
            handler = new Handler();
        }
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {

                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

                if (view != null) {
                    inputMethodManager.showSoftInput(view, 0);
                }
            }
        }, 200);
    }

    @Override
    public void hideKeyboard(View view) {
        if (view == null || view.getContext() == null)
            return;
        InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive()) {
            inputMethodManager.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
        }
    }
}
