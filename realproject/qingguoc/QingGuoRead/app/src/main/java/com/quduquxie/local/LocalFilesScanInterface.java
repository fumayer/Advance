package com.quduquxie.local;

import com.quduquxie.BasePresenter;
import com.quduquxie.BaseView;
import com.quduquxie.bean.ScanFile;

import java.util.ArrayList;

public interface LocalFilesScanInterface {

    interface Presenter extends BasePresenter {

        void initParameter();

        void insertSelectedBooks(ArrayList<ScanFile> selectedFileList);

        void localFileScannedItem(String filePath, String fileName);
    }

    interface View extends BaseView<Presenter> {

        void refreshViewAdapter(ScanFile scanFile);

        void showEmptyView();

        void hideEmptyView();

        void refreshSelectAllState(boolean clickAble);

        void showImportView();

        void refreshImportView(String message);

        void completeImport(String message);

        boolean isShowEmptyView();
    }
}
