package com.quduquxie.revise.view;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.quduquxie.R;
import com.quduquxie.base.util.TypefaceUtil;
import com.quduquxie.communal.utils.UserUtil;
import com.quduquxie.communal.widget.CustomDialog;
import com.quduquxie.revise.ReviseUserInterface;
import com.quduquxie.revise.listener.ReviseUserListener;
import com.quduquxie.revise.presenter.ReviseUserPresenter;
import com.quduquxie.revise.view.fragment.ModifyBindingFragment;
import com.quduquxie.revise.view.fragment.ModifyNumberFragment;
import com.quduquxie.revise.view.fragment.ModifyPasswordFragment;
import com.quduquxie.revise.view.fragment.ModifyQQFragment;
import com.quduquxie.revise.view.fragment.ModifyUserFragment;
import com.quduquxie.view.BaseActivity;
import com.quduquxie.wxapi.WXEntryActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created on 16/12/5.
 * Created by crazylei.
 */

public class ReviseUserActivity extends BaseActivity implements ReviseUserInterface.View, ReviseUserListener {
    private ReviseUserInterface.Presenter reviseUserPresenter;

    @BindView(R.id.revise_head_back)
    public ImageView revise_head_back;
    @BindView(R.id.revise_head_title)
    public TextView revise_head_title;
    @BindView(R.id.revise_head_save)
    public TextView revise_head_save;
    @BindView(R.id.revise_user_content)
    public FrameLayout revise_user_content;

    private FragmentManager fragmentManager;
    private ModifyUserFragment modifyUserFragment;
    private ModifyNumberFragment modifyNumberFragment;
    private ModifyBindingFragment modifyBindingFragment;
    private ModifyPasswordFragment modifyPasswordFragment;
    private ModifyQQFragment modifyQQFragment;

    private CustomDialog progressDialog;

    private Typeface typeface_song_depict;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.layout_activity_revise_user);
        } catch (Resources.NotFoundException exception) {
            collectException(exception);
            exception.printStackTrace();
        }

        ButterKnife.bind(this);

        typeface_song_depict = TypefaceUtil.loadTypeface(this, TypefaceUtil.TYPEFACE_SONG_DEPICT);

        fragmentManager = getSupportFragmentManager();

        reviseUserPresenter = new ReviseUserPresenter(this, getApplicationContext());

        Intent intent = getIntent();
        if (intent != null) {
            String message = intent.getStringExtra("information");
            if ("binding".equals(message)) {
                reviseUserPresenter.init();
                showModifyBindingFragment();
            } else if ("reviserQQ".equals(message)) {
                reviseUserPresenter.init();
                showModifyQQFragment();
            } else {
                reviseUserPresenter.init();
                reviseUserPresenter.initParameter();
            }
        } else {
            reviseUserPresenter.init();
            reviseUserPresenter.initParameter();
        }
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
    public void setPresenter(ReviseUserInterface.Presenter reviseUserPresenter) {
        this.reviseUserPresenter = reviseUserPresenter;
    }

    @Override
    public void initView() {

    }

    @Override
    public void showUserFragment() {
        showModifyUserFragment();
    }

    @Override
    public void finishActivity() {
        finish();
    }

    @Override
    public void showErrorFragment() {

    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void refreshTitleView(String title) {
        if (revise_head_title != null) {
            revise_head_title.setText(title);
            revise_head_title.setTypeface(typeface_song_depict);
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

    @Override
    public void refreshPasswordView() {
        if (modifyPasswordFragment != null) {
            modifyPasswordFragment.refreshView();
        }
    }

    @Override
    public void refreshQQView() {
        if (modifyQQFragment != null) {
            modifyQQFragment.refreshView();
        }
    }

    @Override
    public void showModifyUserFragment() {

        if (fragmentManager == null) {
            fragmentManager = getSupportFragmentManager();
        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        hideFragment(fragmentTransaction);

        checkSaveState(true);

        if (modifyUserFragment == null) {
            modifyUserFragment = new ModifyUserFragment();
            modifyUserFragment.setReviseUserListener(this);
        } else {
            fragmentTransaction.show(modifyUserFragment);
        }

        if (!isFinishing()) {
            refreshTitleView("编辑资料");
            fragmentTransaction.replace(R.id.revise_user_content, modifyUserFragment);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    @Override
    public void showModifyNumberFragment() {
        if (fragmentManager == null) {
            fragmentManager = getSupportFragmentManager();
        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        hideFragment(fragmentTransaction);

        checkSaveState(false);

        if (modifyNumberFragment == null) {
            modifyNumberFragment = new ModifyNumberFragment();
            modifyNumberFragment.setReviseUserListener(this);
        } else {
            fragmentTransaction.show(modifyNumberFragment);
        }

        if (!isFinishing()) {
            refreshTitleView("重新绑定");
            fragmentTransaction.replace(R.id.revise_user_content, modifyNumberFragment);

            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    @Override
    public void showModifyBindingFragment() {
        if (fragmentManager == null) {
            fragmentManager = getSupportFragmentManager();
        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        hideFragment(fragmentTransaction);

        checkSaveState(false);

        if (modifyBindingFragment == null) {
            modifyBindingFragment = new ModifyBindingFragment();
            modifyBindingFragment.setReviseUserListener(this);
        } else {
            fragmentTransaction.show(modifyBindingFragment);
        }

        if (!isFinishing()) {
            refreshTitleView("绑定手机");
            fragmentTransaction.replace(R.id.revise_user_content, modifyBindingFragment);

            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    @Override
    public void showModifyPasswordFragment() {
        if (fragmentManager == null) {
            fragmentManager = getSupportFragmentManager();
        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        hideFragment(fragmentTransaction);

        checkSaveState(false);

        if (modifyPasswordFragment == null) {
            modifyPasswordFragment = new ModifyPasswordFragment();
            modifyPasswordFragment.setReviseUserListener(this);
        } else {
            fragmentTransaction.show(modifyPasswordFragment);
        }

        if (!isFinishing()) {
            refreshTitleView("修改密码");
            fragmentTransaction.replace(R.id.revise_user_content, modifyPasswordFragment);

            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    @Override
    public void showModifyQQFragment() {
        if (fragmentManager == null) {
            fragmentManager = getSupportFragmentManager();
        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        hideFragment(fragmentTransaction);

        checkSaveState(false);

        if (modifyQQFragment == null) {
            modifyQQFragment = new ModifyQQFragment();
            modifyQQFragment.setReviseUserListener(this);
        } else {
            fragmentTransaction.show(modifyQQFragment);
        }

        if (!isFinishing()) {
            refreshTitleView("修改QQ号");
            fragmentTransaction.replace(R.id.revise_user_content, modifyQQFragment);

            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    @Override
    public void modifyTelephoneNumber(String telephone_number, String verification_code, String password) {
        if (reviseUserPresenter == null) {
            reviseUserPresenter = new ReviseUserPresenter(this, getApplicationContext());
        }
        reviseUserPresenter.reviseUserTelephoneNumber(telephone_number, verification_code, password);
    }

    @Override
    public void modifyPassword(String ancient_password, String telephone_number, String verification_code, String fresh_password) {
        if (reviseUserPresenter == null) {
            reviseUserPresenter = new ReviseUserPresenter(this, getApplicationContext());
        }
        reviseUserPresenter.reviseUserPassword(ancient_password, telephone_number, verification_code, fresh_password);
    }

    @Override
    public void modifyQQ(String qq) {
        if (reviseUserPresenter == null) {
            reviseUserPresenter = new ReviseUserPresenter(this, getApplicationContext());
        }
        reviseUserPresenter.reviseUserQQ(qq);
    }

    @Override
    public void exitLogin() {
        if (reviseUserPresenter == null) {
            reviseUserPresenter = new ReviseUserPresenter(this, getApplicationContext());
        }
        reviseUserPresenter.exitLogin();
    }

    @OnClick({R.id.revise_head_back, R.id.revise_head_save})
    public void onClickListener(View view) {
        switch (view.getId()) {
            case R.id.revise_head_back:
                refreshView();
                finishActivity();
                break;
            case R.id.revise_head_save:
                if (modifyUserFragment != null) {
                    modifyUserFragment.checkUserPenName();
                }
                break;
        }
    }

    private void refreshView() {
        refreshPasswordView();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            refreshView();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void hideFragment(FragmentTransaction fragmentTransaction) {
        if (modifyUserFragment != null) {
            fragmentTransaction.hide(modifyUserFragment);
        }

        if (modifyNumberFragment != null) {
            fragmentTransaction.hide(modifyNumberFragment);
        }

        if (modifyBindingFragment != null) {
            fragmentTransaction.hide(modifyBindingFragment);
        }

        if (modifyPasswordFragment != null) {
            fragmentTransaction.hide(modifyPasswordFragment);
        }
    }

    private void checkSaveState(boolean flag) {
        if (revise_head_save != null) {
            revise_head_save.setVisibility(flag ? View.VISIBLE : View.GONE);
        }
    }
}
