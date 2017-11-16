package com.shortmeet.www.service;

import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.shortmeet.www.Api.ApiConstant;
import com.shortmeet.www.R;
import com.shortmeet.www.utilsUsed.LogUtils;
import com.shortmeet.www.utilsUsed.MD5Utils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;
import java.lang.reflect.Method;

import okhttp3.Call;

/**
 * Created by SHM on 2017/01/13.
 */
public class VersionUpdateService extends Service {
    private static final String TAG = "VersionUpdateService";
    private static final int NOTIFICATION_ID = 100;
    private DownloadManager downloadManager;
    private long mTaskId;
    private Notification.Builder notificationBuilder;
    private NotificationManager mNotificationManager;
    private int progressFinal;
    private NotificationUpdaterThread notificationUpdaterThread;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String versionUrl = intent.getStringExtra(ApiConstant.DOWNLOAD_APK_URL);
        double vcode = intent.getDoubleExtra(ApiConstant.DOWNLOAD_APK_VERSION_CODE, 0);
        downloadAPK(versionUrl, vcode);
        return super.onStartCommand(intent, flags, startId);

    }

    private void downloadAPK(String versionUrl, double vcode) {
        if (mNotificationManager == null)
            mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        starDownLoadForground();

        notificationUpdaterThread = new NotificationUpdaterThread();
        notificationUpdaterThread.exit = false;
        notificationUpdaterThread.start();

        File fileDir = new File(Environment.getExternalStorageDirectory(), "tanlangui");
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        String fileName_ = versionUrl.substring(versionUrl.lastIndexOf("/") + 1);
       String fileName = MD5Utils.getMD5String(fileName_) + String.valueOf(vcode) + ".apk";
        OkHttpUtils.get().url(versionUrl).build().execute(new FileCallBack(fileDir.getAbsolutePath(), fileName) {
            @Override
            public void inProgress(float progress, long total, int id) {
                super.inProgress(progress, total, id);
                LogUtils.e(TAG, "inProgress() called with: " + "progressFinal = [" + progress + "], total = [" + total + "], id = [" + id + "]");
                progressFinal = (int) (progress * 100);
                if (progressFinal >= 100) {
                    downloadDone();
                }
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.e(TAG, "onError: ", e);
                downloadError();
            }

            @Override
            public void onResponse(File response, int id) {
                LogUtils.e(TAG, "onResponse: " + response.getAbsolutePath());
                installAPK(response);
            }
        });
    }

    private void downloadError() {
        notificationBuilder.setContentTitle("正在下载更新" + progressFinal + "%"); // the label of the entry
        notificationBuilder.setProgress(0, 0, false);
        notificationBuilder.setTicker("下载失败");
        notificationBuilder.setContentText("下载失败");
        notificationBuilder.setOngoing(false);
        mNotificationManager.notify(NOTIFICATION_ID, notificationBuilder.getNotification());
//        if (mNotificationManager != null) {
//            mNotificationManager.cancel(NOTIFICATION_ID);
//        }
        notificationUpdaterThread.exit = true;
//        collapseStatusBar(this);
        stopSelf();
    }

    private void downloadDone() {
        notificationBuilder.setContentTitle("正在下载更新" + progressFinal + "%"); // the label of the entry
        notificationBuilder.setProgress(100, progressFinal, false);
        notificationBuilder.setTicker("下载完成");
        notificationBuilder.setContentText("下载完成");
        notificationBuilder.setOngoing(false);
        mNotificationManager.notify(NOTIFICATION_ID, notificationBuilder.getNotification());
        if (mNotificationManager != null) {
            mNotificationManager.cancel(NOTIFICATION_ID);
        }
        collapseStatusBar(this);
        notificationUpdaterThread.exit = false;
    }

    @SuppressWarnings("WrongConstant")
    private void collapseStatusBar(Context context) {
        try {
            Object service = context.getSystemService("statusbar");
            Class statusBarManager = Class.forName("android.app.StatusBarManager");
            Method collapse;
            if (service != null) {
                if (Build.VERSION.SDK_INT <= 16) {
                    collapse = statusBarManager.getMethod("collapse");
                } else {
                    // api17开始这个方法的名字变了
                    collapse = statusBarManager.getMethod("collapsePanels");
                }
                collapse.setAccessible(true);
                collapse.invoke(service);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    private void starDownLoadForground() {
        // In this sample, we'll use the same text for the ticker and the expanded notification
        CharSequence text = "下载中,请稍后...";
        // The PendingIntent to launch our activity if the user selects this notification
//        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
//                new Intent(this, MainActivity.class), 0);
        notificationBuilder = new Notification.Builder(this);
        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);  // the status icon
        notificationBuilder.setTicker(text);  // the status text
        notificationBuilder.setWhen(System.currentTimeMillis());  // the time stamp
        notificationBuilder.setContentText(text);  // the contents of the entry
//        notificationBuilder.setContentIntent(contentIntent);  // The intent to send when the entry is clicked
        notificationBuilder.setContentTitle("正在下载更新" + 0 + "%"); // the label of the entry
        notificationBuilder.setProgress(100, 0, false);
        notificationBuilder.setOngoing(true);
//        notificationBuilder.setAutoCancel(true);
        Notification notification = notificationBuilder.getNotification();
//        startForeground(NOTIFICATION_ID, notification);
        mNotificationManager.notify(NOTIFICATION_ID, notification);
    }


//    private void stopDownLoadForground() {
//        stopForeground(true);
//    }

    private class NotificationUpdaterThread extends Thread {
        private volatile boolean exit = false;

        @Override
        public void run() {
            while (!exit) {
                notificationBuilder.setContentTitle("正在下载更新" + progressFinal + "%"); // the label of the entry
                notificationBuilder.setProgress(100, progressFinal, false);
                mNotificationManager.notify(NOTIFICATION_ID, notificationBuilder.getNotification());
                if (progressFinal >= 100) {
                    break;
                }
            }
        }
    }


//    private void downloadAPK(String versionUrl, double versionCode) {
//        //创建下载任务
//        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(versionUrl));
//        request.setAllowedOverRoaming(false);//漫游网络是否可以下载
//
//        //设置文件类型，可以在下载结束后自动打开该文件
//        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
//        String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(versionUrl));
//        request.setMimeType(mimeString);
//
//        //在通知栏中显示，默认就是显示的
//        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
//        request.setVisibleInDownloadsUi(true);
//
//        //sdcard的目录下的download文件夹，必须设置
//        request.setDestinationInExternalPublicDir("/download/", String.valueOf(versionCode));
//        //request.setDestinationInExternalFilesDir(),也可以自己制定下载路径
//
//        //将下载请求加入下载队列
//        downloadManager = (DownloadManager) this.getSystemService(Context.DOWNLOAD_SERVICE);
//        //加入下载队列后会给该任务返回一个long型的id，
//        //通过该id可以取消任务，重启任务等等，看上面源码中框起来的方法
//        mTaskId = downloadManager.enqueue(request);
//
//        //注册广播接收者，监听下载状态
//        this.registerReceiver(receiver,
//                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
//    }
//
//    private BroadcastReceiver receiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            checkDownloadStatus();//检查下载状态
//        }
//    };
//
//    private void checkDownloadStatus() {
//        DownloadManager.Query query = new DownloadManager.Query();
//        query.setFilterById(mTaskId);//筛选下载任务，传入任务ID，可变参数
//        Cursor c = downloadManager.query(query);
//        if (c.moveToFirst()) {
//            int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
//            switch (status) {
//                case DownloadManager.STATUS_PAUSED:
////                    MLog.i(">>>下载暂停");
//                case DownloadManager.STATUS_PENDING:
////                    MLog.i(">>>下载延迟");
//                case DownloadManager.STATUS_RUNNING:
////                    MLog.i(">>>正在下载");
//                    break;
//                case DownloadManager.STATUS_SUCCESSFUL:
////                    MLog.i(">>>下载完成");
//                    //下载完成安装APK
//                    String downloadPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + String.valueOf(vcode);
//                    installAPK(new File(downloadPath));
//                    break;
//                case DownloadManager.STATUS_FAILED:
////                    MLog.i(">>>下载失败");
//                    break;
//            }
//        }
//    }

    protected void installAPK(File file) {
        if (!file.exists()) return;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.parse("file://" + file.toString());
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        //在服务中开启activity必须设置flag,后面解释
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        stopSelf();
    }


}
