package com.quduquxie.local.presenter;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;

import com.quduquxie.Constants;
import com.quduquxie.base.database.helper.BookDaoHelper;
import com.quduquxie.bean.ScanFile;
import com.quduquxie.local.LocalFilesDirectoryInterface;
import com.quduquxie.local.tasks.ImportLocalBookTask;
import com.quduquxie.local.widget.FileDirectoryView;
import com.quduquxie.util.CharacterParser;
import com.quduquxie.util.MD5;
import com.quduquxie.util.QGLog;
import com.quduquxie.util.SharedPreferencesUtils;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LocalFilesDirectoryPresenter implements LocalFilesDirectoryInterface.Presenter, View.OnClickListener {

    private static final String TAG = LocalFilesDirectoryPresenter.class.getSimpleName();

    private LocalFilesDirectoryInterface.View localFilesDirectoryView;
    private WeakReference<Context> contextReference;
    private SharedPreferencesUtils sharedPreferencesUtils;
    private String[] suffixes = {".txt"};

    private CharacterParser characterParser;
    private PinyinComparator pinyinComparator;

    private ArrayList<File> localFilesList;
    private ArrayList<String> localFilesDirector;
    private List<ScanFile> scannedFileList;
    private int book_count = 0;
    private UIHandler uiHandler;

    public LocalFilesDirectoryPresenter(@NonNull LocalFilesDirectoryInterface.View localFilesDirectoryView, Context context) {
        this.localFilesDirectoryView = localFilesDirectoryView;
        this.localFilesDirectoryView.setPresenter(this);
        this.contextReference = new WeakReference<>(context);
        this.sharedPreferencesUtils = new SharedPreferencesUtils(PreferenceManager.getDefaultSharedPreferences(context));
        this.uiHandler = new UIHandler();
    }

    @Override
    public void init() {

    }

    @Override
    public void initParameter() {

        if (localFilesList == null) {
            localFilesList = new ArrayList<>();
        }
        localFilesList.clear();

        if (localFilesDirector == null) {
            localFilesDirector = new ArrayList<>();
        }
        localFilesDirector.clear();

        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();

        String local_file_path = sharedPreferencesUtils.getString(Constants.LOCAL_FILE_PATH);

        if (!TextUtils.isEmpty(local_file_path)) {
            File currentFile = new File(local_file_path);
            if (currentFile.exists()) {
                localFilesDirectoryView.initFilePath(currentFile);
            }
        }
    }

    @Override
    public void initLocalFiles(File currentFile) {

        if (null == currentFile) {

            boolean SDCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED); //判断sd卡是否存在
            if (SDCardExist) {
                currentFile = Environment.getExternalStorageDirectory();//获取跟目录
            } else {
                currentFile = new File("/storage/emulated/0");
            }
        }
        QGLog.e("initLocalFiles", "initLocalFiles: " + currentFile.getAbsolutePath());
        saveCurrentFilePath(currentFile);

        this.localFilesDirectoryView.initFileDirectorName(initFileDirector(currentFile.getAbsolutePath()));

        clearDisabledData();

        if (currentFile.listFiles() != null) {
            File[] fileList = currentFile.listFiles();
            if (fileList.length != 0) {
                for (File file : fileList) {
                    if (file.isHidden()) {
                        continue;
                    }
                    if (checkFileIsBook(file)) {
                        book_count++;
                        localFilesList.add(file);
                        localFilesDirector.add(file.getName());
                    }
                    if (isDirectoryFile(file)) {
                        localFilesList.add(file);
                        localFilesDirector.add(file.getName());
                    }
                }
            }
        }

        scannedFileList = initScanFileList();
        Collections.sort(scannedFileList, pinyinComparator);
        this.localFilesDirectoryView.refreshFileList(scannedFileList);
        isShowEmptyView();
    }

    @Override
    public void insertSelectedBooks(ArrayList<ScanFile> selectedFileList) {
        ImportLocalBookTask importLocalBookTask = new ImportLocalBookTask(selectedFileList, contextReference.get(), uiHandler, BookDaoHelper.getInstance(contextReference.get()));
        importLocalBookTask.execute();
    }

    /**
     * 根据后缀名判断是否是Book
     **/
    @Override
    public boolean checkFileIsBook(File file) {
        String fileName = file.getName().toLowerCase();
        for (String suffix : suffixes) {
            if (fileName.endsWith(suffix)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void saveCurrentFilePath(File currentFile) {
        sharedPreferencesUtils.putString(Constants.LOCAL_FILE_PATH, currentFile.getAbsolutePath());
    }

    private ArrayList<FileDirectoryView> initFileDirector(String filePath) {

        ArrayList<FileDirectoryView> fileDirectoryViews = new ArrayList<>();

        StringBuilder stringBuilder = new StringBuilder();

        String current_file_director = filePath;

        QGLog.e(TAG, "initFileDirector: " + current_file_director);
        if (current_file_director.startsWith("/storage/emulated/0")) {
            stringBuilder.append(current_file_director.substring(0, 19));

            //添加Item
            FileDirectoryView fileDirectoryView = new FileDirectoryView(contextReference.get());
            fileDirectoryView.setDirectoryPath("本地", stringBuilder.toString());
            fileDirectoryView.setOnClickListener(this);

            fileDirectoryViews.add(fileDirectoryView);

            current_file_director = current_file_director.substring(19, current_file_director.length());
        }

        if (current_file_director.startsWith("/")) {
            current_file_director = current_file_director.substring(1, current_file_director.length());
        }

        if (!TextUtils.isEmpty(current_file_director)) {
            String[] path = current_file_director.split("/");
            for (int i = 0; i < path.length; i++) {
                QGLog.e(TAG, "FilePath: " + path[i]);

                stringBuilder.append("/").append(path[i]);

                //添加Item
                FileDirectoryView fileDirectoryView = new FileDirectoryView(contextReference.get());
                fileDirectoryView.setDirectoryPath(path[i], stringBuilder.toString());
                fileDirectoryView.setOnClickListener(this);

                fileDirectoryViews.add(fileDirectoryView);
            }
        }
        return fileDirectoryViews;
    }

    @Override
    public void onClick(View view) {
        FileDirectoryView fileDirectoryView = (FileDirectoryView) view;
        String directory_path = fileDirectoryView.getDirectoryPath();

        if (!TextUtils.isEmpty(directory_path)) {
            File currentFile = new File(directory_path);
            if (currentFile.exists()) {
                initLocalFiles(currentFile);
            }
        } else {
            initLocalFiles(null);
        }
    }

    private void clearDisabledData() {
        if (localFilesList != null) {
            localFilesList.clear();
        } else {
            localFilesList = new ArrayList<>();
        }

        if (localFilesDirector != null) {
            localFilesDirector.clear();
        } else {
            localFilesDirector = new ArrayList<>();
        }

        book_count = 0;
    }

    /**
     * 获取文件长度
     **/
    private double getBookSize(File file) {
        return file.length() / 1024.0;
    }

    /**
     * 判断文件是否是目录
     **/
    private boolean isDirectoryFile(File file) {
        return file.isDirectory();
    }

    private List<ScanFile> initScanFileList() {
        List<ScanFile> scanFileList = new ArrayList<>();
        for (int i = 0; i < book_count; i++) {
            ScanFile file = new ScanFile();
            file.setId(MD5.encodeMD5String(localFilesList.get(i).getAbsolutePath()));
            file.setName(localFilesDirector.get(i));
            file.setFile(localFilesList.get(i));
            file.setSortCollection("@");
            file.setChecked(false);
            scanFileList.add(file);
        }
        for (int j = book_count; j < localFilesDirector.size(); j++) {
            ScanFile file = new ScanFile();
            file.setId(MD5.encodeMD5String(localFilesList.get(j).getAbsolutePath()));
            file.setName(localFilesDirector.get(j));
            file.setFile(localFilesList.get(j));
            file.setChecked(false);
            String pinyin = characterParser.getSelling(localFilesDirector.get(j));
            String sortString = pinyin.substring(0, 1).toUpperCase();
            // 判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                file.setSortCollection(sortString.toUpperCase());
            } else {
                file.setSortCollection("?");
            }
            scanFileList.add(file);
        }
        return scanFileList;
    }

    /**
     * 根据拼音排序
     **/
    private class PinyinComparator implements Comparator<ScanFile> {

        @Override
        public int compare(ScanFile scanFile, ScanFile scanFileObject) {
            if (scanFile.getSortCollection().equals("@") || scanFileObject.getSortCollection().equals("#")) {
                return -1;
            } else if (scanFile.getSortCollection().equals("#") || scanFileObject.getSortCollection().equals("@")) {
                return 1;
            } else {
                return scanFile.getSortCollection().compareTo(scanFileObject.getSortCollection());
            }
        }
    }

    private void isShowEmptyView() {
        if (scannedFileList == null || scannedFileList.size() == 0) {
            this.localFilesDirectoryView.showEmptyView();
        } else {
            this.localFilesDirectoryView.hideEmptyView();
            this.localFilesDirectoryView.resetViewState(book_count);
        }
    }

    private class UIHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            switch (message.what) {
                case ImportLocalBookTask.START_INSERT_BOOK:
                    QGLog.e(TAG, "StartInsertBook");
                    localFilesDirectoryView.showImportView();
                    break;
                case ImportLocalBookTask.REFRESH_IMPORT_VIEW:
                    localFilesDirectoryView.refreshImportView((String) message.obj);
                    QGLog.e(TAG, "RefreshImportView");
                    break;
                case ImportLocalBookTask.COMPLETE_INSERT_BOOK:
                    localFilesDirectoryView.completeImport((String) message.obj);
                    QGLog.e(TAG, "CompleteInsertBook");
                    break;
            }
        }
    }
}
