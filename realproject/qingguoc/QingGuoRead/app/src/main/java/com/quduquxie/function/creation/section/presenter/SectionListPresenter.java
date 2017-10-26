package com.quduquxie.function.creation.section.presenter;

import android.text.TextUtils;

import com.orhanobut.logger.Logger;
import com.quduquxie.base.util.NetworkUtil;
import com.quduquxie.db.SectionDao;
import com.quduquxie.function.creation.section.SectionListInterface;
import com.quduquxie.function.creation.section.view.SectionListActivity;
import com.quduquxie.model.creation.Literature;
import com.quduquxie.model.creation.Section;
import com.quduquxie.model.creation.SectionList;
import com.quduquxie.retrofit.DataRequestService;
import com.quduquxie.retrofit.ServiceGenerator;
import com.quduquxie.retrofit.model.CommunalResult;
import com.quduquxie.widget.LoadingPage;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created on 17/4/10.
 * Created by crazylei.
 */

public class SectionListPresenter implements SectionListInterface.Presenter {

    private SectionListActivity sectionListActivity;

    private SectionDao sectionDao;

    private LoadingPage loadingPage;

    public SectionListPresenter(SectionListActivity sectionListActivity) {
        this.sectionListActivity = sectionListActivity;
    }

    @Override
    public void initParameter() {
        sectionListActivity.initView();
    }

    @Override
    public void loadSectionList(final Literature literature) {
        if (NetworkUtil.loadNetworkType(sectionListActivity.getQuApplicationContext()) != NetworkUtil.NETWORK_NONE) {
            sectionListActivity.showLoadingPage();

            DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, true);
            dataRequestService.loadSectionList(literature.id, 1, Integer.MAX_VALUE - 1)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(Schedulers.io())
                    .doOnNext(new Consumer<CommunalResult<SectionList>>() {
                        @Override
                        public void accept(@NonNull CommunalResult<SectionList> sectionListResult) throws Exception {
                            Logger.d("LoadLiteratureList call: 数据库存储");
                            if (sectionListResult.getCode() == 0 && sectionListResult.getModel() != null && sectionListResult.getModel().chapters != null && sectionListResult.getModel().chapters.size() > 0) {
                                if (sectionDao == null) {
                                    sectionDao = new SectionDao(sectionListActivity.getQuApplicationContext(), literature.id);
                                }
                                sectionDao.deleteAllSection();
                                sectionDao.insertSections(sectionListResult.getModel().chapters);
                                Logger.d("LoadLiteratureList call: 数据库存储结束");
                            }
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new ResourceSubscriber<CommunalResult<SectionList>>() {
                        @Override
                        public void onNext(CommunalResult<SectionList> sectionListResult) {
                            Logger.d("LoadLiteratureList onNext");
                            sectionListActivity.hideLoadingPage();
                            if (sectionListResult != null) {
                                if (sectionListResult.getCode() == 401) {
                                    sectionListActivity.showToast("登录令牌失效，请重新登录！");
                                    sectionListActivity.startLoginActivity();
                                } else {
                                    if (sectionListResult.getCode() == 0 && sectionListResult.getModel() != null && sectionListResult.getModel().chapters != null) {
                                        sectionListActivity.setSectionData(sectionListResult.getModel().chapters);

                                    } else if (!TextUtils.isEmpty(sectionListResult.getMessage())) {
                                        sectionListActivity.showToast(sectionListResult.getMessage());
                                    }
                                }
                            } else {
                                sectionListActivity.showToast("作品章节同步失败！");
                                ArrayList<Section> sectionList = loadSectionFromDB(literature.id);
                                sectionListActivity.setSectionData(sectionList);
                            }
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            Logger.d("LoadLiteratureList onError: " + throwable.toString());

                            sectionListActivity.hideLoadingPage();
                            sectionListActivity.showToast("作品章节同步失败，请检查网络连接！");

                            ArrayList<Section> sectionList = loadSectionFromDB(literature.id);
                            sectionListActivity.setSectionData(sectionList);
                        }

                        @Override
                        public void onComplete() {
                            Logger.d("LoadLiteratureList onComplete");
                        }
                    });

        } else {
            sectionListActivity.showToast("未连接网络，作品章节同步失败！");
            ArrayList<Section> sectionList = loadSectionFromDB(literature.id);
            sectionListActivity.setSectionData(sectionList);
        }
    }

    @Override
    public void setLoadingPage(LoadingPage loadingPage) {
        this.loadingPage = loadingPage;
    }

    private ArrayList<Section> loadSectionFromDB(String literatureID) {
        if (sectionDao == null) {
            sectionDao = new SectionDao(sectionListActivity.getQuApplicationContext(), literatureID);
        }
        return sectionDao.getSectionList();
    }
}
