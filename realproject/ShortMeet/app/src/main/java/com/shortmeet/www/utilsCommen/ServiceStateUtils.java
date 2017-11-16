package com.shortmeet.www.utilsCommen;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.text.TextUtils;

import java.util.List;
/*
 *  Fly 注：  Service 状态工具类
 */
public class ServiceStateUtils {

    /**
     * 判断服务是否运行
     *
     * @param context
     * @param clzService
     * @return
     */
    public static boolean isRunning(Context context, Class<? extends Service> clzService) {
        // 活动管理器
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        // 返回正在运行的服务信息 参1 返回的最大数量
        List<RunningServiceInfo> runningServices = am.getRunningServices(1000);

        for (RunningServiceInfo runningServiceInfo : runningServices) {
            // 获取正在运行的service的组件名称对象
            ComponentName service = runningServiceInfo.service;
            String runclassName = service.getClassName();
            if (TextUtils.equals(runclassName, clzService.getName())) {
                // 如果相同 说明传进来的服务正在运行
                return true;
            }
        }
        return false;
    }

}
