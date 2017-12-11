package com.example.sj.app2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.sj.app2.arc.TestArcActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void go1(View view) {
        Toast.makeText(this, "触发go1", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, TestMathActivity.class));
    }

    public void go2(View view) {
        Toast.makeText(this, "触发go2", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, TestCrashActivity.class));
    }

    public void go3(View view) {
        Toast.makeText(this, "触发go3", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, TestInstanceActivity.class));

    }

    public void go4(View view) {
        Toast.makeText(this, "触发go4", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, TestListActivity.class));

    }

    public void go5(View view) {
        Toast.makeText(this, "触发go5", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, TestLinkedHashMapActivity.class));
    }

    public void go6(View view) {
        Toast.makeText(this, "触发go6", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, TestAnimActivity.class));

    }

    public void go7(View view) {
        Toast.makeText(this, "触发go7", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, TestArcActivity.class));

    }

    public void go8(View view) {
        Toast.makeText(this, "触发go8", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, TestSwitchActivity.class));

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
