package com.quduquxie.base.module.reading.catalog.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.quduquxie.R;
import com.quduquxie.application.component.ApplicationComponent;
import com.quduquxie.base.BaseFragment;
import com.quduquxie.base.bean.Book;
import com.quduquxie.base.bean.Bookmark;
import com.quduquxie.base.database.helper.BookDaoHelper;
import com.quduquxie.base.listener.BookmarkListener;
import com.quduquxie.base.module.reading.catalog.BookmarkContentInterface;
import com.quduquxie.base.module.reading.catalog.adapter.BookmarkAdapter;
import com.quduquxie.base.module.reading.catalog.component.DaggerBookmarkContentComponent;
import com.quduquxie.base.module.reading.catalog.module.BookmarkContentModule;
import com.quduquxie.base.module.reading.catalog.presenter.BookmarkContentPresenter;
import com.quduquxie.base.widget.CustomLoadingPage;
import com.quduquxie.base.widget.dialog.CustomDialogFragment;
import com.quduquxie.base.widget.dialog.CustomDialogListener;
import com.quduquxie.module.read.reading.view.ReadingActivity;
import com.quduquxie.widget.LoadingPage;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 17/7/27.
 * Created by crazylei.
 */

public class BookmarkContentFragment extends BaseFragment<BookmarkContentPresenter> implements BookmarkContentInterface.View, BookmarkListener {

    @BindView(R.id.bookmark_content_root)
    FrameLayout bookmark_content_root;
    @BindView(R.id.bookmark_content_result)
    RecyclerView bookmark_content_result;
    @BindView(R.id.bookmark_content_prompt)
    ViewStub bookmark_content_prompt;

    @Inject
    BookmarkContentPresenter bookmarkContentPresenter;

    private View bookmark_content_prompt_view;

    private Book book;

    private String form;

    private ArrayList<Bookmark> bookmarks = new ArrayList<>();

    private BookmarkAdapter bookmarkAdapter;

    private LinearLayoutManager linearLayoutManager;

    private LoadingPage loadingPage;

    private CustomDialogFragment customDialogFragment;

    private BookDaoHelper bookDaoHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.bookDaoHelper = BookDaoHelper.getInstance(this.getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_fragment_bookmark_content, container, false);
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
        StatService.onPageStart(this.getContext(), BookmarkContentFragment.class.getSimpleName());
    }

    @Override
    public void onPause() {
        super.onPause();
        StatService.onPageEnd(this.getContext(), BookmarkContentFragment.class.getSimpleName());
    }

    @Override
    protected void setFragmentComponent(ApplicationComponent applicationComponent) {
        DaggerBookmarkContentComponent.builder()
                .applicationComponent(applicationComponent)
                .bookmarkContentModule(new BookmarkContentModule(this))
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

        bookmarkAdapter = new BookmarkAdapter(this.getContext(), bookmarks);
        bookmarkAdapter.setBookmarkListener(this);
        bookmarkAdapter.setActivityForm(form);

        linearLayoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);

        bookmark_content_result.setAdapter(bookmarkAdapter);
        bookmark_content_result.setLayoutManager(linearLayoutManager);

        bookmarkContentPresenter.initializeBookmarks(book);
    }

    @Override
    public void recycle() {

    }

    @Override
    public void showLoadingPage() {
        if (loadingPage != null) {
            loadingPage.onSuccess();
        } else {
            loadingPage = new LoadingPage(this.getActivity(), bookmark_content_root);
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
    public void initializeBookmarks(ArrayList<Bookmark> bookmarkList) {
        if (bookmarkList == null || bookmarkList.isEmpty()) {
            changeContentView();
        } else {
            if (bookmarks == null) {
                bookmarks = new ArrayList<>();
            } else {
                bookmarks.clear();
            }

            for (Bookmark bookmark : bookmarkList) {
                bookmarks.add(bookmark);
            }

            bookmarkAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClickedBookmark(Bookmark bookmark) {
        if (bookmark != null && book != null) {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt("sequence", bookmark.sequence);
            bundle.putInt("offset", bookmark.offset);
            bundle.putSerializable("book", book);
            intent.putExtras(bundle);
            intent.setClass(this.getContext(), ReadingActivity.class);

            if (!this.getActivity().isFinishing()) {
                startActivity(intent);
            }
        }
    }

    @Override
    public void onLongClickedBookmark(Bookmark bookmark, int position) {
        showDeleteBookmarkDialog(bookmark, position);
    }

    private void showDeleteBookmarkDialog(final Bookmark bookmark, final int position) {
        if (customDialogFragment == null) {
            customDialogFragment = new CustomDialogFragment();
        }

        customDialogFragment.setPrompt("确认要删除书签？");
        customDialogFragment.setFirstOption("取消");
        customDialogFragment.setSecondOption("确认");
        customDialogFragment.setCustomDialogListener(new CustomDialogListener() {
            @Override
            public void onFirstOptionClicked() {
                hideCustomDialogFragment();
            }

            @Override
            public void onSecondOptionClicked() {
                if (customDialogFragment.getShowsDialog()) {
                    deleteBookMark(bookmark, position);
                    hideCustomDialogFragment();
                }
            }
        });

        if (!this.getActivity().isFinishing()) {
            if (customDialogFragment.isAdded()) {
                customDialogFragment.setShowsDialog(true);
            } else {
                customDialogFragment.show(getFragmentManager(), "CustomDialogFragment");
            }
        }
    }

    private void hideCustomDialogFragment() {
        if (!this.getActivity().isFinishing() && customDialogFragment != null && customDialogFragment.getShowsDialog()) {
            customDialogFragment.dismiss();
        }
    }

    private synchronized void deleteBookMark(Bookmark bookmark, int position) {
        if (bookmark != null && position > -1 && position <= bookmarks.size() - 1) {
            if (bookDaoHelper == null) {
                bookDaoHelper = BookDaoHelper.getInstance(this.getContext());
            }

            if (!TextUtils.isEmpty(bookmark.book_id)) {

                bookDaoHelper.deleteBookmark(bookmark.book_id, bookmark.sequence, bookmark.offset);
                bookmarks.remove(position);

                bookmarkAdapter.notifyDataSetChanged();

                if (bookmarks.size() == 0) {
                    changeContentView();
                }
            } else {
                showPromptMessage("删除书签失败！");
            }
        } else {
            showPromptMessage("删除书签失败！");
        }
    }

    private void changeContentView() {
        if (bookmarks == null || bookmarks.size() == 0) {

            initializePromptView();

            if (bookmark_content_result != null) {
                bookmark_content_result.setVisibility(View.GONE);
            }
        } else {
            if (bookmark_content_result != null) {
                bookmark_content_result.setVisibility(View.VISIBLE);
            }

            if (bookmark_content_prompt != null) {
                bookmark_content_prompt.setVisibility(View.GONE);
            }
        }
    }

    private void initializePromptView() {
        if (bookmark_content_prompt_view == null) {
            bookmark_content_prompt_view = bookmark_content_prompt.inflate();

            TextView communal_prompt_text = (TextView) bookmark_content_prompt_view.findViewById(R.id.communal_prompt_text);
            communal_prompt_text.setText(R.string.prompt_insert_bookmark);

            ImageView communal_prompt_image = (ImageView) bookmark_content_prompt_view.findViewById(R.id.communal_prompt_image);
            communal_prompt_image.setImageResource(R.drawable.icon_bookmark_prompt);
        }
    }
}