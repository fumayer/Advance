package com.aiwue.controller;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.aiwue.model.User;
import com.aiwue.utils.ActionUtils;
import com.aiwue.utils.ConstantValue;
import com.aiwue.utils.SharedPreferencesMgr;
import com.google.gson.Gson;

/**
 *  用户controller， 单例
 * Created by Yibao on 2017年4月13日10:31:59
 * Copyright (c) 2017 aiwue.com All rights reserved
 */
public class UserController {
    private User user = null;

    private UserController() {
    }

    private static class UserControllerHolder {
        private static final UserController mUserController = new UserController();
    }

    public static UserController getInstance() {
        return UserControllerHolder.mUserController;
    }

    public void startLoginActivity(Context context, String toWhere) {
        Intent intent = new Intent();
        intent.setAction(ActionUtils.LOGIN);
        intent.putExtra(ConstantValue.LOGIN_TO_WHERE, toWhere);
        context.startActivity(intent);
    }


    public boolean isLogined() {
        User user= getUser();
        if (user == null || user.getId() == null || TextUtils.isEmpty(user.getAccessToken())) {
            return false;
        }else
            return true;
    }


    public void isLoginedOrLogin(Context context, String toWhere) {
        if (!isLogined()) {
            startLoginActivity(context, toWhere);
        } else {
            context.startActivity(new Intent(toWhere));
        }
    }

    public String getToken() {
        User user = getUser();
        if (user == null) return null;
        return user.getAccessToken();
    }

    public Integer getUserId() {
        User user = getUser();
        if (user == null) return null;
        return user.getId();

    }
    //保存用户对象到sharedpreference中去
    public void saveUser(User user) {
        this.user = user;
        if (this.user == null) {
            SharedPreferencesMgr.setString("UserInfo","");
            return;
        }
        Gson gson=new Gson();
        String str=gson.toJson(this.user);
        SharedPreferencesMgr.setString("UserInfo",str);

    }
    //从sharedpreference中读取用户对象
     public User getUser() {
        if (user == null) {
            String str  = SharedPreferencesMgr.getString("UserInfo", "");
            if (TextUtils.isEmpty(str)) return null;

            Gson gson=new Gson();
            user=gson.fromJson(str, User.class);
        }
        return user;
    }

    public void loginOut() {
        saveUser(null);
//        Instabug.setUsername("");
//        Instabug.setUserEmail("");
    }


//        NetUtil.loadData(AiwueConfig.GET_PERSONAL_DATA, NetUtil.METHOD_GET, params, new NetUtil.ResponseCallback() {
//            @Override
//            public void onResponse(String result) {
////                AiwueLog.d(TAG, "getPersonalDataFromNetwork onResponse result:" + result);
//                String ress = processUserInfoResponse(result);
//                if (ress != null)
//                    onFailed(ress);
//                callback.onSuccessed();
//            }
//
//            @Override
//            public void onFailed(String errorInfo) {
//                AiwueLog.e(TAG, "getPersonalDataFromNetwork onFailed errorInfo:" + errorInfo);
//                callback.onFailed(errorInfo);
//            }
//        });


//    private String processUserInfoResponse(String result) {
//        if (result == null || TextUtils.isEmpty(result))
//            return AiwueApplication.getAppContext().getString(R.string.login_fail);
//
//        Type myType = new TypeToken<ObjectResponseBaseBean<User>>() { }.getType();
//        ObjectResponseBaseBean<User> loginResponseBean = new Gson().fromJson(result, myType);
//
//        AiwueLog.e(TAG, "processUserInfoResponse loginResponseBean:" + loginResponseBean.toString());
//
//        if (!ObjectResponseBaseBean.ECODE_SUCCESS.equals(loginResponseBean.getEcode())) {
//            return loginResponseBean.getEmsg();
//        }
//        if (loginResponseBean.getResult() == null)
//            return AiwueApplication.getAppContext().getString(R.string.login_fail);
//
//        String mToken = loginResponseBean.getResult().getAccessToken();
//        String mUserId = loginResponseBean.getResult().getId();
//
//        AiwueLog.e(TAG, "processUserInfoResponse userId:" + mUserId + "accessToken" + mToken);
//
//        if (mUserId == null || TextUtils.isEmpty(mUserId) || mToken == null || TextUtils.isEmpty(mToken)) {
//            return AiwueApplication.getAppContext().getString(R.string.login_fail);
//        }
//        SharedPrefConfigManager.getInstance(AiwueApplication.getAppContext()).setUserId(mUserId);
//        SharedPrefConfigManager.getInstance(AiwueApplication.getAppContext()).setAccessToken(mToken);
//        SharedPrefConfigManager.getInstance(AiwueApplication.getAppContext()).setUserInfo(result);
//        User user = UserController.getInstance().getUser();
//        if (loginResponseBean.getResult().getNickName() != null)
//            Instabug.setUsername(user.getNickName());
//        if (loginResponseBean.getResult().getEmail() != null)
//            Instabug.setUsername(user.getEmail());
//        else
//            Instabug.setUsername(user.getMobile()+AiwueApplication.getAppContext().getString(R.string.feedback_email_suffix));
//        return null;
//    }

//    public void updatePersonalDataYoNetwork(User user, final UserResultCallback callback) {
//        Map<String, String> params = new HashMap<>();
//        params.put(ParamNames.NICK_NAME, user.getNickName());
//        params.put(ParamNames.REAL_NAME, user.getRealName());
//        params.put(ParamNames.HEAD_PIC_NAME, user.getHeadPicName());
//        params.put(ParamNames.BIRTHDAY, user.getBirthday().toString());
//        params.put(ParamNames.LIVE_CITY, user.getLiveCity());
//        params.put(ParamNames.GENDAR, user.getGendar().toString());
//        params.put(ParamNames.MOBILE, user.getMobile());
//        params.put(ParamNames.EMAIL, user.getEmail());
//        params.put(ParamNames.ADDRESS, user.getAddress());
//        params.put(ParamNames.BIRTH_PLACE, user.getBirthPlace());
//        params.put(ParamNames.SLOGEN, user.getSlogen());
//        params.put(ParamNames.HEIGHI, user.getHeight().toString());
//        params.put(ParamNames.WEIGHT, user.getWeight().toString());
//        params.put(ParamNames.HEADPIC_NAME, user.getHeadPicName());
//        params.put(ParamNames.PRAOSE_NUM, user.getPraiseNum().toString());
//        params.put(ParamNames.LIVE_CITY_ID, user.getLiveCityId().toString());
//        params.put(ParamNames.SCORE, user.getScore().toString());
//        params.put(ParamNames.BALANCE, user.getBalance().toString());
//        params.put(ParamNames.IS_MASTER, user.getIsMaster().toString());
//        params.put(ParamNames.ID, user.getId());
//        params.put(ParamNames.FANS_NUM, user.getFansNum().toString());
//        params.put(ParamNames.LEVEL, user.getLevel().toString());
//        params.put(ParamNames.ACCESSTOKEN, user.getAccessToken());
//        params.put(ParamNames.ILLEGAL_NUM, user.getIllegalNum().toString());
//        params.put(ParamNames.REG_TIME, user.getRegTime().toString());
//        params.put(ParamNames.STATUS, user.getStatus().toString());
//
//
//        NetUtil.loadData(AiwueConfig.UPDATE_PERSONAL_DATA, NetUtil.METHOD_POST, params, new NetUtil.ResponseCallback() {
//
//
//            @Override
//            public void onResponse(String result) {
//                AiwueLog.d(TAG, "updatePersonalDataFromNetwork onResponse result:" + result);
//                callback.onSuccessed();
//            }
//
//            @Override
//            public void onFailed(String errorInfo) {
//                AiwueLog.e(TAG, "updatePersonalDataFromNetwork onFailed errorInfo:" + errorInfo);
//                callback.onFailed(errorInfo);
//            }
//        });
//    }

}
