package com.quduquxie.function.creation.draft.view;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.quduquxie.R;
import com.quduquxie.application.component.ApplicationComponent;
import com.quduquxie.communal.dialog.CustomDialogFragment;
import com.quduquxie.communal.dialog.CustomDialogListener;
import com.quduquxie.communal.utils.UserUtil;
import com.quduquxie.db.DraftDao;
import com.quduquxie.function.BaseActivity;
import com.quduquxie.function.creation.draft.DraftListInterface;
import com.quduquxie.function.creation.draft.adapter.DraftListAdapter;
import com.quduquxie.function.creation.draft.adapter.DraftListAdapter.OnDrafterClickListener;
import com.quduquxie.function.creation.draft.component.DaggerDraftListComponent;
import com.quduquxie.function.creation.draft.module.DraftListModule;
import com.quduquxie.function.creation.draft.presenter.DraftListPresenter;
import com.quduquxie.function.creation.write.view.SectionWriteActivity;
import com.quduquxie.model.creation.Draft;
import com.quduquxie.model.creation.Literature;
import com.quduquxie.widget.LoadingPage;
import com.quduquxie.wxapi.WXEntryActivity;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created on 17/4/10.
 * Created by crazylei.
 */

public class DraftListActivity extends BaseActivity implements DraftListInterface.View, OnDrafterClickListener {

    @BindView(R.id.literature_head_back)
    public ImageView literature_head_back;
    @BindView(R.id.literature_head_title)
    public TextView literature_head_title;
    @BindView(R.id.literature_head_create)
    public TextView literature_head_create;
    @BindView(R.id.draft_list_content)
    public RelativeLayout draft_list_content;
    @BindView(R.id.draft_list_refresh)
    public SwipeRefreshLayout draft_list_refresh;
    @BindView(R.id.draft_list_result)
    public RecyclerView draft_list_result;
    @BindView(R.id.draft_list_empty)
    public RelativeLayout draft_list_empty;

    @Inject
    DraftListPresenter draftListPresenter;

    private LinearLayoutManager linearLayoutManager;
    private DraftListAdapter draftListAdapter;

    private ArrayList<Draft> drafts = new ArrayList<>();

    private LoadingPage loadingPage;

    private Literature literature;

    private DraftDao draftDao;

    private Toast toast;

    private CustomDialogFragment customDialogFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.layout_activity_draft_list);

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

        draftListPresenter.loadDraftList(literature);
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
    protected void setActivityComponent(ApplicationComponent applicationComponent) {
        DaggerDraftListComponent.builder()
                .applicationComponent(applicationComponent)
                .draftListModule(new DraftListModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(DraftListInterface.Presenter draftListPresenter) {
        this.draftListPresenter = (DraftListPresenter) draftListPresenter;
    }

    @Override
    public void initParameter() {
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent.getSerializableExtra("literature") != null) {
            this.literature = (Literature) intent.getSerializableExtra("literature");
        }

        this.draftDao = DraftDao.getInstance(getApplicationContext());

        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        draftListAdapter = new DraftListAdapter(getApplicationContext(), drafts);
        draftListAdapter.setOnDrafterClickListener(this);

        draftListPresenter.initParameter();
    }

    @Override
    public void initView() {
        showEmptyView(false);

        if (literature_head_title != null) {
            literature_head_title.setTypeface(typeface_song_depict);
            literature_head_title.setText(R.string.draft_box);
        }

        if (draft_list_refresh != null) {
            draft_list_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    setRefreshViewState(false);
                    draftListPresenter.loadDraftList(literature);
                }
            });
        }

        if (draft_list_result != null) {
            draft_list_result.setLayoutManager(linearLayoutManager);
            draft_list_result.setAdapter(draftListAdapter);
            draft_list_result.setItemAnimator(new DefaultItemAnimator());
        }
    }

    @Override
    public void showLoadingPage() {
        if (loadingPage != null) {
            loadingPage.onSuccess();
        }

        if (loadingPage == null) {
            loadingPage = new LoadingPage(this, draft_list_content);
        }

        draftListPresenter.setLoadingPage(loadingPage);
    }

    @Override
    public void hideLoadingPage() {
        if (loadingPage != null) {
            loadingPage.onSuccess();
        }
    }

    @Override
    public void setLiteratureDraftData(ArrayList<Draft> draftList) {
        if (draftList == null || draftList.isEmpty()) {
            showEmptyView(true);
        } else {
            showEmptyView(false);

            if (drafts == null) {
                drafts = new ArrayList<>();
            } else {
                drafts.clear();
            }

            for (Draft draft : draftList) {
                drafts.add(draft);
            }

            if (draftListAdapter == null) {
                draftListAdapter = new DraftListAdapter(getApplicationContext(), drafts);
                draftListAdapter.setOnDrafterClickListener(this);
                draft_list_result.setAdapter(draftListAdapter);
            } else {
                draftListAdapter.notifyDataSetChanged();
            }
        }
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
    public void deleteDraft(Draft draft) {
        drafts.remove(draft);

        if (draftListAdapter == null) {
            draftListAdapter = new DraftListAdapter(getApplicationContext(), drafts);
            draftListAdapter.setOnDrafterClickListener(this);
            draft_list_result.setAdapter(draftListAdapter);
        } else {
            draftListAdapter.notifyDataSetChanged();
        }

        if (drafts.size() == 0) {
            showEmptyView(true);
        } else {
            showEmptyView(false);
        }
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
    public void onDraftLongClick(final Draft draft, final int position) {
        if (position > -1 && position < drafts.size()) {

            if (customDialogFragment == null) {
                customDialogFragment = new CustomDialogFragment();
            }

            customDialogFragment.setPrompt("确认要删除草稿？");
            customDialogFragment.setFirstOption("取消");
            customDialogFragment.setSecondOption("确认");
            customDialogFragment.setCustomDialogListener(new CustomDialogListener() {
                @Override
                public void onFirstOptionClicked() {
                    hideCustomDialogFragment();
                }

                @Override
                public void onSecondOptionClicked() {
                    if (customDialogFragment.getShowsDialog()) {
                        if (draft != null && !TextUtils.isEmpty(draft.id)) {
                            draftListPresenter.deleteDraft(draft, position);
                        } else {
                            showToast("参数错误！");
                        }
                        hideCustomDialogFragment();
                    }
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
    }

    public void hideCustomDialogFragment() {
        if (!isFinishing() && customDialogFragment != null && customDialogFragment.getShowsDialog()) {
            customDialogFragment.dismiss();
        }
    }

    @Override
    public void onDraftClick(Draft draft, int position) {
        if (position > -1 && position < drafts.size()) {
            if (draftDao == null) {
                draftDao = DraftDao.getInstance(getApplicationContext());
            }
            Intent intent = new Intent();
            if (draftDao.isContainsDraft(draft.id) && draftDao.getDraft(draft.id) != null) {
                draft = draftDao.getDraft(draft.id);
            }
            intent.putExtra("type", "draft");
            intent.putExtra("draft", draft);
            intent.putExtra("literature", literature);
            intent.setClass(DraftListActivity.this, SectionWriteActivity.class);
            startActivity(intent);
        }
    }

    @OnClick({R.id.literature_head_back, R.id.literature_head_create})
    public void onClickListener(View view) {
        switch (view.getId()) {
            case R.id.literature_head_back:
                finish();
                break;
            case R.id.literature_head_create:
                if (literature.attribute.equals("finish")) {
                    Toast.makeText(getApplicationContext(), "当前作品已完结，无法更新！", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("type", "section");
                    intent.putExtra("literature", literature);
                    intent.setClass(DraftListActivity.this, SectionWriteActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }

    private void showEmptyView(boolean show) {
        if (draft_list_empty != null) {
            draft_list_empty.setVisibility(show ? View.VISIBLE : View.GONE);
        }

        if (draft_list_refresh != null) {
            draft_list_refresh.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public void setRefreshViewState(boolean state) {
        if (draft_list_refresh == null) {
            return;
        }
        if (!state) {
            draft_list_refresh.postDelayed(new Runnable() {
                @Override
                public void run() {
                    draft_list_refresh.setRefreshing(false);
                }
            }, 500);
        } else {
            draft_list_refresh.setRefreshing(true);
        }
    }
}
