package com.quduquxie.function.creation.draft.presenter;

import android.text.TextUtils;

import com.orhanobut.logger.Logger;
import com.quduquxie.base.util.NetworkUtil;
import com.quduquxie.db.DraftDao;
import com.quduquxie.function.creation.draft.DraftListInterface;
import com.quduquxie.function.creation.draft.view.DraftListActivity;
import com.quduquxie.model.creation.Draft;
import com.quduquxie.model.creation.DraftCreate;
import com.quduquxie.model.creation.DraftDelete;
import com.quduquxie.model.creation.DraftList;
import com.quduquxie.model.creation.Literature;
import com.quduquxie.retrofit.DataRequestService;
import com.quduquxie.retrofit.ServiceGenerator;
import com.quduquxie.retrofit.model.CommunalResult;
import com.quduquxie.widget.LoadingPage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created on 17/4/10.
 * Created by crazylei.
 */

public class DraftListPresenter implements DraftListInterface.Presenter {

    private DraftListActivity draftListActivity;

    private LoadingPage loadingPage;

    private DraftDao draftDao;

    public DraftListPresenter(DraftListActivity draftListActivity) {
        this.draftListActivity = draftListActivity;
        this.draftDao = DraftDao.getInstance(draftListActivity.getQuApplicationContext());
    }

    @Override
    public void initParameter() {
        draftListActivity.initView();
    }

    @Override
    public void loadDraftList(final Literature literature) {
        if (NetworkUtil.loadNetworkType(draftListActivity.getQuApplicationContext()) != NetworkUtil.NETWORK_NONE) {
            draftListActivity.showLoadingPage();

            DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, true);
            dataRequestService.loadDraftList(literature.id, 1, Integer.MAX_VALUE - 1)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(Schedulers.io())
                    .doOnNext(new Consumer<CommunalResult<DraftList>>() {
                        @Override
                        public void accept(@NonNull CommunalResult<DraftList> draftListResult) throws Exception {
                            Logger.d("LoadDraftList call: 数据库存储");
                            if (draftListResult.getCode() == 0 && draftListResult.getModel() != null) {
                                if (draftDao == null) {
                                    draftDao = DraftDao.getInstance(draftListActivity.getQuApplicationContext());
                                }

                                ArrayList<Draft> draftList = draftListResult.getModel().drafts;
                                ArrayList<Draft> draftLocalList = draftDao.getDraftsByID(literature.id);

                                if (draftList != null && draftList.size() > 0) {
                                    for (Draft localDraft : draftLocalList) {
                                        if (!TextUtils.isEmpty(localDraft.local)) {
                                            createDraft(localDraft);
                                        } else {
                                            int index = draftList.indexOf(localDraft);
                                            if (index != -1) {
                                                if (localDraft.need_synchronize == 2) {
                                                    reviseDraft(localDraft);
                                                }
                                            } else {
                                                draftDao.deleteDraft(localDraft.id);
                                            }
                                        }
                                    }

                                    for (Draft draft : draftList) {
                                        if (draftDao.isContainsDraft(draft.id)) {
                                            Draft localDraft = draftDao.getDraft(draft.id);
                                            if (localDraft.update_time == draft.update_time && !TextUtils.isEmpty(localDraft.content)) {
                                                draft.need_synchronize = 0;
                                            } else {
                                                draft.need_synchronize = 1;
                                            }
                                            draftDao.updateDraft(draft);
                                        } else {
                                            draftDao.insertDraft(draft);
                                        }
                                    }
                                } else {
                                    for (Draft localDraft : draftLocalList) {
                                        if (!TextUtils.isEmpty(localDraft.local)) {
                                            createDraft(localDraft);
                                        }
                                    }
                                }
                            }
                            Logger.d("LoadDraftList call: 数据库存储结束");
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new ResourceSubscriber<CommunalResult<DraftList>>() {
                        @Override
                        public void onNext(CommunalResult<DraftList> draftListResult) {
                            Logger.d("LoadDraftList onNext");
                            if (draftListResult != null) {
                                if (draftListResult.getCode() == 401) {
                                    draftListActivity.showToast("登录令牌失效，请重新登录！");
                                    draftListActivity.startLoginActivity();
                                } else {
                                    if (draftListResult.getCode() == 0 && draftListResult.getModel() != null) {
                                        ArrayList<Draft> drafts = loadDraftFromDB(literature.id);
                                        draftListActivity.setLiteratureDraftData(drafts);

                                        draftListActivity.hideLoadingPage();

                                    } else if (!TextUtils.isEmpty(draftListResult.getMessage())) {
                                        ArrayList<Draft> drafts = loadDraftFromDB(literature.id);
                                        draftListActivity.setLiteratureDraftData(drafts);

                                        draftListActivity.hideLoadingPage();
                                        draftListActivity.showToast(draftListResult.getMessage());
                                    }
                                }
                            } else {
                                ArrayList<Draft> drafts = loadDraftFromDB(literature.id);
                                draftListActivity.setLiteratureDraftData(drafts);

                                draftListActivity.hideLoadingPage();
                                draftListActivity.showToast("网络连接异常，草稿列表同步失败！");
                            }
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            Logger.d("LoadDraftList onError: " + throwable.toString());

                            ArrayList<Draft> drafts = loadDraftFromDB(literature.id);
                            draftListActivity.setLiteratureDraftData(drafts);

                            draftListActivity.hideLoadingPage();
                            draftListActivity.showToast("同步草稿列表失败，请检查网路连接！");

                        }

                        @Override
                        public void onComplete() {
                            Logger.d("LoadDraftList onComplete");
                        }
                    });
        } else {
            ArrayList<Draft> drafts = loadDraftFromDB(literature.id);
            draftListActivity.setLiteratureDraftData(drafts);

            draftListActivity.showToast("网络连接异常，草稿列表同步失败！");
        }
    }

    @Override
    public void deleteDraft(final Draft draft, int position) {
        if (NetworkUtil.loadNetworkType(draftListActivity.getQuApplicationContext()) != NetworkUtil.NETWORK_NONE) {
            DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, true);
            dataRequestService.deleteDraft(draft.id)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(Schedulers.io())
                    .doOnNext(new Consumer<CommunalResult<DraftDelete>>() {
                        @Override
                        public void accept(@NonNull CommunalResult<DraftDelete> draftDeleteResult) throws Exception {
                            Logger.d("DeleteDraft call: 数据库存储");
                            if (draftDeleteResult.getCode() == 0 && draftDeleteResult.getModel() != null) {
                                if (draftDao == null) {
                                    draftDao = DraftDao.getInstance(draftListActivity.getQuApplicationContext());
                                }
                                draftDao.deleteDraft(draft.id);
                            }
                            Logger.d("DeleteDraft call: 数据库存储结束");
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new ResourceSubscriber<CommunalResult<DraftDelete>>() {
                        @Override
                        public void onNext(CommunalResult<DraftDelete> draftDeleteResult) {
                            Logger.d("DeleteDraft onNext");
                            if (draftDeleteResult != null) {
                                if (draftDeleteResult.getCode() == 401) {
                                    draftListActivity.showToast("登录令牌失效，请重新登录！");
                                    draftListActivity.startLoginActivity();
                                } else {
                                    if (draftDeleteResult.getCode() == 0 && draftDeleteResult.getModel() != null) {
                                        draftListActivity.deleteDraft(draft);
                                    } else if (!TextUtils.isEmpty(draftDeleteResult.getMessage())) {
                                        draftListActivity.showToast(draftDeleteResult.getMessage());
                                    }
                                }
                            } else {
                                draftListActivity.showToast("网络连接异常，删除草稿失败！");
                            }
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            Logger.d("DeleteDraft onError: " + throwable.toString());

                            draftListActivity.showToast("草稿删除失败，请检查网络连接！");
                        }

                        @Override
                        public void onComplete() {
                            Logger.d("DeleteDraft onComplete");
                        }
                    });
        } else {
            draftListActivity.showToast("网络连接异常，删除草稿失败！");
        }
    }

    @Override
    public void setLoadingPage(LoadingPage loadingPage) {
        this.loadingPage = loadingPage;
    }

    private ArrayList<Draft> loadDraftFromDB(String literatureID) {
        if (draftDao == null) {
            draftDao = DraftDao.getInstance(draftListActivity.getQuApplicationContext());
        }
        return draftDao.getDraftsByID(literatureID);
    }

    private void createDraft(final Draft draft) {
        if (draft != null) {
            Map<String, String> parameter = new HashMap<>();

            parameter.put("name", draft.name);
            parameter.put("book_id", draft.book_id);

            if (!TextUtils.isEmpty(draft.content)) {
                parameter.put("content", draft.content);
            }

            DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, true);
            dataRequestService.createDraft(parameter)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(Schedulers.io())
                    .doOnNext(new Consumer<CommunalResult<DraftCreate>>() {
                        @Override
                        public void accept(@NonNull CommunalResult<DraftCreate> draftCreateResult) throws Exception {
                            Logger.d("CreateDraft call: 数据库存储");
                            if (draftCreateResult.getCode() == 0 && draftCreateResult.getModel() != null && draftCreateResult.getModel().draft != null) {

                                Draft createDraft = draftCreateResult.getModel().draft;

                                if (draftDao == null) {
                                    draftDao = DraftDao.getInstance(draftListActivity.getQuApplicationContext());
                                }

                                draftDao.deleteLocalDraft(draft.local);

                                createDraft.need_synchronize = 0;
                                draftDao.insertDraft(createDraft);
                            }
                            Logger.d("CreateDraft call: 数据库存储结束");
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new ResourceSubscriber<CommunalResult<DraftCreate>>() {
                        @Override
                        public void onNext(CommunalResult<DraftCreate> draftCreateResult) {
                            Logger.d("CreateDraft onNext");

                            if (draftCreateResult != null) {
                                if (draftCreateResult.getCode() == 401) {
                                    draftListActivity.showToast("登录令牌失效，请重新登录！");
                                    draftListActivity.startLoginActivity();
                                } else {
                                    if (draftCreateResult.getCode() == 0 && draftCreateResult.getModel() != null && draftCreateResult.getModel().draft != null) {
                                        draftListActivity.showToast("创建草稿成功！");
                                    } else if (!TextUtils.isEmpty(draftCreateResult.getMessage())) {
                                        draftListActivity.showToast(draftCreateResult.getMessage());
                                    }
                                }
                            } else {
                                draftListActivity.showToast("网络连接异常，创建草稿失败！");
                            }
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            Logger.d("CreateDraft onError: " + throwable.toString());
                            draftListActivity.showToast("创建草稿失败，请检查网络连接！");
                        }

                        @Override
                        public void onComplete() {
                            Logger.d("CreateDraft onComplete");
                        }
                    });
        }
    }

    private void reviseDraft(Draft draft) {
        if (draft != null) {
            Map<String, String> parameter = new HashMap<>();
            parameter.put("id", draft.id);
            parameter.put("name", draft.name);
            parameter.put("book_id", draft.book_id);
            parameter.put("update_time", String.valueOf(draft.update_time));

            if (!TextUtils.isEmpty(draft.content)) {
                parameter.put("content", draft.content);
            }

            DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, true);
            dataRequestService.updateDraft(parameter)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(Schedulers.io())
                    .doOnNext(new Consumer<CommunalResult<DraftCreate>>() {
                        @Override
                        public void accept(@NonNull CommunalResult<DraftCreate> draftCreateResult) throws Exception {
                            Logger.d("UpdateDraft call: 数据库存储");
                            if (draftCreateResult.getCode() == 0 && draftCreateResult.getModel() != null && draftCreateResult.getModel().draft != null) {

                                Draft draft = draftCreateResult.getModel().draft;

                                if (draftDao == null) {
                                    draftDao = DraftDao.getInstance(draftListActivity.getQuApplicationContext());
                                }

                                if (draftDao.isContainsDraft(draft.id)) {
                                    draft.need_synchronize = 0;
                                    draftDao.updateDraft(draft);
                                } else {
                                    draftDao.insertDraft(draft);
                                }
                            }
                            Logger.d("UpdateDraft call: 数据库存储结束");
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new ResourceSubscriber<CommunalResult<DraftCreate>>() {

                        @Override
                        public void onNext(CommunalResult<DraftCreate> draftCreateResult) {
                            Logger.d("UpdateDraft onNext");

                            if (draftCreateResult != null) {
                                if (draftCreateResult.getCode() == 401) {
                                    draftListActivity.showToast("登录令牌失效，请重新登录");
                                    draftListActivity.startLoginActivity();
                                } else {
                                    if (draftCreateResult.getCode() == 0 && draftCreateResult.getModel() != null && draftCreateResult.getModel().draft != null) {
                                        draftListActivity.showToast("草稿修改成功！");
                                    } else if (!TextUtils.isEmpty(draftCreateResult.getMessage())) {
                                        draftListActivity.showToast(draftCreateResult.getMessage());
                                    }
                                }
                            } else {
                                draftListActivity.showToast("网络连接异常，修改草稿失败！");
                            }
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            Logger.d("UpdateDraft onError: " + throwable.toString());

                            draftListActivity.showToast("修改草稿失败，请检查网络连接！");
                        }

                        @Override
                        public void onComplete() {
                            Logger.d("UpdateDraft onComplete");
                        }
                    });
        }
    }
}
