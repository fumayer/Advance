package com.quduquxie.function.creation.write.view;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.orhanobut.logger.Logger;
import com.quduquxie.R;
import com.quduquxie.application.component.ApplicationComponent;
import com.quduquxie.base.config.BaseConfig;
import com.quduquxie.communal.dialog.CustomDialogFragment;
import com.quduquxie.communal.dialog.CustomDialogListener;
import com.quduquxie.communal.utils.UserUtil;
import com.quduquxie.creation.write.widget.LiteratureWriteContentView;
import com.quduquxie.creation.write.widget.utils.LiteratureWriteContentListener;
import com.quduquxie.function.BaseActivity;
import com.quduquxie.function.creation.write.SectionWriteInterface;
import com.quduquxie.function.creation.write.component.DaggerSectionWriteComponent;
import com.quduquxie.function.creation.write.module.SectionWriteModule;
import com.quduquxie.function.creation.write.presenter.SectionWritePresenter;
import com.quduquxie.model.creation.Draft;
import com.quduquxie.model.creation.Literature;
import com.quduquxie.model.v2.SensitiveWord;
import com.quduquxie.widget.LoadingPage;
import com.quduquxie.wxapi.WXEntryActivity;

import java.text.MessageFormat;
import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created on 17/4/10.
 * Created by crazylei.
 */

public class SectionWriteActivity extends BaseActivity implements SectionWriteInterface.View, LiteratureWriteContentListener {

    @BindView(R.id.section_write_back)
    public ImageView section_write_back;
    @BindView(R.id.section_write_length)
    public TextView section_write_length;
    @BindView(R.id.section_write_save_draft)
    public TextView section_write_save_draft;
    @BindView(R.id.section_write_publish)
    public TextView section_write_publish;
    @BindView(R.id.section_write_view)
    public RelativeLayout section_write_view;
    @BindView(R.id.section_write_main)
    public LinearLayout section_write_main;
    @BindView(R.id.section_write_chapter)
    public TextView section_write_chapter;
    @BindView(R.id.section_write_content)
    public LiteratureWriteContentView section_write_content;

    @BindView(R.id.section_write_empty)
    public RelativeLayout section_write_empty;

    @Inject
    SectionWritePresenter sectionWritePresenter;

    private Handler handler = new Handler();

    private String type;

    private Draft draft;
    private Literature literature;

    private Toast toast;

    private LoadingPage loadingPage;

    private Runnable runnable;

    private Draft autoDraft;

    private CustomDialogFragment customDialogFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            setContentView(R.layout.layout_activity_section_write);

            initParameter();
        } catch (Resources.NotFoundException exception) {
            exception.printStackTrace();
            collectException(exception);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(this);

        if ("draft".equals(type)) {
            if (draft != null && !TextUtils.isEmpty(draft.id)) {

                autoDraft = draft;

                sectionWritePresenter.initDraft(draft);
            } else {
                showErrorView();
            }
        }

        startAutoUpdateTask();
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (section_write_content != null) {
            section_write_content.recycleData();
        }

        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
    }

    @Override
    protected void setActivityComponent(ApplicationComponent applicationComponent) {
        DaggerSectionWriteComponent.builder()
                .applicationComponent(applicationComponent)
                .sectionWriteModule(new SectionWriteModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(SectionWriteInterface.Presenter sectionWritePresenter) {
        this.sectionWritePresenter = (SectionWritePresenter) sectionWritePresenter;
    }

    @Override
    public void initParameter() {
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent != null) {
            type = intent.getStringExtra("type");
            draft = (Draft) intent.getSerializableExtra("draft");
            literature = (Literature) intent.getSerializableExtra("literature");
        }

        sectionWritePresenter.initParameter(literature);
    }

    @Override
    public void initView(int limit, String title) {
        if (section_write_chapter != null) {
            section_write_chapter.setTypeface(typeface_song_depict);
            section_write_chapter.setText(title);
        }

        if (section_write_content != null) {
            section_write_content.setLiteratureWriteContentListener(this);
            section_write_content.setTitleLimit(limit);
        }
    }

    @Override
    public void setChapterInformation(String title, String content) {
        if (section_write_content != null) {
            section_write_content.initChapterInformation(title, content);
        }
    }

    @Override
    public void showWriteView() {
        if (section_write_empty != null) {
            section_write_empty.setVisibility(View.GONE);
        }

        if (section_write_main != null) {
            section_write_main.setVisibility(View.VISIBLE);
        }

        refreshSaveButtonState(true);

        refreshPublishButtonState(true);
    }

    @Override
    public void showErrorView() {
        if (section_write_empty != null) {
            section_write_empty.setVisibility(View.VISIBLE);
        }

        if (section_write_main != null) {
            section_write_main.setVisibility(View.GONE);
        }

        refreshSaveButtonState(false);

        refreshPublishButtonState(false);
    }

    @Override
    public void showToast(String message) {
        if (TextUtils.isEmpty(message)) {
            return;
        }

        if (toast == null) {
            toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        } else {
            toast.setText(message);
        }

        if (!isFinishing() && toast != null) {
            toast.show();
        }
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
        if (section_write_publish != null) {
            section_write_publish.setEnabled(state);
        }
    }

    @Override
    public void refreshSaveButtonState(boolean state) {
        if (section_write_save_draft != null) {
            section_write_save_draft.setEnabled(state);
        }
    }

    @Override
    public void showLoadingPage() {
        if (loadingPage != null) {
            loadingPage.onSuccess();
        }

        if (loadingPage == null) {
            loadingPage = new LoadingPage(this, section_write_view);
        }
        sectionWritePresenter.setLoadingPage(loadingPage);
    }

    @Override
    public void hideLoadingPage() {
        if (loadingPage != null) {
            loadingPage.onSuccess();
        }
    }

    @Override
    public void showLoadingError() {
        if (loadingPage != null) {
            loadingPage.onError();
        }
    }

    @Override
    public void setAutoUpdateDraft(Draft draft) {
        this.autoDraft = draft;
    }

    @Override
    public void highlightSensitiveWord(ArrayList<SensitiveWord> sensitiveWords) {
        refreshPublishButtonState(true);
        if (section_write_content != null) {
            section_write_content.setSensitiveWord(sensitiveWords);
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
        refreshPublishButtonState(false);
        sectionWritePresenter.publishChapter(autoDraft, section_write_content.getTitle(), section_write_content.getContent());
    }

    @Override
    public void setContentLength(int length) {
        if (section_write_length != null) {
            section_write_length.setText(MessageFormat.format("{0}字", String.valueOf(length)));
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

    @OnClick({R.id.section_write_back, R.id.section_write_save_draft, R.id.section_write_publish})
    public void onClickListener(View view) {
        switch (view.getId()) {
            case R.id.section_write_back:
                finish();
                break;
            case R.id.section_write_save_draft:
                Logger.d("点击存草稿！");
                section_write_save_draft.setEnabled(false);
                if (sectionWritePresenter != null) {
                    if (autoDraft == null) {
                        sectionWritePresenter.saveDraft(section_write_content.getTitle(), section_write_content.getContent());
                    } else {
                        sectionWritePresenter.reviseDraft(autoDraft, section_write_content.getTitle(), section_write_content.getContent());
                    }
                }
                break;
            case R.id.section_write_publish:
                Logger.d("点击发布！");
                if (literature.attribute.equals("finish")) {
                    Toast.makeText(getApplicationContext(), "当前作品已完结，无法更新！", Toast.LENGTH_SHORT).show();
                } else {
                    refreshPublishButtonState(false);
                    hideKeyboard(view);
                    sectionWritePresenter.filterSensitiveWord(section_write_content.getContent());
                }
                break;
        }
    }

    private void startAutoUpdateTask() {
        runnable = new Runnable() {
            @Override
            public void run() {

                if (autoDraft != null) {
                    sectionWritePresenter.updateDraft(autoDraft, section_write_content.getTitle(), section_write_content.getContent());
                } else {
                    sectionWritePresenter.createDraft(section_write_content.getTitle(), section_write_content.getContent());
                }
                handler.postDelayed(runnable, BaseConfig.DEFAULT_CLOCKED_TIME);
            }
        };
        handler.postDelayed(runnable, BaseConfig.DEFAULT_CLOCKED_TIME);
    }
}