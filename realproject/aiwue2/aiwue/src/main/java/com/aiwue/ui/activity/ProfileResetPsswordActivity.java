package com.aiwue.ui.activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aiwue.R;
import com.aiwue.base.BaseMvpActivity;
import com.aiwue.iview.IProfileResetPasswordView;
import com.aiwue.iview.IProfileSettingView;
import com.aiwue.presenter.ProfileResetPasswordPresenter;
import com.aiwue.presenter.ProfileSettingPresenter;
import com.aiwue.ui.view.LoadingDialog;
import com.aiwue.utils.MD5Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.aiwue.base.AiwueConfig.PASSWORD_SALT;

/**
 * Created by Administrator on 2017/4/14.
 */

public class ProfileResetPsswordActivity extends BaseMvpActivity<ProfileResetPasswordPresenter> implements IProfileResetPasswordView {
    private Dialog loadingDialog = null;
    @BindView(R.id.back_resetpassword)
    ImageView back_resetpassword;
    @BindView(R.id.newpasswordInput)
    EditText newpasswordInput ;
    @BindView(R.id.cofirmpasswordInput)
    EditText cofirmpasswordInput;
    @BindView(R.id.savemodify_btn)
    Button savemodify_btn;
    @BindView(R.id.newpassword_img)
    ImageView newpassword_img;
    @BindView(R.id.cofirmpassword_img)
    ImageView cofirmpassword_img;


    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_profilesetting_account_resetpassword);
        ButterKnife.bind(this);
        initEditListener();

    }
    @Override
    protected void bindViews() {
    }
    @Override
    protected ProfileResetPasswordPresenter createPresenter() {
        return new ProfileResetPasswordPresenter(this);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }
    @Override
    protected void setListener() {
    }


    @OnClick({R.id.back_resetpassword,R.id.newpasswordInput,R.id.cofirmpasswordInput,R.id.newpassword_img, R.id.cofirmpassword_img,R.id.savemodify_btn})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_resetpassword:
                finish();
                break;
            case R.id.newpasswordInput:
                break;
            case R.id.cofirmpasswordInput:
                break;
            case R.id.newpassword_img:
                newpasswordInput.setText(null);
                newpassword_img.setVisibility(View.GONE);
                break;
            case R.id.cofirmpassword_img:
                cofirmpasswordInput.setText(null);
                cofirmpassword_img.setVisibility(View.GONE);
                break;

            case R.id.savemodify_btn:
                String newpassword=newpasswordInput.getText().toString().trim();
                String cofirmpassword=cofirmpasswordInput.getText().toString().trim();
                if(!newpassword.equals(cofirmpassword)){
                    showToast("两次输入不一致");
                    return;
                }
                loadingDialog = LoadingDialog.createLoadingDialog(this, this.getResources().getString(R.string.login_loading_msg));
                LoadingDialog.showDialog(loadingDialog);
                //md5加密
                cofirmpassword = cofirmpassword.concat(PASSWORD_SALT);
                cofirmpassword = MD5Utils.EncoderByMd5(cofirmpassword);
                mvpPresenter.resetPassword(cofirmpassword);
                break;
                }
    }
    private void initEditListener() {
        newpasswordInput.addTextChangedListener(myTextWatcher);
        cofirmpasswordInput.addTextChangedListener(myTextWatcher);
    }
    public TextWatcher myTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String newpassword=newpasswordInput.getText().toString().trim();
            String cofirmpassword=cofirmpasswordInput.getText().toString().trim();
                if (newpassword.length()>0){
                    newpassword_img.setVisibility(View.VISIBLE);
                }else if(cofirmpassword.length()>0){
                    cofirmpassword_img.setVisibility(View.VISIBLE);
                }

            if (cofirmpassword.length()>0){
                cofirmpassword_img.setVisibility(View.VISIBLE);
                newpassword_img.setVisibility(View.GONE);
            }else {cofirmpassword_img.setVisibility(View.GONE);
                newpassword_img.setVisibility(View.VISIBLE);}

            if (newpassword.length()>5&&cofirmpassword.length()>5){
                    savemodify_btn.setEnabled(true);
                    savemodify_btn.setClickable(true);
            }else{
                savemodify_btn.setEnabled(false);
                savemodify_btn.setClickable(false);
            }
        }
        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    @Override
    public void onResetPasswordSuccess(Boolean success, String err) {
        LoadingDialog.closeDialog(loadingDialog);
        if (success){
            showToast("密码修改成功");
            finish();
        }else {
            showToast(err);
        }


    }
}
