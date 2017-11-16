package com.shortmeet.www.zTakePai.editt.editor.adapter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shortmeet.www.zTakePai.editt.effects.control.EditorService;
import com.shortmeet.www.zTakePai.editt.effects.control.OnEffectChangeListener;
import com.shortmeet.www.zTakePai.editt.effects.control.onSeltClickListener;

/**
 * Created by zxf on 2017/11/3.
 */

public class BaseChooserFragment extends Fragment {
    public EditorService mEditorService;
    public OnEffectChangeListener mOnEffectChangeListener;
    public onSeltClickListener mOnClickListener;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    public void setmEditorService(EditorService editorService) {
        this.mEditorService = editorService;
    }

    public void setOnEffectChangeListener(OnEffectChangeListener onEffectChangeListener) {
        mOnEffectChangeListener = onEffectChangeListener;
    }

    public void setOnclickListener(onSeltClickListener onClickListener){
        mOnClickListener=onClickListener;
    }

}
