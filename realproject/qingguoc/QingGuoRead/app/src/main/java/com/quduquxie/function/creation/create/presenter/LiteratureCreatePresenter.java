package com.quduquxie.function.creation.create.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;
import com.quduquxie.db.LiteratureDao;
import com.quduquxie.function.creation.create.LiteratureCreateInterface;
import com.quduquxie.function.creation.create.util.LiteratureCreateUtil;
import com.quduquxie.function.creation.create.view.LiteratureCreateActivity;
import com.quduquxie.model.creation.Literature;
import com.quduquxie.model.creation.LiteratureRevise;
import com.quduquxie.model.creation.LiteratureTag;
import com.quduquxie.retrofit.DataRequestService;
import com.quduquxie.retrofit.ServiceGenerator;
import com.quduquxie.retrofit.model.CommunalResult;
import com.quduquxie.widget.LoadingPage;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created on 17/4/10.
 * Created by crazylei.
 */

public class LiteratureCreatePresenter implements LiteratureCreateInterface.Presenter {

    private LiteratureCreateActivity literatureCreateActivity;
    private Context context;

    private static final int name_limit = 20;
    private static final int desc_limit = 500;

    private Map<String, String> parameter = new HashMap<>();

    private String[] keys = {"name", "description", "category", "style", "ending", "fenpin"};

    private LiteratureDao literatureDao;

    private LoadingPage loadingPage;

    public LiteratureCreatePresenter(LiteratureCreateActivity literatureCreateActivity) {
        this.literatureCreateActivity = literatureCreateActivity;
        this.context = literatureCreateActivity.getQuApplicationContext();
    }

    @Override
    public void initParameter() {

    }

    @Override
    public void loadLiteratureTag() {
        literatureCreateActivity.showLoadingPage();

        DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, true);
        dataRequestService.loadLiteratureTag()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResourceSubscriber<CommunalResult<LiteratureTag>>() {
                    @Override
                    public void onNext(CommunalResult<LiteratureTag> literatureTagResult) {
                        Logger.d("LoadLiteratureTag onNext");
                        if (literatureTagResult != null) {
                            if (literatureTagResult.getCode() == 0 && literatureTagResult.getModel() != null) {
                                LiteratureTag literatureTag = literatureTagResult.getModel();
                                if (literatureTag.styles != null && literatureTag.styles.size() > 0) {
                                    LiteratureCreateUtil.setLiteratureStyle(context, literatureTag.styles);
                                }
                                if (literatureTag.endings != null && literatureTag.endings.size() > 0) {
                                    LiteratureCreateUtil.setLiteratureEnding(context, literatureTag.endings);
                                }
                                if (literatureTag.fenpins != null && literatureTag.fenpins.size() > 0) {
                                    LiteratureCreateUtil.setLiteratureChannel(context, literatureTag.fenpins);
                                }
                                if (literatureTag.attributes != null && literatureTag.attributes.size() > 0) {
                                    LiteratureCreateUtil.setLiteratureAttributes(context, literatureTag.attributes);
                                }
                                if (literatureTag.boy_categories != null && literatureTag.boy_categories.size() > 0) {
                                    LiteratureCreateUtil.setLiteratureCategoriesMan(context, literatureTag.boy_categories);
                                }
                                if (literatureTag.girl_categories != null && literatureTag.girl_categories.size() > 0) {
                                    LiteratureCreateUtil.setLiteratureCategoriesWoman(context, literatureTag.girl_categories);
                                }
                            } else if (!TextUtils.isEmpty(literatureTagResult.getMessage())) {
                                literatureCreateActivity.showToast(literatureTagResult.getMessage());
                            }
                        }

                        literatureCreateActivity.initView(name_limit, desc_limit);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Logger.d("LoadLiteratureTag throwable: " + throwable.toString());
                        literatureCreateActivity.hideLoadingPage();
                        literatureCreateActivity.initView(name_limit, desc_limit);
                    }

                    @Override
                    public void onComplete() {
                        Logger.d("LoadLiteratureTag onComplete");
                        literatureCreateActivity.hideLoadingPage();
                    }
                });
    }

    @Override
    public void checkLiteratureCompleteState(String key, String value) {
        parameter.put(key, value);
        if (checkInformation()) {
            literatureCreateActivity.checkLiteratureCreateState(true);
        } else {
            literatureCreateActivity.checkLiteratureCreateState(false);
        }
    }

    @Override
    public boolean verificationInformation() {
        StringBuilder stringBuilder = new StringBuilder();
        if (parameter.containsKey("name")) {
            String name = parameter.get("name");
            if (name.length() > 20) {
                if (stringBuilder.length() != 0) {
                    stringBuilder.append("\n");
                }
                stringBuilder.append("书籍名称最多20字");
            }
        } else {
            if (stringBuilder.length() != 0) {
                stringBuilder.append("\n");
            }
            stringBuilder.append("请输入书籍名称");
        }
        if (parameter.containsKey("description")) {
            String description = parameter.get("description");
            if (description.length() < 20 || description.length() > 500) {
                if (stringBuilder.length() != 0) {
                    stringBuilder.append("\n");
                }
                stringBuilder.append("书籍简介请保持在20~500字之间");
            }
        } else {
            if (stringBuilder.length() != 0) {
                stringBuilder.append("\n");
            }
            stringBuilder.append("请编写简介");
        }

        if (parameter.containsKey("fenpin")) {
            String fenpin = parameter.get("fenpin");
            if (TextUtils.isEmpty(fenpin)) {
                if (stringBuilder.length() != 0) {
                    stringBuilder.append("\n");
                }
                stringBuilder.append("请选择作品类型");
            }
        } else {
            if (stringBuilder.length() != 0) {
                stringBuilder.append("\n");
            }
            stringBuilder.append("请选择作品类型");
        }

        if (parameter.containsKey("category")) {
            String category = parameter.get("category");
            if (TextUtils.isEmpty(category)) {
                if (stringBuilder.length() != 0) {
                    stringBuilder.append("\n");
                }
                stringBuilder.append("请选择作品分类");
            }
        } else {
            if (stringBuilder.length() != 0) {
                stringBuilder.append("\n");
            }
            stringBuilder.append("请选择作品分类");
        }
        if (parameter.containsKey("style")) {
            String style = parameter.get("style");
            if (TextUtils.isEmpty(style)) {
                if (stringBuilder.length() != 0) {
                    stringBuilder.append("\n");
                }
                stringBuilder.append("请选择作品风格");
            }
        } else {
            if (stringBuilder.length() != 0) {
                stringBuilder.append("\n");
            }
            stringBuilder.append("请选择作品风格");
        }
        if (parameter.containsKey("ending")) {
            String ending = parameter.get("ending");
            if (TextUtils.isEmpty(ending)) {
                if (stringBuilder.length() != 0) {
                    stringBuilder.append("\n");
                }
                stringBuilder.append("请选择结局类型");
            }
        } else {
            if (stringBuilder.length() != 0) {
                stringBuilder.append("\n");
            }
            stringBuilder.append("请选择结局类型");
        }

        if (stringBuilder.length() > 0) {
            literatureCreateActivity.showToast(stringBuilder.toString());
            return false;
        }
        return true;
    }

    @Override
    public void createLiterature(File file) {

        RequestBody requestBody = null;
        MultipartBody.Part requestFile = null;
        if (file != null) {
            requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            requestFile = MultipartBody.Part.createFormData("image", file.getName(), requestBody);
        }

        Map<String, RequestBody> parameterMap = new HashMap<>();

        if (parameter != null && !parameter.isEmpty()) {
            for (String key : parameter.keySet()) {
                parameterMap.put(key, RequestBody.create(MediaType.parse("multipart/form-data-data"), parameter.get(key)));
            }
        }

        DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, true);
        dataRequestService.createLiterature(parameterMap, requestFile)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .doOnNext(new Consumer<CommunalResult<LiteratureRevise>>() {
                    @Override
                    public void accept(@NonNull CommunalResult<LiteratureRevise> literatureReviseResult) throws Exception {
                        Logger.d("CreateLiterature call: 数据库操作");
                        if (literatureReviseResult != null && literatureReviseResult.getCode() == 0 && literatureReviseResult.getModel() != null && !TextUtils.isEmpty(literatureReviseResult.getModel().id)) {
                            Literature literature = new Literature();
                            literature.id = literatureReviseResult.getModel().id;
                            literature.name = parameter.get("name");
                            literature.description = parameter.get("description");
                            literature.attribute = "serialize";
                            literature.category = parameter.get("category");
                            literature.style = parameter.get("style");
                            literature.ending = parameter.get("ending");
                            literature.fenpin = parameter.get("fenpin");

                            if (literatureDao == null) {
                                literatureDao = LiteratureDao.getInstance(literatureCreateActivity.getQuApplicationContext());
                            }

                            if (literatureDao.isContainsLiterature(literature.id)) {
                                literatureDao.updateLiterature(literature);
                            } else {
                                literatureDao.insertLiterature(literature);
                            }
                        }
                        Logger.d("CreateLiterature call: 数据库操作完成");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResourceSubscriber<CommunalResult<LiteratureRevise>>() {
                    @Override
                    public void onNext(CommunalResult<LiteratureRevise> literatureReviseResult) {
                        Logger.d("CreateLiterature onNext");

                        if (literatureReviseResult != null) {
                            if (literatureReviseResult.getCode() == 401) {
                                literatureCreateActivity.showToast("登录令牌失效，请重新登录");
                                literatureCreateActivity.startLoginActivity();
                            } else {
                                if (literatureReviseResult.getCode() == 0 && literatureReviseResult.getModel() != null && !TextUtils.isEmpty(literatureReviseResult.getModel().id)) {
                                    literatureCreateActivity.finishActivity();
                                } else if (!TextUtils.isEmpty(literatureReviseResult.getMessage())) {
                                    literatureCreateActivity.showToast(literatureReviseResult.getMessage());
                                }
                            }
                        } else {
                            literatureCreateActivity.showToast("请检查网络");
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Logger.d("CreateLiterature onError: " + throwable.toString());
                        literatureCreateActivity.showToast("请检查网络");
                    }

                    @Override
                    public void onComplete() {
                        Logger.d("CreateLiterature onComplete");
                    }
                });
    }

    @Override
    public void setLoadingPage(LoadingPage loadingPage) {
        this.loadingPage = loadingPage;
    }

    private boolean checkInformation() {
        for (int i = 0; i < keys.length; i++) {
            if (!parameter.containsKey(keys[i]) || TextUtils.isEmpty(parameter.get(keys[i]))) {
                return false;
            }
        }
        return true;
    }
}
