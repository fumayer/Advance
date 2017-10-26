package com.quduquxie.creation.modify.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;
import com.quduquxie.creation.modify.LiteratureSectionModifyInterface;
import com.quduquxie.db.SectionDao;
import com.quduquxie.model.creation.Section;
import com.quduquxie.model.creation.SectionPublish;
import com.quduquxie.model.v2.Rank;
import com.quduquxie.model.v2.SensitiveWord;
import com.quduquxie.retrofit.DataRequestService;
import com.quduquxie.retrofit.ServiceGenerator;
import com.quduquxie.retrofit.model.CommunalResult;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created on 16/11/18.
 * Created by crazylei.
 */

public class LiteratureSectionModifyPresenter implements LiteratureSectionModifyInterface.Presenter {

    private LiteratureSectionModifyInterface.View literatureWriteView;
    private WeakReference<Context> contextReference;

    private static final int title_limit = 20;

    private SectionDao sectionDao;

    public LiteratureSectionModifyPresenter(@NonNull LiteratureSectionModifyInterface.View literatureWriteView, Context context) {
        this.literatureWriteView = literatureWriteView;
        this.contextReference = new WeakReference<>(context);
    }

    @Override
    public void init() {

    }

    @Override
    public void initParameter(Section section) {
        if (section != null) {
            literatureWriteView.initView(title_limit, "第" + section.serial_number + "章");
        }
    }

    @Override
    public void initSection(Section section) {
        DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, true);
        dataRequestService.loadSection(section.id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .doOnNext(new Consumer<CommunalResult<SectionPublish>>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull CommunalResult<SectionPublish> sectionPublishResult) throws Exception {
                        Logger.d("LoadSection call: 数据库存储");
                        if (sectionPublishResult.getCode() == 0 && sectionPublishResult.getModel() != null && sectionPublishResult.getModel().chapter != null) {
                            Section sectionObject = sectionPublishResult.getModel().chapter;
                            if (sectionDao == null) {
                                sectionDao = new SectionDao(contextReference.get(), sectionObject.book_id);
                            }
                            if (sectionDao.isContainsSection(sectionObject.id)) {
                                sectionDao.updateSection(sectionObject);
                            } else {
                                sectionDao.insertSection(sectionObject);
                            }
                        }
                        Logger.d("LoadSection call: 数据库存储结束");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResourceSubscriber<CommunalResult<SectionPublish>>() {
                    @Override
                    public void onNext(CommunalResult<SectionPublish> sectionPublishResult) {
                        Logger.d("LoadSection onNext");
                        if (sectionPublishResult != null) {
                            if (sectionPublishResult.getCode() == 401) {
                                literatureWriteView.showToast("登录令牌失效，请重新登录！");
                                literatureWriteView.startLoginActivity();
                            } else {
                                if (sectionPublishResult.getCode() == 0 && sectionPublishResult.getModel() != null && sectionPublishResult.getModel().chapter != null) {
                                    Section sectionObject = sectionPublishResult.getModel().chapter;

                                    literatureWriteView.setChapterInformation(sectionObject.name, sectionObject.content);

                                } else if (!TextUtils.isEmpty(sectionPublishResult.getMessage())) {
                                    literatureWriteView.showToast(sectionPublishResult.getMessage());
                                }
                            }
                        } else {
                            literatureWriteView.showToast("获取章节内容失败");
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Logger.d("LoadSection onError: " + throwable.toString());
                        literatureWriteView.showToast("获取章节内容失败，请检查网络连接！");
                    }

                    @Override
                    public void onComplete() {
                        Logger.d("LoadSection onComplete");
                    }
                });
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
                                            literatureWriteView.highlightSensitiveWord(sensitiveWords);
                                            literatureWriteView.showToast("当前内容包含敏感词，请修改后发布！");
                                        } else if (sensitiveDanger >= 10) {
                                            literatureWriteView.highlightSensitiveWord(sensitiveWords);
                                            literatureWriteView.showToast("当前内容包含敏感词，请修改后发布！");
                                        } else if (sensitiveDanger > 0 && sensitiveDanger < 10) {
                                            literatureWriteView.highlightSensitiveWord(sensitiveWords);
                                            literatureWriteView.showPromptDialog("当前内容涉及风险较高，继续提交有可能被驳回，是否继续？");
                                        } else if (sensitiveOrdinary > 50) {
                                            literatureWriteView.highlightSensitiveWord(sensitiveWords);
                                            literatureWriteView.showPromptDialog("当前内容涉及风险较高，继续提交有可能被驳回，是否继续？");
                                        } else {
                                            literatureWriteView.publishChapter();
                                        }
                                    } else {
                                        literatureWriteView.publishChapter();
                                    }
                                } else if (!TextUtils.isEmpty(sensitiveWordResult.getMessage())) {
                                    literatureWriteView.showToast(sensitiveWordResult.getMessage());
                                    literatureWriteView.refreshPublishButtonState(true);
                                } else {
                                    literatureWriteView.showToast("敏感词检查失败！");
                                    literatureWriteView.refreshPublishButtonState(true);
                                }
                            } else {
                                literatureWriteView.publishChapter();
                            }
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            Logger.d("FilterSensitiveWord onError: " + throwable.toString());
                            literatureWriteView.showToast("敏感词检查失败！");
                            literatureWriteView.refreshPublishButtonState(true);
                        }

                        @Override
                        public void onComplete() {
                            Logger.d("FilterSensitiveWord onComplete");
                        }
                    });
        } else {
            literatureWriteView.publishChapter();
        }
    }

    @Override
    public void publishChapter(Section section, String title, String content) {
        if (!TextUtils.isEmpty(title) && title.length() < title_limit && !TextUtils.isEmpty(content) && !TextUtils.isEmpty(section.book_id)) {
            Map<String, String> parameter = new HashMap<>();
            parameter.put("id", section.id);
            parameter.put("name", title);
            parameter.put("content", content);
            parameter.put("book_id", section.book_id);
            parameter.put("update_time", String.valueOf(section.update_time));

            DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, true);
            dataRequestService.updateSection(parameter)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(Schedulers.io())
                    .doOnNext(new Consumer<CommunalResult<SectionPublish>>() {
                        @Override
                        public void accept(@io.reactivex.annotations.NonNull CommunalResult<SectionPublish> sectionPublishResult) throws Exception {
                            Logger.d("UpdateSection call: 数据库存储");
                            if (sectionPublishResult.getCode() == 0 && sectionPublishResult.getModel() != null && sectionPublishResult.getModel().chapter != null) {

                                Section sectionObject = sectionPublishResult.getModel().chapter;

                                if (sectionDao == null) {
                                    sectionDao = new SectionDao(contextReference.get(), sectionObject.book_id);
                                }

                                if (sectionDao.isContainsSection(sectionObject.id)) {
                                    sectionDao.updateSection(sectionObject);
                                } else {
                                    sectionDao.insertSection(sectionObject);
                                }
                            }
                            Logger.d("UpdateSection call: 数据库存储结束");
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new ResourceSubscriber<CommunalResult<SectionPublish>>() {
                        @Override
                        public void onNext(CommunalResult<SectionPublish> sectionPublishResult) {
                            Logger.d("UpdateSection onNext");
                            if (sectionPublishResult != null) {
                                if (sectionPublishResult.getCode() == 401) {
                                    literatureWriteView.showToast("登录令牌失效，请重新登录！");
                                    literatureWriteView.startLoginActivity();
                                } else {
                                    if (sectionPublishResult.getCode() == 0 && sectionPublishResult.getModel() != null && sectionPublishResult.getModel().chapter != null) {

                                        literatureWriteView.finishActivity();

                                    } else if (!TextUtils.isEmpty(sectionPublishResult.getMessage())) {
                                        literatureWriteView.showToast(sectionPublishResult.getMessage());
                                    }
                                }
                            } else {
                                literatureWriteView.showToast("章节发布失败");
                            }
                            literatureWriteView.refreshPublishButtonState(true);
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            Logger.d("UpdateSection onError: " + throwable.toString());
                            literatureWriteView.showToast("章节发布失败，请检查网络连接！");
                            literatureWriteView.refreshPublishButtonState(true);
                        }

                        @Override
                        public void onComplete() {
                            Logger.d("UpdateSection onComplete");
                        }
                    });
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            if (TextUtils.isEmpty(title) || title.length() > title_limit) {
                if (stringBuilder.length() != 0) {
                    stringBuilder.append("\n");
                }
                stringBuilder.append("请编写章节名称");
            }
            if (TextUtils.isEmpty(content)) {
                if (stringBuilder.length() != 0) {
                    stringBuilder.append("\n");
                }
                stringBuilder.append("请编写章节内容");
            }

            if (stringBuilder.length() > 0) {
                literatureWriteView.showToast(stringBuilder.toString());
            }
            literatureWriteView.refreshPublishButtonState(true);
        }
    }
}
