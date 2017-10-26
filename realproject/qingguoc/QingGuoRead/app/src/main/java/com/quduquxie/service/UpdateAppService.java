package com.quduquxie.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.widget.RemoteViews;

import com.orhanobut.logger.Logger;
import com.quduquxie.Constants;
import com.quduquxie.R;
import com.quduquxie.base.bean.Update;
import com.quduquxie.retrofit.DataRequestService;
import com.quduquxie.retrofit.ServiceGenerator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class UpdateAppService extends Service {
    protected UpdateServiceBinder binder;

    ExecutorService executorService;

    boolean is_show_down_notify = false;
    Update update;
    NotificationManager notificationManager;

    private static final int NOTIFICATION_APPLICATION_FLAG = "青果阅读应用离线下载".hashCode();

    private String file_path;
    private String apk_url;

    private boolean isDowning;

    private long notifyTime = 0;
    private int down_num = 0;
    private boolean isInstall = false;

    @Override
    public void onCreate() {
        super.onCreate();
        binder = new UpdateServiceBinder();
        if (notificationManager == null) {
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        notificationManager.cancel(NOTIFICATION_APPLICATION_FLAG);

        if (executorService == null) {
            executorService = Executors.newSingleThreadExecutor(new DownloadApplicationThreadFactory());
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            update = (Update) intent.getSerializableExtra("updateBean");
            if (update != null) {
                apk_url = update.url;
            }

            if (apk_url != null && !TextUtils.isEmpty(apk_url) && apk_url.contains(".apk")) {
                file_path = apk_url.substring(apk_url.lastIndexOf("/") + 1, apk_url.length());
            }

            is_show_down_notify = intent.getBooleanExtra("show_notify", false);

            isDowning = true;
            isInstall = intent.getBooleanExtra("isInstall", false);
            executeGetTopicTask();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        if (binder == null) {
            return new UpdateServiceBinder();
        }
        return binder;
    }

    @Override
    public void onDestroy() {
        if (executorService != null) {
            executorService.shutdown();
            executorService = null;
        }
        super.onDestroy();
    }

    public class UpdateServiceBinder extends Binder {
        public UpdateAppService getService() {
            return UpdateAppService.this;
        }
    }

    public void executeGetTopicTask() {
        if (executorService == null) {
            executorService = Executors.newSingleThreadExecutor(new DownloadApplicationThreadFactory());
        }
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                downLoaderOkHttp();
            }
        });
    }

    private void installApp(File file) {
        UpdateAppService.this.stopSelf();
        if (file == null || !isInstall) {
            return;
        }

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        String type = "application/vnd.android.package-archive";
        Uri data = Uri.fromFile(file);
        intent.setDataAndType(data, type);
        startActivity(intent);
    }

    private void downloadSuccess(File file, int getBytes, int entitySize) {
        isDowning = false;
        showSuccessNotification(file, getString(R.string.app_name), getBytes, entitySize);
        if (file != null && file.length() == entitySize) {
            installApp(file);
        }
    }

    private void downloadProgress(int progress, int getBytes) {
        long cur_time = System.currentTimeMillis();
        if (getBytes > down_num && cur_time - notifyTime > 1500L) {
            if (isDowning) {
                showNotification(getString(R.string.app_name), progress);
            }
            notifyTime = cur_time;
            down_num = getBytes;
        }
    }

    private void downloadError(File file, int getBytes, int entitySize) {
        isDowning = false;
        showErrorNotification(getString(R.string.app_name), getBytes, entitySize);
        if (file != null) {
            delFile(file.getPath());
        }
    }

    private void downloadFinish(File file) {
        installApp(file);
    }

    private void showNotification(String appName, int progress) {

        Logger.d("ShowNotification: " + progress);

        if (notificationManager == null) {
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notify_update_app);
        remoteViews.setTextViewText(R.id.notify_title_tv, getString(R.string.download_app_notify_title));
        remoteViews.setProgressBar(R.id.pb_update_app, 100, progress, false);
        remoteViews.setTextViewText(R.id.notify_text_tv, appName + getString(R.string.download_app_notify_ticker));
        remoteViews.setTextViewText(R.id.notify_time, getCurTime());
        remoteViews.setImageViewResource(R.id.notify_icon_iv, R.drawable.icon_notification);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);

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

        notificationManager.notify(NOTIFICATION_APPLICATION_FLAG, notification);
    }

    private void showSuccessNotification(File file, String appName, int getBytes, int total) {
        if (notificationManager == null)
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notify_update_app);
        remoteViews.setTextViewText(R.id.notify_title_tv, getString(R.string.download_app_notify_finish_title));
        remoteViews.setTextViewText(R.id.notify_text_tv, appName + getString(R.string.download_app_notify_finish_content));
        remoteViews.setProgressBar(R.id.pb_update_app, total, getBytes, false);
        remoteViews.setImageViewResource(R.id.notify_icon_iv, R.drawable.icon_notification);

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        String type = "application/vnd.android.package-archive";
        Uri data = Uri.fromFile(file);
        intent.setDataAndType(data, type);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);


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

        notificationManager.notify(NOTIFICATION_APPLICATION_FLAG, notification);

    }

    private void showErrorNotification(String appName, int getBytes, int total) {
        if (notificationManager == null)
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notify_update_app);
        remoteViews.setTextViewText(R.id.notify_title_tv, getString(R.string.download_app_notify_title));

        remoteViews.setProgressBar(R.id.pb_update_app, total, getBytes, false);
        remoteViews.setTextViewText(R.id.notify_text_tv, appName + getString(R.string.download_app_notify_info_error));
        remoteViews.setTextViewText(R.id.notify_time, getCurTime());
        remoteViews.setImageViewResource(R.id.notify_icon_iv, R.drawable.icon_notification);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);


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

        notificationManager.notify(NOTIFICATION_APPLICATION_FLAG, notification);
    }

    private String getCurTime() {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(System.currentTimeMillis());
        CharSequence s = DateFormat.format("k:mm", mCalendar);
        return (String) s;

    }

    private void downLoaderOkHttp() {
        DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class);
        dataRequestService.downloadApplication(apk_url)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResourceSubscriber<Response<ResponseBody>>() {
                    @Override
                    public void onNext(retrofit2.Response<ResponseBody> responseBody) {
                        Logger.d("DownloadApplication onNext");
                        if (responseBody.isSuccessful() && responseBody.body() != null) {
                            if (responseBody.body().contentLength() > 0)
                            writeDownloadInput(responseBody.body());
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Logger.d("DownloadApplication onError: " + throwable.toString());
                    }

                    @Override
                    public void onComplete() {
                        Logger.d("DownloadApplication onComplete");
                    }
                });

    }

    private void writeDownloadInput(ResponseBody responseBody) {
        String filePath = Constants.APP_PATH_DOWNLOAD + file_path;

        File file = null;

        int getBytes = 0;
        int entitySize = (int) responseBody.contentLength();

        OutputStream outputStream = null;
        InputStream inputStream = null;


        try {
            makeDirs(filePath);// 创建目录
            file = new File(filePath);

            // 判断当文件名和大小都相同的情况下不会重复下载
            if (isFileExist(filePath) && file.length() == entitySize) {
                downloadFinish(file);
                return;
            }

            inputStream = responseBody.byteStream();
            outputStream = new FileOutputStream(filePath);
            writeFile(file, entitySize, inputStream, outputStream);

        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
            downloadError(file, getBytes, entitySize);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
                inputStream = null;
            }
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                outputStream = null;
            }
        }
    }

    private static boolean makeDirs(String filePath) {
        String folderName = getFolderName(filePath);
        if (TextUtils.isEmpty(folderName)) {
            return false;
        }

        File folder = new File(folderName);
        return (folder != null && folder.exists() && folder.isDirectory()) ? true : folder.mkdirs();
    }

    private static String getFolderName(String filePath) {

        if (filePath == null || TextUtils.isEmpty(filePath)) {
            return filePath;
        }

        int filePosi = filePath.lastIndexOf(File.separator);
        return (filePosi == -1) ? "" : filePath.substring(0, filePosi);
    }

    /**
     * Indicates if this file represents a file on the underlying file system.
     */
    private static boolean isFileExist(String filePath) {
        if ((filePath == null || filePath.trim() == null || filePath.trim().length() == 0)) {
            return false;
        }

        File file = new File(filePath);
        return (file != null && file.exists() && file.isFile());
    }

    /**
     * write file
     */
    private void writeFile(File file, int entitySize, InputStream stream, OutputStream outputStream) {
        int getBytes = 0;
        int progress;
        try {
            byte data[] = new byte[102400];
            int length = -1;
            while ((length = stream.read(data)) != -1) {
                outputStream.write(data, 0, length);
                getBytes += length;
                progress = (getBytes * 100) / entitySize;
                if (getBytes > entitySize) {
                    getBytes = entitySize;
                }

                if (progress > 99) {
                    downloadSuccess(file, getBytes, entitySize);
                } else {
                    downloadProgress(progress, getBytes);
                }
            }
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        }
    }

    private void delFile(String localPath) {
        File file = new File(localPath);
        if (file.exists()) {
            file.delete();
        }
    }

    private class DownloadApplicationThreadFactory implements ThreadFactory {
        public Thread newThread(@NonNull Runnable runnable) {
            Thread thread = new Thread(runnable);
            thread.setPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
            return thread;
        }
    }
}
