package com.aiwue.ui.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aiwue.R;
import com.aiwue.base.BaseMvpActivity;
import com.aiwue.controller.UserController;
import com.aiwue.iview.ILoginView;
import com.aiwue.model.Notice;
import com.aiwue.model.User;
import com.aiwue.presenter.LoginPresenter;
import com.aiwue.ui.view.LoadingDialog;
import com.aiwue.utils.ConstantValue;
import com.aiwue.utils.MD5Utils;
import com.aiwue.utils.RegExpValidator;
import com.aiwue.utils.RxCountDown;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.apache.http.util.TextUtils;
import org.greenrobot.eventbus.EventBus;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.functions.Action0;
import timber.log.Timber;

import static com.aiwue.base.AiwueConfig.PASSWORD_SALT;

/**
 *  登录/注册activity
 * Created by Yibao on 2017年4月13日10:44:39
 * Copyright (c) 2017 aiwue.com All rights reserved
 */
public class LoginActivity extends BaseMvpActivity<LoginPresenter> implements ILoginView {
    @BindView(R.id.first_line_input)
    RelativeLayout first_line_input;
    @BindView(R.id.accountInput)
    EditText accountInput;
    @BindView(R.id.no_pwd_input_line)
    LinearLayout no_pwd_input_line;
    @BindView(R.id.mobileInput)
    EditText mobileInput;
    @BindView(R.id.sendVCodeBtn)
    TextView sendVCodeBtn;
    @BindView(R.id.passwordInput)
    EditText passwordInput;
    @BindView(R.id.textTip)
    TextView textTip;
    @BindView(R.id.loginBtn)
    Button loginBtn;
    @BindView(R.id.loginSwitch)
    TextView loginSwitch;
    @BindView(R.id.app_auth_qq)
    ImageView app_auth_qq;
    @BindView(R.id.app_auth_sina)
    ImageView app_auth_sina;

    public static final String TAG = LoginActivity.class.getSimpleName();
    private Context mContext = null;
    private UMShareAPI mShareAPI;
    private String toWhere = null;
    private Boolean isNormalLogin = true; //初始时，正常的账号密码登陆
    private boolean isCountingDown = false;//是否正在倒计时
    private int verifyId; //验证码Id
    private Dialog loadingDialog = null;

    //初始化，加载布局和方法
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Intent intent = getIntent();
        mContext = this;
        toWhere = intent.getStringExtra(ConstantValue.LOGIN_TO_WHERE);
        super.onCreate(savedInstanceState);
        initToolBar(null);
        initEditListener();
        //umeng初始化，获取UMShareAPI
        mShareAPI = UMShareAPI.get(this);
    }
    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }
    @Override
    protected void bindViews() {
    }
    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter(this);
    }
    @Override
    protected void processLogic(Bundle savedInstanceState) {
    }
    @Override
    protected void setListener() {
    }

    @OnClick({R.id.loginSwitch, R.id.sendVCodeBtn, R.id.loginBtn})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.loginSwitch:
                if (isNormalLogin) {
                    isNormalLogin = false;
                    no_pwd_input_line.setVisibility(View.VISIBLE);
                    accountInput.setVisibility(View.GONE);
                    String str = accountInput.getText().toString().trim();
                    if (RegExpValidator.IsDigital(str))
                        mobileInput.setText(str);
                    mobileInput.setInputType(InputType.TYPE_CLASS_NUMBER);
                    passwordInput.setInputType(InputType.TYPE_CLASS_NUMBER);
                    textTip.setText(R.string.login_tip_no_pwd);
                    loginSwitch.setText(R.string.login_switch_with_pwd);

                }else{
                    isNormalLogin = true;
                    no_pwd_input_line.setVisibility(View.GONE);
                    accountInput.setVisibility(View.VISIBLE);
                    String str = mobileInput.getText().toString().trim();
                    if (!TextUtils.isBlank(str))
                        accountInput.setText(str);
                    passwordInput.setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                    textTip.setText(R.string.login_tip_normal);
                    loginSwitch.setText(R.string.login_switch_no_pwd);
                }
                break;
            case R.id.sendVCodeBtn:
                String mobile = mobileInput.getText().toString().trim();
                if (!RegExpValidator.IsValidMobileNum(mobile)) {
                    showToast(this.getResources().getString(R.string.login_mobile_num_error));
                    return;
                }
                //new Thread(new countDownThread()).start();
                startCountDown(60, mobile);// 发送验证码，并开启倒计时
                break;
            case R.id.loginBtn:
                if (isNormalLogin) {
                    mobile = accountInput.getText().toString().trim();
                    String password = passwordInput.getText().toString().trim();
                    if (mobile.length() <4 || password.length() < 4){
                        showToast(this.getResources().getString(R.string.login_invalid_mobile_vcode));
                        return;
                    }
                    loadingDialog = LoadingDialog.createLoadingDialog(this, this.getResources().getString(R.string.login_loading_msg));
                    LoadingDialog.showDialog(loadingDialog);

                    password = password.concat(PASSWORD_SALT);
                    password = MD5Utils.EncoderByMd5(password);
                    mvpPresenter.LoginWithNormal(mobile,password,""); //TODO ipAddr未来再加
                }else {
                    mobile = mobileInput.getText().toString().trim();
                    String vcode = passwordInput.getText().toString().trim();
                    if (vcode.length() <4 || !RegExpValidator.IsValidMobileNum(mobile)){
                        showToast(this.getResources().getString(R.string.login_invalid_mobile_vcode));
                        return;
                    }
                    loadingDialog = LoadingDialog.createLoadingDialog(this, this.getResources().getString(R.string.login_loading_msg));
                    LoadingDialog.showDialog(loadingDialog);
                    mvpPresenter.LoginWithVCode(mobile,verifyId,vcode,""); //TODO 设备名称未来再加
                }
                break;
        }
    }
    //倒计时发送验证码
    private void startCountDown(int countDonwcount, final String mobile) {
        mSubscription = RxCountDown.countDown(countDonwcount)
        .doOnSubscribe(new Action0() {
            @Override
            public void call() {
                isCountingDown = true;
                sendVCodeBtn.setEnabled(false);
                sendVCodeBtn.setClickable(false);
                mobileInput.setEnabled(false);
                //调取发送验证码接口,发送验证码
                mvpPresenter.sendVCode(mobile);
            }
        })
        .subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                Timber.i("结束倒计时");
                isCountingDown = false;
                sendVCodeBtn.setText(R.string.login_vcode_button_caption);
                sendVCodeBtn.setTextColor(Color.WHITE);
                sendVCodeBtn.setClickable(true);
                sendVCodeBtn.setEnabled(true);
                mobileInput.setEnabled(true);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Integer integer) {
                Timber.i("正在倒计时：" + integer);
               // mSkipReal.setText(TextUtils.concat(integer.intValue() + "s", getResources().getString(R.string.splash_ad_ignore)));
                sendVCodeBtn.setText(integer.intValue() + "s");
            }
        });
    }
    //监听Edit输入的方法
    private void initEditListener() {
        accountInput.addTextChangedListener(myTextWatcher);
        mobileInput.addTextChangedListener(myTextWatcher);
        passwordInput.addTextChangedListener(myTextWatcher);
    }

    public TextWatcher myTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (isNormalLogin){
                String account = accountInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();
                if(account.length()>4&&password.length()>4){
                    loginBtn.setEnabled(true);
                    loginBtn.setClickable(true);
                }else {
                    loginBtn.setEnabled(false);
                    loginBtn.setClickable(false);
                }
            }else{
                String mobileNum = mobileInput.getText().toString().trim();
                String vcode = passwordInput.getText().toString().trim();
                if (RegExpValidator.IsValidMobileNum(mobileNum)){
                    if (vcode.length()>= 4){
                        loginBtn.setEnabled(true);
                        loginBtn.setClickable(true);
                    }
                    if (!isCountingDown) {
                        sendVCodeBtn.setTextColor(Color.WHITE);
                        sendVCodeBtn.setClickable(true);
                    }
                }else {
                    loginBtn.setEnabled(false);
                    loginBtn.setClickable(false);
                    if (!isCountingDown) {
                        sendVCodeBtn.setTextColor(Color.GRAY);
                        sendVCodeBtn.setClickable(false);
                    }

                }
            }
        }
        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    //开始第三方登录
    public void onClickAuth(View view) {
        SHARE_MEDIA platform = null;
        if (view.getId() == R.id.app_auth_sina) {
            platform = SHARE_MEDIA.SINA;
        } else if (view.getId() == R.id.app_auth_qq) {
            platform = SHARE_MEDIA.QQ;
        }

//        else if (view.getId() == R.id.app_auth_wechat) {
//            platform = SHARE_MEDIA.WEIXIN;
//        }
        /**begin invoke umeng api**/
        mShareAPI.doOauthVerify(LoginActivity.this, platform, umAuthListener);
    }


    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            Log.e(TAG, platform + " Authorize succeed");
            int type = -1;
            if (platform == SHARE_MEDIA.WEIXIN) {
                type = ConstantValue.THIRD_PARTY_LOGIN_WEIXIN;
            } else if (platform == SHARE_MEDIA.SINA) {
                type = ConstantValue.THIRD_PARTY_LOGIN_WEIBO;
            } else if (platform == SHARE_MEDIA.QQ) {
                type = ConstantValue.THIRD_PARTY_LOGIN_QQ;
            }

            String openId;
            if (platform == SHARE_MEDIA.SINA) {
                openId = data.get("uid");
            }else{
                openId = data.get("openid");
                }
            String openAccessToken = data.get("access_token");
            Timber.e(", openId:%s, type:%d, openAccessToken:%s", openId, type, openAccessToken );
            mvpPresenter.thirdPartyLogin(openId, openAccessToken,type, "");
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Log.e(TAG, platform + " Authorize fail");
            Toast.makeText(mContext, R.string.login_3rd_failure, Toast.LENGTH_SHORT).show();

            //登录失败，仍然会停留在登录页，用户可以继续选择其它方式登录，
            // 如果想返回，可以点击左上角的按钮返回
            //LoginActivity.this.finish();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText(mContext, R.string.login_3rd_cancel, Toast.LENGTH_SHORT).show();

            //登录失败，仍然会停留在登录页，用户可以继续选择其它方式登录，
            // 如果想返回，可以点击左上角的按钮返回
            //LoginActivity.this.finish();
            Log.e(TAG, platform + " Authorize cancel");
        }
    };
    //验证码发送后处理
    @Override
    public void onSendVCodeSuccess(Boolean success, String err, Integer verifyId){
        if (success) {
            this.verifyId = verifyId;
            showToast(this.getResources().getString(R.string.login_has_sent_auth_code));
        }else {
            showToast(err);
        }
    }

    //用户登录后处理
    @Override
    public void onLoginSuccess(Boolean success, String err,User response) {
        LoadingDialog.closeDialog(loadingDialog);

        if (success) {
            Timber.e("Login onSuccessed");
            UserController.getInstance().saveUser(response);
            //发送登录变化的消息
            Notice msg = new  Notice();
            msg.type = ConstantValue.EVENTBUS_MESSENGE_LOGIN_LOGOUT;
            EventBus.getDefault().post(msg);

            LoginActivity.this.finish();
            if (!TextUtils.isEmpty(toWhere)) {
                Intent intent = new Intent();
                intent.setAction(toWhere);
                startActivity(intent);
            }
        }else {
            Timber.e("Login onFailed");
            showToast(err);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mShareAPI.onActivityResult(requestCode, resultCode, data);
    }
}
