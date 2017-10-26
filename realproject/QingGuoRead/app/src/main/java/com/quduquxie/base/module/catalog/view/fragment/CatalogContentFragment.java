package com.quduquxie.base.module.catalog.view.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.StringSignature;
import com.quduquxie.R;
import com.quduquxie.application.component.ApplicationComponent;
import com.quduquxie.base.BaseFragment;
import com.quduquxie.base.bean.Book;
import com.quduquxie.base.bean.Chapter;
import com.quduquxie.base.config.BaseConfig;
import com.quduquxie.base.database.helper.BookDaoHelper;
import com.quduquxie.base.listener.ChapterListener;
import com.quduquxie.base.module.catalog.CatalogContentInterface;
import com.quduquxie.base.module.catalog.adapter.CatalogAdapter;
import com.quduquxie.base.module.catalog.component.DaggerCatalogContentComponent;
import com.quduquxie.base.module.catalog.module.CatalogContentModule;
import com.quduquxie.base.module.catalog.presenter.CatalogContentPresenter;
import com.quduquxie.base.util.CommunalUtil;
import com.quduquxie.base.util.NetworkUtil;
import com.quduquxie.base.widget.CustomLoadingPage;
import com.quduquxie.base.widget.helper.CustomLinearLayoutManager;
import com.quduquxie.communal.widget.GlideRoundTransform;
import com.quduquxie.modular.cover.view.CoverActivity;
import com.quduquxie.module.read.reading.view.ReadingActivity;
import com.quduquxie.widget.LoadingPage;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created on 17/7/27.
 * Created by crazylei.
 */

public class CatalogContentFragment extends BaseFragment<CatalogContentPresenter> implements CatalogContentInterface.View, ChapterListener {

    @BindView(R.id.catalog_content_root)
    LinearLayout catalog_content_root;
    @BindView(R.id.catalog_content_book)
    RelativeLayout catalog_content_book;
    @BindView(R.id.catalog_content_book_cover)
    ImageView catalog_content_book_cover;
    @BindView(R.id.catalog_content_book_name)
    TextView catalog_content_book_name;
    @BindView(R.id.catalog_content_book_author)
    TextView catalog_content_book_author;
    @BindView(R.id.catalog_content_book_divider)
    View catalog_content_book_divider;
    @BindView(R.id.catalog_content_result)
    RecyclerView catalog_content_result;
    @BindView(R.id.catalog_navigation_view)
    LinearLayout catalog_navigation_view;
    @BindView(R.id.catalog_navigation_first)
    RelativeLayout catalog_navigation_first;
    @BindView(R.id.catalog_navigation_index_first)
    ImageView catalog_navigation_index_first;
    @BindView(R.id.catalog_navigation_second)
    RelativeLayout catalog_navigation_second;
    @BindView(R.id.catalog_navigation_index_second)
    ImageView catalog_navigation_index_second;
    @BindView(R.id.catalog_navigation_third)
    RelativeLayout catalog_navigation_third;
    @BindView(R.id.catalog_navigation_index_third)
    ImageView catalog_navigation_index_third;
    @BindView(R.id.catalog_navigation_fourth)
    RelativeLayout catalog_navigation_fourth;
    @BindView(R.id.catalog_navigation_index_fourth)
    ImageView catalog_navigation_index_fourth;
    @BindView(R.id.catalog_navigation_fifth)
    RelativeLayout catalog_navigation_fifth;
    @BindView(R.id.catalog_navigation_index_fifth)
    ImageView catalog_navigation_index_fifth;
    @BindView(R.id.catalog_navigation_sixth)
    RelativeLayout catalog_navigation_sixth;
    @BindView(R.id.catalog_navigation_index_sixth)
    ImageView catalog_navigation_index_sixth;
    @BindView(R.id.catalog_navigation_seven)
    RelativeLayout catalog_navigation_seven;
    @BindView(R.id.catalog_navigation_index_seven)
    ImageView catalog_navigation_index_seven;
    @BindView(R.id.catalog_navigation_eight)
    RelativeLayout catalog_navigation_eight;
    @BindView(R.id.catalog_navigation_index_eight)
    ImageView catalog_navigation_index_eight;
    @BindView(R.id.catalog_navigation_ninth)
    RelativeLayout catalog_navigation_ninth;
    @BindView(R.id.catalog_navigation_index_ninth)
    ImageView catalog_navigation_index_ninth;
    @BindView(R.id.catalog_navigation_tenth)
    RelativeLayout catalog_navigation_tenth;
    @BindView(R.id.catalog_navigation_index_tenth)
    ImageView catalog_navigation_index_tenth;

    @Inject
    CatalogContentPresenter catalogContentPresenter;

    private Book book;

    private String form;

    private LoadingPage loadingPage;

    private ArrayList<Chapter> chapters = new ArrayList<>();

    private CatalogAdapter catalogAdapter;

    private CustomLinearLayoutManager customLinearLayoutManager;

    private int current_navigation = 0;

    private List<ImageView> navigationList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_fragment_catalog_content, container, false);
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
        StatService.onPageStart(this.getContext(), CatalogContentFragment.class.getSimpleName());
    }

    @Override
    public void onPause() {
        super.onPause();
        StatService.onPageEnd(this.getContext(), CatalogContentFragment.class.getSimpleName());
    }

    @Override
    protected void setFragmentComponent(ApplicationComponent applicationComponent) {
        DaggerCatalogContentComponent.builder()
                .applicationComponent(applicationComponent)
                .catalogContentModule(new CatalogContentModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void initializeParameter() {

        Bundle bundle = this.getArguments();

        if (bundle != null) {
            book = (Book) bundle.getSerializable("book");
            form = bundle.getString("form");
        }

        catalogAdapter = new CatalogAdapter(this.getContext(), chapters);
        catalogAdapter.setChapterListener(this);
        catalogAdapter.setActivityForm(form);

        customLinearLayoutManager = new CustomLinearLayoutManager(this.getContext());

        navigationList = new LinkedList<>();
        navigationList.add(catalog_navigation_index_first);
        navigationList.add(catalog_navigation_index_second);
        navigationList.add(catalog_navigation_index_third);
        navigationList.add(catalog_navigation_index_fourth);
        navigationList.add(catalog_navigation_index_fifth);
        navigationList.add(catalog_navigation_index_sixth);
        navigationList.add(catalog_navigation_index_seven);
        navigationList.add(catalog_navigation_index_eight);
        navigationList.add(catalog_navigation_index_ninth);
        navigationList.add(catalog_navigation_index_tenth);


        if (book != null && !"CatalogActivity".equals(form)) {
            catalog_content_book.setVisibility(View.VISIBLE);

            if (book.book_type == Book.TYPE_ONLINE) {

                boolean viewState = (BaseConfig.READING_BACKGROUND_MODE == 5 || BaseConfig.READING_BACKGROUND_MODE == 6);

                catalog_content_book_name.setText(TextUtils.isEmpty(book.name) ? "青果作品" : book.name);
                catalog_content_book_name.setTextColor(viewState ? Color.parseColor("#9B9B9B") : Color.parseColor("#444444"));

                if (book.author != null) {
                    catalog_content_book_author.setText(TextUtils.isEmpty(book.author.name) ? "青果作家" : book.author.name);
                    catalog_content_book_author.setTextColor(viewState ? Color.parseColor("#9B9B9B") : Color.parseColor("#3E3E3E"));
                }

                catalog_content_book_cover.setVisibility(View.VISIBLE);

                if (!TextUtils.isEmpty(book.image)) {
                    Glide.with(this.getContext())
                            .load(book.image)
                            .signature(new StringSignature(book.image))
                            .transform(new GlideRoundTransform(this.getContext()))
                            .diskCacheStrategy(DiskCacheStrategy.RESULT)
                            .error(R.drawable.icon_cover_default)
                            .placeholder(R.drawable.icon_cover_default)
                            .into(catalog_content_book_cover);
                } else {
                    catalog_content_book_cover.setImageResource(R.drawable.icon_cover_default);
                }

                catalog_content_book_divider.setBackgroundColor(viewState ? Color.parseColor("#464646") : Color.parseColor("#D8D8D8"));

            } else {
                catalog_content_book.setVisibility(View.GONE);
            }
        } else {
            catalog_content_book.setVisibility(View.GONE);
        }

        catalog_content_result.setAdapter(catalogAdapter);
        catalog_content_result.setLayoutManager(customLinearLayoutManager);

        catalogContentPresenter.initializeChapters(book);
    }

    @Override
    public void recycle() {

    }

    @Override
    public void showLoadingPage() {
        if (loadingPage != null) {
            loadingPage.onSuccess();
        } else {
            loadingPage = new LoadingPage(this.getActivity(), catalog_content_root);
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
    public void insertChapters(ArrayList<Chapter> chapterList) {
        if (chapters == null) {
            chapters = new ArrayList<>();
        } else {
            chapters.clear();
        }

        if (chapterList != null && chapterList.size() > 0) {
            chapters.addAll(chapterList);
            initializeChapters();
        } else {
            showPromptMessage("目录请求失败！");
            showLoadingError();
        }
    }

    @OnClick({R.id.catalog_content_book, R.id.catalog_navigation_first, R.id.catalog_navigation_second, R.id.catalog_navigation_third, R.id.catalog_navigation_fourth, R.id.catalog_navigation_fifth, R.id.catalog_navigation_sixth, R.id.catalog_navigation_seven, R.id.catalog_navigation_eight, R.id.catalog_navigation_ninth, R.id.catalog_navigation_tenth})
    public void onClickListener(View view) {
        switch (view.getId()) {
            case R.id.catalog_content_book:
                if (book != null && !TextUtils.isEmpty(book.id)) {
                    Intent intent = new Intent();
                    intent.setClass(this.getContext(), CoverActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("id", book.id);
                    intent.putExtras(bundle);

                    if (!this.getActivity().isFinishing()) {
                        startActivity(intent);
                    }
                }
                break;
            case R.id.catalog_navigation_first:
                onNavigationClicked(0);
                break;
            case R.id.catalog_navigation_second:
                onNavigationClicked(1);
                break;
            case R.id.catalog_navigation_third:
                onNavigationClicked(2);
                break;
            case R.id.catalog_navigation_fourth:
                onNavigationClicked(3);
                break;
            case R.id.catalog_navigation_fifth:
                onNavigationClicked(4);
                break;
            case R.id.catalog_navigation_sixth:
                onNavigationClicked(5);
                break;
            case R.id.catalog_navigation_seven:
                onNavigationClicked(6);
                break;
            case R.id.catalog_navigation_eight:
                onNavigationClicked(7);
                break;
            case R.id.catalog_navigation_ninth:
                onNavigationClicked(8);
                break;
            case R.id.catalog_navigation_tenth:
                onNavigationClicked(9);
                break;
        }
    }

    @Override
    public void onClickedChapter(Chapter chapter, int position) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        if (chapters != null && !chapters.isEmpty()) {
            if (!CommunalUtil.checkChapterExist(chapter.id, book.id) && book.book_type == Book.TYPE_ONLINE && NetworkUtil.loadNetworkType(this.getContext()) == NetworkUtil.NETWORK_NONE) {
                showPromptMessage("网络不给力，请检查网络连接！");
                return;
            }

            bundle.putInt("sequence", position);
            bundle.putInt("offset", 0);
            intent.setClass(this.getContext(), ReadingActivity.class);
        }

        bundle.putSerializable("book", book);
        intent.putExtras(bundle);

        if (!this.getActivity().isFinishing()) {
            startActivity(intent);
        }
    }

    private void initializeChapters() {
        BookDaoHelper bookDaoHelper = BookDaoHelper.getInstance(this.getContext());
        if (bookDaoHelper.subscribeBook(book.id)) {
            Book localBook = bookDaoHelper.loadBook(book.id);
            if (localBook != null) {
                catalogAdapter.setSelectedPosition(localBook.sequence);
                catalog_content_result.smoothScrollToPosition(localBook.sequence);
            } else {
                catalogAdapter.setSelectedPosition(0);
                catalog_content_result.smoothScrollToPosition(0);
            }
        } else {
            catalogAdapter.setSelectedPosition(0);
            catalog_content_result.smoothScrollToPosition(0);
        }

        if (chapters != null) {
            if (chapters.size() >= 60) {
                catalog_navigation_view.setVisibility(View.VISIBLE);
                catalog_content_result.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                    }

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);

                        int totalItemCount = customLinearLayoutManager.getItemCount();
                        int firstVisiblePosition = customLinearLayoutManager.findFirstVisibleItemPosition();

                        int distance = totalItemCount / 10;
                        if (current_navigation != firstVisiblePosition / distance) {
                            current_navigation = firstVisiblePosition / distance;
                            changeNavigationIndex(current_navigation);
                        }
                    }
                });
            } else {
                catalog_navigation_view.setVisibility(View.GONE);
            }
        }

        catalogAdapter.notifyDataSetChanged();
    }

    private void changeNavigationIndex(int index) {
        for (int i = 0; i < navigationList.size(); i++) {
            if (i == index) {
                navigationList.get(index).setBackgroundColor(Color.parseColor("#0094D5"));
            } else {
                navigationList.get(i).setBackgroundColor(Color.parseColor("#D8D8D8"));
            }
        }
    }

    private void onNavigationClicked(int index) {
        changeNavigationIndex(index);
        int position = chapters.size() / 10;
        catalog_content_result.smoothScrollToPosition(position * index);
        String firstChapter = chapters.get(position * index).name;
        String lastChapter;
        if (index <= 8) {
            lastChapter = chapters.get(position * (index + 1)).name;
        } else {
            lastChapter = chapters.get(chapters.size() - 1).name;
        }
        showPromptMessage(firstChapter + " ~~ " + lastChapter);
    }
}