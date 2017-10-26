package com.quduquxie.base.widget.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.quduquxie.R;

import java.text.MessageFormat;

/**
 * Created on 17/5/9.
 * Created by crazylei.
 */

public class CustomDialogUpdateFragment extends DialogFragment implements View.OnClickListener {
    public TextView update_version;
    public TextView update_description;

    public TextView update_cancel;
    public TextView update_confirm;

    private String version;
    private String description;

    private int layoutWidth;

    private CustomDialogUpdateListener customDialogUpdateListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialogFragment);

        this.layoutWidth = this.getResources().getDimensionPixelOffset(R.dimen.width_600);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_dialog_update_application, container, false);

        update_version = (TextView) view.findViewById(R.id.update_version);
        update_description = (TextView) view.findViewById(R.id.update_description);
        update_cancel = (TextView) view.findViewById(R.id.update_cancel);
        update_confirm = (TextView) view.findViewById(R.id.update_confirm);

        update_cancel.setOnClickListener(this);
        update_confirm.setOnClickListener(this);

        if (!TextUtils.isEmpty(version)) {
            update_version.setText(MessageFormat.format("现版本：{0}", version));
        }

        if (!TextUtils.isEmpty(description)) {
            update_description.setText(description);
        }

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
            case R.id.update_cancel:
                if (customDialogUpdateListener != null) {
                    customDialogUpdateListener.onCancelClicked();
                }
                break;
            case R.id.update_confirm:
                if (customDialogUpdateListener != null) {
                    customDialogUpdateListener.onConfirmClicked();
                }
                break;
        }
    }

    public void setCustomDialogUpdateListener(CustomDialogUpdateListener customDialogUpdateListener) {
        this.customDialogUpdateListener = customDialogUpdateListener;
    }

    public void setVersion(String version) {
        if (!TextUtils.isEmpty(version)) {
            this.version = version;
            if (update_version != null) {
                update_version.setText(MessageFormat.format("现版本：{0}", version));
            }
        }
    }

    public void setDescription(String description) {
        if (!TextUtils.isEmpty(description)) {
            this.description = description;
            if (update_description != null) {
                update_description.setText(description);
            }
        }
    }
}