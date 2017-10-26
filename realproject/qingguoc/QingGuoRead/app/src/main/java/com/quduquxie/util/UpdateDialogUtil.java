package com.quduquxie.util;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.widget.RemoteViews;

import com.orhanobut.logger.Logger;
import com.quduquxie.R;
import com.quduquxie.base.bean.Update;
import com.quduquxie.base.config.BaseConfig;
import com.quduquxie.base.util.CommunalUtil;
import com.quduquxie.base.util.NetworkUtil;
import com.quduquxie.base.widget.dialog.CustomDialogUpdateFragment;
import com.quduquxie.base.widget.dialog.CustomDialogUpdateListener;
import com.quduquxie.service.UpdateAppService;

import java.util.Date;

public class UpdateDialogUtil {
    private Activity activity;
    private CustomDialogUpdateFragment customDialogUpdateFragment;

    private UpdateDialogCallback updateDialogCallback;

    public UpdateDialogUtil(Activity activity) {
        this.activity = activity;
    }

    public void apiUpdate(Update update, boolean manualCheck, boolean activityState, FragmentManager fragmentManager) {
        if (manualCheck) {

            Logger.e("UpdateDialogUtil ManualCheck true: " + update.code);

            apiUpdateVersionDialog(false, update, false, fragmentManager);
        } else if (update.force) {

            Logger.e("UpdateDialogUtil Forced true: " + update.code);

            apiUpdateVersionDialog(activityState, update, false, fragmentManager);
        } else {
            if (NetworkUtil.NETWORK_TYPE == NetworkUtil.NETWORK_WIFI) {

                Logger.e("UpdateDialogUtil WiFi Available: " + update.code);

                autoUpdateDownload(activity, update, true, false);
            }

            SharedPreferences sharedPreferences = activity.getSharedPreferences(BaseConfig.FLAG_SHARE_PREFERENCE_NAME, Context.MODE_PRIVATE);
            long checkVersionTime = sharedPreferences.getLong(BaseConfig.FLAG_CHECK_VERSION_TIME, 0);

            Logger.e("UpdateDialogUtil Check Version Time: " + update.code);

            if (CommunalUtil.checkVersionState(checkVersionTime)) {

                Logger.e("UpdateDialogUtil Check Version Time: " + update.code);

                apiUpdateVersionDialog(activityState, update, false, fragmentManager);
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
                            updateDialogCallback.apiDialogCancel(true);
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

    public static void autoUpdateDownload(Activity activity, Update update, boolean isAutoDownLoad, boolean isInstall) {
        boolean showNotify = !isAutoDownLoad;

        Intent intent = new Intent(activity, UpdateAppService.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("updateBean", update);
        intent.putExtra("show_notify", showNotify);
        intent.putExtra("isInstall", isInstall);

        if (activity != null) {
            activity.startService(intent);
        }
    }


    //TODO 修改通知展示方式
    public static void updateDownNotification(Context context, Update update, boolean isAutoDownLoad) {
        boolean showNotify = !isAutoDownLoad;

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification();
        notification.tickerText = context.getResources().getString(R.string.update_new_version_ticker) + update.code;
        notification.icon = R.drawable.icon_launcher;
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        notification.contentView = new RemoteViews(context.getPackageName(), R.layout.notify_update_content);
        notification.contentView.setTextViewText(R.id.textView1, update.desc);// FIXME

        Intent intent = new Intent(context, UpdateAppService.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("updateBean", update);
        intent.putExtra("show_notify", showNotify);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.contentIntent = pendingIntent;

        manager.notify(1, notification);
    }

    public void setUpdateDialogCallback(UpdateDialogCallback updateDialogCallback) {
        this.updateDialogCallback = updateDialogCallback;
    }

    public interface UpdateDialogCallback {
        void apiDialogCancel(boolean isForceUpdate);
    }
}
