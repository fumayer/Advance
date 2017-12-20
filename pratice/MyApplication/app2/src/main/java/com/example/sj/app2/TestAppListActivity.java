package com.example.sj.app2;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class TestAppListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_app_list);
    }

    public void go1(View view) {
        List<String> thirdAppList = getThirdAppList(this);
        for (int i = 0; i < thirdAppList.size(); i++) {
            LogUtil.e("TestAppListActivity", "24-----go1--->" + i + "  " + thirdAppList.get(i));
        }


    }

    public void go2(View view) {
        List<String> thirdAppList = getThirdAppList(SYSTEMAPP,this);
        for (int i = 0; i < thirdAppList.size(); i++) {
            LogUtil.e("TestAppListActivity", "24-----go1--->" + i + "  " + thirdAppList.get(i));
        }
    }

    public void go3(View view) {

    }

    public void go4(View view) {

    }


    public static final int THIRDAPP=000;
    public static final int SYSTEMAPP=111;

    private List<String> getThirdAppList(Context context) {
        return getThirdAppList(THIRDAPP,context);
    }
    private List<String> getThirdAppList(int tag,Context context) {
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> packageInfoList = packageManager.getInstalledPackages(0);
        // 判断是否系统应用：
        List<String> thirdAPP = new ArrayList<>();
        List<String> systemApp = new ArrayList<>();
        for (int i = 0; i < packageInfoList.size(); i++) {
            PackageInfo pak = (PackageInfo) packageInfoList.get(i);
            //判断是否为系统预装的应用
            if ((pak.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
                // 第三方应用

                // apps.add(pak);
                thirdAPP.add(pak.packageName);
            } else {
                //系统应用ÒÒ
                systemApp.add(pak.packageName);
            }
        }
        return tag==THIRDAPP?thirdAPP:systemApp;

    }
}
