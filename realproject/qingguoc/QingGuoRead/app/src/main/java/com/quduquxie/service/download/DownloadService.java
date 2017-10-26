package com.quduquxie.service.download;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.quduquxie.R;
import com.quduquxie.base.bean.Book;
import com.quduquxie.base.bean.Chapter;
import com.quduquxie.base.config.BaseConfig;
import com.quduquxie.base.database.dao.ChapterDao;
import com.quduquxie.base.database.helper.BookDaoHelper;
import com.quduquxie.communal.utils.FileUtil;
import com.quduquxie.retrofit.DataRequestService;
import com.quduquxie.retrofit.ServiceGenerator;
import com.quduquxie.retrofit.model.CommunalResult;
import com.quduquxie.util.BlockingLinkedHashMap;

import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import javax.inject.Singleton;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created on 17/4/19.
 * Created by crazylei.
 */

@Singleton
public class DownloadService extends Service {

    private DownloadBinder downloadBinder;

    private BookDaoHelper bookDaoHelper;

    private ExecutorService executorService;

    private DownloadTask currentDownloadTask;

    private NotificationManager notificationManager;

    private BlockingLinkedHashMap<String, DownloadTask> downloadTaskQueue;

    private static final int DOWN_SIZE = 10;
    private static final int MAX_SIZE = 1000;

    private int download_number = 0;
    private int download_progress = 0;

    private int download_position = 0;

    private Handler handler = new Handler();


    private long notification_time = 0;

    private long refresh_progress = 0;

    private static final int NOTIFICATION_FLAG = "青果阅读小说离线下载".hashCode();

    public static final String DOWNLOAD_SERVICE_START = "com.quduquxie.service.download.action_start";
    public static final String DOWNLOAD_SERVICE_FINISH = "com.quduquxie.service.download.action_finish";
    public static final String DOWNLOAD_SERVICE_FAILED = "com.quduquxie.service.download.action_failed";
    public static final String DOWNLOAD_SERVICE_REFRESH = "com.quduquxie.service.download.action_refresh";
    public static final String DOWNLOAD_SERVICE_REFRESH_UI = "com.quduquxie.service.download.action_refresh_ui";
    public static final String DOWNLOAD_SERVICE_INDEX_REFRESH = "com.quduquxie.service.download.action_index_refresh";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return downloadBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        downloadBinder = new DownloadBinder();
        if (bookDaoHelper == null) {
            bookDaoHelper = BookDaoHelper.getInstance(this);
        }
        if (downloadTaskQueue == null) {
            downloadTaskQueue = new BlockingLinkedHashMap<>(MAX_SIZE);
        }
        if (executorService == null) {
            executorService = Executors.newSingleThreadExecutor(new DownloadThreadFactory());
        }
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    public void onDestroy() {

        if (executorService != null) {
            executorService.shutdown();
            executorService = null;
        }

        super.onDestroy();
    }

    public boolean containsDownloadTask(String id) {
        return downloadTaskQueue.containsKey(id);
    }

    public void addDownloadTask(DownloadTask downloadTask) {
        if (downloadTask != null && !containsDownloadTask(downloadTask.getBookId())) {
            downloadTaskQueue.put(downloadTask.getBookId(), downloadTask);
        }
    }

    public DownloadTask getDownloadTask(String id) {
        return downloadTaskQueue.get(id);
    }

    public void startDownloadTask(String id) {
        DownloadTask downloadTask = downloadTaskQueue.get(id);
        if (downloadTask != null) {
            if (downloadTask.downloadState == DownloadState.REFRESH) {
                downloadTask.start = DownloadServiceUtil.downloadStartIndex(this, id);
            }

            downloadTask.downloadState = DownloadState.WAITING;
            updateDownloadTask();
        }
    }

    public void startDownloadTask(String id, int index) {
        DownloadTask downloadTask = downloadTaskQueue.get(id);
        if (downloadTask != null) {
            downloadTask.downloadState = DownloadState.WAITING;
            downloadTask.start = Math.max(0, index);
        }
        updateDownloadTask();
    }

    public void cancelDownloadTask(String id_book) {
        DownloadTask downloadTask = downloadTaskQueue.get(id_book);
        if (downloadTask != null && downloadTask.downloadState != DownloadState.FINISH) {
            downloadTask.downloadState = DownloadState.PAUSED;

            if (currentDownloadTask != null && currentDownloadTask == downloadTask) {
                currentDownloadTask.unSubscribe();

                currentDownloadTask = null;
            }

            DownloadServiceUtil.writeDownloadIndex(this, downloadTask.book.id, downloadTask.start);
            DownloadServiceUtil.downloadServiceRefreshUI(this, downloadTask.getBookId());
        }
        updateDownloadTask();
    }

    public ArrayList<DownloadTask> cancelAllDownloadTask() {
        if (downloadTaskQueue != null) {
            Collection<Map.Entry<String, DownloadTask>> list = downloadTaskQueue.getAll();
            ArrayList<DownloadTask> downloadTasks = new ArrayList<>();

            for (Map.Entry<String, DownloadTask> entry : list) {
                DownloadTask downloadTask = entry.getValue();
                if (downloadTask != null && (downloadTask.downloadState == DownloadState.WAITING || downloadTask.downloadState == DownloadState.DOWNLOADING)) {
                    downloadTask.downloadState = DownloadState.PAUSED;
                    downloadTasks.add(downloadTask);
                }
            }
            notificationManager.cancel(NOTIFICATION_FLAG);
            return downloadTasks;
        }
        return null;
    }

    public void deleteDownloadTask(String book_id) {
        if (downloadTaskQueue != null && downloadTaskQueue.containsKey(book_id)) {
            downloadTaskQueue.remove(book_id);
            if (null != currentDownloadTask && currentDownloadTask.book != null && book_id.equals(currentDownloadTask.book.id)) {
                currentDownloadTask.downloadState = DownloadState.PAUSED;
                if (notificationManager != null) {
                    notificationManager.cancel(NOTIFICATION_FLAG);
                }
                updateDownloadTask();
            }
        }
    }

    private void updateDownloadTask() {
        if ((currentDownloadTask == null) && !downloadTaskQueue.isEmpty()) {
            Collection<Map.Entry<String, DownloadTask>> list = downloadTaskQueue.getAll();
            for (Map.Entry<String, DownloadTask> entry : list) {
                DownloadTask downloadTask = entry.getValue();
                if (downloadTask != null && downloadTask.downloadState == DownloadState.WAITING) {
                    currentDownloadTask = downloadTask;
                    startCurrentDownloadTask();
                    break;
                }
            }
        }
    }

    private void startCurrentDownloadTask() {
        if (currentDownloadTask != null) {
            if (executorService == null) {
                executorService = Executors.newSingleThreadExecutor(new DownloadThreadFactory());
            }
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    downloadBook();
                }
            });
        } else {
            updateDownloadTask();
        }
    }


    private void downloadBook() {
        if (currentDownloadTask == null) {
            updateDownloadTask();
            return;
        }

        final DownloadTask downloadTask = currentDownloadTask;

        download_number = 0;
        download_progress = 0;
        download_position = 0;

        downloadTaskStart(downloadTask);

        ChapterDao chapterDao = new ChapterDao(this, downloadTask.getBookId());
        ArrayList<Chapter> chapterList = chapterDao.loadChapters();

        if (chapterList == null || chapterList.size() == 0) {
            DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, false);
            dataRequestService.loadBookCatalog(downloadTask.getBookId(), 1)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(Schedulers.io())
                    .doOnNext(new Consumer<CommunalResult<ArrayList<Chapter>>>() {
                        @Override
                        public void accept(@io.reactivex.annotations.NonNull CommunalResult<ArrayList<Chapter>> chapterListResult) throws Exception {
                            if (chapterListResult != null && chapterListResult.getCode() == 0 && chapterListResult.getModel() != null && chapterListResult.getModel().size() > 0) {
                                ArrayList<Chapter> chapters = chapterListResult.getModel();

                                ChapterDao chapterDao = new ChapterDao(DownloadService.this, downloadTask.getBookId());

                                chapterDao.insertBookChapters(chapters);

                                Chapter lastChapter = chapters.get(chapters.size() - 1);

                                Book book = new Book();
                                book.id = downloadTask.getBookId();
                                book.chapter = new Chapter();
                                book.chapter.sn = lastChapter.sn;
                                book.chapters_update_state = 2;
                                book.chapters_update_index = lastChapter.sn + 1;
                                BookDaoHelper.getInstance(DownloadService.this).updateBook(book);
                            }
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.newThread())
                    .subscribe(new ResourceSubscriber<CommunalResult<ArrayList<Chapter>>>() {

                        @Override
                        public void onNext(CommunalResult<ArrayList<Chapter>> chapterListResult) {
                            Logger.d("LoadBookCatalog onNext");

                            if (chapterListResult != null) {
                                if (chapterListResult.getCode() == 0 && chapterListResult.getModel() != null && chapterListResult.getModel().size() >= 0) {

                                    ArrayList<Chapter> chapters = chapterListResult.getModel();

                                    if (chapters != null && chapters.size() > 0) {

                                        downloadTask.end = chapters.size() - 1;
                                        startDownloadChapter(chapters, downloadTask);
                                    }
                                } else if (!TextUtils.isEmpty(chapterListResult.getMessage())) {
                                    Toast.makeText(getApplicationContext(), chapterListResult.getMessage(), Toast.LENGTH_SHORT).show();

                                    downloadTaskFail(downloadTask);
                                }
                            } else {
                                downloadTaskFail(downloadTask);
                            }
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            Logger.d("LoadBookCatalog onError: " + throwable.toString());
                            downloadTaskFail(downloadTask);
                        }

                        @Override
                        public void onComplete() {
                            Logger.d("LoadBookCatalog onComplete");
                        }
                    });
        } else {
            startDownloadChapter(chapterList, downloadTask);
        }
    }

    public void startDownloadChapter(final ArrayList<Chapter> chapters, final DownloadTask downloadTask) {

        if (chapters != null && chapters.size() > 0) {
            final int count = chapters.size();

            download_position = downloadTask.start;
            downloadTask.end = (downloadTask.end > count - 1) ? count - 1 : downloadTask.end;

            DisposableSubscriber<CommunalResult<ArrayList<Chapter>>> downloadSubscriber = new DisposableSubscriber<CommunalResult<ArrayList<Chapter>>>() {
                @Override
                public void onNext(CommunalResult<ArrayList<Chapter>> chapterListResult) {
                    Logger.d("LoadChapterBatch onNext");
                    if (chapterListResult != null) {
                        if (chapterListResult.getCode() == 0 && chapterListResult.getModel() != null && chapterListResult.getModel().size() > 0) {

                            ArrayList<Chapter> chapterList = chapterListResult.getModel();
                            for (Chapter chapter : chapterList) {
                                if (chapter == null) {
                                    continue;
                                }
                                if (FileUtil.checkFileExist(chapter.id, downloadTask.getBookId())) {
                                    continue;
                                }
                                String content = chapter.content;
                                if (TextUtils.isEmpty(content)) {
                                    content = "null";
                                }
                                if (TextUtils.equals(chapter.status, BaseConfig.ENABLE)) {
                                    FileUtil.saveChapterToCache(content, chapter.id, downloadTask.getBookId());
                                }
                            }

                            int size = chapterListResult.getModel().size();
                            refreshDownloadProgress(downloadTask, size);
                        }
                    }
                }

                @Override
                public void onError(Throwable throwable) {
                    Logger.d("LoadChapterBatch onError: " + throwable.toString());
                    downloadTaskFail(downloadTask);
                }

                @Override
                public void onComplete() {
                    Logger.d("LoadChapterBatch onComplete");
                    checkDownloadTaskFinish(downloadTask);
                }
            };

            downloadTask.addSubscribe(downloadSubscriber);

            final DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, false);

            Flowable.fromIterable(chapters)
                    .buffer(DOWN_SIZE)
                    .subscribeOn(Schedulers.io())
                    .filter(new Predicate<List<Chapter>>() {
                        @Override
                        public boolean test(@io.reactivex.annotations.NonNull List<Chapter> chapters) throws Exception {
                            if (downloadTask.downloadState != DownloadState.DOWNLOADING) {
                                notificationManager.cancel(NOTIFICATION_FLAG);
                                currentDownloadTask = null;
                            }

                            Chapter chapter = chapters.get(0);

                            if (chapter == null || TextUtils.isEmpty(downloadTask.getBookId())) {
                                return false;
                            }

                            for (int i = 0; i < chapters.size(); i++) {
                                Chapter currentChapter = chapters.get(i);
                                if (currentChapter != null) {
                                    if (FileUtil.checkFileExist(currentChapter.id, downloadTask.getBookId())) {
                                        chapter.content = "isChapterExists";
                                    } else {
                                        return true;
                                    }
                                }
                            }
                            return false;
                        }
                    })
                    .concatMap(new Function<List<Chapter>, Publisher<CommunalResult<ArrayList<Chapter>>>>() {
                        @Override
                        public Publisher<CommunalResult<ArrayList<Chapter>>> apply(@io.reactivex.annotations.NonNull List<Chapter> chapters) throws Exception {
                            Chapter chapter = chapters.get(0);

                            if (chapter != null) {
                                return dataRequestService.loadChapterBatch(downloadTask.getBookId(), chapter.sn);
                            } else {
                                return null;
                            }
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io(), false, 1)
                    .unsubscribeOn(Schedulers.io())
                    .subscribeWith(downloadSubscriber);
        }
    }

    private void checkDownloadTaskFinish(DownloadTask downloadTask) {
        if (downloadTask.downloadState == DownloadState.DOWNLOADING) {
            downloadTaskFinish(downloadTask);
        }

        if (currentDownloadTask != null && currentDownloadTask == downloadTask) {
            currentDownloadTask.unSubscribe();
            currentDownloadTask = null;
        }

        updateDownloadTask();
    }

    private void refreshDownloadProgress(DownloadTask downloadTask, int count) {
        download_position += count;

        download_position = Math.min(download_position, downloadTask.end);

        if (download_position == downloadTask.end) {
            onTaskProgress(downloadTask, download_position, downloadTask.end);
        } else {
            if (downloadTask.downloadState == DownloadState.DOWNLOADING) {
                downloadTask.start = download_position;
                onTaskProgress(downloadTask, download_position, downloadTask.end);
            }
        }
    }

    private void downloadTaskStart(DownloadTask downloadTask) {

        Collection<Map.Entry<String, DownloadTask>> downloadTaskCollection = downloadTaskQueue.getAll();
        for (Map.Entry<String, DownloadTask> entry : downloadTaskCollection) {
            if (entry != null && entry.getValue() != null && entry.getValue().downloadState == DownloadState.DOWNLOADING) {
                entry.getValue().downloadState = DownloadState.PAUSED;
            }
        }
        if (downloadTask != null) {
            downloadTask.downloadState = DownloadState.DOWNLOADING;
            DownloadServiceUtil.downloadServiceStart(DownloadService.this, downloadTask.getBookId());
        }
    }

    private void downloadTaskFail(final DownloadTask downloadTask) {
        if (downloadTask != null) {
            downloadTask.downloadState = DownloadState.REFRESH;
            int start = DownloadServiceUtil.downloadStartIndex(this, downloadTask.getBookId());
            downloadTask.start = start + DownloadServiceUtil.downloadCacheCount(downloadTask.getBookId(), start, downloadTask.book.chapter.sn);
            DownloadServiceUtil.writeDownloadIndex(this, downloadTask.getBookId(), downloadTask.start);
            DownloadServiceUtil.downloadServiceFailed(DownloadService.this, downloadTask.getBookId(), downloadTask.start);

        }

        if (currentDownloadTask != null && currentDownloadTask == downloadTask) {
            currentDownloadTask.unSubscribe();
            currentDownloadTask = null;
        }

        updateDownloadTask();
    }

    private void onTaskProgress(final DownloadTask downloadTask, final int index, final int total) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                int count = 0;
                long current_time = System.currentTimeMillis();
                try {
                    count = (index + 1) * 100 / total;
                } catch (Exception exception) {
                    exception.printStackTrace();
                }

                if (count > download_progress) {
                    download_progress = count;
                }

                if ((current_time - notification_time > 1000 * 2) || count == 100) {
                    if (downloadTask.downloadState == DownloadState.DOWNLOADING) {
                        showNotification(downloadTask.book.name, count, index, total, downloadTask.getBookId());
                    }
                    download_number = index;
                    notification_time = current_time;
                }

                if (downloadTask.downloadState == DownloadState.DOWNLOADING) {
                    if ((current_time - refresh_progress) > 1000 * 1.5) {
                        refresh_progress = current_time;
                        DownloadServiceUtil.downloadServiceRefresh(DownloadService.this, downloadTask.getBookId(), index);
                    }
                }
            }
        });
    }

    private void downloadTaskFinish(final DownloadTask downloadTask) {
        if (downloadTask != null) {
            downloadTask.book = BookDaoHelper.getInstance(this).loadBook(downloadTask.getBookId(), Book.TYPE_ONLINE);
            int count = DownloadServiceUtil.loadDownloadStartIndex(this, downloadTask.book);
            int start = DownloadServiceUtil.downloadStartIndex(this, downloadTask.getBookId());
            downloadTask.start = start + DownloadServiceUtil.downloadCacheCount(downloadTask.getBookId(), start, downloadTask.book.chapter.sn);

            DownloadServiceUtil.writeDownloadIndex(this, downloadTask.getBookId(), downloadTask.start);

            if (count >= downloadTask.end) {
                downloadTask.downloadState = DownloadState.FINISH;
            } else {
                downloadTask.downloadState = DownloadState.REFRESH;
            }

            DownloadServiceUtil.downloadServiceFinish(DownloadService.this, downloadTask.getBookId());
        }
    }

    private void showNotification(String bookName, int progress, int index, int total, String id) {
        if (notificationManager == null) {
            notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        }

        if (index > total)
            index = total;
        int x = progress == 100 ? index + 1 : index;

        PendingIntent pendingIntent = null;
        Intent intent;
        if (!TextUtils.isEmpty(id)) {
            intent = new Intent(this, DownloadNotificationReceiver.class);
            intent.setAction(DownloadNotificationReceiver.ACTION_DOWNLOAD_NOTIFICATION);
            intent.putExtra("id", id);
            pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notify);
        remoteViews.setTextViewText(R.id.notify_title_tv, bookName);
        remoteViews.setTextViewText(R.id.notify_text_tv, x + "/" + total + "  " + progress + "%");
        remoteViews.setImageViewResource(R.id.notify_icon_iv, R.drawable.icon_notification);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContent(remoteViews)
                .setSmallIcon(R.drawable.icon_notification_small)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.icon_notification))
                .setContentIntent(pendingIntent)
                .setWhen(System.currentTimeMillis())
                .setOngoing(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            builder.setPriority(Notification.PRIORITY_HIGH);
        }

        Notification notification = builder.build();
        notification.flags = Notification.FLAG_AUTO_CANCEL;

        if (notificationManager != null) {
            notificationManager.notify(NOTIFICATION_FLAG, notification);
        }
    }

    public void resetDownloadTaskEnd(String id, int end) {
        if (downloadTaskQueue != null) {
            DownloadTask downloadTask = downloadTaskQueue.get(id);
            if (downloadTask != null) {
                downloadTask.end = end;
            }
        }
    }


    public class DownloadBinder extends Binder {
        public DownloadService getService() {
            return DownloadService.this;
        }
    }

    private class DownloadThreadFactory implements ThreadFactory {
        public Thread newThread(@NonNull Runnable runnable) {
            Thread thread = new Thread(runnable);
            thread.setPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
            return thread;
        }
    }
}
