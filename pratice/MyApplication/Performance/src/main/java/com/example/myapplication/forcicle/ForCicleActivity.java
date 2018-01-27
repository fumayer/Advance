package com.example.myapplication.forcicle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.myapplication.R;
import com.example.myapplication.TVAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class ForCicleActivity extends AppCompatActivity implements BaseQuickAdapter.OnItemClickListener {

    private static List<String> data = new ArrayList<>();

    static {
        data.add("简单for循环");
        data.add("增强for循环");
        data.add("迭代器for循环");
    }


    private RecyclerView recy;
    private TextView tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_for_cicle);
        addObject();
        tv = (TextView) findViewById(R.id.tv);
        recy = (RecyclerView) findViewById(R.id.recy);
        recy.setLayoutManager(new LinearLayoutManager(this));
        TVAdapter adapter = new TVAdapter(data);
        recy.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
    }

    private void addObject() {
        for (int i = 0; i < 10; i++) {
            objects.add(new Person("假数据" + i, i));
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        switch (data.get(position)) {
            case "简单for循环":
                easyFor();
                break;
            case "增强for循环":
                strengthenFor();
                break;
            case "迭代器for循环":
                iteratorFor();
                break;
            default:
                break;
        }
    }


    private List<Person> objects = new ArrayList<>();

    /**
     * 简单for循环，测试对象赋值情况
     */
    private void easyFor() {
        for (int i = 0; i < objects.size(); i++) {
            Person person = objects.get(i);
//            person = new Person("大黄" + i, i); // 数据不会改变，指向的对象确实是新的，但是和objects 没有关系
            person.setName("dahuang");
//            Log.e("ForCicleActivity", "72-----easyFor--->" + person);
        }
    }

    /**
     * 增强for循环
     */
    private void strengthenFor() {
        for (Person person : objects) {
            person.setName("大黄"); //  居然也是可以改变对象属性的，
        }
        int a[] = {1, 2, 3, 4};
        Collection intList = java.util.Arrays.asList(a);
        for (Iterator i = intList.iterator(); i.hasNext(); ) {
            Object j = i.next();
            System.out.println(j);
        }
//        StackTraceElement[] stackElements = new Throwable().getStackTrace();
//        if (stackElements != null) {
//            for (int i = 0; i < stackElements.length; i++) {
//                Log.e("ForCicleActivity", "107-----strengthenFor--->" + stackElements[i]);
//            }
//        }
    }


    /**
     * 迭代for循环
     */
    private void iteratorFor() {
        for (Iterator<Person> iterator = objects.iterator(); iterator.hasNext(); ) {
            Log.e("ForCicleActivity", "109-----iteratorFor--->" + iterator.next());
        }

    }


    public void go1(View view) {
        Toast.makeText(this, "触发go1", Toast.LENGTH_SHORT).show();
        Log.e("ForCicleActivity", "76-----go1--->\n" + objects);
    }

    public void go2(View view) {
        Toast.makeText(this, "触发go2", Toast.LENGTH_SHORT).show();
    }

    public void go3(View view) {
        Toast.makeText(this, "触发go3", Toast.LENGTH_SHORT).show();
    }

    public void go4(View view) {
        Toast.makeText(this, "触发go4", Toast.LENGTH_SHORT).show();
    }
}
