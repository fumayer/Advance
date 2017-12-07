package com.example.sj.app2;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunjie on 2017/12/7.
 */

public class TestListActivity extends ListActivity {
    private static List<String> mStrings = new ArrayList<>();

    static {

        for (int i = 0; i < 5; i++) {
            mStrings.add("--------测试内容--------" + i);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setListAdapter(new ArrayAdapter<String>(this, R.layout.item, mStrings));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapter(new ArrayAdapter<String>(this, R.layout.item, mStrings));

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Toast.makeText(this, mStrings.get(position), Toast.LENGTH_SHORT).show();
    }
}
