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

/**
 * Created on 17/7/28.
 * Created by crazylei.
 */

public class CustomLoadingDialog extends DialogFragment {

    public TextView loading_prompt;

    private String prompt;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialogFragment);

        setCancelable(false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_dialog_loading, container, false);

        loading_prompt = (TextView) view.findViewById(R.id.loading_prompt);

        if (!TextUtils.isEmpty(prompt)) {
            loading_prompt.setText(prompt);
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
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
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

    public void setPrompt(String prompt) {
        if (!TextUtils.isEmpty(prompt)) {
            this.prompt = prompt;
            if (loading_prompt != null) {
                loading_prompt.setText(prompt);
            }
        }
    }
}