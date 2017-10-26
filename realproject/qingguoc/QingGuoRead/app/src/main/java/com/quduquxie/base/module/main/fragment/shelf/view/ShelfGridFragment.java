package com.quduquxie.base.module.main.fragment.shelf.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.mobstat.StatService;
import com.quduquxie.R;
import com.quduquxie.application.component.ApplicationComponent;
import com.quduquxie.base.BaseFragment;
import com.quduquxie.base.listener.ShelfListener;
import com.quduquxie.base.module.main.fragment.shelf.adapter.ShelfGridAdapter;
import com.quduquxie.base.bean.Book;
import com.quduquxie.base.config.BaseConfig;
import com.quduquxie.base.database.helper.BookDaoHelper;
import com.quduquxie.base.listener.MainInteractiveListener;
import com.quduquxie.base.module.main.fragment.shelf.ShelfGridInterface;
import com.quduquxie.base.module.main.fragment.shelf.component.DaggerShelfGridComponent;
import com.quduquxie.base.module.main.fragment.shelf.module.ShelfGridModule;
import com.quduquxie.base.module.main.fragment.shelf.presenter.ShelfGridPresenter;
import com.quduquxie.base.module.main.activity.view.MainActivity;
import com.quduquxie.base.util.CommunalUtil;
import com.quduquxie.base.widget.helper.ShelfGridSpanSizeLookup;

import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 17/7/19.
 * Created by crazylei.
 */

public class ShelfGridFragment extends BaseFragment<ShelfGridPresenter> implements ShelfGridInterface.View, ShelfListener {

    @BindView(R.id.shelf_grid_content)
    RecyclerView shelf_grid_content;

    @Inject
    ShelfGridPresenter shelfGridPresenter;

    private BookDaoHelper bookDaoHelper;

    private ShelfGridAdapter shelfGridAdapter;

    private MainInteractiveListener mainInteractiveListener;

    private ArrayList<Book> books = new ArrayList<>();

    private int totalY = 0;

    private int count = 0;

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
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_fragment_shelf_grid, container, false);
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
        StatService.onPageStart(this.getContext(), ShelfGridFragment.class.getSimpleName());
    }

    @Override
    public void onPause() {
        super.onPause();
        StatService.onPageEnd(this.getContext(), ShelfGridFragment.class.getSimpleName());
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (!hidden) {
            if (mainInteractiveListener != null) {
                mainInteractiveListener.storeContentScrollDistance(totalY);
                mainInteractiveListener.changeTitle("我的书架", MessageFormat.format("{0}本书", count));
            }

            shelfGridPresenter.initializeData();
        }
    }

    @Override
    public void initializeParameter() {
        if (mainInteractiveListener != null) {
            mainInteractiveListener.changeTitle("我的书架", MessageFormat.format("{0}本书", 0));
        }

        if (bookDaoHelper == null) {
            bookDaoHelper = BookDaoHelper.getInstance(getContext());
        }

        shelfGridAdapter = new ShelfGridAdapter(this.getContext(), books);
        shelfGridAdapter.setShelfClickListener(this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);

        ShelfGridSpanSizeLookup shelfGridSpanSizeLookup = new ShelfGridSpanSizeLookup(shelfGridAdapter);
        gridLayoutManager.setSpanSizeLookup(shelfGridSpanSizeLookup);

        shelf_grid_content.setLayoutManager(gridLayoutManager);
        shelf_grid_content.setAdapter(shelfGridAdapter);

        shelf_grid_content.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int state) {
                super.onScrollStateChanged(recyclerView, state);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int x, int y) {
                super.onScrolled(recyclerView, x, y);

                totalY += y;

                if (mainInteractiveListener != null) {
                    mainInteractiveListener.storeContentScrollDistance(totalY);
                }
            }
        });

        shelfGridPresenter.initializeData();
    }

    @Override
    public void recycle() {

    }

    @Override
    protected void setFragmentComponent(ApplicationComponent applicationComponent) {
        DaggerShelfGridComponent.builder()
                .applicationComponent(applicationComponent)
                .shelfGridModule(new ShelfGridModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void resetBookData(ArrayList<Book> bookList) {
        if (bookList == null || bookList.size() == 0) {
            count = 0;

            if (mainInteractiveListener != null) {
                mainInteractiveListener.changeTitle("我的书架", MessageFormat.format("{0}本书", count));
                mainInteractiveListener.showEmptyPrompt();
            }
        } else {
            count = bookList.size();

            if (mainInteractiveListener != null) {
                mainInteractiveListener.changeTitle("我的书架", MessageFormat.format("{0}本书", count));
                mainInteractiveListener.hideEmptyPrompt();
            }

            if (books == null) {
                books = new ArrayList<>();
            } else {
                books.clear();
            }

            books.add(createBook(ShelfGridAdapter.TYPE_EMPTY_FILLER));

            for (Book book : bookList) {
                books.add(book);
            }

            books.add(createBook(ShelfGridAdapter.TYPE_INSERT_BOOK));

            shelfGridAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void insertBook() {
        Intent intent = new Intent();
        intent.putExtra("position", 1);
        intent.setClass(getContext(), MainActivity.class);

        if (!this.getActivity().isFinishing()) {
            startActivity(intent);
        }
    }

    @Override
    public void clickedBook(Book book, boolean delete) {

        if (book != null && mainInteractiveListener != null) {
            mainInteractiveListener.cancelUpdateStatus(book.id);
        }

        if (delete) {
            if (mainInteractiveListener != null) {
                mainInteractiveListener.resetDeleteSelected(shelfGridAdapter.loadCheckedBookSize());
            }
        } else {
            if (book != null) {
                if (book.book_type == Book.TYPE_LOCAL_TXT) {
                    File file = new File(book.file_path);
                    if (!file.exists()) {
                        showPromptMessage("文件不存在，请从书架移除！");
                        return;
                    }
                } else if (book.book_type == Book.TYPE_ONLINE) {
                    if (BaseConfig.DISABLE.equals(book.status)) {
                        showPromptMessage("书籍已下架，请从书架移除！");
                        return;
                    }
                }

                CommunalUtil.startCoverOrReading(this.getActivity(), book, true);
            }
        }
    }

    @Override
    public void longClickedBook(Book book) {
        shelfGridAdapter.updateDeleteState(true);

        if (mainInteractiveListener != null) {
            mainInteractiveListener.hideBottomView(shelfGridAdapter.loadCheckedBookSize());
        }
    }

    private Book createBook(int type) {
        Book book = new Book();
        book.item_type = type;
        return book;
    }

    public ArrayList<Book> loadCheckedBooks() {
        return shelfGridAdapter.loadCheckedBooks();
    }

    public void refreshBook(Book book) {
        if (book != null && shelfGridAdapter != null) {
            shelfGridAdapter.notifyItemChanged(book);
        }
    }

    public void refreshDeleteData(boolean state) {
        shelfGridAdapter.updateDeleteState(state);
    }

    public void refreshDataSet() {
        shelfGridPresenter.initializeData();
    }
}