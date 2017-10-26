package com.quduquxie.base.module.main.fragment.shelf.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.orhanobut.logger.Logger;
import com.quduquxie.R;
import com.quduquxie.application.SharedPreferencesUtil;
import com.quduquxie.application.component.ApplicationComponent;
import com.quduquxie.base.BaseFragment;
import com.quduquxie.base.bean.Book;
import com.quduquxie.base.config.BaseConfig;
import com.quduquxie.base.database.helper.BookDaoHelper;
import com.quduquxie.base.listener.MainInteractiveListener;
import com.quduquxie.base.module.main.fragment.shelf.BookShelfInterface;
import com.quduquxie.base.module.main.fragment.shelf.component.DaggerBookShelfComponent;
import com.quduquxie.base.module.main.fragment.shelf.module.BookShelfModule;
import com.quduquxie.base.module.main.fragment.shelf.presenter.BookShelfPresenter;
import com.quduquxie.base.util.CommunalUtil;
import com.quduquxie.base.util.NetworkUtil;
import com.quduquxie.base.widget.dialog.CustomDialogFragment;
import com.quduquxie.base.widget.dialog.CustomDialogListener;
import com.quduquxie.base.widget.dialog.ShelfOptionDialogFragment;
import com.quduquxie.base.widget.dialog.ShelfOptionDialogListener;
import com.quduquxie.base.widget.helper.CustomSwipeRefreshLayout;
import com.quduquxie.function.download.view.DownloadManagerActivity;
import com.quduquxie.function.search.view.SearchActivity;
import com.quduquxie.local.view.LocalFilesActivity;
import com.quduquxie.model.v2.CheckUpdateResult;
import com.quduquxie.service.check.CheckNovelUpdateCallBack;
import com.quduquxie.service.check.CheckNovelUpdateService;
import com.quduquxie.wifi.view.WiFiTransportActivity;

import java.lang.ref.WeakReference;
import java.text.MessageFormat;
import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created on 17/7/17.
 * Created by crazylei.
 */

public class BookShelfFragment extends BaseFragment<BookShelfPresenter> implements BookShelfInterface.View, CheckNovelUpdateCallBack {

    @BindView(R.id.shelf_title)
    View shelf_title;
    @BindView(R.id.shelf_head_search)
    ImageView shelf_head_search;
    @BindView(R.id.shelf_head_title)
    TextView shelf_head_title;
    @BindView(R.id.shelf_head_subtitle)
    TextView shelf_head_subtitle;
    @BindView(R.id.shelf_head_more)
    ImageView shelf_head_more;
    @BindView(R.id.shelf_head_divider)
    View shelf_head_divider;

    @BindView(R.id.shelf_slide_more)
    ImageView shelf_slide_more;

    @BindView(R.id.shelf_selected)
    View shelf_selected;
    @BindView(R.id.shelf_delete_cancel)
    TextView shelf_delete_cancel;
    @BindView(R.id.shelf_delete_selected)
    TextView shelf_delete_selected;

    @BindView(R.id.shelf_bottom)
    View shelf_bottom;

    @BindView(R.id.shelf_refresh)
    CustomSwipeRefreshLayout shelf_refresh;
    @BindView(R.id.shelf_content)
    FrameLayout shelf_content;

    @BindView(R.id.shelf_empty)
    ViewStub shelf_empty;

    private View shelf_empty_view;

    ImageView shelf_empty_insert;

    @Inject
    BookShelfPresenter bookShelfPresenter;

    private BookDaoHelper bookDaoHelper;

    private FragmentManager fragmentManager;

    private SharedPreferencesUtil sharedPreferencesUtil;

    private MainInteractiveListener mainInteractiveListener;

    private CustomDialogFragment customDialogFragment;

    private ShelfOptionDialogFragment shelfOptionDialogFragment;

    private final Handler handler = new UiHandler(this);

    public long time = System.currentTimeMillis();

    private int display;

    private long loadDataFinishTime;

    private static final int DELETE_REFRESH = 0x80;

    private static final int PULL_REFRESH_DELAY = 30 * 1000;

    private ShelfGridFragment shelfGridFragment;
    private ShelfSlideFragment shelfSlideFragment;

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

        fragmentManager = this.getFragmentManager();

        sharedPreferencesUtil = this.loadSharedPreferencesUtil();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_fragment_bookshelf, container, false);
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
        StatService.onPageStart(this.getContext(), BookShelfFragment.class.getSimpleName());

        refreshDeletedData();
    }

    @Override
    public void onPause() {
        super.onPause();
        StatService.onPageEnd(this.getContext(), BookShelfFragment.class.getSimpleName());
    }

    @Override
    protected void setFragmentComponent(ApplicationComponent applicationComponent) {
        DaggerBookShelfComponent.builder()
                .applicationComponent(applicationComponent)
                .bookShelfModule(new BookShelfModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (!hidden) {
            refreshDeletedData();
        }
    }

    @Override
    public void initializeParameter() {

        shelf_head_title.setTypeface(typeface_song_depict);

        shelf_head_subtitle.setTypeface(typeface_song);

        shelf_head_divider.setAlpha(0);

        if (sharedPreferencesUtil == null) {
            sharedPreferencesUtil = this.loadSharedPreferencesUtil();
        }

        if (bookDaoHelper == null) {
            bookDaoHelper = BookDaoHelper.getInstance(getContext());
        }

        display = sharedPreferencesUtil.loadInteger(BaseConfig.FLAG_SHELF_DISPLAY_MODE, BaseConfig.SHELF_DISPLAY_GRID);

        changeShelfDisplayView(display);

        shelf_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (!NetworkUtil.checkNetwork(BookShelfFragment.this.getContext())) {
                    showPromptMessage("网络连接异常，请检查网络！");

                    if (shelf_refresh != null) {
                        shelf_refresh.setRefreshing(false);
                    }
                    return;
                }

                long startRefreshTime = System.currentTimeMillis();
                long delay = startRefreshTime - loadDataFinishTime;

                if (delay <= PULL_REFRESH_DELAY) {
                    showPromptMessage("您追看的小说暂无更新！");
                    shelf_refresh.setRefreshing(false);
                } else {
                    shelf_refresh.setRefreshing(true);
                    insertCheckUpdateTask();
                }
            }
        });
    }

    @Override
    public void recycle() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @OnClick({R.id.shelf_head_search, R.id.shelf_head_more, R.id.shelf_delete_cancel, R.id.shelf_bottom})
    public void OnItemClick(View view) {
        switch (view.getId()) {
            case R.id.shelf_head_search:
                startSearchActivity();
                break;
            case R.id.shelf_head_more:
                if (shelf_slide_more != null) {
                    shelf_slide_more.setVisibility(View.GONE);

                    if (sharedPreferencesUtil == null) {
                        sharedPreferencesUtil = loadSharedPreferencesUtil();
                    }

                    sharedPreferencesUtil.insertBoolean(BaseConfig.FLAG_SHELF_SLIDE_MORE_GUIDE, true);
                }
                showShelfMoreDialog();
                break;
            case R.id.shelf_delete_cancel:
                hideDeleteView();
                break;
            case R.id.shelf_bottom:
                showDeleteConfirmDialog();
                break;
        }
    }

    @Override
    public void onSuccess(CheckUpdateResult checkUpdateResult) {
        loadDataFinishTime = System.currentTimeMillis();

        if (shelf_refresh != null) {
            shelf_refresh.setRefreshing(false);
        }

        if (sharedPreferencesUtil == null) {
            sharedPreferencesUtil = this.loadSharedPreferencesUtil();
        }

        sharedPreferencesUtil.insertLong(BaseConfig.FLAG_SHELF_CHECK_BOOK_TIME, System.currentTimeMillis());

        checkUpdateSuccessToast(checkUpdateResult);

        if (checkUpdateResult != null && checkUpdateResult.books != null && checkUpdateResult.books.size() > 0) {
            for (Book book : checkUpdateResult.books) {
                if (book != null && !TextUtils.isEmpty(book.id)) {
                    Book localBook = bookDaoHelper.loadBook(book.id, Book.TYPE_ONLINE);
                    if (localBook != null) {
                        refreshDataItem(book);
                    }
                }
            }
        }
    }

    @Override
    public void onException(Exception exception) {
        collectException(exception);

        showPromptMessage("检查更新异常，请检查网络连接！");

        loadDataFinishTime = System.currentTimeMillis();

        if (shelf_refresh != null) {
            shelf_refresh.setRefreshing(false);
        }
    }

    private void changeShelfDisplayView(int mode) {

        if (sharedPreferencesUtil == null) {
            sharedPreferencesUtil = this.loadSharedPreferencesUtil();
        }

        sharedPreferencesUtil.insertInteger(BaseConfig.FLAG_SHELF_DISPLAY_MODE, display);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        hideFragment(fragmentTransaction);

        switch (mode) {
            case BaseConfig.SHELF_DISPLAY_GRID:
                if (shelfGridFragment == null) {
                    shelfGridFragment = new ShelfGridFragment();
                    fragmentTransaction.add(R.id.shelf_content, shelfGridFragment);
                } else {
                    fragmentTransaction.show(shelfGridFragment);
                }

                break;
            case BaseConfig.SHELF_DISPLAY_SLIDE:
                if (shelfSlideFragment == null) {
                    shelfSlideFragment = new ShelfSlideFragment();
                    fragmentTransaction.add(R.id.shelf_content, shelfSlideFragment);
                } else {
                    fragmentTransaction.show(shelfSlideFragment);
                }

                mainInteractiveListener.checkShelfGuide();

                break;
        }

        if (!this.getActivity().isFinishing()) {
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    private void hideFragment(FragmentTransaction fragmentTransaction) {
        if (shelfGridFragment != null) {
            fragmentTransaction.hide(shelfGridFragment);
        }

        if (shelfSlideFragment != null) {
            fragmentTransaction.hide(shelfSlideFragment);
        }
    }

    private void insertCheckUpdateTask() {
        if (applicationUtil != null) {
            CheckNovelUpdateService checkNovelUpdateService = applicationUtil.getCheckNovelUpdateService();
            if (checkNovelUpdateService != null && bookDaoHelper.loadOnlineBookSize() > 0) {
                ArrayList<Book> books = bookDaoHelper.loadOnlineReadBooks();
                checkNovelUpdateService.addCheckNovelUpdateTask(CommunalUtil.initCheckNovelUpdateTask(books, this));
            } else {
                if (shelf_refresh != null) {
                    shelf_refresh.setRefreshing(false);
                }
            }
        }
    }

    protected void checkUpdateSuccessToast(CheckUpdateResult checkUpdateResult) {
        ArrayList<Book> hasUpdateList = new ArrayList<>();
        ArrayList<Book> repairList = new ArrayList<>();

        if (checkUpdateResult != null && checkUpdateResult.books != null && checkUpdateResult.books.size() > 0) {
            ArrayList<Book> books = checkUpdateResult.books;
            int size = books.size();
            if (size > 0) {
                for (int i = 0; i < size; i++) {
                    Book book = books.get(i);
                    if (book != null && !TextUtils.isEmpty(book.id)) {
                        if (book.update_count > 0) {
                            hasUpdateList.add(book);
                        } else if (book.update_count == 0) {
                            if (book.repairBookmark) {
                                repairList.add(book);
                            }
                        }
                    }
                }
            }
            if (bookDaoHelper.loadOnlineBookSize() > 0) {
                if (hasUpdateList.size() == 0) {
                    showPromptMessage("您追看的小说暂无更新！");
                } else {
                    checkUpdateSuccessMoreToast(hasUpdateList.size(), hasUpdateList);
                }

                if (repairList.size() > 0) {
                    showRepairBookMarkToast(repairList);
                }
            }
        }
    }


    private void checkUpdateSuccessMoreToast(int count, ArrayList<Book> books) {
        String name = null;

        Book book = books.get(0);

        if (getContext() == null || book == null) {
            return;
        }

        if (!TextUtils.isEmpty(book.name)) {
            name = book.name;
        }

        if (name != null && !TextUtils.isEmpty(name)) {
            if (count == 1) {
                showPromptMessage("《" + name + "》更新了" + book.update_count + "章");
            } else {
                int size = books.size();
                int updateCount = 0;
                for (int i = 0; i < size; i++) {
                    updateCount += books.get(i).update_count;
                }
                showPromptMessage("《" + name + "》等" + size + "本，更新了" + updateCount + "章");
            }
        }
    }

    private void showRepairBookMarkToast(ArrayList<Book> repairList) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("为了给你最好的阅读体验，");

        for (Book book : repairList) {
            if (book != null && !TextUtils.isEmpty(book.name)) {
                stringBuilder.append("《").append(book.name).append("》");
            }
        }
        stringBuilder.append("进行了修订。 \n\n");
        stringBuilder.append("请重新加入书签，开始新的故事。");
        showPromptMessage(stringBuilder.toString());
    }

    public void refreshDataItem(Book book) {

        if (display == BaseConfig.SHELF_DISPLAY_GRID) {

            if (shelfGridFragment != null) {
                shelfGridFragment.refreshBook(book);
            }

        } else if (display == BaseConfig.SHELF_DISPLAY_SLIDE) {

            if (shelfSlideFragment != null) {
                shelfSlideFragment.refreshBook(book);
                Logger.e("RefreshDataItem: " + book.name + " : " + book.update_count);
            }
        }
    }

    private void startSearchActivity() {
        Intent intent = new Intent();
        intent.setClass(this.getContext(), SearchActivity.class);

        if (!this.getActivity().isFinishing()) {
            startActivity(intent);
        }
    }

    private void showShelfMoreDialog() {
        if (shelfOptionDialogFragment == null) {
            shelfOptionDialogFragment = new ShelfOptionDialogFragment();
        }

        if (display == BaseConfig.SHELF_DISPLAY_GRID) {
            shelfOptionDialogFragment.resetDisplayMode(BaseConfig.SHELF_DISPLAY_SLIDE);
        } else if (display == BaseConfig.SHELF_DISPLAY_SLIDE) {
            shelfOptionDialogFragment.resetDisplayMode(BaseConfig.SHELF_DISPLAY_GRID);
        }

        shelfOptionDialogFragment.setShelfOptionDialogListener(new ShelfOptionDialogListener() {

            @Override
            public void startWiFiTransportActivity() {
                hideShelfOptionDialogFragment();
                startOtherActivity(WiFiTransportActivity.class);
            }

            @Override
            public void startLocalFilesActivity() {
                hideShelfOptionDialogFragment();
                startOtherActivity(LocalFilesActivity.class);
            }

            @Override
            public void startDownloadManagerActivity() {
                hideShelfOptionDialogFragment();
                startOtherActivity(DownloadManagerActivity.class);
            }

            @Override
            public void changeShelfDisplayMode() {
                hideShelfOptionDialogFragment();
                if (display == BaseConfig.SHELF_DISPLAY_GRID) {
                    display = BaseConfig.SHELF_DISPLAY_SLIDE;
                } else if (display == BaseConfig.SHELF_DISPLAY_SLIDE) {
                    display = BaseConfig.SHELF_DISPLAY_GRID;
                }

                changeShelfDisplayView(display);
            }

            @Override
            public void cancelShelfOptionDialog() {
                hideShelfOptionDialogFragment();
            }
        });

        if (getActivity() != null && !getActivity().isFinishing()) {
            if (shelfOptionDialogFragment.isAdded()) {
                shelfOptionDialogFragment.setShowsDialog(true);
            } else {
                shelfOptionDialogFragment.show(getFragmentManager(), "ShelfOptionDialogFragment");
            }

        }
    }

    private void hideShelfOptionDialogFragment() {
        if (getActivity() != null && !getActivity().isFinishing() && shelfOptionDialogFragment != null && shelfOptionDialogFragment.getShowsDialog()) {
            shelfOptionDialogFragment.dismiss();
        }
    }

    private void startOtherActivity(Class<?> classObject) {
        Intent intent = new Intent();
        intent.setClass(this.getContext(), classObject);

        if (!this.getActivity().isFinishing()) {
            startActivity(intent);
        }
    }

    private void showDeleteConfirmDialog() {

        ArrayList<Book> checkedBooks = null;

        if (display == BaseConfig.SHELF_DISPLAY_GRID) {
            if (shelfGridFragment != null) {
                checkedBooks = shelfGridFragment.loadCheckedBooks();
            }
        } else if (display == BaseConfig.SHELF_DISPLAY_SLIDE) {
            if (shelfSlideFragment != null) {
                checkedBooks = shelfSlideFragment.loadCheckedBooks();
            }
        }

        if (checkedBooks == null || checkedBooks.isEmpty()) {
            showPromptMessage("请选择书籍！");
            return;
        }

        if (customDialogFragment == null) {
            customDialogFragment = new CustomDialogFragment();
        }

        customDialogFragment.setPrompt("确认要删除作品？");
        customDialogFragment.setFirstOption("取消");
        customDialogFragment.setSecondOption("删除");

        final ArrayList<Book> checkedBookList = checkedBooks;

        customDialogFragment.setCustomDialogListener(new CustomDialogListener() {
            @Override
            public void onFirstOptionClicked() {
                hideCustomDialogFragment();
            }

            @Override
            public void onSecondOptionClicked() {
                deleteCheckedBooks(checkedBookList);
                hideCustomDialogFragment();
            }
        });

        if (getActivity() != null && !getActivity().isFinishing()) {
            if (customDialogFragment.isAdded()) {
                customDialogFragment.setShowsDialog(true);
            } else {
                customDialogFragment.show(getFragmentManager(), "CustomDialogFragment");
            }

        }
    }

    public void hideCustomDialogFragment() {
        if (getActivity() != null && !getActivity().isFinishing() && customDialogFragment != null && customDialogFragment.getShowsDialog()) {
            customDialogFragment.dismiss();
        }
    }

    private void deleteCheckedBooks(ArrayList<Book> books) {
        if (books == null || books.isEmpty()) {
            showPromptMessage("请选择书籍！");
        } else {
            handleCheckedBooks(books);
        }
    }

    private void handleCheckedBooks(final ArrayList<Book> books) {
        final int size = books.size();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String[] book_ids = new String[size];
                for (int i = 0; i < books.size(); i++) {
                    Book book = books.get(i);
                    book_ids[i] = book.id;
                }
                if (bookDaoHelper != null) {
                    bookDaoHelper.deleteBook(book_ids);
                }
                handler.obtainMessage(DELETE_REFRESH).sendToTarget();
            }
        }).start();
    }

    public void refreshDeletedData() {

        if (display == BaseConfig.SHELF_DISPLAY_GRID) {
            if (shelfGridFragment != null) {
                shelfGridFragment.refreshDataSet();
            }
        } else if (display == BaseConfig.SHELF_DISPLAY_SLIDE) {
            if (shelfSlideFragment != null) {
                shelfSlideFragment.refreshDataSet();
            }
        }

        hideDeleteView();
    }

    public void changeTitle(String title, String subtitle) {
        if (shelf_head_title != null) {
            shelf_head_title.setText(title);
        }

        if (shelf_head_subtitle != null) {
            shelf_head_subtitle.setText(subtitle);
        }
    }

    public void cancelUpdateStatus(String id) {
        if (bookDaoHelper == null) {
            bookDaoHelper = BookDaoHelper.getInstance(this.getContext());
        }

        Book book = new Book();
        book.id = id;
        book.update_count = 0;
        book.update_status = 0;
        bookDaoHelper.updateBook(book);
    }

    public void resetDeleteSelected(int count) {
        if (shelf_delete_selected != null) {
            shelf_delete_selected.setText(MessageFormat.format("已选中{0}本", count));
        }
    }

    public void changeDeleteView(boolean state, int count) {
        if (shelf_selected != null) {
            shelf_selected.setVisibility(state ? View.VISIBLE : View.GONE);
        }
        if (shelf_bottom != null) {
            shelf_bottom.setVisibility(state ? View.VISIBLE : View.GONE);
            resetDeleteSelected(count);
        }
        if (shelf_title != null) {
            shelf_title.setVisibility(state ? View.GONE : View.VISIBLE);
        }

        if (state) {
            if (shelf_slide_more.getVisibility() == View.VISIBLE) {
                shelf_slide_more.setVisibility(View.GONE);
            }
        } else {
            if (sharedPreferencesUtil == null) {
                sharedPreferencesUtil = this.loadSharedPreferencesUtil();
            }

            if (!sharedPreferencesUtil.loadBoolean(BaseConfig.FLAG_SHELF_SLIDE_MORE_GUIDE, false) && shelfSlideFragment != null && shelfSlideFragment.isVisible() && shelfSlideFragment.loadBookCount() > 10) {
                shelf_slide_more.setVisibility(View.VISIBLE);
            }
        }
    }

    public void hideDeleteView() {
        if (mainInteractiveListener != null) {
            mainInteractiveListener.showBottomView();
        }

        if (display == BaseConfig.SHELF_DISPLAY_GRID) {
            if (shelfGridFragment != null) {
                shelfGridFragment.refreshDeleteData(false);
            }
        } else if (display == BaseConfig.SHELF_DISPLAY_SLIDE) {
            if (shelfSlideFragment != null) {
                shelfSlideFragment.refreshDeleteData(false);
            }
        }
        changeDeleteView(false, 0);
    }

    public boolean showBottomView() {
        return shelf_bottom != null && shelf_bottom.getVisibility() == View.VISIBLE;
    }

    public void showShelfPrompt() {
        if (shelf_empty_view == null) {
            shelf_empty_view = shelf_empty.inflate();

            shelf_empty_insert = (ImageView) shelf_empty_view.findViewById(R.id.shelf_empty_insert);

            shelf_empty_insert.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mainInteractiveListener != null) {
                        mainInteractiveListener.showFragment(1);
                    }
                }
            });
        }

        if (shelf_refresh.getVisibility() == View.VISIBLE) {

            if (shelf_refresh.isRefreshing()) {
                shelf_refresh.setRefreshing(false);
            }

            shelf_refresh.setVisibility(View.GONE);
        }

        if (shelf_empty.getVisibility() == View.GONE) {
            shelf_empty.setVisibility(View.VISIBLE);
        }
    }

    public void hideShelfPrompt() {
        if (shelf_empty_view != null) {
            shelf_empty.setVisibility(View.GONE);
        }

        if (shelf_refresh != null && shelf_refresh.getVisibility() == View.GONE) {
            shelf_refresh.setVisibility(View.VISIBLE);
        }
    }

    public void storeContentScrollDistance(int distance) {
        if (shelf_head_divider != null) {
            if (distance < 100) {
                shelf_head_divider.setAlpha(distance / 100.0f);
            } else {
                shelf_head_divider.setAlpha(1);
            }
        }
    }

    public void showShelfSlideMore() {
        if (shelf_slide_more != null) {
            shelf_slide_more.setVisibility(View.VISIBLE);
        }
    }

    public void hideShelfSlideMore() {
        if (shelf_slide_more != null) {
            shelf_slide_more.setVisibility(View.GONE);
        }
    }

    private static class UiHandler extends Handler {

        private WeakReference<BookShelfFragment> bookShelfFragmentReference;

        UiHandler(BookShelfFragment bookShelfFragment) {
            bookShelfFragmentReference = new WeakReference<>(bookShelfFragment);
        }

        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);

            BookShelfFragment bookShelfFragment = bookShelfFragmentReference.get();

            if (bookShelfFragment == null || bookShelfFragment.getActivity().isFinishing()) {
                return;
            }

            switch (message.what) {
                case DELETE_REFRESH:
                    bookShelfFragment.refreshDeletedData();
                    break;
            }
        }
    }
}