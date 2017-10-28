package com.example.testnavigationbaractivity;

import android.os.Bundle;
import android.support.design.internal.BottomNavigationItemView;
import android.support.v7.app.AppCompatActivity;

public class NavigatorBarActivity extends AppCompatActivity {
    private BottomNavigationItemView mNavigationbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigator_bar);
        mNavigationbar = (BottomNavigationItemView) findViewById(R.id.navigationbar);
    }
}
