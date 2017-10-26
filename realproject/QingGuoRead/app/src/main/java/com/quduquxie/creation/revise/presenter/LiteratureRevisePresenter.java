package com.quduquxie.creation.revise.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;
import com.quduquxie.creation.revise.LiteratureReviseInterface;
import com.quduquxie.db.LiteratureDao;
import com.quduquxie.model.creation.Literature;
import com.quduquxie.model.creation.LiteratureRevise;
import com.quduquxie.retrofit.DataRequestService;
import com.quduquxie.retrofit.ServiceGenerator;
import com.quduquxie.retrofit.model.CommunalResult;
import com.quduquxie.util.QGLog;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created on 16/11/22.
 * Created by crazylei.
 */

public class LiteratureRevisePresenter implements LiteratureReviseInterface.Presenter {

    private static final String TAG = LiteratureRevisePresenter.class.getSimpleName();

    private LiteratureReviseInterface.View literatureReviseView;
    private WeakReference<Context> contextReference;

    private static final int desc_limit = 500;

    private Map<String, String> parameter = new HashMap<>();

    private String[] keys = {"id", "name", "description", "category", "style", "ending", "fenpin", "attribute"};

    private LiteratureDao literatureDao;

    public LiteratureRevisePresenter(@NonNull LiteratureReviseInterface.View literatureReviseView, Context context) {
        this.literatureReviseView = literatureReviseView;
        this.contextReference = new WeakReference<>(context);
        this.literatureDao = LiteratureDao.getInstance(contextReference.get());
    }

    @Override
    public void init() {

    }

    @Override
    public void initParameter(Literature literature) {
        if (literature != null) {
            initLiteratureParameter(literature);
            literatureReviseView.initView(desc_limit);
        } else {
            literatureReviseView.showErrorView();
        }
    }

    @Override
    public void checkLiteratureCompleteState(String key, String value) {
        parameter.put(key, value);
        if (checkInformation()) {
            QGLog.e(TAG, "CheckInformation: true");
            literatureReviseView.checkLiteratureCreateState(true);
        } else {
            QGLog.e(TAG, "CheckInformation: false");
            literatureReviseView.checkLiteratureCreateState(false);
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
        if (parameter.containsKey("attribute")) {
            String attribute = parameter.get("attribute");
            if (TextUtils.isEmpty(attribute)) {
                if (stringBuilder.length() != 0) {
                    stringBuilder.append("\n");
                }
                stringBuilder.append("请选择作品状态");
            }
        } else {
            if (stringBuilder.length() != 0) {
                stringBuilder.append("\n");
            }
            stringBuilder.append("请选择作品状态");
        }

        if (stringBuilder.length() > 0) {
            literatureReviseView.showToast(stringBuilder.toString());
            return false;
        }
        return true;
    }

    @Override
    public void reviseLiterature(File file) {

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
        dataRequestService.updateLiterature(parameterMap, requestFile)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .doOnNext(new Consumer<CommunalResult<LiteratureRevise>>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull CommunalResult<LiteratureRevise> literatureReviseResult) throws Exception {
                        Logger.d("UpdateLiterature call: 数据库存储");
                        if (literatureReviseResult != null && literatureReviseResult.getCode() == 0 && literatureReviseResult.getModel() != null && !TextUtils.isEmpty(literatureReviseResult.getModel().id)) {
                            Literature literature = new Literature();
                            literature.id = literatureReviseResult.getModel().id;
                            literature.name = parameter.get("name");
                            literature.description = parameter.get("description");
                            literature.attribute = parameter.get("attribute");
                            literature.category = parameter.get("category");
                            literature.style = parameter.get("style");
                            literature.ending = parameter.get("ending");
                            literature.fenpin = parameter.get("fenpin");

                            if (literatureDao == null) {
                                literatureDao = LiteratureDao.getInstance(contextReference.get());
                            }

                            if (literatureDao.isContainsLiterature(literature.id)) {
                                literatureDao.updateLiterature(literature);
                            } else {
                                literatureDao.insertLiterature(literature);
                            }
                        }
                        Logger.d("UpdateLiterature call: 数据库存储结束");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResourceSubscriber<CommunalResult<LiteratureRevise>>() {
                    @Override
                    public void onNext(CommunalResult<LiteratureRevise> literatureReviseResult) {
                        Logger.d("UpdateLiterature onNext");
                        if (literatureReviseResult != null) {
                            if (literatureReviseResult.getCode() == 401) {
                                literatureReviseView.showToast("登录令牌失效，请重新登录！");
                                literatureReviseView.startLoginActivity();
                            } else {
                                if (literatureReviseResult.getCode() == 0 && literatureReviseResult.getModel() != null) {
                                    literatureReviseView.finishActivity();
                                } else if (!TextUtils.isEmpty(literatureReviseResult.getMessage())) {
                                    literatureReviseView.showToast(literatureReviseResult.getMessage());
                                }
                            }
                        } else {
                            literatureReviseView.showToast("请检查网络");
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Logger.d("UpdateLiterature onError: " + throwable.toString());
                        literatureReviseView.showToast("请检查网络");
                    }

                    @Override
                    public void onComplete() {
                        Logger.d("UpdateLiterature onComplete");
                    }
                });
    }

    private void initLiteratureParameter(Literature literature) {
        if (!TextUtils.isEmpty(literature.id)) {
            parameter.put("id", literature.id);
        }
        if (!TextUtils.isEmpty(literature.name)) {
            parameter.put("name", literature.name);
        }
        if (!TextUtils.isEmpty(literature.description)) {
            parameter.put("description", literature.description);
        }
        if (!TextUtils.isEmpty(literature.category)) {
            parameter.put("category", literature.category);
        }
        if (!TextUtils.isEmpty(literature.style)) {
            parameter.put("style", literature.style);
        }
        if (!TextUtils.isEmpty(literature.ending)) {
            parameter.put("ending", literature.ending);
        }
        if (!TextUtils.isEmpty(literature.fenpin)) {
            parameter.put("fenpin", literature.fenpin);
        }
        if (!TextUtils.isEmpty(literature.attribute)) {
            parameter.put("attribute", literature.attribute);
        }

        literatureReviseView.checkLiteratureCreateState(true);
    }

    private boolean checkInformation() {
        for (int i  = 0; i < keys.length; i++) {
            if (!parameter.containsKey(keys[i]) || TextUtils.isEmpty(parameter.get(keys[i]))) {
                return false;
            }
        }
        return true;
    }
}
