package com.quduquxie.base.module.main.activity.presenter;

import android.text.TextUtils;

import com.orhanobut.logger.Logger;
import com.quduquxie.base.RxPresenter;
import com.quduquxie.base.module.main.activity.MainInterface;
import com.quduquxie.base.module.main.activity.view.MainActivity;
import com.quduquxie.base.retrofit.DataRequestFactory;
import com.quduquxie.base.retrofit.DataRequestInterface;
import com.quduquxie.base.retrofit.bean.CommunalResult;
import com.quduquxie.base.rxjava.RxSchedulers;
import com.quduquxie.base.util.NetworkUtil;
import com.quduquxie.communal.utils.BookUtil;
import com.quduquxie.community.bean.UpdateShelf;
import com.quduquxie.db.LiteratureDao;
import com.quduquxie.model.creation.Literature;
import com.quduquxie.model.creation.LiteratureList;

import org.json.JSONException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;
import okhttp3.RequestBody;

/**
 * Created on 17/7/17.
 * Created by crazylei.
 */

public class MainPresenter extends RxPresenter implements MainInterface.Presenter {

    private MainActivity mainActivity;

    private LiteratureDao literatureDao;

    public MainPresenter(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        this.literatureDao = LiteratureDao.getInstance(mainActivity);
    }

    @Override
    public void initLiteratureParameter() {

        if (NetworkUtil.checkNetwork(mainActivity)) {

            mainActivity.showLoadingDialog("正在获取作品信息...");

            DataRequestInterface dataRequestInterface = DataRequestFactory.instantiation(DataRequestInterface.class, DataRequestFactory.TYPE_HTTPS, true);
            insertDisposable(dataRequestInterface.loadLiteratureList()
                    .subscribeOn(Schedulers.io())
                    .doOnNext(new Consumer<CommunalResult<LiteratureList>>() {
                        @Override
                        public void accept(@NonNull CommunalResult<LiteratureList> literatureListResult) throws Exception {
                            Logger.d("LoadLiteratureList call: 数据库存储");
                            if (literatureListResult.getCode() == 0 && literatureListResult.getModel() != null && literatureListResult.getModel().books != null && literatureListResult.getModel().books.size() > 0) {
                                if (literatureDao == null) {
                                    literatureDao = LiteratureDao.getInstance(mainActivity);
                                }

                                for (Literature literature : literatureListResult.getModel().books) {
                                    if (literature.chapter != null) {
                                        literature.serial_number = literature.chapter.serial_number;
                                    }

                                    if (literatureDao.isContainsLiterature(literature.id)) {
                                        literatureDao.updateLiterature(literature);
                                    } else {
                                        literatureDao.insertLiterature(literature);
                                    }
                                }
                            }
                            Logger.d("LoadLiteratureList call: 数据库存储结束");
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new ResourceSubscriber<CommunalResult<LiteratureList>>() {
                        @Override
                        public void onNext(CommunalResult<LiteratureList> literatureListResult) {
                            if (literatureListResult != null) {
                                if (literatureListResult.getCode() == 401) {
                                    mainActivity.showPromptMessage("登录令牌失效，请重新登录！");
                                    mainActivity.startLoginActivity();
                                } else {
                                    if (literatureListResult.getCode() == 0 && literatureListResult.getModel() != null) {
                                        if (literatureListResult.getModel().books != null) {
                                            if (literatureListResult.getModel().books.size() > 0) {
                                                mainActivity.startLiteratureActivity();
                                            } else {
                                                mainActivity.startLiteratureCreateActivity();
                                            }

                                        } else {
                                            mainActivity.startLiteratureCreateActivity();
                                        }
                                    } else if (!TextUtils.isEmpty(literatureListResult.getMessage())) {
                                        mainActivity.showPromptMessage(literatureListResult.getMessage());
                                        mainActivity.startLiteratureCreateActivity();
                                    }
                                }
                            } else {
                                mainActivity.showPromptMessage("获取作品列表失败！");
                                mainActivity.startLiteratureCreateActivity();
                            }
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            mainActivity.hideLoadingDialog();
                            mainActivity.collectException(throwable);
                            mainActivity.showPromptMessage("获取作品列表失败！");
                            mainActivity.startLiteratureCreateActivity();

                            Logger.d("LoadLiteratureList onError: " + throwable.toString());
                        }

                        @Override
                        public void onComplete() {
                            mainActivity.hideLoadingDialog();

                            Logger.d("LoadLiteratureList onComplete");
                        }
                    }));
        } else {
            mainActivity.showPromptMessage("网络连接异常，无法获取作品列表！");
            mainActivity.startLiteratureCreateActivity();
        }
    }

    @Override
    public void updateBookshelf() {

        if (NetworkUtil.checkNetwork(mainActivity)) {
            mainActivity.showLoadingDialog("正在上传书架信息...");
            try {
                RequestBody requestBody = BookUtil.getUpdateUserBookShelf(mainActivity);

                DataRequestInterface dataRequestInterface = DataRequestFactory.instantiation(DataRequestInterface.class, DataRequestFactory.TYPE_HTTPS, true);
                insertDisposable(dataRequestInterface.updateUserBookShelf(requestBody)
                        .compose(RxSchedulers.<CommunalResult<UpdateShelf>>schedulerHelper())
                        .subscribeWith(new ResourceSubscriber<CommunalResult<UpdateShelf>>() {
                            @Override
                            public void onNext(CommunalResult<UpdateShelf> updateShelfResult) {
                                mainActivity.showPromptMessage("书架上传成功！");
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                mainActivity.collectException(throwable);
                                mainActivity.hideLoadingDialog();
                                mainActivity.finishActivity();

                                throwable.printStackTrace();

                                Logger.d("UploadUserBookShelf onError: " + throwable.toString());
                            }

                            @Override
                            public void onComplete() {
                                Logger.d("UploadUserBookShelf onComplete");
                                mainActivity.hideLoadingDialog();
                                mainActivity.finishActivity();
                            }
                        }));
            } catch (JSONException exception) {
                mainActivity.collectException(exception);

                exception.printStackTrace();
            }
        } else {
            mainActivity.showPromptMessage("网络连接异常，无法上传书架信息！");
            mainActivity.hideLoadingDialog();
            mainActivity.finishActivity();
        }
    }

    @Override
    public void recycle() {
        if (literatureDao != null) {
            literatureDao = null;
        }

        if (mainActivity != null) {
            mainActivity = null;
        }
    }
}