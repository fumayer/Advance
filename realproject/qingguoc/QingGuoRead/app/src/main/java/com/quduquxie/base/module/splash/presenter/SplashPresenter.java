package com.quduquxie.base.module.splash.presenter;

import android.text.TextUtils;

import com.baidu.mobstat.SendStrategyEnum;
import com.baidu.mobstat.StatService;
import com.orhanobut.logger.Logger;
import com.quduquxie.application.QuApplication;
import com.quduquxie.application.SharedPreferencesUtil;
import com.quduquxie.base.RxPresenter;
import com.quduquxie.base.bean.Book;
import com.quduquxie.base.bean.Splash;
import com.quduquxie.base.config.BaseConfig;
import com.quduquxie.base.database.helper.BookDaoHelper;
import com.quduquxie.base.module.splash.SplashInterface;
import com.quduquxie.base.module.splash.view.SplashActivity;
import com.quduquxie.base.retrofit.DataRequestFactory;
import com.quduquxie.base.retrofit.DataRequestInterface;
import com.quduquxie.base.retrofit.bean.CommunalResult;
import com.quduquxie.base.rxjava.RxSchedulers;
import com.quduquxie.base.util.CommunalUtil;
import com.quduquxie.base.util.NetworkUtil;
import com.quduquxie.base.util.StatServiceUtil;

import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.ArrayList;

import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created on 17/7/12.
 * Created by crazylei.
 */

public class SplashPresenter extends RxPresenter implements SplashInterface.Presenter {

    private SplashActivity splashActivity;

    private SharedPreferencesUtil sharedPreferencesUtil;

    private BookDaoHelper bookDaoHelper;

    public SplashPresenter(SplashActivity splashActivity) {
        this.splashActivity = splashActivity;
        this.sharedPreferencesUtil = splashActivity.loadSharedPreferencesUtil();
    }

    @Override
    public void loadSplashRecommend() {
        DataRequestInterface dataRequestInterface = DataRequestFactory.instantiation(DataRequestInterface.class, DataRequestFactory.TYPE_HTTP, false);
        insertDisposable(dataRequestInterface.loadSplashRecommend()
                .compose(RxSchedulers.<CommunalResult<Splash>>schedulerHelper())
                .subscribeWith(new ResourceSubscriber<CommunalResult<Splash>>() {

                    @Override
                    public void onNext(CommunalResult<Splash> splashResult) {
                        if (splashResult != null) {
                            if (splashResult.getCode() == 0 && splashResult.getModel() != null) {
                                splashActivity.showRecommend(splashResult.getModel());
                            } else if (!TextUtils.isEmpty(splashResult.getMessage())) {
                                splashActivity.showPromptMessage(splashResult.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        splashActivity.collectException(throwable);

                        throwable.printStackTrace();

                        Logger.d("LoadSplashRecommend onError: " + throwable.toString());
                    }

                    @Override
                    public void onComplete() {
                        Logger.d("LoadSplashRecommend onComplete");
                    }
                })
        );
    }

    @Override
    public void checkDefaultBook() {
        if (bookDaoHelper == null) {
            bookDaoHelper = BookDaoHelper.getInstance(splashActivity);
        }

        if (bookDaoHelper.loadBookSize() == 0) {
            if (!NetworkUtil.checkNetwork(splashActivity)) {
                splashActivity.showPromptMessage("网络连接异常，请检查网络！");
            } else {
                DataRequestInterface dataRequestInterface = DataRequestFactory.instantiation(DataRequestInterface.class, DataRequestFactory.TYPE_HTTP, false);
                dataRequestInterface.loadDefaultBooks()
                        .compose(RxSchedulers.<CommunalResult<ArrayList<Book>>>schedulerHelper())
                        .subscribeWith(new ResourceSubscriber<CommunalResult<ArrayList<Book>>>() {
                            @Override
                            public void onNext(CommunalResult<ArrayList<Book>> booksResult) {

                                if (booksResult != null) {
                                    if (booksResult.getCode() == 0 && booksResult.getModel() != null && booksResult.getModel().size() > 0) {
                                        insertDefaultBooks(booksResult.getModel());
                                    } else if (!TextUtils.isEmpty(booksResult.getMessage())) {
                                        splashActivity.showPromptMessage(booksResult.getMessage());
                                    }
                                }
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                splashActivity.showPromptMessage("书架初始化失败！");

                                splashActivity.collectException(throwable);

                                throwable.printStackTrace();

                                Logger.d("LoadDefaultBooks onError: " + throwable.toString());
                            }

                            @Override
                            public void onComplete() {
                                Logger.d("LoadDefaultBooks onComplete");
                            }
                        });
            }
        }
    }

    @Override
    public void initializePresenter() {
        if (sharedPreferencesUtil == null) {
            sharedPreferencesUtil = splashActivity.loadSharedPreferencesUtil();
        }

        boolean startPush = sharedPreferencesUtil.loadBoolean(BaseConfig.FLAG_PUSH_NOTIFICATION, true);

        if (startPush) {
            MiPushClient.resumePush(splashActivity, null);
        } else {
            MiPushClient.pausePush(splashActivity, null);
        }

        long lastTime = sharedPreferencesUtil.loadLong(BaseConfig.FLAG_START_APPLICATION_TIME, 0);

        if (lastTime != 0) {
            StatServiceUtil.statStartInterval(splashActivity, CommunalUtil.compareTime(BaseConfig.simpleDateFormat, lastTime));
        }

        try {
            StatService.setLogSenderDelayed(20);
            StatService.setSendLogStrategy(splashActivity, SendStrategyEnum.APP_START, 1, false);

            if (BaseConfig.DEVELOP_MODE) {
                StatService.setDebugOn(BaseConfig.DEVELOP_MODE);
            }

        } catch (Exception exception) {
            splashActivity.collectException(exception);
        }
    }

    @Override
    public void checkStartActivity() {

        if (sharedPreferencesUtil == null) {
            sharedPreferencesUtil = splashActivity.loadSharedPreferencesUtil();
        }

        BaseConfig.book_list_sort = sharedPreferencesUtil.loadInteger(BaseConfig.FLAG_BOOK_LIST_SORT, 0);

        splashActivity.startMainActivity();
    }

    @Override
    public void recycle() {
        if (splashActivity != null) {
            splashActivity = null;
        }
    }

    private void insertDefaultBooks(ArrayList<Book> books) {
        if (books == null || books.isEmpty()) {
            return;
        }

        if (bookDaoHelper == null) {
            bookDaoHelper = BookDaoHelper.getInstance(splashActivity);
        }

        for (Book book : books) {
            if (book != null && !TextUtils.isEmpty(book.id) && !bookDaoHelper.subscribeBook(book.id)) {
                int result = bookDaoHelper.insertBook(book);
                Logger.e("InsertDefaultBooks: " + result);
            }
        }
    }
}