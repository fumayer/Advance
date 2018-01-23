package com.example.sj.app2.fragmentconn;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sj.app2.LogUtil;
import com.example.sj.app2.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CallFragment extends Fragment {

    private TextView tv;
    public static final String TAG = CallFragment.class.getSimpleName();

    public CallFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_call, container, false);
        tv = (TextView) view.findViewById(R.id.tv);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Functions.getInstance().invokeFunction(TAG);
            }
        });
        return view;
    }

}
