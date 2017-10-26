package com.quduquxie.application;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Handler;
import android.os.Process;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.quduquxie.application.component.ApplicationComponent;
import com.quduquxie.application.component.DaggerApplicationComponent;
import com.quduquxie.application.module.ApplicationModule;
import com.quduquxie.application.module.ApplicationUtilModule;
import com.quduquxie.application.module.SharedPreferencesUtilModule;
import com.quduquxie.base.config.BaseConfig;
import com.quduquxie.base.database.helper.BookDaoHelper;
import com.quduquxie.base.handler.CrashHandler;
import com.quduquxie.base.util.Initialization;
import com.quduquxie.read.ReadStatus;
import com.quduquxie.service.download.DownloadService;
import com.quduquxie.util.OpenUDID;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.List;

public class QuApplication extends MultiDexApplication {

    private ApplicationComponent applicationComponent;

    private ApplicationUtil applicationUtil;

    private static QuApplication quApplication;

    private static int version_code;
    private static String channel_id;
    private static String version_name;
    private static String package_name;

    private static String user_device_id;

    private int replyCount;

    private int commentCount;

    private DownloadService downloadService;


    private ReadStatus readStatus;

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化注入
        this.initializeInjector();
        //初始化小米推送
        this.initializeMiPush();
        //初始化Logger
        this.initializeLogger();
        //初始化用户标识
        this.initializeDeviceID();
        //初始化异常捕获
        this.initializeCrashHandler();
        //初始化参数
        this.initializeParameter();


        applicationHandler = new Handler(this.getMainLooper());
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }

    private void initializeInjector() {
        this.applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .applicationUtilModule(new ApplicationUtilModule(this))
                .sharedPreferencesUtilModule(new SharedPreferencesUtilModule(this))
                .build();
    }

    private void initializeMiPush() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> appProcessInfo = activityManager.getRunningAppProcesses();
        String packageName = getPackageName();
        int pid = Process.myPid();
        for (ActivityManager.RunningAppProcessInfo processInfo : appProcessInfo) {
            if (processInfo.pid == pid && packageName.equals(processInfo.processName)) {

                Logger.e("initializeMiPush registerPush: " + processInfo.pid);

                MiPushClient.registerPush(this, BaseConfig.MI_PUSH_APP_ID, BaseConfig.MI_PUSH_APP_KEY);
            }
        }
    }

    private void initializeLogger() {
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(true)
                .methodCount(0)
                .methodOffset(0)
                .tag(BaseConfig.TAG)
                .build();

        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy) {
            @Override
            public boolean isLoggable(int priority, String tag) {
                return BaseConfig.DEVELOP_MODE;
            }
        });
    }

    private void initializeDeviceID() {
        OpenUDID.syncContext(this);
        setUserDeviceID(OpenUDID.getCorpUDID("com.quduquxie"));
    }

    private void initializeCrashHandler() {
        CrashHandler.getCrashHandler().initialize(this);
    }

    private void initializeParameter() {
        quApplication = this;

        setQuPackageName(getPackageName());

        applicationUtil = applicationComponent.getApplicationUtil();

        if (applicationUtil.getDownloadService() == null) {
            applicationUtil.bindDownloadService();
        }

        if (applicationUtil.getCheckNovelUpdateService() == null) {
            applicationUtil.bindCheckNovelUpdateService();
        }

        setChannelID(applicationUtil.getChannelID());
        setVersionName(applicationUtil.getVersionName());
        setVersionCode(applicationUtil.getVersionCode());

        //初始化书籍数据库
        BookDaoHelper.getInstance(this);

        //初始化存储路径
        Initialization.initializeFile(this);

        Initialization.initApplicationParameter(this);
    }

    public ApplicationComponent getApplicationComponent() {
        return this.applicationComponent;
    }

    public static QuApplication getInstance() {
        return quApplication;
    }

    public static int getVersionCode() {
        return version_code;
    }

    public static void setVersionCode(int version_code) {
        QuApplication.version_code = version_code;
    }

    public static String getChannelID() {
        return channel_id;
    }

    public static void setChannelID(String channel_id) {
        QuApplication.channel_id = channel_id;
    }

    public static String getVersionName() {
        return version_name;
    }

    public static void setVersionName(String version_name) {
        QuApplication.version_name = version_name;
    }

    public static String getQuPackageName() {
        return package_name;
    }

    public static void setQuPackageName(String package_name) {
        QuApplication.package_name = package_name;
    }

    public static String getUserDeviceID() {
        return user_device_id;
    }

    public static void setUserDeviceID(String user_device_id) {
        QuApplication.user_device_id = user_device_id;
    }

    public int loadReplyCount() {
        return replyCount;
    }

    public int loadCommentCount() {
        return commentCount;
    }

    public void setCommentsMessage(int commentCount, int replyCount) {
        this.replyCount = replyCount;
        this.commentCount = commentCount;
    }

    public void addCommentsCount() {
        this.commentCount += 1;
    }

    public void addReplyCount() {
        this.replyCount += 1;
    }

    public DownloadService getDownloadService() {
        return applicationUtil.getDownloadService();
    }

    public ReadStatus getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(ReadStatus readStatus) {
        this.readStatus = readStatus;
    }

    public static volatile Handler applicationHandler;
}
