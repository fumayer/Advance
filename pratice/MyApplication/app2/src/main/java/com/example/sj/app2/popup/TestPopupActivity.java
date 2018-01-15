package com.example.sj.app2.popup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.sj.app2.R;

public class TestPopupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_popup);
    }


    public void go1(View view) {
            Toast.makeText(this, "触发go1", Toast.LENGTH_SHORT).show();
            new MyPop(this).showAsDropDown(view);
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
