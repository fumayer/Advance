package com.quduquxie.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import com.quduquxie.Constants;
import com.quduquxie.application.QuApplication;
import com.quduquxie.base.bean.Book;
import com.quduquxie.base.bean.Chapter;
import com.quduquxie.base.database.dao.ChapterDao;
import com.quduquxie.base.database.helper.BookDaoHelper;
import com.quduquxie.modular.cover.view.CoverActivity;
import com.quduquxie.module.read.reading.view.ReadingActivity;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class BookHelper {

    public static final int CHAPTER_TYPE_EMPTY = 0;
    public static final int CHAPTER_TYPE_PIC = 1;
    public static final int CHAPTER_TYPE_WORD = 2;
    public static final int CHAPTER_CACHE_COUNT = 5;
    public static final int AUTO_SELECT_CHAPTER = 1;

    static String TAG = "BookHelper";

    public static void goToCoverOrRead(Context context, String id_book) {
        Intent intent = new Intent();
        intent.setClass(context, CoverActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("id", id_book);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    /**
     * 跳转小说封面或者小说阅读页
     * <p/>
     * ctx
     * book
     */
    public static void goToCoverOrRead(Context context, Activity activity, Book book, boolean isBookShelf) {

        switch (book.book_type) {
            case Book.TYPE_ONLINE:
                int chapter_count;

                if (isChapterDBExist(context, book.id)) {
                    chapter_count = new ChapterDao(context, book.id).loadChapterCount();
                } else {
                    chapter_count = 0;
                }
                if ((chapter_count > 0 || book.sequence > -1 || book.read == 1 || isBookShelf) && BookDaoHelper.getInstance(context).subscribeBook(book.id)) {
                    startReading(context, activity, book);
                } else {
                    Intent intent = new Intent();
                    intent.setClass(context, CoverActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("id_book", book.id);
                    intent.putExtras(bundle);
                    activity.startActivity(intent);
                }
                break;
            case Book.TYPE_LOCAL_TXT:
                Intent intent = new Intent(context, ReadingActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("sequence", book.sequence);
                bundle.putInt("offset", book.offset);
                bundle.putSerializable("book", book);

                intent.putExtras(bundle);
                activity.startActivity(intent);
                break;

            default:
                break;
        }
    }

    public static void goToCoverOrRead(Context context, Activity activity, Book book) {
        goToCoverOrRead(context, activity, book, false);
    }

    private static void startReading(Context context, Activity activity, Book book) {
        Intent intent = new Intent(context, ReadingActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("sequence", book.sequence);
        bundle.putInt("offset", book.offset);
        bundle.putSerializable("book", book);
        intent.putExtras(bundle);
        QGLog.e("lq", "sequence:" + book.sequence + " offset:" + book.offset + " book:" + book + " chapterUpdate:" + book.chapters_update_state);
        activity.startActivity(intent);
    }

    /**
     * 判断目录数据库是否存在
     */
    public static boolean isChapterDBExist(Context context, String book_id) {
        return isDBExist(context, "book_chapter_" + book_id);
    }

    /**
     * 判断数据库是否存在
     * <p/>
     * context
     * name
     */
    public static boolean isDBExist(Context context, String name) {
        try {
            File file = context.getDatabasePath(name);
            if (file != null && file.exists()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 判断章节状态
     * <p/>
     * chapter
     */
    public static int getChapterType(Chapter chapter) {
        if (!TextUtils.isEmpty(chapter.content)) {
            return CHAPTER_TYPE_WORD;
        }
        return CHAPTER_TYPE_EMPTY;
    }


    public static boolean removeChapterCacheFile(String book_id) {
        String filePath = Constants.APP_PATH_BOOK + book_id;
        String newFilePath = Constants.APP_PATH_BOOK + book_id + ".delete";
        File srcfile = new File(filePath);
        int count = 0;
        if (srcfile != null && srcfile.exists()) {
            File newfile = new File(newFilePath);
            try {
                if (srcfile.listFiles() != null && srcfile.listFiles().length > 0) {// 灰度修改非空判断
                    File file = srcfile.listFiles()[0];
                    if (file != null) {
                        FileOutputStream fos = new FileOutputStream(file);
                        fos.close();
                    }
                }

                while (true) {
                    if (newfile.exists()) {
                        newfile = new File(Constants.APP_PATH_BOOK + book_id + "." + count++ + ".delete");
                    } else {
                        break;
                    }
                }

                // 重命名不成功 执行删除
                if (!srcfile.renameTo(newfile)) {
                    if (!srcfile.delete()) {
                        QGLog.e(TAG, newfile + " delete failure");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                QGLog.e(TAG, newfile + " delete failure");
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return true;
        }
        return false;
    }


    /**
     * 根据目录清除无效缓存
     */
    public static void deleteChapterCache(String id_book, ArrayList<Chapter> chapters) {
        String filePath = Constants.APP_PATH_BOOK + id_book + "/";
        String[] fileList = new File(filePath).list();

        if (fileList != null && fileList.length > 0) {
            File file;
            String name;
            Chapter chapter = new Chapter();
            for (String fileName : fileList) {
                name = fileName.substring(0, fileName.lastIndexOf("."));
                chapter.id = name;
                if (!chapters.contains(chapter)) {
                    file = new File(filePath + "/" + fileName);
                    if (file.exists()) {
                        file.delete();
                    }
                }
            }
        }
    }

    public static void removeMiPushNotification() {
        MiPushClient.clearNotification(QuApplication.getInstance(), 0);
    }
}
