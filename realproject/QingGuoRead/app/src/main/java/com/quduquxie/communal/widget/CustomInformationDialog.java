package com.quduquxie.communal.widget;

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

/**
 * Created on 17/3/10.
 * Created by crazylei.
 */

public class CustomInformationDialog extends DialogFragment implements View.OnClickListener {

    public TextView dialog_prompt;
    public TextView dialog_cancel;
    public TextView dialog_confirm;

    private String prompt;

    private int layoutWidth;

    private CustomInformationListener customInformationListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialogFragment);

        this.layoutWidth = this.getResources().getDimensionPixelOffset(R.dimen.width_600);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_dialog_information, container, false);

        dialog_prompt = (TextView) view.findViewById(R.id.dialog_prompt);
        dialog_cancel = (TextView) view.findViewById(R.id.dialog_cancel);
        dialog_confirm = (TextView) view.findViewById(R.id.dialog_confirm);

        dialog_prompt.setOnClickListener(this);
        dialog_cancel.setOnClickListener(this);
        dialog_confirm.setOnClickListener(this);

        if (!TextUtils.isEmpty(prompt)) {
            dialog_prompt.setText(prompt);
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
            case R.id.dialog_cancel:
                if (customInformationListener != null) {
                    customInformationListener.onCancelClicked();
                }
                break;
            case R.id.dialog_confirm:
                if (customInformationListener != null) {
                    customInformationListener.onConfirmClicked();
                }
                break;
        }
    }

    public void setPrompt(String prompt) {
        if (!TextUtils.isEmpty(prompt)) {
            this.prompt = prompt;
            if (dialog_prompt != null) {
                dialog_prompt.setText(prompt);
            }
        }
    }

    public void setInformationClickListener(CustomInformationListener customInformationListener) {
        this.customInformationListener = customInformationListener;
    }
}
