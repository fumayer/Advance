package com.quduquxie.service.download;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.quduquxie.Constants;
import com.quduquxie.application.QuApplication;
import com.quduquxie.base.bean.Book;
import com.quduquxie.base.bean.Chapter;
import com.quduquxie.base.database.dao.ChapterDao;

import java.io.File;
import java.util.ArrayList;

/**
 * Created on 17/4/20.
 * Created by crazylei.
 */

public class DownloadServiceUtil {

    private static final String DOWN_INDEX = "down_index_2";

    public static void addDownloadBookTask(Context context, Book book, boolean fromStartIndex) {

        QuApplication application = QuApplication.getInstance();
        DownloadService downloadService = application.getDownloadService();

        if (downloadService != null) {
            int firstDownIndex = downloadStartIndex(context, book.id);
            int count = downloadCacheNumber(book, firstDownIndex);
            DownloadState downloadState = initDownloadState(book, count);
            DownloadTask downloadTask = loadDownloadTask(book, downloadState, fromStartIndex);
            if (firstDownIndex > 0) {
                downloadTask.start = firstDownIndex;
            }
            downloadService.addDownloadTask(downloadTask);
        } else {
            Toast.makeText(context, "启动缓存服务失败", Toast.LENGTH_SHORT).show();
        }
    }

    public static void startDownloadBookTask(DownloadService downloadService, Context context, Book book, int startDownIndex) {
        if (downloadService != null) {
            downloadService.startDownloadTask(book.id, startDownIndex);
        } else {
            Toast.makeText(context, "启动缓存服务失败", Toast.LENGTH_SHORT).show();
        }
    }

    public static void writeDownloadIndex(Context context, String id, int downIndex) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(DOWN_INDEX, Context.MODE_PRIVATE);
        if (downIndex < 0) {
            downIndex = 0;
        }
        sharedPreferences.edit().putInt(String.valueOf(id), downIndex).apply();
    }

    public static int downloadStartIndex(Context context, String id) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(DOWN_INDEX, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(id, -1);
    }

    public static int downloadCacheNumber(Book book, int index) {
        int count = downloadCacheCount(book.id, index, book.chapter.sn);
        if (index >= -1) {
            return (count + index);
        } else {
            return -1;
        }
    }

    public static int loadDownloadStartIndex(Context context, Book book) {
        int index = downloadStartIndex(context, book.id);
        if (index > -1) {
            int disable_count = downloadDisableCount(context, book.id, index);
            int count = downloadCacheCount(book.id, index, book.chapter.sn);
            return (count + index + disable_count);
        } else {
            return -1;
        }
    }

    public static DownloadState initDownloadState(Book book, int count) {
        if (count > -1) {
            if (count >= book.chapter.sn) {
                return DownloadState.FINISH;
            }
            return DownloadState.PAUSED;
        } else {
            return DownloadState.NO_START;
        }
    }

    public static DownloadTask loadDownloadTask(Book book, DownloadState downloadState, boolean fromStart) {
        int end = book.chapter.sn - 1;
        int start = 0;
        if (!fromStart) {
            start = book.sequence > -1 ? book.sequence : 0;
        }
        return new DownloadTask(book, downloadState, start, end);
    }

    public static int downloadDisableCount(Context context, String id_book, int index) {
        ChapterDao chapterDao = new ChapterDao(context, id_book);
        ArrayList<Chapter> chapters = chapterDao.loadChapters();
        int count = 0;
        if (chapters == null || chapters.size() == 0) {
            return count;
        } else {
            for (int i = 0; i < chapters.size(); i++) {
                Chapter chapter = chapters.get(i);
                if (chapter.status.equals("disable")) {
                    count++;
                }
            }
            return count;
        }
    }

    public static int downloadCacheCount(String id, int fromIndex, int end) {
        String filePath = Constants.APP_PATH_BOOK + id + "/";
        String[] fileList = new File(filePath).list();
        int count = 0;
        if (fileList != null) {
            count = fileList.length;

            if (count > fromIndex) {
                if (count <= end) {
                    return count - fromIndex;
                } else {
                    return end - fromIndex;
                }

            } else {
                return 0;
            }
        }
        return 0;
    }

    public static void dealDownloadIndex(Context context, String id) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(DOWN_INDEX, Context.MODE_PRIVATE);
        sharedPreferences.edit().remove(id).apply();
    }

    public static void downloadServiceStart(Context context, String id) {
        Intent intent = new Intent(DownloadService.DOWNLOAD_SERVICE_START);
        intent.putExtra("id", id);
        context.sendBroadcast(intent);
    }

    public static void downloadServiceFailed(Context context, String id, int sequence) {
        Intent intent = new Intent(DownloadService.DOWNLOAD_SERVICE_FAILED);
        intent.putExtra("id", id);
        intent.putExtra("sequence", sequence);
        context.sendBroadcast(intent);
    }

    public static void downloadServiceRefresh(Context context, String id, int sequence) {
        Intent intent = new Intent(DownloadService.DOWNLOAD_SERVICE_REFRESH);
        intent.putExtra("id", id);
        intent.putExtra("sequence", sequence);
        context.sendBroadcast(intent);
    }

    public static void downloadServiceFinish(Context context, String id) {
        Intent intent = new Intent(DownloadService.DOWNLOAD_SERVICE_FINISH);
        intent.putExtra("id", id);
        context.sendBroadcast(intent);
    }

    public static void downloadServiceRefreshUI(Context context, String id) {
        Intent intent = new Intent(DownloadService.DOWNLOAD_SERVICE_REFRESH_UI);
        intent.putExtra("id", id);
        context.sendBroadcast(intent);
    }
}
