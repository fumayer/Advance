package com.example.sj.app2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class TestListViewActivity extends AppCompatActivity {
    private ListView mList;

    private static ArrayList<String> datas;

    static {
        if (datas == null) {
            datas = new ArrayList<>();
        }
        if (datas.size() == 0) {
            for (int i = 0; i < 10; i++) {
                datas.add("数据" + i);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_list_view);
        mList = (ListView) findViewById(R.id.list);
        mList.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return datas == null ? 0 : datas.size();
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = new TextView(TestListViewActivity.this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                textView.setLayoutParams(params);
                textView.setGravity(Gravity.CENTER);
                textView.setText(datas.get(position));
                return textView;
            }
        });
        ImageView imageView_header = new ImageView(this);
        imageView_header.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        imageView_header.setImageDrawable(getResources().getDrawable(R.drawable.claw_doll_success_default));
        mList.addHeaderView(imageView_header);

        ImageView imageView_header_second = new ImageView(this);
        imageView_header_second.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        imageView_header_second.setImageDrawable(getResources().getDrawable(R.drawable.service_icon));
        mList.addHeaderView(imageView_header_second);

        ImageView imageView_footer = new ImageView(this);
        imageView_footer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        imageView_footer.setImageDrawable(getResources().getDrawable(R.drawable.appointment_flag));
        mList.addFooterView(imageView_footer);


    }
}
