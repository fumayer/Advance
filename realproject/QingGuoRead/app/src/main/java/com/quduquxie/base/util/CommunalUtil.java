package com.quduquxie.base.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.quduquxie.Constants;
import com.quduquxie.base.bean.Book;
import com.quduquxie.base.database.dao.ChapterDao;
import com.quduquxie.base.database.helper.BookDaoHelper;
import com.quduquxie.modular.cover.view.CoverActivity;
import com.quduquxie.module.comment.view.CommentListActivity;
import com.quduquxie.module.read.reading.view.ReadingActivity;
import com.quduquxie.service.check.CheckNovelUpdateCallBack;
import com.quduquxie.service.check.CheckNovelUpdateTask;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

/**
 * Created on 17/7/14.
 * Created by crazylei.
 */

public class CommunalUtil {

    public static void formatNumber(TextView countView, TextView unitView, long count) {
        if (count > 10000) {
            if (unitView.getVisibility() != View.VISIBLE) {
                unitView.setVisibility(View.VISIBLE);
            }
            if (count > 100000000) {
                countView.setText(String.valueOf(count / 100000000));
                unitView.setText("亿+");
            } else {
                countView.setText(String.valueOf(count / 10000));
                unitView.setText("万+");
            }
        } else {
            unitView.setVisibility(View.GONE);
            countView.setText(String.valueOf(count));
        }
    }

    public static String formatNumber(long count) {
        String result;
        if (count > 10000) {
            if (count > 100000000) {
                result = (count / 100000000) + "亿+";
            } else {
                result = (count / 10000) + "万+";
            }
        } else {
            result = String.valueOf(count);
        }
        return result;
    }


    public static boolean checkVersionState(long time) {
        Date date = new Date();
        if (time >= 0) {
            long interval = date.getTime() - time;
            long hour = (interval / (60 * 60 * 1000));
            return hour > 24;
        }
        return false;
    }

    public static String compareTime(SimpleDateFormat formatter, long time) {
        String date;
        long interval = System.currentTimeMillis() - time;
        if (interval < 0) {
            date = "刚刚";
        } else {
            long day = interval / (24 * 60 * 60 * 1000);
            long hour = (interval / (60 * 60 * 1000) - day * 24);
            long minute = ((interval / (60 * 1000)) - day * 24 * 60 - hour * 60);

            if (day > 0) {
                if (day >= 1 && day <= 7) {
                    date = day + "天前";
                } else {
                    date = formatter.format(time);
                }
            } else if (hour > 0) {
                date = hour + "小时前";
            } else if (minute > 0) {
                date = minute + "分钟前";
            } else {
                date = "刚刚";
            }
        }
        return date;
    }

    public static boolean checkChapterDBExist(Context context, String id) {
        return checkFileExist(context, "book_chapter_" + id);
    }

    private static boolean checkFileExist(Context context, String name) {
        try {
            File file = context.getDatabasePath(name);
            if (file != null && file.exists()) {
                return true;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }

    public static boolean checkChapterExist(String id, String book_id) {
        String filePath = Constants.APP_PATH_BOOK + book_id + "/" + id + ".text";
        File file = new File(filePath);
        return file.exists();
    }


    public static void startCoverOrReading(Activity activity, Book book, boolean shelf) {
        if (book == null || TextUtils.isEmpty(book.id)) {
            return;
        }

        switch (book.book_type) {
            case Book.TYPE_ONLINE:
                int chapter_count;
                if (checkChapterDBExist(activity, book.id)) {
                    chapter_count = new ChapterDao(activity, book.id).loadChapterCount();
                } else {
                    chapter_count = 0;
                }
                if ((chapter_count > 0 || book.sequence > -1 || book.read == 1 || shelf) && BookDaoHelper.getInstance(activity).subscribeBook(book.id)) {
                    startReadingActivity(activity, book);
                } else {
                    startCoverActivity(activity, book);
                }
                break;
            case Book.TYPE_LOCAL_TXT:
                startReadingActivity(activity, book);
                break;
        }
    }

    public static void startReadingActivity(Activity activity, Book book) {
        Bundle bundle = new Bundle();
        bundle.putInt("sequence", book.sequence);
        bundle.putInt("offset", book.offset);
        bundle.putSerializable("book", book);

        Intent intent = new Intent();
        intent.setClass(activity, ReadingActivity.class);
        intent.putExtras(bundle);

        if (!activity.isFinishing()) {
            activity.startActivity(intent);
        }
    }

    public static void startCoverActivity(Activity activity, Book book) {
        Bundle bundle = new Bundle();
        bundle.putString("id", book.id);

        Intent intent = new Intent();
        intent.setClass(activity, CoverActivity.class);
        intent.putExtras(bundle);

        if (!activity.isFinishing()) {
            activity.startActivity(intent);
        }
    }

    public static void startCommentActivity(Activity activity, Book book) {
        if (book != null && !TextUtils.isEmpty(book.id)) {
            Intent intent = new Intent();
            intent.putExtra("book", book);
            intent.putExtra("id_book", book.id);
            intent.setClass(activity, CommentListActivity.class);

            if (!activity.isFinishing()) {
                activity.startActivity(intent);
            }
        }
    }

    public static CheckNovelUpdateTask initCheckNovelUpdateTask(ArrayList<Book> books, CheckNovelUpdateCallBack checkNovelUpdateCallBack) {
        CheckNovelUpdateTask checkNovelUpdateTask = new CheckNovelUpdateTask();
        checkNovelUpdateTask.checkUpdateBooks = books;
        checkNovelUpdateTask.checkNovelUpdateCallBack = checkNovelUpdateCallBack;
        checkNovelUpdateTask.from = CheckNovelUpdateTask.CheckNovelUpdateTaskFrom.FROM_BOOK_SHELF;
        return checkNovelUpdateTask;
    }

    public static class MultiComparator implements Comparator<Object> {
        @Override
        public int compare(Object first, Object second) {
            return ((Book) first).sequence_time == ((Book) second).sequence_time ? 0 : (((Book) first).sequence_time < ((Book) second).sequence_time ? 1 : -1);
        }
    }

    public static Book changeBookItemType(Book book, int type) {
        book.item_type = type;
        return book;
    }
}