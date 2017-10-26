package com.quduquxie.base.module.main.fragment.shelf.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.mobstat.StatService;
import com.quduquxie.R;
import com.quduquxie.application.SharedPreferencesUtil;
import com.quduquxie.application.component.ApplicationComponent;
import com.quduquxie.base.BaseFragment;
import com.quduquxie.base.config.BaseConfig;
import com.quduquxie.base.listener.ShelfSlideDetailListener;
import com.quduquxie.base.bean.Book;
import com.quduquxie.base.database.helper.BookDaoHelper;
import com.quduquxie.base.listener.MainInteractiveListener;
import com.quduquxie.base.listener.ShelfListener;
import com.quduquxie.base.module.main.fragment.shelf.ShelfSlideInterface;
import com.quduquxie.base.module.main.fragment.shelf.adapter.ShelfSlideAdapter;
import com.quduquxie.base.module.main.fragment.shelf.adapter.ShelfSlideDetailAdapter;
import com.quduquxie.base.module.main.fragment.shelf.component.DaggerShelfSlideComponent;
import com.quduquxie.base.module.main.fragment.shelf.module.ShelfSlideModule;
import com.quduquxie.base.module.main.fragment.shelf.presenter.ShelfSlidePresenter;
import com.quduquxie.base.module.main.activity.view.MainActivity;
import com.quduquxie.base.util.CommunalUtil;
import com.quduquxie.base.widget.helper.CustomLinearSnapHelper;

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

public class ShelfSlideFragment extends BaseFragment<ShelfSlidePresenter> implements ShelfSlideInterface.View, ShelfListener, ShelfSlideDetailListener {

    @BindView(R.id.shelf_slide_detail_content)
    RecyclerView shelf_slide_detail_content;
    @BindView(R.id.shelf_slide_content)
    RecyclerView shelf_slide_content;

    @Inject
    ShelfSlidePresenter shelfSlidePresenter;

    private MainInteractiveListener mainInteractiveListener;

    private ShelfSlideAdapter shelfSlideAdapter;

    private ShelfSlideDetailAdapter shelfSlideDetailAdapter;

    private BookDaoHelper bookDaoHelper;

    private ArrayList<Book> books = new ArrayList<>();

    private int position = 0;

    public static final int TYPE_INSERT_BOOK = 0x60;

    private int width;
    private int itemWidth;

    private int totalY = 0;

    private int count = 0;

    private SharedPreferencesUtil sharedPreferencesUtil;

    private LinearLayoutManager shelfLinearLayoutManager;

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

        width = this.getResources().getDimensionPixelOffset(R.dimen.width_186);

        itemWidth = this.getResources().getDimensionPixelOffset(R.dimen.width_186);

        sharedPreferencesUtil = this.loadSharedPreferencesUtil();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_fragment_shelf_slide, container, false);
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

        if (mainInteractiveListener != null) {
            mainInteractiveListener.storeContentScrollDistance(totalY);
        }

        StatService.onPageStart(this.getContext(), ShelfSlideFragment.class.getSimpleName());
    }

    @Override
    public void onPause() {
        super.onPause();
        StatService.onPageEnd(this.getContext(), ShelfSlideFragment.class.getSimpleName());
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (!hidden) {
            if (mainInteractiveListener != null) {
                mainInteractiveListener.storeContentScrollDistance(totalY);

                if (shelfLinearLayoutManager != null) {
                    position = shelfLinearLayoutManager.findFirstVisibleItemPosition();

                    if (position != shelfLinearLayoutManager.getItemCount() - 1) {
                        mainInteractiveListener.changeTitle(String.valueOf(position + 1), String.valueOf(count));
                    } else {
                        mainInteractiveListener.changeTitle(String.valueOf(position), String.valueOf(count));
                    }
                }
            }

            shelfSlidePresenter.initializeData();
        }
    }

    @Override
    protected void setFragmentComponent(ApplicationComponent applicationComponent) {
        DaggerShelfSlideComponent.builder()
                .applicationComponent(applicationComponent)
                .shelfSlideModule(new ShelfSlideModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void initializeParameter() {

        if (mainInteractiveListener != null) {
            mainInteractiveListener.changeTitle("我的书架", MessageFormat.format("{0}本书", count));
        }

        if (bookDaoHelper == null) {
            bookDaoHelper = BookDaoHelper.getInstance(getContext());
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false);
        shelf_slide_detail_content.setLayoutManager(linearLayoutManager);

        shelfSlideDetailAdapter = new ShelfSlideDetailAdapter(this.getContext(), books);
        shelfSlideDetailAdapter.setShelfSlideDetailListener(this);

        shelf_slide_detail_content.setAdapter(shelfSlideDetailAdapter);
        shelf_slide_detail_content.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
                return recyclerView.getScrollState() == RecyclerView.SCROLL_STATE_DRAGGING;
            }
        });

        shelfLinearLayoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false);
        shelf_slide_content.setLayoutManager(shelfLinearLayoutManager);

        shelfSlideAdapter = new ShelfSlideAdapter(this.getContext(), books);
        shelfSlideAdapter.setShelfListener(this);

        shelf_slide_content.setAdapter(shelfSlideAdapter);

        CustomLinearSnapHelper linearSnapHelper = new CustomLinearSnapHelper();
        linearSnapHelper.attachToRecyclerView(shelf_slide_content);

        shelf_slide_content.addOnScrollListener(new RecyclerView.OnScrollListener() {

            private int totalX = 0;
            private int resetTotalX = 0;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int first = shelfLinearLayoutManager.findFirstVisibleItemPosition();

                totalX += dx;

                mainInteractiveListener.changeTitle(String.valueOf(first + 1), String.valueOf(count));

                float speed = totalX * 1.000f / itemWidth;

                float totalOffset = width * speed;

                int resetDx = (int) (totalOffset - resetTotalX);

                resetTotalX += resetDx;

                shelf_slide_detail_content.scrollBy(resetDx, dy);
            }
        });

        shelfSlidePresenter.initializeData();
    }

    @Override
    public void recycle() {

    }

    @Override
    public void resetBookData(ArrayList<Book> bookList) {
        if (bookList == null || bookList.size() == 0) {
            count = 0;

            if (mainInteractiveListener != null) {
                mainInteractiveListener.changeTitle("0", String.valueOf(count));
                mainInteractiveListener.showEmptyPrompt();
            }
        } else {
            count = bookList.size();

            if (mainInteractiveListener != null) {
                mainInteractiveListener.changeTitle(String.valueOf(position + 1), String.valueOf(count));
                mainInteractiveListener.hideEmptyPrompt();
            }

            if (books == null) {
                books = new ArrayList<>();
            } else {
                books.clear();
            }

            for (Book book : bookList) {
                books.add(book);
            }

            checkMoreState(count);

            insertPromptBook(ShelfSlideFragment.TYPE_INSERT_BOOK);

            shelfSlideDetailAdapter.notifyDataSetChanged();

            shelfSlideAdapter.notifyDataSetChanged();
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
    public void onClickedOnlineBook(Book book, String type) {

        if (!shelfSlideAdapter.loadDeleteState()) {
            if (book != null && mainInteractiveListener != null) {
                mainInteractiveListener.cancelUpdateStatus(book.id);
            }

            if (book != null) {

                if (book.book_type == Book.TYPE_ONLINE) {

                    if (BaseConfig.DISABLE.equals(book.status)) {
                        showPromptMessage("书籍已下架，请从书架移除！");
                        return;
                    }

                    if ("Chapter".equals(type)) {
                        CommunalUtil.startReadingActivity(this.getActivity(), book);
                    } else if ("Comment".equals(type)) {
                        CommunalUtil.startCommentActivity(this.getActivity(), book);
                    } else if ("AuthorTalk".equals(type)) {
                        CommunalUtil.startCoverActivity(this.getActivity(), book);
                    } else {
                        CommunalUtil.startCoverOrReading(this.getActivity(), book, true);
                    }
                }

            }
        }
    }

    @Override
    public void onClickedLocalBook(Book book) {

        if (!shelfSlideAdapter.loadDeleteState()) {

            if (book != null && mainInteractiveListener != null) {
                mainInteractiveListener.cancelUpdateStatus(book.id);
            }

            if (book != null) {
                if (book.book_type == Book.TYPE_LOCAL_TXT) {
                    File file = new File(book.file_path);
                    if (!file.exists()) {
                        showPromptMessage("文件不存在，请从书架移除！");
                        return;
                    }
                }

                CommunalUtil.startCoverOrReading(this.getActivity(), book, true);
            }
        }
    }

    @Override
    public void clickedBook(Book book, boolean delete) {

        if (book != null && mainInteractiveListener != null) {
            mainInteractiveListener.cancelUpdateStatus(book.id);
        }

        if (delete) {
            if (mainInteractiveListener != null) {
                mainInteractiveListener.resetDeleteSelected(shelfSlideAdapter.loadCheckedBookSize());
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

        removePromptBook();

        shelfSlideAdapter.updateDeleteState(true);
        shelfSlideDetailAdapter.notifyDataSetChanged();

        if (mainInteractiveListener != null) {
            mainInteractiveListener.hideBottomView(shelfSlideAdapter.loadCheckedBookSize());
        }
    }

    private void insertPromptBook(int type) {
        if (books != null && !books.isEmpty()) {
            Book book = new Book();
            book.item_type = type;
            books.add(book);
        }
    }

    private void removePromptBook() {
        if (books != null && !books.isEmpty()) {
            for (int i = 0; i < books.size(); i++) {
                if (books.get(i).item_type == ShelfSlideFragment.TYPE_INSERT_BOOK) {
                    books.remove(i);
                    break;
                }
            }
        }
    }

    public ArrayList<Book> loadCheckedBooks() {
        return shelfSlideAdapter.loadCheckedBooks();
    }

    public int loadBookCount() {
        return count;
    }

    public void refreshBook(Book book) {
        if (book != null && shelfSlideAdapter != null) {
            shelfSlideAdapter.notifyItemChanged(book);
        }

        if (book != null && !TextUtils.isEmpty(book.id) && shelfSlideDetailAdapter != null) {
            int index = books.indexOf(book);
            if (index != -1) {

                if (bookDaoHelper == null) {
                    bookDaoHelper = BookDaoHelper.getInstance(this.getContext());
                }

                Book localBook = bookDaoHelper.loadBook(book.id);

                if (localBook != null) {
                    books.remove(index);
                    books.add(index, localBook);
                    shelfSlideDetailAdapter.notifyItemChanged(index);
                }
            }
        }
    }

    public void refreshDeleteData(boolean state) {

        int position = -1;

        if (books != null && !books.isEmpty()) {
            for (int i = 0; i < books.size(); i++) {
                if (books.get(i).item_type == ShelfSlideFragment.TYPE_INSERT_BOOK) {
                    position = i;
                    break;
                }
            }
        }

        if (position == -1) {
            insertPromptBook(ShelfSlideFragment.TYPE_INSERT_BOOK);
        }
        shelfSlideDetailAdapter.notifyDataSetChanged();
        shelfSlideAdapter.updateDeleteState(state);
    }

    public void refreshDataSet() {
        shelfSlidePresenter.initializeData();
    }

    private void checkMoreState(int count) {
        if (count > 10) {
            if (sharedPreferencesUtil == null) {
                sharedPreferencesUtil = this.loadSharedPreferencesUtil();
            }

            if (!sharedPreferencesUtil.loadBoolean(BaseConfig.FLAG_SHELF_SLIDE_MORE_GUIDE, false)) {
                if (mainInteractiveListener != null) {
                    mainInteractiveListener.showShelfSlideMore();
                }
            } else {
                if (mainInteractiveListener != null) {
                    mainInteractiveListener.hideShelfSlideMore();
                }
            }
        } else {
            if (mainInteractiveListener != null) {
                mainInteractiveListener.hideShelfSlideMore();
            }
        }
    }
}