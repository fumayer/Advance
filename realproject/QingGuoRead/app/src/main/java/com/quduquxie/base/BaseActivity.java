package com.quduquxie.base;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ContentResolver;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.quduquxie.application.QuApplication;
import com.quduquxie.application.SharedPreferencesUtil;
import com.quduquxie.application.component.ApplicationComponent;
import com.quduquxie.application.ApplicationUtil;
import com.quduquxie.base.config.BaseConfig;
import com.quduquxie.base.handler.CrashHandler;
import com.quduquxie.base.manager.ActivityStackManager;
import com.quduquxie.base.util.TypefaceUtil;

import java.util.List;

import javax.inject.Inject;

/**
 * Created on 17/7/12.
 * Created by crazylei.
 */

public abstract class BaseActivity<T extends BasePresenter> extends AppCompatActivity implements BaseView {

    @Inject
    protected T presenter;

    protected static boolean active;

    public static int system_brightness = 0;

    public static boolean system_auto_brightness;

    public ApplicationUtil applicationUtil;

    private SharedPreferencesUtil sharedPreferencesUtil;

    public Typeface typeface_song;
    public Typeface typeface_song_depict;

    private Toast toast;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        ApplicationComponent applicationComponent = QuApplication.getInstance().getApplicationComponent();

        setActivityComponent(applicationComponent);

        applicationUtil = applicationComponent.getApplicationUtil();
        sharedPreferencesUtil = applicationComponent.getSharedPreferencesUtil();

        ActivityStackManager.getActivityStackManager().insertActivity(this);

        if (presenter != null) {
            presenter.attachView();
        }

        typeface_song = TypefaceUtil.loadTypeface(this, TypefaceUtil.TYPEFACE_SONG);
        typeface_song_depict = TypefaceUtil.loadTypeface(this, TypefaceUtil.TYPEFACE_SONG_DEPICT);

        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!active) {
            active = true;
            checkBrightnessState();
        }

        if (!sharedPreferencesUtil.loadBoolean(BaseConfig.FLAG_BRIGHTNESS_FOLLOW_SYSTEM, true)) {
            int brightness = sharedPreferencesUtil.loadInteger(BaseConfig.FLAG_SCREEN_BRIGHTNESS, -1);
            if (brightness >= 0) {
                resetBrightnessValue(this, brightness);
            } else {
                resetBrightnessValue(this, system_brightness);
            }
        } else {
            system_brightness = loadCurrentBrightnessValue(this);

            resetBrightnessValue(this, system_brightness);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (!checkApplicationForeground()) {
            active = false;
            restoreSystemAutoBrightness();
        }
    }

    @Override
    protected void onDestroy() {

        if (presenter != null) {
            presenter.detachView();
        }

        recycle();

        super.onDestroy();
    }

    @Override
    public void finish() {
        super.finish();
        ActivityStackManager.getActivityStackManager().finishActivity(this);
    }

    @Override
    public void showPromptMessage(String message) {
        if (TextUtils.isEmpty(message)) {
            return;
        }

        try {
            if (toast == null) {
                toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
            } else {
                toast.setText(message);
                toast.setDuration(Toast.LENGTH_SHORT);
            }

            if (!isFinishing() && toast != null) {
                toast.show();
            }
        } catch (Exception exception) {
            collectException(exception);
        }
    }

    @Override
    public void changeNightMode() {

    }

    protected void checkBrightnessState() {
        boolean auto_brightness = sharedPreferencesUtil.loadBoolean(BaseConfig.FLAG_BRIGHTNESS_FOLLOW_SYSTEM, true);
        if (!auto_brightness) {
            loadBrightnessValue();
        }
    }

    protected void loadBrightnessValue() {
        system_auto_brightness = systemAutoBrightness(this);
        if (system_auto_brightness) {
            stopSystemAutoBrightness(this);
        }
        system_brightness = loadCurrentBrightnessValue(this);
    }

    /**
     * 判断系统自动亮度状态
     **/
    protected boolean systemAutoBrightness(Activity activity) {
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
     * 暂停系统自动亮度
     **/
    protected void stopSystemAutoBrightness(Activity activity) {
        try {
            Settings.System.putInt(activity.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
        } catch (SecurityException exception) {
            collectException(exception);
            exception.printStackTrace();
        }
    }

    /**
     * 获取当前亮度
     **/
    protected int loadCurrentBrightnessValue(Activity activity) {
        int brightnessValue = 0;
        ContentResolver contentResolver = activity.getContentResolver();
        try {
            brightnessValue = Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS);
        } catch (Exception exception) {
            collectException(exception);
            exception.printStackTrace();
        }
        return brightnessValue;
    }

    /**
     * 重置屏幕亮度
     **/
    protected void resetBrightnessValue(Activity activity, int value) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams localLayoutParams = window.getAttributes();
        localLayoutParams.screenBrightness = value / 255.0F;
        window.setAttributes(localLayoutParams);
    }

    /**
     * 重置系统自动亮度
     **/
    public void restoreSystemAutoBrightness() {
        boolean auto_brightness = sharedPreferencesUtil.loadBoolean(BaseConfig.FLAG_BRIGHTNESS_FOLLOW_SYSTEM, true);
        if (!auto_brightness) {
            restoreScreenBrightness();
        }
    }

    protected void restoreScreenBrightness() {
        resetBrightnessValue(this, -1);

        if (system_auto_brightness) {
            startSystemAutoBrightness(this);
        }
    }

    /**
     * 开启系统自动亮度
     **/
    public void startSystemAutoBrightness(Activity activity) {
        try {
            Settings.System.putInt(activity.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
        } catch (Exception exception) {
            collectException(exception);
            exception.printStackTrace();
        }
    }

    /**
     * 检查前台程序
     **/
    protected boolean checkApplicationForeground() {
        String packageName = getApplication().getPackageName();

        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfoList = activityManager.getRunningAppProcesses();

        if (processInfoList == null) {
            return false;
        }
        for (ActivityManager.RunningAppProcessInfo processInfo : processInfoList) {
            if (processInfo.processName.equals(packageName) && processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }

    public ApplicationUtil loadApplicationUtil() {
        return applicationUtil;
    }

    public SharedPreferencesUtil loadSharedPreferencesUtil() {
        return sharedPreferencesUtil;
    }

    public void collectException(Exception exception) {
        Logger.d(exception.toString());
//        CrashHandler.getCrashHandler().handleException(exception);
    }

    public void collectException(Throwable throwable) {
        Logger.d(throwable.toString());
//        CrashHandler.getCrashHandler().handleException(throwable);
    }

    public void inspectDownloadService() {
        if (applicationUtil.getDownloadService() == null) {
            applicationUtil.bindDownloadService();
        }
    }

    public void inspectCheckNovelUpdateService() {
        if (applicationUtil.getCheckNovelUpdateService() == null) {
            applicationUtil.bindCheckNovelUpdateService();
        }
    }

    protected abstract void setActivityComponent(ApplicationComponent applicationComponent);
}