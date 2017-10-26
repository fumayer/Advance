package com.quduquxie.function.creation.write.presenter;

import android.text.TextUtils;

import com.orhanobut.logger.Logger;
import com.quduquxie.base.util.NetworkUtil;
import com.quduquxie.db.DraftDao;
import com.quduquxie.db.LiteratureDao;
import com.quduquxie.db.SectionDao;
import com.quduquxie.function.creation.write.SectionWriteInterface;
import com.quduquxie.function.creation.write.view.SectionWriteActivity;
import com.quduquxie.model.creation.Draft;
import com.quduquxie.model.creation.DraftCreate;
import com.quduquxie.model.creation.DraftPublish;
import com.quduquxie.model.creation.Literature;
import com.quduquxie.model.creation.Section;
import com.quduquxie.model.creation.SectionPublish;
import com.quduquxie.model.v2.Rank;
import com.quduquxie.model.v2.SensitiveWord;
import com.quduquxie.retrofit.DataRequestService;
import com.quduquxie.retrofit.ServiceGenerator;
import com.quduquxie.retrofit.model.CommunalResult;
import com.quduquxie.util.MD5;
import com.quduquxie.widget.LoadingPage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created on 17/4/10.
 * Created by crazylei.
 */

public class SectionWritePresenter implements SectionWriteInterface.Presenter {

    private SectionWriteActivity sectionWriteActivity;

    private static final int title_limit = 20;

    private DraftDao draftDao;

    private SectionDao sectionDao;

    private LiteratureDao literatureDao;

    private Literature literature;

    private LoadingPage loadingPage;

    public SectionWritePresenter(SectionWriteActivity sectionWriteActivity) {
        this.sectionWriteActivity = sectionWriteActivity;
        this.draftDao = DraftDao.getInstance(sectionWriteActivity.getQuApplicationContext());
        this.literatureDao = LiteratureDao.getInstance(sectionWriteActivity.getQuApplicationContext());
    }

    @Override
    public void initParameter() {

    }

    @Override
    public void initParameter(Literature literature) {
        if (literature != null) {
            this.literature = literature;

            sectionWriteActivity.showWriteView();

            if (literatureDao == null) {
                literatureDao = LiteratureDao.getInstance(sectionWriteActivity.getQuApplicationContext());
            }

            Literature localLiterature = literatureDao.getLiterature(literature.id);

            if (localLiterature != null) {
                if (localLiterature.serial_number != 0) {
                    sectionWriteActivity.initView(title_limit, "第" + (localLiterature.serial_number + 1) + "章");
                } else {
                    sectionWriteActivity.initView(title_limit, "第1章");
                }
            } else {
                if (literature.chapter != null) {
                    sectionWriteActivity.initView(title_limit, "第" + (literature.serial_number + 1) + "章");
                } else {
                    sectionWriteActivity.initView(title_limit, "第1章");
                }
            }
        } else {
            sectionWriteActivity.showErrorView();
        }
    }

    @Override
    public void initDraft(Draft draft) {
        if (draft.need_synchronize == 1) {

            sectionWriteActivity.showLoadingPage();

            DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, true);
            dataRequestService.loadDraft(draft.id)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(Schedulers.io())
                    .doOnNext(new Consumer<CommunalResult<DraftCreate>>() {
                        @Override
                        public void accept(@NonNull CommunalResult<DraftCreate> draftCreateResult) throws Exception {
                            Logger.d("LoadDraft call: 数据库存储");
                            if (draftCreateResult.getCode() == 0 && draftCreateResult.getModel() != null && draftCreateResult.getModel().draft != null) {

                                Draft draftObject = draftCreateResult.getModel().draft;
                                draftObject.need_synchronize = 0;

                                if (draftDao == null) {
                                    draftDao = DraftDao.getInstance(sectionWriteActivity.getQuApplicationContext());
                                }

                                if (draftDao.isContainsDraft(draftObject.id)) {
                                    draftDao.updateDraft(draftObject);
                                } else {
                                    draftDao.insertDraft(draftObject);
                                }
                            }
                            Logger.d("LoadDraft call: 数据库存储结束");
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new ResourceSubscriber<CommunalResult<DraftCreate>>() {
                        @Override
                        public void onNext(CommunalResult<DraftCreate> draftCreateResult) {
                            Logger.d("LoadDraft onNext");

                            sectionWriteActivity.hideLoadingPage();

                            if (draftCreateResult != null) {
                                if (draftCreateResult.getCode() == 401) {
                                    sectionWriteActivity.showToast("登录令牌失效，请重新登录");
                                    sectionWriteActivity.startLoginActivity();
                                } else {
                                    if (draftCreateResult.getCode() == 0 && draftCreateResult.getModel() != null && draftCreateResult.getModel().draft != null) {

                                        Draft draftObject = draftCreateResult.getModel().draft;

                                        sectionWriteActivity.setChapterInformation(draftObject.name, draftObject.content);

                                    } else if (!TextUtils.isEmpty(draftCreateResult.getMessage())) {
                                        sectionWriteActivity.showLoadingError();
                                        sectionWriteActivity.showToast(draftCreateResult.getMessage());
                                    }
                                }
                            } else {
                                sectionWriteActivity.showLoadingError();
                                sectionWriteActivity.showToast("获取草稿内容失败！");
                            }
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            Logger.d("LoadDraft onError: " + throwable.toString());

                            sectionWriteActivity.showLoadingError();
                            sectionWriteActivity.showToast("获取草稿内容失败，请检查网络连接！");
                        }

                        @Override
                        public void onComplete() {
                            Logger.d("LoadDraft onComplete");
                        }
                    });
        } else {
            sectionWriteActivity.setChapterInformation(draft.name, draft.content);
        }
    }

    @Override
    public void saveDraft(final String title, final String content) {
        if (NetworkUtil.loadNetworkType(sectionWriteActivity.getQuApplicationContext()) != NetworkUtil.NETWORK_NONE) {
            if (!TextUtils.isEmpty(title) && title.length() <= title_limit && literature != null && !TextUtils.isEmpty(literature.id)) {

                Map<String, String> parameter = new HashMap<>();
                parameter.put("name", title);
                parameter.put("book_id", literature.id);

                if (!TextUtils.isEmpty(content)) {
                    parameter.put("content", content);
                }

                DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, true);
                dataRequestService.createDraft(parameter)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ResourceSubscriber<CommunalResult<DraftCreate>>() {
                            @Override
                            public void onNext(CommunalResult<DraftCreate> draftCreateResult) {
                                Logger.d("CreateDraft onNext");
                                sectionWriteActivity.refreshSaveButtonState(true);
                                if (draftCreateResult != null) {
                                    if (draftCreateResult.getCode() == 401) {
                                        sectionWriteActivity.showToast("登录令牌失效，请重新登录！");
                                        sectionWriteActivity.startLoginActivity();
                                    } else {
                                        if (draftCreateResult.getCode() == 0 && draftCreateResult.getModel() != null && draftCreateResult.getModel().draft != null) {

                                            Draft draft = draftCreateResult.getModel().draft;

                                            if (draftDao == null) {
                                                draftDao = DraftDao.getInstance(sectionWriteActivity.getQuApplicationContext());
                                            }

                                            if (draftDao.isContainsDraft(draft.id)) {
                                                draft.need_synchronize = 0;
                                                draftDao.updateDraft(draft);
                                            } else {
                                                draftDao.insertDraft(draft);
                                            }

                                            sectionWriteActivity.finishActivity();

                                        } else if (!TextUtils.isEmpty(draftCreateResult.getMessage())) {
                                            sectionWriteActivity.showToast(draftCreateResult.getMessage());
                                        }
                                    }
                                } else {
                                    Draft draft = new Draft();
                                    draft.local = MD5.encodeMD5String(title + content + System.currentTimeMillis());
                                    draft.name = title;
                                    draft.content = content;
                                    draft.word_count = content.replace(" ", "").replace("\n", "").length();
                                    draft.book_id = literature.id;
                                    draft.need_synchronize = 2;

                                    if (draftDao == null) {
                                        draftDao = DraftDao.getInstance(sectionWriteActivity.getQuApplicationContext());
                                    }

                                    draftDao.insertDraft(draft);

                                    sectionWriteActivity.showToast("草稿存储失败，已将草稿存储到本地数据库！");
                                    sectionWriteActivity.finishActivity();
                                }
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                Logger.d("CreateDraft onError: " + throwable.toString());

                                Draft draft = new Draft();
                                draft.local = MD5.encodeMD5String(title + content + System.currentTimeMillis());
                                draft.name = title;
                                draft.content = content;
                                draft.word_count = content.replace(" ", "").replace("\n", "").length();
                                draft.book_id = literature.id;
                                draft.need_synchronize = 2;

                                if (draftDao == null) {
                                    draftDao = DraftDao.getInstance(sectionWriteActivity.getQuApplicationContext());
                                }

                                draftDao.insertDraft(draft);

                                sectionWriteActivity.refreshSaveButtonState(true);

                                sectionWriteActivity.showToast("草稿存储失败，已将草稿存储到本地数据库！");
                                sectionWriteActivity.finishActivity();
                            }

                            @Override
                            public void onComplete() {
                                Logger.d("CreateDraft onComplete");
                            }
                        });
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                if (TextUtils.isEmpty(title)) {
                    if (stringBuilder.length() != 0) {
                        stringBuilder.append("\n");
                    }
                    stringBuilder.append("请编写章节名称");
                } else if (title.length() > title_limit) {
                    if (stringBuilder.length() != 0) {
                        stringBuilder.append("\n");
                    }
                    stringBuilder.append("章节名称过长");
                }
                if (literature == null || TextUtils.isEmpty(literature.id)) {
                    if (stringBuilder.length() != 0) {
                        stringBuilder.append("\n");
                    }
                    stringBuilder.append("作品ID遗失，请退出重新编辑");
                }

                if (stringBuilder.length() > 0) {
                    sectionWriteActivity.showToast(stringBuilder.toString());
                }
                sectionWriteActivity.refreshSaveButtonState(true);
            }
        } else {
            sectionWriteActivity.showToast("网络连接异常，草稿存储失败！");
        }
    }

    @Override
    public void reviseDraft(final Draft draft, final String title, final String content) {
        if (NetworkUtil.loadNetworkType(sectionWriteActivity.getQuApplicationContext()) != NetworkUtil.NETWORK_NONE) {
            if (draft != null) {
                if (!TextUtils.isEmpty(draft.id)) {
                    if (!TextUtils.isEmpty(title) && title.length() <= title_limit && literature != null && !TextUtils.isEmpty(literature.id)) {
                        Map<String, String> parameter = new HashMap<>();

                        parameter.put("name", title);
                        parameter.put("book_id", draft.book_id);

                        if (!TextUtils.isEmpty(draft.id)) {
                            parameter.put("id", draft.id);
                        }
                        if (!TextUtils.isEmpty(content)) {
                            parameter.put("content", content);
                        }

                        if (draft.update_time != 0) {
                            parameter.put("update_time", String.valueOf(draft.update_time));
                        }

                        DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, true);
                        dataRequestService.updateDraft(parameter)
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new ResourceSubscriber<CommunalResult<DraftCreate>>() {
                                    @Override
                                    public void onNext(CommunalResult<DraftCreate> draftCreateResult) {
                                        Logger.d("UpdateDraft onNext");

                                        sectionWriteActivity.refreshSaveButtonState(true);

                                        if (draftCreateResult != null) {
                                            if (draftCreateResult.getCode() == 401) {
                                                sectionWriteActivity.showToast("登录令牌失效，请重新登录！");
                                                sectionWriteActivity.startLoginActivity();
                                            } else {
                                                if (draftCreateResult.getCode() == 0 && draftCreateResult.getModel() != null && draftCreateResult.getModel().draft != null) {

                                                    Draft draftObject = draftCreateResult.getModel().draft;
                                                    if (!TextUtils.isEmpty(content)) {
                                                        draftObject.content = content;
                                                    }

                                                    if (draftDao == null) {
                                                        draftDao = DraftDao.getInstance(sectionWriteActivity.getQuApplicationContext());
                                                    }

                                                    if (draftDao.isContainsDraft(draftObject.id)) {
                                                        draftObject.need_synchronize = 0;
                                                        draftDao.updateDraft(draftObject);
                                                    } else {
                                                        draftDao.insertDraft(draftObject);
                                                    }

                                                    sectionWriteActivity.finishActivity();

                                                } else if (!TextUtils.isEmpty(draftCreateResult.getMessage())) {
                                                    sectionWriteActivity.showToast(draftCreateResult.getMessage());
                                                }
                                            }
                                        } else {
                                            Draft localDraft = new Draft();
                                            localDraft.name = title;
                                            localDraft.content = content;
                                            localDraft.word_count = content.replace(" ", "").replace("\n", "").length();
                                            localDraft.book_id = literature.id;
                                            localDraft.need_synchronize = 2;

                                            if (draftDao == null) {
                                                draftDao = DraftDao.getInstance(sectionWriteActivity.getQuApplicationContext());
                                            }

                                            localDraft.id = draft.id;
                                            if (draftDao.isContainsDraft(localDraft.id)) {
                                                draftDao.updateDraft(localDraft);
                                            } else {
                                                draftDao.insertDraft(localDraft);
                                            }

                                            sectionWriteActivity.refreshSaveButtonState(true);

                                            sectionWriteActivity.showToast("草稿同步失败，已存储草稿到本地数据库，请检查网络连接！");
                                            sectionWriteActivity.finishActivity();
                                        }
                                    }

                                    @Override
                                    public void onError(Throwable throwable) {
                                        Logger.d("UpdateDraft onError: " + throwable.toString());

                                        Draft localDraft = new Draft();
                                        localDraft.name = title;
                                        localDraft.content = content;
                                        localDraft.word_count = content.replace(" ", "").replace("\n", "").length();
                                        localDraft.book_id = literature.id;
                                        localDraft.need_synchronize = 2;

                                        if (draftDao == null) {
                                            draftDao = DraftDao.getInstance(sectionWriteActivity.getQuApplicationContext());
                                        }

                                        localDraft.id = draft.id;
                                        if (draftDao.isContainsDraft(localDraft.id)) {
                                            draftDao.updateDraft(localDraft);
                                        } else {
                                            draftDao.insertDraft(localDraft);
                                        }

                                        sectionWriteActivity.refreshSaveButtonState(true);

                                        sectionWriteActivity.showToast("草稿同步失败，已存储草稿到本地数据库，请检查网络连接！");
                                        sectionWriteActivity.finishActivity();
                                    }

                                    @Override
                                    public void onComplete() {
                                        Logger.d("UpdateDraft onComplete");
                                    }
                                });
                    } else {
                        StringBuilder stringBuilder = new StringBuilder();
                        if (TextUtils.isEmpty(title)) {
                            if (stringBuilder.length() != 0) {
                                stringBuilder.append("\n");
                            }
                            stringBuilder.append("请编写章节名称");
                        } else if (title.length() > title_limit) {
                            if (stringBuilder.length() != 0) {
                                stringBuilder.append("\n");
                            }
                            stringBuilder.append("章节名称过长");
                        } else if (literature == null || TextUtils.isEmpty(literature.id)) {
                            if (stringBuilder.length() != 0) {
                                stringBuilder.append("\n");
                            }
                            stringBuilder.append("作品ID遗失，请退出重新编辑");
                        }
                        if (stringBuilder.length() > 0) {
                            sectionWriteActivity.showToast(stringBuilder.toString());
                        }
                        sectionWriteActivity.refreshSaveButtonState(true);
                    }
                } else {
                    if (!TextUtils.isEmpty(title) && title.length() <= title_limit && literature != null && !TextUtils.isEmpty(literature.id)) {

                        Map<String, String> parameter = new HashMap<>();

                        parameter.put("name", title);
                        parameter.put("book_id", literature.id);

                        if (!TextUtils.isEmpty(content)) {
                            parameter.put("content", content);
                        }

                        DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, true);
                        dataRequestService.createDraft(parameter)
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new ResourceSubscriber<CommunalResult<DraftCreate>>() {
                                    @Override
                                    public void onNext(CommunalResult<DraftCreate> draftCreateResult) {
                                        Logger.d("UpdateDraft onNext");

                                        sectionWriteActivity.refreshSaveButtonState(true);

                                        if (draftCreateResult != null) {
                                            if (draftCreateResult.getCode() == 401) {
                                                sectionWriteActivity.showToast("登录令牌失效，请重新登录！");
                                                sectionWriteActivity.startLoginActivity();
                                            } else {
                                                if (draftCreateResult.getCode() == 0 && draftCreateResult.getModel() != null && draftCreateResult.getModel().draft != null) {

                                                    Draft draftObject = draftCreateResult.getModel().draft;
                                                    if (!TextUtils.isEmpty(content)) {
                                                        draftObject.content = content;
                                                    }

                                                    if (draftDao == null) {
                                                        draftDao = DraftDao.getInstance(sectionWriteActivity.getQuApplicationContext());
                                                    }

                                                    if (!TextUtils.isEmpty(draft.local)) {
                                                        draftDao.deleteLocalDraft(draft.local);
                                                    } else if (!TextUtils.isEmpty(draft.id)) {
                                                        draftDao.deleteDraft(draft.id);
                                                    }

                                                    if (draftDao.isContainsDraft(draftObject.id)) {
                                                        draftObject.need_synchronize = 0;
                                                        draftDao.updateDraft(draftObject);
                                                    } else {
                                                        draftDao.insertDraft(draftObject);
                                                    }

                                                    sectionWriteActivity.finishActivity();

                                                } else if (!TextUtils.isEmpty(draftCreateResult.getMessage())) {
                                                    sectionWriteActivity.showToast(draftCreateResult.getMessage());
                                                }
                                            }
                                        } else {
                                            Draft localDraft = new Draft();
                                            localDraft.name = title;
                                            localDraft.content = content;
                                            localDraft.word_count = content.replace(" ", "").replace("\n", "").length();
                                            localDraft.book_id = literature.id;
                                            localDraft.need_synchronize = 2;

                                            if (draftDao == null) {
                                                draftDao = DraftDao.getInstance(sectionWriteActivity.getQuApplicationContext());
                                            }

                                            if (!TextUtils.isEmpty(draft.local)) {
                                                localDraft.local = draft.local;
                                                if (draftDao.isContainsLocalDraft(localDraft.local)) {
                                                    draftDao.updateLocalDraft(localDraft);
                                                } else {
                                                    draftDao.insertDraft(localDraft);
                                                }
                                            }

                                            sectionWriteActivity.refreshSaveButtonState(true);

                                            sectionWriteActivity.showToast("草稿同步失败，已存储草稿到本地数据库，请检查网络连接！");
                                            sectionWriteActivity.finishActivity();
                                        }
                                    }

                                    @Override
                                    public void onError(Throwable throwable) {
                                        Logger.d("UpdateDraft onError: " + throwable.toString());

                                        Draft localDraft = new Draft();
                                        localDraft.name = title;
                                        localDraft.content = content;
                                        localDraft.word_count = content.replace(" ", "").replace("\n", "").length();
                                        localDraft.book_id = literature.id;
                                        localDraft.need_synchronize = 2;

                                        if (draftDao == null) {
                                            draftDao = DraftDao.getInstance(sectionWriteActivity.getQuApplicationContext());
                                        }

                                        if (!TextUtils.isEmpty(draft.local)) {
                                            localDraft.local = draft.local;
                                            if (draftDao.isContainsLocalDraft(localDraft.local)) {
                                                draftDao.updateLocalDraft(localDraft);
                                            } else {
                                                draftDao.insertDraft(localDraft);
                                            }
                                        }

                                        sectionWriteActivity.refreshSaveButtonState(true);

                                        sectionWriteActivity.showToast("草稿同步失败，已存储草稿到本地数据库，请检查网络连接！");
                                        sectionWriteActivity.finishActivity();
                                    }

                                    @Override
                                    public void onComplete() {
                                        Logger.d("UpdateDraft onComplete");
                                    }
                                });
                    } else {
                        StringBuilder stringBuilder = new StringBuilder();
                        if (TextUtils.isEmpty(title)) {
                            if (stringBuilder.length() != 0) {
                                stringBuilder.append("\n");
                            }
                            stringBuilder.append("请编写章节名称");
                        } else if (title.length() > title_limit) {
                            if (stringBuilder.length() != 0) {
                                stringBuilder.append("\n");
                            }
                            stringBuilder.append("章节名称过长");
                        } else if (literature == null || TextUtils.isEmpty(literature.id)) {
                            if (stringBuilder.length() != 0) {
                                stringBuilder.append("\n");
                            }
                            stringBuilder.append("作品ID遗失，请退出重新编辑");
                        }
                        if (stringBuilder.length() > 0) {
                            sectionWriteActivity.showToast(stringBuilder.toString());
                        }
                        sectionWriteActivity.refreshSaveButtonState(true);
                    }
                }
            }
        } else {
            sectionWriteActivity.showToast("网络连接异常，草稿同步失败！");
        }
    }

    @Override
    public void filterSensitiveWord(final String content) {
        if (!TextUtils.isEmpty(content)) {
            DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, false);
            dataRequestService.filterSensitiveWord(content)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new ResourceSubscriber<CommunalResult<ArrayList<SensitiveWord>>>() {
                        @Override
                        public void onNext(CommunalResult<ArrayList<SensitiveWord>> sensitiveWordResult) {
                            Logger.d("FilterSensitiveWord onNext");
                            if (sensitiveWordResult != null) {
                                if (sensitiveWordResult.getCode() == 0) {
                                    ArrayList<SensitiveWord> sensitiveWords = sensitiveWordResult.getModel();

                                    if (sensitiveWords != null && sensitiveWords.size() > 0) {
                                        int sensitiveOrdinary = 0;
                                        int sensitiveDanger = 0;
                                        int sensitiveHighRisk = 0;

                                        Pattern pattern;
                                        Matcher matcher;

                                        for (SensitiveWord sensitiveWord : sensitiveWords) {
                                            pattern = Pattern.compile(sensitiveWord.word);
                                            matcher = pattern.matcher(content);
                                            while (matcher.find()) {
                                                if (sensitiveWord.rank.equals(Rank.A)) {
                                                    sensitiveOrdinary += 1;
                                                } else if (sensitiveWord.rank.equals(Rank.B)) {
                                                    sensitiveDanger += 1;
                                                } else if (sensitiveWord.rank.equals(Rank.C)) {
                                                    sensitiveHighRisk +=1;
                                                }
                                            }
                                        }

                                        Logger.d("filterSensitiveWord: " + sensitiveOrdinary + " : " + sensitiveDanger + " : " + sensitiveHighRisk);

                                        if (sensitiveHighRisk > 0) {
                                            sectionWriteActivity.highlightSensitiveWord(sensitiveWords);
                                            sectionWriteActivity.showToast("当前内容包含敏感词，请修改后发布！");
                                        } else if (sensitiveDanger >= 10) {
                                            sectionWriteActivity.highlightSensitiveWord(sensitiveWords);
                                            sectionWriteActivity.showToast("当前内容包含敏感词，请修改后发布！");
                                        } else if (sensitiveDanger > 0 && sensitiveDanger < 10) {
                                            sectionWriteActivity.highlightSensitiveWord(sensitiveWords);
                                            sectionWriteActivity.showPromptDialog("当前内容涉及风险较高，继续提交有可能被驳回，是否继续？");
                                        } else if (sensitiveOrdinary > 50) {
                                            sectionWriteActivity.highlightSensitiveWord(sensitiveWords);
                                            sectionWriteActivity.showPromptDialog("当前内容涉及风险较高，继续提交有可能被驳回，是否继续？");
                                        } else {
                                            sectionWriteActivity.publishChapter();
                                        }
                                    } else {
                                        sectionWriteActivity.publishChapter();
                                    }
                                } else if (!TextUtils.isEmpty(sensitiveWordResult.getMessage())) {
                                    sectionWriteActivity.showToast(sensitiveWordResult.getMessage());
                                    sectionWriteActivity.refreshPublishButtonState(true);
                                } else {
                                    sectionWriteActivity.showToast("敏感词检查失败！");
                                    sectionWriteActivity.refreshPublishButtonState(true);
                                }
                            } else {
                                sectionWriteActivity.publishChapter();
                            }
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            Logger.d("FilterSensitiveWord onError: " + throwable.toString());
                            sectionWriteActivity.showToast("敏感词检查失败！");
                            sectionWriteActivity.refreshPublishButtonState(true);
                        }

                        @Override
                        public void onComplete() {
                            Logger.d("FilterSensitiveWord onComplete");
                        }
                    });
        } else {
            sectionWriteActivity.publishChapter();
        }
    }

    @Override
    public void publishChapter(final Draft draft, final String title, final String content) {
        if (NetworkUtil.loadNetworkType(sectionWriteActivity.getQuApplicationContext()) != NetworkUtil.NETWORK_NONE) {
            if (draft != null) {
                if (!TextUtils.isEmpty(draft.id)) {
                    if (!TextUtils.isEmpty(title) && title.length() <= title_limit && literature != null && !TextUtils.isEmpty(literature.id)) {
                        Map<String, String> parameter = new HashMap<>();

                        parameter.put("id", draft.id);
                        parameter.put("name", title);
                        parameter.put("book_id", literature.id);

                        if (draft.update_time != 0) {
                            parameter.put("update_time", String.valueOf(draft.update_time));
                        }

                        if (!TextUtils.isEmpty(content)) {
                            parameter.put("content", content);
                        }

                        DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, true);
                        dataRequestService.publishDraft(parameter)
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(Schedulers.io())
                                .doOnNext(new Consumer<CommunalResult<DraftPublish>>() {
                                    @Override
                                    public void accept(@NonNull CommunalResult<DraftPublish> draftPublishResult) throws Exception {
                                        if (draftPublishResult.getCode() == 0 && draftPublishResult.getModel() != null && draftPublishResult.getModel().draft != null) {

                                            Section section = draftPublishResult.getModel().draft;

                                            if (sectionDao == null) {
                                                sectionDao = new SectionDao(sectionWriteActivity.getQuApplicationContext(), literature.id);
                                            }

                                            if (sectionDao.isContainsSection(section.id)) {
                                                sectionDao.updateSection(section);
                                            } else {
                                                sectionDao.insertSection(section);
                                            }

                                            if (literatureDao == null) {
                                                literatureDao = LiteratureDao.getInstance(sectionWriteActivity.getQuApplicationContext());
                                            }

                                            literature.serial_number = section.serial_number;

                                            if (literatureDao.isContainsLiterature(literature.id)) {
                                                literatureDao.updateLiterature(literature);
                                            } else {
                                                literatureDao.insertLiterature(literature);
                                            }

                                            if (draftDao == null) {
                                                draftDao = DraftDao.getInstance(sectionWriteActivity.getQuApplicationContext());
                                            }

                                            if (!TextUtils.isEmpty(draft.id)) {
                                                draftDao.deleteDraft(draft.id);
                                            } else if (!TextUtils.isEmpty(draft.local)) {
                                                draftDao.deleteLocalDraft(draft.local);
                                            }
                                        }
                                    }
                                })
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new ResourceSubscriber<CommunalResult<DraftPublish>>() {
                                    @Override
                                    public void onNext(CommunalResult<DraftPublish> draftPublishResult) {
                                        Logger.d("PublishDraft onNext");

                                        sectionWriteActivity.refreshPublishButtonState(true);

                                        if (draftPublishResult != null) {
                                            if (draftPublishResult.getCode() == 401) {
                                                sectionWriteActivity.showToast("登录令牌失效，请重新登录！");
                                                sectionWriteActivity.startLoginActivity();
                                            } else {
                                                if (draftPublishResult.getCode() == 0 && draftPublishResult.getModel() != null && draftPublishResult.getModel().draft != null) {

                                                    sectionWriteActivity.finishActivity();

                                                } else if (!TextUtils.isEmpty(draftPublishResult.getMessage())) {
                                                    sectionWriteActivity.showToast(draftPublishResult.getMessage());
                                                }
                                            }
                                        } else {
                                            sectionWriteActivity.showToast("章节发布失败，请检查网络连接！");
                                        }
                                    }

                                    @Override
                                    public void onError(Throwable throwable) {
                                        Logger.d("PublishDraft onError: " + throwable.toString());

                                        sectionWriteActivity.showToast("章节发布失败，请检查网络连接！");
                                        sectionWriteActivity.refreshPublishButtonState(true);
                                    }

                                    @Override
                                    public void onComplete() {
                                        Logger.d("PublishDraft onComplete");
                                    }
                                });
                    } else {
                        StringBuilder stringBuilder = new StringBuilder();
                        if (TextUtils.isEmpty(title)) {
                            if (stringBuilder.length() != 0) {
                                stringBuilder.append("\n");
                            }
                            stringBuilder.append("请编写章节名称");
                        } else if (title.length() > title_limit) {
                            if (stringBuilder.length() != 0) {
                                stringBuilder.append("\n");
                            }
                            stringBuilder.append("章节名称过长");
                        }
                        if (literature == null || TextUtils.isEmpty(literature.id)) {
                            if (stringBuilder.length() != 0) {
                                stringBuilder.append("\n");
                            }
                            stringBuilder.append("作品ID遗失，请退出重新编辑");
                        }

                        if (stringBuilder.length() > 0) {
                            sectionWriteActivity.showToast(stringBuilder.toString());
                        }
                        sectionWriteActivity.refreshPublishButtonState(true);
                    }
                } else {
                    if (!TextUtils.isEmpty(title) && title.length() <= title_limit && literature != null && !TextUtils.isEmpty(literature.id)) {
                        Map<String, String> parameter = new HashMap<>();

                        parameter.put("name", title);
                        parameter.put("book_id", literature.id);

                        if (draft.update_time != 0) {
                            parameter.put("update_time", String.valueOf(draft.update_time));
                        }

                        if (!TextUtils.isEmpty(content)) {
                            parameter.put("content", content);
                        }

                        DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, true);
                        dataRequestService.publishSection(parameter)
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(Schedulers.io())
                                .doOnNext(new Consumer<CommunalResult<SectionPublish>>() {
                                    @Override
                                    public void accept(@NonNull CommunalResult<SectionPublish> sectionPublishResult) throws Exception {
                                        if (sectionPublishResult.getCode() == 0 && sectionPublishResult.getModel() != null && sectionPublishResult.getModel().chapter != null) {

                                            Section section = sectionPublishResult.getModel().chapter;

                                            if (sectionDao == null) {
                                                sectionDao = new SectionDao(sectionWriteActivity.getQuApplicationContext(), literature.id);
                                            }

                                            if (sectionDao.isContainsSection(section.id)) {
                                                sectionDao.updateSection(section);
                                            } else {
                                                sectionDao.insertSection(section);
                                            }

                                            if (literatureDao == null) {
                                                literatureDao = LiteratureDao.getInstance(sectionWriteActivity.getQuApplicationContext());
                                            }

                                            literature.serial_number = section.serial_number;

                                            if (literatureDao.isContainsLiterature(literature.id)) {
                                                literatureDao.updateLiterature(literature);
                                            } else {
                                                literatureDao.insertLiterature(literature);
                                            }

                                            if (draftDao == null) {
                                                draftDao = DraftDao.getInstance(sectionWriteActivity.getQuApplicationContext());
                                            }

                                            if (!TextUtils.isEmpty(draft.local)) {
                                                draftDao.deleteLocalDraft(draft.local);
                                            }
                                        }
                                    }
                                })
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new ResourceSubscriber<CommunalResult<SectionPublish>>() {
                                    @Override
                                    public void onNext(CommunalResult<SectionPublish> sectionPublishResult) {
                                        Logger.d("PublishDraft onNext");

                                        sectionWriteActivity.refreshPublishButtonState(true);

                                        if (sectionPublishResult != null) {
                                            if (sectionPublishResult.getCode() == 401) {
                                                sectionWriteActivity.showToast("登录令牌失效，请重新登录！");
                                                sectionWriteActivity.startLoginActivity();
                                            } else {
                                                if (sectionPublishResult.getCode() == 0 && sectionPublishResult.getModel() != null && sectionPublishResult.getModel().chapter != null) {

                                                    sectionWriteActivity.finishActivity();

                                                } else if (!TextUtils.isEmpty(sectionPublishResult.getMessage())) {
                                                    sectionWriteActivity.showToast(sectionPublishResult.getMessage());
                                                }
                                            }
                                        } else {
                                            sectionWriteActivity.showToast("章节发布失败，请检查网络连接！");
                                        }
                                    }

                                    @Override
                                    public void onError(Throwable throwable) {
                                        Logger.d("publishSection onError: " + throwable.toString());

                                        sectionWriteActivity.showToast("章节发布失败，请检查网络连接！");
                                        sectionWriteActivity.refreshPublishButtonState(true);
                                    }

                                    @Override
                                    public void onComplete() {
                                        Logger.d("publishSection onComplete");
                                    }
                                });
                    } else {
                        StringBuilder stringBuilder = new StringBuilder();
                        if (TextUtils.isEmpty(title)) {
                            if (stringBuilder.length() != 0) {
                                stringBuilder.append("\n");
                            }
                            stringBuilder.append("请编写章节名称");
                        } else if (title.length() > title_limit) {
                            if (stringBuilder.length() != 0) {
                                stringBuilder.append("\n");
                            }
                            stringBuilder.append("章节名称过长");
                        }
                        if (literature == null || TextUtils.isEmpty(literature.id)) {
                            if (stringBuilder.length() != 0) {
                                stringBuilder.append("\n");
                            }
                            stringBuilder.append("作品ID遗失，请退出重新编辑");
                        }

                        if (stringBuilder.length() > 0) {
                            sectionWriteActivity.showToast(stringBuilder.toString());
                        }
                        sectionWriteActivity.refreshPublishButtonState(true);
                    }
                }
            } else {
                if (!TextUtils.isEmpty(title) && title.length() <= title_limit && !TextUtils.isEmpty(content) && literature != null && !TextUtils.isEmpty(literature.id)) {
                    Map<String, String> parameter = new HashMap<>();

                    parameter.put("name", title);
                    parameter.put("book_id", literature.id);

                    if (!TextUtils.isEmpty(content)) {
                        parameter.put("content", content);
                    }

                    DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, true);
                    dataRequestService.publishSection(parameter)
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(Schedulers.io())
                            .doOnNext(new Consumer<CommunalResult<SectionPublish>>() {
                                @Override
                                public void accept(@NonNull CommunalResult<SectionPublish> sectionPublishResult) throws Exception {
                                    if (sectionPublishResult.getCode() == 0 && sectionPublishResult.getModel() != null && sectionPublishResult.getModel().chapter != null) {

                                        Section section = sectionPublishResult.getModel().chapter;

                                        if (sectionDao == null) {
                                            sectionDao = new SectionDao(sectionWriteActivity.getQuApplicationContext(), literature.id);
                                        }

                                        if (sectionDao.isContainsSection(section.id)) {
                                            sectionDao.updateSection(section);
                                        } else {
                                            sectionDao.insertSection(section);
                                        }

                                        if (literatureDao == null) {
                                            literatureDao = LiteratureDao.getInstance(sectionWriteActivity.getQuApplicationContext());
                                        }

                                        literature.serial_number = section.serial_number;

                                        if (literatureDao.isContainsLiterature(literature.id)) {
                                            literatureDao.updateLiterature(literature);
                                        } else {
                                            literatureDao.insertLiterature(literature);
                                        }
                                    }
                                }
                            })
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new ResourceSubscriber<CommunalResult<SectionPublish>>() {
                                @Override
                                public void onNext(CommunalResult<SectionPublish> sectionPublishResult) {
                                    Logger.d("PublishSection onNext");

                                    sectionWriteActivity.refreshPublishButtonState(true);

                                    if (sectionPublishResult != null) {
                                        if (sectionPublishResult.getCode() == 401) {
                                            sectionWriteActivity.showToast("登录令牌失效，请重新登录！");
                                            sectionWriteActivity.startLoginActivity();
                                        } else {
                                            if (sectionPublishResult.getCode() == 0 && sectionPublishResult.getModel() != null && sectionPublishResult.getModel().chapter != null) {

                                                sectionWriteActivity.finishActivity();

                                            } else if (!TextUtils.isEmpty(sectionPublishResult.getMessage())) {
                                                sectionWriteActivity.showToast(sectionPublishResult.getMessage());
                                            }
                                        }
                                    } else {
                                        sectionWriteActivity.showToast("章节发布失败！");
                                    }
                                }

                                @Override
                                public void onError(Throwable throwable) {
                                    Logger.d("PublishSection onError: " + throwable.toString());

                                    sectionWriteActivity.refreshPublishButtonState(true);
                                    sectionWriteActivity.showToast("章节发布失败，请检查网络连接！");
                                }

                                @Override
                                public void onComplete() {
                                    Logger.d("PublishSection onComplete");
                                }
                            });
                } else {
                    StringBuilder stringBuilder = new StringBuilder();
                    if (TextUtils.isEmpty(title)) {
                        if (stringBuilder.length() != 0) {
                            stringBuilder.append("\n");
                        }
                        stringBuilder.append("请编写章节名称");
                    } else if (title.length() > title_limit) {
                        if (stringBuilder.length() != 0) {
                            stringBuilder.append("\n");
                        }
                        stringBuilder.append("章节名称过长");
                    }
                    if (TextUtils.isEmpty(content)) {
                        if (stringBuilder.length() != 0) {
                            stringBuilder.append("\n");
                        }
                        stringBuilder.append("请编写章节内容");
                    }

                    if (stringBuilder.length() > 0) {
                        sectionWriteActivity.showToast(stringBuilder.toString());
                    }
                    sectionWriteActivity.refreshPublishButtonState(true);
                }
            }
        } else {
            sectionWriteActivity.showToast("网络连接异常，章节发布失败！");
        }
    }

    @Override
    public void createDraft(String title, String content) {
        if (NetworkUtil.loadNetworkType(sectionWriteActivity.getQuApplicationContext()) != NetworkUtil.NETWORK_NONE) {
            if (literature != null && !TextUtils.isEmpty(literature.id)) {

                Map<String, String> parameter = new HashMap<>();

                if (!TextUtils.isEmpty(title) || !TextUtils.isEmpty(content)) {
                    if (TextUtils.isEmpty(title)) {
                        if (content.length() >= 20) {
                            title = content.substring(0, 20);
                        } else {
                            title = content;
                        }
                    }

                    parameter.put("name", title);
                    parameter.put("book_id", literature.id);

                    if (!TextUtils.isEmpty(content)) {
                        parameter.put("content", content);
                    }

                    final String draftTitle = title;
                    final String draftContent = content;

                    DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, true);
                    dataRequestService.createDraft(parameter)
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new ResourceSubscriber<CommunalResult<DraftCreate>>() {
                                @Override
                                public void onNext(CommunalResult<DraftCreate> draftCreateResult) {
                                    Logger.d("CreateDraft onNext");
                                    if (draftCreateResult != null) {
                                        if (draftCreateResult.getCode() == 0 && draftCreateResult.getModel() != null && draftCreateResult.getModel().draft != null) {

                                            Draft draftObject = draftCreateResult.getModel().draft;

                                            if (!TextUtils.isEmpty(draftContent)) {
                                                draftObject.content = draftContent;
                                            }

                                            if (draftDao == null) {
                                                draftDao = DraftDao.getInstance(sectionWriteActivity.getQuApplicationContext());
                                            }

                                            if (draftDao.isContainsDraft(draftObject.id)) {
                                                draftObject.need_synchronize = 0;
                                                draftDao.updateDraft(draftObject);
                                            } else {
                                                draftDao.insertDraft(draftObject);
                                            }

                                            Logger.d("自动保存草稿成功: " + draftObject.toString());

                                            sectionWriteActivity.showToast("自动保存草稿成功！");
                                            sectionWriteActivity.setAutoUpdateDraft(draftObject);
                                        } else if (!TextUtils.isEmpty(draftCreateResult.getMessage())) {
                                            Draft draft = new Draft();
                                            draft.name = draftTitle;
                                            draft.content = draftContent;
                                            draft.word_count = draftContent.replace(" ", "").replace("\n", "").length();
                                            draft.book_id = literature.id;
                                            draft.local = MD5.encodeMD5String(draftTitle + draftContent + System.currentTimeMillis());
                                            draft.need_synchronize = 2;

                                            if (draftDao == null) {
                                                draftDao = DraftDao.getInstance(sectionWriteActivity.getQuApplicationContext());
                                            }

                                            draftDao.insertDraft(draft);
                                            sectionWriteActivity.showToast(draftCreateResult.getMessage());
                                            sectionWriteActivity.setAutoUpdateDraft(draft);
                                        }
                                    } else {
                                        Draft draft = new Draft();
                                        draft.name = draftTitle;
                                        draft.content = draftContent;
                                        draft.word_count = draftContent.replace(" ", "").replace("\n", "").length();
                                        draft.book_id = literature.id;
                                        draft.local = MD5.encodeMD5String(draftTitle + draftContent + System.currentTimeMillis());
                                        draft.need_synchronize = 2;

                                        if (draftDao == null) {
                                            draftDao = DraftDao.getInstance(sectionWriteActivity.getQuApplicationContext());
                                        }

                                        draftDao.insertDraft(draft);
                                        sectionWriteActivity.showToast("自动保存草稿失败，已存储到本地数据库！");
                                        sectionWriteActivity.setAutoUpdateDraft(draft);
                                    }
                                }

                                @Override
                                public void onError(Throwable throwable) {
                                    Logger.d("CreateDraft onError: " + throwable.toString());

                                    Draft draft = new Draft();
                                    draft.name = draftTitle;
                                    draft.content = draftContent;
                                    draft.word_count = draftContent.replace(" ", "").replace("\n", "").length();
                                    draft.book_id = literature.id;
                                    draft.local = MD5.encodeMD5String(draftTitle + draftContent + System.currentTimeMillis());
                                    draft.need_synchronize = 2;

                                    if (draftDao == null) {
                                        draftDao = DraftDao.getInstance(sectionWriteActivity.getQuApplicationContext());
                                    }

                                    draftDao.insertDraft(draft);

                                    sectionWriteActivity.showToast("自动保存草稿失败，请检查网络连接！");
                                    sectionWriteActivity.setAutoUpdateDraft(draft);
                                }

                                @Override
                                public void onComplete() {
                                    Logger.d("CreateDraft onComplete");
                                }
                            });
                }
            }
        } else {

            if (literature != null && !TextUtils.isEmpty(literature.id)) {
                if (TextUtils.isEmpty(title)) {
                    if (content.length() >= 20) {
                        title = content.substring(0, 20);
                    } else {
                        title = content;
                    }
                }

                Draft draft = new Draft();
                draft.name = title;
                draft.content = content;
                draft.word_count = content.replace(" ", "").replace("\n", "").length();
                draft.book_id = literature.id;
                draft.local = MD5.encodeMD5String(title + content + System.currentTimeMillis());
                draft.need_synchronize = 2;

                if (draftDao == null) {
                    draftDao = DraftDao.getInstance(sectionWriteActivity.getQuApplicationContext());
                }

                draftDao.insertDraft(draft);

                sectionWriteActivity.showToast("网络连接异常，自动保存草稿失败！");
                sectionWriteActivity.setAutoUpdateDraft(draft);
            }
        }
    }

    @Override
    public void updateDraft(final Draft draft, String title, String content) {
        if (NetworkUtil.loadNetworkType(sectionWriteActivity.getQuApplicationContext()) != NetworkUtil.NETWORK_NONE) {
            if (literature != null && !TextUtils.isEmpty(literature.id)) {

                if (TextUtils.isEmpty(draft.id)) {

                    Map<String, String> parameter = new HashMap<>();

                    if (!TextUtils.isEmpty(title) || !TextUtils.isEmpty(content)) {
                        if (TextUtils.isEmpty(title)) {
                            if (content.length() >= 20) {
                                title = content.substring(0, 20);
                            } else {
                                title = content;
                            }
                        }

                        parameter.put("name", title);
                        parameter.put("book_id", literature.id);

                        if (!TextUtils.isEmpty(content)) {
                            parameter.put("content", content);
                        }

                        final String draftTitle = title;
                        final String draftContent = content;

                        DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, true);
                        dataRequestService.createDraft(parameter)
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new ResourceSubscriber<CommunalResult<DraftCreate>>() {
                                    @Override
                                    public void onNext(CommunalResult<DraftCreate> draftCreateResult) {
                                        Logger.d("CreateDraft onNext");
                                        if (draftCreateResult != null) {
                                            if (draftCreateResult.getCode() == 0 && draftCreateResult.getModel() != null && draftCreateResult.getModel().draft != null) {


                                                Draft draftObject = draftCreateResult.getModel().draft;
                                                if (!TextUtils.isEmpty(draftContent)) {
                                                    draftObject.content = draftContent;
                                                }

                                                if (draftDao == null) {
                                                    draftDao = DraftDao.getInstance(sectionWriteActivity.getQuApplicationContext());
                                                }

                                                if (!TextUtils.isEmpty(draft.local)) {
                                                    draftDao.deleteLocalDraft(draft.local);
                                                } else if (!TextUtils.isEmpty(draft.id)) {
                                                    draftDao.deleteDraft(draft.id);
                                                }

                                                if (draftDao.isContainsDraft(draftObject.id)) {
                                                    draftObject.need_synchronize = 0;
                                                    draftDao.updateDraft(draftObject);
                                                } else {
                                                    draftDao.insertDraft(draftObject);
                                                }

                                                Logger.d("自动保存草稿成功: " + draftObject.toString());

                                                sectionWriteActivity.showToast("自动保存草稿成功！");
                                                sectionWriteActivity.setAutoUpdateDraft(draftObject);
                                            } else if (!TextUtils.isEmpty(draftCreateResult.getMessage())) {
                                                Draft localDraft = new Draft();
                                                localDraft.name = draftTitle;
                                                localDraft.content = draftContent;
                                                localDraft.word_count = draftContent.replace(" ", "").replace("\n", "").length();
                                                localDraft.book_id = literature.id;
                                                localDraft.need_synchronize = 2;

                                                if (draftDao == null) {
                                                    draftDao = DraftDao.getInstance(sectionWriteActivity.getQuApplicationContext());
                                                }

                                                if (!TextUtils.isEmpty(draft.local)) {
                                                    localDraft.local = draft.local;
                                                    if (draftDao.isContainsLocalDraft(localDraft.local)) {
                                                        draftDao.updateLocalDraft(localDraft);
                                                    } else {
                                                        draftDao.insertDraft(localDraft);
                                                    }
                                                }

                                                sectionWriteActivity.showToast(draftCreateResult.getMessage());
                                                sectionWriteActivity.setAutoUpdateDraft(localDraft);
                                            }
                                        } else {
                                            Draft localDraft = new Draft();
                                            localDraft.name = draftTitle;
                                            localDraft.content = draftContent;
                                            localDraft.word_count = draftContent.replace(" ", "").replace("\n", "").length();
                                            localDraft.book_id = literature.id;
                                            localDraft.need_synchronize = 2;

                                            if (draftDao == null) {
                                                draftDao = DraftDao.getInstance(sectionWriteActivity.getQuApplicationContext());
                                            }

                                            if (!TextUtils.isEmpty(draft.local)) {
                                                localDraft.local = draft.local;
                                                if (draftDao.isContainsLocalDraft(localDraft.local)) {
                                                    draftDao.updateLocalDraft(localDraft);
                                                } else {
                                                    draftDao.insertDraft(localDraft);
                                                }
                                            }

                                            sectionWriteActivity.showToast("自动保存草稿失败，请检查网络连接！");
                                            sectionWriteActivity.setAutoUpdateDraft(localDraft);
                                        }
                                    }

                                    @Override
                                    public void onError(Throwable throwable) {
                                        Logger.d("CreateDraft onError: " + throwable.toString());

                                        Draft localDraft = new Draft();
                                        localDraft.name = draftTitle;
                                        localDraft.content = draftContent;
                                        localDraft.word_count = draftContent.replace(" ", "").replace("\n", "").length();
                                        localDraft.book_id = literature.id;
                                        localDraft.need_synchronize = 2;

                                        if (draftDao == null) {
                                            draftDao = DraftDao.getInstance(sectionWriteActivity.getQuApplicationContext());
                                        }


                                        if (!TextUtils.isEmpty(draft.local)) {
                                            localDraft.local = draft.local;
                                            if (draftDao.isContainsLocalDraft(localDraft.local)) {
                                                draftDao.updateLocalDraft(localDraft);
                                            } else {
                                                draftDao.insertDraft(localDraft);
                                            }
                                        }

                                        sectionWriteActivity.showToast("自动保存草稿失败，请检查网络连接！");
                                        sectionWriteActivity.setAutoUpdateDraft(draft);
                                    }

                                    @Override
                                    public void onComplete() {
                                        Logger.d("CreateDraft onComplete");
                                    }
                                });
                    }
                } else {
                    Map<String, String> parameter = new HashMap<>();

                    if (!TextUtils.isEmpty(title) || !TextUtils.isEmpty(content)) {
                        if (TextUtils.isEmpty(title)) {
                            if (content.length() >= 20) {
                                title = content.substring(0, 20);
                            } else {
                                title = content;
                            }
                        }
                        parameter.put("id", draft.id);
                        parameter.put("name", title);
                        parameter.put("book_id", draft.book_id);

                        if (!TextUtils.isEmpty(content)) {
                            parameter.put("content", content);
                        }

                        if (draft.update_time != 0) {
                            parameter.put("update_time", String.valueOf(draft.update_time));
                        }

                        final String draftTitle = title;
                        final String draftContent = content;

                        DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, true);
                        dataRequestService.updateDraft(parameter)
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new ResourceSubscriber<CommunalResult<DraftCreate>>() {
                                    @Override
                                    public void onNext(CommunalResult<DraftCreate> draftCreateResult) {
                                        Logger.d("UpdateDraft onNext");

                                        if (draftCreateResult != null) {
                                            if (draftCreateResult.getCode() == 0 && draftCreateResult.getModel() != null && draftCreateResult.getModel().draft != null) {
                                                Draft draftObject = draftCreateResult.getModel().draft;
                                                if (!TextUtils.isEmpty(draftContent)) {
                                                    draftObject.content = draftContent;
                                                }

                                                if (draftDao == null) {
                                                    draftDao = DraftDao.getInstance(sectionWriteActivity.getQuApplicationContext());
                                                }

                                                if (!TextUtils.isEmpty(draft.local)) {
                                                    draftDao.deleteLocalDraft(draft.local);
                                                } else if (!TextUtils.isEmpty(draft.id)) {
                                                    draftDao.deleteDraft(draft.id);
                                                }

                                                if (draftDao.isContainsDraft(draftObject.id)) {
                                                    draftObject.need_synchronize = 0;
                                                    draftDao.updateDraft(draftObject);
                                                } else {
                                                    draftDao.insertDraft(draftObject);
                                                }

                                                Logger.d("自动保存草稿成功: " + draftObject.toString());

                                                sectionWriteActivity.showToast("自动保存草稿成功！");
                                                sectionWriteActivity.setAutoUpdateDraft(draftObject);
                                            } else if (!TextUtils.isEmpty(draftCreateResult.getMessage())) {
                                                Draft localDraft = new Draft();
                                                localDraft.name = draftTitle;
                                                localDraft.content = draftContent;
                                                localDraft.word_count = draftContent.replace(" ", "").replace("\n", "").length();
                                                localDraft.book_id = literature.id;
                                                localDraft.update_time = draft.update_time;
                                                localDraft.need_synchronize = 2;

                                                if (draftDao == null) {
                                                    draftDao = DraftDao.getInstance(sectionWriteActivity.getQuApplicationContext());
                                                }

                                                localDraft.id = draft.id;
                                                if (draftDao.isContainsDraft(localDraft.id)) {
                                                    draftDao.updateDraft(localDraft);
                                                } else {
                                                    draftDao.insertDraft(localDraft);
                                                }

                                                sectionWriteActivity.showToast(draftCreateResult.getMessage());
                                                sectionWriteActivity.setAutoUpdateDraft(localDraft);
                                            }
                                        } else {
                                            Draft localDraft = new Draft();
                                            localDraft.name = draftTitle;
                                            localDraft.content = draftContent;
                                            localDraft.word_count = draftContent.replace(" ", "").replace("\n", "").length();
                                            localDraft.book_id = literature.id;
                                            localDraft.update_time = draft.update_time;
                                            localDraft.need_synchronize = 2;

                                            if (draftDao == null) {
                                                draftDao = DraftDao.getInstance(sectionWriteActivity.getQuApplicationContext());
                                            }

                                            localDraft.id = draft.id;
                                            if (draftDao.isContainsDraft(localDraft.id)) {
                                                draftDao.updateDraft(localDraft);
                                            } else {
                                                draftDao.insertDraft(localDraft);
                                            }

                                            sectionWriteActivity.showToast("自动保存草稿失败，请检查网络连接！");
                                            sectionWriteActivity.setAutoUpdateDraft(localDraft);
                                        }
                                    }

                                    @Override
                                    public void onError(Throwable throwable) {
                                        Logger.d("UpdateDraft onError: " + throwable.toString());

                                        Draft localDraft = new Draft();
                                        localDraft.name = draftTitle;
                                        localDraft.content = draftContent;
                                        localDraft.word_count = draftContent.replace(" ", "").replace("\n", "").length();
                                        localDraft.book_id = literature.id;
                                        localDraft.update_time = draft.update_time;
                                        localDraft.need_synchronize = 2;

                                        if (draftDao == null) {
                                            draftDao = DraftDao.getInstance(sectionWriteActivity.getQuApplicationContext());
                                        }

                                        localDraft.id = draft.id;
                                        if (draftDao.isContainsDraft(localDraft.id)) {
                                            draftDao.updateDraft(localDraft);
                                        } else {
                                            draftDao.insertDraft(localDraft);
                                        }

                                        sectionWriteActivity.showToast("自动保存草稿失败，请检查网络连接！");
                                        sectionWriteActivity.setAutoUpdateDraft(localDraft);
                                    }

                                    @Override
                                    public void onComplete() {
                                        Logger.d("UpdateDraft onComplete");
                                    }
                                });
                    }
                }
            }
        } else {

            if (!TextUtils.isEmpty(title) || !TextUtils.isEmpty(content)) {
                if (TextUtils.isEmpty(title)) {
                    if (content.length() >= 20) {
                        title = content.substring(0, 20);
                    } else {
                        title = content;
                    }
                }

                Draft localDraft = new Draft();
                localDraft.name = title;
                localDraft.content = content;
                localDraft.word_count = content.replace(" ", "").replace("\n", "").length();
                localDraft.book_id = literature.id;
                localDraft.update_time = draft.update_time;
                localDraft.need_synchronize = 2;

                if (draftDao == null) {
                    draftDao = DraftDao.getInstance(sectionWriteActivity.getQuApplicationContext());
                }

                if (TextUtils.isEmpty(draft.id)) {
                    if (!TextUtils.isEmpty(draft.local)) {
                        localDraft.local = draft.local;
                        if (draftDao.isContainsLocalDraft(localDraft.local)) {
                            draftDao.updateLocalDraft(localDraft);
                        } else {
                            draftDao.insertDraft(localDraft);
                        }
                    }
                } else {
                    localDraft.id = draft.id;
                    if (draftDao.isContainsDraft(localDraft.id)) {
                        draftDao.updateDraft(localDraft);
                    } else {
                        draftDao.insertDraft(localDraft);
                    }
                }

                sectionWriteActivity.showToast("自动保存草稿失败，请检查网络连接！");
                sectionWriteActivity.setAutoUpdateDraft(localDraft);
            }
        }
    }

    @Override
    public void setLoadingPage(LoadingPage loadingPage) {
        this.loadingPage = loadingPage;
    }
}