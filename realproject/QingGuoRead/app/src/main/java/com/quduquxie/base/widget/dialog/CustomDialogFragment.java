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
 * Created on 17/4/25.
 * Created by crazylei.
 */

public class CustomDialogFragment extends DialogFragment implements View.OnClickListener {

    public TextView communal_prompt;
    public TextView communal_option_first;
    public TextView communal_option_second;

    private String prompt;
    private String optionFirst;
    private String optionSecond;

    private int layoutWidth;

    private CustomDialogListener customDialogListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialogFragment);

        this.layoutWidth = this.getResources().getDimensionPixelOffset(R.dimen.width_600);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_dialog_communal_fragment, container, false);

        communal_prompt = (TextView) view.findViewById(R.id.communal_prompt);
        communal_option_first = (TextView) view.findViewById(R.id.communal_option_first);
        communal_option_second = (TextView) view.findViewById(R.id.communal_option_second);

        communal_option_first.setOnClickListener(this);
        communal_option_second.setOnClickListener(this);

        if (!TextUtils.isEmpty(prompt)) {
            communal_prompt.setText(prompt);
        }

        if (!TextUtils.isEmpty(optionFirst)) {
            communal_option_first.setText(optionFirst);
        }

        if (!TextUtils.isEmpty(optionSecond)) {
            communal_option_second.setText(optionSecond);
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
            case R.id.communal_option_first:
                if (customDialogListener != null) {
                    customDialogListener.onFirstOptionClicked();
                }
                break;
            case R.id.communal_option_second:
                if (customDialogListener != null) {
                    customDialogListener.onSecondOptionClicked();
                }
                break;
        }
    }

    public void setCustomDialogListener(CustomDialogListener customDialogListener) {
        this.customDialogListener = customDialogListener;
    }

    public void setPrompt(String prompt) {
        if (!TextUtils.isEmpty(prompt)) {
            this.prompt = prompt;
            if (communal_prompt != null) {
                communal_prompt.setText(prompt);
            }
        }
    }

    public void setFirstOption(String optionFirst) {
        if (!TextUtils.isEmpty(optionFirst)) {
            this.optionFirst = optionFirst;
            if (communal_option_first != null) {
                communal_option_first.setText(optionFirst);
            }
        }
    }

    public void setSecondOption(String optionSecond) {
        if (!TextUtils.isEmpty(optionSecond)) {
            this.optionSecond = optionSecond;
            if (communal_option_second != null) {
                communal_option_second.setText(optionSecond);
            }
        }
    }
}