package com.quduquxie.communal.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
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

public class CustomDialogImageFragment extends DialogFragment implements View.OnClickListener {

    public TextView image_crop_gallery;
    public TextView image_crop_camera;
    public TextView image_crop_cancel;

    private CustomDialogImageListener customDialogImageListener;

    private int layoutWidth;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialogFragment);

        this.layoutWidth = this.getResources().getDimensionPixelOffset(R.dimen.width_600);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_dialog_image_crop_fragment, container, false);

        image_crop_gallery = (TextView) view.findViewById(R.id.image_crop_gallery);
        image_crop_camera = (TextView) view.findViewById(R.id.image_crop_camera);
        image_crop_cancel = (TextView) view.findViewById(R.id.image_crop_cancel);

        image_crop_gallery.setOnClickListener(this);
        image_crop_camera.setOnClickListener(this);
        image_crop_cancel.setOnClickListener(this);

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

        this.getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dismiss();
                    return true;
                }
                return false;
            }
        });

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
            case R.id.image_crop_gallery:
                if (customDialogImageListener != null) {
                    customDialogImageListener.onGalleryClicked();
                }
                break;
            case R.id.image_crop_camera:
                if (customDialogImageListener != null) {
                    customDialogImageListener.onCameraClicked();
                }
                break;
            case R.id.image_crop_cancel:
                if (customDialogImageListener != null) {
                    customDialogImageListener.onCancelClicked();
                }
        }
    }

    public void setCustomDialogImageListener(CustomDialogImageListener customDialogImageListener) {
        this.customDialogImageListener = customDialogImageListener;
    }

}
