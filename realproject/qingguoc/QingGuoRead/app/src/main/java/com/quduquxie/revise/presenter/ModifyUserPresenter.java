package com.quduquxie.revise.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;
import com.quduquxie.model.Avatar;
import com.quduquxie.model.PenName;
import com.quduquxie.model.User;
import com.quduquxie.db.UserDao;
import com.quduquxie.model.UserInformation;
import com.quduquxie.retrofit.DataRequestService;
import com.quduquxie.retrofit.ServiceGenerator;
import com.quduquxie.retrofit.model.CommunalResult;
import com.quduquxie.revise.ModifyUserInterface;

import java.io.File;
import java.lang.ref.WeakReference;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created on 16/12/5.
 * Created by crazylei.
 */

public class ModifyUserPresenter implements ModifyUserInterface.Presenter {

    private ModifyUserInterface.View modifyUserView;
    private WeakReference<Context> contextReference;
    private UserDao userDao;

    public ModifyUserPresenter(@NonNull ModifyUserInterface.View modifyUserView, Context context) {
        this.modifyUserView = modifyUserView;
        this.contextReference = new WeakReference<>(context);
        this.userDao = UserDao.getInstance(contextReference.get());
    }

    @Override
    public void init() {
        modifyUserView.showLoadingPage();

        if (userDao == null) {
            userDao = UserDao.getInstance(contextReference.get());
        }
        User user = userDao.getUser();
        if (user != null) {
            if (modifyUserView != null) {
                modifyUserView.showPenName(user.penname);
                modifyUserView.showAvatarImage(user.avatar_url);
                modifyUserView.showBindingNumber(user.user_name);
                modifyUserView.showQQNumber(user.qq);
            }
            modifyUserView.hideLoadingPage();
        } else {
            DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, true);
            dataRequestService.loadUserInformation()
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(Schedulers.io())
                    .doOnNext(new Consumer<CommunalResult<UserInformation>>() {
                        @Override
                        public void accept(@io.reactivex.annotations.NonNull CommunalResult<UserInformation> userInformationResult) throws Exception {
                            if (userInformationResult != null && userInformationResult.getCode() == 0 && userInformationResult.getModel() != null && userInformationResult.getModel().user != null) {
                                Logger.d("LoadUserInformation call 数据库操作");

                                User user = userInformationResult.getModel().user;
                                if (userDao == null) {
                                    userDao = UserDao.getInstance(contextReference.get());
                                }
                                if (userDao.isContainsUser(UserDao.getToken(contextReference.get()))) {
                                    userDao.updateUser(user, UserDao.getToken(contextReference.get()));
                                } else {
                                    userDao.insertUser(user);
                                }

                                Logger.d("LoadUserInformation call 数据库操作完成");
                            }
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new ResourceSubscriber<CommunalResult<UserInformation>>() {
                        @Override
                        public void onNext(CommunalResult<UserInformation> userInformationResult) {
                            Logger.d("LoadUserInformation onNext");
                            if (userInformationResult != null) {
                                if (userInformationResult.getCode() == 401) {
                                    modifyUserView.showToast("登录令牌过期，请重新登录！");
                                    modifyUserView.startLoginActivity();
                                } else {
                                    if (userInformationResult.getCode() == 0 && userInformationResult.getModel() != null && userInformationResult.getModel().user != null) {
                                        User user = userInformationResult.getModel().user;
                                        if (modifyUserView != null) {
                                            modifyUserView.showPenName(user.penname);
                                            modifyUserView.showAvatarImage(user.avatar_url);
                                            modifyUserView.showBindingNumber(user.user_name);
                                            modifyUserView.showQQNumber(user.qq);
                                        }
                                        modifyUserView.hideLoadingPage();
                                    } else if (!TextUtils.isEmpty(userInformationResult.getMessage())) {
                                        if (modifyUserView != null) {
                                            modifyUserView.showLoadingError();
                                            modifyUserView.showToast(userInformationResult.getMessage());
                                        }
                                    }
                                }
                            } else {
                                modifyUserView.showLoadingError();
                                modifyUserView.showToast("请检查网络");
                            }
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            Logger.d("LoadUserInformation onError: " + throwable.toString());
                            modifyUserView.showLoadingError();
                            modifyUserView.showToast("请检查网络");
                        }

                        @Override
                        public void onComplete() {
                            Logger.d("LoadUserInformation onComplete");
                        }
                    });
        }
    }

    @Override
    public void changePenName(String fresh_name, final boolean finish) {
        if (userDao == null) {
            userDao = UserDao.getInstance(contextReference.get());
        }

        User user = userDao.getUser();
        if (user.penname.equals(fresh_name)) {
            if (finish) {
                modifyUserView.destroyActivity();
            }
            return;
        }

        modifyUserView.showProgressDialog();

        DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, true);
        dataRequestService.reviserPenName(fresh_name)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .doOnNext(new Consumer<CommunalResult<PenName>>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull CommunalResult<PenName> penNameResult) throws Exception {
                        if (penNameResult != null && penNameResult.getCode() == 0 && penNameResult.getModel() != null) {
                            Logger.d("ReviserPenName call 数据库操作");

                            User user = new User();
                            user.penname = penNameResult.getModel().penname;
                            if (userDao == null) {
                                userDao = UserDao.getInstance(contextReference.get());
                            }
                            if (userDao.isContainsUser(UserDao.getToken(contextReference.get()))) {
                                userDao.updateUser(user, UserDao.getToken(contextReference.get()));
                            } else {
                                userDao.insertUser(user);
                            }

                            Logger.d("ReviserPenName call 数据库操作完成");
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResourceSubscriber<CommunalResult<PenName>>() {
                    @Override
                    public void onNext(CommunalResult<PenName> penNameResult) {
                        Logger.d("ReviserPenName onNext");
                        modifyUserView.hideProgressDialog();
                        if (penNameResult != null) {
                            if (penNameResult.getCode() == 401) {
                                modifyUserView.showToast("登录令牌失效，请重新登录");
                                modifyUserView.startLoginActivity();
                            } else {
                                if (penNameResult.getCode() == 0 && penNameResult.getModel() != null) {

                                    if (finish) {
                                        modifyUserView.destroyActivity();
                                    } else {
                                        if (modifyUserView != null) {
                                            modifyUserView.showPenName(penNameResult.getModel().penname);
                                        }
                                    }
                                    modifyUserView.showToast("昵称修改成功！");
                                } else if (!TextUtils.isEmpty(penNameResult.getMessage())) {
                                    if (modifyUserView != null) {
                                        modifyUserView.showToast(penNameResult.getMessage());
                                    }
                                }
                            }
                        } else {
                            modifyUserView.showToast("请检查网络");
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Logger.d("ReviserPenName onError: " + throwable.toString());
                        modifyUserView.hideProgressDialog();
                        modifyUserView.showToast("请检查网络");
                    }

                    @Override
                    public void onComplete() {
                        Logger.d("ReviserPenName onComplete");
                    }
                });
    }

    @Override
    public void changeAvatar(File file) {
        modifyUserView.showProgressDialog();
        if (userDao == null) {
            userDao = UserDao.getInstance(contextReference.get());
        }

        MultipartBody.Part requestFile = null;
        if (file != null) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            requestFile = MultipartBody.Part.createFormData("avatar", file.getName(), requestBody);
        }

        DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, true);
        dataRequestService.reviserAvatar(requestFile)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .doOnNext(new Consumer<CommunalResult<Avatar>>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull CommunalResult<Avatar> avatarResult) throws Exception {
                        if (avatarResult != null && avatarResult.getCode() == 0 && avatarResult.getModel() != null) {
                            Logger.d("ReviserAvatar call 数据库操作");

                            User user = new User();
                            user.avatar_url = avatarResult.getModel().avatar_url;
                            if (userDao == null) {
                                userDao = UserDao.getInstance(contextReference.get());
                            }
                            if (userDao.isContainsUser(UserDao.getToken(contextReference.get()))) {
                                userDao.updateUser(user, UserDao.getToken(contextReference.get()));
                            } else {
                                userDao.insertUser(user);
                            }

                            Logger.d("ReviserAvatar call 数据库操作完成");
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResourceSubscriber<CommunalResult<Avatar>>() {
                    @Override
                    public void onNext(CommunalResult<Avatar> avatarResult) {
                        Logger.d("ReviserAvatar onNext");
                        modifyUserView.hideProgressDialog();
                        if (avatarResult != null) {
                            if (avatarResult.getCode() == 401) {
                                modifyUserView.showToast("登录令牌失效，请重新登录！");
                                modifyUserView.startLoginActivity();
                            } else {
                                if (avatarResult.getCode() == 0 && avatarResult.getModel() != null) {
                                    if (modifyUserView != null) {
                                        modifyUserView.showAvatarImage(avatarResult.getModel().avatar_url);
                                    }
                                    modifyUserView.showToast("头像修改成功！");
                                } else if (!TextUtils.isEmpty(avatarResult.getMessage())) {
                                    if (modifyUserView != null) {
                                        modifyUserView.showToast(avatarResult.getMessage());
                                    }
                                }
                            }
                        } else {
                            modifyUserView.showToast("请检查网络");
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Logger.d("ReviserAvatar onError: " + throwable.toString());
                        modifyUserView.hideProgressDialog();
                        modifyUserView.showToast("请检查网络");
                    }

                    @Override
                    public void onComplete() {
                        Logger.d("ReviserAvatar onComplete");
                    }
                });
    }
}
