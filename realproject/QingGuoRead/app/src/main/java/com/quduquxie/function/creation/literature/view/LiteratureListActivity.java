package com.quduquxie.function.creation.literature.view;

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
import com.orhanobut.logger.Logger;
import com.quduquxie.R;
import com.quduquxie.application.component.ApplicationComponent;
import com.quduquxie.communal.dialog.CustomDialogFragment;
import com.quduquxie.communal.dialog.CustomDialogListener;
import com.quduquxie.communal.utils.UserUtil;
import com.quduquxie.creation.revise.view.LiteratureReviseActivity;
import com.quduquxie.function.BaseActivity;
import com.quduquxie.function.creation.create.view.LiteratureCreateActivity;
import com.quduquxie.function.creation.literature.LiteratureListInterface;
import com.quduquxie.function.creation.literature.adapter.LiteratureListAdapter;
import com.quduquxie.function.creation.literature.component.DaggerLiteratureListComponent;
import com.quduquxie.function.creation.literature.listener.LiteratureListListener;
import com.quduquxie.function.creation.literature.module.LiteratureListModule;
import com.quduquxie.function.creation.literature.presenter.LiteratureListPresenter;
import com.quduquxie.function.creation.section.view.SectionListActivity;
import com.quduquxie.function.creation.write.view.SectionWriteActivity;
import com.quduquxie.model.creation.Literature;
import com.quduquxie.modular.cover.view.CoverActivity;
import com.quduquxie.revise.view.ReviseUserActivity;
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

public class LiteratureListActivity extends BaseActivity implements LiteratureListInterface.View, LiteratureListListener {

    @BindView(R.id.literature_head_back)
    public ImageView literature_head_back;
    @BindView(R.id.literature_head_title)
    public TextView literature_head_title;
    @BindView(R.id.literature_head_create)
    public TextView literature_head_create;
    @BindView(R.id.literature_list_content)
    public RelativeLayout literature_list_content;
    @BindView(R.id.literature_list_refresh)
    public SwipeRefreshLayout literature_list_refresh;
    @BindView(R.id.literature_list_result)
    public RecyclerView literature_list_result;
    @BindView(R.id.literature_list_empty)
    public RelativeLayout literature_list_empty;

    @Inject
    LiteratureListPresenter literatureListPresenter;

    private LinearLayoutManager linearLayoutManager;
    private LiteratureListAdapter literatureListAdapter;

    private ArrayList<Literature> literatureList = new ArrayList<>();

    private LoadingPage loadingPage;

    private Toast toast;

    private CustomDialogFragment customDialogFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.layout_activity_literature_list);

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

        literatureListPresenter.loadLiteratureList();
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
        DaggerLiteratureListComponent.builder()
                .applicationComponent(applicationComponent)
                .literatureListModule(new LiteratureListModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(LiteratureListInterface.Presenter literatureListPresenter) {
        this.literatureListPresenter = (LiteratureListPresenter) literatureListPresenter;
    }

    @Override
    public void initParameter() {

        ButterKnife.bind(this);

        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        literatureListAdapter = new LiteratureListAdapter(getQuApplicationContext(), literatureList);
        literatureListAdapter.setLiteratureListListener(this);

        literatureListPresenter.initParameter();
    }

    @Override
    public void initView() {
        if (literature_head_title != null) {
            literature_head_title.setTypeface(typeface_song_depict);
            literature_head_title.setText("创作");
        }

        if (literature_head_create != null) {
            literature_head_create.setText("新建");
        }

        changeEmptyViewState(false);

        if (literature_list_refresh != null) {
            literature_list_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    setRefreshViewState(false);
                    literatureListPresenter.loadLiteratureList();
                }
            });
        }

        if (literature_list_result != null) {
            literature_list_result.setLayoutManager(linearLayoutManager);
            literature_list_result.setAdapter(literatureListAdapter);
            literature_list_result.setItemAnimator(new DefaultItemAnimator());
        }
    }

    @Override
    public void showLoadingPage() {
        if (loadingPage != null) {
            loadingPage.onSuccess();
        }

        if (loadingPage == null) {
            loadingPage = new LoadingPage(this, literature_list_content);
        }

        literatureListPresenter.setLoadingPage(loadingPage);
    }

    @Override
    public void hideLoadingPage() {
        if (loadingPage != null) {
            loadingPage.onSuccess();
        }
    }

    @Override
    public void setLiteratureData(ArrayList<Literature> literatureResult) {
        if (literatureResult == null || literatureResult.isEmpty()) {
            changeEmptyViewState(true);
        } else {
            changeEmptyViewState(false);

            if (literatureList == null) {
                literatureList = new ArrayList<>();
            } else {
                literatureList.clear();
            }

            for (Literature literature : literatureResult) {
                literatureList.add(literature);
            }

            if (literatureListAdapter == null) {
                literatureListAdapter = new LiteratureListAdapter(getApplicationContext(), literatureList);
                literatureListAdapter.setLiteratureListListener(this);
                literature_list_result.setAdapter(literatureListAdapter);
            } else {
                literatureListAdapter.notifyDataSetChanged();
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
    public void deleteLiterature(int position) {
        if (position > -1 && position < literatureList.size()) {

            literatureList.remove(position);
            if (position - 1 > -1 && literatureList.get(position - 1).item_type == LiteratureListAdapter.TYPE_EMPTY_FILL_EIGHT) {
                literatureList.remove(position - 1);
            }
            literatureListAdapter.notifyDataSetChanged();
        }

        if (literatureList.size() == 0) {
            changeEmptyViewState(true);
        }
    }

    @Override
    public void startLoginActivity() {
        UserUtil.deleteUser(getQuApplicationContext());
        Intent intent = new Intent();
        intent.putExtra("exit_login", true);
        intent.setClass(this, WXEntryActivity.class);
        startActivity(intent);
    }

    @Override
    public void startLiteratureDetailed(Literature literature) {
        if (literature != null && !TextUtils.isEmpty(literature.id)) {
            Intent intent = new Intent();
            intent.setClass(LiteratureListActivity.this, CoverActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("id", literature.id);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    @Override
    public void startLiteratureChapterCreate(Literature literature) {
        if (literature.attribute.equals("finish")) {
            showToast("当前作品已完结，无法更新！");
        } else {
            Logger.d("创建新章节！");
            Intent intent = new Intent();
            intent.putExtra("type", "section");
            intent.putExtra("literature", literature);
            intent.setClass(LiteratureListActivity.this, SectionWriteActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void startLiteratureChapterManager(Literature literature) {
        Logger.d("章节管理！");
        Intent intent = new Intent();
        intent.putExtra("literature", literature);
        intent.setClass(LiteratureListActivity.this, SectionListActivity.class);
        startActivity(intent);
    }

    @Override
    public void startLiteratureRevise(Literature literature) {
        Logger.d("修改作品信息！");
        Intent intent = new Intent();
        intent.putExtra("literature", literature);
        intent.setClass(LiteratureListActivity.this, LiteratureReviseActivity.class);
        startActivity(intent);
    }

    @Override
    public void deleteLiterature(final int position, final Literature literature) {
        if (position > -1 && position < literatureList.size()) {

            if (customDialogFragment == null) {
                customDialogFragment = new CustomDialogFragment();
            }

            customDialogFragment.setPrompt("确认要删除作品？");
            customDialogFragment.setCustomDialogListener(new CustomDialogListener() {
                @Override
                public void onFirstOptionClicked() {
                    hideCustomDialogFragment();
                }

                @Override
                public void onSecondOptionClicked() {
                    if (customDialogFragment.getShowsDialog()) {
                        if (literature != null && !TextUtils.isEmpty(literature.id)) {
                            literatureListPresenter.deleteLiterature(position, literature);
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

    @Override
    public void showReviserUser() {
        Intent intent = new Intent();
        intent.putExtra("information", "reviserQQ");
        intent.setClass(LiteratureListActivity.this, ReviseUserActivity.class);
        startActivity(intent);
    }

    @OnClick({R.id.literature_head_back, R.id.literature_head_create})
    public void onClickListener(View view) {
        switch (view.getId()) {
            case R.id.literature_head_back:
                finish();
                break;
            case R.id.literature_head_create:
                startActivity(new Intent(this, LiteratureCreateActivity.class));
                break;
        }
    }

    private void changeEmptyViewState(boolean show) {
        if (literature_list_empty != null) {
            literature_list_empty.setVisibility(show ? View.VISIBLE : View.GONE);
        }

        if (literature_list_refresh != null) {
            literature_list_refresh.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public void setRefreshViewState(boolean state) {
        if (literature_list_refresh == null) {
            return;
        }
        if (!state) {
            literature_list_refresh.postDelayed(new Runnable() {
                @Override
                public void run() {
                    literature_list_refresh.setRefreshing(false);
                }
            }, 500);
        } else {
            literature_list_refresh.setRefreshing(true);
        }
    }

    public void hideCustomDialogFragment() {
        if (!isFinishing() && customDialogFragment != null && customDialogFragment.getShowsDialog()) {
            customDialogFragment.dismiss();
        }
    }
}
