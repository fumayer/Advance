package com.quduquxie.wxapi.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.quduquxie.application.QuApplication;
import com.quduquxie.communal.utils.BookUtil;
import com.quduquxie.communal.utils.DeviceUtil;
import com.quduquxie.model.CompleteUser;
import com.quduquxie.model.RecoveredPassword;
import com.quduquxie.model.UnionID;
import com.quduquxie.model.User;
import com.quduquxie.communal.utils.RSAUtil;
import com.quduquxie.communal.utils.UserUtil;
import com.quduquxie.db.UserDao;
import com.quduquxie.model.RegisterUser;
import com.quduquxie.retrofit.DataRequestService;
import com.quduquxie.retrofit.ServiceGenerator;
import com.quduquxie.retrofit.model.CommunalResult;
import com.quduquxie.wxapi.WXEntryInterface;
import com.xiaomi.mipush.sdk.MiPushClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created on 16/12/6.
 * Created by crazylei.
 */

public class WXEntryPresenter implements WXEntryInterface.Presenter {

    private UserDao userDao;
    private WXEntryInterface.View wxEntryView;
    private WeakReference<Context> contextReference;

    private Map<String, String> user_information_QQ = new HashMap<>();
    private Map<String, Object> user_information_Own = new HashMap<>();

    public WXEntryPresenter(@NonNull WXEntryInterface.View wxEntryView, Context context) {
        this.wxEntryView = wxEntryView;
        this.contextReference = new WeakReference<>(context);
        this.userDao = UserDao.getInstance(contextReference.get());
    }

    @Override
    public void init() {
        user_information_QQ.clear();
        if (wxEntryView != null) {
            wxEntryView.initView();
            wxEntryView.refreshViewTitle("登录");
            wxEntryView.showLoginFragment();
        }
    }

    @Override
    public void initQQAccessToken(JSONObject jsonObject) throws JSONException {

        user_information_QQ.clear();

        final String access_token = jsonObject.getString("access_token");
        final String oauth_id = jsonObject.getString("openid");
        final String expire_sec = jsonObject.getString("expires_in");

        user_information_QQ.put("access_token", access_token);
        user_information_QQ.put("expire_sec", expire_sec);

        DataRequestService dataRequestService = ServiceGenerator.getSpecialGenerator(DataRequestService.class);
        dataRequestService.loadQQUnionID(access_token)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResourceSubscriber<String>() {
                    @Override
                    public void onNext(String result) {
                        Logger.d("LoadQQUnionID: onNext: " + result);

                        result = result.substring(result.indexOf('{'), result.indexOf('}') + 1);
                        UnionID unionID = new UnionID();
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            if (!jsonObject.isNull("client_id")) {
                                unionID.client_id = jsonObject.getString("client_id");
                                if (!jsonObject.isNull("openid")) {
                                    unionID.openid = jsonObject.getString("openid");
                                }
                                if (!jsonObject.isNull("unionid")) {
                                    unionID.union_id = jsonObject.getString("unionid");
                                }
                            }
                        } catch (JSONException exception) {
                            exception.printStackTrace();
                        }

                        if (!TextUtils.isEmpty(unionID.union_id)) {
                            user_information_QQ.put("oauth_id", unionID.union_id);
                        }

                        if (user_information_QQ.containsKey("oauth_id") && !TextUtils.isEmpty(user_information_QQ.get("oauth_id"))) {
                            if (wxEntryView != null) {
                                wxEntryView.setQQAccessToken(access_token, oauth_id, expire_sec);
                                wxEntryView.updateUserInformationQQ();
                            }
                        } else {
                            wxEntryView.showToast("授权过程中出现异常，请重新登陆！");
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Logger.d("LoadQQUnionID: onError: " + throwable.toString());
                        wxEntryView.showToast("授权过程中出现异常，请重新登陆！");
                    }

                    @Override
                    public void onComplete() {
                        Logger.d("LoadQQUnionID: onComplete");
                    }
                });
    }

    @Override
    public void updateUserInformationQQ(JSONObject jsonObject) throws JSONException {

        String province = jsonObject.getString("province");
        String city = jsonObject.getString("city");
        if (!TextUtils.isEmpty(province + city)) {
            user_information_QQ.put("address", province + city);
        }
        String penname = jsonObject.getString("nickname");
        if (!TextUtils.isEmpty(penname)) {
            user_information_QQ.put("penname", penname);
        }
        String avatar_url = jsonObject.getString("figureurl_qq_2");
        if (!TextUtils.isEmpty(avatar_url)) {
            user_information_QQ.put("avatar_url", avatar_url);
        }
        String gender = jsonObject.getString("gender");
        if (!TextUtils.isEmpty(gender)) {
            user_information_QQ.put("gender", gender);
        }

        registerUserInformationQQ();
    }

    @Override
    public void registerUserInformationWeChat(String code) {
        DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, false);
        dataRequestService.landingUserWeChat(code)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .doOnNext(new Consumer<CommunalResult<RegisterUser>>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull CommunalResult<RegisterUser> registerUserResult) throws Exception {
                        if (registerUserResult != null && registerUserResult.getCode() == 0 && registerUserResult.getModel() != null && registerUserResult.getModel().user != null) {
                            Logger.d("LandingUserWeChat call: 数据库操作");

                            User user = registerUserResult.getModel().user;
                            user.token = registerUserResult.getModel().token;
                            user.is_new = registerUserResult.getModel().is_new;
                            user.platform = "WeChat";
                            user.is_uploaded = registerUserResult.getModel().is_uploaded;
                            user.register_time = System.currentTimeMillis();

                            if (userDao == null) {
                                userDao = UserDao.getInstance(contextReference.get());
                            }

                            User localUser = userDao.getUser();
                            if (localUser != null && !localUser.id.equals(user.id)) {
                                UserUtil.deleteUser(contextReference.get());
                                UserUtil.deleteLiterature(contextReference.get());
                            }

                            if (userDao.isContainsUser(UserDao.getToken(contextReference.get()))) {
                                userDao.updateUser(user, UserDao.getToken(contextReference.get()));
                            } else {
                                userDao.insertUser(user);
                            }

                            if (!TextUtils.isEmpty(registerUserResult.getModel().token)) {
                                UserDao.setToken(registerUserResult.getModel().token);
                            }

                            Logger.d("LandingUserWeChat call: 数据库操作完成");
                        }
                    }
                })
                .observeOn(Schedulers.newThread())
                .doOnNext(new Consumer<CommunalResult<RegisterUser>>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull CommunalResult<RegisterUser> registerUserResult) throws Exception {
                        if (registerUserResult != null && registerUserResult.getCode() == 0 && registerUserResult.getModel() != null && !TextUtils.isEmpty(registerUserResult.getModel().token)) {
                            Logger.d("LandingUserWeChat call: 绑定推送设备");
                            DeviceUtil.pushBindingDevice(MiPushClient.getRegId(contextReference.get()), registerUserResult.getModel().token);
                        }
                    }
                })
                .observeOn(Schedulers.newThread())
                .doOnNext(new Consumer<CommunalResult<RegisterUser>>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull CommunalResult<RegisterUser> registerUserResult) throws Exception {
                        if (registerUserResult != null && registerUserResult.getCode() == 0 && registerUserResult.getModel() != null) {
                            if (registerUserResult.getModel().is_uploaded == 0) {
                                Logger.d("LandingUserWeChat call: 上传用户书架信息");
                                try {
                                    BookUtil.uploadUserBookShelf(contextReference.get());
                                } catch (JSONException exception) {
                                    exception.printStackTrace();
                                }
                            } else if (registerUserResult.getModel().is_uploaded == 1) {
                                Logger.d("LandingUserWeChat call: 下载用户书架信息");
                                BookUtil.downloadUserBookShelf(contextReference.get());
                            }
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResourceSubscriber<CommunalResult<RegisterUser>>() {
                    @Override
                    public void onNext(CommunalResult<RegisterUser> registerUserResult) {
                        if (registerUserResult != null) {
                            if (registerUserResult.getCode() == 0 && registerUserResult.getModel() != null && registerUserResult.getModel().user != null) {
                                Logger.d("LandingUserWeChat onNext: " + registerUserResult.getModel().toString());
                                if (registerUserResult.getModel().is_new == 1) {
                                    wxEntryView.showCompleteInformationFragment();
                                } else {
                                    wxEntryView.finishActivityLiteratureCheck();
                                }
                            } else if (!TextUtils.isEmpty(registerUserResult.getMessage())) {
                                wxEntryView.showToast(registerUserResult.getMessage());
                            }
                        } else {
                            wxEntryView.showToast("请检查网络");
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Logger.d("LandingUserWeChat onError: " + throwable.toString());
                        wxEntryView.showToast("请检查网络");
                    }

                    @Override
                    public void onComplete() {
                        Logger.d("LandingUserWeChat onComplete");
                    }
                });
    }

    @Override
    public void registerUserInformation(final String telephone_number, String verification_code, String password) {
        wxEntryView.showProgressDialog();

        user_information_Own.clear();
        user_information_Own.put("user_name", telephone_number);
        user_information_Own.put("password", password);
        user_information_Own.put("sms", verification_code);
        user_information_Own.put("n", (int) (Math.random() * 100));

        Gson gson = new Gson();
        String information = gson.toJson(user_information_Own);

        try {
            information = RSAUtil.encryptionPublicKey(information);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, false);
        dataRequestService.registerUser(information)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .doOnNext(new Consumer<CommunalResult<RegisterUser>>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull CommunalResult<RegisterUser> registerUserResult) throws Exception {
                        if (registerUserResult != null && registerUserResult.getCode() == 0 && registerUserResult.getModel() != null && registerUserResult.getModel().user != null) {
                            Logger.d("RegisterUser call: 数据库操作");

                            User user = registerUserResult.getModel().user;
                            user.token = registerUserResult.getModel().token;
                            user.is_new = registerUserResult.getModel().is_new;
                            user.platform = "Qingoo";
                            user.register_time = System.currentTimeMillis();

                            if (userDao == null) {
                                userDao = UserDao.getInstance(contextReference.get());
                            }

                            User localUser = userDao.getUser();
                            if (localUser != null) {
                                if (!localUser.id.equals(user.id)) {
                                    UserUtil.deleteUser(contextReference.get());
                                    UserUtil.deleteLiterature(contextReference.get());
                                }
                            } else {
                                UserUtil.deleteUser(contextReference.get());
                                UserUtil.deleteLiterature(contextReference.get());
                            }

                            if (userDao.isContainsUser(UserDao.getToken(contextReference.get()))) {
                                userDao.updateUser(user, UserDao.getToken(contextReference.get()));
                            } else {
                                userDao.insertUser(user);
                            }

                            if (!TextUtils.isEmpty(registerUserResult.getModel().token)) {
                                UserDao.setToken(registerUserResult.getModel().token);
                            }

                            Logger.d("RegisterUser call: 数据库操作完成");
                        }
                    }
                })
                .observeOn(Schedulers.newThread())
                .doOnNext(new Consumer<CommunalResult<RegisterUser>>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull CommunalResult<RegisterUser> registerUserResult) throws Exception {
                        if (registerUserResult != null && registerUserResult.getCode() == 0 && registerUserResult.getModel() != null && !TextUtils.isEmpty(registerUserResult.getModel().token)) {
                            Logger.d("RegisterUser call: 绑定推送设备");
                            DeviceUtil.pushBindingDevice(MiPushClient.getRegId(contextReference.get()), registerUserResult.getModel().token);
                        }
                    }
                })
                .observeOn(Schedulers.newThread())
                .doOnNext(new Consumer<CommunalResult<RegisterUser>>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull CommunalResult<RegisterUser> registerUserCommunalResult) throws Exception {
                        Logger.d("RegisterUser call: 上传用户书架数据");
                        try {
                            BookUtil.uploadUserBookShelf(contextReference.get());
                        } catch (JSONException exception) {
                            exception.printStackTrace();
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResourceSubscriber<CommunalResult<RegisterUser>>() {

                    @Override
                    public void onNext(CommunalResult<RegisterUser> registerUserResult) {
                        wxEntryView.hideProgressDialog();
                        if (registerUserResult != null) {
                            if (registerUserResult.getCode() == 0 && registerUserResult.getModel() != null && registerUserResult.getModel().user != null) {
                                Logger.d("RegisterUser onNext: " + registerUserResult.getModel().toString());
                                wxEntryView.showCompleteInformationFragment();
                            } else if (!TextUtils.isEmpty(registerUserResult.getMessage())) {
                                wxEntryView.showToast(registerUserResult.getMessage());
                            }
                        } else {
                            wxEntryView.showToast("请检查网络");
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Logger.d("RegisterUser onError: " + throwable.toString());
                        wxEntryView.hideProgressDialog();
                        wxEntryView.showToast("请检查网络");
                    }

                    @Override
                    public void onComplete() {
                        Logger.d("RegisterUser onComplete");
                    }
                });
    }

    @Override
    public void landingUserInformation(String telephone_number, String password) {
        wxEntryView.showProgressDialog();

        Map<String, Object> parameter = new HashMap<>();

        parameter.put("user_name", telephone_number);
        parameter.put("password", password);
        parameter.put("n", (int) (Math.random() * 100));

        Gson gson = new Gson();
        String information = gson.toJson(parameter);

        try {
            information = RSAUtil.encryptionPublicKey(information);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, false);
        dataRequestService.landingUser(information)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .doOnNext(new Consumer<CommunalResult<RegisterUser>>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull CommunalResult<RegisterUser> registerUserResult) throws Exception {
                        if (registerUserResult != null && registerUserResult.getCode() == 0 && registerUserResult.getModel() != null && registerUserResult.getModel().user != null) {
                            Logger.d("LandingUser call: 数据库操作");

                            User user = registerUserResult.getModel().user;
                            user.token = registerUserResult.getModel().token;
                            user.is_new = registerUserResult.getModel().is_new;
                            user.platform = "Qingoo";
                            user.is_uploaded = registerUserResult.getModel().is_uploaded;
                            user.register_time = System.currentTimeMillis();

                            if (userDao == null) {
                                userDao = UserDao.getInstance(contextReference.get());
                            }

                            User localUser = userDao.getUser();
                            if (localUser != null) {
                                if (!localUser.id.equals(user.id)) {
                                    UserUtil.deleteUser(contextReference.get());
                                    UserUtil.deleteLiterature(contextReference.get());
                                }
                            } else {
                                UserUtil.deleteUser(contextReference.get());
                                UserUtil.deleteLiterature(contextReference.get());
                            }

                            if (userDao.isContainsUser(UserDao.getToken(contextReference.get()))) {
                                userDao.updateUser(user, UserDao.getToken(contextReference.get()));
                            } else {
                                userDao.insertUser(user);
                            }

                            if (!TextUtils.isEmpty(registerUserResult.getModel().token)) {
                                UserDao.setToken(registerUserResult.getModel().token);
                            }

                            Logger.d("LandingUser call: 数据库操作完成");
                        }
                    }
                })
                .observeOn(Schedulers.newThread())
                .doOnNext(new Consumer<CommunalResult<RegisterUser>>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull CommunalResult<RegisterUser> registerUserResult) throws Exception {
                        if (registerUserResult != null && registerUserResult.getCode() == 0 && registerUserResult.getModel() != null && !TextUtils.isEmpty(registerUserResult.getModel().token)) {
                            Logger.d("LandingUser call: 绑定推送设备");
                            DeviceUtil.pushBindingDevice(MiPushClient.getRegId(contextReference.get()), registerUserResult.getModel().token);
                        }
                    }
                })
                .observeOn(Schedulers.newThread())
                .doOnNext(new Consumer<CommunalResult<RegisterUser>>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull CommunalResult<RegisterUser> registerUserResult) throws Exception {
                        if (registerUserResult != null && registerUserResult.getCode() == 0 && registerUserResult.getModel() != null) {
                            if (registerUserResult.getModel().is_uploaded == 0) {
                                Logger.d("LandingUser call: 上传用户书架数据");
                                try {
                                    BookUtil.uploadUserBookShelf(contextReference.get());
                                } catch (JSONException exception) {
                                    exception.printStackTrace();
                                }
                            } else if (registerUserResult.getModel().is_uploaded == 1) {
                                Logger.d("LandingUser call: 下载用户书架数据");
                                BookUtil.downloadUserBookShelf(contextReference.get());
                            }
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResourceSubscriber<CommunalResult<RegisterUser>>() {
                    @Override
                    public void onNext(CommunalResult<RegisterUser> registerUserResult) {
                        wxEntryView.hideProgressDialog();
                        if (registerUserResult != null) {
                            if (registerUserResult.getCode() == 0 && registerUserResult.getModel() != null && registerUserResult.getModel().user != null) {
                                Logger.d("LandingUser onNext: " + registerUserResult.getModel().user.toString());
                                wxEntryView.finishActivityLiteratureCheck();
                            } else if (!TextUtils.isEmpty(registerUserResult.getMessage())) {
                                wxEntryView.showToast(registerUserResult.getMessage());
                            }
                        } else {
                            wxEntryView.showToast("请检查网络");
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Logger.d("LandingUser onError: " + throwable.toString());
                        wxEntryView.hideProgressDialog();
                        wxEntryView.showToast("请检查网络");
                    }

                    @Override
                    public void onComplete() {
                        Logger.d("LandingUser onComplete");
                    }
                });
    }

    @Override
    public void recoveredUserPassword(String telephone_number, String verification_code, String password) {
        wxEntryView.showProgressDialog();

        Map<String, Object> parameter = new HashMap<>();

        parameter.put("user_name", telephone_number);
        parameter.put("password", password);
        parameter.put("sms", verification_code);
        parameter.put("n", (int) (Math.random() * 100));

        Gson gson = new Gson();
        String information = gson.toJson(parameter);

        try {
            information = RSAUtil.encryptionPublicKey(information);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, false);
        dataRequestService.recoveredPassword(information)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnNext(new Consumer<CommunalResult<RecoveredPassword>>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull CommunalResult<RecoveredPassword> recoveredPasswordResult) throws Exception {
                        if (recoveredPasswordResult != null && recoveredPasswordResult.getCode() == 0 && recoveredPasswordResult.getModel() != null && recoveredPasswordResult.getModel().user != null) {
                            Logger.d("RecoveredPassword call: 数据库操作");

                            User user = new User();
                            user.platform = "Qingoo";
                            user.token = recoveredPasswordResult.getModel().token;
                            user.is_new = recoveredPasswordResult.getModel().is_new;
                            user.is_uploaded = recoveredPasswordResult.getModel().is_uploaded;
                            user.register_time = System.currentTimeMillis();

                            if (userDao == null) {
                                userDao = UserDao.getInstance(contextReference.get());
                            }

                            User localUser = userDao.getUser();
                            if (localUser != null) {
                                if (!localUser.id.equals(user.id)) {
                                    UserUtil.deleteUser(contextReference.get());
                                    UserUtil.deleteLiterature(contextReference.get());
                                }
                            } else {
                                UserUtil.deleteUser(contextReference.get());
                                UserUtil.deleteLiterature(contextReference.get());
                            }

                            if (userDao.isContainsUser(UserDao.getToken(contextReference.get()))) {
                                userDao.updateUser(user, UserDao.getToken(contextReference.get()));
                            } else {
                                userDao.insertUser(user);
                            }

                            if (!TextUtils.isEmpty(recoveredPasswordResult.getModel().token)) {
                                UserDao.setToken(recoveredPasswordResult.getModel().token);
                            }

                            Logger.d("RecoveredPassword call: 数据库操作完成");
                        }
                    }
                })
                .observeOn(Schedulers.newThread())
                .doOnNext(new Consumer<CommunalResult<RecoveredPassword>>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull CommunalResult<RecoveredPassword> recoveredPasswordResult) throws Exception {
                        if (recoveredPasswordResult != null && recoveredPasswordResult.getCode() == 0 && recoveredPasswordResult.getModel() != null && recoveredPasswordResult.getModel().user != null) {
                            if (recoveredPasswordResult.getModel().user.is_new == 1) {
                                Logger.d("RecoveredPassword call: 绑定推送设备");
                                DeviceUtil.pushBindingDevice(MiPushClient.getRegId(contextReference.get()), recoveredPasswordResult.getModel().token);
                            }
                        }
                    }
                })
                .observeOn(Schedulers.newThread())
                .doOnNext(new Consumer<CommunalResult<RecoveredPassword>>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull CommunalResult<RecoveredPassword> recoveredPasswordResult) throws Exception {
                        if (recoveredPasswordResult != null && recoveredPasswordResult.getCode() == 0 && recoveredPasswordResult.getModel() != null && recoveredPasswordResult.getModel().user != null) {
                            if (recoveredPasswordResult.getModel().user.is_uploaded == 0) {
                                Logger.d("RecoveredPassword call: 上传用户书架信息");
                                try {
                                    BookUtil.uploadUserBookShelf(contextReference.get());
                                } catch (JSONException exception) {
                                    exception.printStackTrace();
                                }
                            } else if (recoveredPasswordResult.getModel().user.is_uploaded == 1) {
                                Logger.d("RecoveredPassword call: 下载用户书架信息");
                                BookUtil.downloadUserBookShelf(contextReference.get());
                            }
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResourceSubscriber<CommunalResult<RecoveredPassword>>() {
                    @Override
                    public void onNext(CommunalResult<RecoveredPassword> recoveredPasswordResult) {
                        wxEntryView.hideProgressDialog();
                        if (recoveredPasswordResult != null) {
                            if (recoveredPasswordResult.getCode() == 0 && recoveredPasswordResult.getModel() != null && recoveredPasswordResult.getModel().user != null) {
                                Logger.d("RecoveredPassword onNext: " + recoveredPasswordResult.getCode());
                                wxEntryView.finishActivityLiteratureCheck();

                            } else if (!TextUtils.isEmpty(recoveredPasswordResult.getMessage())) {
                                wxEntryView.showToast(recoveredPasswordResult.getMessage());
                            }
                        } else {
                            wxEntryView.showToast("请检查网络");
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Logger.d("RecoveredPassword onError: " + throwable.toString());
                        wxEntryView.hideProgressDialog();
                        wxEntryView.showToast("请检查网络");
                    }

                    @Override
                    public void onComplete() {
                        Logger.d("RecoveredPassword onComplete");
                    }
                });
    }

    @Override
    public void completeUserInformation(String nickname, File avatar) {
        wxEntryView.showProgressDialog();

        MultipartBody.Part requestFile = null;
        if (avatar != null) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), avatar);
            requestFile = MultipartBody.Part.createFormData("avatar", avatar.getName(), requestBody);
        }

        MultipartBody.Part penname = MultipartBody.Part.create(Headers.of("Content-Disposition", "form-data; name=\"penname\""), RequestBody.create(null, nickname));

        DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, true);
        dataRequestService.completeUser(penname, requestFile)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .doOnNext(new Consumer<CommunalResult<CompleteUser>>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull CommunalResult<CompleteUser> completeUserResult) throws Exception {
                        if (completeUserResult != null && completeUserResult.getCode() == 0 && completeUserResult.getModel() != null) {
                            Logger.d("CompleteUser call: 数据库存储");

                            User user = new User();
                            user.penname = completeUserResult.getModel().penname;
                            user.avatar_url = completeUserResult.getModel().avatar_url;

                            if (userDao == null) {
                                userDao = UserDao.getInstance(contextReference.get());
                            }

                            if (userDao.isContainsUser(UserDao.getToken(contextReference.get()))) {
                                userDao.updateUser(user, UserDao.getToken(contextReference.get()));
                            } else {
                                userDao.insertUser(user);
                            }
                            Logger.d("CompleteUser call: 数据库存储结束");
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResourceSubscriber<CommunalResult<CompleteUser>>() {

                    @Override
                    public void onNext(CommunalResult<CompleteUser> completeUserResult) {
                        wxEntryView.hideProgressDialog();
                        if (completeUserResult != null) {
                            if (completeUserResult.getCode() == 401) {
                                wxEntryView.showToast("登录令牌失效，请重新登录");
                                wxEntryView.startLoginActivity();
                            } else {
                                if (completeUserResult.getCode() == 0 && completeUserResult.getModel() != null) {
                                    Logger.d("CompleteUser onNext: " + completeUserResult.getModel().toString());
                                    wxEntryView.finishActivityLiteratureCheck();

                                } else if (!TextUtils.isEmpty(completeUserResult.getMessage())) {
                                    wxEntryView.showCompleteMessage(completeUserResult.getMessage());
                                }
                            }
                        } else {
                            wxEntryView.showToast("请检查网络");
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Logger.d("CompleteUser onError: " + throwable.toString());
                        wxEntryView.hideProgressDialog();
                        wxEntryView.showToast("请检查网络");
                    }

                    @Override
                    public void onComplete() {
                        Logger.d("CompleteUser onComplete");
                    }
                });
    }


    private void registerUserInformationQQ() {
        Logger.d(user_information_QQ);

        wxEntryView.showProgressDialog();

        DataRequestService dataRequestService = ServiceGenerator.getInstance(DataRequestService.class, true);
        dataRequestService.landingUserQQ(user_information_QQ)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnNext(new Consumer<CommunalResult<RegisterUser>>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull CommunalResult<RegisterUser> registerUserResult) throws Exception {
                        if (registerUserResult != null && registerUserResult.getCode() == 0 && registerUserResult.getModel() != null && registerUserResult.getModel().user != null) {
                            Logger.d("LandingUserQQ call: 数据库操作");

                            User user = registerUserResult.getModel().user;
                            user.token = registerUserResult.getModel().token;
                            user.is_new = registerUserResult.getModel().is_new;
                            user.platform = "QQ";
                            user.is_uploaded = registerUserResult.getModel().is_uploaded;
                            user.register_time = System.currentTimeMillis();

                            if (userDao == null) {
                                userDao = UserDao.getInstance(contextReference.get());
                            }

                            User localUser = userDao.getUser();
                            if (localUser != null) {
                                if (!localUser.id.equals(user.id)) {
                                    UserUtil.deleteUser(contextReference.get());
                                    UserUtil.deleteLiterature(contextReference.get());
                                }
                            } else {
                                UserUtil.deleteUser(contextReference.get());
                                UserUtil.deleteLiterature(contextReference.get());
                            }

                            if (userDao.isContainsUser(UserDao.getToken(contextReference.get()))) {
                                userDao.updateUser(user, UserDao.getToken(contextReference.get()));
                            } else {
                                userDao.insertUser(user);
                            }

                            if (!TextUtils.isEmpty(registerUserResult.getModel().token)) {
                                UserDao.setToken(registerUserResult.getModel().token);
                            }

                            Logger.d("LandingUserQQ call: 数据库操作完成");
                        }
                    }
                })
                .observeOn(Schedulers.newThread())
                .doOnNext(new Consumer<CommunalResult<RegisterUser>>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull CommunalResult<RegisterUser> registerUserResult) throws Exception {
                        if (registerUserResult != null && registerUserResult.getCode() == 0 && registerUserResult.getModel() != null && !TextUtils.isEmpty(registerUserResult.getModel().token)) {
                            Logger.d("LandingUserQQ call: 绑定推送设备");
                            DeviceUtil.pushBindingDevice(MiPushClient.getRegId(contextReference.get()), registerUserResult.getModel().token);
                        }
                    }
                })
                .observeOn(Schedulers.newThread())
                .doOnNext(new Consumer<CommunalResult<RegisterUser>>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull CommunalResult<RegisterUser> registerUserResult) throws Exception {
                        if (registerUserResult != null && registerUserResult.getCode() == 0 && registerUserResult.getModel() != null) {
                            if (registerUserResult.getModel().is_uploaded == 0) {
                                Logger.d("LandingUserQQ call: 上传用户书架信息");
                                try {
                                    BookUtil.uploadUserBookShelf(contextReference.get());
                                } catch (JSONException exception) {
                                    exception.printStackTrace();
                                }
                            } else if (registerUserResult.getModel().is_uploaded == 1) {
                                Logger.d("LandingUserQQ call: 下载用户书架信息");
                                BookUtil.downloadUserBookShelf(contextReference.get());
                            }
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResourceSubscriber<CommunalResult<RegisterUser>>() {
                    @Override
                    public void onNext(CommunalResult<RegisterUser> registerUserResult) {
                        wxEntryView.hideProgressDialog();
                        if (registerUserResult != null) {
                            if (registerUserResult.getCode() == 0 && registerUserResult.getModel() != null && registerUserResult.getModel().user != null) {
                                Logger.d("LandingUserQQ onNext: " + registerUserResult.getModel().user.toString());
                                if (registerUserResult.getModel().is_new == 1) {
                                    wxEntryView.showCompleteInformationFragment();
                                } else {
                                    wxEntryView.finishActivityLiteratureCheck();
                                }

                            } else if (!TextUtils.isEmpty(registerUserResult.getMessage())) {
                                wxEntryView.showToast(registerUserResult.getMessage());
                            }
                        } else {
                            wxEntryView.showToast("请检查网络");
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Logger.d("LandingUserQQ onError: " + throwable.toString());
                        wxEntryView.hideProgressDialog();
                        wxEntryView.showToast("请检查网络");
                    }

                    @Override
                    public void onComplete() {
                        Logger.d("LandingUserQQ onComplete");
                    }
                });
    }
}
