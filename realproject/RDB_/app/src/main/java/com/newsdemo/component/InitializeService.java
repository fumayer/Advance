package com.newsdemo.component;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.newsdemo.app.App;
import com.newsdemo.app.Constants;
import com.newsdemo.util.LogUtil;
import com.newsdemo.util.SystemUtil;
import com.orhanobut.logger.Logger;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.smtt.sdk.QbSdk;



/**
 * Created by jianqiang.hu on 2017/5/11.
 */

public class InitializeService extends IntentService {
    private static final String ACTION_INIT="initApplication";
    public InitializeService(String name) {
        super("initializeService");
    }

    public static void start(Context context){
        Intent intent=new Intent(context,InitializeService.class);
        intent.setAction(ACTION_INIT);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent!=null){
            String action=intent.getAction();
            if (ACTION_INIT.equals(action)){
                initApplication();
            }
        }
    }

    private void initApplication(){
        //初始化日志
        Logger.init(getPackageName()).hideThreadInfo();

        //初始化错误收集
        initBugly();

        //初始化内存泄漏检测
        LeakCanary.install(App.getInstance());

        //初始化tbs x5 webview
        QbSdk.allowThirdPartyAppDownload(true);
        QbSdk.initX5Environment(getApplicationContext(),QbSdk.WebviewInitType.FIRSTUSE_AND_PRELOAD, new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {

            }

            @Override
            public void onViewInitFinished(boolean b) {

            }
        });
    }

    private void initBugly(){
        Context context=getApplicationContext();
        String packageName=context.getPackageName();
        String processName= SystemUtil.getProcessName(android.os.Process.myPid());
        CrashReport.UserStrategy strategy=new CrashReport.UserStrategy(context);
        strategy.setUploadProcess(processName==null||processName.equals(packageName));
        CrashReport.initCrashReport(context, Constants.BUGLY_ID, LogUtil.isDebug,strategy);
    }
}
