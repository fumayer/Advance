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
import com.quduquxie.revise.ModifyPasswordInterface;
import com.quduquxie.revise.listener.ReviseUserListener;
import com.quduquxie.revise.presenter.ModifyPasswordPresenter;
import com.quduquxie.util.QGLog;
import com.quduquxie.wxapi.WXEntryActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created on 16/12/5.
 * Created by crazylei.
 */

public class ModifyPasswordFragment extends BaseFragment implements ModifyPasswordInterface.View {

    private static final String TAG = ModifyPasswordFragment.class.getSimpleName();
    
    private ModifyPasswordInterface.Presenter modifyPasswordPresenter;

    @BindView(R.id.modify_ancient_password_visible)
    public ImageView modify_ancient_password_visible;
    @BindView(R.id.modify_ancient_password_clear)
    public RelativeLayout modify_ancient_password_clear;
    @BindView(R.id.modify_ancient_password_input)
    public EditText modify_ancient_password_input;
    @BindView(R.id.modify_password_number_clear)
    public ImageView modify_password_number_clear;
    @BindView(R.id.modify_password_number_input)
    public EditText modify_password_number_input;
    @BindView(R.id.modify_password_obtain_verification_code)
    public TextView modify_password_obtain_verification_code;
    @BindView(R.id.modify_password_verification_code_clear)
    public RelativeLayout modify_password_verification_code_clear;
    @BindView(R.id.modify_password_verification_code)
    public EditText modify_password_verification_code;
    @BindView(R.id.modify_password_visible)
    public ImageView modify_password_visible;
    @BindView(R.id.modify_password_clear)
    public RelativeLayout modify_password_clear;
    @BindView(R.id.modify_password_input)
    public EditText modify_password_input;
    @BindView(R.id.modify_password_complete)
    public TextView modify_password_complete;

    private ReviseUserListener reviseUserListener;

    private CountDownTimer countDownTimer;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        modifyPasswordPresenter = new ModifyPasswordPresenter(this, getContext());
        modifyPasswordPresenter.init();
    }

    @Override
    protected View initLayout(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_modify_password, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    protected void initView() {
        if (modify_ancient_password_input != null) {
            modify_ancient_password_input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            modify_ancient_password_input.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() != 0) {
                        if (modify_ancient_password_clear.getVisibility() == View.INVISIBLE) {
                            modify_ancient_password_clear.setVisibility(View.VISIBLE);
                        }
                    } else {
                        if (modify_ancient_password_clear.getVisibility() == View.VISIBLE) {
                            modify_ancient_password_clear.setVisibility(View.INVISIBLE);
                        }
                    }
                    if (modifyPasswordPresenter != null) {
                        modifyPasswordPresenter.checkModifyCompleteState("ancient_password", s.toString());
                    }
                }
            });
        }

        if (modify_password_number_input != null) {
            modify_password_number_input.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() != 0) {
                        if (modify_password_number_clear.getVisibility() == View.INVISIBLE) {
                            modify_password_number_clear.setVisibility(View.VISIBLE);
                        }
                    } else {
                        if (modify_password_number_clear.getVisibility() == View.VISIBLE) {
                            modify_password_number_clear.setVisibility(View.INVISIBLE);
                        }
                    }
                    if (modifyPasswordPresenter != null) {
                        modifyPasswordPresenter.checkModifyCompleteState("telephone_number", s.toString());
                    }
                }
            });
        }

        if (modify_password_verification_code != null) {
            modify_password_verification_code.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() != 0) {
                        if (modify_password_verification_code_clear.getVisibility() == View.INVISIBLE) {
                            modify_password_verification_code_clear.setVisibility(View.VISIBLE);
                        }
                    } else {
                        if (modify_password_verification_code_clear.getVisibility() == View.VISIBLE) {
                            modify_password_verification_code_clear.setVisibility(View.INVISIBLE);
                        }
                    }
                    if (modifyPasswordPresenter != null) {
                        modifyPasswordPresenter.checkModifyCompleteState("verification_code", s.toString());
                    }
                }
            });
        }

        if (modify_password_input != null) {
            modify_password_input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            modify_password_input.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() != 0) {
                        if (modify_password_clear.getVisibility() == View.INVISIBLE) {
                            modify_password_clear.setVisibility(View.VISIBLE);
                        }
                    } else {
                        if (modify_password_clear.getVisibility() == View.VISIBLE) {
                            modify_password_clear.setVisibility(View.INVISIBLE);
                        }
                    }
                    if (modifyPasswordPresenter != null) {
                        modifyPasswordPresenter.checkModifyCompleteState("fresh_password", s.toString());
                    }
                }
            });
        }

        if (modify_password_complete != null) {
            modify_password_complete.setEnabled(false);
        }

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

    @OnClick({R.id.modify_ancient_password_visible, R.id.modify_ancient_password_clear, R.id.modify_password_number_clear, R.id.modify_password_obtain_verification_code, R.id.modify_password_visible, R.id.modify_password_clear, R.id.modify_password_complete})
    public void onClickListener(View view) {
        switch (view.getId()) {
            case R.id.modify_ancient_password_visible:
                if (modify_ancient_password_input.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                    modify_ancient_password_input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    modify_ancient_password_input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                Editable ancient_editable = modify_ancient_password_input.getText();
                Selection.setSelection(ancient_editable, ancient_editable.length());

                changeAncientPasswordVisibleState();
                break;
            case R.id.modify_ancient_password_clear:
                if (modify_ancient_password_input != null) {
                    modify_ancient_password_input.setText("");
                    modify_ancient_password_input.setSelection(0);
                }
                break;
            case R.id.modify_password_number_clear:
                if (modify_password_number_input != null) {
                    modify_password_number_input.setText("");
                    modify_password_number_input.setSelection(0);
                }
                break;
            case R.id.modify_password_verification_code_clear:
                if (modify_password_verification_code != null) {
                    modify_password_verification_code.setText("");
                    modify_password_verification_code.setSelection(0);
                }
                break;
            case R.id.modify_password_obtain_verification_code:
                modifyPasswordPresenter.obtainVerificationCode(modify_password_number_input.getText().toString());
                break;
            case R.id.modify_password_visible:
                if (modify_password_input.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                    modify_password_input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    modify_password_input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                Editable editable = modify_password_input.getText();
                Selection.setSelection(editable, editable.length());

                changePasswordVisibleState();
                break;
            case R.id.modify_password_clear:
                if (modify_password_input != null) {
                    modify_password_input.setText("");
                    modify_password_input.setSelection(0);
                }
                break;
            case R.id.modify_password_complete:
                hideSoftInput(view);

                String ancient_password = modify_ancient_password_input.getText().toString();
                String telephone_number = modify_password_number_input.getText().toString();
                String verification_code = modify_password_verification_code.getText().toString();
                String fresh_password = modify_password_input.getText().toString();

                if (modifyPasswordPresenter.verificationInformation(ancient_password, telephone_number, verification_code, fresh_password)) {
                    if (reviseUserListener != null) {
                        reviseUserListener.modifyPassword(ancient_password, telephone_number, verification_code, fresh_password);
                    }
                }
                break;
        }
    }

    public void refreshView() {
        if (modify_ancient_password_input != null) {
            modify_ancient_password_input.setText("");
        }
        if (modify_password_number_input != null) {
            modify_password_number_input.setText("");
        }
        if (modify_password_verification_code != null) {
            modify_password_verification_code.setText("");
        }
        if (modify_password_input != null) {
            modify_password_input.setText("");
        }
        if (countDownTimer != null) {
            countDownTimer.onFinish();
        }
    }

    @Override
    public void setPresenter(ModifyPasswordInterface.Presenter modifyPasswordPresenter) {
        this.modifyPasswordPresenter = modifyPasswordPresenter;
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void checkCompleteState(boolean state) {
        if (state) {
            modify_password_complete.setEnabled(true);
        } else {
            modify_password_complete.setEnabled(false);
        }
    }

    @Override
    public void showCountdownView(int second) {
        QGLog.e(TAG, "开始倒计时！");
        countDownTimer = new CountDownTimer(second * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                modify_password_obtain_verification_code.setText(millisUntilFinished / 1000 + "秒后重新获取");
            }

            @Override
            public void onFinish() {
                modify_password_obtain_verification_code.setText("获取验证码");
                modify_password_obtain_verification_code.setClickable(true);
            }
        };
        modify_password_obtain_verification_code.setClickable(false);
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
        if (modify_password_input != null && modify_password_visible != null) {
            if (modify_password_input.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                modify_password_visible.setImageResource(R.drawable.icon_password_invisible);
            } else {
                modify_password_visible.setImageResource(R.drawable.icon_password_visible);
            }
        }
    }

    private void changeAncientPasswordVisibleState() {
        if (modify_ancient_password_input != null && modify_ancient_password_visible != null) {
            if (modify_ancient_password_input.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                modify_ancient_password_visible.setImageResource(R.drawable.icon_password_invisible);
            } else {
                modify_ancient_password_visible.setImageResource(R.drawable.icon_password_visible);
            }
        }
    }

    public void setReviseUserListener(ReviseUserListener reviseUserListener) {
        this.reviseUserListener = reviseUserListener;
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
