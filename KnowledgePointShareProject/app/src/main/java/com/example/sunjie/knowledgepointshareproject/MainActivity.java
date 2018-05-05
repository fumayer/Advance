package com.example.sunjie.knowledgepointshareproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.sunjie.knowledgepointshareproject.abstract_factory.AbstractFactoryActivity;
import com.example.sunjie.knowledgepointshareproject.abstract_factory2.AbstractFactory2Activity;
import com.example.sunjie.knowledgepointshareproject.builder.BuilderActivity;
import com.example.sunjie.knowledgepointshareproject.compose.ComposeActivity;
import com.example.sunjie.knowledgepointshareproject.factory.FactoryActivity;
import com.example.sunjie.knowledgepointshareproject.filter.FilterActivity;
import com.example.sunjie.knowledgepointshareproject.flyweight.FlyWeightActivity;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.addLogAdapter(new AndroidLogAdapter());
        setContentView(R.layout.activity_main);
    }

    public void go1(View view) {
        Toast.makeText(this, "触发go1", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, FactoryActivity.class));
    }

    public void go2(View view) {
        Toast.makeText(this, "触发go2", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, AbstractFactoryActivity.class));
    }

    public void go3(View view) {
        Toast.makeText(this, "触发go3", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, AbstractFactory2Activity.class));


    }

    public void go4(View view) {
        Toast.makeText(this, "触发go4", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, ComposeActivity.class));

    }

    public void go5(View view) {
        Toast.makeText(this, "触发go5", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, FilterActivity.class));
    }

    public void go6(View view) {
        Toast.makeText(this, "触发go6", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, FlyWeightActivity.class));

    }

    public void go7(View view) {
        Toast.makeText(this, "触发go7", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, BuilderActivity.class));


    }

    public void go8(View view) {
        Toast.makeText(this, "触发go8", Toast.LENGTH_SHORT).show();

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

    public void go13(View view) {
        Toast.makeText(this, "触发go13", Toast.LENGTH_SHORT).show();
    }

    public void go14(View view) {
        Toast.makeText(this, "触发go14", Toast.LENGTH_SHORT).show();
    }

    public void go15(View view) {
        Toast.makeText(this, "触发go15", Toast.LENGTH_SHORT).show();

    }

    public void go16(View view) {
        Toast.makeText(this, "触发go16", Toast.LENGTH_SHORT).show();

    }

    public void go17(View view) {
        Toast.makeText(this, "触发go17", Toast.LENGTH_SHORT).show();
    }

    public void go18(View view) {
        Toast.makeText(this, "触发go18", Toast.LENGTH_SHORT).show();
    }

    public void go19(View view) {
        Toast.makeText(this, "触发go19", Toast.LENGTH_SHORT).show();
    }

    public void go20(View view) {
        Toast.makeText(this, "触发go20", Toast.LENGTH_SHORT).show();
    }

    public void go21(View view) {
        Toast.makeText(this, "触发go21", Toast.LENGTH_SHORT).show();
    }

    public void go22(View view) {
        Toast.makeText(this, "触发go22", Toast.LENGTH_SHORT).show();
    }

    public void go23(View view) {
        Toast.makeText(this, "触发go23", Toast.LENGTH_SHORT).show();
    }

    public void go24(View view) {
        Toast.makeText(this, "触发go24", Toast.LENGTH_SHORT).show();
    }
}
