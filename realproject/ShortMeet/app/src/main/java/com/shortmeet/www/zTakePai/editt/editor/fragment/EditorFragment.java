package com.shortmeet.www.zTakePai.editt.editor.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shortmeet.www.R;
import com.shortmeet.www.zTakePai.editt.editor.adapter.BaseChooserFragment;

/**
 * Created by zxf on 2017/11/3.
 */

public class EditorFragment extends BaseChooserFragment implements View.OnClickListener{
    private TextView shortmeet_icon_crop;
    private TextView shortmeet_icon_music;
    public static final int CROPTAG=0;
    public static final int MUSICTAG=1;


    public static EditorFragment newInstance() {
        Bundle args = new Bundle();
        EditorFragment editfrg = new EditorFragment();
        editfrg.setArguments(args);
        return editfrg;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_editor, null);
        shortmeet_icon_crop= (TextView) mView.findViewById(R.id.shortmeet_icon_crop);
        shortmeet_icon_music= (TextView) mView.findViewById(R.id.shortmeet_icon_music);

        shortmeet_icon_crop.setTag(CROPTAG);
        shortmeet_icon_music.setTag(MUSICTAG);
        shortmeet_icon_crop.setOnClickListener(this);
        shortmeet_icon_music.setOnClickListener(this);
        return mView;
    }

    @Override
    public void onClick(View v) {
        mOnClickListener.onSelfClick(v);
    }
}
