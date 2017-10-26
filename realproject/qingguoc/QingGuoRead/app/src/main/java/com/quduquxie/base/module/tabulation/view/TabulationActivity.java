package com.quduquxie.base.module.tabulation.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.InflateException;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.orhanobut.logger.Logger;
import com.quduquxie.R;
import com.quduquxie.application.component.ApplicationComponent;
import com.quduquxie.base.BaseActivity;
import com.quduquxie.base.adapter.BookInformationAdapter;
import com.quduquxie.base.bean.Book;
import com.quduquxie.base.database.helper.BookDaoHelper;
import com.quduquxie.base.listener.TabulationListener;
import com.quduquxie.base.module.tabulation.TabulationInterface;
import com.quduquxie.base.module.tabulation.component.DaggerTabulationComponent;
import com.quduquxie.base.module.tabulation.module.TabulationModule;
import com.quduquxie.base.module.tabulation.presenter.TabulationPresenter;
import com.quduquxie.base.util.CommunalUtil;
import com.quduquxie.base.widget.CustomLoadingPage;
import com.quduquxie.base.widget.DropWindow;
import com.quduquxie.modular.cover.view.CoverActivity;
import com.quduquxie.module.comment.view.CommentListActivity;
import com.quduquxie.widget.LoadingPage;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created on 17/7/25.
 * Created by crazylei.
 */

public class TabulationActivity extends BaseActivity<TabulationPresenter> implements TabulationInterface.View, TabulationListener {

    @BindView(R.id.common_head_back)
    ImageView common_head_back;
    @BindView(R.id.common_head_title)
    TextView common_head_title;
    @BindView(R.id.tabulation_content)
    FrameLayout tabulation_content;
    @BindView(R.id.tabulation_refresh)
    SwipeRefreshLayout tabulation_refresh;
    @BindView(R.id.tabulation_result)
    RecyclerView tabulation_result;

    @Inject
    TabulationPresenter tabulationPresenter;

    private String uri;
    private String type;
    private String name;
    private String title;

    private int page = 1;

    private ArrayList<Book> books = new ArrayList<>();

    private BookInformationAdapter bookInformationAdapter;

    private LinearLayoutManager linearLayoutManager;

    private LoadingPage loadingPage;

    private BookDaoHelper bookDaoHelper;

    private DropWindow dropWindow;
    private View dropView;

    private TextView tabulation_window_insert;
    private TextView tabulation_window_comment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_tabulation_content);
        initializeParameter();
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
        DaggerTabulationComponent.builder()
                .applicationComponent(applicationComponent)
                .tabulationModule(new TabulationModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void initializeParameter() {

        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent != null) {
            this.uri = intent.getStringExtra("uri");
            this.type = intent.getStringExtra("type");
            this.name = intent.getStringExtra("name");
            this.title = intent.getStringExtra("title");
        }

        if (bookDaoHelper == null) {
            bookDaoHelper = BookDaoHelper.getInstance(this);
        }

        common_head_title.setText(TextUtils.isEmpty(title) ? "榜单" : title);
        common_head_title.setTypeface(typeface_song_depict);

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        bookInformationAdapter = new BookInformationAdapter(this, books);
        bookInformationAdapter.setTabulationListener(this);
        bookInformationAdapter.setShowCategory(!"category".equals(type));

        tabulation_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                resetRefreshViewState(true);
                page = 1;
                tabulationPresenter.loadTabulationData(uri, name, type, page);
            }
        });

        tabulation_result.setAdapter(bookInformationAdapter);
        tabulation_result.setLayoutManager(linearLayoutManager);
        tabulation_result.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = linearLayoutManager.getChildCount();
                int totalItemCount = linearLayoutManager.getItemCount();
                int firstVisiblePosition = linearLayoutManager.findFirstVisibleItemPosition();

                if (!tabulation_refresh.isRefreshing() && tabulationPresenter.loadingMoreState() && (visibleItemCount + firstVisiblePosition) == totalItemCount - 5) {
                    resetRefreshViewState(true);
                    page += 1;
                    tabulationPresenter.loadTabulationDataMore(uri, name, type, page);
                }
            }
        });

        tabulationPresenter.loadTabulationData(uri, name, type, page);
    }

    @Override
    public void recycle() {

    }

    @Override
    public void showLoadingPage() {
        if (loadingPage != null) {
            loadingPage.onSuccess();
        } else {
            loadingPage = new LoadingPage(this, tabulation_content);
        }
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
    public void initializeTabulationData(ArrayList<Book> bookList) {
        if (books == null) {
            books = new ArrayList<>();
        } else {
            books.clear();
        }

        for (Book book : bookList) {
            if (book != null && !TextUtils.isEmpty(name)) {
                if ("大神".equals(name)) {
                    if (!TextUtils.isEmpty(book.bannerImage)) {
                        books.add(CommunalUtil.changeBookItemType(book, BookInformationAdapter.TYPE_BOOK_SELECTED));
                    } else {
                        books.add(CommunalUtil.changeBookItemType(book, BookInformationAdapter.TYPE_BOOK_RECOMMEND));
                    }
                } else {
                    books.add(CommunalUtil.changeBookItemType(book, BookInformationAdapter.TYPE_BOOK_INFORMATION));
                }
            }
        }

        bookInformationAdapter.notifyDataSetChanged();
    }

    @Override
    public void initializeTabulationDataMore(ArrayList<Book> bookList) {
        for (Book book : bookList) {
            if (book != null && !TextUtils.isEmpty(name)) {
                if ("大神".equals(name)) {
                    if (!TextUtils.isEmpty(book.bannerImage)) {
                        books.add(CommunalUtil.changeBookItemType(book, BookInformationAdapter.TYPE_BOOK_SELECTED));
                    } else {
                        books.add(CommunalUtil.changeBookItemType(book, BookInformationAdapter.TYPE_BOOK_RECOMMEND));
                    }
                } else {
                    books.add(CommunalUtil.changeBookItemType(book, BookInformationAdapter.TYPE_BOOK_INFORMATION));
                }
            }
        }

        bookInformationAdapter.notifyDataSetChanged();
    }

    @Override
    public void resetRefreshViewState(boolean state) {
        if (null == tabulation_refresh) {
            return;
        }
        if (state) {
            tabulation_refresh.setRefreshing(true);
        } else {
            if (tabulation_refresh.isRefreshing()) {
                tabulation_refresh.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tabulation_refresh.setRefreshing(false);
                    }
                }, 500);
            }
        }
    }

    @Override
    public void startCoverActivity(Book book) {
        Intent intent = new Intent();
        intent.setClass(TabulationActivity.this, CoverActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("id", book.id);
        intent.putExtras(bundle);

        startActivity(intent);
    }

    @Override
    public void showOptionMore(View view, Book book, int position) {
        initPopupWindow(view, book, position);
    }

    @OnClick({R.id.common_head_back})
    public void onClickListener(View view) {
        switch (view.getId()) {
            case R.id.common_head_back:
                finish();
                break;
        }
    }

    private void initPopupWindow(View view, final Book book, int position) {
        if (dropView == null) {
            try {
                dropView = View.inflate(this, R.layout.layout_window_tabulation, null);
            } catch (InflateException inflateException) {
                collectException(inflateException);
                inflateException.printStackTrace();
                return;
            }

            tabulation_window_insert = (TextView) dropView.findViewById(R.id.tabulation_window_insert);
            tabulation_window_comment = (TextView) dropView.findViewById(R.id.tabulation_window_comment);
        }


        dropWindow = new DropWindow(this, dropView);

        tabulation_window_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissPopupWindow(dropWindow);
                insertBook(book);
            }
        });

        tabulation_window_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissPopupWindow(dropWindow);
                startCommentListActivity(book);
            }
        });

        showPopupWindow(dropWindow, dropView, view, position);
    }

    private void insertBook(Book book) {
        if (book != null && !TextUtils.isEmpty(book.id)) {

            if (bookDaoHelper == null) {
                bookDaoHelper = BookDaoHelper.getInstance(this);
            }

            if (bookDaoHelper.subscribeBook(book.id)) {
                showPromptMessage("已加入书架！");
            } else {
                int result = bookDaoHelper.insertBook(book);
                if (result == 1) {
                    showPromptMessage("成功添加到书架！");
                }
            }
        } else {
            showPromptMessage("参数异常！");
        }
    }

    private void startCommentListActivity(Book book) {
        if (book != null && !TextUtils.isEmpty(book.id)) {
            Intent intent = new Intent();
            intent.putExtra("book", book);
            intent.putExtra("id_book", book.id);
            intent.setClass(TabulationActivity.this, CommentListActivity.class);
            startActivity(intent);
        }
    }

    public void showPopupWindow(DropWindow dropWindow, View dropView, View root, int position) {
        if (dropWindow != null && !dropWindow.isShowing()) {
            if (root != null && dropView != null && dropView.getParent() != null) {
                ((ViewGroup) dropView.getParent()).removeAllViews();
            }

            if (root != null) {
                dropWindow.showAsDropDown(root);
            }
        }
    }

    private void dismissPopupWindow(DropWindow dropWindow) {
        if (dropWindow != null && dropWindow.isShowing()) {
            dropWindow.dismiss();
        }
    }
}