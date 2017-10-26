package com.quduquxie.base.util;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.text.format.DateFormat;
import android.widget.RemoteViews;

import com.orhanobut.logger.Logger;
import com.quduquxie.R;
import com.quduquxie.base.bean.Update;
import com.quduquxie.base.config.BaseConfig;
import com.quduquxie.base.widget.dialog.CustomDialogUpdateFragment;
import com.quduquxie.base.widget.dialog.CustomDialogUpdateListener;
import com.quduquxie.service.UpdateAppService;

import java.util.Date;

/**
 * Created on 17/7/28.
 * Created by crazylei.
 */

public class AppUpdateDialogHelper {

    private Activity activity;
    private CustomDialogUpdateFragment customDialogUpdateFragment;

    private UpdateDialogCallback updateDialogCallback;

    private int NOTIFICATION_FLAG = AppUpdateDialogHelper.class.getSimpleName().hashCode();

    public AppUpdateDialogHelper(Activity activity) {
        this.activity = activity;
    }

    public void apiUpdate(Update update, boolean manualCheck, boolean activityFinish, FragmentManager fragmentManager) {
        Logger.e("应用检查更新：" + manualCheck + " : " + update.force + " : " + update.code);

        if (manualCheck) {
            apiUpdateVersionDialog(false, update, false, fragmentManager);
        } else if (update.force) {
            apiUpdateVersionDialog(activityFinish, update, false, fragmentManager);
        } else {
            if (NetworkUtil.NETWORK_WIFI == NetworkUtil.loadNetworkType(activity)) {
                autoUpdateDownload(activity, update, true, false);
            }

            SharedPreferences sharedPreferences = activity.getSharedPreferences(BaseConfig.FLAG_SHARE_PREFERENCE_NAME, Context.MODE_PRIVATE);
            long checkVersionTime = sharedPreferences.getLong(BaseConfig.FLAG_CHECK_VERSION_TIME, 0);

            if (CommunalUtil.checkVersionState(checkVersionTime)) {
                apiUpdateVersionDialog(activityFinish, update, false, fragmentManager);
            }
        }
    }

    private void apiUpdateVersionDialog(final boolean activityState, final Update update, final boolean autoDownload, FragmentManager fragmentManager) {
        if (!activityState) {

            SharedPreferences sharedPreferences = activity.getSharedPreferences(BaseConfig.FLAG_SHARE_PREFERENCE_NAME, Context.MODE_PRIVATE);
            sharedPreferences.edit().putLong(BaseConfig.FLAG_CHECK_VERSION_TIME, new Date().getTime()).apply();

            if (activity == null || activity.isFinishing() || update == null) {
                return;
            }

            if (customDialogUpdateFragment == null) {
                customDialogUpdateFragment = new CustomDialogUpdateFragment();
            }

            String[] descriptions = update.desc.split(";");
            String desc = "";
            for (int i = 0; i < descriptions.length; i++) {
                if (i != descriptions.length - 1) {
                    desc += descriptions[i] + "\n\n";
                } else {
                    desc += descriptions[i];
                }
            }

            customDialogUpdateFragment.setVersion(update.name);
            customDialogUpdateFragment.setDescription(desc);
            customDialogUpdateFragment.setCustomDialogUpdateListener(new CustomDialogUpdateListener() {
                @Override
                public void onCancelClicked() {
                    if (update.force) {
                        if (updateDialogCallback != null) {
                            updateDialogCallback.appUpdateDialogCancel(true);
                        }
                    } else {
                        hideCustomDialogFragment();
                    }
                }

                @Override
                public void onConfirmClicked() {
                    if (customDialogUpdateFragment.getShowsDialog()) {
                        hideCustomDialogFragment();
                        autoUpdateDownload(activity, update, autoDownload, true);
                    }
                }
            });

            if (!activity.isFinishing()) {
                if (customDialogUpdateFragment.isAdded()) {
                    customDialogUpdateFragment.setShowsDialog(true);
                } else {
                    customDialogUpdateFragment.show(fragmentManager, "CustomDialogUpdateFragment");
                }
            }
        } else {
            updateDownNotification(activity, update, autoDownload);
        }
    }

    private void hideCustomDialogFragment() {
        if (!activity.isFinishing() && customDialogUpdateFragment != null && customDialogUpdateFragment.getShowsDialog()) {
            customDialogUpdateFragment.dismiss();
        }
    }

    public static void autoUpdateDownload(Activity activity, Update update, boolean autoDownload, boolean isInstall) {
        Intent intent = new Intent(activity, UpdateAppService.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("updateBean", update);
        intent.putExtra("show_notify", !autoDownload);
        intent.putExtra("isInstall", isInstall);

        if (activity != null) {
            activity.startService(intent);
        }
    }

    private void updateDownNotification(Context context, Update update, boolean autoDownload) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent;

        PendingIntent pendingIntent;

        if (update != null) {
            intent = new Intent(context, UpdateAppService.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("updateBean", update);
            intent.putExtra("show_notify", !autoDownload);
            pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.layout_notification_app_update);
            remoteViews.setTextViewText(R.id.notification_title, "有新版本:" + update.name);
            remoteViews.setTextViewText(R.id.notification_content, update.desc);
            remoteViews.setTextViewText(R.id.notification_time, BaseConfig.simpleDateFormat.format(new Date()));
            remoteViews.setImageViewResource(R.id.notification_icon, R.drawable.icon_notification);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            builder.setContent(remoteViews)
                    .setSmallIcon(R.drawable.icon_notification_small)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_notification))
                    .setContentIntent(pendingIntent)
                    .setWhen(System.currentTimeMillis())
                    .setOngoing(true);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                builder.setPriority(Notification.PRIORITY_HIGH);
            }

            Notification notification = builder.build();
            notification.flags = Notification.FLAG_AUTO_CANCEL;

            if (notificationManager != null) {
                notificationManager.notify(NOTIFICATION_FLAG, notification);
            }
        }
    }

    public void setUpdateDialogCallback(UpdateDialogCallback updateDialogCallback) {
        this.updateDialogCallback = updateDialogCallback;
    }

    public interface UpdateDialogCallback {
        void appUpdateDialogCancel(boolean cancel);
    }
}