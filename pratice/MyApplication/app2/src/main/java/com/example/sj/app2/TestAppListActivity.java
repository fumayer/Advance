package com.example.sj.app2;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
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
        List<String> thirdAppList = getThirdAppList(SYSTEMAPP, this);
        for (int i = 0; i < thirdAppList.size(); i++) {
            LogUtil.e("TestAppListActivity", "24-----go1--->" + i + "  " + thirdAppList.get(i));
        }
    }

    public void go3(View view) {

    }

    public void go4(View view) {

    }


    public static final int THIRDAPP = 000;
    public static final int SYSTEMAPP = 111;

    private List<String> getThirdAppList(Context context) {
        return getThirdAppList(THIRDAPP, context);
    }

    private List<String> getThirdAppList(int tag, Context context) {
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
                LogUtil.e("TestAppListActivity", "66-----getThirdAppList--->packageName   " + pak.packageName);
                LogUtil.e("TestAppListActivity", "64-----getThirdAppList--->versionCode   " + pak.versionCode);
                LogUtil.e("TestAppListActivity", "65-----getThirdAppList--->versionName   " + pak.versionName);
                LogUtil.e("TestAppListActivity", "67-----getThirdAppList--->sharedUserId   " + pak.sharedUserId);
                LogUtil.e("TestAppListActivity", "68-----getThirdAppList--->activities   " + pak.activities);
                LogUtil.e("TestAppListActivity", "69-----getThirdAppList--->applicationInfo   " + pak.applicationInfo);
                LogUtil.e("TestAppListActivity", "70-----getThirdAppList--->baseRevisionCode   " + pak.baseRevisionCode);
                LogUtil.e("TestAppListActivity", "71-----getThirdAppList--->configPreferences   " + pak.configPreferences);
                LogUtil.e("TestAppListActivity", "72-----getThirdAppList--->gids   " + pak.gids);
                LogUtil.e("TestAppListActivity", "73-----getThirdAppList--->installLocation   " + pak.installLocation);
                LogUtil.e("TestAppListActivity", "74-----getThirdAppList--->lastUpdateTime   " + pak.lastUpdateTime);
                LogUtil.e("TestAppListActivity", "75-----getThirdAppList--->firstInstallTime   " + pak.firstInstallTime);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    LogUtil.e("TestAppListActivity", "76-----getThirdAppList--->featureGroups   " + pak.featureGroups);
                }
                LogUtil.e("TestAppListActivity", "   ");
                LogUtil.e("TestAppListActivity", "   ");
                // apps.add(pak);
                thirdAPP.add(pak.packageName);
            } else {
                //系统应用ÒÒ
                systemApp.add(pak.packageName);
            }
        }
        return tag == THIRDAPP ? thirdAPP : systemApp;

    }
}
