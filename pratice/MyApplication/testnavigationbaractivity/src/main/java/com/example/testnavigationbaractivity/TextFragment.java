package com.example.testnavigationbaractivity;


import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class TextFragment extends Fragment {
    private TextView mTv;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_text, container, false);
        mTv = (TextView) view.findViewById(R.id.tv);
        return view;
    }

    public TextFragment() {
    }



    public void setText(@StringRes int text) {
        if (mTv != null) {
            mTv.setText(text);
        }
    }
}
