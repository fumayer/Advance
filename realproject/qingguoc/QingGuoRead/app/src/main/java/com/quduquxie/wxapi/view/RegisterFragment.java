package com.quduquxie.wxapi.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.Html;
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
import com.quduquxie.module.agreement.view.AgreementActivity;
import com.quduquxie.util.QGLog;
import com.quduquxie.wxapi.RegisterInterface;
import com.quduquxie.wxapi.listener.LandingListener;
import com.quduquxie.wxapi.presenter.RegisterPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created on 16/11/30.
 * Created by crazylei.
 */

public class RegisterFragment extends BaseFragment implements RegisterInterface.View {

    private static final String TAG = RegisterFragment.class.getSimpleName();

    private RegisterInterface.Presenter registerPresenter;

    @BindView(R.id.register_telephone_number_clear)
    public ImageView register_telephone_number_clear;
    @BindView(R.id.register_telephone_number_input)
    public EditText register_telephone_number_input;
    @BindView(R.id.register_verification_code)
    public EditText register_verification_code;
    @BindView(R.id.register_verification_code_clear)
    public RelativeLayout register_verification_code_clear;
    @BindView(R.id.register_obtain_verification_code)
    public TextView register_obtain_verification_code;
    @BindView(R.id.register_password_input)
    public EditText register_password_input;
    @BindView(R.id.register_password_clear)
    public RelativeLayout register_password_clear;
    @BindView(R.id.register_password_visible)
    public ImageView register_password_visible;
    @BindView(R.id.register_next_step)
    public TextView register_next_step;
    @BindView(R.id.register_user_agreement)
    public TextView register_user_agreement;

    private LandingListener landingListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        registerPresenter = new RegisterPresenter(this, getContext());
        registerPresenter.init();
    }

    @Override
    public void onResume() {
        super.onResume();
        StatService.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        StatService.onPause(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void setPresenter(RegisterInterface.Presenter registerPresenter) {
        this.registerPresenter = registerPresenter;
    }

    @Override
    protected View initLayout(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_register, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void initView() {
        if (register_telephone_number_input != null) {
            register_telephone_number_input.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() != 0) {
                        if (register_telephone_number_clear.getVisibility() == View.INVISIBLE) {
                            register_telephone_number_clear.setVisibility(View.VISIBLE);
                        }
                    } else {
                        if (register_telephone_number_clear.getVisibility() == View.VISIBLE) {
                            register_telephone_number_clear.setVisibility(View.INVISIBLE);
                        }
                    }
                    if (registerPresenter != null) {
                        registerPresenter.checkRegisterNextStepState("telephone_number", s.toString());
                    }
                }
            });
        }

        if (register_verification_code != null) {
            register_verification_code.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() != 0) {
                        if (register_verification_code_clear.getVisibility() == View.INVISIBLE) {
                            register_verification_code_clear.setVisibility(View.VISIBLE);
                        }
                    } else {
                        if (register_verification_code_clear.getVisibility() == View.VISIBLE) {
                            register_verification_code_clear.setVisibility(View.INVISIBLE);
                        }
                    }
                    if (registerPresenter != null) {
                        registerPresenter.checkRegisterNextStepState("verification_code", s.toString());
                    }
                }
            });
        }

        if (register_password_input != null) {
            register_password_input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            register_password_input.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() != 0) {
                        if (register_password_clear.getVisibility() == View.INVISIBLE) {
                            register_password_clear.setVisibility(View.VISIBLE);
                        }
                    } else {
                        if (register_password_clear.getVisibility() == View.VISIBLE) {
                            register_password_clear.setVisibility(View.INVISIBLE);
                        }
                    }
                    if (registerPresenter != null) {
                        registerPresenter.checkRegisterNextStepState("password", s.toString());
                    }
                }
            });
        }

        if (register_next_step != null) {
            register_next_step.setEnabled(false);
        }

        if (register_user_agreement != null) {
            register_user_agreement.setText(Html.fromHtml("<font color=\"#6e6e6e\">点击下一步即代表您同意</font><font color=\"#4d91d0\">《青果用户使用协议》</font>"));
        }

        changePasswordVisibleState();
    }

    @OnClick({R.id.register_telephone_number_clear, R.id.register_obtain_verification_code, R.id.register_verification_code_clear, R.id.register_password_visible, R.id.register_password_clear, R.id.register_next_step, R.id.register_user_agreement})
    public void onClickListener(View view) {
        switch (view.getId()) {
            case R.id.register_telephone_number_clear:
                if (register_telephone_number_input != null) {
                    register_telephone_number_input.setText("");
                    register_telephone_number_input.setSelection(0);
                }
                break;
            case R.id.register_obtain_verification_code:
                    registerPresenter.obtainVerificationCode(register_telephone_number_input.getText().toString());
                break;
            case R.id.register_verification_code_clear:
                if (register_verification_code != null) {
                    register_verification_code.setText("");
                    register_verification_code.setSelection(0);
                }
                break;
            case R.id.register_password_visible:
                if (register_password_input.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                    register_password_input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    register_password_input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                Editable editable = register_password_input.getText();
                Selection.setSelection(editable, editable.length());

                changePasswordVisibleState();
                break;
            case R.id.register_password_clear:
                if (register_password_input != null) {
                    register_password_input.setText("");
                    register_password_input.setSelection(0);
                }
                break;
            case R.id.register_next_step:
                hideSoftInput(view);

                String telephone_number = register_telephone_number_input.getText().toString();
                String verification_code = register_verification_code.getText().toString();
                String password = register_password_input.getText().toString();

                if (registerPresenter.verificationInformation(telephone_number, verification_code, password)) {
                    if (landingListener != null) {
                        landingListener.registerUserInformation(telephone_number, verification_code, password);
                    }
                }
                break;
            case R.id.register_user_agreement:
                startActivity(new Intent(getContext(), AgreementActivity.class));
                break;
        }
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void checkNextStepState(boolean state) {
        if (state) {
            register_next_step.setEnabled(true);
        } else {
            register_next_step.setEnabled(false);
        }
    }

    @Override
    public void showCountdownView(int second) {
        QGLog.e(TAG, "开始倒计时！");
        CountDownTimer countDownTimer = new CountDownTimer(second * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                register_obtain_verification_code.setText(millisUntilFinished / 1000 + "秒后重新获取");
            }

            @Override
            public void onFinish() {
                register_obtain_verification_code.setText("获取验证码");
                register_obtain_verification_code.setClickable(true);
            }
        };
        register_obtain_verification_code.setClickable(false);
        countDownTimer.start();
    }

    private void changePasswordVisibleState() {
        if (register_password_input != null && register_password_visible != null) {
            if (register_password_input.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                register_password_visible.setImageResource(R.drawable.icon_password_invisible);
            } else {
                register_password_visible.setImageResource(R.drawable.icon_password_visible);
            }
        }
    }

    public void setLandingListener(LandingListener landingListener) {
        this.landingListener = landingListener;
    }

    public void hideSoftInput(final View view) {
        if (view == null || view.getContext() == null)
            return;
        InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive()) {
            inputMethodManager.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
        }
    }
}
