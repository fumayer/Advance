package com.quduquxie.local;

import com.quduquxie.BasePresenter;
import com.quduquxie.BaseView;
import com.quduquxie.bean.ScanFile;
import com.quduquxie.local.widget.FileDirectoryView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public interface LocalFilesDirectoryInterface {

    interface Presenter extends BasePresenter {

        void initParameter();

        void initLocalFiles(File currentFile);

        void insertSelectedBooks(ArrayList<ScanFile> selectedFileList);

        boolean checkFileIsBook(File file);

        void saveCurrentFilePath(File currentFile);
    }

    interface View extends BaseView<Presenter> {

        void initFilePath(File currentFile);

        void initFileDirectorName(ArrayList<FileDirectoryView> fileDirectoryViews);

        void refreshFileList(List<ScanFile> scannedFileList);

        void showEmptyView();

        void hideEmptyView();

        void resetViewState(int book_count);

        void showImportView();

        void refreshImportView(String message);

        void completeImport(String message);
    }
}
