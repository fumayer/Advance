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
        if (mSFragmentManager == null) {
            mSFragmentManager = new SFragmentManager(R.id.fragment_container, this);
        }
        initNavigator();
        initFragment(savedInstanceState);

    }

    private void initFragment(Bundle saveInstanceState) {

        if (mSFragmentManager != null) {
            mSFragmentManager.setBundle(saveInstanceState)
                    .addFragment(new AFragment())
                    .addFragment(new BFragment())
                    .addFragment(new CFragment())
                    .showSelectFrag();
        }
    }

    BottomNavigationView bottomNavigationView;

    private void initNavigator() {
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        Log.e("NavigaActivity", "42-----initNavigator--->" + bottomNavigationView.getSelectedItemId());

    }


    private boolean isFirst = true;

    @Override
    protected void onResume() {
        super.onResume();
        if (bottomNavigationView != null) {
            Log.e("NavigaActivity", "52-----onResume--->" + bottomNavigationView.getSelectedItemId());
            Log.e("NavigaActivity", "54-----onResume---isFirst>" + isFirst);

            if (isFirst) {
                bottomNavigationView.setSelectedItemId(R.id.recents);
                isFirst = false;
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if (mSFragmentManager == null) {
            return false;
        }
        Log.e("NavigaActivity", "57-----onNavigationItemSelected--->" + item.toString());

        switch (item.getItemId()) {
            case R.id.recents:
                mSFragmentManager.showSelectFrag(0);
                break;
            case R.id.favourites:
                mSFragmentManager.showSelectFrag(1);
                break;
            case R.id.nearby:
                mSFragmentManager.showSelectFrag(2);
                break;
            default:
                return false;
        }
        return true;
    }

}
