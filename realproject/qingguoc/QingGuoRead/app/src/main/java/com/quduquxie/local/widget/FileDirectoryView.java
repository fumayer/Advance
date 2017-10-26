package com.quduquxie.local.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quduquxie.R;

public class FileDirectoryView extends RelativeLayout {

    private String directory_path;

    private TextView file_directory_name;

    public FileDirectoryView(Context context) {
        super(context);
        initView(context);
    }

    public FileDirectoryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public FileDirectoryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FileDirectoryView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_file_directory, this, true);
        file_directory_name = (TextView) view.findViewById(R.id.file_directory_name);
    }

    public void setDirectoryPath(String directory_name, String directory_path) {
        this.directory_path = directory_path;
        this.file_directory_name.setText(directory_name);
    }

    public String getDirectoryPath() {
        return this.directory_path;
    }
}
