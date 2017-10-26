package com.quduquxie.function.download.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.orhanobut.logger.Logger;
import com.quduquxie.R;
import com.quduquxie.application.component.ApplicationComponent;
import com.quduquxie.base.bean.Book;
import com.quduquxie.base.database.helper.BookDaoHelper;
import com.quduquxie.base.util.NetworkUtil;
import com.quduquxie.function.BaseActivity;
import com.quduquxie.function.download.DownloadManagerInterface;
import com.quduquxie.function.download.adapter.DownloadManagerAdapter;
import com.quduquxie.function.download.component.DaggerDownloadManagerComponent;
import com.quduquxie.function.download.listener.DownloadListener;
import com.quduquxie.function.download.module.DownloadManagerModule;
import com.quduquxie.function.download.presenter.DownloadManagerPresenter;
import com.quduquxie.service.download.DownloadService;
import com.quduquxie.service.download.DownloadServiceUtil;
import com.quduquxie.service.download.DownloadState;
import com.quduquxie.service.download.DownloadTask;
import com.quduquxie.widget.LoadingPage;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created on 17/4/10.
 * Created by crazylei.
 */

public class DownloadManagerActivity extends BaseActivity implements DownloadManagerInterface.View, DownloadListener {

    @BindView(R.id.common_head_back)
    public ImageView common_head_back;
    @BindView(R.id.common_head_title)
    public TextView common_head_title;
    @BindView(R.id.download_manager_content)
    public RelativeLayout download_manager_content;
    @BindView(R.id.download_manager_result)
    public RecyclerView download_manager_result;

    @BindView(R.id.empty_default)
    public View empty_default;
    @BindView(R.id.empty_default_prompt)
    public TextView empty_default_prompt;
    @BindView(R.id.empty_default_image)
    public ImageView empty_default_image;

    @Inject
    DownloadManagerPresenter downloadManagerPresenter;

    private BookDaoHelper bookDaoHelper;

    private DownloadService downloadService;

    private ArrayList<Book> books = new ArrayList<>();

    private DownloadManagerAdapter downloadManagerAdapter;

    private long download_start_time = System.currentTimeMillis();

    private Toast toast;

    private LoadingPage loadingPage;

    private DownloadReceiver downloadReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.layout_activity_download_manager);

            initParameter();
        } catch (Resources.NotFoundException exception) {
            collectException(exception);
            exception.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(this);

        initData();

        downloadManagerPresenter.initParameter();
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegisterDownloadReceiver();
    }

    @Override
    protected void setActivityComponent(ApplicationComponent applicationComponent) {
        DaggerDownloadManagerComponent.builder()
                .applicationComponent(applicationComponent)
                .downloadManagerModule(new DownloadManagerModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(DownloadManagerInterface.Presenter downloadManagerPresenter) {
        this.downloadManagerPresenter = (DownloadManagerPresenter) downloadManagerPresenter;
    }

    @Override
    public void initParameter() {
        ButterKnife.bind(this);

        if (common_head_title != null) {
            common_head_title.setText("下载管理");
            common_head_title.setTypeface(typeface_song_depict);
        }

        registerDownloadReceiver();
    }

    private void initData() {
        bookDaoHelper = BookDaoHelper.getInstance(this);

        downloadService = applicationUtil.getDownloadService();

        if (downloadService == null) {
            applicationUtil.bindDownloadService();
            downloadService = applicationUtil.getDownloadService();
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        downloadManagerAdapter = new DownloadManagerAdapter(this, books);

        download_manager_result.setLayoutManager(linearLayoutManager);
        download_manager_result.setAdapter(downloadManagerAdapter);

        if (downloadManagerAdapter != null) {
            downloadManagerAdapter.setDownloadListener(this);
            downloadManagerAdapter.setDownloadService(downloadService);
        }
    }

    @Override
    public void showLoadingPage() {
        if (loadingPage != null) {
            loadingPage.onSuccess();
        }

        if (loadingPage == null) {
            loadingPage = new LoadingPage(this, download_manager_content);
        }
    }

    @Override
    public void hideLoadingPage() {
        if (loadingPage != null) {
            loadingPage.onSuccess();
        }
    }


    @Override
    public void setDownloadResource(ArrayList<Book> bookList) {
        if (bookList == null || bookList.size() == 0) {
            showEmptyDefaultView();
        } else {
            showContentView();

            showLoadingPage();

            if (books == null) {
                books = new ArrayList<>();
            } else {
                books.clear();
            }

            for (Book book : bookList) {
                books.add(book);
            }

            if (bookDaoHelper == null) {
                bookDaoHelper = BookDaoHelper.getInstance(this);
            }

            int count = books.size();

            for (int i = 0; i < count; i++) {
                Book book = books.get(i);
                if (book != null) {
                    if (downloadService.getDownloadTask(book.id) == null) {
                        DownloadServiceUtil.addDownloadBookTask(DownloadManagerActivity.this, book, true);
                    }
                }
            }

            Book book = new Book();
            book.item_type = DownloadManagerAdapter.TYPE_EMPTY_FULL_20;
            books.add(book);

            if (downloadManagerAdapter == null) {
                downloadManagerAdapter = new DownloadManagerAdapter(this, books);
                if (download_manager_result != null) {
                    download_manager_result.setAdapter(downloadManagerAdapter);
                }
            } else {
                downloadManagerAdapter.notifyDataSetChanged();
            }

            hideLoadingPage();
        }
    }

    @Override
    public void showToast(String message) {
        if (TextUtils.isEmpty(message)) {
            return;
        }

        if (toast == null) {
            toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        } else {
            toast.setText(message);
        }

        if (!isFinishing() && toast != null) {
            toast.show();
        }
    }

    @Override
    public void downloadBook(Book book, boolean fromStartIndex) {
        if (bookDaoHelper == null) {
            bookDaoHelper = BookDaoHelper.getInstance(this);
        }

        if (book == null || TextUtils.isEmpty(book.id)) {
            return;
        }

        int downIndex = DownloadServiceUtil.downloadStartIndex(this, book.id);

        if (downloadService != null) {
            if (downloadService.getDownloadTask(book.id) == null) {
                DownloadServiceUtil.addDownloadBookTask(this, book, true);
            }
            DownloadServiceUtil.startDownloadBookTask(downloadService, this, book, fromStartIndex ? downIndex : 0);
            DownloadServiceUtil.writeDownloadIndex(this, book.id, downIndex);
        } else {
            showToast("缓存服务启动失败，请退出重试！");
        }
    }

    @Override
    public void downloadCancelTask(Book book) {
        if (book == null || TextUtils.isEmpty(book.id)) {
            return;
        }

        if (downloadService != null) {
            downloadService.cancelDownloadTask(book.id);
        }
    }

    @Override
    public void startDownBookTask(Book book) {
        if (NetworkUtil.loadNetworkType(this) == NetworkUtil.NETWORK_NONE) {
            showToast("网络不给力，请检查网络连接！");
            return;
        }

        if (book == null || TextUtils.isEmpty(book.id)) {
            return;
        }

        if (downloadService != null) {
            downloadService.startDownloadTask(book.id);
        } else {
            showToast("启动缓存服务失败！");
        }
    }

    @OnClick({R.id.common_head_back})
    public void onClickListener(View view) {
        switch (view.getId()) {
            case R.id.common_head_back:
                finish();
                break;
        }
    }

    private void showEmptyDefaultView() {
        if (download_manager_result != null) {
            download_manager_result.setVisibility(View.GONE);
        }

        if (empty_default != null) {
            empty_default.setVisibility(View.VISIBLE);

            if (empty_default_prompt != null) {
                empty_default_prompt.setText("添加到书架的书才可以下载哦～\n" + "赶紧去填充你的书架吧。");
            }

            if (empty_default_image != null) {
                empty_default_image.setImageResource(R.drawable.icon_download_empty);
            }
        }
    }

    private void showContentView() {
        if (empty_default != null) {
            empty_default.setVisibility(View.GONE);
        }

        if (download_manager_result != null) {
            download_manager_result.setVisibility(View.VISIBLE);
        }
    }

    public class DownloadReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(DownloadService.DOWNLOAD_SERVICE_START)) {
                String id = intent.getStringExtra("id");
                if (!TextUtils.isEmpty(id)) {
                    downloadTaskStart(id);
                }
            } else if (intent.getAction().equals(DownloadService.DOWNLOAD_SERVICE_REFRESH)) {
                int sequence = intent.getIntExtra("sequence", 0);
                String id = intent.getStringExtra("id");
                if (!TextUtils.isEmpty(id) && sequence != 0) {
                    downloadTaskRefresh(id, sequence);
                }
            } else if (intent.getAction().equals(DownloadService.DOWNLOAD_SERVICE_FINISH)) {
                String id = intent.getStringExtra("id");
                if (!TextUtils.isEmpty(id)) {
                    downloadTaskFinish(id);
                }
            } else if (intent.getAction().equals(DownloadService.DOWNLOAD_SERVICE_FAILED)) {
                int sequence = intent.getIntExtra("sequence", 0);
                String id = intent.getStringExtra("id");
                if (!TextUtils.isEmpty(id) && sequence != 0) {
                    downloadTaskFailed(id, sequence);
                }
            } else if (intent.getAction().equals(DownloadService.DOWNLOAD_SERVICE_REFRESH_UI)) {
                downloadIndexRefresh();
            } else if (intent.getAction().equals(DownloadService.DOWNLOAD_SERVICE_INDEX_REFRESH)) {
                downloadIndexRefresh();
            }
        }
    }

    private void registerDownloadReceiver() {
        downloadReceiver = new DownloadReceiver();

        IntentFilter filter = new IntentFilter();
        filter.addAction(DownloadService.DOWNLOAD_SERVICE_START);
        filter.addAction(DownloadService.DOWNLOAD_SERVICE_REFRESH);
        filter.addAction(DownloadService.DOWNLOAD_SERVICE_FINISH);
        filter.addAction(DownloadService.DOWNLOAD_SERVICE_FAILED);
        filter.addAction(DownloadService.DOWNLOAD_SERVICE_REFRESH_UI);
        filter.addAction(DownloadService.DOWNLOAD_SERVICE_INDEX_REFRESH);

        registerReceiver(downloadReceiver, filter);
    }


    private void unRegisterDownloadReceiver() {
        if (downloadReceiver != null) {
            try {
                unregisterReceiver(downloadReceiver);
            } catch (IllegalArgumentException exception) {
                exception.printStackTrace();
            }
        }
    }

    private void downloadTaskStart(String id) {
        Logger.d("DownloadTaskStart: " + id);

        if (bookDaoHelper == null) {
            bookDaoHelper = BookDaoHelper.getInstance(this);
        }

        Book book = bookDaoHelper.loadBook(id, Book.TYPE_ONLINE);
        if (downloadManagerAdapter != null) {
            downloadManagerAdapter.notifyItemChanged(book);
        } else {
            downloadManagerAdapter = new DownloadManagerAdapter(this, books);
            download_manager_result.setAdapter(downloadManagerAdapter);
        }
    }

    private void downloadTaskRefresh(String id, int sequence) {
        Logger.d("DownloadTaskRefresh: " + id + " : " + sequence);

        if (bookDaoHelper == null) {
            bookDaoHelper = BookDaoHelper.getInstance(this);
        }

        if (System.currentTimeMillis() - download_start_time > 1000) {
            download_start_time = System.currentTimeMillis();

            if (bookDaoHelper == null) {
                bookDaoHelper = BookDaoHelper.getInstance(this);
            }

            Book book = bookDaoHelper.loadBook(id, Book.TYPE_ONLINE);

            if (downloadManagerAdapter != null) {
                downloadManagerAdapter.notifyItemChanged(book);
            } else {
                downloadManagerAdapter = new DownloadManagerAdapter(this, books);
                download_manager_result.setAdapter(downloadManagerAdapter);
            }
        }
    }

    private void downloadTaskFinish(String id) {
        Logger.e("DownloadTaskFinish: " + id);

        if (!TextUtils.isEmpty(id)) {

            if (bookDaoHelper == null) {
                bookDaoHelper = BookDaoHelper.getInstance(this);
            }

            Book book = bookDaoHelper.loadBook(id, Book.TYPE_ONLINE);

            if (downloadService == null) {
                downloadService = applicationUtil.getDownloadService();
            }

            DownloadTask downloadTask = downloadService.getDownloadTask(id);

            if (null != downloadTask && downloadTask.downloadState == DownloadState.FINISH) {
                showToast("《" + book.name + "》缓存完成");
                if (downloadManagerAdapter != null) {
                    downloadManagerAdapter.notifyItemChanged(book);
                } else {
                    downloadManagerAdapter = new DownloadManagerAdapter(this, books);
                    download_manager_result.setAdapter(downloadManagerAdapter);
                }
            }
        }
    }

    private void downloadTaskFailed(String id, int sequence) {
        Logger.e("DownloadTaskFailed: " + id + " : " + sequence);

        if (bookDaoHelper == null) {
            bookDaoHelper = BookDaoHelper.getInstance(this);
        }

        Book book = bookDaoHelper.loadBook(id, Book.TYPE_ONLINE);
        if (book != null && !TextUtils.isEmpty(book.name)) {
            showToast("《" + book.name + "》缓存失败");
        }
        if (downloadManagerAdapter != null) {
            downloadManagerAdapter.notifyItemChanged(book);
        } else {
            downloadManagerAdapter = new DownloadManagerAdapter(this, books);
            download_manager_result.setAdapter(downloadManagerAdapter);
        }
    }

    private void downloadIndexRefresh() {
        Logger.d("DownloadIndexRefresh");
        if (downloadManagerAdapter != null) {
            downloadManagerAdapter.notifyDataSetChanged();
        } else {
            downloadManagerAdapter = new DownloadManagerAdapter(this, books);
            download_manager_result.setAdapter(downloadManagerAdapter);
        }
    }
}
