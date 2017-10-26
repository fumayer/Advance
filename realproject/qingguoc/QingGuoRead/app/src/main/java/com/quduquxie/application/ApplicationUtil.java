package com.quduquxie.application;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;
import com.quduquxie.base.config.BaseConfig;
import com.quduquxie.service.check.CheckNovelUpdateService;
import com.quduquxie.service.download.DownloadService;

import java.util.HashMap;

/**
 * Created on 17/3/16.
 * Created by crazylei.
 */

public class ApplicationUtil {

    private Context context;

    private boolean bindDownloadService = false;

    private boolean bindCheckNovelUpdateService = false;

    private DownloadService downloadService;

    private CheckNovelUpdateService checkNovelUpdateService;

    private HashMap<String, Long> request_map = new HashMap<>();

    private HashMap<String, Long> view_draw_map = new HashMap<>();

    public ApplicationUtil(Context context) {
        this.context = context;
        if (request_map == null) {
            request_map = new HashMap<>();
        } else {
            request_map.clear();
        }
    }

    public void bindCheckNovelUpdateService() {
        if (bindCheckNovelUpdateService) {
            unBindCheckNovelUpdateService();
        }

        if (checkNovelUpdateService == null) {
            try {
                Intent intent = new Intent();
                intent.setClass(context, CheckNovelUpdateService.class);
                context.startService(intent);
                context.bindService(intent, checkNovelUpdateConnection, Context.BIND_AUTO_CREATE);
                bindCheckNovelUpdateService = true;
            } catch (Exception exception) {
                bindCheckNovelUpdateService = false;
                collectException(exception);
                exception.printStackTrace();
            }
        }
    }

    private void unBindCheckNovelUpdateService() {
        try {
            context.unbindService(checkNovelUpdateConnection);
        } catch (Exception exception) {
            collectException(exception);
            exception.printStackTrace();
        }
    }


    private ServiceConnection checkNovelUpdateConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Logger.d("CheckNovelUpdateConnection OnServiceDisconnected: " + name);
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Logger.d("CheckNovelUpdateConnection OnServiceConnected: " + name);

            try {
                checkNovelUpdateService = ((CheckNovelUpdateService.CheckNovelUpdateBinder) service).getService();
            } catch (ClassCastException exception) {
                exception.printStackTrace();
            }
        }
    };

    public void bindDownloadService() {
        if (bindDownloadService) {
            unBindDownloadService();
        }

        if (downloadService == null) {
            try {
                Intent intent = new Intent();
                intent.setClass(context, DownloadService.class);
                context.startService(intent);
                context.bindService(intent, downloadServiceConnection, Context.BIND_AUTO_CREATE);
                bindDownloadService = true;
            } catch (Exception exception) {
                bindDownloadService = false;
                collectException(exception);
                exception.printStackTrace();
            }
        }
    }

    private void unBindDownloadService() {
        try {
            context.unbindService(downloadServiceConnection);
        } catch (Exception exception) {
            collectException(exception);
            exception.printStackTrace();
        }
    }

    private ServiceConnection downloadServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Logger.d("DownloadServiceConnection OnServiceDisConnected: " + name);
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {
            Logger.d("DownloadServiceConnection onServiceConnected: " + name);

            try {
                downloadService = ((DownloadService.DownloadBinder) iBinder).getService();
            } catch (Exception exception) {
                collectException(exception);
                exception.printStackTrace();
            }
        }
    };

    public DownloadService getDownloadService() {
        return downloadService;
    }

    public CheckNovelUpdateService getCheckNovelUpdateService() {
        return checkNovelUpdateService;
    }

    /**
     * 获取版本信息
     **/
    private PackageInfo loadPackageInfo() {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException exception) {
            exception.printStackTrace();
        }
        return packageInfo;
    }

    /**
     * 获取版本名称
     **/
    public String getVersionName() {
        String versionName = "";
        PackageInfo packageInfo = loadPackageInfo();
        if (packageInfo != null) {
            versionName = packageInfo.versionName;
        }
        return versionName;
    }

    /**
     * 获取版本号
     **/
    public int getVersionCode() {
        int versionCode = -1;
        PackageInfo packageInfo = loadPackageInfo();
        if (packageInfo != null) {
            versionCode = packageInfo.versionCode;
        }
        return versionCode;
    }

    /**
     * 获取渠道标识
     **/
    public String getChannelID() {
        return getApplicationMetadata("BaiduMobAd_CHANNEL");
    }

    private String getApplicationMetadata(String metaKey) {
        if (context != null && !TextUtils.isEmpty(metaKey)) {
            String packageName = context.getPackageName();
            if (!TextUtils.isEmpty(packageName)) {
                try {
                    ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_META_DATA);

                    if (applicationInfo != null && applicationInfo.metaData != null)
                        return applicationInfo.metaData.getString(metaKey);
                } catch (PackageManager.NameNotFoundException exception) {
                    exception.printStackTrace();
                }
                return BaseConfig.DEFAULT_CHANNEL;
            }
        }
        return BaseConfig.DEFAULT_CHANNEL;
    }

    public void collectException(Exception exception) {
        Logger.d(exception.toString());
    }

    public void collectException(Throwable throwable) {
        Logger.d(throwable.toString());
    }

    public void addRequestRecord(String url, long start) {
        request_map.put(url, start);
    }

    public void deleteRequestRecord(String url, long end) {
        if (request_map.containsKey(url)) {
            long start = request_map.get(url);
            Logger.e("网络请求: " + url + " 耗时 " + (end - start));
        }
    }

    public void addViewDrawRecord(String view, long start) {
        view_draw_map.put(view, start);
    }

    public void deleteViewDrawRecord(String view, long end) {
        if (view_draw_map.containsKey(view)) {
            long start = view_draw_map.get(view);
            Logger.e("页面绘制: " + view + " 耗时 " + (end - start));
        }
    }
}