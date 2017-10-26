package com.quduquxie.base.module.main.fragment.mine.presenter;

import android.text.TextUtils;

import com.orhanobut.logger.Logger;
import com.quduquxie.base.RxPresenter;
import com.quduquxie.base.module.main.fragment.mine.MineInterface;
import com.quduquxie.base.module.main.fragment.mine.view.MineFragment;
import com.quduquxie.db.CommentLikeDao;
import com.quduquxie.db.UserDao;
import com.quduquxie.model.User;
import com.quduquxie.model.UserInformation;
import com.quduquxie.retrofit.DataRequestService;
import com.quduquxie.retrofit.ServiceGenerator;
import com.quduquxie.retrofit.model.CommunalResult;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created on 17/7/17.
 * Created by crazylei.
 */

public class MinePresenter extends RxPresenter implements MineInterface.Presenter {

    private MineFragment mineFragment;

    private UserDao userDao;
    private CommentLikeDao commentLikeDao;

    public MinePresenter(MineFragment mineFragment) {
        this.mineFragment = mineFragment;

        if (userDao == null) {
            userDao = UserDao.getInstance(mineFragment.getContext());
        }

        if (commentLikeDao == null) {
            commentLikeDao = CommentLikeDao.getInstance(mineFragment.getContext());
        }
    }

    @Override
    public void refreshUserInformation() {
        if (UserDao.checkUserLogin()) {
            DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, true);
            dataRequestService.loadUserInformation()
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(Schedulers.io())
                    .doOnNext(new Consumer<CommunalResult<UserInformation>>() {
                        @Override
                        public void accept(@NonNull CommunalResult<UserInformation> userInformationResult) throws Exception {
                            if (userInformationResult != null && userInformationResult.getCode() == 0 && userInformationResult.getModel() != null) {
                                Logger.d("LoadUserInformation call 数据库操作");

                                UserInformation userInformation = userInformationResult.getModel();
                                if (userInformation.user != null) {
                                    User user = userInformation.user;

                                    if (userDao == null) {
                                        userDao = UserDao.getInstance(mineFragment.getContext());
                                    }
                                    if (userDao.isContainsUser(UserDao.getToken(mineFragment.getContext()))) {
                                        userDao.updateUser(user, UserDao.getToken(mineFragment.getContext()));
                                    } else {
                                        userDao.insertUser(user);
                                    }
                                }

                                if (userInformation.likes != null && userInformation.likes.size() > 0) {

                                    CommentLikeDao commentLikeDao = CommentLikeDao.getInstance(mineFragment.getContext());
                                    commentLikeDao.deleteAllComment();
                                    for (String id : userInformation.likes) {
                                        commentLikeDao.insertComment(id);
                                    }
                                }
                                Logger.d("LoadUserInformation call 数据库操作完成");
                            }
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new ResourceSubscriber<CommunalResult<UserInformation>>() {
                        @Override
                        public void onNext(CommunalResult<UserInformation> userInformationResult) {
                            if (userInformationResult != null) {
                                if (userInformationResult.getCode() == 401) {
                                    mineFragment.showToastMessage("登录令牌失效，请重新登录！");
                                    mineFragment.startLoginActivity();
                                } else {
                                    if (userInformationResult.getCode() == 0 && userInformationResult.getModel() != null && userInformationResult.getModel().user != null) {
                                        Logger.d("LoadUserInformation onNext: " + userInformationResult.getCode() + " : " + userInformationResult.getModel().user.toString());
                                        if (mineFragment != null) {
                                            mineFragment.initUserInformation(userInformationResult.getModel().user);
                                            mineFragment.initCommentCountView(userInformationResult.getModel().messages.comments_count, userInformationResult.getModel().messages.reply_count);
                                        }
                                    } else if (!TextUtils.isEmpty(userInformationResult.getMessage())) {
                                        if (mineFragment != null) {
                                            mineFragment.showToastMessage(userInformationResult.getMessage());
                                            mineFragment.initUserInformation(null);
                                            mineFragment.initCommentCountView(0, 0);
                                        }
                                    }
                                }
                            } else {
                                mineFragment.showToastMessage("获取用户信息失败！");
                                mineFragment.initUserInformation(null);
                                mineFragment.initCommentCountView(0, 0);
                            }
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            Logger.d("LoadUserInformation onError: " + throwable.toString());

                            mineFragment.showToastMessage("获取用户信息失败，请检查网络连接！");
                            mineFragment.initUserInformation(null);
                        }

                        @Override
                        public void onComplete() {
                            Logger.d("LoadUserInformation onComplete");
                        }
                    });
        } else {
            mineFragment.initUserInformation(null);
            mineFragment.initCommentCountView(0, 0);
        }
    }

    @Override
    public void recycle() {

    }
}