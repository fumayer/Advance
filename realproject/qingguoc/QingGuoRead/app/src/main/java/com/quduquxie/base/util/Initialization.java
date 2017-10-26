package com.quduquxie.base.util;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;
import com.quduquxie.application.QuApplication;
import com.quduquxie.base.bean.Initialize;
import com.quduquxie.base.config.BaseConfig;
import com.quduquxie.base.retrofit.DataRequestFactory;
import com.quduquxie.base.retrofit.DataRequestInterface;
import com.quduquxie.base.retrofit.bean.CommunalResult;
import com.quduquxie.communal.utils.DeviceUtil;
import com.quduquxie.db.CommentLikeDao;
import com.quduquxie.db.UserDao;
import com.quduquxie.model.User;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created on 17/7/13.
 * Created by crazylei.
 */

public class Initialization {


    private static String SDCARD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    private static String APP_PATH = SDCARD_PATH + "/quduquxie";
    private static String APP_PATH_CACHE = APP_PATH + "/cache/";

    public static String APP_PATH_BOOK = APP_PATH + "/book/";
    public static String APP_PATH_DOWNLOAD = APP_PATH + "/download/";
    public static String APP_PATH_IMAGE = APP_PATH + "/image/";
    public static String APP_PATH_GLIDE = APP_PATH + "/glide/";
    public static String APP_PATH_LOCAL = APP_PATH + "/local/";
    public static String APP_PATH_AVATAR = APP_PATH + "/avatar/";
    public static String APP_PATH_LOG = APP_PATH + "/log/";


    public static void initApplicationParameter(final Context context) {

        final Map<String, String> information = new HashMap<>();

        String register = MiPushClient.getRegId(context);

        if (!TextUtils.isEmpty(register)) {
            information.put("regId", register);
        } else {
            return;
        }
        String address = DeviceUtil.getMacAddress();
        if (!TextUtils.isEmpty(address)) {
            information.put("mac", address);
        }

        final UserDao userDao = UserDao.getInstance(context);

        final User user = userDao.getUser();

        if (user != null) {
            if (!TextUtils.isEmpty(user.token)) {
                UserDao.setToken(user.token);
                information.put("token", user.token);
            }
        }

        Logger.d(information);

        DataRequestInterface dataRequestInterface = DataRequestFactory.instantiation(DataRequestInterface.class, DataRequestFactory.TYPE_HTTP, false);
        dataRequestInterface.initializeParameter(information)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .doOnNext(new Consumer<CommunalResult<Initialize>>() {
                    @Override
                    public void accept(@NonNull CommunalResult<Initialize> initializeResult) throws Exception {
                        if (initializeResult.getCode() == 0 && initializeResult.getModel() != null) {
                            if (user != null && !TextUtils.isEmpty(initializeResult.getModel().token)) {

                                Logger.d("InitializeParameter call: 设置Token " + initializeResult.getModel().token);

                                user.token = initializeResult.getModel().token;
                                userDao.updateUser(user, UserDao.getToken(context));
                                UserDao.setToken(initializeResult.getModel().token);
                            }

                            if (initializeResult.getModel().messages != null) {

                                Logger.d("InitializeParameter call: 消息数量 " + initializeResult.getModel().messages.comments_count + " : " + initializeResult.getModel().messages.reply_count);

                                QuApplication.getInstance().setCommentsMessage(initializeResult.getModel().messages.comments_count, initializeResult.getModel().messages.reply_count);
                            }
                        }
                    }
                })
                .observeOn(Schedulers.io())
                .doOnNext(new Consumer<CommunalResult<Initialize>>() {
                    @Override
                    public void accept(@NonNull CommunalResult<Initialize> initializeResult) throws Exception {
                        if (initializeResult.getCode() == 0 && initializeResult.getModel() != null && initializeResult.getModel().likes != null && initializeResult.getModel().likes.size() > 0) {

                            Logger.d("InitializeParameter call: 数据库操作");

                            CommentLikeDao commentLikeDao = CommentLikeDao.getInstance(context);
                            ArrayList<String> comments = commentLikeDao.getCommentIDList();
                            for (String id : initializeResult.getModel().likes) {
                                if (comments.contains(id)) {
                                    comments.remove(id);
                                } else {
                                    commentLikeDao.insertComment(id);
                                }
                            }

                            if (comments != null && comments.size() > 0) {
                                for (String id : comments) {
                                    commentLikeDao.deleteComment(id);
                                }
                            }

                            Logger.d("InitializeParameter call: 数据库操作完成");
                        }
                    }
                })
                .observeOn(Schedulers.io())
                .doOnNext(new Consumer<CommunalResult<Initialize>>() {
                    @Override
                    public void accept(@NonNull CommunalResult<Initialize> initializeResult) throws Exception {
                        if (initializeResult != null && initializeResult.getCode() == 0 && initializeResult.getModel() != null && !TextUtils.isEmpty(initializeResult.getModel().alias)) {

                            Logger.d("InitializeParameter call: 设置别名 " + initializeResult.getModel().alias);

                            String alias = initializeResult.getModel().alias;
                            if (!TextUtils.isEmpty(alias)) {
                                MiPushClient.setAlias(context, alias, null);
                            }
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResourceSubscriber<CommunalResult<Initialize>>() {
                    @Override
                    public void onNext(CommunalResult<Initialize> initializeResult) {
                        Logger.d("InitializeParameter onNext");
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Logger.d("InitializeParameter onError: " + throwable.toString());
                    }

                    @Override
                    public void onComplete() {
                        Logger.d("InitializeParameter onComplete");
                    }
                });
    }

    public static void initializeFile(Context context) {
        boolean cardState = android.os.Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());

        if (!cardState) {
            SDCARD_PATH = "mnt/sdcard";
        }

        File file = new File(APP_PATH);
        if (!file.exists()) {
            boolean result = file.mkdirs();

            Logger.d("InitializeFile: " + APP_PATH + " : " + result);
        }

        file = new File(APP_PATH_BOOK);
        if (!file.exists()) {
            boolean result = file.mkdirs();

            Logger.d("InitializeFile: " + APP_PATH_BOOK + " : " + result);
        }

        file = new File(APP_PATH_CACHE);
        if (!file.exists()) {
            boolean result = file.mkdirs();

            Logger.d("InitializeFile: " + APP_PATH_CACHE + " : " + result);
        }

        file = new File(APP_PATH_DOWNLOAD);
        if (!file.exists()) {
            boolean result = file.mkdirs();

            Logger.d("InitializeFile: " + APP_PATH_DOWNLOAD + " : " + result);
        }

        file = new File(APP_PATH_IMAGE);
        if (!file.exists()) {
            boolean result = file.mkdirs();

            Logger.d("InitializeFile: " + APP_PATH_IMAGE + " : " + result);
        }

        file = new File(APP_PATH_GLIDE);
        if (!file.exists()) {
            boolean result = file.mkdirs();

            Logger.d("InitializeFile: " + APP_PATH_GLIDE + " : " + result);
        }

        file = new File(APP_PATH_LOCAL);
        if (!file.exists()) {
            boolean result = file.mkdirs();

            Logger.d("InitializeFile: " + APP_PATH_LOCAL + " : " + result);
        }

        file = new File(APP_PATH_AVATAR);
        if (!file.exists()) {
            boolean result = file.mkdirs();

            Logger.d("InitializeFile: " + APP_PATH_AVATAR + " : " + result);
        }

        if (BaseConfig.DEVELOP_MODE) {
            file = new File(APP_PATH_LOG);

            if (!file.exists()) {
                boolean result = file.mkdirs();
                Logger.d("InitializeFile: " + APP_PATH_LOG + " : " + result);
            }
        }

        NetworkUtil.loadNetworkType(context);
    }
}