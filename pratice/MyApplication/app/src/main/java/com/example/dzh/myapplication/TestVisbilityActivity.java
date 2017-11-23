package com.example.dzh.myapplication;

import android.opengl.Visibility;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class TestVisbilityActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_visbility);
    }

    public void go1(View view) {
        int visibility = view.getVisibility();
        Toast.makeText(this, "触发go1-----" + visibility, Toast.LENGTH_SHORT).show();

        if (visibility == View.GONE) {
            return;
        } else if (visibility == View.VISIBLE) {
            view.setVisibility(View.INVISIBLE);
        } else if (visibility == View.INVISIBLE) {
            view.setVisibility(View.VISIBLE);
        }
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
