package com.quduquxie.local.view;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.quduquxie.R;
import com.quduquxie.base.database.helper.BookDaoHelper;
import com.quduquxie.bean.ScanFile;
import com.quduquxie.local.LocalFilesScanInterface;
import com.quduquxie.local.adapter.ScanFileItemAdapter;
import com.quduquxie.local.presenter.LocalFilesScanPresenter;
import com.quduquxie.util.BaseAsyncTask;
import com.quduquxie.util.MD5;
import com.quduquxie.util.QGLog;
import com.quduquxie.communal.widget.CustomDialog;
import com.quduquxie.widget.LoadingPage;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

public class LocalFilesScanFragment extends Fragment implements LocalFilesScanInterface.View, View.OnClickListener, ScanFileItemAdapter.LocalFileItemClickListener {

    private static final String TAG = LocalFilesScanFragment.class.getSimpleName();

    private LocalFilesScanInterface.Presenter localFilesScanPresenter;
    private LinearLayoutManager linearLayoutManager;

    public RelativeLayout local_files_scan_view;
    public RelativeLayout local_files_scan_content;
    public RelativeLayout local_files_scan_empty;
    public RecyclerView local_files_scan;
    public TextView local_files_select_all;
    public TextView local_files_add_shelf;
    public TextView local_files_scanning_file;
    public TextView local_files_scan_empty_prompt;

    private ArrayList<ScanFile> scannedFileList;
    private ArrayList<ScanFile> selectedFileList;
    private ScanFileItemAdapter scanFileItemAdapter;
    private BookDaoHelper bookDaoHelper;

    private CustomDialog customDialog;
    private TextView local_book_import_prompt;

    public LoadingPage loadingPage;

    public int book_selected_count = 0;

    public ScanningAsyncTask scanningAsyncTask;

    public CustomDialog customScanFileDialog;
    public TextView scanning_files_count;
    public TextView scanning_files_txt_count;
    public TextView scanning_files_stop;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        localFilesScanPresenter = new LocalFilesScanPresenter(this, getContext());
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        scannedFileList = new ArrayList<>();
        selectedFileList = new ArrayList<>();
        book_selected_count = 0;

        bookDaoHelper = BookDaoHelper.getInstance(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_local_files_scan, container, false);
        local_files_scan_view = (RelativeLayout) view.findViewById(R.id.local_files_scan_view);
        local_files_scan_content = (RelativeLayout) view.findViewById(R.id.local_files_scan_content);
        local_files_scan = (RecyclerView) view.findViewById(R.id.local_files_scan);
        local_files_select_all = (TextView) view.findViewById(R.id.local_files_bottom_select_all);
        local_files_add_shelf = (TextView) view.findViewById(R.id.local_files_bottom_add_shelf);
        local_files_scanning_file = (TextView) view.findViewById(R.id.local_files_bottom_scanning_file);
        local_files_scan_empty = (RelativeLayout) view.findViewById(R.id.local_files_scan_empty);
        local_files_scan_empty_prompt = (TextView) view.findViewById(R.id.local_files_scan_empty_prompt);

        local_files_scan_content.setVisibility(View.VISIBLE);
        local_files_scan_empty.setVisibility(View.GONE);

        scanFileItemAdapter = new ScanFileItemAdapter(scannedFileList, getContext());
        scanFileItemAdapter.setLocalFileItemClickListener(this);

        if (local_files_scan != null) {
            local_files_scan.setItemAnimator(new DefaultItemAnimator());
            local_files_scan.setLayoutManager(linearLayoutManager);
            local_files_scan.setAdapter(scanFileItemAdapter);
        }

        initListener();

        initData();

        return view;
    }

    private void initListener() {
        if (local_files_select_all != null) {
            local_files_select_all.setOnClickListener(this);
        }

        if (local_files_add_shelf != null) {
            local_files_add_shelf.setOnClickListener(this);
        }

        if (local_files_scanning_file != null) {
            local_files_scanning_file.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.local_files_bottom_select_all:
                ScanFile scanFile;
                if ((scannedFileList.size() - book_selected_count) == selectedFileList.size()) {
                    for (int i = 0; i < scannedFileList.size(); i++) {
                        scanFile = scannedFileList.get(i);
                        if (bookDaoHelper.subscribeBook(scanFile.getId())) {
                            continue;
                        }
                        if (selectedFileList.contains(scanFile)) {
                            scanFile.setChecked(false);
                            selectedFileList.remove(scanFile);
                            refreshRecyclerViewItem(scanFile);
                        }
                    }
                } else {
                    for (int i = 0; i < scannedFileList.size(); i++) {
                        scanFile = scannedFileList.get(i);
                        if (bookDaoHelper.subscribeBook(scanFile.getId())) {
                            continue;
                        }
                        if (!selectedFileList.contains(scanFile)) {
                            scanFile.setChecked(true);
                            selectedFileList.add(scanFile);
                            refreshRecyclerViewItem(scanFile);
                        }
                    }
                }

                refreshAddShelfView();
                refreshSelectAllView();
                break;
            case R.id.local_files_bottom_add_shelf:
                if (selectedFileList.size() == 0) {
                    Toast.makeText(getContext(), "你需要选择TXT文件！", Toast.LENGTH_SHORT).show();
                } else {
                    localFilesScanPresenter.insertSelectedBooks(selectedFileList);
                }
                break;
            case R.id.local_files_bottom_scanning_file:
                scanningLocalFiles();
                break;
            case R.id.scanning_files_stop:
                if (scanningAsyncTask != null && !scanningAsyncTask.isCancelled()) {
                    scanningAsyncTask.cancel(true);
                    if (customScanFileDialog != null) {
                        customScanFileDialog.dismiss();
                    }
                }
                break;
        }
    }

    private void initData() {

        if (local_files_select_all != null) {
            local_files_select_all.setText("全选");
            local_files_select_all.setClickable(false);
        }

        if (local_files_add_shelf != null) {
            local_files_add_shelf.setText("加入书架（0）");
        }

        if (local_files_scanning_file != null) {
            local_files_scanning_file.setText("扫描");
        }

        scannedFileList.clear();
        selectedFileList.clear();
        book_selected_count = 0;

        localFilesScanPresenter.initParameter();
    }

    @Override
    public void onResume() {
        super.onResume();
        StatService.onResume(getContext());
    }

    @Override
    public void onPause() {
        super.onPause();
        StatService.onPause(getContext());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (scanningAsyncTask != null && !scanningAsyncTask.isCancelled()) {
            scanningAsyncTask.onCancelled();
        }
    }

    @Override
    public void setPresenter(LocalFilesScanInterface.Presenter localFilesScanPresenter) {
        this.localFilesScanPresenter = localFilesScanPresenter;
    }

    @Override
    public void refreshViewAdapter(ScanFile scanFile) {
        if (scanFile != null) {
            scannedFileList.add(scanFile);

            if (bookDaoHelper.subscribeBook(scanFile.getId())) {
                book_selected_count++;
            }

            if (scanFileItemAdapter != null) {
                scanFileItemAdapter.notifyDataSetChanged();
            } else {
                scanFileItemAdapter = new ScanFileItemAdapter(scannedFileList, getContext());
                scanFileItemAdapter.setLocalFileItemClickListener(this);
                local_files_scan.setAdapter(scanFileItemAdapter);
            }

            if (isShowEmptyView()) {
                hideEmptyView();
            }
        }
    }

    @Override
    public void showEmptyView() {

        if (local_files_scan_content != null) {
            local_files_scan_content.setVisibility(View.GONE);
        }

        if (local_files_scan_empty != null) {
            local_files_scan_empty.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideEmptyView() {

        if (local_files_scan_content != null) {
            local_files_scan_content.setVisibility(View.VISIBLE);
        }

        if (local_files_scan_empty != null) {
            local_files_scan_empty.setVisibility(View.GONE);
        }
    }

    @Override
    public void refreshSelectAllState(boolean clickAble) {
        if (local_files_select_all != null) {
            if (local_files_select_all.isClickable() != clickAble) {
                local_files_select_all.setClickable(clickAble);
            }
        }
    }

    @Override
    public void showImportView() {
        if (customDialog == null) {
            customDialog = new CustomDialog(getActivity(), R.layout.layout_local_book_import);
            local_book_import_prompt = (TextView) customDialog.findViewById(R.id.local_book_import_prompt);
        }
        local_book_import_prompt.setText("导入书籍中....");
        QGLog.e(TAG, "message: show");
        customDialog.show();
    }

    @Override
    public void refreshImportView(String message) {
        if (customDialog != null && local_book_import_prompt != null) {
            local_book_import_prompt.setText(message);
            QGLog.e(TAG, "message: " + message);
        } else {
            customDialog = new CustomDialog(getActivity(), R.layout.layout_local_book_import);
            local_book_import_prompt = (TextView) customDialog.findViewById(R.id.local_book_import_prompt);
            local_book_import_prompt.setText(message);
            QGLog.e(TAG, "message: " + message);
        }
    }

    @Override
    public void completeImport(String message) {
        if (customDialog != null && customDialog.isShowing()) {
            customDialog.dismiss();
        }
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        if (!getActivity().isFinishing()) {
            getActivity().finish();
        }
    }

    @Override
    public boolean isShowEmptyView() {
        return local_files_scan_content != null && local_files_scan_content.getVisibility() == View.GONE;
    }

    @Override
    public void localFileItemClick(ScanFile scanFile, int position) {
        if (scanFile != null && scanFile.getFile() != null) {
            File file = scanFile.getFile();

            String file_path = file.getAbsolutePath();
            if (bookDaoHelper.subscribeBook(file_path)) {
                return;
            }

            if (selectedFileList.contains(scanFile)) {
                scanFile.setChecked(false);
                selectedFileList.remove(scanFile);
                refreshRecyclerViewItem(scanFile);
            } else {
                selectedFileList.add(scanFile);
                scanFile.setChecked(true);
                refreshRecyclerViewItem(scanFile);
            }
            refreshSelectAllView();
            refreshAddShelfView();
        }
    }

    private void refreshRecyclerViewItem(ScanFile scanFile) {
        if (scanFile != null) {
            int position = scannedFileList.indexOf(scanFile);
            if (position != -1) {
                scannedFileList.remove(position);
                scannedFileList.add(position, scanFile);
                scanFileItemAdapter.notifyItemChanged(position);
            }
        }
    }

    private void refreshSelectAllView() {
        if (local_files_select_all != null) {
            if ((scannedFileList.size() - book_selected_count) == selectedFileList.size()) {
                local_files_select_all.setText("取消全选");
            } else {
                local_files_select_all.setText("全选");
            }
            local_files_select_all.setClickable(true);
        }
    }

    private void refreshAddShelfView() {
        if (local_files_add_shelf != null) {
            local_files_add_shelf.setText("加入书架（" + selectedFileList.size() + "）");
            local_files_add_shelf.setClickable(true);
        }
    }

    public void scanningLocalFiles() {

//        IntentFilter intentfilter = new IntentFilter(Intent.ACTION_MEDIA_SCANNER_FINISHED);
//        intentfilter.addAction(Intent.ACTION_MEDIA_SCANNER_STARTED);
//        intentfilter.addDataScheme("file");
//        if (scanLocalFileReceiver == null) {
//            scanLocalFileReceiver = new ScanLocalFileReceiver();
//            scanLocalFileReceiver.setOnScanLocalFileFinished(this);
//        }
//        getActivity().registerReceiver(scanLocalFileReceiver, intentfilter);
//        MediaScannerConnection.scanFile(getActivity(), new String[]{Environment.getExternalStorageDirectory().getAbsolutePath()}, new String[]{"text/plan"}, null);

        if (customScanFileDialog == null) {
            customScanFileDialog = new CustomDialog(getActivity(), R.layout.layout_local_files_scan_dialog, Gravity.BOTTOM);
            scanning_files_count = (TextView) customScanFileDialog.findViewById(R.id.scanning_files_count);
            scanning_files_txt_count = (TextView) customScanFileDialog.findViewById(R.id.scanning_files_txt_count);
            scanning_files_stop = (TextView) customScanFileDialog.findViewById(R.id.scanning_files_stop);
            scanning_files_stop.setOnClickListener(this);
        }

        scanning_files_count.setText(Html.fromHtml(String.format("<font color=\"#000000\">已扫描了</font><font color=\"#4D91D0\">%1$d</font><font color=\"#000000\">个文件</font>", 0)));
        scanning_files_txt_count.setText(Html.fromHtml(String.format("<font color=\"#000000\">新添加</font><font color=\"#4D91D0\">%1$d</font><font color=\"#000000\">个TXT文件</font>", 0)));

        customScanFileDialog.show();

        if (scanningAsyncTask == null) {
            scanningAsyncTask = new ScanningAsyncTask(getContext());
        }
        scanningAsyncTask.execute();
        keepScreenOn();

    }

    public class ScanningAsyncTask extends BaseAsyncTask<Object, Object, Object> {

        private Context context;
        private int totalCount;
        private int result_count = 0;
        private long bufferTime;

        private ArrayList<File> fileList = new ArrayList<>();

        private static final String file_filter = "[^@]+\\.(?i)(txt)$";

        public ScanningAsyncTask(Context context) {
            this.context = context;
        }


        private final FileFilter mFilter = new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if (pathname.isDirectory()) {
                    return true;
                }
                return pathname.getName().matches(file_filter);
            }
        };

        @Override
        protected Void doInBackground(Object... params) {

            filterFiles(Environment.getExternalStorageDirectory());

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            bufferTime = System.currentTimeMillis();
        }

        @Override
        protected void onProgressUpdate(Object... values) {
            super.onProgressUpdate(values);
            if (values.length != 0) {
                File file = (File) values[0];
                if (fileList.size() > 0) {
                    for (File item : fileList) {
                        if (getFileName(item.getAbsolutePath()).equals(getFileName(file.getAbsolutePath())) && item.length() == file.length()) {
                            return;
                        }
                    }
                }

                ScanFile scanFile = transformationToScanFile(file);
                if (!scannedFileList.contains(scanFile)) {
                    QGLog.e(TAG, "ScanningFile: " + file.getName() + " : " + file.length());
                    result_count++;
                    scannedFileList.add(0, scanFile);

                    if (scannedFileList.size() > 0) {
                        if (local_files_select_all != null) {
                            local_files_select_all.setText("全选");
                            local_files_select_all.setClickable(true);
                        }
                    }

                    scanFileItemAdapter.notifyDataSetChanged();
                }
                fileList.add(file);
            }
            scanning_files_count.setText(Html.fromHtml("<font color=\"#000000\">已扫描了</font><font color=\"#4D91D0\">" + totalCount + "</font><font color=\"#000000\">个文件</font>"));
            scanning_files_txt_count.setText(Html.fromHtml("<font color=\"#000000\">新添加</font><font color=\"#4D91D0\">" + result_count +"</font><font color=\"#000000\">个TXT文件</font>"));
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            if (result_count == 0) {
                String information = "暂时未发现新的TXT文件";
                Toast.makeText(this.context, information, Toast.LENGTH_SHORT).show();
            } else {
                String information = "为您新添加了" + result_count + "个TXT文件";
                Toast.makeText(this.context, information, Toast.LENGTH_SHORT).show();
            }
            scanningAsyncTask = null;
        }

        @Override
        protected void onPostExecute(Object result) {
            scanningAsyncTask = null;
            customScanFileDialog.dismiss();
            scanningAsyncTask = null;
            if (result_count == 0) {
                String information = "暂时未发现新的TXT文件";
                Toast.makeText(this.context, information, Toast.LENGTH_SHORT).show();
            } else {
                String information = "为您新添加了" + result_count + "个TXT文件";
                Toast.makeText(this.context, information, Toast.LENGTH_SHORT).show();
            }
            clearScreenOn();
        }

        private static final int kMaxLevelCount = 7;
        private static final int kFileMinSize = 50;
        private int levelCount = 0;

        private void filterFiles(File file) {
            if (isCancelled())
                return;
            File[] files = file.listFiles(mFilter);
            if (null == files) {
                return;
            }
            String[] array = file.list();
            if (null == array) {
                return;
            }
            totalCount += array.length;
            long tempTime = System.currentTimeMillis();
            if (tempTime - bufferTime > 200) {
                publishProgress();
                bufferTime = tempTime;
            }
            for (int i = 0; i < files.length; i++) {
                if (isCancelled())
                    return;
                if (files[i].isDirectory()) {
                    if (levelCount < kMaxLevelCount) {
                        ++levelCount;
                        filterFiles(files[i]);
                        --levelCount;
                    }
                } else {
                    if (!isCancelled()) {
                        if (getFileSize(files[i]) < kFileMinSize) {
                            continue;
                        }
                        publishProgress(files[i]);
                    }
                }
            }
        }

        private double getFileSize(File file) {
            return file.length() / 1024.0;
        }

        private String getExtensionName(String name) {
            if ((name != null) && (name.length() > 0)) {
                int dot = name.lastIndexOf('.');
                if ((dot > -1) && (dot < (name.length() - 1))) {
                    return name.substring(dot + 1);
                }
            }
            return name;
        }

        public String getFileName(String name) {
            if (!TextUtils.isEmpty(name)) {
                int index = name.lastIndexOf("/");
                if (index > -1 && index < name.length() - 1) {
                    return name.substring(index + 1);
                }
            }
            return name;
        }
    }

    private void keepScreenOn() {
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void clearScreenOn() {
        if (getActivity() != null && getActivity().getWindow() != null) {
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    public ScanFile transformationToScanFile(File file) {
        ScanFile scanFile = new ScanFile();
        scanFile.setId(MD5.encodeMD5String(file.getAbsolutePath()));
        scanFile.setName(file.getName());
        scanFile.setFile(file);
        scanFile.setSortCollection("@");
        scanFile.setChecked(false);
        return scanFile;
    }

}
