package com.quduquxie.wxapi.view;

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
import com.quduquxie.util.QGLog;
import com.quduquxie.wxapi.RecoveredInterface;
import com.quduquxie.wxapi.WXEntryActivity;
import com.quduquxie.wxapi.listener.LandingListener;
import com.quduquxie.wxapi.presenter.RecoveredPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created on 16/11/30.
 * Created by crazylei.
 */

public class RecoveredFragment extends BaseFragment implements RecoveredInterface.View {

    private static final String TAG = RecoveredFragment.class.getSimpleName();

    private RecoveredInterface.Presenter recoveredPresenter;

    @BindView(R.id.recovered_telephone_number_input)
    public EditText recovered_telephone_number_input;
    @BindView(R.id.recovered_telephone_number_clear)
    public ImageView recovered_telephone_number_clear;
    @BindView(R.id.recovered_verification_code)
    public EditText recovered_verification_code;
    @BindView(R.id.recovered_verification_code_clear)
    public RelativeLayout recovered_verification_code_clear;
    @BindView(R.id.recovered_obtain_verification_code)
    public TextView recovered_obtain_verification_code;
    @BindView(R.id.recovered_password_input)
    public EditText recovered_password_input;
    @BindView(R.id.recovered_password_visible)
    public ImageView recovered_password_visible;
    @BindView(R.id.recovered_password_clear)
    public RelativeLayout recovered_password_clear;
    @BindView(R.id.recovered_complete)
    public TextView recovered_complete;

    private LandingListener landingListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        recoveredPresenter = new RecoveredPresenter(this, getContext());
        recoveredPresenter.init();
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
    protected View initLayout(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_recovered, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    protected void initView() {

        if (recovered_telephone_number_input != null) {
            recovered_telephone_number_input.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() != 0) {
                        if (recovered_telephone_number_clear.getVisibility() == View.INVISIBLE) {
                            recovered_telephone_number_clear.setVisibility(View.VISIBLE);
                        }
                    } else {
                        if (recovered_telephone_number_clear.getVisibility() == View.VISIBLE) {
                            recovered_telephone_number_clear.setVisibility(View.INVISIBLE);
                        }
                    }
                    if (recoveredPresenter != null) {
                        recoveredPresenter.checkRecoveredNextStepState("telephone_number", s.toString());
                    }
                }
            });
        }

        if (recovered_verification_code != null) {
            recovered_verification_code.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() != 0) {
                        if (recovered_verification_code_clear.getVisibility() == View.INVISIBLE) {
                            recovered_verification_code_clear.setVisibility(View.VISIBLE);
                        }
                    } else {
                        if (recovered_verification_code_clear.getVisibility() == View.VISIBLE) {
                            recovered_verification_code_clear.setVisibility(View.INVISIBLE);
                        }
                    }
                    if (recoveredPresenter != null) {
                        recoveredPresenter.checkRecoveredNextStepState("verification_code", s.toString());
                    }
                }
            });
        }

        if (recovered_password_input != null) {
            recovered_password_input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            recovered_password_input.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() != 0) {
                        if (recovered_password_clear.getVisibility() == View.INVISIBLE) {
                            recovered_password_clear.setVisibility(View.VISIBLE);
                        }
                    } else {
                        if (recovered_password_clear.getVisibility() == View.VISIBLE) {
                            recovered_password_clear.setVisibility(View.INVISIBLE);
                        }
                    }
                    if (recoveredPresenter != null) {
                        recoveredPresenter.checkRecoveredNextStepState("password", s.toString());
                    }
                }
            });
        }

        if (recovered_complete != null) {
            recovered_complete.setEnabled(false);
        }
    }

    @Override
    public void setPresenter(RecoveredInterface.Presenter recoveredPresenter) {
        this.recoveredPresenter = recoveredPresenter;
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void checkCompleteState(boolean state) {
        if (state) {
            recovered_complete.setEnabled(true);
        } else {
            recovered_complete.setEnabled(false);
        }
    }

    @Override
    public void showCountdownView(int second) {
        QGLog.e(TAG, "开始倒计时！");
        CountDownTimer countDownTimer = new CountDownTimer(second * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                recovered_obtain_verification_code.setText(millisUntilFinished / 1000 + "秒后重新获取");
            }

            @Override
            public void onFinish() {
                recovered_obtain_verification_code.setText("获取验证码");
                recovered_obtain_verification_code.setClickable(true);
            }
        };
        recovered_obtain_verification_code.setClickable(false);
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

    @OnClick({R.id.recovered_telephone_number_clear, R.id.recovered_obtain_verification_code, R.id.recovered_verification_code_clear, R.id.recovered_password_visible, R.id.recovered_password_clear, R.id.recovered_complete})
    public void onClickListener(View view) {
        switch (view.getId()) {
            case R.id.recovered_telephone_number_clear:
                if (recovered_telephone_number_input != null) {
                    recovered_telephone_number_input.setText("");
                    recovered_telephone_number_input.setSelection(0);
                }
                break;
            case R.id.recovered_obtain_verification_code:
                recoveredPresenter.obtainVerificationCode(recovered_telephone_number_input.getText().toString());
                break;
            case R.id.recovered_verification_code_clear:
                if (recovered_verification_code != null) {
                    recovered_verification_code.setText("");
                    recovered_verification_code.setSelection(0);
                }
                break;
            case R.id.recovered_password_visible:
                if (recovered_password_input.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                    recovered_password_input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    recovered_password_input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                Editable editable = recovered_password_input.getText();
                Selection.setSelection(editable, editable.length());

                changePasswordVisibleState();
                break;
            case R.id.recovered_password_clear:
                if (recovered_password_input != null) {
                    recovered_password_input.setText("");
                    recovered_password_input.setSelection(0);
                }
                break;
            case R.id.recovered_complete:
                hideSoftInput(view);

                String telephone_number = recovered_telephone_number_input.getText().toString();
                String verification_code = recovered_verification_code.getText().toString();
                String password = recovered_password_input.getText().toString();

                if (recoveredPresenter.verificationInformation(telephone_number, verification_code, password)) {
                    if (landingListener != null) {
                        landingListener.recoveredUserPassword(telephone_number, verification_code, password);
                    }
                }
                break;
        }
    }

    public void setLandingListener(LandingListener landingListener) {
        this.landingListener = landingListener;
    }

    private void changePasswordVisibleState() {
        if (recovered_password_input != null && recovered_password_visible != null) {
            if (recovered_password_input.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                recovered_password_visible.setImageResource(R.drawable.icon_password_invisible);
            } else {
                recovered_password_visible.setImageResource(R.drawable.icon_password_visible);
            }
        }
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
