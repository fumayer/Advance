package com.quduquxie.receiver;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;
import com.quduquxie.application.QuApplication;
import com.quduquxie.base.bean.Book;
import com.quduquxie.base.database.helper.BookDaoHelper;
import com.quduquxie.base.module.main.activity.view.MainActivity;
import com.quduquxie.modular.cover.view.CoverActivity;
import com.quduquxie.module.comment.view.ReceivedCommentsActivity;
import com.quduquxie.module.comment.view.ReceivedRepliesActivity;
import com.quduquxie.module.read.reading.view.ReadingActivity;
import com.xiaomi.mipush.sdk.ErrorCode;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageReceiver;

import java.util.List;
import java.util.Map;

/**
 * Created on 16/12/30.
 * Created by crazylei.
 */

public class MiPushReceiver extends PushMessageReceiver {
    private String register_id;
    private String topic;
    private String alias;
    private String content;
    private String userAccount;

    @Override
    public void onReceivePassThroughMessage(Context context, MiPushMessage message) {
        content = message.getContent();
        Logger.d("MiPush onReceivePassThroughMessage: " + message.toString());
        if (!TextUtils.isEmpty(message.getTopic())) {
            topic = message.getTopic();
        } else if (!TextUtils.isEmpty(message.getAlias())) {
            alias = message.getAlias();
        } else if (!TextUtils.isEmpty(message.getUserAccount())) {
            userAccount = message.getUserAccount();
        }
    }

    @Override
    public void onNotificationMessageClicked(Context context, MiPushMessage message) {
        Logger.d("MiPush onNotificationMessageClicked: " + message.toString());
        content = message.getContent();
        if (!TextUtils.isEmpty(message.getTopic())) {
            topic = message.getTopic();
        } else if (!TextUtils.isEmpty(message.getAlias())) {
            alias = message.getAlias();
        } else if (!TextUtils.isEmpty(message.getUserAccount())) {
            userAccount = message.getUserAccount();
        }
        Map<String, String> parameter = message.getExtra();
        if (parameter != null) {
            String book_id = "";
            String push_type = "";
            int serial_number = 0;
            if (parameter.containsKey("bi")) {
                book_id = parameter.get("bi");
            }
            if (parameter.containsKey("pt")) {
                push_type = parameter.get("pt");
            }
            if (parameter.containsKey("sn")) {
                serial_number = Integer.valueOf(parameter.get("sn"));
            }

            if (push_type.equals("0")) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                BookDaoHelper bookDaoHelper = BookDaoHelper.getInstance(context);
                if (TextUtils.isEmpty(book_id)) {
                    intent.setClass(context, MainActivity.class);
                } else {
                    if (bookDaoHelper.subscribeBook(book_id)) {
                        Book book = bookDaoHelper.loadBook(book_id, Book.TYPE_ONLINE);
                        intent.setClass(context, ReadingActivity.class);
                        if (serial_number >= book.chapters_update_index) {
                            bundle.putBoolean("need_update", true);
                        }
                        bundle.putInt("offset", book.offset);
                        bundle.putInt("sequence", book.sequence);
                        bundle.putSerializable("book", book);
                    } else {
                        intent.setClass(context, CoverActivity.class);
                        bundle.putString("id", book_id);
                    }
                }
                intent.putExtras(bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } else if (push_type.equals("1")) {
                Intent coverIntent = new Intent();
                if (TextUtils.isEmpty(book_id)) {
                    coverIntent.setClass(context, MainActivity.class);
                } else {
                    coverIntent.setClass(context, CoverActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("id", book_id);
                    coverIntent.putExtras(bundle);
                }
                coverIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(coverIntent);
            } else if (push_type.equals("2")) {
                Intent reviewIntent = new Intent();
                reviewIntent.setClass(context, ReceivedCommentsActivity.class);
                reviewIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(reviewIntent);
            } else if (push_type.equals("3")) {
                Intent reviewIntent = new Intent();
                reviewIntent.setClass(context, ReceivedRepliesActivity.class);
                reviewIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(reviewIntent);
            }
        }
    }

    @Override
    public void onNotificationMessageArrived(Context context, MiPushMessage message) {
        content = message.getContent();
        Logger.d("MiPush onNotificationMessageArrived: " + message.toString());
        if (!TextUtils.isEmpty(message.getTopic())) {
            topic = message.getTopic();
        } else if (!TextUtils.isEmpty(message.getAlias())) {
            alias = message.getAlias();
        } else if (!TextUtils.isEmpty(message.getUserAccount())) {
            userAccount = message.getUserAccount();
        }

        Map<String, String> parameter = message.getExtra();
        if (parameter != null) {
            String book_id = "";
            String push_type = "";
            int serial_number = 0;
            if (parameter.containsKey("bi")) {
                book_id = parameter.get("bi");
            }
            if (parameter.containsKey("pt")) {
                push_type = parameter.get("pt");
            }
            if (parameter.containsKey("sn")) {
                serial_number = Integer.valueOf(parameter.get("sn"));
            }

//            if (push_type.equals("2")) {
//                QuApplication.getInstance().addCommentsCount();
//                Intent intent = new Intent(MainActivity.CommentNotificationReceiver.ACTION_COMMENT_COUNT_CHANGE);
//                context.sendBroadcast(intent);
//            } else if (push_type.equals("3")) {
//                QuApplication.getInstance().addReplyCount();
//                Intent intent = new Intent(MainActivity.CommentNotificationReceiver.ACTION_COMMENT_COUNT_CHANGE);
//                context.sendBroadcast(intent);
//            }
        }
    }

    @Override
    public void onCommandResult(Context context, MiPushCommandMessage message) {
        String command = message.getCommand();
        List<String> arguments = message.getCommandArguments();
        String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);

        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                register_id = cmdArg1;
            }
        } else if (MiPushClient.COMMAND_SET_ALIAS.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                alias = cmdArg1;
            }
        } else if (MiPushClient.COMMAND_UNSET_ALIAS.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                alias = cmdArg1;
            }
        } else if (MiPushClient.COMMAND_SUBSCRIBE_TOPIC.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                topic = cmdArg1;
            }
        } else if (MiPushClient.COMMAND_UNSUBSCRIBE_TOPIC.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                topic = cmdArg1;
            }
        }
    }

    @Override
    public void onReceiveRegisterResult(Context context, MiPushCommandMessage message) {
        String command = message.getCommand();
        List<String> arguments = message.getCommandArguments();
        String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                register_id = cmdArg1;
            }
        }
    }
}