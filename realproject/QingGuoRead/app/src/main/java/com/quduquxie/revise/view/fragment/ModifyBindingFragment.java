package com.quduquxie.revise.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.quduquxie.BaseFragment;
import com.quduquxie.R;
import com.quduquxie.communal.utils.UserUtil;
import com.quduquxie.revise.ModifyBindingInterface;
import com.quduquxie.revise.listener.ReviseUserListener;
import com.quduquxie.revise.presenter.ModifyBindingPresenter;
import com.quduquxie.util.QGLog;
import com.quduquxie.wxapi.WXEntryActivity;

import java.text.MessageFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created on 16/12/5.
 * Created by crazylei.
 */

public class ModifyBindingFragment extends BaseFragment implements ModifyBindingInterface.View {

    private static final String TAG = ModifyBindingFragment.class.getSimpleName();

    private ModifyBindingInterface.Presenter modifyBindingPresenter;

    @BindView(R.id.modify_binding_clear)
    public ImageView modify_binding_clear;
    @BindView(R.id.modify_binding_input)
    public EditText modify_binding_input;

    @BindView(R.id.modify_binding_obtain_verification_code)
    public TextView modify_binding_obtain_verification_code;
    @BindView(R.id.modify_binding_verification_code)
    public EditText modify_binding_verification_code;
    @BindView(R.id.modify_binding_verification_code_clear)
    public RelativeLayout modify_binding_verification_code_clear;
    
    @BindView(R.id.modify_binding_password_visible)
    public ImageView modify_binding_password_visible;
    @BindView(R.id.modify_binding_password_clear)
    public RelativeLayout modify_binding_password_clear;
    @BindView(R.id.modify_binding_password_input)
    public EditText modify_binding_password_input;
   
    @BindView(R.id.modify_binding_complete)
    public TextView modify_binding_complete;

    private ReviseUserListener reviseUserListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        modifyBindingPresenter = new ModifyBindingPresenter(this, getContext());
        modifyBindingPresenter.init();
    }

    @Override
    protected View initLayout(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_modify_binding, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    protected void initView() {

        if (modify_binding_input != null) {
            modify_binding_input.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() != 0) {
                        if (modify_binding_clear.getVisibility() == View.INVISIBLE) {
                            modify_binding_clear.setVisibility(View.VISIBLE);
                        }
                    } else {
                        if (modify_binding_clear.getVisibility() == View.VISIBLE) {
                            modify_binding_clear.setVisibility(View.INVISIBLE);
                        }
                    }
                    if (modifyBindingPresenter != null) {
                        modifyBindingPresenter.checkModifyCompleteState("telephone_number", s.toString());
                    }
                }
            });
        }

        if (modify_binding_verification_code != null) {
            modify_binding_verification_code.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() != 0) {
                        if (modify_binding_verification_code_clear.getVisibility() == View.INVISIBLE) {
                            modify_binding_verification_code_clear.setVisibility(View.VISIBLE);
                        }
                    } else {
                        if (modify_binding_verification_code_clear.getVisibility() == View.VISIBLE) {
                            modify_binding_verification_code_clear.setVisibility(View.INVISIBLE);
                        }
                    }
                    if (modifyBindingPresenter != null) {
                        modifyBindingPresenter.checkModifyCompleteState("verification_code", s.toString());
                    }
                }
            });
        }

        if (modify_binding_password_input != null) {
            modify_binding_password_input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            modify_binding_password_input.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() != 0) {
                        if (modify_binding_password_clear.getVisibility() == View.INVISIBLE) {
                            modify_binding_password_clear.setVisibility(View.VISIBLE);
                        }
                    } else {
                        if (modify_binding_password_clear.getVisibility() == View.VISIBLE) {
                            modify_binding_password_clear.setVisibility(View.INVISIBLE);
                        }
                    }
                    if (modifyBindingPresenter != null) {
                        modifyBindingPresenter.checkModifyCompleteState("password", s.toString());
                    }
                }
            });
        }

        if (modify_binding_complete != null) {
            modify_binding_complete.setEnabled(false);
        }

        QGLog.e(TAG, "ModifyNumberFragment : initView");

        changePasswordVisibleState();
    }

    @Override
    public void onResume() {
        super.onResume();
        StatService.onResume(getContext());
    }

    @Override
    public void onPause() {
        super.onPause();
        StatService.onPause(getContext());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @OnClick({R.id.modify_binding_password_visible, R.id.modify_binding_password_clear, R.id.modify_binding_clear, R.id.modify_binding_verification_code_clear, R.id.modify_binding_obtain_verification_code, R.id.modify_binding_complete})
    public void onClickListener(View view) {
        switch (view.getId()) {
            case R.id.modify_binding_password_visible:
                if (modify_binding_password_input.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                    modify_binding_password_input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    modify_binding_password_input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                Editable editable = modify_binding_password_input.getText();
                Selection.setSelection(editable, editable.length());

                changePasswordVisibleState();
                break;
            case R.id.modify_binding_password_clear:
                if (modify_binding_password_input != null) {
                    modify_binding_password_input.setText("");
                    modify_binding_password_input.setSelection(0);
                }
                break;
            case R.id.modify_binding_clear:
                if (modify_binding_input != null) {
                    modify_binding_input.setText("");
                    modify_binding_input.setSelection(0);
                }
                break;
            case R.id.modify_binding_verification_code_clear:
                if (modify_binding_verification_code != null) {
                    modify_binding_verification_code.setText("");
                    modify_binding_verification_code.setSelection(0);
                }
                break;
            case R.id.modify_binding_obtain_verification_code:
                modifyBindingPresenter.obtainVerificationCode(modify_binding_input.getText().toString());
                break;
            case R.id.modify_binding_complete:
                hideSoftInput(view);

                String telephone_number = modify_binding_input.getText().toString();
                String verification_code = modify_binding_verification_code.getText().toString();
                String password = modify_binding_password_input.getText().toString();

                if (modifyBindingPresenter.verificationInformation(telephone_number, verification_code, password)) {
                    if (reviseUserListener != null) {
                        reviseUserListener.modifyTelephoneNumber(telephone_number, verification_code, password);
                    }
                }
                break;
        }
    }

    @Override
    public void setPresenter(ModifyBindingInterface.Presenter modifyBindingPresenter) {
        this.modifyBindingPresenter = modifyBindingPresenter;
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void checkCompleteState(boolean state) {
        if (state) {
            modify_binding_complete.setEnabled(true);
        } else {
            modify_binding_complete.setEnabled(false);
        }
    }

    @Override
    public void showCountdownView(int second) {
        QGLog.e(TAG, "开始倒计时！");
        CountDownTimer countDownTimer = new CountDownTimer(second * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                modify_binding_obtain_verification_code.setText(MessageFormat.format("{0}s", millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                QGLog.e(TAG, "onFinish");
                modify_binding_obtain_verification_code.setText("获取验证码");
                modify_binding_obtain_verification_code.setClickable(true);
            }
        };
        modify_binding_obtain_verification_code.setClickable(false);

        countDownTimer.start();
    }

    @Override
    public void startLoginActivity() {
        UserUtil.deleteUser(getContext());
        Intent intent = new Intent();
        intent.putExtra("exit_login", true);
        intent.setClass(getContext(), WXEntryActivity.class);
        startActivity(intent);
    }

    private void changePasswordVisibleState() {
        if (modify_binding_password_input != null && modify_binding_password_visible != null) {
            if (modify_binding_password_input.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                modify_binding_password_visible.setImageResource(R.drawable.icon_password_invisible);
            } else {
                modify_binding_password_visible.setImageResource(R.drawable.icon_password_visible);
            }
        }
    }

    public void setReviseUserListener(ReviseUserListener reviseUserListener) {
        this.reviseUserListener = reviseUserListener;
    }

    public void hideSoftInput(final View view) {
        if (view == null || view.getContext() == null) {
            return;
        }
        InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive()) {
            inputMethodManager.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
        }
    }
}
