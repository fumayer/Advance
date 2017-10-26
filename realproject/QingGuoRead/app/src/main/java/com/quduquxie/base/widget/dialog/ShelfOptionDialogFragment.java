package com.quduquxie.base.widget.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quduquxie.R;
import com.quduquxie.base.config.BaseConfig;

/**
 * Created on 17/7/18.
 * Created by crazylei.
 */

public class ShelfOptionDialogFragment extends DialogFragment implements View.OnClickListener{

    public RelativeLayout option_wifi_transport;
    public RelativeLayout option_import_local;
    public RelativeLayout option_download_manager;
    public RelativeLayout option_display_mode;

    public ImageView option_display_mode_flag;
    public TextView option_display_mode_content;

    public TextView option_cancel;

    private int layoutWidth;

    private ShelfOptionDialogListener shelfOptionDialogListener;

    private int mode = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialogFragment);

        this.layoutWidth = this.getResources().getDimensionPixelOffset(R.dimen.width_690);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_dialog_shelf_option, container, false);

        option_cancel = (TextView) view.findViewById(R.id.option_cancel);
        option_import_local = (RelativeLayout) view.findViewById(R.id.option_import_local);
        option_display_mode = (RelativeLayout) view.findViewById(R.id.option_display_mode);
        option_wifi_transport = (RelativeLayout) view.findViewById(R.id.option_wifi_transport);
        option_download_manager = (RelativeLayout) view.findViewById(R.id.option_download_manager);

        option_display_mode_flag = (ImageView) view.findViewById(R.id.option_display_mode_flag);
        option_display_mode_content = (TextView) view.findViewById(R.id.option_display_mode_content);

        if (mode != 0) {
            resetDisplayMode(mode);
        }

        option_cancel.setOnClickListener(this);
        option_import_local.setOnClickListener(this);
        option_display_mode.setOnClickListener(this);
        option_wifi_transport.setOnClickListener(this);
        option_download_manager.setOnClickListener(this);

        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onResume() {
        if (getDialog() != null && getDialog().getWindow() != null) {
            WindowManager.LayoutParams layoutParams = getDialog().getWindow().getAttributes();
            layoutParams.width = layoutWidth;
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            layoutParams.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
            this.getDialog().getWindow().setAttributes(layoutParams);
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.option_cancel:
                if (shelfOptionDialogListener != null) {
                    shelfOptionDialogListener.cancelShelfOptionDialog();
                }
                break;
            case R.id.option_import_local:
                if (shelfOptionDialogListener != null) {
                    shelfOptionDialogListener.startLocalFilesActivity();
                }
                break;
            case R.id.option_display_mode:
                if (shelfOptionDialogListener != null) {
                    shelfOptionDialogListener.changeShelfDisplayMode();
                }
                break;
            case R.id.option_wifi_transport:
                if (shelfOptionDialogListener != null) {
                    shelfOptionDialogListener.startWiFiTransportActivity();
                }
                break;
            case R.id.option_download_manager:
                if (shelfOptionDialogListener != null) {
                    shelfOptionDialogListener.startDownloadManagerActivity();
                }
                break;
        }
    }

    public void setShelfOptionDialogListener(ShelfOptionDialogListener shelfOptionDialogListener) {
        this.shelfOptionDialogListener = shelfOptionDialogListener;
    }

    public void resetDisplayMode(int mode) {

        this.mode = mode;

        switch (mode) {
            case BaseConfig.SHELF_DISPLAY_GRID:
                if (option_display_mode_flag != null) {
                    option_display_mode_flag.setImageResource(R.drawable.selector_display_grid);
                }

                if (option_display_mode_content != null) {
                    option_display_mode_content.setText(R.string.shelf_grid_prompt);
                }
                break;
            case BaseConfig.SHELF_DISPLAY_SLIDE:
                if (option_display_mode_flag != null) {
                    option_display_mode_flag.setImageResource(R.drawable.selector_display_slide);
                }

                if (option_display_mode_content != null) {
                    option_display_mode_content.setText(R.string.shelf_slide_prompt);
                }
                break;
        }
    }
}