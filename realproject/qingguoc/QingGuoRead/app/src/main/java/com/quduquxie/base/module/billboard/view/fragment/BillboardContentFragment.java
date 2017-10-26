package com.quduquxie.base.module.billboard.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.quduquxie.R;
import com.quduquxie.application.component.ApplicationComponent;
import com.quduquxie.base.BaseFragment;
import com.quduquxie.base.adapter.BookInformationAdapter;
import com.quduquxie.base.bean.Book;
import com.quduquxie.base.database.helper.BookDaoHelper;
import com.quduquxie.base.listener.BillboardListener;
import com.quduquxie.base.listener.TabulationListener;
import com.quduquxie.base.module.billboard.BillboardContentInterface;
import com.quduquxie.base.module.billboard.component.DaggerBillboardContentComponent;
import com.quduquxie.base.module.billboard.module.BillboardContentModule;
import com.quduquxie.base.module.billboard.presenter.BillboardContentPresenter;
import com.quduquxie.base.module.billboard.view.BillboardActivity;
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

/**
 * Created on 17/7/25.
 * Created by crazylei.
 */

public class BillboardContentFragment extends BaseFragment<BillboardContentPresenter> implements BillboardContentInterface.View, TabulationListener {

    @BindView(R.id.billboard_content_loading)
    RelativeLayout billboard_content_loading;
    @BindView(R.id.billboard_content_refresh)
    SwipeRefreshLayout billboard_content_refresh;
    @BindView(R.id.billboard_content_result)
    RecyclerView billboard_content_result;

    @Inject
    BillboardContentPresenter billboardContentPresenter;

    private ArrayList<Book> books = new ArrayList<>();

    private BookInformationAdapter bookInformationAdapter;

    private LinearLayoutManager linearLayoutManager;

    private int page = 1;

    private String uri;
    private String date;
    private String title;
    private String channel;
    private String parameter;

    private LoadingPage loadingPage;

    public DropWindow dropWindow;
    public View dropView;

    private BookDaoHelper bookDaoHelper;

    private TextView tabulation_window_insert;
    private TextView tabulation_window_comment;

    private BillboardListener billboardListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            billboardListener = (BillboardActivity) activity;
        } catch (ClassCastException classCastException) {
            throw new ClassCastException(activity.toString() + " must implement BillboardListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bookInformationAdapter = new BookInformationAdapter(getContext(), books);
        bookInformationAdapter.setTabulationListener(this);

        linearLayoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);

        Bundle bundle = getArguments();

        if (bundle != null) {
            uri = bundle.getString("uri");
            title = bundle.getString("title");
            channel = bundle.getString("channel");
            parameter = bundle.getString("parameter");
        }

        this.date = billboardListener.loadBillboardDate();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_fragment_billboard_content, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initializeParameter();
    }

    @Override
    public void onResume() {
        super.onResume();
        StatService.onPageStart(this.getContext(), BillboardContentFragment.class.getSimpleName());
    }

    @Override
    public void onPause() {
        super.onPause();
        StatService.onPageEnd(this.getContext(), BillboardContentFragment.class.getSimpleName());
    }

    @Override
    protected void setFragmentComponent(ApplicationComponent applicationComponent) {
        DaggerBillboardContentComponent.builder()
                .applicationComponent(applicationComponent)
                .billboardContentModule(new BillboardContentModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void initializeParameter() {
        if (bookDaoHelper == null) {
            bookDaoHelper = BookDaoHelper.getInstance(this.getContext());
        }

        billboard_content_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                resetRefreshViewState(true);
                page = 1;

                if ("男频榜".equals(title) || "女频榜".equals(title)) {
                    billboardContentPresenter.loadBillboardContent(uri, date, parameter, page);
                } else {
                    billboardContentPresenter.loadBillboardContent(uri, date, channel, page);
                }

            }
        });

        billboard_content_result.setLayoutManager(linearLayoutManager);
        billboard_content_result.setAdapter(bookInformationAdapter);
        billboard_content_result.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = linearLayoutManager.getChildCount();
                int totalItemCount = linearLayoutManager.getItemCount();
                int firstVisiblePosition = linearLayoutManager.findFirstVisibleItemPosition();

                if (!billboard_content_refresh.isRefreshing() && billboardContentPresenter.loadingMoreState() && (visibleItemCount + firstVisiblePosition) == totalItemCount - 5) {
                    resetRefreshViewState(true);
                    page += 1;

                    if ("男频榜".equals(title) || "女频榜".equals(title)) {
                        billboardContentPresenter.loadBillboardContentMore(uri, date, parameter, page);
                    } else {
                        billboardContentPresenter.loadBillboardContentMore(uri, date, channel, page);
                    }
                }
            }
        });

        loadBillboardData();
    }

    @Override
    public void recycle() {

    }

    @Override
    public void showLoadingPage() {
        if (loadingPage != null) {
            loadingPage.onSuccess();
        } else {
            loadingPage = new LoadingPage(this.getActivity(), billboard_content_loading);
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
    public void initializeData(ArrayList<Book> bookList) {

        if (books == null) {
            books = new ArrayList<>();
        } else {
            books.clear();
        }

        for (Book book : bookList) {
            if (book != null) {
                books.add(CommunalUtil.changeBookItemType(book, BookInformationAdapter.TYPE_BOOK_INFORMATION));
            }
        }

        bookInformationAdapter.notifyDataSetChanged();
    }

    @Override
    public void initializeDataMore(ArrayList<Book> bookList) {
        for (Book book : bookList) {
            if (book != null) {
                books.add(CommunalUtil.changeBookItemType(book, BookInformationAdapter.TYPE_BOOK_INFORMATION));
            }
        }

        bookInformationAdapter.notifyDataSetChanged();
    }

    @Override
    public void resetRefreshViewState(boolean state) {
        if (null == billboard_content_refresh) {
            return;
        }
        if (state) {
            billboard_content_refresh.setRefreshing(true);
        } else {
            if (billboard_content_refresh.isRefreshing()) {
                billboard_content_refresh.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        billboard_content_refresh.setRefreshing(false);
                    }
                }, 500);
            }
        }
    }

    @Override
    public void startCoverActivity(Book book) {
        Intent intent = new Intent();
        intent.setClass(this.getContext(), CoverActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("id", book.id);
        intent.putExtras(bundle);

        if (!this.getActivity().isFinishing()) {
            startActivity(intent);
        }
    }

    @Override
    public void showOptionMore(View view, Book book, int position) {
        initPopupWindow(view, book, position);
    }

    private void loadBillboardData() {
        if (books == null || books.size() == 0) {
            if ("男频榜".equals(title) || "女频榜".equals(title)) {
                billboardContentPresenter.loadBillboardContent(uri, date, parameter, page);
            } else {
                billboardContentPresenter.loadBillboardContent(uri, date, channel, page);
            }
        } else {
            bookInformationAdapter.notifyDataSetChanged();
        }
    }

    public void changeData(String dateValue) {
        if (!date.equals(dateValue)) {
            this.page = 1;
            this.date = dateValue;

            refreshLayoutScrollPosition();

            if ("男频榜".equals(title) || "女频榜".equals(title)) {
                billboardContentPresenter.loadBillboardContent(uri, date, parameter, page);
            } else {
                billboardContentPresenter.loadBillboardContent(uri, date, channel, page);
            }
        }
    }

    private void refreshLayoutScrollPosition() {
        if (billboard_content_result != null) {
            billboard_content_result.scrollToPosition(0);
        }
    }

    private void initPopupWindow(View view, final Book book, int position) {
        if (dropView == null) {
            try {
                dropView = View.inflate(this.getContext(), R.layout.layout_window_tabulation, null);
            } catch (InflateException inflateException) {
                collectException(inflateException);
                inflateException.printStackTrace();
                return;
            }

            tabulation_window_insert = (TextView) dropView.findViewById(R.id.tabulation_window_insert);
            tabulation_window_comment = (TextView) dropView.findViewById(R.id.tabulation_window_comment);
        }


        dropWindow = new DropWindow(this.getContext(), dropView);

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
                bookDaoHelper = BookDaoHelper.getInstance(this.getContext());
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
            intent.setClass(this.getContext(), CommentListActivity.class);

            if (!this.getActivity().isFinishing()) {
                startActivity(intent);
            }
        }
    }

    public void showPopupWindow(DropWindow dropWindow, View dropView, View root, int position) {
        if (dropWindow != null && !dropWindow.isShowing()) {
            if (root != null && dropView != null && dropView.getParent() != null) {
                ((ViewGroup) dropView.getParent()).removeAllViews();
            }

            int x = this.getResources().getDimensionPixelOffset(R.dimen.width_260);

            if (root != null) {
                dropWindow.showAsDropDown(root, -x, 0);
            }
        }
    }

    private void dismissPopupWindow(DropWindow dropWindow) {
        if (dropWindow != null && dropWindow.isShowing()) {
            dropWindow.dismiss();
        }
    }
}