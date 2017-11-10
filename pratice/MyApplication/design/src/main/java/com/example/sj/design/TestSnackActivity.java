package com.example.sj.design;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class TestSnackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_snack);
    }

    public void go1(View view) {
        Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
        showSnack(null);
    }



    public void go2(View view) {
        Toast.makeText(this, "view", Toast.LENGTH_SHORT).show();
        showSnack(view);

    }

    public void go3(View view) {
        Toast.makeText(this, "触发go3", Toast.LENGTH_SHORT).show();
        showSnack(new View(this));

    }

    public void go4(View view) {
        Toast.makeText(this, "触发go4", Toast.LENGTH_SHORT).show();
    }

    private void showSnack(View view) {
        Snackbar.make(view,"fafasfas",Snackbar.LENGTH_SHORT).show();
    }
}
