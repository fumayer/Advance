package com.example.sj.app2.arc;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.sj.app2.R;

public class TestArcActivity extends AppCompatActivity {
    private ArcLayout mLayoutArc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_arc);
        mLayoutArc = (ArcLayout) findViewById(R.id.layout_arc);

    }

    public void go1(View view) {
        Toast.makeText(this, "触发go1", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, TestOutlineProviderActivity.class));
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void go2(View view) {
        Toast.makeText(this, "触发go2", Toast.LENGTH_SHORT).show();
        mLayoutArc.setClipToOutline(!mLayoutArc.getClipToOutline());
    }

    public void go3(View view) {
        Toast.makeText(this, "触发go3", Toast.LENGTH_SHORT).show();
    }

    public void go4(View view) {
        Toast.makeText(this, "触发go4", Toast.LENGTH_SHORT).show();
    }
}
