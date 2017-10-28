package com.example.testnavigationbaractivity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MenuItem;

public class NavigaActivity extends FragmentActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private SFragmentManager mSFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_naviga);
        initNavigator();
        if (savedInstanceState == null) {
            initFragment();
        }


    }

    private void initFragment() {
        if (mSFragmentManager == null) {
            mSFragmentManager = new SFragmentManager(R.id.fragment_container, this);
        }
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
//        int[][] states = new int[][]{
//                new int[]{-android.R.attr.state_checked},
//                new int[]{android.R.attr.state_checked}
//        };
//
//        int[] colors = new int[]{getResources().getColor(R.color.normal_color),
//                getResources().getColor(R.color.master_color)
//        };
//        ColorStateList csl = new ColorStateList(states, colors);
//        bottomNavigationView.setItemTextColor(csl);
//        bottomNavigationView.setItemIconTintList(csl);
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
