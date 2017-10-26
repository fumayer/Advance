package com.quduquxie.function.read.end.view;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.quduquxie.R;
import com.quduquxie.application.component.ApplicationComponent;
import com.quduquxie.base.bean.Book;
import com.quduquxie.base.manager.ActivityStackManager;
import com.quduquxie.base.module.main.activity.view.MainActivity;
import com.quduquxie.function.BaseActivity;
import com.quduquxie.function.read.end.ReadEndInterface;
import com.quduquxie.function.read.end.adapter.ReadEndAdapter;
import com.quduquxie.function.read.end.component.DaggerReadEndComponent;
import com.quduquxie.function.read.end.module.ReadEndModule;
import com.quduquxie.function.read.end.presenter.ReadEndPresenter;
import com.quduquxie.function.read.end.util.ReadEndItemDecoration;
import com.quduquxie.function.read.end.util.ReadEndSpanSizeLookup;
import com.quduquxie.modular.cover.view.CoverActivity;
import com.quduquxie.widget.LoadingPage;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created on 17/4/16.
 * Created by crazylei.
 */

public class ReadEndActivity extends BaseActivity implements ReadEndInterface.View, ReadEndAdapter.ReadEndItemClickListener {

    @BindView(R.id.common_head_back)
    public ImageView common_head_back;
    @BindView(R.id.common_head_title)
    public TextView common_head_title;
    @BindView(R.id.read_end_content)
    public FrameLayout read_end_content;
    @BindView(R.id.read_end_result)
    public RecyclerView read_end_result;

    @Inject
    ReadEndPresenter readEndPresenter;

    private Book book;

    private Toast toast;

    private LoadingPage loadingPage;

    private ArrayList<Book> recommendList = new ArrayList<>();

    private GridLayoutManager gridLayoutManager;
    private ReadEndAdapter readEndAdapter;

    private ReadEndItemDecoration readEndItemDecoration;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.layout_activity_read_end);

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
        DaggerReadEndComponent.builder()
                .applicationComponent(applicationComponent)
                .readEndModule(new ReadEndModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(ReadEndInterface.Presenter readEndPresenter) {
        this.readEndPresenter = (ReadEndPresenter) readEndPresenter;
    }

    @Override
    public void initParameter() {
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent != null) {
            this.book = (Book) intent.getSerializableExtra("Book");
        }

        readEndAdapter = new ReadEndAdapter(getQuApplicationContext(), recommendList);
        readEndAdapter.setBookInformation(book);
        readEndAdapter.setReadEndItemClickListener(this);

        readEndItemDecoration = new ReadEndItemDecoration(getQuApplicationContext());

        gridLayoutManager = new GridLayoutManager(getQuApplicationContext(), 3);

        ReadEndSpanSizeLookup readEndSpanSizeLookup = new ReadEndSpanSizeLookup(readEndAdapter);
        gridLayoutManager.setSpanSizeLookup(readEndSpanSizeLookup);

        readEndPresenter.initParameter();

        readEndPresenter.loadRecommendData(book.id);
    }

    @Override
    public void initView() {
        if (common_head_title != null) {
            common_head_title.setText("相关书籍推荐");
            common_head_title.setTypeface(typeface_song_depict);
        }

        if (read_end_result != null) {
            read_end_result.setLayoutManager(gridLayoutManager);
            read_end_result.setAdapter(readEndAdapter);
            read_end_result.addItemDecoration(readEndItemDecoration);
        }
    }

    @Override
    public void showLoadingPage() {
        if (loadingPage != null) {
            loadingPage.onSuccess();
        }

        if (loadingPage == null) {
            loadingPage = new LoadingPage(this, read_end_content);
        }
        readEndPresenter.setLoadingPage(loadingPage);
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
    public void setRecommendData(List<Book> books) {

        if (recommendList == null) {
            recommendList = new ArrayList<>();
        } else {
            recommendList.clear();
        }

        recommendList.add(createBook(ReadEndAdapter.TYPE_READ_END_PROMPT));

        if (books != null && books.size() > 0) {

            recommendList.add(createBook(ReadEndAdapter.TYPE_READ_END_RECOMMEND_HEAD));

            for (Book book : books) {
                book.item_type = ReadEndAdapter.TYPE_READ_END_BOOK;
                recommendList.add(book);
            }
            recommendList.add(createBook(ReadEndAdapter.TYPE_READ_END_RECOMMEND_BOTTOM));
        }

        if (readEndAdapter == null) {
            readEndAdapter = new ReadEndAdapter(getQuApplicationContext(), recommendList);
            if (read_end_result != null) {
                read_end_result.setAdapter(readEndAdapter);
            }
        } else {
            readEndAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showToast(String message) {
        if (TextUtils.isEmpty(message)) {
            return;
        }

        if (toast == null) {
            toast = Toast.makeText(getQuApplicationContext(), message, Toast.LENGTH_SHORT);
        } else {
            toast.setText(message);
        }

        if (!isFinishing() && toast != null) {
            toast.show();
        }
    }

    @Override
    public void startCoverActivity(String id) {
        Intent intent = new Intent();
        intent.setClass(ReadEndActivity.this, CoverActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void startBookStoreFragment() {
        Intent intent = new Intent();
        intent.putExtra("position", 1);
        intent.setClass(ReadEndActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (ActivityStackManager.getActivityStackManager().getActivities() == 1) {
                Intent intent = new Intent();
                intent.setClass(this, MainActivity.class);
                startActivity(intent);
            }
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @OnClick({R.id.common_head_back})
    public void onClickListener(View view) {
        switch (view.getId()) {
            case R.id.common_head_back:
                if (ActivityStackManager.getActivityStackManager().getActivities() == 1) {
                    Intent intent = new Intent();
                    intent.setClass(this, MainActivity.class);
                    startActivity(intent);
                }
                finish();
                break;
        }
    }

    private Book createBook(int type) {
        Book book = new Book();
        book.item_type = type;
        return book;
    }
}