package com.quduquxie.service.check;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.quduquxie.base.module.main.activity.view.MainActivity;

public class CheckNovelUpdateReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        CheckNovelUpdateHelper.dealLocalNotification(context);

        if (intent != null) {
            int type = intent.getIntExtra("push_type", CheckNovelUpdateService.TYPE_CHECK_NOVEL_UPDATE);

            if (type == CheckNovelUpdateService.TYPE_CHECK_NOVEL_UPDATE) {
                Intent checkIntent = new Intent();
                checkIntent.setClass(context, MainActivity.class);
                checkIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                String id = intent.getStringExtra("id");

                if (!TextUtils.isEmpty(id)) {
                    checkIntent.putExtra("id", id);
                    checkIntent.putExtra("notification", true);
                }

                context.startActivity(checkIntent);
            }
        }
    }
}