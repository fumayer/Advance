package com.example.sj.app2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class TestLinkedHashMapActivity extends AppCompatActivity {
    private Map<Integer, Person> mHashMap = new HashMap<>();
    private Map<Integer, Person> mLinkedMap = new LinkedHashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_linked_hash_map);
        for (int i = 0; i < 10; i++) {
            Person person = new Person(i, "Person" + i);
            mHashMap.put(i, person);
            mLinkedMap.put(i, person);
        }
    }

    public void go1(View view) {
        Toast.makeText(this, "触发go1", Toast.LENGTH_SHORT).show();

        LogUtil.e("TestLinkedHashMapActivity", "32-----go1--->\n\n\n");

        for (Integer integer : mHashMap.keySet()) {
            LogUtil.e("TestLinkedHashMapActivity", "33-----go1--->hashmap   key    " + integer + "       value    " + mHashMap.get(integer));
        }

        LogUtil.e("TestLinkedHashMapActivity", "32-----go1--->\n\n\n");


        LogUtil.e("TestLinkedHashMapActivity", "32-----go1--->\n\n\n");

        for (Integer integer : mLinkedMap.keySet()) {
            LogUtil.e("TestLinkedHashMapActivity", "33-----go1--->linkedhashmap   key    " + integer + "       value    " + mLinkedMap.get(integer));
        }


    }

    public void go2(View view) {
        Toast.makeText(this, "触发go2", Toast.LENGTH_SHORT).show();

        Person x因素 = new Person(-1, "X因素");
        mHashMap.put(-1, x因素);
        mLinkedMap.put(-1, x因素);
    }

    public void go3(View view) {
        Toast.makeText(this, "触发go3", Toast.LENGTH_SHORT).show();
        mHashMap.remove(0);
        mLinkedMap.remove(0);
    }

    public void go4(View view) {
        Toast.makeText(this, "触发go4", Toast.LENGTH_SHORT).show();
    }

    public void go5(View view) {
        Toast.makeText(this, "触发go5", Toast.LENGTH_SHORT).show();
    }

    public void go6(View view) {
        Toast.makeText(this, "触发go6", Toast.LENGTH_SHORT).show();
    }

    public void go7(View view) {
        Toast.makeText(this, "触发go7", Toast.LENGTH_SHORT).show();
    }

    public void go8(View view) {
        Toast.makeText(this, "触发go8", Toast.LENGTH_SHORT).show();
    }

}
