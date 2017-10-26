package com.quduquxie.base.module.main.fragment.content.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
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
import com.quduquxie.base.bean.Banner;
import com.quduquxie.base.bean.Book;
import com.quduquxie.base.bean.Category;
import com.quduquxie.base.bean.Direct;
import com.quduquxie.base.database.helper.BookDaoHelper;
import com.quduquxie.base.listener.BannerListener;
import com.quduquxie.base.listener.BookListener;
import com.quduquxie.base.listener.CategoryListener;
import com.quduquxie.base.listener.DirectListener;
import com.quduquxie.base.listener.MainContentListener;
import com.quduquxie.base.module.billboard.view.BillboardActivity;
import com.quduquxie.base.module.main.activity.adapter.MainContentAdapter;
import com.quduquxie.base.bean.MainContent;
import com.quduquxie.base.bean.MainContentItem;
import com.quduquxie.base.listener.MainInteractiveListener;
import com.quduquxie.base.module.main.fragment.content.component.DaggerContentComponent;
import com.quduquxie.base.module.main.fragment.content.util.ContentUtil;
import com.quduquxie.base.module.main.fragment.content.ContentInterface;
import com.quduquxie.base.module.main.fragment.content.module.ContentModule;
import com.quduquxie.base.module.main.fragment.content.presenter.ContentPresenter;
import com.quduquxie.base.module.tabulation.view.TabulationActivity;
import com.quduquxie.base.widget.CustomLoadingPage;
import com.quduquxie.base.widget.DropWindow;
import com.quduquxie.base.widget.helper.MainContentItemDecoration;
import com.quduquxie.base.widget.helper.MainContentSpanSizeLookup;
import com.quduquxie.function.banner.view.BannerActivity;
import com.quduquxie.function.search.view.SearchActivity;
import com.quduquxie.modular.cover.view.CoverActivity;
import com.quduquxie.module.comment.view.CommentListActivity;
import com.quduquxie.widget.LoadingPage;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 17/7/20.
 * Created by crazylei.
 */

public class ContentFragment extends BaseFragment<ContentPresenter> implements ContentInterface.View, BookListener, BannerListener, DirectListener, CategoryListener, MainContentListener {

    @BindView(R.id.main_content_root)
    RelativeLayout main_content_root;
    @BindView(R.id.main_content_divider)
    View main_content_divider;
    @BindView(R.id.main_content_refresh)
    SwipeRefreshLayout main_content_refresh;
    @BindView(R.id.main_content_result)
    RecyclerView main_content_result;

    @Inject
    ContentPresenter contentPresenter;

    private MainInteractiveListener mainInteractiveListener;

    public LoadingPage loadingPage;

    private ArrayList<MainContentItem> mainContentItems = new ArrayList<>();

    private GridLayoutManager gridLayoutManager;
    private MainContentAdapter mainContentAdapter;
    private MainContentItemDecoration mainContentItemDecoration;
    private MainContentSpanSizeLookup mainContentSpanSizeLookup;

    private int type;

    private String key;
    private String title;

    private int totalY = 0;

    public static final int TYPE_SELECTED = 0x80;
    public static final int TYPE_LIBRARY = 0x81;

    private BookDaoHelper bookDaoHelper;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mainInteractiveListener = (MainInteractiveListener) activity;
        } catch (ClassCastException classCastException) {
            throw new ClassCastException(activity.toString() + " must implement MainInteractiveListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainContentAdapter = new MainContentAdapter(getContext(), mainContentItems);
        mainContentAdapter.setBookListener(this);
        mainContentAdapter.setBannerListener(this);
        mainContentAdapter.setDirectListener(this);
        mainContentAdapter.setCategoryListener(this);
        mainContentAdapter.setMainContentListener(this);

        mainContentItemDecoration = new MainContentItemDecoration(this.getContext(), mainContentAdapter);

        gridLayoutManager = new GridLayoutManager(this.getContext(), 6);

        mainContentSpanSizeLookup = new MainContentSpanSizeLookup(mainContentAdapter);
        gridLayoutManager.setSpanSizeLookup(mainContentSpanSizeLookup);

        Bundle bundle = getArguments();

        if (bundle != null) {
            type = bundle.getInt("type", TYPE_SELECTED);
            key = bundle.getString("key");
            title = bundle.getString("title");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_fragment_main_content, container, false);
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
        StatService.onPageStart(this.getContext(), ContentFragment.class.getSimpleName());
    }

    @Override
    public void onPause() {
        super.onPause();
        StatService.onPageEnd(this.getContext(), ContentFragment.class.getSimpleName());
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser && mainInteractiveListener != null) {
            mainInteractiveListener.storeContentScrollDistance(totalY);
        }
    }

    @Override
    protected void setFragmentComponent(ApplicationComponent applicationComponent) {
        DaggerContentComponent.builder()
                .applicationComponent(applicationComponent)
                .contentModule(new ContentModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void initializeParameter() {

        if (bookDaoHelper == null) {
            bookDaoHelper = BookDaoHelper.getInstance(this.getContext());
        }

        main_content_divider.setAlpha(0);

        main_content_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                resetRefreshViewState(true);
                contentPresenter.loadSelectedData(type, key);
            }
        });

        main_content_result.setLayoutManager(gridLayoutManager);
        main_content_result.setAdapter(mainContentAdapter);
        main_content_result.addItemDecoration(mainContentItemDecoration);

        main_content_result.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int state) {
                super.onScrollStateChanged(recyclerView, state);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int x, int y) {
                super.onScrolled(recyclerView, x, y);

                totalY += y;

                storeContentScrollDistance(totalY);
            }
        });

        loadMainContentData();
    }

    @Override
    public void recycle() {

    }

    @Override
    public void setMainContentData(MainContent mainContent) {
        if (mainContent != null && mainContent.data != null && mainContent.data.size() > 0) {

            if (mainContentItems == null) {
                mainContentItems = new ArrayList<>();
            } else {
                mainContentItems.clear();
            }

            ArrayList<MainContentItem> mainContentItemList = ContentUtil.encapsulationMainContent(mainContent, type);

            if (mainContentItemList != null && mainContentItemList.size() > 0) {
                for (MainContentItem mainContentItem : mainContentItemList) {
                    mainContentItems.add(mainContentItem);
                }

                mainContentAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void showLoadingPage() {
        if (loadingPage == null) {
            loadingPage = new LoadingPage(this.getActivity(), main_content_root);

            loadingPage.initializeReloadAction(new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    if (contentPresenter != null) {
                        contentPresenter.loadSelectedData(type, key);
                    }
                    return null;
                }
            });
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
    public void resetRefreshViewState(boolean state) {
        if (null == main_content_refresh) {
            return;
        }
        if (state) {
            main_content_refresh.setRefreshing(true);
        } else {
            if (main_content_refresh.isRefreshing()) {
                main_content_refresh.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        main_content_refresh.setRefreshing(false);
                    }
                }, 500);
            }
        }
    }

    public void loadMainContentData() {
        if (mainContentItems == null || mainContentItems.size() == 0) {
            contentPresenter.loadSelectedData(type, key);
        } else {
            mainContentAdapter.notifyDataSetChanged();
        }
    }

    public void storeContentScrollDistance(int distance) {
        if (main_content_divider != null) {
            if (distance < 100) {
                main_content_divider.setAlpha(distance / 100.0f);
            } else {
                main_content_divider.setAlpha(1);
            }
        }
    }

    @Override
    public void onClickedBook(Book book) {
        if (book != null && !TextUtils.isEmpty(book.id)) {
            Intent intent = new Intent();
            intent.setClass(this.getContext(), CoverActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("id", book.id);
            intent.putExtras(bundle);

            if (!this.getActivity().isFinishing()) {
                this.getContext().startActivity(intent);
            }
        }
    }

    @Override
    public void onClickedBanner(Banner banner) {
        if (banner != null) {
            if (!TextUtils.isEmpty(banner.id_book)) {
                Intent intent = new Intent();
                intent.setClass(this.getContext(), CoverActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", banner.id_book);
                intent.putExtras(bundle);

                if (!this.getActivity().isFinishing()) {
                    this.getContext().startActivity(intent);
                }
            } else {
                Intent intent = new Intent();
                intent.setClass(this.getContext(), BannerActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Banner", banner);
                intent.putExtras(bundle);

                if (!this.getActivity().isFinishing()) {
                    this.getContext().startActivity(intent);
                }
            }
        }
    }

    @Override
    public void onClickedDirect(Direct direct) {
        if (direct != null && !TextUtils.isEmpty(direct.uri)) {
            if ("榜单".equals(direct.name)) {
                Intent intent = new Intent();
                intent.setClass(this.getContext(), BillboardActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("uri", direct.uri);

                if ("男频".equals(title)) {
                    bundle.putString("title", "男频榜单");
                } else if ("女频".equals(title)) {
                    bundle.putString("title", "女频榜单");
                } else {
                    bundle.putString("title", direct.name);
                }

                intent.putExtras(bundle);

                if (!this.getActivity().isFinishing()) {
                    this.getContext().startActivity(intent);
                }
            } else {
                Intent intent = new Intent();
                intent.setClass(this.getContext(), TabulationActivity.class);
                Bundle bundle = new Bundle();

                if ("男频".equals(title) && type == TYPE_LIBRARY) {
                    bundle.putString("name", "男频");
                } else if ("女频".equals(title) && type == TYPE_LIBRARY) {
                    bundle.putString("name", "女频");
                } else {
                    bundle.putString("name", direct.name);
                }

                bundle.putString("uri", direct.uri);
                bundle.putString("title", direct.name);
                intent.putExtras(bundle);

                if (!this.getActivity().isFinishing()) {
                    this.getContext().startActivity(intent);
                }
            }
        }
    }

    @Override
    public void startTabulationActivity(String title, String uri) {
        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(uri)) {
            Intent intent = new Intent();
            intent.setClass(this.getContext(), TabulationActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("uri", uri);
            bundle.putString("name", title);
            bundle.putString("title", title);
            intent.putExtras(bundle);

            if (!this.getActivity().isFinishing()) {
                this.getContext().startActivity(intent);
            }
        }
    }

    @Override
    public void startSearchActivity() {
        Intent intent = new Intent();
        intent.setClass(this.getContext(), SearchActivity.class);

        if (!this.getActivity().isFinishing()) {
            this.getContext().startActivity(intent);
        }
    }

    @Override
    public void showPromptView(View view, Book book) {
        initPopupWindow(view, book);
    }

    @Override
    public void onClickedCategory(Category category) {
        if (!TextUtils.isEmpty(category.title)) {
            Intent intent = new Intent();
            intent.setClass(this.getContext(), TabulationActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("name", category.title);
            bundle.putString("type", "category");
            bundle.putString("title", category.title);
            intent.putExtras(bundle);

            if (!this.getActivity().isFinishing()) {
                this.getContext().startActivity(intent);
            }
        }
    }

    private void initPopupWindow(View view, final Book book) {
        try {
            View dropView = View.inflate(this.getContext(), R.layout.layout_window_tabulation, null);

            TextView tabulation_window_insert = (TextView) dropView.findViewById(R.id.tabulation_window_insert);
            TextView tabulation_window_comment = (TextView) dropView.findViewById(R.id.tabulation_window_comment);

            final DropWindow dropWindow = new DropWindow(this.getContext(), dropView);

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

            showPopupWindow(dropWindow, dropView, view);

        } catch (InflateException inflateException) {
            collectException(inflateException);
            inflateException.printStackTrace();
        }
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

    public void showPopupWindow(DropWindow dropWindow, View dropView, View root) {
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