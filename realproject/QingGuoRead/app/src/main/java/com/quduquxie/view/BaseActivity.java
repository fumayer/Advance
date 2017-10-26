package com.quduquxie.view;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.orhanobut.logger.Logger;
import com.quduquxie.application.QuApplication;
import com.quduquxie.application.component.ApplicationComponent;
import com.quduquxie.base.manager.ActivityStackManager;
import com.quduquxie.base.util.ResourceUtil;

import java.util.List;

/**
 * Created on 16/12/27.
 * Created by crazylei.
 */

public class BaseActivity extends AppCompatActivity {

    protected static boolean active;

    public int system_lock_time;

    public static boolean system_auto_brightness;

    public static int system_brightness = 0;

    private SharedPreferences sharedPreferences;

    private String mode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityStackManager.getActivityStackManager().insertActivity(this);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!active) {
            active = true;
            setDisplayState();
        }
        if (sharedPreferences != null && !sharedPreferences.getBoolean("auto_brightness", true)) {
            int screen_bright = sharedPreferences.getInt("screen_bright", -1);
            if (screen_bright >= 0) {
                setScreenBrightness(this, 20 + screen_bright);
            } else if (system_brightness >= 20) {
                setScreenBrightness(this, system_brightness);
            } else {
                setScreenBrightness(this, 20);
            }
        } else {
            setScreenBrightness(this, -1);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!isAppOnForeground()) {
            active = false;
            restoreSystemDisplayState();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void finish() {
        super.finish();
        ActivityStackManager.getActivityStackManager().finishActivity(this);
    }

    protected ApplicationComponent getQuApplicationComponent() {
        return ((QuApplication) getApplication()).getApplicationComponent();
    }

    public void collectException(Exception exception) {
        Logger.d(exception);
    }

    protected void setDisplayState() {
        if (sharedPreferences == null) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        }
        boolean autoBrightness = sharedPreferences.getBoolean("auto_brightness", true);
        if (!autoBrightness) {
            setReaderDisplayBrightness();
        }
        system_lock_time = getSystemScreenOffTimeout();
    }

    /**
     * 获取屏幕超时时间
     **/
    private int getSystemScreenOffTimeout() {
        int timeout = 0;
        try {
            timeout = Settings.System.getInt(this.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT);
        } catch (Settings.SettingNotFoundException exception) {
            collectException(exception);
            exception.printStackTrace();
        }
        return timeout;
    }

    protected void setReaderDisplayBrightness() {
        system_auto_brightness = isSystemAutoBrightness(this);
        if (system_auto_brightness) {
            stopAutoBrightness(this);
        }
        system_brightness = getScreenBrightness(this);
    }

    /**
     * 停止自动亮度调节
     **/
    public void stopAutoBrightness(Activity activity) {
        try {
            Settings.System.putInt(activity.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
        } catch (SecurityException exception) {
            collectException(exception);
            exception.printStackTrace();
        }
    }

    /**
     * 获取屏幕的亮度
     **/
    public int getScreenBrightness(Activity activity) {
        int nowBrightnessValue = 0;
        ContentResolver resolver = activity.getContentResolver();
        try {
            nowBrightnessValue = Settings.System.getInt(resolver, Settings.System.SCREEN_BRIGHTNESS);
        } catch (Exception exception) {
            collectException(exception);
            exception.printStackTrace();
        }
        return nowBrightnessValue;
    }

    /**
     * 判断系统是否开启了自动亮度调节
     **/
    public boolean isSystemAutoBrightness(Activity activity) {
        boolean autoBrightness = false;
        try {
            autoBrightness = Settings.System.getInt(activity.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
        } catch (Settings.SettingNotFoundException exception) {
            collectException(exception);
            exception.printStackTrace();
        }
        return autoBrightness;
    }

    /**
     * 保存当前的屏幕亮度值，并使之生效
     **/
    public void setScreenBrightness(Activity activity, int paramInt) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams localLayoutParams = window.getAttributes();
        localLayoutParams.screenBrightness = paramInt / 255.0F;
        window.setAttributes(localLayoutParams);
    }

    /**
     * 设置屏幕超时时间
     **/
    public void setScreenOffTimeout(int time) {
        try {
            if (time == Integer.MAX_VALUE) {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            } else {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                Settings.System.putInt(this.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, time);
            }
        } catch (Exception exception) {
            collectException(exception);
            exception.printStackTrace();
        }
    }

    /**
     * 程序是否在前台运行
     **/
    public boolean isAppOnForeground() {
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(ACTIVITY_SERVICE);

        String packageName = getApplication().getPackageName();

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null)
            return false;

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(packageName) && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }

        return false;
    }

    /**
     * 还原系统亮度
     */
    public void restoreSystemDisplayState() {
        if (sharedPreferences == null) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        }
        boolean autoBrightness = sharedPreferences.getBoolean("auto_brightness", true);
        if (!autoBrightness) {
            restoreBrightness();
        }
    }

    protected void restoreBrightness() {
        setScreenBrightness(this, -1);
        if (system_auto_brightness) {
            startAutoBrightness(this);
        }
    }

    /**
     * 开启亮度自动调节
     */
    public void startAutoBrightness(Activity activity) {
        try {
            Settings.System.putInt(activity.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
        } catch (Exception exception) {
            collectException(exception);
            exception.printStackTrace();
        }
    }

    protected boolean isModeChange() {
        boolean change = !ResourceUtil.mode.equals(mode);
        if (change) {
            this.mode = ResourceUtil.mode;
        }
        return change;
    }

    public interface FragmentCallback {
        void frameHelper();
    }
}
