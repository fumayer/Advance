package com.quduquxie.function.creation.section.view;

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
import com.quduquxie.communal.utils.UserUtil;
import com.quduquxie.creation.modify.view.LiteratureSectionModifyActivity;
import com.quduquxie.function.BaseActivity;
import com.quduquxie.function.creation.draft.view.DraftListActivity;
import com.quduquxie.function.creation.section.SectionListInterface;
import com.quduquxie.function.creation.section.adapter.SectionListAdapter;
import com.quduquxie.function.creation.section.component.DaggerSectionListComponent;
import com.quduquxie.function.creation.section.module.SectionListModule;
import com.quduquxie.function.creation.section.presenter.SectionListPresenter;
import com.quduquxie.function.creation.write.view.SectionWriteActivity;
import com.quduquxie.model.creation.Literature;
import com.quduquxie.model.creation.Section;
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

public class SectionListActivity extends BaseActivity implements SectionListInterface.View, SectionListAdapter.OnSectionClickListener {

    @BindView(R.id.literature_head_back)
    public ImageView literature_head_back;
    @BindView(R.id.literature_head_title)
    public TextView literature_head_title;
    @BindView(R.id.literature_head_create)
    public TextView literature_head_create;
    @BindView(R.id.section_list_content)
    public RelativeLayout section_list_content;
    @BindView(R.id.section_list_refresh)
    public SwipeRefreshLayout section_list_refresh;
    @BindView(R.id.section_list_result)
    public RecyclerView section_list_result;

    @Inject
    SectionListPresenter sectionListPresenter;

    private Literature literature;

    private ArrayList<Section> sectionList = new ArrayList<>();

    private LinearLayoutManager linearLayoutManager;
    private SectionListAdapter sectionListAdapter;

    private LoadingPage loadingPage;

    private Toast toast;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.layout_activity_section_list);

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

        sectionListPresenter.loadSectionList(literature);
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
        DaggerSectionListComponent.builder()
                .applicationComponent(applicationComponent)
                .sectionListModule(new SectionListModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(SectionListInterface.Presenter sectionListPresenter) {
        this.sectionListPresenter = (SectionListPresenter) sectionListPresenter;
    }

    @Override
    public void initParameter() {
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent.getSerializableExtra("literature") != null) {
            this.literature = (Literature) intent.getSerializableExtra("literature");
        }

        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        sectionListAdapter = new SectionListAdapter(getApplicationContext(), sectionList);
        sectionListAdapter.setLiteratureData(literature);
        sectionListAdapter.setOnSectionClickListener(this);

        sectionListPresenter.initParameter();
    }

    @Override
    public void initView() {

        if (literature_head_title != null) {
            literature_head_title.setTypeface(typeface_song_depict);
            literature_head_title.setText("章节列表");
        }

        if (literature_head_create != null) {
            literature_head_create.setText("新建章节");
        }

        if (section_list_refresh != null) {
            section_list_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    setRefreshViewState(false);
                    sectionListPresenter.loadSectionList(literature);
                }
            });
        }

        if (section_list_result != null) {
            section_list_result.setLayoutManager(linearLayoutManager);
            section_list_result.setAdapter(sectionListAdapter);
            section_list_result.setItemAnimator(new DefaultItemAnimator());
        }
    }

    @Override
    public void showLoadingPage() {
        if (loadingPage != null) {
            loadingPage.onSuccess();
        }

        if (loadingPage == null) {
            loadingPage = new LoadingPage(this, section_list_content);
        }

        sectionListPresenter.setLoadingPage(loadingPage);
    }

    @Override
    public void hideLoadingPage() {
        if (loadingPage != null) {
            loadingPage.onSuccess();
        }
    }

    @Override
    public void setSectionData(ArrayList<Section> sections) {
        if (sectionList == null) {
            sectionList = new ArrayList<>();
        } else {
            sectionList.clear();
        }

        Section draftCountSection = new Section();
        draftCountSection.item_type = SectionListAdapter.TYPE_DRAFT_COUNT;
        sectionList.add(draftCountSection);

        if (sections != null && sections.size() > 0) {
            for (Section section : sections) {
                sectionList.add(section);
            }
        }

        if (sectionListAdapter == null) {
            sectionListAdapter = new SectionListAdapter(getQuApplicationContext(), sections);
            sectionListAdapter.setOnSectionClickListener(this);
            section_list_result.setAdapter(sectionListAdapter);
        } else {
            sectionListAdapter.notifyDataSetChanged();
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
    public void startDraftListActivity() {
        Intent draftIntent = new Intent();
        draftIntent.putExtra("literature", literature);
        draftIntent.setClass(this, DraftListActivity.class);
        startActivity(draftIntent);
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
    public void onSectionClicked(Section section) {
        if (section != null) {
            Intent intent = new Intent();
            intent.putExtra("section", section);
            intent.setClass(this, LiteratureSectionModifyActivity.class);
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
                    intent.setClass(this, SectionWriteActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }

    public void setRefreshViewState(boolean state) {
        if (section_list_refresh == null) {
            return;
        }
        if (!state) {
            section_list_refresh.postDelayed(new Runnable() {
                @Override
                public void run() {
                    section_list_refresh.setRefreshing(false);
                }
            }, 500);
        } else {
            section_list_refresh.setRefreshing(true);
        }
    }
}
