package com.quduquxie.service.download;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.quduquxie.base.bean.Book;
import com.quduquxie.base.database.helper.BookDaoHelper;
import com.quduquxie.base.module.main.activity.view.MainActivity;
import com.quduquxie.module.read.reading.view.ReadingActivity;

/**
 * Created on 17/4/21.
 * Created by crazylei.
 */

public class DownloadNotificationReceiver extends BroadcastReceiver {

    public static final String ACTION_DOWNLOAD_NOTIFICATION = "com.quduquxie.service.download.action_notification";

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean start = false;
        if (intent != null) {
            ActivityManager activityManager = (ActivityManager) context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningTaskInfo runningTaskInfo : activityManager.getRunningTasks(Integer.MAX_VALUE)) {
                if (MainActivity.class.getName().equals(runningTaskInfo.baseActivity.getClassName())) {
                    start = true;
                    break;
                }
            }
            if (!start) {
                String id = intent.getStringExtra("id");
                BookDaoHelper bookDaoHelper = BookDaoHelper.getInstance(context);
                if (bookDaoHelper != null && bookDaoHelper.subscribeBook(id)) {

                    Intent activityIntent = new Intent();
                    activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activityIntent.setClass(context, ReadingActivity.class);
                    if (!TextUtils.isEmpty(id)) {
                        Book book = BookDaoHelper.getInstance(context).loadBook(id, Book.TYPE_ONLINE);
                        Bundle bundle = new Bundle();
                        bundle.putInt("sequence", book.sequence);
                        bundle.putInt("offset", book.offset);
                        bundle.putSerializable("book", book);
                        activityIntent.putExtras(bundle);
                        context.startActivity(activityIntent);
                    }
                }
            } else {
                String id = intent.getStringExtra("id");
                BookDaoHelper bookDaoHelper = BookDaoHelper.getInstance(context);
                if (bookDaoHelper != null && bookDaoHelper.subscribeBook(id)) {
                    Intent activityIntent = new Intent();
                    activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activityIntent.setClass(context, ReadingActivity.class);
                    if (!TextUtils.isEmpty(id)) {
                        Book book = BookDaoHelper.getInstance(context).loadBook(id, 0);
                        Bundle bundle = new Bundle();
                        bundle.putInt("sequence", book.sequence);
                        bundle.putInt("offset", book.offset);
                        bundle.putSerializable("book", book);
                        activityIntent.putExtras(bundle);
                        context.startActivity(activityIntent);
                    }
                } else {
                    Toast.makeText(context, "书籍已删除！", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }
}