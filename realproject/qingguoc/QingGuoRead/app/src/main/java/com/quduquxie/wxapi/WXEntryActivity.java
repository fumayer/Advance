package com.quduquxie.wxapi;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.quduquxie.application.QuApplication;
import com.quduquxie.R;
import com.quduquxie.base.config.BaseConfig;
import com.quduquxie.base.manager.ActivityStackManager;
import com.quduquxie.base.module.main.activity.view.MainActivity;
import com.quduquxie.base.util.TypefaceUtil;
import com.quduquxie.communal.utils.UserUtil;
import com.quduquxie.db.UserDao;
import com.quduquxie.function.creation.literature.view.LiteratureListActivity;
import com.quduquxie.view.BaseActivity;
import com.quduquxie.communal.widget.CustomDialog;
import com.quduquxie.util.QGLog;
import com.quduquxie.wxapi.listener.LandingListener;
import com.quduquxie.wxapi.presenter.WXEntryPresenter;
import com.quduquxie.wxapi.view.CompleteUserFragment;
import com.quduquxie.wxapi.view.LoginFragment;
import com.quduquxie.wxapi.view.RecoveredFragment;
import com.quduquxie.wxapi.view.RegisterFragment;
import com.tencent.connect.UserInfo;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created on 16/11/30.
 * Created by crazylei.
 */

public class WXEntryActivity extends BaseActivity implements WXEntryInterface.View, IWXAPIEventHandler, IUiListener, LandingListener {

    private static final String TAG = WXEntryActivity.class.getSimpleName();

    private WXEntryInterface.Presenter wxEntryPresenter;

    @BindView(R.id.common_head_back)
    public ImageView common_head_back;
    @BindView(R.id.common_head_title)
    public TextView common_head_title;

    private FragmentManager fragmentManager;

    private IWXAPI iwxapi;


    private LoginFragment loginFragment;
    private RegisterFragment registerFragment;
    private RecoveredFragment recoveredFragment;
    private CompleteUserFragment completeUserFragment;

    private CustomDialog progressDialog;

    private static Tencent tencent;

    private boolean exit_login = false;
    private boolean literature = false;

    private Toast toast;

    private Typeface typeface_song_depict;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.layout_activity_wxentry);
        } catch (Resources.NotFoundException exception) {
            collectException(exception);
            exception.printStackTrace();
        }

        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent.getBooleanExtra("exit_login", false)) {
            exit_login = true;
        }
        if (intent.getBooleanExtra("literature", false)) {
            literature = true;
        }

        typeface_song_depict = TypefaceUtil.loadTypeface(this, TypefaceUtil.TYPEFACE_SONG_DEPICT);

        fragmentManager = getSupportFragmentManager();

        iwxapi = WXAPIFactory.createWXAPI(this, BaseConfig.WX_APP_ID);
        iwxapi.registerApp(BaseConfig.WX_APP_ID);

        iwxapi.handleIntent(getIntent(), this);

        wxEntryPresenter = new WXEntryPresenter(this, getApplicationContext());
        wxEntryPresenter.init();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        iwxapi.handleIntent(intent, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void setPresenter(WXEntryInterface.Presenter wxEntryPresenter) {
        this.wxEntryPresenter = wxEntryPresenter;
    }

    @Override
    public void initView() {
        if (common_head_title != null) {
            common_head_title.setText(R.string.login);
            common_head_title.setTypeface(typeface_song_depict);
        }
    }

    @Override
    public void refreshViewTitle(String title) {
        if (common_head_title != null) {
            common_head_title.setText(title);
        }
    }

    @Override
    public void showLoginFragment() {
        if (fragmentManager == null) {
            fragmentManager = getSupportFragmentManager();
        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        hideFragment(fragmentTransaction);

        if (loginFragment == null) {
            loginFragment = new LoginFragment();
            loginFragment.setLandingListener(this);
        } else {
            fragmentTransaction.show(loginFragment);
        }

        if (!isFinishing()) {
            refreshViewTitle("登录");
            fragmentTransaction.replace(R.id.wxentry_content, loginFragment);

            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    @Override
    public void showCompleteInformationFragment() {
        if (fragmentManager == null) {
            fragmentManager = getSupportFragmentManager();
        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        hideFragment(fragmentTransaction);

        if (completeUserFragment == null) {
            completeUserFragment = new CompleteUserFragment();
            completeUserFragment.setLandingListener(this);
        } else {
            fragmentTransaction.show(completeUserFragment);
        }

        if (!isFinishing()) {
            refreshViewTitle("注册");
            fragmentTransaction.replace(R.id.wxentry_content, completeUserFragment);

            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    @Override
    public void finishActivity() {
        if (ActivityStackManager.getActivityStackManager().getActivities() == 1) {
            Intent intent = new Intent();
            intent.setClass(this, MainActivity.class);
            startActivity(intent);
        }
        finish();
    }

    @Override
    public void finishActivityLiteratureCheck() {
        if (literature) {
            startActivity(new Intent(this, LiteratureListActivity.class));
        } else {
            if (ActivityStackManager.getActivityStackManager().getActivities() == 1) {
                Intent intent = new Intent();
                intent.setClass(this, MainActivity.class);
                startActivity(intent);
            }
        }
        finish();
    }

    @Override
    public void setQQAccessToken(String access_token, String openid, String expires_in) {
        if (!TextUtils.isEmpty(access_token) &&  !TextUtils.isEmpty(expires_in) && !TextUtils.isEmpty(openid)) {
            tencent.setAccessToken(access_token, expires_in);
            tencent.setOpenId(openid);
        }
    }

    @Override
    public void updateUserInformationQQ() {
        if (tencent != null && tencent.isSessionValid()) {
            UserInfo userInfo = new UserInfo(getApplicationContext(), tencent.getQQToken());
            userInfo.getUserInfo(new IUiListener() {
                @Override
                public void onComplete(Object object) {
                    QGLog.e(TAG, "onComplete: " + object.getClass().getSimpleName() + " : " + object.toString());
                    if (wxEntryPresenter != null) {
                        try {
                            wxEntryPresenter.updateUserInformationQQ((JSONObject) object);
                        } catch (JSONException exception) {
                            collectException(exception);
                            exception.printStackTrace();
                        }
                    }
                }

                @Override
                public void onError(UiError uiError) {
                    QGLog.e(TAG, "onError: " + uiError.errorMessage + " : " + uiError.errorCode + " : " + uiError.errorDetail);
                    Toast.makeText(getApplicationContext(), "获取用户信息失败！", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancel() {
                    QGLog.e(TAG, "onCancel");
                    Toast.makeText(getApplicationContext(), "已取消获取用户信息！", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "获取用户信息失败！", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showToast(String message) {
        if (TextUtils.isEmpty(message)) {
            return;
        }

        if (toast == null) {
            toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        } else {
            toast.setText(message);
        }

        if (!isFinishing() && toast != null) {
            toast.show();
        }
    }

    @Override
    public void showCompleteMessage(String message) {
        if (completeUserFragment != null) {
            completeUserFragment.setCompletePrompt(message);
        }
    }

    @Override
    public void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new CustomDialog(this, R.layout.layout_dialog_progress, Gravity.CENTER);
            TextView progress_prompt = (TextView) progressDialog.findViewById(R.id.progress_prompt);
            progress_prompt.setText("正在处理用户请求...");
        }
        if (!progressDialog.isShowing() && !isFinishing()) {
            progressDialog.show();
        }
    }

    @Override
    public void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing() && !isFinishing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void startLoginActivity() {
        UserUtil.deleteUser(getApplicationContext());
        Intent intent = new Intent();
        intent.putExtra("exit_login", true);
        intent.setClass(this, WXEntryActivity.class);
        startActivity(intent);
    }

    @OnClick({R.id.common_head_back})
    public void onClickListener(View view) {
        switch (view.getId()) {
            case R.id.common_head_back:
                if (exit_login) {
                    ActivityStackManager.getActivityStackManager().exitLogin();
                    UserDao.setToken("");
                    UserUtil.deleteUser(this);
                    UserUtil.deleteLiterature(this);
                }
                finishActivity();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (exit_login) {
                ActivityStackManager.getActivityStackManager().exitLogin();
                UserDao.setToken("");
                UserUtil.deleteUser(this);
                UserUtil.deleteLiterature(this);
            }
            finishActivity();
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 微信登录监听
     **/
    @Override
    public void onReq(BaseReq baseReq) {
        QGLog.e(TAG, "微信回调 onReq: " + baseReq.openId);
    }

    @Override
    public void onResp(BaseResp baseResp) {
        QGLog.e(TAG, "微信回调 onResp: " + baseResp.errCode);

        hideProgressDialog();

        if(baseResp instanceof SendAuth.Resp){
            SendAuth.Resp resp = (SendAuth.Resp) baseResp;
            String code = resp.code;
            if (!TextUtils.isEmpty(code)) {
                if (wxEntryPresenter == null) {
                    wxEntryPresenter = new WXEntryPresenter(this, getApplicationContext());
                }
                wxEntryPresenter.registerUserInformationWeChat(code);
            }
        }
    }

    @Override
    public void landing(String telephone_number, String password) {
        if (wxEntryPresenter == null) {
            wxEntryPresenter = new WXEntryPresenter(this, getApplicationContext());
        }
        wxEntryPresenter.landingUserInformation(telephone_number, password);
    }

    @Override
    public void showRegisterFragment() {
        if (fragmentManager == null) {
            fragmentManager = getSupportFragmentManager();
        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        hideFragment(fragmentTransaction);

        if (registerFragment == null) {
            registerFragment = new RegisterFragment();
            registerFragment.setLandingListener(this);
        } else {
            fragmentTransaction.show(registerFragment);
        }

        if (!isFinishing()) {
            refreshViewTitle("注册");
            fragmentTransaction.replace(R.id.wxentry_content, registerFragment);

            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    @Override
    public void showRecoveredFragment() {
        if (fragmentManager == null) {
            fragmentManager = getSupportFragmentManager();
        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        hideFragment(fragmentTransaction);

        if (recoveredFragment == null) {
            recoveredFragment = new RecoveredFragment();
            recoveredFragment.setLandingListener(this);
        } else {
            fragmentTransaction.show(recoveredFragment);
        }

        if (!isFinishing()) {
            refreshViewTitle("找回密码");
            fragmentTransaction.replace(R.id.wxentry_content, recoveredFragment);

            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    @Override
    public void landingWidthWeChat() {
        if (iwxapi == null) {
            iwxapi = WXAPIFactory.createWXAPI(getApplicationContext(), BaseConfig.WX_APP_ID, true);
        }
        if (!iwxapi.isWXAppInstalled()) {
            showToast("没有安装微信,请先安装微信!");
            return;
        }

        showProgressDialog();

        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = QuApplication.getUserDeviceID();
        iwxapi.sendReq(req);
    }

    @Override
    public void landingWidthQQ() {
        loginToQQ();
    }

    @Override
    public void registerUserInformation(String telephone_number, String verification_code, String password) {
        if (wxEntryPresenter == null) {
            wxEntryPresenter = new WXEntryPresenter(this, getApplicationContext());
        }
        wxEntryPresenter.registerUserInformation(telephone_number, verification_code, password);
    }

    @Override
    public void completeUserInformation(String nickname, File avatar) {
        if (wxEntryPresenter == null) {
            wxEntryPresenter = new WXEntryPresenter(this, getApplicationContext());
        }
        wxEntryPresenter.completeUserInformation(nickname, avatar);
    }

    @Override
    public void recoveredUserPassword(String telephone_number, String verification_code, String password) {
        if (wxEntryPresenter == null) {
            wxEntryPresenter = new WXEntryPresenter(this, getApplicationContext());
        }
        wxEntryPresenter.recoveredUserPassword(telephone_number, verification_code, password);
    }

    public void hideFragment(FragmentTransaction fragmentTransaction) {

        if (loginFragment != null) {
            fragmentTransaction.hide(loginFragment);
        }

        if (registerFragment != null) {
            fragmentTransaction.hide(registerFragment);
        }

        if (recoveredFragment != null) {
            fragmentTransaction.hide(recoveredFragment);
        }

        if (completeUserFragment != null) {
            fragmentTransaction.hide(completeUserFragment);
        }
    }

    public void loginToQQ() {
        tencent = Tencent.createInstance(BaseConfig.QQ_APP_ID, getApplicationContext());
        tencent.login(this, "all", this);
    }

    /**
     * QQ登录监听
     **/
    @Override
    public void onComplete(Object object) {
        QGLog.e(TAG, "onComplete: " + object.getClass().getSimpleName() + " : " + object.toString());
        if (wxEntryPresenter != null && object != null) {
            try {
                wxEntryPresenter.initQQAccessToken((JSONObject) object);
            } catch (JSONException exception) {
                collectException(exception);
                exception.printStackTrace();
            }
        }
    }

    @Override
    public void onError(UiError uiError) {
        QGLog.e(TAG, "onError: " + uiError.errorMessage + " : " + uiError.errorCode + " : " + uiError.errorDetail);
        Toast.makeText(getApplicationContext(), "获取授权信息失败！", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCancel() {
        QGLog.e(TAG, "onCancel");
        Toast.makeText(getApplicationContext(), "已取消获取授权信息！", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Tencent.onActivityResultData(requestCode, resultCode, data, this);
    }
}
