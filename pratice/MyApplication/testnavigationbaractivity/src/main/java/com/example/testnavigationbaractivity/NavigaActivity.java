package com.example.testnavigationbaractivity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MenuItem;

public class NavigaActivity extends FragmentActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private JFragmentManager mSFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_naviga);
        if (mSFragmentManager == null) {
            mSFragmentManager = new JFragmentManager(R.id.fragment_container, this);
        }
        initNavigator();
//        if (savedInstanceState == null) {
//            initFragment();
//        }
            initFragment();


    }

    private void initFragment() {

        if (mSFragmentManager != null) {
            mSFragmentManager.addFragment(new AFragment())
                    .addFragment(new BFragment())
                    .addFragment(new CFragment())
                    .show();
        }
    }

    private void initNavigator() {
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if (mSFragmentManager == null) {
            return false;
        }
        Log.e("NavigaActivity", "57-----onNavigationItemSelected--->" + item.toString());

        switch (item.getItemId()) {
            case R.id.recents:
                mSFragmentManager.changeCurrentFragment(0);
                break;
            case R.id.favourites:
                mSFragmentManager.changeCurrentFragment(1);
                break;
            case R.id.nearby:
                mSFragmentManager.changeCurrentFragment(2);
                break;
            default:
                return false;
        }
        return true;
    }

}
