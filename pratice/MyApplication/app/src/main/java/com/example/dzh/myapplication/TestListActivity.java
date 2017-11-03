package com.example.dzh.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class TestListActivity extends AppCompatActivity {

    private List<String> mList1 = new ArrayList<>();
    private List<String> mList2 = new ArrayList<>();
    private List<String> mList3;
    private String s = "fadsfasdf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_list);
        mList1.add("a");
        mList1.add("b");
        mList1.add("c");
        mList2.add("A");
        mList2.add("B");
        mList2.add("C");
    }

    public void go1(View view) {
        Toast.makeText(this, "触发go1", Toast.LENGTH_SHORT).show();
        Log.e("TestListActivity", "30-----go1---mList1>" + mList1);
        Log.e("TestListActivity", "38-----go1---mList2>" + mList2);
        if (mList3 != null) {
            Log.e("TestListActivity", "38-----go1---mList3>" + mList3);
        }


    }

    public void go2(View view) {
        Toast.makeText(this, "触发go2", Toast.LENGTH_SHORT).show();

    }

    public void go3(View view) {
        Toast.makeText(this, "触发go3", Toast.LENGTH_SHORT).show();
        mList1.addAll(mList2);
    }

    public void go4(View view) {
        Toast.makeText(this, "触发go4", Toast.LENGTH_SHORT).show();
        mList2.set(0, null);

    }

    public void go5(View view) {
        Toast.makeText(this, "触发go5", Toast.LENGTH_SHORT).show();
        mList2.remove(0);

    }

    public void go6(View view) {
        Toast.makeText(this, "触发go6", Toast.LENGTH_SHORT).show();
        mList2.set(0, "X");
    }


    /* 要添加的集合不能为null*/
    public void go7(View view) {
        Toast.makeText(this, "触发go7", Toast.LENGTH_SHORT).show();
        mList1.addAll(mList3);

    }

    public void go8(View view) {
        Toast.makeText(this, "触发go8", Toast.LENGTH_SHORT).show();
        if (mList3 == null) {
            mList3 = new ArrayList<>(mList2);
        }
    }


    public void go9(View view) {
        Toast.makeText(this, "触发go9", Toast.LENGTH_SHORT).show();
    }

    public void go10(View view) {
        Toast.makeText(this, "触发go10", Toast.LENGTH_SHORT).show();
    }

    public void go11(View view) {
        Toast.makeText(this, "触发go11", Toast.LENGTH_SHORT).show();
    }

    public void go12(View view) {
        Toast.makeText(this, "触发go12", Toast.LENGTH_SHORT).show();
    }


}
