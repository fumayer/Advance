package com.quduquxie.local.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.quduquxie.R;
import com.quduquxie.base.database.helper.BookDaoHelper;
import com.quduquxie.bean.ScanFile;
import com.quduquxie.local.LocalFilesDirectoryInterface;
import com.quduquxie.local.adapter.LocalFileDirectoryAdapter;
import com.quduquxie.local.presenter.LocalFilesDirectoryPresenter;
import com.quduquxie.local.widget.FileDirectoryView;
import com.quduquxie.util.QGLog;
import com.quduquxie.communal.widget.CustomDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LocalFilesDirectoryFragment extends Fragment implements LocalFilesDirectoryInterface.View, LocalFileDirectoryAdapter.LocalFileItemClickListener, View.OnClickListener {

    private static final String TAG = LocalFilesDirectoryFragment.class.getSimpleName();

    private LocalFilesDirectoryPresenter localFileDirectorPresenter;

    private RelativeLayout local_files_directory_content;
    private RelativeLayout local_files_directory_empty;

    public LinearLayout local_files_directory_name;
    public RecyclerView local_files_directory;
    public TextView local_files_select_all;
    public TextView local_files_add_shelf;

    private LocalFileDirectoryAdapter localFileDirectoryAdapter;
    private LinearLayoutManager linearLayoutManager;

    private ArrayList<ScanFile> scannedFileList;
    private ArrayList<ScanFile> selectedFileList;

    private File currentFile;

    private BookDaoHelper bookDaoHelper;

    private int book_count = 0;

    private int book_selected_count = 0;

    private CustomDialog customDialog;
    private TextView local_book_import_prompt;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        localFileDirectorPresenter = new LocalFilesDirectoryPresenter(this, getContext());
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        scannedFileList = new ArrayList<>();
        selectedFileList = new ArrayList<>();

        bookDaoHelper = BookDaoHelper.getInstance(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_local_files_directory, container, false);

        local_files_directory_content = (RelativeLayout) view.findViewById(R.id.local_files_directory_content);
        local_files_directory_empty = (RelativeLayout) view.findViewById(R.id.local_files_directory_empty);
        local_files_directory_name = (LinearLayout) view.findViewById(R.id.local_files_directory_name);
        local_files_directory = (RecyclerView) view.findViewById(R.id.local_files_directory);
        local_files_select_all = (TextView) view.findViewById(R.id.local_files_bottom_select_all);
        local_files_add_shelf = (TextView) view.findViewById(R.id.local_files_bottom_add_shelf);

        local_files_directory_content.setVisibility(View.VISIBLE);
        local_files_directory_empty.setVisibility(View.GONE);

        initListener();

        initData();

        return view;
    }

    private void initData() {

        localFileDirectoryAdapter = new LocalFileDirectoryAdapter(scannedFileList, getContext());
        localFileDirectoryAdapter.setLocalFileItemClickListener(this);

        if (local_files_directory != null) {
            local_files_directory.setLayoutManager(linearLayoutManager);
            local_files_directory.setAdapter(localFileDirectoryAdapter);
        }

        localFileDirectorPresenter.initParameter();
        localFileDirectorPresenter.initLocalFiles(currentFile);
    }

    private void initListener() {
        if (local_files_select_all != null) {
            local_files_select_all.setOnClickListener(this);
        }

        if (local_files_add_shelf != null) {
            local_files_add_shelf.setOnClickListener(this);
        }
    }

    @Override
    public void localFileItemClick(ScanFile scanFile, int position) {
        if (scanFile != null && scanFile.getFile() != null) {
            File file = scanFile.getFile();
            if (localFileDirectorPresenter.checkFileIsBook(file)) {
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
                refreshAddShelfView();
                refreshSelectAllView();
            } else {
                localFileDirectorPresenter.initLocalFiles(scanFile.getFile());
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.local_files_bottom_select_all:
                ScanFile scanFile;

                if ((book_count - book_selected_count) == selectedFileList.size()) {
                    for (int i = 0; i < scannedFileList.size(); i++) {
                        scanFile = scannedFileList.get(i);
                        if (localFileDirectorPresenter.checkFileIsBook(scanFile.getFile())) {
                            if (bookDaoHelper.subscribeBook(scanFile.getId())) {
                                continue;
                            }
                            if (selectedFileList.contains(scanFile)) {
                                scanFile.setChecked(false);
                                selectedFileList.remove(scanFile);
                                refreshRecyclerViewItem(scanFile);
                            }
                        }
                    }
                } else {
                    for (int i = 0; i < scannedFileList.size(); i++) {
                        scanFile = scannedFileList.get(i);
                        if (localFileDirectorPresenter.checkFileIsBook(scanFile.getFile())) {
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
                }

                refreshAddShelfView();
                refreshSelectAllView();

                break;
            case R.id.local_files_bottom_add_shelf:
                if (selectedFileList.size() == 0) {
                    Toast.makeText(getContext(), "你需要选择TXT文件！", Toast.LENGTH_SHORT).show();
                    QGLog.e(TAG, "OnClick : local_files_add_shelf: " + selectedFileList.size());
                } else {
                    QGLog.e(TAG, "OnClick : local_files_add_shelf: " + selectedFileList.size());
                    localFileDirectorPresenter.insertSelectedBooks(selectedFileList);
                }
                break;
        }
    }

    private void refreshRecyclerViewItem(ScanFile scanFile) {
        if (scanFile != null) {
            int position = scannedFileList.indexOf(scanFile);
            if (position != -1) {
                scannedFileList.remove(position);
                scannedFileList.add(position, scanFile);
                localFileDirectoryAdapter.notifyItemChanged(position);
                refreshSelectAllView();
            }
        }
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
        localFileDirectorPresenter.initParameter();
    }

    @Override
    public void setPresenter(LocalFilesDirectoryInterface.Presenter localFileDirectorPresenter) {
        this.localFileDirectorPresenter = (LocalFilesDirectoryPresenter) localFileDirectorPresenter;
    }

    @Override
    public void initFilePath(File currentFile) {
        this.currentFile = currentFile;
    }

    @Override
    public void initFileDirectorName(ArrayList<FileDirectoryView> fileDirectoryViews) {
        local_files_directory_name.removeAllViews();
        for (int i = 0; i < fileDirectoryViews.size(); i++) {
            local_files_directory_name.addView(fileDirectoryViews.get(i));
        }
    }

    @Override
    public void refreshFileList(List<ScanFile> scannedFileList) {
        book_selected_count = 0;

        if (this.scannedFileList != null) {
            this.scannedFileList.clear();
        } else {
            this.scannedFileList = new ArrayList<>();
        }

        for (int i = 0; i < scannedFileList.size(); i++) {
            this.scannedFileList.add(scannedFileList.get(i));
            if (bookDaoHelper.subscribeBook(scannedFileList.get(i).getId())) {
                book_selected_count++;
            }
        }

        if (localFileDirectoryAdapter != null) {
            localFileDirectoryAdapter.notifyDataSetChanged();
        } else {
            localFileDirectoryAdapter = new LocalFileDirectoryAdapter(this.scannedFileList, getContext());
            localFileDirectoryAdapter.setLocalFileItemClickListener(this);
            local_files_directory.setAdapter(localFileDirectoryAdapter);
        }
    }

    @Override
    public void showEmptyView() {
        if (local_files_directory_content != null) {
            local_files_directory_content.setVisibility(View.GONE);
        }
        if (local_files_directory_empty != null) {
            local_files_directory_empty.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideEmptyView() {
        if (local_files_directory_content != null) {
            local_files_directory_content.setVisibility(View.VISIBLE);
        }
        if (local_files_directory_empty != null) {
            local_files_directory_empty.setVisibility(View.GONE);
        }
    }

    @Override
    public void resetViewState(int book_count) {

        this.book_count = book_count;

        if (selectedFileList != null) {
            selectedFileList.clear();
        } else {
            selectedFileList = new ArrayList<>();
        }

        if (local_files_select_all != null) {
            local_files_select_all.setText("全选");
            if ((book_count - book_selected_count) == 0) {
                local_files_select_all.setClickable(false);
            } else {
                local_files_select_all.setClickable(true);
            }
        }

        if (local_files_add_shelf != null) {
            local_files_add_shelf.setText("加入书架（0）");
            if ((book_count - book_selected_count) == 0) {
                local_files_add_shelf.setClickable(false);
            } else {
                local_files_add_shelf.setClickable(true);
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
        customDialog.show();
    }

    @Override
    public void refreshImportView(String message) {
        if (customDialog != null && local_book_import_prompt != null) {
            local_book_import_prompt.setText(message);
        } else {
            customDialog = new CustomDialog(getActivity(), R.layout.layout_local_book_import);
            local_book_import_prompt = (TextView) customDialog.findViewById(R.id.local_book_import_prompt);
            local_book_import_prompt.setText(message);
        }
    }

    @Override
    public void completeImport(String message) {
        if (customDialog != null) {
            customDialog.dismiss();
        }
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        if (!getActivity().isFinishing()) {
            getActivity().finish();
        }
    }

    public void refreshAddShelfView() {
        if (local_files_add_shelf != null) {
            local_files_add_shelf.setText("加入书架（" + selectedFileList.size() + "）");
            local_files_add_shelf.setClickable(true);
        }
    }

    public void refreshSelectAllView() {
        if (local_files_select_all != null) {
            if (book_count - book_selected_count == selectedFileList.size()) {
                local_files_select_all.setText("取消全选");
            } else {
                local_files_select_all.setText("全选");
            }
            local_files_select_all.setClickable(true);
        }
    }
}
