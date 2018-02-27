package com.aiwue.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.aiwue.base.AiwueApplication;
import com.aiwue.model.User;
import com.aiwue.update.PushMessage;
import com.google.gson.Gson;


/**
 * Created by liaixiong on 16-8-25.
 */
public class SharedPrefConfigManager {
    private static Context context = null;
    private String mSharedPreferenceName = null;
    private SharedPreferences mSharedPref = null;

    private static class InnerConfigManager {
        private static final SharedPrefConfigManager instance = new SharedPrefConfigManager(context);
    }

    private SharedPrefConfigManager(Context context) {
        if (ProcessCheck.IsServiceProcess()) {
            mSharedPreferenceName = context.getPackageName() + "_preferences";
            mSharedPref = AiwueApplication.getAppContext().getSharedPreferences(mSharedPreferenceName, Context.MODE_PRIVATE);
        }
    }

    public static SharedPrefConfigManager getInstance(Context context) {
        if (context == null) {
            context = AiwueApplication.getApplication().getApplicationContext();
        }
        SharedPrefConfigManager.context = context.getApplicationContext(); // context
        SharedPrefConfigManager configManager = InnerConfigManager.instance;
        return configManager;
    }

    public String getSharedPreferenceName() {
        return mSharedPreferenceName;
    }

    private SharedPreferences getSharedPreference() {
        ProcessCheck.CheckServiceProcess();
        return mSharedPref;
    }

    public long getLong(String key, long defValue) {
        if (ProcessCheck.IsServiceProcess()) {
            return getSharedPreference().getLong(key, defValue);
        } else {
            return SharedPrefConfigProvider.getLong(key, defValue);
        }
    }

    public boolean getBoolean(String key, boolean defValue) {
        if (ProcessCheck.IsServiceProcess()) {
            return getSharedPreference().getBoolean(key, defValue);
        } else {
            return SharedPrefConfigProvider.getBoolean(key, defValue);
        }
    }

    public int getInt(String key, int defValue) {
        if (ProcessCheck.IsServiceProcess()) {
            return getSharedPreference().getInt(key, defValue);
        } else {
            return SharedPrefConfigProvider.getInt(key, defValue);
        }
    }

    public float getFloat(String key, float defValue) {
        if (ProcessCheck.IsServiceProcess()) {
            return getSharedPreference().getFloat(key, defValue);
        } else {
            return SharedPrefConfigProvider.getFloat(key, defValue);
        }
    }

    public String getString(String key, String defValue) {
        if (ProcessCheck.IsServiceProcess()) {
            return getSharedPreference().getString(key, defValue);
        } else {
            return SharedPrefConfigProvider.getString(key, defValue);
        }
    }

    public void setBoolean(String key, boolean value) {
        if (ProcessCheck.IsServiceProcess()) {
            SharedPreferences.Editor editor = getSharedPreference().edit();
            editor.putBoolean(key, value);
            SharedPrefUtil.applyToEditor(editor);
        } else {
            SharedPrefConfigProvider.setBoolean(key, value);
        }
    }

    public void setBooleanForce(String key, boolean value) {
        if (ProcessCheck.IsServiceProcess()) {
            SharedPreferences.Editor editor = getSharedPreference().edit();
            editor.putBoolean(key, value);
            SharedPrefUtil.commitToEditor(editor);
        } else {
            SharedPrefConfigProvider.setBoolean(key, value);
        }
    }

    public void setLong(String key, long value) {
        if (ProcessCheck.IsServiceProcess()) {
            SharedPreferences.Editor editor = getSharedPreference().edit();
            editor.putLong(key, value);
            SharedPrefUtil.applyToEditor(editor);
        } else {
            SharedPrefConfigProvider.setLong(key, value);
        }
    }

    public void setMultiLong(String[] key, long[] value) {
        if (key == null || value == null) {
            return;
        }
        if (key.length != value.length) {
            return;
        }
        if (ProcessCheck.IsServiceProcess()) {
            SharedPreferences.Editor editor = getSharedPreference().edit();
            for (int i = 0; i < key.length; i++) {
                if (value[i] < 0) {
                    continue;
                }
                editor.putLong(key[i], value[i]);
            }
            SharedPrefUtil.applyToEditor(editor);
        } else {
            for (int i = 0; i < key.length; i++) {
                if (value[i] < 0) {
                    continue;
                }
                SharedPrefConfigProvider.setLong(key[i], value[i]);
            }
        }
    }

    public void setInt(String key, int value) {
        if (ProcessCheck.IsServiceProcess()) {
            SharedPreferences.Editor editor = getSharedPreference().edit();
            editor.putInt(key, value);
            SharedPrefUtil.applyToEditor(editor);
        } else {
            SharedPrefConfigProvider.setInt(key, value);
        }
    }

    public void setFloat(String key, float value) {
        if (ProcessCheck.IsServiceProcess()) {
            SharedPreferences.Editor editor = getSharedPreference().edit();
            editor.putFloat(key, value);
            SharedPrefUtil.applyToEditor(editor);
        } else {
            SharedPrefConfigProvider.setFloat(key, value);
        }
    }

    public void setString(String key, String value) {
        if (ProcessCheck.IsServiceProcess()) {
            SharedPreferences.Editor editor = getSharedPreference().edit();
            editor.putString(key, value);
            SharedPrefUtil.applyToEditor(editor);
        } else {
            SharedPrefConfigProvider.setString(key, value);
        }
    }


    //请从这以后新增shared preference的(key,value)读写接口
    //保存用户对象到sharedpreference中去
    public void setUserInfo(User user) {
        if (user == null) {
            setString("UserInfo", "");
            return;
        }
        Gson gson=new Gson();
        String str=gson.toJson(user);
        setString("UserInfo", str);
    }
    //从sharedpreference中读取用户对象
    public User getUserInfo() {
        String str = getString("UserInfo", "");
        if (TextUtils.isEmpty(str))
            return null;

        Gson gson=new Gson();
        User user=gson.fromJson(str, User.class);
        return user;
    }

    public String getUserId() { return getString("userId", "");    }
    public void setUserId(String userId) {setString("userId", userId);   }
    public String getAccessToken() {
        return getString("accessToken", "");
    }
    public void setAccessToken(String accessToken) {
        setString("accessToken", accessToken);
    }

    public String getOpenId() {
        return getString("openId", "");
    }
    public void setOpenId(String openId) {
        setString("openId", openId);
    }


    /**********************************************************************
     **                                  升级相关                         **
     *********************************************************************/

    public String getDataVersion() {
        return getString("version_data", null);
    }

    public void setDataVerison(String version) {

        setString("version_data", version);

    }

    public void setUpdateTimeChecked(String version) {

        setString("update_time_checked", version + "_true");

    }

    public boolean isUpdateTimeChecked(String version) {
        String checked = getString("update_time_checked", version + "_false");
        if (checked.equalsIgnoreCase(version + "_true")) {
            return true;
        }
        return false;
    }

    public long getUpdateInstallTime(String version) {
        String installTime = getString("update_install_time", null);
        if (installTime != null) {
            if (installTime.startsWith(version)) {
                return Long.parseLong(installTime.substring(installTime.lastIndexOf("_") + 1,
                        installTime.length()));
            }
        }
        return 0;
    }

    /*
     * 主动升级 推荐升级 type = initiative recommend none
     */
    //
    public static final String UPDATE_TYPE_INITIATIVE = "initiative";
    public static final String UPDATE_TYPE_RECOMMEND = "recommend";
    public static final String UPDATE_TYPE_NONE = "none";

    public void setUpdateType(String version, String type) {

        setString("update_install_type", version + "_" + type);

    }

    public String getUpdateType(String version) {
        String installType = getString("update_install_type", null);
        if (installType != null) {
            if (installType.startsWith(version)) {
                return installType
                        .substring(installType.lastIndexOf("_") + 1, installType.length());
            }
        }
        return "none";
    }

    public void setUpdateInstallTime(String version) {

        setString("update_install_time", version + "_" + System.currentTimeMillis());

    }

    public boolean isForceUpdate(String version) {
        String ForceVersion = getString("is_force_update", null);
        return (ForceVersion != null && ForceVersion.compareToIgnoreCase(version) == 0);
    }

    public void setForceUpdate(String version) {

        setString("is_force_update", version);

    }

    public void setUpdateAutoCheck(boolean auto) {

        setBoolean("update_auto_check", auto);

    }

    public boolean isUpdateAutoCheck() {
        return getBoolean("update_auto_check", true);
    }

    public void setAutoDownInWifiModel(boolean down) {

        setBoolean("update_auto_down_in_wifi", down);

    }

    public boolean isAutoDownInWifiModel() {
        return getBoolean("update_auto_down_in_wifi", true);
    }

    public void setUpdateTime() {

        setLong("update_check_time", System.currentTimeMillis());

    }

    public long getUpdateTime() {
        return getLong("update_check_time", 0);
    }

    public void setPushTime() {

        setLong("push_check_time", System.currentTimeMillis());

    }

    public long getPushTime() {
        return getLong("push_check_time", 0);
    }

    public void setUpdateCheckInterval(int days) {
        setInt("update_check_interval_in_2g3g", days);
    }

    public int getUpdateCheckInterval() {
        return getInt("update_check_interval_in_2g3g", 3);
    }

    public void setPushVersion(int version) {
        setInt("push_message_version", version);
    }

    public int getPushVersion() {
        return getInt("push_message_version", 0);
    }

    public void setLastApkUpdateTime(long time) {
        setLong("last_apk_update_time", time);
    }

    public long getLastApkUpdateTime() {
        return getLong("last_apk_update_time", 0);
    }

    public void setLastInjectAppsTime(long t) {
        setLong("last_ijt_app_time", t);
    }

    public long getLastInjectAppsTime() {
        return getLong("last_ijt_app_time", 0);
    }

    public boolean isShowUpdateTip() {
        return getBoolean("is_show_update_tip", false);
    }

    public String getUpdateTipContent() {
        return getString("main_tip_update_content", null);
    }

    public void setShowUpdateTip(boolean show, String content) {

        setBoolean("is_show_update_tip", show);
        if (content != null) {
            setString("main_tip_update_content", content);
        }

    }

    public boolean isShowPushTip() {
        return getBoolean("is_show_push_main_acitivity", false);
    }

    public void setShowPushTip(boolean show) {

        setBoolean("is_show_push_main_acitivity", show);

    }

    public void setRecommandVersion(String version) {

        setString("update_recommand_version", version);

    }

    public void setNeedUploadAdflow(boolean isneed)
    {
        setBoolean("is_need_upload_adflow", isneed);
    }

    public boolean getNeedUploadAdflow()
    {
        return getBoolean("is_need_upload_adflow", true);
    }

    public String getRecommandVersion() {
        return getString("update_recommand_version", null);
    }

    public int getNewLogCount() {
        return getInt("new_log_count", 0);
    }

    public int addOneToNewLogCount() {
        int count = getInt("new_log_count", 0);
        if (count < 100) {
            count++;
            setInt("new_log_count", count);
        }
        return count;
    }

    public int deleteOneFromNewLogCount() {
        int count = getInt("new_log_count", 0);
        if (count > 0) {
            count--;
            setInt("new_log_count", count);
        }
        return count;
    }

    public void clearNewLogCount() {
        setInt("new_log_count", 0);
    }

    //liaixoing:下面这个函数有依赖,不适合这在这个文件.把它移出去.
    public PushMessage getPush() {
        PushMessage msg = new PushMessage();

        msg.type = getString("push_tip_type", "");
        msg.noti_title = getString("push_tip_noti_title", "");
        msg.noti_content = getString("push_tip_noti_content", "");
        msg.des_title = getString("push_tip_des_title", "");
        msg.description = getString("push_tip_description", "");
        msg.url = getString("push_tip_url", "");
        msg.btn_positive = getString("push_tip_btn_positive", "");
        msg.btn_negetive = getString("push_tip_btn_negetive", "");
        msg.activity = getString("push_tip_activity", "");

        return msg;
    }
    public void savePush(PushMessage msg) {

        setString("push_tip_type", msg.type);
        setString("push_tip_noti_title", msg.noti_title);
        setString("push_tip_noti_content", msg.noti_content);
        setString("push_tip_des_title", msg.des_title);
        setString("push_tip_description", msg.description);
        setString("push_tip_url", msg.url);
        setString("push_tip_btn_positive", msg.btn_positive);
        setString("push_tip_btn_negetive", msg.btn_negetive);
        setString("push_tip_activity", msg.activity);
    }

    public String getUpdateCheckDate() {
        return getString("update_check_date", "");
    }
    public void setUpdateCheckDate(String date) {
        setString("update_check_date", date);
    }
    public int getUpdateCheckNumber() {
        return getInt("update_check_times", 0);
    }
    public void setUpdateCheckNumber(int number) {
        setInt("update_check_times", number);
    }


}
