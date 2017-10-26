package com.quduquxie.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;

import com.quduquxie.Constants;

import com.quduquxie.base.bean.Book;
import com.quduquxie.base.database.helper.BookDaoHelper;
import com.quduquxie.service.check.CheckNovelUpdateHelper;
import com.quduquxie.service.check.CheckNovelUpdateService;

import java.util.Comparator;

public class FrameBookHelper {

    private Context context;

    private BookUpdateListener bookUpdateListener;

    private NotificationClickListener notificationClickListener;

    private CheckNovelUpdateService checkNovelUpdateService;

    private BookDaoHelper bookDaoHelper;

    public SharedPreferences sharedPreferences;

    public FrameBookHelper(Context context) {
        this.context = context;

        CheckNovelUpdateHelper.dealLocalNotification(context);

        DeleteBookHelper deleteBookHelper = new DeleteBookHelper(context);
        deleteBookHelper.startPendingService();

        if (bookDaoHelper == null) {
            bookDaoHelper = BookDaoHelper.getInstance(context);
        }
        if (sharedPreferences == null) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        }
    }

    public void initCheckNovelUpdateService() {
        Intent intent;
        if (checkNovelUpdateService == null) {
            try {
                intent = new Intent();
                intent.setClass(context, CheckNovelUpdateService.class);
                context.startService(intent);
                context.bindService(intent, checkNovelUpdateConnection, Context.BIND_AUTO_CREATE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private ServiceConnection checkNovelUpdateConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            try {
                checkNovelUpdateService = ((CheckNovelUpdateService.CheckNovelUpdateBinder) service).getService();
                if (checkNovelUpdateService != null && bookUpdateListener != null) {
                    bookUpdateListener.updateBook();
                }
            } catch (ClassCastException exception) {
                exception.printStackTrace();
            }
        }
    };

    public CheckNovelUpdateService getCheckNovelUpdateService() {
        return checkNovelUpdateService;
    }


    public void restoreState() {
        if (checkNovelUpdateService != null && checkNovelUpdateConnection != null) {
            context.unbindService(checkNovelUpdateConnection);
        }
    }

    /**
     * 对书籍列表进行多类型排序
     */
    public static class MultiComparator implements Comparator<Object> {
        @Override
        public int compare(Object bookObject_first, Object bookObject_second) {
            if (Constants.book_list_sort_type == 2) {
                return 0;
            } else {
                return ((Book) bookObject_first).sequence_time == ((Book) bookObject_second).sequence_time ? 0 : (((Book) bookObject_first).sequence_time < ((Book) bookObject_second).sequence_time ? 1 : -1);
            }
        }
    }

    /**
     * 对书籍列表按照阅读时间排序
     */
    public static class ReadTimeComparator implements Comparator<Object> {
        @Override
        public int compare(Object bookObject_first, Object bookObject_second) {
            return ((Book) bookObject_first).sequence_time == ((Book) bookObject_second).sequence_time ? 0 : (((Book) bookObject_first).sequence_time < ((Book) bookObject_second).sequence_time ? 1 : -1);
        }
    }

    public interface BookUpdateListener {
        void updateBook();
    }

    public void setBookUpdateListener(BookUpdateListener bookUpdateListener) {
        this.bookUpdateListener = bookUpdateListener;
    }


    public interface NotificationClickListener {
        void notificationClicked(String id);
    }

    public void setNotificationClickListener(NotificationClickListener notificationClickListener) {
        this.notificationClickListener = notificationClickListener;
    }

    public void clickedNotification(Intent intent) {
        if (intent != null) {
            if (intent.getBooleanExtra("notification", false)) {
                String id = intent.getStringExtra("id");
                if (notificationClickListener != null) {
                    notificationClickListener.notificationClicked(id);
                }
            }
        }
    }
}