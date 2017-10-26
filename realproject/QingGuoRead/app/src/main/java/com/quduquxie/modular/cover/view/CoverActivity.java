package com.quduquxie.modular.cover.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.InflateException;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.quduquxie.R;
import com.quduquxie.application.component.ApplicationComponent;
import com.quduquxie.base.BaseActivity;
import com.quduquxie.base.bean.Book;
import com.quduquxie.base.database.helper.BookDaoHelper;
import com.quduquxie.base.listener.BookListener;
import com.quduquxie.base.manager.ActivityStackManager;
import com.quduquxie.base.module.catalog.view.CatalogActivity;
import com.quduquxie.base.module.main.activity.view.MainActivity;
import com.quduquxie.communal.help.comment.CommentPopupWindowHelp;
import com.quduquxie.communal.help.comment.ReplyPopupWindowHelp;
import com.quduquxie.communal.widget.CustomCommentReplyDialog;
import com.quduquxie.communal.widget.CustomInformationDialog;
import com.quduquxie.communal.widget.CustomInformationListener;
import com.quduquxie.communal.widget.CustomReviewReplyDialog;
import com.quduquxie.communal.widget.CustomReviewWriteDialog;
import com.quduquxie.communal.widget.DividerItemDecoration;
import com.quduquxie.db.CommentLikeDao;
import com.quduquxie.function.comment.list.adapter.CommentAdapter;
import com.quduquxie.model.Comment;
import com.quduquxie.model.CommentItem;
import com.quduquxie.model.CommentReply;
import com.quduquxie.model.CommentUser;
import com.quduquxie.modular.cover.CoverInterface;
import com.quduquxie.modular.cover.component.DaggerCoverComponent;
import com.quduquxie.modular.cover.module.CoverModule;
import com.quduquxie.modular.cover.presenter.CoverPresenter;
import com.quduquxie.module.comment.listener.CommentItemListener;
import com.quduquxie.module.comment.view.CommentDetailsActivity;
import com.quduquxie.module.comment.view.CommentListActivity;
import com.quduquxie.module.read.reading.view.ReadingActivity;
import com.quduquxie.service.download.DownloadService;
import com.quduquxie.service.download.DownloadServiceUtil;
import com.quduquxie.service.download.DownloadState;
import com.quduquxie.service.download.DownloadTask;
import com.quduquxie.util.StatServiceUtils;
import com.quduquxie.widget.LoadingPage;
import com.quduquxie.wxapi.WXEntryActivity;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created on 17/4/13.
 * Created by crazylei.
 */

public class CoverActivity extends BaseActivity<CoverPresenter> implements CoverInterface.View, BookListener, CommentItemListener, CustomReviewWriteDialog.OnReviewWriteSentClickListener, CustomReviewReplyDialog.OnReviewReplySentClickListener, CustomCommentReplyDialog.OnCommentReplySentClickListener {

    @BindView(R.id.cover_head_back)
    public ImageView cover_head_back;
    @BindView(R.id.cover_head_download)
    public LinearLayout cover_head_download;
    @BindView(R.id.cover_head_download_state)
    public TextView cover_head_download_state;
    @BindView(R.id.cover_head_download_image)
    public ImageView cover_head_download_image;

    @BindView(R.id.cover_content)
    public RelativeLayout cover_content;

    @BindView(R.id.cover_refresh)
    public SwipeRefreshLayout cover_refresh;
    @BindView(R.id.cover_result)
    public RecyclerView cover_result;

    @BindView(R.id.cover_bottom_insert)
    public RelativeLayout cover_bottom_insert;
    @BindView(R.id.cover_bottom_read)
    public RelativeLayout cover_bottom_read;

    @Inject
    CoverPresenter coverPresenter;

    private String id;

    private Book book;

    private int page = 1;

    private BookDaoHelper bookDaoHelper;

    private DownloadService downloadService;

    private DownloadReceiver downloadReceiver;

    private CommentLikeDao commentLikeDao;

    private CommentAdapter commentAdapter;
    private LinearLayoutManager linearLayoutManager;

    private ArrayList<CommentItem> commentItems = new ArrayList<>();

    private LoadingPage loadingPage;

    private CustomReviewWriteDialog customReviewWriteDialog;
    private CustomReviewReplyDialog customReviewReplyDialog;
    private CustomCommentReplyDialog customCommentReplyDialog;

    private View commentPopupView;
    private PopupWindow commentPopupWindow;
    private CommentPopupWindowHelp commentPopupWindowHelp;

    private View replyPopupView;
    private PopupWindow replyPopupWindow;
    private ReplyPopupWindowHelp replyPopupWindowHelp;

    private CustomInformationDialog customInformationDialog;

    private boolean register = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_cover_content);
        initializeParameter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(this);

        if (book != null && !TextUtils.isEmpty(book.id)) {
            initAddShelfState(book);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
    }

    @Override
    protected void onDestroy() {

        recycle();

        super.onDestroy();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null && intent.hasExtra("id")) {
            this.id = intent.getStringExtra("id");
            initView();
        }
    }

    @Override
    protected void setActivityComponent(ApplicationComponent applicationComponent) {
        DaggerCoverComponent.builder()
                .applicationComponent(applicationComponent)
                .coverModule(new CoverModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void initializeParameter() {
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent != null) {
            this.id = intent.getStringExtra("id");
        }

        bookDaoHelper = BookDaoHelper.getInstance(this);

        downloadService = applicationUtil.getDownloadService();

        if (downloadService == null) {
            applicationUtil.bindDownloadService();
            downloadService = applicationUtil.getDownloadService();
        }

        initView();
    }

    @Override
    public void recycle() {

        unRegisterDownloadReceiver();

        if (bookDaoHelper != null) {
            bookDaoHelper = null;
        }

        if (commentLikeDao != null) {
            commentLikeDao = null;
        }

        try {
            this.setContentView(R.layout.layout_view_empty);
        } catch (Exception exception) {
            collectException(exception);
        }
    }

    @Override
    public void initView() {

        registerDownloadReceiver();

        setDownloadState(false);

        if (commentItems == null) {
            commentItems = new ArrayList<>();
        } else {
            commentItems.clear();
        }

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        commentAdapter = new CommentAdapter(this, commentItems);
        commentAdapter.setBookListener(this);
        commentAdapter.setCommentItemListener(this);

        if (cover_refresh != null) {
            cover_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    setRefreshViewState(false);
                    page = 1;
                    coverPresenter.loadCoverInformation(id);
                }
            });
        }

        if (cover_result != null) {
            cover_result.setLayoutManager(linearLayoutManager);
            cover_result.setAdapter(commentAdapter);
            cover_result.setItemAnimator(new DefaultItemAnimator());
            cover_result.addItemDecoration(new DividerItemDecoration());
        }

        coverPresenter.loadCoverInformation(id);
    }

    @Override
    public void showLoadingPage() {
        if (loadingPage != null) {
            loadingPage.onSuccess();
        } else {
            loadingPage = new LoadingPage(this, cover_content);
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
    public void initBookInformation(Book book) {
        this.book = book;

        if (commentItems == null) {
            commentItems = new ArrayList<>();
        } else {
            commentItems.clear();
        }

        commentItems.add(createCommentItem(book));
        commentAdapter.notifyDataSetChanged();

        initAddShelfState(book);
        checkBookDownloadState(id);

        coverPresenter.loadCommentList(id, page);
    }

    @Override
    public void setCoverRecommend(ArrayList<Book> books) {
        if (books != null && books.size() > 0) {
            CommentItem commentItem = new CommentItem();
            commentItem.type = CommentAdapter.TYPE_COVER_RECOMMEND;
            commentItem.books = books;

            commentItems.add(commentItem);

            commentItems.add(createCommentItem(CommentAdapter.TYPE_COMMENT_FILL));

            commentAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void setCommentItemList(ArrayList<CommentItem> commentItemList) {
        for (CommentItem commentItem : commentItemList) {
            commentItems.add(commentItem);
        }
        commentAdapter.notifyDataSetChanged();

        coverPresenter.loadCoverRecommend(book.id);
    }

    @Override
    public void setRefreshViewState(boolean state) {
        if (cover_refresh == null) {
            return;
        }
        if (state) {
            cover_refresh.setRefreshing(true);
        } else {
            if (cover_refresh.isRefreshing()) {
                cover_refresh.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        cover_refresh.setRefreshing(false);
                    }
                }, 500);
            }
        }
    }

    @Override
    public void hideReviewWriteDialog() {
        if (customReviewWriteDialog != null && !isFinishing()) {
            customReviewWriteDialog.dismiss();
        }
    }

    @Override
    public void addComment(Comment comment) {
        if (comment != null) {
            CommentItem commentItem = new CommentItem();
            commentItem.type = CommentAdapter.TYPE_COMMENT_NEW;
            commentItem.comment = comment;

            int size = commentItems.size();

            if (size > 0) {
                int countPosition = -1;
                int emptyPosition = -1;
                int hotPosition = -1;
                int newPosition = -1;

                for (int i = 0; i < commentItems.size(); i++) {
                    CommentItem item = commentItems.get(i);

                    if (item.type == CommentAdapter.TYPE_COMMENT_EMPTY) {
                        emptyPosition = i;
                    } else if (item.type == CommentAdapter.TYPE_COMMENT_HOT) {
                        hotPosition = i;
                    } else if (item.type == CommentAdapter.TYPE_COMMENT_NEW) {
                        newPosition = i;
                    } else if (item.type == CommentAdapter.TYPE_COMMENT_NUMBER) {
                        countPosition = i;
                    }
                }

                if (emptyPosition != -1) {
                    commentItems.remove(emptyPosition);
                    commentAdapter.notifyItemRemoved(emptyPosition);
                    commentItems.add(emptyPosition, commentItem);
                    if (countPosition != -1) {
                        commentItems.get(countPosition).count += 1;
                    }
                } else if (newPosition != -1) {
                    commentItems.add(newPosition + 1, commentItem);
                    if (countPosition != -1) {
                        commentItems.get(countPosition).count += 1;
                    }
                } else if (hotPosition != -1) {
                    commentItems.add(hotPosition + 1, createCommentItem(CommentAdapter.TYPE_COMMENT_NEW_FLAG));
                    commentItems.add(hotPosition + 2, commentItem);
                    if (countPosition != -1) {
                        commentItems.get(countPosition).count += 1;
                    }
                }
            }
            commentAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void hideReviewReplyDialog() {
        if (customReviewReplyDialog != null && !isFinishing()) {
            customReviewReplyDialog.dismiss();
        }
    }

    @Override
    public void addCommentReply(Comment comment) {
        if (comment != null && !TextUtils.isEmpty(comment.id) && comment.replies != null && comment.replies.size() > 0) {
            for (CommentItem commentItem : commentItems) {
                if (commentItem.comment != null) {
                    if (commentItem.comment.id.equals(comment.id)) {
                        if (commentItem.comment.replies != null) {
                            commentItem.comment.replies.addAll(comment.replies);
                        } else {
                            commentItem.comment.replies = comment.replies;
                        }
                        break;
                    }
                }
            }
            commentAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void hideCommentReplyDialog() {
        if (customCommentReplyDialog != null && !isFinishing()) {
            customCommentReplyDialog.dismiss();
        }
    }

    @Override
    public void deleteCommentReplyAction(Comment comment, CommentReply commentReply) {
        if (comment != null && !TextUtils.isEmpty(comment.id) && comment.replies != null && comment.replies.size() > 0) {
            for (CommentItem commentItem : commentItems) {
                if (commentItem.comment != null) {
                    if (commentItem.comment.id.equals(comment.id)) {
                        int index = -1;
                        for (int i = 0; i < commentItem.comment.replies.size(); i++) {
                            if (comment.replies.get(i).sn == commentReply.sn) {
                                index = i;
                            }
                        }
                        if (index != -1) {
                            commentItem.comment.replies.remove(index);
                        }
                        break;
                    }
                }
            }
            commentAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void deleteCommentAction(Comment comment, int position) {
        if (position > -1 & position < commentItems.size()) {
            commentItems.remove(position);
            commentAdapter.notifyItemRemoved(position);

            int comment_new_count = 0;
            int comment_hot_count = 0;
            int comment_count = 0;

            int countPosition = -1;

            CommentItem commentItemNew = null;
            CommentItem commentItemHot = null;
            CommentItem commentItemEnd = null;


            for (int i = 0; i < commentItems.size(); i++) {
                CommentItem item = commentItems.get(i);

                if (item.type == CommentAdapter.TYPE_COMMENT_HOT) {
                    comment_count += 1;
                    comment_hot_count += 1;
                } else if (item.type == CommentAdapter.TYPE_COMMENT_NEW) {
                    comment_count += 1;
                    comment_new_count += 1;
                } else if (item.type == CommentAdapter.TYPE_COMMENT_NEW_FLAG) {
                    commentItemNew = commentItems.get(i);
                } else if (item.type == CommentAdapter.TYPE_COMMENT_HOT_FLAG) {
                    commentItemHot = commentItems.get(i);
                } else if (item.type == CommentAdapter.TYPE_COMMENT_END) {
                    commentItemEnd = commentItems.get(i);
                } else if (item.type == CommentAdapter.TYPE_COMMENT_NUMBER) {
                    countPosition = i;
                }
            }

            if (countPosition != -1) {
                commentItems.get(countPosition).count -= 1;
            }

            if (comment_hot_count == 0 && commentItemNew != null) {
                commentItems.remove(commentItemNew);
            }
            if (comment_new_count == 0 && commentItemHot != null) {
                commentItems.remove(commentItemHot);
            }
            if (comment_count == 0 && commentItemEnd != null) {
                commentItems.remove(commentItemEnd);

                CommentItem commentItem = new CommentItem();
                commentItem.type = CommentAdapter.TYPE_COMMENT_EMPTY;
                commentItems.add(commentItem);
            }
            commentAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void commendLike(Comment comment, int position) {
        if (position != -1 && position > -1 && position < commentItems.size()) {
            commentItems.get(position).comment.like_count += 1;
            if (commentLikeDao == null) {
                commentLikeDao = CommentLikeDao.getInstance(this);
            }
            if (!TextUtils.isEmpty(comment.id)) {
                commentLikeDao.insertComment(comment.id);
            }
            commentAdapter.notifyItemChanged(position);
        }
    }

    @Override
    public void commendDislike(Comment comment, int position) {
        if (position != -1 && position > -1 && position < commentItems.size()) {
            commentItems.get(position).comment.like_count -= 1;
            if (commentLikeDao == null) {
                commentLikeDao = CommentLikeDao.getInstance(this);
            }
            if (!TextUtils.isEmpty(comment.id)) {
                commentLikeDao.deleteComment(comment.id);
            }
            commentAdapter.notifyItemChanged(position);
        }
    }

    @Override
    public void commentLikeAction(Comment comment, int position) {
        if (book != null && !TextUtils.isEmpty(book.id) && comment != null && !TextUtils.isEmpty(comment.id)) {
            coverPresenter.commentCommendAction(book.id, comment, true, position);
        }
    }

    @Override
    public void commentDislikeAction(Comment comment, int position) {
        if (book != null && !TextUtils.isEmpty(book.id) && comment != null && !TextUtils.isEmpty(comment.id)) {
            coverPresenter.commentCommendAction(book.id, comment, false, position);
        }
    }

    @Override
    public void showReviewWriteView() {
        if (customReviewWriteDialog == null) {
            customReviewWriteDialog = new CustomReviewWriteDialog();
            customReviewWriteDialog.setOnReviewWriteSentClickListener(this);
        }

        if (customReviewWriteDialog != null && !isFinishing()) {
            if (customReviewWriteDialog.isAdded()) {
                customReviewWriteDialog.setShowsDialog(true);
            } else {
                customReviewWriteDialog.show(getSupportFragmentManager(), "CustomReviewWriteDialog");
            }
        }
    }

    @Override
    public void showReviewReplyView(Comment comment) {
        if (customReviewReplyDialog == null) {
            customReviewReplyDialog = new CustomReviewReplyDialog();
            customReviewReplyDialog.setOnReviewReplySentClickListener(this);
        }

        customReviewReplyDialog.setReplyCommentData(comment);

        if (customReviewReplyDialog != null && !isFinishing()) {
            if (customReviewReplyDialog.isAdded()) {
                customReviewReplyDialog.setShowsDialog(true);
            } else {
                customReviewReplyDialog.show(getSupportFragmentManager(), "customReviewReplyDialog");
            }
        }
    }

    @Override
    public void showCommentReplyView(Comment comment, CommentReply commentReply) {
        if (customCommentReplyDialog == null) {
            customCommentReplyDialog = new CustomCommentReplyDialog();
            customCommentReplyDialog.setOnCommentReplySentClickListener(this);
        }

        customCommentReplyDialog.setReplyUser(commentReply.sender);
        customCommentReplyDialog.setReplyCommentData(comment);

        if (customCommentReplyDialog != null && !isFinishing()) {
            if (customCommentReplyDialog.isAdded()) {
                customCommentReplyDialog.setShowsDialog(true);
            } else {
                customCommentReplyDialog.show(getSupportFragmentManager(), "CustomCommentReplyDialog");
            }
        }
    }

    @Override
    public void clickedReview(Comment comment, View view, int position) {
        if (comment != null && comment.sender != null) {
            initCommentPopupWindow(comment, view, position);
        } else {
            showPromptMessage("参数错误！");
        }
    }

    @Override
    public void clickedCommentReply(Comment comment, CommentReply commentReply, View view, int position) {
        if (comment != null && commentReply != null && commentReply.sender != null) {
            initReplyPopupWindow(comment, commentReply, view, position);
        } else {
            showPromptMessage("参数错误！");
        }
    }

    @Override
    public void reportComment(Comment comment) {
        if (comment != null && !TextUtils.isEmpty(comment.id)) {
            coverPresenter.reportComment(comment);
        } else {
            showPromptMessage("参数错误！");
        }
    }

    @Override
    public void reportCommentReply(Comment comment, CommentReply commentReply) {
        if (comment != null && !TextUtils.isEmpty(comment.id)) {
            coverPresenter.reportCommentReply(comment, commentReply);
        } else {
            showPromptMessage("参数错误！");
        }
    }

    @Override
    public void deleteComment(final Comment comment, final int position) {
        if (customInformationDialog == null) {
            customInformationDialog = new CustomInformationDialog();
        }

        customInformationDialog.setPrompt("是否确定删除评论内容？");
        customInformationDialog.setInformationClickListener(new CustomInformationListener() {
            @Override
            public void onCancelClicked() {
                hideCustomInformationDialog();
            }

            @Override
            public void onConfirmClicked() {
                if (customInformationDialog.getShowsDialog()) {
                    if (!TextUtils.isEmpty(book.id) && comment != null && !TextUtils.isEmpty(comment.id)) {
                        coverPresenter.deleteCommentRequest(book.id, comment, position);
                    } else {
                        showPromptMessage("参数错误！");
                    }
                    customInformationDialog.dismiss();
                }
            }
        });

        if (!isFinishing()) {
            if (customInformationDialog.isAdded()) {
                customInformationDialog.setShowsDialog(true);
            } else {
                customInformationDialog.show(getSupportFragmentManager(), "CustomInformationDialog");
            }

        }
    }

    @Override
    public void deleteCommentReply(final Comment comment, final CommentReply commentReply, int position) {
        if (customInformationDialog == null) {
            customInformationDialog = new CustomInformationDialog();
        }

        customInformationDialog.setPrompt("是否确定删除回复内容？");
        customInformationDialog.setInformationClickListener(new CustomInformationListener() {
            @Override
            public void onCancelClicked() {
                hideCustomInformationDialog();
            }

            @Override
            public void onConfirmClicked() {
                if (customInformationDialog.getShowsDialog()) {
                    if (!TextUtils.isEmpty(book.id) && comment != null && !TextUtils.isEmpty(comment.id) && commentReply != null) {
                        coverPresenter.deleteCommentReplyRequest(book.id, comment, commentReply);
                    } else {
                        showPromptMessage("参数错误！");
                    }
                    customInformationDialog.dismiss();
                }
            }
        });

        if (!isFinishing()) {
            if (customInformationDialog.isAdded()) {
                customInformationDialog.setShowsDialog(true);
            } else {
                customInformationDialog.show(getSupportFragmentManager(), "CustomInformationDialog");
            }

        }
    }

    @Override
    public void startLoginActivity() {
        startActivity(new Intent(this, WXEntryActivity.class));
    }

    @Override
    public void startCatalogActivity() {
        if (book != null) {
            Intent catalogIntent = new Intent(this, CatalogActivity.class);
            catalogIntent.putExtra("book", book);
            startActivity(catalogIntent);
        }
    }

    @Override
    public void startCommentDetailsActivity(String id) {
        if (book != null && !TextUtils.isEmpty(book.id) && !TextUtils.isEmpty(id)) {
            Intent intent = new Intent();
            intent.setClass(CoverActivity.this, CommentDetailsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("id_book", book.id);
            bundle.putString("id_comment", id);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    @Override
    public void startCommentListActivity() {
        Intent intent = new Intent();
        intent.putExtra("book", book);
        intent.putExtra("id_book", book.id);
        intent.setClass(CoverActivity.this, CommentListActivity.class);
        startActivity(intent);
    }

    @Override
    public void hideCommentPopupWindow() {
        if (commentPopupWindow != null && commentPopupWindow.isShowing()) {
            commentPopupWindow.dismiss();
        }
    }

    @Override
    public void hideReplyPopupWindow() {
        if (replyPopupWindow != null && replyPopupWindow.isShowing()) {
            replyPopupWindow.dismiss();
        }
    }

    @OnClick({R.id.cover_head_back, R.id.cover_head_download, R.id.cover_bottom_read, R.id.cover_bottom_insert})
    public void onClickListener(View view) {
        switch (view.getId()) {
            case R.id.cover_head_back:
                if (ActivityStackManager.getActivityStackManager().getActivities() == 1) {
                    Intent intent = new Intent();
                    intent.setClass(this, MainActivity.class);
                    startActivity(intent);
                }
                finish();
                break;
            case R.id.cover_head_download:
                startDownloadBook();
                break;
            case R.id.cover_bottom_read:
                if (book != null && !TextUtils.isEmpty(book.id)) {
                    if (bookDaoHelper == null) {
                        bookDaoHelper = BookDaoHelper.getInstance(this);
                    }

                    StatServiceUtils.statBookDetailsRead(CoverActivity.this);

                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();

                    if (bookDaoHelper.subscribeBook(book.id)) {
                        Book local_book = bookDaoHelper.loadBook(id, Book.TYPE_ONLINE);
                        bundle.putInt("sequence", local_book.sequence);
                        bundle.putInt("offset", local_book.offset);
                        bundle.putSerializable("book", local_book);
                    } else {
                        bundle.putInt("sequence", -1);
                        bundle.putInt("offset", 0);
                        bundle.putSerializable("book", book);
                    }

                    intent.setClass(this, ReadingActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                break;
            case R.id.cover_bottom_insert:
                if (bookDaoHelper == null) {
                    bookDaoHelper = BookDaoHelper.getInstance(this);
                }
                if (bookDaoHelper.subscribeBook(id)) {
                    showPromptMessage("已加入书架！");
                } else {
                    if (this.book == null) {
                        return;
                    }
                    int result = bookDaoHelper.insertBook(book);
                    if (result == 1) {
                        StatServiceUtils.statBookDetailsAddBook(CoverActivity.this);
                        cover_bottom_insert.setSelected(true);
                        showPromptMessage("成功添加到书架！");
                    }
                }
                break;
        }
    }

    private void initCommentPopupWindow(Comment comment, View view, int position) {
        if (commentPopupWindow == null) {
            try {
                commentPopupView = View.inflate(this, R.layout.layout_view_comment_option, null);
            } catch (InflateException exception) {
                collectException(exception);
                exception.printStackTrace();
                return;
            }

            if (commentPopupView != null) {
                commentPopupView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

                commentPopupWindow = new PopupWindow(commentPopupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                commentPopupWindow.setBackgroundDrawable(new BitmapDrawable());
                commentPopupWindow.setOutsideTouchable(true);

                commentPopupWindowHelp = new CommentPopupWindowHelp(this, commentPopupView, this);
            }
        }

        if (view != null && commentPopupView != null && commentPopupWindowHelp != null) {

            commentPopupWindowHelp.initPopupWindowView(comment, position);

            int popupWidth = commentPopupView.getMeasuredWidth();
            int popupHeight = commentPopupView.getMeasuredHeight();

            int[] location = new int[2];
            view.getLocationOnScreen(location);

            commentPopupWindow.showAtLocation(view, Gravity.NO_GRAVITY, (location[0] + view.getWidth() / 2) - popupWidth / 2, (location[1] - popupHeight) + view.getHeight() / 2);
        }
    }

    private void initReplyPopupWindow(Comment comment, CommentReply commentReply, View view, int position) {
        if (replyPopupWindow == null) {
            try {
                replyPopupView = View.inflate(this, R.layout.layout_view_comment_reply_option, null);
            } catch (InflateException exception) {
                collectException(exception);
                exception.printStackTrace();
                return;
            }

            if (replyPopupView != null) {
                replyPopupView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

                replyPopupWindow = new PopupWindow(replyPopupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                replyPopupWindow.setBackgroundDrawable(new BitmapDrawable());
                replyPopupWindow.setOutsideTouchable(true);

                replyPopupWindowHelp = new ReplyPopupWindowHelp(this, replyPopupView, this);
            }
        }

        if (view != null && replyPopupView != null && replyPopupWindowHelp != null) {

            replyPopupWindowHelp.initPopupWindowView(comment, commentReply, position);

            int popupWidth = replyPopupView.getMeasuredWidth();
            int popupHeight = replyPopupView.getMeasuredHeight();

            int[] location = new int[2];
            view.getLocationOnScreen(location);

            replyPopupWindow.showAtLocation(view, Gravity.NO_GRAVITY, (location[0] + view.getWidth() / 2) - popupWidth / 2, (location[1] - popupHeight) + view.getHeight() / 2);
        }
    }

    @Override
    public void onReviewWriteSentClick(String content) {
        if (book != null && !TextUtils.isEmpty(book.id) && book.author != null && !TextUtils.isEmpty(book.author.id)) {
            if (TextUtils.isEmpty(content)) {
                showPromptMessage("书评内容为空，请填写评论后重试！");
            } else {
                coverPresenter.publishBookComment(book.id, book.author.id, content);
            }
        } else {
            showPromptMessage("参数错误，请返回重试！");
        }
    }

    @Override
    public void onReviewReplyClicked(Comment comment, String content) {
        if (comment != null && !TextUtils.isEmpty(comment.id) && comment.sender != null && !TextUtils.isEmpty(comment.sender.id) && !TextUtils.isEmpty(book.id)) {
            if (TextUtils.isEmpty(content)) {
                showPromptMessage("评论内容为空，请填写评论后重试！");
            } else {
                coverPresenter.publishReviewReply(comment, book.id, content);
            }
        } else {
            showPromptMessage("参数错误，请返回重试！");
        }
    }

    @Override
    public void onCommentReplySentClicked(Comment comment, CommentUser commentUser, String content) {
        if (comment != null && !TextUtils.isEmpty(comment.id) && commentUser != null && !TextUtils.isEmpty(commentUser.id) && !TextUtils.isEmpty(book.id)) {
            if (TextUtils.isEmpty(content)) {
                showPromptMessage("评论内容为空，请填写评论后重试！");
            } else {
                coverPresenter.publishCommentReply(comment, commentUser, book.id, content);
            }
        } else {
            showPromptMessage("参数错误，请返回重试！");
        }
    }

    public void hideCustomInformationDialog() {
        if (!isFinishing() && customInformationDialog != null && customInformationDialog.getShowsDialog()) {
            customInformationDialog.dismiss();
        }
    }

    private CommentItem createCommentItem(Book book) {
        CommentItem commentItem = new CommentItem();
        commentItem.type = CommentAdapter.TYPE_COVER_DETAILS;
        commentItem.book = book;
        return commentItem;
    }

    private CommentItem createCommentItem(int type) {
        CommentItem commentItem = new CommentItem();
        commentItem.type = type;
        return commentItem;
    }

    private void initAddShelfState(Book book) {
        if (bookDaoHelper == null) {
            bookDaoHelper = BookDaoHelper.getInstance(this);
        }
        if (bookDaoHelper.subscribeBook(book.id)) {
            cover_bottom_insert.setSelected(true);
        } else {
            cover_bottom_insert.setSelected(false);
        }
    }

    private void initDownloadState(DownloadState downloadState) {
        if (downloadState == DownloadState.NO_START || downloadState == DownloadState.PAUSED || downloadState == DownloadState.REFRESH) {
            if (cover_head_download_state != null) {
                if (downloadState == DownloadState.NO_START) {
                    cover_head_download_state.setText("下载");
                } else if (downloadState == DownloadState.PAUSED) {
                    cover_head_download_state.setText("已暂停");
                } else {
                    cover_head_download_state.setText("下载失败");
                }
            }
            setDownloadState(true);
        } else {
            if (downloadState == DownloadState.FINISH) {
                cover_head_download_state.setText("下载完成");
            } else if (downloadState == DownloadState.DOWNLOADING) {
                cover_head_download_state.setText("下载中");
            } else if (downloadState == DownloadState.WAITING) {
                cover_head_download_state.setText("排队中");
            }
            setDownloadState(false);
        }

    }

    private void setDownloadState(boolean state) {
        if (cover_head_download != null) {
            cover_head_download.setEnabled(state);
        }

        if (cover_head_download_state != null) {
            cover_head_download_state.setEnabled(state);
        }

        if (cover_head_download_image != null) {
            cover_head_download_image.setEnabled(state);
        }
    }

    private void checkBookDownloadState(String id) {
        if (!TextUtils.isEmpty(id)) {
            if (bookDaoHelper == null) {
                bookDaoHelper = BookDaoHelper.getInstance(this);
            }

            if (bookDaoHelper.subscribeBook(id)) {
                Book book = bookDaoHelper.loadBook(id, Book.TYPE_ONLINE);
                if (book != null) {
                    DownloadServiceUtil.addDownloadBookTask(CoverActivity.this, book, true);
                    if (downloadService != null) {
                        DownloadTask downloadTask = downloadService.getDownloadTask(id);
                        if (downloadTask != null) {
                            initDownloadState(downloadTask.downloadState);
                        }
                    }
                }
            } else {
                initDownloadState(DownloadState.NO_START);
            }
        }
    }

    @Override
    public void onClickedBook(Book book) {
        if (book != null && !TextUtils.isEmpty(book.id)) {
            Intent intent = new Intent();
            intent.setClass(CoverActivity.this, CoverActivity.class);
            intent.putExtra("id", book.id);
            startActivity(intent);
        }
    }

    public class DownloadReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(DownloadService.DOWNLOAD_SERVICE_START)) {
                String id = intent.getStringExtra("id");
                if (!TextUtils.isEmpty(id)) {
                    onDownloadStart(id);
                }
            }
            if (intent.getAction().equals(DownloadService.DOWNLOAD_SERVICE_FINISH)) {
                String id = intent.getStringExtra("id");
                if (!TextUtils.isEmpty(id)) {
                    onDownloadFinish(id);
                }
            } else if (intent.getAction().equals(DownloadService.DOWNLOAD_SERVICE_FAILED)) {
                int sequence = intent.getIntExtra("sequence", 0);
                String id = intent.getStringExtra("id");
                if (!TextUtils.isEmpty(id) && sequence != 0) {
                    onDownloadFiled(id);
                }
            }
        }
    }

    private void registerDownloadReceiver() {
        if (downloadReceiver == null && !register) {
            downloadReceiver = new DownloadReceiver();

            IntentFilter filter = new IntentFilter();
            filter.addAction(DownloadService.DOWNLOAD_SERVICE_START);
            filter.addAction(DownloadService.DOWNLOAD_SERVICE_FINISH);
            filter.addAction(DownloadService.DOWNLOAD_SERVICE_FAILED);

            registerReceiver(downloadReceiver, filter);

            register = true;
        }
    }


    private void unRegisterDownloadReceiver() {
        if (downloadReceiver != null && register) {
            try {
                unregisterReceiver(downloadReceiver);
                register = false;
            } catch (IllegalArgumentException exception) {
                exception.printStackTrace();
                collectException(exception);
            }
        }
    }

    private void onDownloadStart(String id) {
        if (!TextUtils.isEmpty(id) && book != null && id.equals(book.id)) {
            DownloadTask downloadTask = downloadService.getDownloadTask(id);
            if (null != downloadTask && downloadTask.downloadState == DownloadState.DOWNLOADING) {
                initDownloadState(DownloadState.DOWNLOADING);
            }
        }
    }

    private void onDownloadFinish(String id) {
        if (!TextUtils.isEmpty(id) && book != null && id.equals(book.id)) {
            DownloadTask downloadTask = downloadService.getDownloadTask(id);
            if (null != downloadTask && downloadTask.downloadState == DownloadState.FINISH) {
                initDownloadState(DownloadState.FINISH);
                showPromptMessage("《" + book.name + "》缓存完成");
            }
        }
    }

    private void onDownloadFiled(String id) {
        if (!TextUtils.isEmpty(id) && book != null && id.equals(book.id)) {
            DownloadTask downloadTask = downloadService.getDownloadTask(id);
            if (null != downloadTask && downloadTask.downloadState == DownloadState.REFRESH) {
                initDownloadState(DownloadState.REFRESH);
                showPromptMessage("《" + book.name + "》缓存失败");
            }
        }
    }

    private void startDownloadBook() {

        if (book == null || TextUtils.isEmpty(book.id)) {
            return;
        }

        if (downloadService == null) {
            applicationUtil.bindDownloadService();
            downloadService = applicationUtil.getDownloadService();
        }

        if (bookDaoHelper == null) {
            bookDaoHelper = BookDaoHelper.getInstance(this);
        }

        if (!bookDaoHelper.subscribeBook(book.id)) {
            bookDaoHelper.insertBook(book);
            initAddShelfState(book);
        }

        int downIndex = DownloadServiceUtil.downloadStartIndex(this, book.id);

        if (downloadService != null) {
            if (downloadService.getDownloadTask(book.id) == null) {
                DownloadServiceUtil.addDownloadBookTask(this, book, true);
            }
            DownloadServiceUtil.startDownloadBookTask(downloadService, this, book, downIndex);
            DownloadServiceUtil.writeDownloadIndex(this, book.id, downIndex);
        } else {
            showPromptMessage("缓存服务启动失败，请退出重试！");
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (ActivityStackManager.getActivityStackManager().getActivities() <= 1) {
            Intent intent = new Intent();
            intent.setClass(this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}