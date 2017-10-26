package com.quduquxie.base.handler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;

import com.orhanobut.logger.Logger;
import com.quduquxie.application.QuApplication;
import com.quduquxie.base.config.BaseConfig;
import com.quduquxie.base.manager.ActivityStackManager;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created on 17/7/12.
 * Created by crazylei.
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private Thread.UncaughtExceptionHandler uncaughtExceptionHandler;

    private Context context;

    private Map<String, String> parameters = new HashMap<>();

    @SuppressLint("StaticFieldLeak")
    private static CrashHandler crashHandler;

    private CrashHandler() {

    }

    public static synchronized CrashHandler getCrashHandler() {
        if (null == crashHandler) {
            crashHandler = new CrashHandler();
        }
        return crashHandler;
    }

    public void initialize(Context context) {
        this.context = context;

        uncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();

        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        if (!handleException(throwable) && uncaughtExceptionHandler != null) {
            uncaughtExceptionHandler.uncaughtException(thread, throwable);
        } else {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException interruptedException) {
                Logger.e("UncaughtException: " + interruptedException);
            }

            ActivityStackManager.getActivityStackManager().exitApplication(context);
        }
    }

    public boolean handleException(Throwable throwable) {
        if (throwable == null) {
            return false;
        }

        collectDeviceInformation();

        insertCustomInformation();

        sentCrashException(throwable);
        return true;
    }

    private void collectDeviceInformation() {

        parameters.put("versionName", QuApplication.getVersionName());
        parameters.put("versionCode", String.valueOf(QuApplication.getVersionCode()));

        Field[] fields = Build.class.getDeclaredFields();

        for (Field field : fields) {
            try {
                field.setAccessible(true);
                parameters.put(field.getName(), field.get(null).toString());
            } catch (Exception exception) {
                Logger.e("CollectDeviceInformation: " + exception);
            }
        }
    }

    private void insertCustomInformation() {
        parameters.put("exceptionTime", BaseConfig.simpleDateFormat.format(new Date()));
    }

    private void sentCrashException(Throwable throwable) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry entry : parameters.entrySet()) {
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            stringBuilder.append(key).append("=").append(value).append("\n");
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);

        throwable.printStackTrace(printWriter);

        Throwable cause = throwable.getCause();

        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }

        printWriter.close();

        stringBuilder.append(writer.toString());

        Logger.e("SentCrashException: " + stringBuilder.toString());
    }
}