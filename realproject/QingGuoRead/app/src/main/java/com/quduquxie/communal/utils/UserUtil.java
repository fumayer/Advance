package com.quduquxie.communal.utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.quduquxie.db.CommentLikeDao;
import com.quduquxie.db.DraftDao;
import com.quduquxie.db.LiteratureDao;
import com.quduquxie.db.SectionDao;
import com.quduquxie.db.UserDao;
import com.quduquxie.model.creation.Literature;
import com.quduquxie.model.Token;
import com.quduquxie.model.User;
import com.quduquxie.retrofit.DataRequestService;
import com.quduquxie.retrofit.ServiceGenerator;
import com.quduquxie.retrofit.model.CommunalResult;
import com.quduquxie.wxapi.WXEntryActivity;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created on 16/12/2.
 * Created by crazylei.
 */

public class UserUtil {

    public static void deleteUser(Context context) {
        UserDao userDao = UserDao.getInstance(context);
        userDao.deleteUser();
    }

    public static void deleteLiterature(Context context) {
        DraftDao draftDao = DraftDao.getInstance(context);
        draftDao.deleteAllDraft();

        LiteratureDao literatureDao = LiteratureDao.getInstance(context);
        ArrayList<Literature> literatureList = literatureDao.getLiteratureList();

        SectionDao sectionDao;
        for (Literature literature : literatureList) {
            sectionDao = new SectionDao(context, literature.id);
            sectionDao.deleteSections(0);
        }
        literatureDao.deleteAllLiterature();
    }

    public static void deleteCommentLike(Context context) {
        CommentLikeDao commentLikeDao = CommentLikeDao.getInstance(context);
        commentLikeDao.deleteAllComment();
    }


    public static void refreshToken(final Context context) {

        final UserDao userDao = UserDao.getInstance(context);

        final User user = userDao.getUser();

        if (user != null) {
            if (!TextUtils.isEmpty(user.token)) {

                UserDao.setToken(user.token);

                DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, true);
                dataRequestService.reviserToken()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(Schedulers.io())
                        .doOnNext(new Consumer<CommunalResult<Token>>() {
                            @Override
                            public void accept(@NonNull CommunalResult<Token> tokenResult) throws Exception {
                                if (tokenResult != null && tokenResult.getCode() == 0 && tokenResult.getModel() != null) {
                                    Logger.d("ReviserToken call 数据库操作");
                                    if (tokenResult.getModel().is_refresh == 1) {
                                        user.token = tokenResult.getModel().token;
                                        userDao.updateUser(user, UserDao.getToken(context));
                                        UserDao.setToken(tokenResult.getModel().token);
                                    }
                                    Logger.d("ReviserToken call 数据库操作结束");
                                }
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ResourceSubscriber<CommunalResult<Token>>() {
                            @Override
                            public void onNext(CommunalResult<Token> tokenResult) {
                                if (tokenResult != null) {
                                    if (tokenResult.getCode() == 401) {
                                        Toast.makeText(context, "登录令牌失效，请重新登录！", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent();
                                        intent.putExtra("exit_login", true);
                                        intent.setClass(context, WXEntryActivity.class);
                                        context.startActivity(intent);
                                    } else {
                                        if (tokenResult.getCode() == 0 && tokenResult.getModel() != null) {
                                            Logger.d("ReviserToken onNext: " + tokenResult.getModel().toString());
                                        } else if (!TextUtils.isEmpty(tokenResult.getMessage())) {
                                            Toast.makeText(context, tokenResult.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                } else {
                                    Toast.makeText(context, "令牌刷新失败！", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                Logger.d("ReviserToken onError: " + throwable.toString());
                                Toast.makeText(context, "令牌刷新失败，请检查网络连接！", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onComplete() {
                                Logger.d("ReviserToken onComplete");
                            }
                        });
            }
        }
    }
}
