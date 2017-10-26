package com.quduquxie.revise.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.quduquxie.application.QuApplication;
import com.quduquxie.communal.utils.BookUtil;
import com.quduquxie.model.Alias;
import com.quduquxie.model.QQ;
import com.quduquxie.model.ReviseUser;
import com.quduquxie.model.Token;
import com.quduquxie.model.UploadShelfResult;
import com.quduquxie.model.User;
import com.quduquxie.communal.utils.RSAUtil;
import com.quduquxie.communal.utils.UserUtil;
import com.quduquxie.db.UserDao;
import com.quduquxie.retrofit.DataRequestService;
import com.quduquxie.retrofit.ServiceGenerator;
import com.quduquxie.retrofit.model.CommunalResult;
import com.quduquxie.revise.ReviseUserInterface;
import com.xiaomi.mipush.sdk.MiPushClient;

import org.json.JSONException;
import org.reactivestreams.Publisher;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;
import okhttp3.RequestBody;

/**
 * Created on 16/12/5.
 * Created by crazylei.
 */

public class ReviseUserPresenter implements ReviseUserInterface.Presenter {

    private ReviseUserInterface.View reviseUserView;
    private WeakReference<Context> contextReference;
    private UserDao userDao;

    private User user;

    public ReviseUserPresenter(@NonNull ReviseUserInterface.View reviseUserView, Context context) {
        this.reviseUserView = reviseUserView;
        this.contextReference = new WeakReference<>(context);
        this.userDao = UserDao.getInstance(contextReference.get());
    }

    @Override
    public void init() {
        if (reviseUserView != null) {
            reviseUserView.initView();
        }
    }

    @Override
    public void initParameter() {
        if (userDao == null) {
            userDao = UserDao.getInstance(contextReference.get());
        }
        user = userDao.getUser();
        if (user != null) {
            if (reviseUserView != null) {
                reviseUserView.showUserFragment();
            }
        } else {
            if (reviseUserView != null) {
                reviseUserView.showErrorFragment();
            }
        }
    }

    @Override
    public void reviseUserTelephoneNumber(final String telephone_number, String verification_code, String password) {

        reviseUserView.showProgressDialog();

        Map<String, Object> parameter = new HashMap<>();

        parameter.put("user_name", telephone_number);
        parameter.put("password", password);
        parameter.put("sms", verification_code);
        parameter.put("n", (int) (Math.random() * 100));

        Gson gson = new Gson();
        String information = gson.toJson(parameter);

        try {
            information = RSAUtil.encryptionPublicKey(information);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, true);
        dataRequestService.reviseTelephoneNumber(information)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .doOnNext(new Consumer<CommunalResult<ReviseUser>>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull CommunalResult<ReviseUser> reviseUserResult) throws Exception {
                        if (reviseUserResult != null && reviseUserResult.getCode() == 0 && reviseUserResult.getModel() != null) {

                            Logger.d("ReviseTelephoneNumber call: 数据库操作");

                            User user = new User();
                            user.user_name = reviseUserResult.getModel().user_name;
                            user.token = reviseUserResult.getModel().token;

                            if (userDao == null) {
                                userDao = UserDao.getInstance(contextReference.get());
                            }

                            if (userDao.isContainsUser(UserDao.getToken(contextReference.get()))) {
                                userDao.updateUser(user, UserDao.getToken(contextReference.get()));
                            } else {
                                userDao.insertUser(user);
                            }

                            if (!TextUtils.isEmpty(reviseUserResult.getModel().token)) {
                                UserDao.setToken(reviseUserResult.getModel().token);
                            }

                            Logger.d("ReviseTelephoneNumber call: 数据库操作完成");
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResourceSubscriber<CommunalResult<ReviseUser>>() {
                    @Override
                    public void onNext(CommunalResult<ReviseUser> reviseUserResult) {
                        Logger.d("ReviseTelephoneNumber onNext");
                        reviseUserView.hideProgressDialog();
                        if (reviseUserResult != null) {
                            if (reviseUserResult.getCode() == 401) {
                                reviseUserView.showToast("登录令牌失效，请重新登录！");
                                reviseUserView.startLoginActivity();
                            } else {
                                if (reviseUserResult.getCode() == 0 && reviseUserResult.getModel() != null) {
                                    if (reviseUserView != null) {
                                        reviseUserView.showUserFragment();
                                    }
                                } else if (!TextUtils.isEmpty(reviseUserResult.getMessage())) {
                                    reviseUserView.showToast(reviseUserResult.getMessage());
                                }
                            }
                        } else {
                            reviseUserView.showToast("请检查网络");
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Logger.d("ReviseTelephoneNumber onError: " + throwable.toString());
                        reviseUserView.hideProgressDialog();
                        reviseUserView.showToast("请检查网络");
                    }

                    @Override
                    public void onComplete() {
                        Logger.d("ReviseTelephoneNumber onComplete");
                    }
                });
    }

    @Override
    public void reviseUserPassword(String ancient_password, String telephone_number, String verification_code, String fresh_password) {

        reviseUserView.showProgressDialog();

        Map<String, Object> parameter = new HashMap<>();

        parameter.put("password", ancient_password);
        parameter.put("user_name", telephone_number);
        parameter.put("new_password", fresh_password);
        parameter.put("sms", verification_code);
        parameter.put("n", (int) (Math.random() * 100));

        Gson gson = new Gson();
        String information = gson.toJson(parameter);

        try {
            information = RSAUtil.encryptionPublicKey(information);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, true);
        dataRequestService.reviserPassword(information)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .doOnNext(new Consumer<CommunalResult<Token>>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull CommunalResult<Token> tokenResult) throws Exception {
                        if (tokenResult != null && tokenResult.getCode() == 0 && tokenResult.getModel() != null) {
                            Logger.d("ReviserPassword call 数据库操作");

                            User user = new User();
                            user.token = tokenResult.getModel().token;

                            if (userDao == null) {
                                userDao = UserDao.getInstance(contextReference.get());
                            }

                            if (userDao.isContainsUser(UserDao.getToken(contextReference.get()))) {
                                userDao.updateUser(user, UserDao.getToken(contextReference.get()));
                            } else {
                                userDao.insertUser(user);
                            }

                            if (!TextUtils.isEmpty(user.token)) {
                                UserDao.setToken(user.token);
                            }

                            Logger.d("ReviserPassword call 数据库操作完成");
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResourceSubscriber<CommunalResult<Token>>() {

                    @Override
                    public void onNext(CommunalResult<Token> tokenResult) {
                        Logger.d("ReviserPassword onNext");
                        reviseUserView.hideProgressDialog();
                        if (tokenResult != null) {
                            if (tokenResult.getCode() == 401) {
                                reviseUserView.showToast("登录令牌已失效，请重新登录！");
                                reviseUserView.startLoginActivity();
                            } else {
                                if (tokenResult.getCode() == 0 && tokenResult.getModel() != null) {

                                    if (reviseUserView != null) {
                                        reviseUserView.refreshPasswordView();
                                        reviseUserView.showUserFragment();
                                    }
                                } else if (!TextUtils.isEmpty(tokenResult.getMessage())) {
                                    reviseUserView.showToast(tokenResult.getMessage());
                                }
                            }

                        } else {
                            reviseUserView.showToast("请检查网络");
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Logger.d("ReviserPassword onError: " + throwable.toString());
                        reviseUserView.hideProgressDialog();
                        reviseUserView.showToast("请检查网络");
                    }

                    @Override
                    public void onComplete() {
                        Logger.d("ReviserPassword onComplete");
                    }
                });
    }

    @Override
    public void reviseUserQQ(String qq) {

        reviseUserView.showProgressDialog();

        DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, true);
        dataRequestService.reviserQQ(qq)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .doOnNext(new Consumer<CommunalResult<QQ>>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull CommunalResult<QQ> qqResult) throws Exception {
                        if (qqResult != null && qqResult.getCode() == 0 && qqResult.getModel() != null) {
                            Logger.d("ReviserQQ call 数据库操作");

                            User user = new User();
                            user.qq = qqResult.getModel().qq;

                            if (userDao == null) {
                                userDao = UserDao.getInstance(contextReference.get());
                            }
                            if (userDao.isContainsUser(UserDao.getToken(contextReference.get()))) {
                                userDao.updateUser(user, UserDao.getToken(contextReference.get()));
                            } else {
                                userDao.insertUser(user);
                            }

                            Logger.d("ReviserQQ call 数据库操作完成");
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResourceSubscriber<CommunalResult<QQ>>() {
                    @Override
                    public void onNext(CommunalResult<QQ> qqResult) {
                        Logger.d("ReviserQQ onNext");
                        reviseUserView.hideProgressDialog();
                        if (qqResult != null) {
                            if (qqResult.getCode() == 401) {
                                reviseUserView.showToast("登录令牌失效，请重新登录");
                                reviseUserView.startLoginActivity();
                            } else {
                                if (qqResult.getCode() == 0 && qqResult.getModel() != null) {
                                    reviseUserView.showToast("QQ号修改成功！");
                                    reviseUserView.refreshQQView();
                                    reviseUserView.showUserFragment();
                                } else if (!TextUtils.isEmpty(qqResult.getMessage())) {
                                    if (reviseUserView != null) {
                                        reviseUserView.showToast(qqResult.getMessage());
                                    }
                                }
                            }
                        } else {
                            reviseUserView.showToast("请检查网络");
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Logger.d("ReviserQQ onError: " + throwable.toString());
                        reviseUserView.hideProgressDialog();
                        reviseUserView.showToast("请检查网络");
                    }

                    @Override
                    public void onComplete() {
                        Logger.d("ReviserQQ onComplete");
                    }
                });
    }

    @Override
    public void exitLogin() {
        reviseUserView.showProgressDialog();

        final DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, true);
        RequestBody requestBody;
        try {
            requestBody = BookUtil.getUpdateUserBookShelf(QuApplication.getInstance());
            dataRequestService.uploadUserBookShelf(requestBody)
                    .flatMap(new Function<CommunalResult<UploadShelfResult>, Publisher<CommunalResult<Alias>>>() {
                        @Override
                        public Publisher<CommunalResult<Alias>> apply(@io.reactivex.annotations.NonNull CommunalResult<UploadShelfResult> uploadShelfResult) throws Exception {
                            if (uploadShelfResult != null) {
                                Logger.d("ExitLanding UploadUserBookShelf call: " + uploadShelfResult.getCode());
                            }
                            return dataRequestService.exitLanding();
                        }
                    })
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(Schedulers.io())
                    .doOnNext(new Consumer<CommunalResult<Alias>>() {
                        @Override
                        public void accept(@io.reactivex.annotations.NonNull CommunalResult<Alias> aliasResult) throws Exception {
                            if (aliasResult != null && aliasResult.getCode() == 0) {
                                Logger.d("ExitLanding call: 删除用户信息");
                                UserDao.setToken("");
                                UserUtil.deleteUser(contextReference.get());
                                UserUtil.deleteLiterature(contextReference.get());
                                UserUtil.deleteCommentLike(contextReference.get());
                            }
                        }
                    })
                    .observeOn(Schedulers.io())
                    .doOnNext(new Consumer<CommunalResult<Alias>>() {
                        @Override
                        public void accept(@io.reactivex.annotations.NonNull CommunalResult<Alias> aliasResult) throws Exception {
                            if (aliasResult != null && aliasResult.getCode() == 0) {
                                Logger.d("ExitLanding call: 删除线上书籍信息");
                                BookUtil.deleteAllOnLineBook(QuApplication.getInstance());
                            }
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new ResourceSubscriber<CommunalResult<Alias>>() {
                        @Override
                        public void onNext(CommunalResult<Alias> aliasResult) {
                            Logger.d("ExitLanding onNext");
                            reviseUserView.hideProgressDialog();
                            if (aliasResult != null) {
                                if (aliasResult.getCode() == 0) {
                                    if (!TextUtils.isEmpty(aliasResult.getModel().alias)) {
                                        MiPushClient.unsetAlias(QuApplication.getInstance(), aliasResult.getModel().alias, null);
                                    }

                                    if (reviseUserView != null) {
                                        reviseUserView.finishActivity();
                                    }
                                } else if (!TextUtils.isEmpty(aliasResult.getMessage())) {
                                    reviseUserView.showToast(aliasResult.getMessage());
                                }
                            } else {
                                reviseUserView.showToast("请检查网络");
                            }
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            Logger.d("ExitLanding onError: " + throwable.toString());
                            reviseUserView.hideProgressDialog();
                            reviseUserView.showToast("请检查网络");
                        }

                        @Override
                        public void onComplete() {
                            Logger.d("ExitLanding onComplete");
                        }
                    });

        } catch (JSONException exception) {
            exception.printStackTrace();
        }
    }
}
