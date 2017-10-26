package com.quduquxie.revise.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.quduquxie.BaseFragment;
import com.quduquxie.R;
import com.quduquxie.revise.ModifyQQInterface;
import com.quduquxie.revise.listener.ReviseUserListener;
import com.quduquxie.revise.presenter.ModifyQQPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created on 16/12/5.
 * Created by crazylei.
 */

public class ModifyQQFragment extends BaseFragment implements ModifyQQInterface.View {

    private static final String TAG = ModifyQQFragment.class.getSimpleName();

    private ModifyQQInterface.Presenter modifyQQPresenter;

    @BindView(R.id.modify_qq_clear)
    public ImageView modify_qq_clear;
    @BindView(R.id.modify_qq_input)
    public EditText modify_qq_input;

    @BindView(R.id.modify_qq_complete)
    public TextView modify_qq_complete;

    private ReviseUserListener reviseUserListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        modifyQQPresenter = new ModifyQQPresenter(this, getContext());
        modifyQQPresenter.init();
    }

    @Override
    protected View initLayout(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_modify_qq, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    protected void initView() {

        if (modify_qq_input != null) {
            modify_qq_input.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() != 0) {
                        if (modify_qq_clear.getVisibility() == View.INVISIBLE) {
                            modify_qq_clear.setVisibility(View.VISIBLE);
                        }
                    } else {
                        if (modify_qq_clear.getVisibility() == View.VISIBLE) {
                            modify_qq_clear.setVisibility(View.INVISIBLE);
                        }
                    }
                }
            });
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
    public void onDestroy() {
        super.onDestroy();
    }

    @OnClick({R.id.modify_qq_clear, R.id.modify_qq_complete})
    public void onClickListener(View view) {
        switch (view.getId()) {
            case R.id.modify_qq_clear:
                if (modify_qq_input != null) {
                    modify_qq_input.setText("");
                    modify_qq_input.setSelection(0);
                }
                break;

            case R.id.modify_qq_complete:
                hideSoftInput(view);

                String qq = modify_qq_input.getText().toString();

                if (TextUtils.isEmpty(qq)) {
                    showToast("QQ号为空，请填写正确的QQ号！");
                } else {
                    if (reviseUserListener != null) {
                        reviseUserListener.modifyQQ(qq);
                    }
                }
                break;
        }
    }

    @Override
    public void setPresenter(ModifyQQInterface.Presenter modifyQQPresenter) {
        this.modifyQQPresenter = modifyQQPresenter;
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
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

    public void refreshView() {
        if (modify_qq_input != null) {
            modify_qq_input.setText("");
        }
    }
}
