package com.example.sj.design;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
        Log.e("TestSnackActivity", "40-----go4--->" + getVersionName());
        Log.e("TestSnackActivity", "41-----go4--->" + getVersionCode());

    }

    private void showSnack(View view) {
        Snackbar.make(view, "fafasfas", Snackbar.LENGTH_SHORT).show();
    }


    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    private String getVersionName() {
        try {
            PackageManager manager = getPackageManager();
            PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private int getVersionCode() {
        try {
            PackageManager manager = getPackageManager();
            PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
            return info.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
