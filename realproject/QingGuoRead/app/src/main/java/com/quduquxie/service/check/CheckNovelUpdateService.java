package com.quduquxie.service.check;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.widget.RemoteViews;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.quduquxie.Constants;
import com.quduquxie.R;
import com.quduquxie.application.QuApplication;
import com.quduquxie.base.bean.Book;
import com.quduquxie.base.database.dao.ChapterDao;
import com.quduquxie.base.database.helper.BookDaoHelper;
import com.quduquxie.model.v2.CheckItem;
import com.quduquxie.model.v2.CheckUpdateResult;
import com.quduquxie.retrofit.DataRequestService;
import com.quduquxie.retrofit.ServiceGenerator;
import com.quduquxie.retrofit.model.CommunalResult;
import com.quduquxie.service.download.DownloadService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;
import okhttp3.RequestBody;

public class CheckNovelUpdateService extends Service {

    public static ArrayList<CheckNovelUpdateHelper.CheckBook> localCheckBooks;

    private CheckNovelUpdateBinder checkNovelUpdateBinder;

    private NotificationManager notificationManager = null;

    private BookDaoHelper bookDaoHelper;

    private SharedPreferences sharedPreferences;

    private int checkUpdateRefreshTime;

    private Handler handler = new Handler();

    private final static String TIME_FORMAT = "k:mm";
    public static final int CHECK_NOVEL_UPDATE_NOTIFICATION_ID = "青果阅读".hashCode();

    public static final String ACTION_CHECK_NOVEL_UPDATE = "com.quduquxie.action_check_update";

    public static final String ACTION_CHECK_NOVEL_UPDATE_CLICK = "com.quduquxie.receiver.check_novel_update";

    public static final int TYPE_CHECK_NOVEL_UPDATE = 0x80;

    public boolean initBook = true;

    @Override
    public IBinder onBind(Intent intent) {
        return checkNovelUpdateBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        checkNovelUpdateBinder = new CheckNovelUpdateBinder();

        if (bookDaoHelper == null) {
            bookDaoHelper = BookDaoHelper.getInstance(this);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            if (ACTION_CHECK_NOVEL_UPDATE.equals(intent.getAction())) {
                checkNovelUpdateIntent();
            }
        }

        if (sharedPreferences == null) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        }

        checkUpdateRefreshTime = sharedPreferences.getInt("check_update_time", Constants.refreshTime);

        if (checkUpdateRefreshTime != 0) {
            Intent checkUpdateIntent = new Intent(this, CheckNovelUpdateService.class);
            checkUpdateIntent.setAction(ACTION_CHECK_NOVEL_UPDATE);
            PendingIntent pendingIntent = PendingIntent.getService(this, 0, checkUpdateIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(pendingIntent);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.add(Calendar.MILLISECOND, checkUpdateRefreshTime);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), checkUpdateRefreshTime, pendingIntent);
        } else {
            Intent checkIntent = new Intent(this, CheckNovelUpdateService.class);
            checkIntent.setAction(ACTION_CHECK_NOVEL_UPDATE);
            PendingIntent pendingIntent = PendingIntent.getService(this, 0, checkIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(pendingIntent);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public void onDestroy() {
        super.onDestroy();
    }

    private void checkNovelUpdateIntent() {
        if (isPushOn()) {
            CheckNovelUpdateTask checkNovelUpdateTask = new CheckNovelUpdateTask();

            if (bookDaoHelper == null) {
                bookDaoHelper = BookDaoHelper.getInstance(this);
            }
            if (checkUpdateRefreshTime != 0) {
                checkNovelUpdateTask.checkUpdateBooks = bookDaoHelper.loadOnlineReadBooks();
            } else {
                checkNovelUpdateTask.checkUpdateBooks = null;
            }

            checkNovelUpdateTask.from = CheckNovelUpdateTask.CheckNovelUpdateTaskFrom.FROM_SELF;
            checkNovelUpdateTask.checkNovelUpdateCallBack = new ShelfCheckNovelUpdateCallBack();
            checkNovelUpdate(checkNovelUpdateTask);
        }
    }

    private void sendCheckNovelUpdateIntent(CheckNovelUpdateTask checkNovelUpdateTask) {
        checkNovelUpdateTask.from = CheckNovelUpdateTask.CheckNovelUpdateTaskFrom.FROM_SELF;
        checkNovelUpdateTask.checkNovelUpdateCallBack = new ShelfCheckNovelUpdateCallBack();
        checkNovelUpdate(checkNovelUpdateTask);
    }

    public void addCheckNovelUpdateTask(CheckNovelUpdateTask checkNovelUpdateTask) {
        if (checkUpdateRefreshTime != 0) {
            checkNovelUpdate(checkNovelUpdateTask);
        } else {
            checkNovelUpdateTask.checkUpdateBooks = null;
            checkNovelUpdate(checkNovelUpdateTask);
        }
    }

    private void checkNovelUpdate(final CheckNovelUpdateTask checkNovelUpdateTask) {
        if (checkNovelUpdateTask == null) {
            return;
        }

        final ArrayList<Book> books = checkNovelUpdateTask.checkUpdateBooks;

        final CheckUpdateResult checkUpdateResult = new CheckUpdateResult();
        if (books == null || books.size() == 0) {
            onCheckUpdateSuccess(checkNovelUpdateTask, checkUpdateResult);
            return;
        }

        List<CheckItem> checkItems = new ArrayList<>();

        CheckItem checkItem;
        for (Book book : books) {
            if (!TextUtils.isEmpty(book.id) && book.chapter != null && book.chapter.sn != 0) {
                checkItem = new CheckItem(book.id, book.chapter.sn);
                checkItems.add(checkItem);
            }
        }

        Gson gson = new Gson();

        RequestBody json = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), gson.toJson(checkItems));

        Logger.d(json);

        DataRequestService dataRequestService = ServiceGenerator.getHttpsInstance(DataRequestService.class, false);
        dataRequestService.checkBookUpdate(initBook ? 1 : 0, json)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .doOnNext(new Consumer<CommunalResult<ArrayList<Book>>>() {
                    @Override
                    public void accept(@NonNull CommunalResult<ArrayList<Book>> booksResult) throws Exception {
                        if (initBook && booksResult != null && booksResult.getCode() == 0 && booksResult.getModel() != null && booksResult.getModel().size() > 0) {
                            ArrayList<Book> bookList = booksResult.getModel();
                            for (Book book : bookList) {
                                BookDaoHelper bookDaoHelper = BookDaoHelper.getInstance(CheckNovelUpdateService.this);
                                bookDaoHelper.updateBook(book);
                            }
                        }
                        initBook = false;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResourceSubscriber<CommunalResult<ArrayList<Book>>>() {
                    @Override
                    public void onNext(CommunalResult<ArrayList<Book>> opusesResult) {
                        Logger.d("CheckBookUpdate onNext");
                        if (opusesResult != null) {
                            if (opusesResult.getCode() == 0 && opusesResult.getModel() != null && opusesResult.getModel().size() > 0) {

                                ArrayList<Book> bookList = opusesResult.getModel();

                                Logger.d("CheckBookUpdate onNext: " + bookList);

                                BookDaoHelper bookDaoHelper = BookDaoHelper.getInstance(CheckNovelUpdateService.this);

                                /*
                                  使用本地目录同服务器目录进行对比,通过对比长度以及最后的章节名来确定是否全量更新
                                 */
                                if (bookList != null && !bookList.isEmpty()) {
                                    for (Book book : bookList) {
                                        if (book == null || !bookDaoHelper.subscribeBook(book.id)) {
                                            continue;
                                        }

                                        ChapterDao chapterDao = new ChapterDao(CheckNovelUpdateService.this, book.id);

                                        int count_lasted = book.chapter.sn;
                                        int count_before = chapterDao.loadChapterCount();

                                        /*
                                          判断chapters_update_index是否等于0,适配新的更新逻辑,修复断章逻辑
                                         */
                                        Book localBook = bookDaoHelper.loadBook(book.id, Book.TYPE_ONLINE);

                                        if (localBook.chapters_update_index == 0 && localBook.chapters_update_state == 1) {

                                            chapterDao.deleteChapter(0);

                                            book.update_status = 1;
                                            book.chapters_update_state = 1;
                                            book.chapters_update_index = 1;

                                            if (count_lasted - count_before > 0) {
                                                book.update_count = count_lasted - count_before;
                                            } else {
                                                book.update_count = 0;
                                            }

                                            bookDaoHelper.updateBook(book);

                                            resetDownloadEndSequence(book.id, book.chapter.sn - 1);

                                            Logger.d("checkNovelUpdate ID: " + book.id + " count_lasted:" + count_lasted + " count_before:" + count_before + " update_count:" + book.update_count);
                                            continue;
                                        }

                                        /*
                                          本地目录和服务器目录做长度对比
                                         */
                                        if (count_before == count_lasted) {
                                            boolean isExist = isExistChapter(chapterDao, book);
                                            if (!isExist) {
                                                chapterDao.deleteChapter(0);

                                                bookDaoHelper.deleteBookmarks(book.id);

                                                book.update_status = 1;
                                                book.chapters_update_state = 1;
                                                book.chapters_update_index = 1;

                                                book.update_count = 0;
                                                book.repairBookmark = true;

                                                bookDaoHelper.updateBook(book);

                                                resetDownloadEndSequence(book.id, book.chapter.sn - 1);

                                                Logger.d("checkNovelUpdate ID: " + book.id + " count_lasted:" + count_lasted + " count_before:" + count_before + " update_count:" + book.update_count);
                                            }
                                        } else if (count_lasted > count_before) {

                                            book.update_status = 1;
                                            book.chapters_update_state = 1;
                                            if (localBook.chapters_update_index == 0) {
                                                book.chapters_update_index = count_before + 1;
                                            }
                                            if (count_before != chapterDao.loadChapterCount()) {
                                                book.chapters_update_index = 1;
                                            }

                                            Logger.d("Chapter_Update_Index: " + book.chapters_update_index);

                                            book.update_count = count_lasted - count_before;

                                            bookDaoHelper.updateBook(book);

                                            resetDownloadEndSequence(book.id, book.chapter.sn - 1);

                                            Logger.d("checkNovelUpdate ID: " + book.id + " count_lasted:" + count_lasted + " count_before:" + count_before + " update_count:" + book.update_count);
                                        } else {
                                            chapterDao.deleteChapter(0);
                                            bookDaoHelper.deleteBookmarks(book.id);

                                            book.update_status = 1;
                                            if ((localBook.sequence + (count_before - count_lasted)) >= count_before) {
                                                book.offset = 0;
                                            }
                                            book.chapters_update_state = 1;
                                            book.chapters_update_index = 1;


                                            book.update_count = 0;
                                            book.repairBookmark = true;

                                            bookDaoHelper.updateBook(book);

                                            resetDownloadEndSequence(book.id, book.chapter.sn - 1);

                                            Logger.d("checkNovelUpdate ID: " + book.id + " count_lasted:" + count_lasted + " count_before:" + count_before + " update_count:" + book.update_count);
                                        }
                                    }
                                }

                                if (bookList == null) {
                                    checkUpdateResult.books = null;
                                } else {
                                    checkUpdateResult.books = bookList;
                                }
                                if (checkNovelUpdateTask.checkNovelUpdateCallBack != null) {
                                    onCheckUpdateSuccess(checkNovelUpdateTask, checkUpdateResult);
                                }
                            } else {
                                if (checkNovelUpdateTask.checkNovelUpdateCallBack != null) {
                                    handler.post(new Runnable() {

                                        @Override
                                        public void run() {
                                            checkNovelUpdateTask.checkNovelUpdateCallBack.onException(new Exception("检查更新失败！"));
                                        }
                                    });
                                }
                            }
                        } else {
                            if (checkNovelUpdateTask.checkNovelUpdateCallBack != null) {
                                handler.post(new Runnable() {

                                    @Override
                                    public void run() {
                                        checkNovelUpdateTask.checkNovelUpdateCallBack.onException(new Exception("检查更新失败！"));
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onError(final Throwable throwable) {
                        Logger.d("CheckBookUpdate onError: " + throwable.toString());

                        if (checkNovelUpdateTask.checkNovelUpdateCallBack != null) {
                            handler.post(new Runnable() {

                                @Override
                                public void run() {
                                    checkNovelUpdateTask.checkNovelUpdateCallBack.onException((Exception) throwable);
                                }
                            });
                        }
                    }

                    @Override
                    public void onComplete() {
                        Logger.d("CheckBookUpdate onComplete");
                    }
                });
    }

    private void onCheckUpdateSuccess(final CheckNovelUpdateTask checkNovelUpdateTask, final CheckUpdateResult checkUpdateResult) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (checkNovelUpdateTask.checkNovelUpdateCallBack != null) {
                    checkNovelUpdateTask.checkNovelUpdateCallBack.onSuccess(checkUpdateResult);
                }
            }
        });
    }

    public static void startCheckUpdateService(Context context) {
        try {
            Intent intent = new Intent();
            intent.setClass(context, CheckNovelUpdateService.class);
            context.startService(intent);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }


    public class CheckNovelUpdateBinder extends Binder {
        public CheckNovelUpdateService getService() {
            return CheckNovelUpdateService.this;
        }
    }

    private class ShelfCheckNovelUpdateCallBack implements CheckNovelUpdateCallBack {

        @Override
        public void onSuccess(CheckUpdateResult checkUpdateResult) {
            onCheckNovelUpdateSuccess(checkUpdateResult);
        }

        @Override
        public void onException(Exception exception) {

        }
    }

    private void onCheckNovelUpdateSuccess(CheckUpdateResult checkUpdateResult) {
        if (checkUpdateResult != null) {
            ArrayList<Book> books = checkUpdateResult.books;

            if (books == null) {
                return;
            }

            ArrayList<CheckNovelUpdateHelper.CheckBook> checkBooks = new ArrayList<>();

            String chapter = null;
            if (bookDaoHelper == null) {
                bookDaoHelper = BookDaoHelper.getInstance(this);
            }
            for (int i = 0; i < books.size(); i++) {
                Book bookObject = books.get(i);

                if (bookObject != null && !TextUtils.isEmpty(bookObject.id)) {
                    Book book = bookDaoHelper.loadBook(bookObject.id, Book.TYPE_ONLINE);
                    if (bookObject.update_count > 0) {
                        chapter = bookObject.name;

                        if (!TextUtils.isEmpty(book.name)) {
                            CheckNovelUpdateHelper.CheckBook checkBook = new CheckNovelUpdateHelper.CheckBook(book.name, book.id, bookObject.update_count);
                            checkBooks.add(checkBook);
                        }
                    }
                }
            }
            if (checkBooks.size() > 0) {
                localCheckBooks = checkBooks;

                int size = checkBooks.size();

                Intent intent = new Intent(DownloadService.DOWNLOAD_SERVICE_INDEX_REFRESH);
                sendBroadcast(intent);

                if (size > 0) {
                    if (size == 1) {
                        CheckNovelUpdateHelper.CheckBook checkBook = checkBooks.get(0);
                        if (checkBook.count > 0) {
                            showNotification(initNotificationMessage(checkBooks) + getString(R.string.updatenofiy_new_update) + checkBook.count + getString(R.string.update_notify_zhang), chapter, "");
                        }
                    } else {
                        String result = "《" + checkBooks.get(0).name + "》" + getString(R.string.update_notify_and) + size + getString(R.string.update_book_count_new);
                        showNotification(result, initNotificationMessage(checkBooks), "");
                    }
                }
            }
        }
    }

    private void showNotification(String message, String content, String id) {
        if (isPushOn() && !TextUtils.isEmpty(content)) {
            if (notificationManager == null) {
                notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            }
            String packageName = getPackageName();
            if (TextUtils.isEmpty(packageName)) {
                packageName = "com.quduquxie";
            }

            RemoteViews remoteViews = new RemoteViews(packageName, R.layout.notify_chk_novel_upd);
            remoteViews.setTextViewText(R.id.notify_title_tv, message);
            remoteViews.setTextViewText(R.id.notify_text_tv, content);
            remoteViews.setTextViewText(R.id.notify_time, getCurrentTime());
            remoteViews.setImageViewResource(R.id.notify_icon_iv, R.drawable.icon_notification);

            try {
                Intent intent = new Intent();
                intent.setAction(ACTION_CHECK_NOVEL_UPDATE_CLICK);
                intent.putExtra("id", id);
                intent.setPackage(packageName);
                PendingIntent pending = PendingIntent.getBroadcast(getApplicationContext(), TYPE_CHECK_NOVEL_UPDATE, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
                builder.setContent(remoteViews)
                        .setSmallIcon(R.drawable.icon_notification_small)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.icon_notification))
                        .setContentIntent(pending)
                        .setWhen(System.currentTimeMillis())
                        .setOngoing(true);
                Notification notify = builder.build();
                notify.flags = Notification.FLAG_AUTO_CANCEL;
                notificationManager.notify(CHECK_NOVEL_UPDATE_NOTIFICATION_ID, notify);

            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    private String getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        CharSequence charSequence = DateFormat.format(TIME_FORMAT, calendar);
        return (String) charSequence;

    }

    private boolean isPushOn() {
        if (sharedPreferences == null) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        }
        return sharedPreferences.getBoolean("settings_push", true);
    }

    private String initNotificationMessage(ArrayList<CheckNovelUpdateHelper.CheckBook> checkBooks) {
        if (checkBooks == null || checkBooks.size() == 0) {
            return null;
        }

        StringBuilder stringBuilder = new StringBuilder();

        for (CheckNovelUpdateHelper.CheckBook checkBook : checkBooks) {
            if (checkBook != null && !TextUtils.isEmpty(checkBook.name))
                stringBuilder.append("《").append(checkBook.name).append("》");
        }
        return stringBuilder.toString();
    }


    private void resetDownloadEndSequence(String id, int count) {
        QuApplication application = QuApplication.getInstance();
        DownloadService downloadService = application.getDownloadService();
        if (downloadService != null) {
            downloadService.resetDownloadTaskEnd(id, count - 1);
        }
    }

    private synchronized boolean isExistChapter(ChapterDao chapterDao, Book book) {
        boolean isExist = false;
        if (book != null && book.chapter != null) {
            isExist = chapterDao.loadChapter(book.chapter.id);
        }
        return isExist;
    }
}
