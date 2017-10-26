package com.quduquxie.function;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.quduquxie.application.QuApplication;
import com.quduquxie.application.component.ApplicationComponent;
import com.quduquxie.application.ApplicationUtil;
import com.quduquxie.base.manager.ActivityStackManager;
import com.quduquxie.base.util.TypefaceUtil;

import java.util.List;

/**
 * Created on 17/3/15.
 * Created by crazylei.
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected static boolean active;

    public static int system_brightness = 0;

    public static boolean system_auto_brightness;

    private SharedPreferences sharedPreferences;

    private ApplicationComponent applicationComponent;

    public ApplicationUtil applicationUtil;

    public Typeface typeface_song;
    public Typeface typeface_song_depict;

    private Toast toast;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        applicationComponent = QuApplication.getInstance().getApplicationComponent();

        applicationUtil = applicationComponent.getApplicationUtil();

        setActivityComponent(applicationComponent);

        ActivityStackManager.getActivityStackManager().insertActivity(this);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        typeface_song = TypefaceUtil.loadTypeface(getQuApplicationContext(), TypefaceUtil.TYPEFACE_SONG);
        typeface_song_depict = TypefaceUtil.loadTypeface(getQuApplicationContext(), TypefaceUtil.TYPEFACE_SONG_DEPICT);
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
            setScreenBrightnessState();
        }

        if (sharedPreferences != null && !sharedPreferences.getBoolean("auto_brightness", true)) {
            int screen_bright = sharedPreferences.getInt("screen_bright", -1);
            if (screen_bright >= 0) {
                setScreenBrightnessValue(this, 20 + screen_bright);
            } else if (system_brightness >= 20) {
                setScreenBrightnessValue(this, system_brightness);
            } else {
                setScreenBrightnessValue(this, 20);
            }
        } else {
            setScreenBrightnessValue(this, -1);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (!checkAppOnForeground()) {
            active = false;
            restoreSystemAutoBrightnessState();
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

    protected void setScreenBrightnessState() {
        if (sharedPreferences == null) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        }
        boolean autoBrightness = sharedPreferences.getBoolean("auto_brightness", true);
        if (!autoBrightness) {
            getScreenBrightnessValue();
        }
    }

    protected void getScreenBrightnessValue() {
        system_auto_brightness = checkSystemAutoBrightnessState(this);
        if (system_auto_brightness) {
            stopAutoBrightnessState(this);
        }
        system_brightness = loadScreenBrightnessValue(this);
    }


    protected boolean checkSystemAutoBrightnessState(Activity activity) {
        boolean autoBrightness = false;
        try {
            autoBrightness = Settings.System.getInt(activity.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
        } catch (Settings.SettingNotFoundException exception) {
            collectException(exception);
            exception.printStackTrace();
        }
        return autoBrightness;
    }

    protected void stopAutoBrightnessState(Activity activity) {
        try {
            Settings.System.putInt(activity.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
        } catch (SecurityException exception) {
            collectException(exception);
            exception.printStackTrace();
        }
    }

    protected int loadScreenBrightnessValue(Activity activity) {
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

    protected void setScreenBrightnessValue(Activity activity, int value) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams localLayoutParams = window.getAttributes();
        localLayoutParams.screenBrightness = value / 255.0F;
        window.setAttributes(localLayoutParams);
    }

    protected boolean checkAppOnForeground() {
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


    public void restoreSystemAutoBrightnessState() {
        if (sharedPreferences == null) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        }
        boolean autoBrightness = sharedPreferences.getBoolean("auto_brightness", true);
        if (!autoBrightness) {
            restoreScreenBrightnessValue();
        }
    }

    protected void restoreScreenBrightnessValue() {
        setScreenBrightnessValue(this, -1);
        if (system_auto_brightness) {
            startAutoBrightnessState(this);
        }
    }

    public void startAutoBrightnessState(Activity activity) {
        try {
            Settings.System.putInt(activity.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
        } catch (Exception exception) {
            collectException(exception);
            exception.printStackTrace();
        }
    }

    public QuApplication getQuApplication() {
        return applicationComponent.getQuApplication();
    }

    public ApplicationUtil getApplicationUtil() {
        return applicationUtil;
    }

    public Context getQuApplicationContext() {
        return applicationComponent.getQuApplication().getApplicationContext();
    }

    public void collectException(Exception exception) {
        if (applicationUtil == null) {
            applicationUtil = applicationComponent.getApplicationUtil();
        }
        applicationUtil.collectException(exception);
    }

    public void collectException(Throwable throwable) {
        if (applicationUtil == null) {
            applicationUtil = applicationComponent.getApplicationUtil();
        }
        applicationUtil.collectException(throwable);
    }

    public void showPrompt(String message) {
        if (TextUtils.isEmpty(message)) {
            return;
        }
        if (toast == null) {
            toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        } else {
            toast.setText(message);
            toast.setDuration(Toast.LENGTH_SHORT);
        }

        if (!isFinishing() && toast != null) {
            toast.show();
        }
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

    public interface FragmentCallback {
        void frameHelper();
    }
}
