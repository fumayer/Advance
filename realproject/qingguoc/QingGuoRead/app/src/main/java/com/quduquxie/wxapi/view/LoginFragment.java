package com.quduquxie.wxapi.view;

import android.content.Context;
import android.os.Bundle;
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
import com.quduquxie.wxapi.LoginInterface;
import com.quduquxie.wxapi.listener.LandingListener;
import com.quduquxie.wxapi.presenter.LoginPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created on 16/12/6.
 * Created by crazylei.
 */

public class LoginFragment extends BaseFragment implements LoginInterface.View {

    private LoginInterface.Presenter loginPresenter;

    @BindView(R.id.login_telephone_number_input)
    public EditText login_telephone_number_input;
    @BindView(R.id.login_telephone_number_clear)
    public ImageView login_telephone_number_clear;
    @BindView(R.id.login_password_input)
    public EditText login_password_input;
    @BindView(R.id.login_password_clear)
    public RelativeLayout login_password_clear;
    @BindView(R.id.login_password_visible)
    public ImageView login_password_visible;
    @BindView(R.id.login_landing)
    public TextView login_landing;
    @BindView(R.id.login_register_immediately)
    public TextView login_register_immediately;
    @BindView(R.id.login_forget_password)
    public TextView login_forget_password;
    @BindView(R.id.login_other_method)
    public TextView login_other_method;
    @BindView(R.id.login_landing_weixin)
    public TextView login_landing_weixin;
    @BindView(R.id.login_landing_qq)
    public TextView login_landing_qq;

    private LandingListener landingListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        loginPresenter = new LoginPresenter(this, getContext());
    }

    @Override
    protected View initLayout(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_login, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    protected void initView() {
        if (login_telephone_number_input != null) {
            login_telephone_number_input.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() != 0) {
                        if (login_telephone_number_clear.getVisibility() == View.INVISIBLE) {
                            login_telephone_number_clear.setVisibility(View.VISIBLE);
                        }
                    } else {
                        if (login_telephone_number_clear.getVisibility() == View.VISIBLE) {
                            login_telephone_number_clear.setVisibility(View.INVISIBLE);
                        }
                    }
                    if (loginPresenter != null) {
                        loginPresenter.checkLandingState("telephone_number", s.toString());
                    }
                }
            });
        }

        if (login_password_input != null) {
            login_password_input.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() != 0) {
                        if (login_password_clear.getVisibility() == View.INVISIBLE) {
                            login_password_clear.setVisibility(View.VISIBLE);
                        }
                    } else {
                        if (login_password_clear.getVisibility() == View.VISIBLE) {
                            login_password_clear.setVisibility(View.INVISIBLE);
                        }
                    }

                    if (loginPresenter != null) {
                        loginPresenter.checkLandingState("password", s.toString());
                    }
                }
            });

            login_password_input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }

        if (login_landing != null) {
            login_landing.setEnabled(false);
        }
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
    public void setPresenter(LoginInterface.Presenter loginPresenter) {
        this.loginPresenter = loginPresenter;
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void changeLoginViewState(boolean state) {
        if (state) {
            login_landing.setEnabled(true);
        } else {
            login_landing.setEnabled(false);
        }
    }

    @OnClick({R.id.login_telephone_number_clear, R.id.login_password_clear, R.id.login_password_visible, R.id.login_landing, R.id.login_register_immediately, R.id.login_forget_password, R.id.login_landing_weixin, R.id.login_landing_qq})
    public void onClickListener(View view) {
        switch (view.getId()) {
            case R.id.login_telephone_number_clear:
                if (login_telephone_number_input != null) {
                    login_telephone_number_input.setText("");
                    login_telephone_number_input.setSelection(0);
                }
                break;
            case R.id.login_password_clear:
                if (login_password_input != null) {
                    login_password_input.setText("");
                    login_password_input.setSelection(0);
                }
                break;
            case R.id.login_password_visible:
                if (login_password_input.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                    login_password_input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    login_password_input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                Editable editable = login_password_input.getText();
                Selection.setSelection(editable, editable.length());

                changePasswordVisibleState();

                break;
            case R.id.login_landing:
                hideSoftInput(view);

                String telephone_number = login_telephone_number_input.getText().toString();
                String password = login_password_input.getText().toString();

                if (loginPresenter.verificationInformation(telephone_number, password)) {
                    if (landingListener != null) {
                        landingListener.landing(telephone_number, password);
                    }
                }
                break;
            case R.id.login_register_immediately:
                if (landingListener != null) {
                    landingListener.showRegisterFragment();
                }
                break;
            case R.id.login_forget_password:
                if (landingListener != null) {
                    landingListener.showRecoveredFragment();
                }
                break;
            case R.id.login_landing_weixin:
                if (landingListener != null) {
                    landingListener.landingWidthWeChat();
                }
                break;
            case R.id.login_landing_qq:
                if (landingListener != null) {
                    landingListener.landingWidthQQ();
                }
                break;
        }
    }

    private void changePasswordVisibleState() {
        if (login_password_input != null && login_password_visible != null) {
            if (login_password_input.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                login_password_visible.setImageResource(R.drawable.icon_password_invisible);
            } else {
                login_password_visible.setImageResource(R.drawable.icon_password_visible);
            }
        }
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

    public void setLandingListener(LandingListener landingListener) {
        this.landingListener = landingListener;
    }
}
