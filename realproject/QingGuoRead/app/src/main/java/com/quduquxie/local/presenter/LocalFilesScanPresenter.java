package com.quduquxie.local.presenter;

import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import com.quduquxie.base.database.helper.BookDaoHelper;
import com.quduquxie.bean.ScanFile;
import com.quduquxie.local.LocalFilesScanInterface;
import com.quduquxie.local.tasks.ImportLocalBookTask;
import com.quduquxie.util.MD5;
import com.quduquxie.util.QGLog;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

public class LocalFilesScanPresenter implements LocalFilesScanInterface.Presenter {

    private static final String TAG = LocalFilesScanPresenter.class.getSimpleName();

    private LocalFilesScanInterface.View localFilesScanView;
    private WeakReference<Context> contextReference;
    private UIHandler uiHandler;

    private HashMap<String, String> scanFiles = new HashMap<>();

    public LocalFilesScanPresenter(@NonNull LocalFilesScanInterface.View localFilesScanView, Context context) {
        this.localFilesScanView = localFilesScanView;
        this.localFilesScanView.setPresenter(this);
        this.contextReference = new WeakReference<>(context);
        this.uiHandler = new UIHandler();
    }

    @Override
    public void init() {

    }

    @Override
    public void initParameter() {
        if (scanFiles == null) {
            scanFiles = new HashMap<>();
        }

        scanFiles.clear();

        Cursor cursor = contextReference.get().getContentResolver().query(MediaStore.Files.getContentUri("external"), new String[]{MediaStore.Files.FileColumns.DATA, MediaStore.Files.FileColumns.TITLE, MediaStore.Files.FileColumns.SIZE, MediaStore.Files.FileColumns.DATE_ADDED}, MediaStore.Files.FileColumns.MIME_TYPE + "= ?", new String[]{MimeTypeMap.getSingleton().getMimeTypeFromExtension("txt")}, MediaStore.Files.FileColumns.DATE_ADDED + " DESC");
        if (cursor != null) {
            while (cursor.moveToNext()) {
                if (Long.valueOf(cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.SIZE))) > 2 * 1024) {
                    localFileScannedItem(cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA)), cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.TITLE)));
                    QGLog.e("initParameter", "initParameter: " + cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA)));
                }
            }
        } else {
            localFilesScanView.showEmptyView();
        }

        if (cursor != null) {
            cursor.close();
        }
    }

    @Override
    public void insertSelectedBooks(ArrayList<ScanFile> selectedFileList) {
        BookDaoHelper bookDaoHelper = BookDaoHelper.getInstance(contextReference.get());
        ImportLocalBookTask importLocalBookTask = new ImportLocalBookTask(selectedFileList, contextReference.get(), uiHandler, bookDaoHelper);
        importLocalBookTask.execute();
    }

    @Override
    public void localFileScannedItem(String filePath, String fileName) {
        if (!TextUtils.isEmpty(filePath) && !scanFiles.containsKey(filePath)) {

            scanFiles.put(filePath, fileName);

            localFilesScanView.refreshViewAdapter(transformationToScanFile(filePath, fileName));
            localFilesScanView.refreshSelectAllState(true);

        }
    }

    public ScanFile transformationToScanFile(String filePath, String fileName) {
        ScanFile scanFile = new ScanFile();
        scanFile.setId(MD5.encodeMD5String(filePath));
        scanFile.setName(fileName);
        scanFile.setFile(new File(filePath));
        scanFile.setSortCollection("@");
        scanFile.setChecked(false);
        return scanFile;
    }

    private class UIHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            switch (message.what) {
                case ImportLocalBookTask.START_INSERT_BOOK:
                    QGLog.e(TAG, "StartInsertBook");
                    localFilesScanView.showImportView();
                    break;
                case ImportLocalBookTask.REFRESH_IMPORT_VIEW:
                    localFilesScanView.refreshImportView((String) message.obj);
                    QGLog.e(TAG, "RefreshImportView");
                    break;
                case ImportLocalBookTask.COMPLETE_INSERT_BOOK:
                    localFilesScanView.completeImport((String) message.obj);
                    QGLog.e(TAG, "CompleteInsertBook");
                    break;
            }
        }
    }
}
