package com.quduquxie.receiver;

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


public class DownBookClickReceiver extends BroadcastReceiver {
    public static final String action = "com.quduquxie.receiver.CLICK_DOWN_BOOK";

    @Override
    public void onReceive(Context ctt, Intent paramIntent) {
        boolean isStart = false;
        if (paramIntent != null) {
            ActivityManager am = (ActivityManager) ctt.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningTaskInfo taskInfo : am.getRunningTasks(Integer.MAX_VALUE)) {
                if (MainActivity.class.getName().equals(taskInfo.baseActivity.getClassName())) {
                    isStart = true;
                    break;
                }
            }
            if (!isStart) {
                String book_ids = paramIntent.getStringExtra("id_book");
                BookDaoHelper mBookDaoHelper = BookDaoHelper.getInstance(ctt);
                if (mBookDaoHelper != null && mBookDaoHelper.subscribeBook(book_ids)) {
                    Intent intent = new Intent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setClass(ctt, ReadingActivity.class);
                    if (!TextUtils.isEmpty(book_ids)) {
                        Book book = BookDaoHelper.getInstance(ctt).loadBook(book_ids, 0);
                        Bundle bundle = new Bundle();
                        bundle.putInt("sequence", book.sequence);
                        bundle.putInt("offset", book.offset);
                        bundle.putSerializable("book", book);
                        intent.putExtras(bundle);
                        ctt.startActivity(intent);
                    }
                }
            } else {
                String book_ids = paramIntent.getStringExtra("id_book");
                BookDaoHelper mBookDaoHelper = BookDaoHelper.getInstance(ctt);
                if (mBookDaoHelper != null && mBookDaoHelper.subscribeBook(book_ids)) {
                    Intent intent = new Intent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setClass(ctt, ReadingActivity.class);
                    if (!TextUtils.isEmpty(book_ids)) {
                        Book book = BookDaoHelper.getInstance(ctt).loadBook(book_ids, 0);
                        Bundle bundle = new Bundle();
                        bundle.putInt("sequence", book.sequence);
                        bundle.putInt("offset", book.offset);
                        bundle.putSerializable("book", book);
                        intent.putExtras(bundle);
                        ctt.startActivity(intent);
                    }
                } else {
                    Toast.makeText(ctt, "资源已删除", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }
}
