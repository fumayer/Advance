package com.quduquxie.function.creation.literature.presenter;

import android.text.TextUtils;

import com.orhanobut.logger.Logger;
import com.quduquxie.base.util.NetworkUtil;
import com.quduquxie.db.LiteratureDao;
import com.quduquxie.function.creation.literature.LiteratureListInterface;
import com.quduquxie.function.creation.literature.adapter.LiteratureListAdapter;
import com.quduquxie.function.creation.literature.view.LiteratureListActivity;
import com.quduquxie.model.creation.Literature;
import com.quduquxie.model.creation.LiteratureDelete;
import com.quduquxie.model.creation.LiteratureList;
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

public class LiteratureListPresenter implements LiteratureListInterface.Presenter {

    private LiteratureListActivity literatureListActivity;

    private LiteratureDao literatureDao;

    private LoadingPage loadingPage;

    public LiteratureListPresenter(LiteratureListActivity literatureListActivity) {
        this.literatureListActivity = literatureListActivity;
        this.literatureDao = LiteratureDao.getInstance(literatureListActivity.getQuApplicationContext());
    }

    @Override
    public void initParameter() {
        literatureListActivity.initView();
    }

    @Override
    public void loadLiteratureList() {
        if (NetworkUtil.loadNetworkType(literatureListActivity.getQuApplicationContext()) != NetworkUtil.NETWORK_NONE) {
            literatureListActivity.showLoadingPage();
            DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, true);
            dataRequestService.loadLiteratureList()
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(Schedulers.io())
                    .doOnNext(new Consumer<CommunalResult<LiteratureList>>() {
                        @Override
                        public void accept(@NonNull CommunalResult<LiteratureList> literatureListResult) throws Exception {
                            Logger.d("LoadLiteratureList call: 数据库存储");
                            if (literatureListResult.getCode() == 0 && literatureListResult.getModel() != null && literatureListResult.getModel().books != null && literatureListResult.getModel().books.size() > 0) {
                                if (literatureDao == null) {
                                    literatureDao = LiteratureDao.getInstance(literatureListActivity.getQuApplicationContext());
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
                    .subscribe(new ResourceSubscriber<CommunalResult<LiteratureList>>() {
                        @Override
                        public void onNext(CommunalResult<LiteratureList> literatureListResult) {
                            Logger.d("LoadLiteratureList onNext");
                            literatureListActivity.hideLoadingPage();
                            if (literatureListResult != null) {
                                if (literatureListResult.getCode() == 401) {
                                    literatureListActivity.showToast("登录令牌失效，请重新登录");
                                    literatureListActivity.startLoginActivity();
                                } else {
                                    if (literatureListResult.getCode() == 0 && literatureListResult.getModel() != null) {

                                        LiteratureList literatureResult = literatureListResult.getModel();

                                        if (literatureResult.books != null && literatureResult.books.size() > 0) {
                                            ArrayList<Literature> literatureList = new ArrayList<>();
                                            for (int i = 0; i < literatureResult.books.size(); i++) {
                                                literatureList.add(literatureResult.books.get(i));
                                                if (i != literatureResult.books.size() - 1) {
                                                    literatureList.add(createLiteratureFull());
                                                }
                                            }

                                            literatureList.add(createLiteraturePrompt());
                                            literatureList.add(createLiteratureComplete());

                                            literatureListActivity.setLiteratureData(literatureList);
                                        } else {
                                            ArrayList<Literature> literatureList = loadLiteratureFromDB();

                                            literatureListActivity.setLiteratureData(literatureList);
                                        }
                                    } else if (!TextUtils.isEmpty(literatureListResult.getMessage())) {
                                        literatureListActivity.showToast(literatureListResult.getMessage());

                                        ArrayList<Literature> literatureList = loadLiteratureFromDB();
                                        literatureListActivity.setLiteratureData(literatureList);
                                    }
                                }
                            } else {
                                literatureListActivity.hideLoadingPage();
                                literatureListActivity.showToast("同步作品列表失败！");
                                ArrayList<Literature> literatureList = loadLiteratureFromDB();
                                literatureListActivity.setLiteratureData(literatureList);
                            }
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            Logger.d("LoadLiteratureList onError: " + throwable.toString());

                            literatureListActivity.hideLoadingPage();
                            literatureListActivity.showToast("同步作品列表失败，请检查网络连接！");

                            ArrayList<Literature> literatureList = loadLiteratureFromDB();

                            literatureListActivity.setLiteratureData(literatureList);
                        }

                        @Override
                        public void onComplete() {
                            Logger.d("LoadLiteratureList onComplete");
                        }
                    });

        } else {
            literatureListActivity.showToast("网络连接异常，作品列表同步失败！");
            ArrayList<Literature> literatureList = loadLiteratureFromDB();
            literatureListActivity.setLiteratureData(literatureList);
        }
    }

    @Override
    public void deleteLiterature(final int position, final Literature literature) {

        if (NetworkUtil.loadNetworkType(literatureListActivity.getQuApplicationContext()) != NetworkUtil.NETWORK_NONE) {

            DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, true);
            dataRequestService.deleteLiterature(literature.id)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(Schedulers.io())
                    .doOnNext(new Consumer<CommunalResult<LiteratureDelete>>() {
                        @Override
                        public void accept(@NonNull CommunalResult<LiteratureDelete> literatureDeleteResult) throws Exception {
                            Logger.d("DeleteLiterature call: 数据库存储");
                            if (literatureDeleteResult.getCode() == 0 && literatureDeleteResult.getModel() != null) {
                                if (literatureDao == null) {
                                    literatureDao = LiteratureDao.getInstance(literatureListActivity.getQuApplicationContext());
                                }
                                literatureDao.deleteLiterature(literature.id);
                            }
                            Logger.d("DeleteLiterature call: 数据库存储结束");
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new ResourceSubscriber<CommunalResult<LiteratureDelete>>() {
                        @Override
                        public void onNext(CommunalResult<LiteratureDelete> literatureDeleteResult) {
                            Logger.d("DeleteLiterature onNext");
                            if (literatureDeleteResult != null) {
                                if (literatureDeleteResult.getCode() == 401) {
                                    literatureListActivity.showToast("登录令牌失效，请重新登录");
                                    literatureListActivity.startLoginActivity();
                                } else {
                                    if (literatureDeleteResult.getCode() == 0 && literatureDeleteResult.getModel() != null) {
                                        literatureListActivity.deleteLiterature(position);

                                    } else if (!TextUtils.isEmpty(literatureDeleteResult.getMessage())) {
                                        literatureListActivity.showToast(literatureDeleteResult.getMessage());
                                    }
                                }
                            } else {
                                literatureListActivity.showToast("删除作品失败！");
                            }
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            Logger.d("DeleteLiterature onError: " + throwable.toString());
                            literatureListActivity.showToast("删除作品失败，请检查网络连接！");
                        }

                        @Override
                        public void onComplete() {
                            Logger.d("DeleteLiterature onComplete");
                        }
                    });
        } else {
            literatureListActivity.showToast("网络连接异常，删除作品失败！");
        }
    }

    @Override
    public void setLoadingPage(LoadingPage loadingPage) {
        this.loadingPage = loadingPage;
    }

    private ArrayList<Literature> loadLiteratureFromDB() {
        if (literatureDao == null) {
            literatureDao = LiteratureDao.getInstance(literatureListActivity.getQuApplicationContext());
        }
        ArrayList<Literature> literatureList = literatureDao.getLiteratureList();

        ArrayList<Literature> literatureResult = new ArrayList<>();

        if (literatureList != null && literatureList.size() > 0) {
            for (int i = 0; i < literatureList.size(); i++) {
                literatureResult.add(literatureList.get(i));
                if (i != literatureList.size() - 1) {
                    literatureResult.add(createLiteratureFull());
                }
            }

            literatureResult.add(createLiteraturePrompt());
            literatureResult.add(createLiteratureComplete());
        }

        return literatureResult;
    }

    private Literature createLiteraturePrompt() {
        Literature literature = new Literature();
        literature.item_type = LiteratureListAdapter.TYPE_PROMPT;
        return literature;
    }

    private Literature createLiteratureComplete() {
        Literature literature = new Literature();
        literature.item_type = LiteratureListAdapter.TYPE_COMPLETE_INFORMATION;
        return literature;
    }

    private Literature createLiteratureFull() {
        Literature literature = new Literature();
        literature.item_type = LiteratureListAdapter.TYPE_EMPTY_FILL_EIGHT;
        return literature;
    }
}