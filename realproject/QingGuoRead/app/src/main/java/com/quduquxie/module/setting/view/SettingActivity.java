package com.quduquxie.module.setting.view;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.quduquxie.R;
import com.quduquxie.application.QuApplication;
import com.quduquxie.base.bean.Update;
import com.quduquxie.base.util.TypefaceUtil;
import com.quduquxie.communal.dialog.CustomDialogUpdateFragment;
import com.quduquxie.communal.dialog.CustomDialogUpdateListener;
import com.quduquxie.communal.widget.CustomProgressDialog;
import com.quduquxie.communal.widget.TextCheckView;
import com.quduquxie.module.about.view.AboutActivity;
import com.quduquxie.module.agreement.view.AgreementActivity;
import com.quduquxie.module.copyright.view.CopyrightActivity;
import com.quduquxie.module.setting.SettingInterface;
import com.quduquxie.module.setting.presenter.SettingPresenter;
import com.quduquxie.util.UpdateDialogUtil;
import com.quduquxie.view.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created on 17/2/15.
 * Created by crazylei.
 */

public class SettingActivity extends BaseActivity implements SettingInterface.View {

    @BindView(R.id.common_head_back)
    public ImageView common_head_back;
    @BindView(R.id.common_head_title)
    public TextView common_head_title;
    @BindView(R.id.setting_push)
    public TextCheckView setting_push;
    @BindView(R.id.setting_feedback)
    public TextView setting_feedback;
    @BindView(R.id.setting_check_update)
    public RelativeLayout setting_check_update;
    @BindView(R.id.setting_check_update_text)
    public TextView setting_check_update_text;
    @BindView(R.id.setting_check_update_version)
    public TextView setting_check_update_version;
    @BindView(R.id.setting_market)
    public TextView setting_market;
    @BindView(R.id.setting_about_us)
    public TextView setting_about_us;
    @BindView(R.id.setting_agreement)
    public TextView setting_agreement;
    @BindView(R.id.setting_copyright)
    public TextView setting_copyright;

    private SettingInterface.Presenter settingPresenter;

    private CustomProgressDialog customProgressDialog;

    private CustomDialogUpdateFragment customDialogUpdateFragment;

    private Toast toast;

    private Typeface typeface_song_depict;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.layout_activity_setting);
        } catch (Resources.NotFoundException exception) {
            collectException(exception);
            exception.printStackTrace();
        }

        ButterKnife.bind(this);

        typeface_song_depict = TypefaceUtil.loadTypeface(this, TypefaceUtil.TYPEFACE_SONG_DEPICT);

        settingPresenter = new SettingPresenter(this, this);
        settingPresenter.initParameter();

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
    public void setPresenter(SettingInterface.Presenter settingPresenter) {
        this.settingPresenter = settingPresenter;
    }

    @Override
    public void initView() {
        if (common_head_title != null) {
            common_head_title.setText(R.string.setting);
            common_head_title.setTypeface(typeface_song_depict);
        }
    }

    @Override
    public void initPushState(boolean check) {
        if (setting_push != null) {
            setting_push.setChecked(check);
        }
    }

    @Override
    public void setVersionName(String version) {
        if (setting_check_update_version != null) {
            setting_check_update_version.setText(version);
        }
    }

    @Override
    public void showProgressLoading() {
        if (customProgressDialog == null) {
            customProgressDialog = new CustomProgressDialog(SettingActivity.this);
            TextView progress_prompt = (TextView) customProgressDialog.findViewById(R.id.progress_prompt);
            progress_prompt.setText("正在检查更新...");
        } else {
            TextView progress_prompt = (TextView) customProgressDialog.findViewById(R.id.progress_prompt);
            if (progress_prompt != null) {
                progress_prompt.setText("正在检查更新...");
            }
        }

        if (!isFinishing() && customProgressDialog != null && !customProgressDialog.isShowing()) {
            customProgressDialog.show();
        }
    }

    @Override
    public void hideProgressLoading() {
        if (!isFinishing() && customProgressDialog != null && customProgressDialog.isShowing()) {
            customProgressDialog.dismiss();
        }
    }

    @Override
    public void showToastMessage(String message) {
        showToast(message);
    }

    @Override
    public void showUpdateInformation(final Update update) {
        if (customDialogUpdateFragment == null) {
            customDialogUpdateFragment = new CustomDialogUpdateFragment();
        }

        String[] descriptions = update.desc.split(";");
        String desc = "";
        for (int i = 0; i < descriptions.length; i++) {
            if (i != descriptions.length - 1) {
                desc += descriptions[i] + "\n\n";
            } else {
                desc += descriptions[i];
            }
        }

        customDialogUpdateFragment.setVersion(update.name);
        customDialogUpdateFragment.setDescription(desc);
        customDialogUpdateFragment.setCustomDialogUpdateListener(new CustomDialogUpdateListener() {
            @Override
            public void onCancelClicked() {
                hideCustomDialogFragment();
            }

            @Override
            public void onConfirmClicked() {
                if (customDialogUpdateFragment.getShowsDialog()) {
                    hideCustomDialogFragment();
                    UpdateDialogUtil.autoUpdateDownload(SettingActivity.this, update, false, true);
                }
            }
        });

        if (!isFinishing()) {
            if (customDialogUpdateFragment.isAdded()) {
                customDialogUpdateFragment.setShowsDialog(true);
            } else {
                customDialogUpdateFragment.show(getSupportFragmentManager(), "CustomDialogUpdateFragment");
            }
        }

    }

    public void hideCustomDialogFragment() {
        if (!isFinishing() && customDialogUpdateFragment != null && customDialogUpdateFragment.getShowsDialog()) {
            customDialogUpdateFragment.dismiss();
        }
    }

    @OnClick({R.id.common_head_back, R.id.setting_push, R.id.setting_feedback, R.id.setting_check_update, R.id.setting_market, R.id.setting_about_us, R.id.setting_agreement, R.id.setting_copyright})
    public void onClickListener(View view) {
        switch (view.getId()) {
            case R.id.common_head_back:
                finish();
                break;
            case R.id.setting_push:
                settingPresenter.savePushState();
                break;
            case R.id.setting_feedback:
                String message = settingPresenter.openFeedbackActivity();
                if (!TextUtils.isEmpty(message)) {
                    showToast(message);
                }
                break;
            case R.id.setting_check_update:
                settingPresenter.checkUpdate();
                break;
            case R.id.setting_market:
                try {
                    Uri uri = Uri.parse("market://details?id=" + QuApplication.getQuPackageName());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } catch (Exception exception) {
                    showToast("本地未找到应用市场,请下载市场后再评价,谢谢!");
                    collectException(exception);
                }
                break;
            case R.id.setting_about_us:
                startActivity(new Intent(SettingActivity.this, AboutActivity.class));
                break;
            case R.id.setting_agreement:
                startActivity(new Intent(SettingActivity.this, AgreementActivity.class));
                break;
            case R.id.setting_copyright:
                startActivity(new Intent(SettingActivity.this, CopyrightActivity.class));
                break;
        }
    }

    private void showToast(String message) {
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
}