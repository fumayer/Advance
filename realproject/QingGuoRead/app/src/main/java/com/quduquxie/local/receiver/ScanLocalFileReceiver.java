package com.quduquxie.local.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.MediaStore;
import android.webkit.MimeTypeMap;

import com.quduquxie.util.QGLog;

public class ScanLocalFileReceiver extends BroadcastReceiver {

    private ScanFileFinishedListener scanFileFinishedListener;

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        if (Intent.ACTION_MEDIA_SCANNER_STARTED.equals(action)) {
            QGLog.e("ScanLocalFileReceiver", "ScanLocalFileReceiver: ACTION_MEDIA_SCANNER_STARTED");
        } else if (Intent.ACTION_MEDIA_SCANNER_FINISHED.equals(action)) {
            QGLog.e("ScanLocalFileReceiver", "ScanLocalFileReceiver: ACTION_MEDIA_SCANNER_FINISHED");
            Cursor cursor = context.getContentResolver().query(MediaStore.Files.getContentUri("external"), new String[]{MediaStore.Files.FileColumns.DATA, MediaStore.Files.FileColumns.TITLE, MediaStore.Files.FileColumns.SIZE, MediaStore.Files.FileColumns.DATE_ADDED}, MediaStore.Files.FileColumns.MIME_TYPE + "= ?", new String[]{MimeTypeMap.getSingleton().getMimeTypeFromExtension("txt")}, MediaStore.Files.FileColumns.DATE_ADDED + " DESC");
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    if (scanFileFinishedListener != null) {
                        if (Long.valueOf(cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.SIZE))) > 2 * 1024) {
                            scanFileFinishedListener.localFileScannedItem(cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA)), cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.TITLE)));
                            QGLog.e("ScanLocalFileReceiver", "ScanLocalFileReceiver: " + cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA)));
                        }
                    }
                }
            }
            if (cursor != null) {
                cursor.close();
            }
            if (scanFileFinishedListener != null) {
                scanFileFinishedListener.localFileScannedFinish();
            }
        }
    }

    public void setOnScanLocalFileFinished(ScanFileFinishedListener scanFileFinishedListener) {
        this.scanFileFinishedListener = scanFileFinishedListener;
    }

    public interface ScanFileFinishedListener {

        void localFileScannedItem(String filePath, String fileName);

        void localFileScannedFinish();
    }
}
