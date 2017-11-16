package com.shortmeet.www.utilsUsed;

import android.content.Context;
import android.text.TextUtils;

import com.shortmeet.www.entity.percenalCenter.DataEntity;

/**
 * 本地用户管理类
 */

public class UserUtils {
    /*
     *  Fly 注：   设置与获取 用户当前身份
     */
    public static void  setUserIdintify(Context context,int type){
    PreferenceUtil.putInt (context,"usertype",type);
    }
    public static int getUserIdintify(Context context){
     return  PreferenceUtil.getInt(context,"usertype");
    }

    //保存游客 信息
    public  static void  saveVisitor(Context context,DataEntity user){
        PreferenceUtil.putLong(context, "yk_account_id", user.getAccount_id());
    //****    PreferenceUtil.putInt (context,"usertype", user.getUsertype());
        PreferenceUtil.putString(context,"yk_password",user.getPassword());
        PreferenceUtil.putString(context,"yk_username",user.getUsername());
        PreferenceUtil.putString(context,"yk_create_time",user.getCreate_time());
        PreferenceUtil.putString(context, "yk_session_time", user.getSession_time());
        if (!TextUtils.isEmpty(user.getSessionid()))
            UserUtils.setSessionId(context, user.getSessionid());
    }
//    //游客本地保存 和获取 Sessionid
//    public static void setYKSessionId(Context context, String token) {
//        PreferenceUtil.putString(context, "yk_token", token);
//    }
//    public static String getYKSessionId(Context context) {
//        return PreferenceUtil.getString(context, "yk_token");
//    }


    //获取游客信息
    public static DataEntity getVisitor(Context context){
        DataEntity user = new DataEntity();
        user.setAccount_id(PreferenceUtil.getLong(context, "yk_account_id"));
        user.setUsername(PreferenceUtil.getString(context,"yk_username"));
   //****     user.setUsertype(PreferenceUtil.getInt(context,"usertype"));
        user.setPassword(PreferenceUtil.getString(context,"yk_password"));
        user.setCreate_time(PreferenceUtil.getString(context,"yk_create_time"));
        user.setSession_time(PreferenceUtil.getString(context, "yk_session_time"));
        user.setSessionid(UserUtils.getSessionId(context));
        return user;
    }


   //保存用户信息
    public static void saveUser(Context context,DataEntity user) {
        PreferenceUtil.putLong(context, "account_id", user.getAccount_id());
 //     PreferenceUtil.putString(context,"username",user.getUsername());
//      PreferenceUtil.putString(context,"password",user.getPassword());
  //    PreferenceUtil.putString(context,"create_time",user.getCreate_time());
 //****       PreferenceUtil.putInt (context,"usertype", user.getUsertype());

        PreferenceUtil.putString(context, "phone", user.getPhone());
        PreferenceUtil.putString(context, "img", user.getImg());
        PreferenceUtil.putString(context, "sex", user.getSex());
        PreferenceUtil.putString(context, "area", user.getArea());
        PreferenceUtil.putString(context, "nickname", user.getNickname());
        PreferenceUtil.putString(context, "birthday", user.getBirthday());
        PreferenceUtil.putString(context, "content", user.getContent());
        PreferenceUtil.putString(context, "reg_time", user.getReg_time());
        PreferenceUtil.putString(context, "session_time", user.getSession_time());

        if (!TextUtils.isEmpty(user.getSessionid()))
        UserUtils.setSessionId(context, user.getSessionid());
       // UserUtils.setLogin(context, true);
    }



    //获取当前用户信息
    public static  DataEntity  getUser(Context context) {
       DataEntity user = new DataEntity();
        user.setAccount_id(PreferenceUtil.getLong(context, "account_id"));
      // user.setUsername(PreferenceUtil.getString(context,"username"));
     //****   user.setUsertype(PreferenceUtil.getInt(context,"usertype"));
     // user.setPassword(PreferenceUtil.getString(context,"password"));
     // user.setCreate_time(PreferenceUtil.getString(context,"create_time"));

        user.setPhone(PreferenceUtil.getString(context, "phone"));
        user.setImg(PreferenceUtil.getString(context, "img"));
        user.setSex(PreferenceUtil.getString(context, "sex"));
        user.setArea(PreferenceUtil.getString(context, "area"));
        user.setNickname(PreferenceUtil.getString(context, "nickname"));
        user.setBirthday(PreferenceUtil.getString(context, "birthday"));
        user.setContent(PreferenceUtil.getString(context, "content"));
        user.setReg_time(PreferenceUtil.getString(context, "reg_time"));
        user.setSession_time(PreferenceUtil.getString(context, "session_time"));
        user.setSessionid(UserUtils.getSessionId(context));
        return user;
    }



    //清空当前用户信息
    public static void clearUser(Context context) {
        PreferenceUtil.putLong(context, "account_id",0);
        PreferenceUtil.putString(context,"username","");
        PreferenceUtil.putInt(context,"usertype",0); //***************
        PreferenceUtil.putString(context,"password","");
        PreferenceUtil.putString(context,"create_time","");

        PreferenceUtil.putString(context, "user_id","");
        PreferenceUtil.putString(context, "phone", "");
        PreferenceUtil.putString(context, "img","");
        PreferenceUtil.putString(context, "sex","");
        PreferenceUtil.putString(context, "area","");
        PreferenceUtil.putString(context, "nickname","");
        PreferenceUtil.putString(context, "birthday","");
        PreferenceUtil.putString(context, "content","");
        PreferenceUtil.putString(context, "reg_time","");
        PreferenceUtil.putInt(context, "session_time",0);
        UserUtils.setSessionId(context, "");
        UserUtils.setLogin(context, false);
    }



    //正式用户本地保存 和获取 Sessionid
    public static void setSessionId(Context context, String token) {
        PreferenceUtil.putString(context, "token", token);
    }
    public static String getSessionId(Context context) {
        return PreferenceUtil.getString(context, "token");
    }



    //引导页
    public static boolean GetIsFirst(Context context) {
        return PreferenceUtil.getBoolean(context, "isF", true);
    }
    public static void SetIsFirst(Context context) {
        PreferenceUtil.putBoolean(context, "isF",false);
    }




    //当前用户是否登录
    public static boolean isLogin(Context context) {
        return PreferenceUtil.getBoolean(context, "isLogin", false);
    }
    public static void setLogin(Context context, boolean login) {
        PreferenceUtil.putBoolean(context, "isLogin", login);
    }



    //保存替换用户头像
    public  static void saveUserImage(Context context, String str){
        PreferenceUtil.putString(context, "img",str);
    }

}
