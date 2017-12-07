package com.example.sj.app2;

import android.app.Application;

import com.example.sj.app2.crash.CrashHandler;

/**
 * Created by sunjie on 2017/11/29.
 */

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler crashHandler = CrashHandler.getInstance();
//        crashHandler.init(getApplicationContext());


    }
}
