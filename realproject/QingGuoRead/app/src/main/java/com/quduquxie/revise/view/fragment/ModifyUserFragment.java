package com.quduquxie.revise.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.orhanobut.logger.Logger;
import com.quduquxie.BaseFragment;
import com.quduquxie.Constants;
import com.quduquxie.application.QuApplication;
import com.quduquxie.R;
import com.quduquxie.communal.crop.BitmapUtil;
import com.quduquxie.communal.crop.CropHandler;
import com.quduquxie.communal.crop.CropHelper;
import com.quduquxie.communal.crop.CropParams;
import com.quduquxie.communal.utils.UserUtil;
import com.quduquxie.communal.widget.CustomDialog;
import com.quduquxie.communal.widget.GlideCircleTransform;
import com.quduquxie.communal.widget.GlideRoundTransform;
import com.quduquxie.revise.ModifyUserInterface;
import com.quduquxie.revise.listener.ReviseUserListener;
import com.quduquxie.revise.presenter.ModifyUserPresenter;
import com.quduquxie.revise.view.ReviseUserActivity;
import com.quduquxie.widget.LoadingPage;
import com.quduquxie.wxapi.WXEntryActivity;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created on 16/12/5.
 * Created by crazylei.
 */

public class ModifyUserFragment extends BaseFragment implements ModifyUserInterface.View, CropHandler{

    private ModifyUserInterface.Presenter modifyUserPresenter;

    @BindView(R.id.modify_user_content)
    public RelativeLayout modify_user_content;
    @BindView(R.id.modify_user_avatar)
    public RelativeLayout modify_user_avatar;
    @BindView(R.id.modify_user_avatar_text)
    public TextView modify_user_avatar_text;
    @BindView(R.id.modify_user_avatar_image)
    public ImageView modify_user_avatar_image;
    @BindView(R.id.modify_user_penname)
    public RelativeLayout modify_user_penname;
    @BindView(R.id.modify_user_penname_text)
    public TextView modify_user_penname_text;
    @BindView(R.id.modify_user_penname_input)
    public EditText modify_user_penname_input;
    @BindView(R.id.modify_user_number)
    public RelativeLayout modify_user_number;
    @BindView(R.id.modify_user_number_text)
    public TextView modify_user_number_text;
    @BindView(R.id.modify_user_number_prompt)
    public TextView modify_user_number_prompt;
    @BindView(R.id.modify_user_binding_number)
    public TextView modify_user_binding_number;
    @BindView(R.id.modify_user_qq)
    public RelativeLayout modify_user_qq;
    @BindView(R.id.modify_user_qq_text)
    public TextView modify_user_qq_text;
    @BindView(R.id.modify_user_qq_prompt)
    public TextView modify_user_qq_prompt;
    @BindView(R.id.modify_user_qq_number)
    public TextView modify_user_qq_number;
    @BindView(R.id.modify_user_afresh_password)
    public RelativeLayout modify_user_afresh_password;
    @BindView(R.id.modify_user_afresh_password_text)
    public TextView modify_user_afresh_password_text;
    @BindView(R.id.modify_user_exit_login)
    public TextView modify_user_exit_login;

    private ReviseUserListener reviseUserListener;

    private String telephone_number = "";
    private String qq = "";

    private CustomDialog progressDialog;

    public CropParams cropParams;

    public static final int DEFAULT_ASPECT_X = 1;
    public static final int DEFAULT_ASPECT_Y = 1;
    public static final int DEFAULT_OUTPUT_X = 200;
    public static final int DEFAULT_OUTPUT_Y = 200;

    private String compile = "[^\\u4e00-\\u9fa50-9a-zA-Z_' ']";
    private Pattern pattern;

    private Toast toast;

    private LoadingPage loadingPage;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        modifyUserPresenter = new ModifyUserPresenter(this, getContext());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cropParams = new CropParams(getContext(), DEFAULT_ASPECT_X, DEFAULT_ASPECT_Y, DEFAULT_OUTPUT_X, DEFAULT_OUTPUT_Y);

    }

    @Override
    protected View initLayout(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_modify_user, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    protected void initView() {

        if (modify_user_penname_input != null) {
            modify_user_penname_input.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence sequence, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence sequence, int start, int before, int count) {
                    String content = sequence.toString();
                    if (nickNameFilter(content)) {
                        showToast("检测到昵称含有非法字符！");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            modify_user_penname_input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {

                    if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_SEND || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                        String pen_name = null;
                        if (modify_user_penname_input != null) {
                            pen_name = modify_user_penname_input.getText().toString();
                        }
                        if (pen_name != null && pen_name.trim().equals("")) {
                            Toast.makeText(getContext(), R.string.check_pen_name, Toast.LENGTH_SHORT).show();
                        } else {
                            hideSoftInput(view);
                            if (pen_name != null && !pen_name.equals("") && modifyUserPresenter != null) {
                                modifyUserPresenter.changePenName(pen_name, false);
                            }
                        }
                        return true;
                    }
                    return false;
                }
            });
        }
        if (modifyUserPresenter != null) {
            modifyUserPresenter.init();
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

    @Override
    public void setPresenter(ModifyUserInterface.Presenter modifyUserPresenter) {
        this.modifyUserPresenter = modifyUserPresenter;
    }

    public void showAvatarImage(String url) {
        if (!TextUtils.isEmpty(url)) {
            Glide.with(this.getContext())
                    .load(url)
                    .transform(new GlideCircleTransform(getContext()))
                    .signature(new StringSignature(url))
                    .error(R.drawable.icon_avatar_default)
                    .placeholder(R.drawable.icon_avatar_default)
                    .into(modify_user_avatar_image);
        } else {
            modify_user_avatar_image.setImageResource(R.drawable.icon_avatar_default);
        }
    }

    public void showPenName(String pen_name) {
        if (!TextUtils.isEmpty(pen_name)) {
            if (modify_user_penname_input != null) {
                modify_user_penname_input.setText(pen_name);
                modify_user_penname_input.setSelection(Math.min(pen_name.length(), 10));
            }
        } else {
            modify_user_penname_input.setHint(R.string.input_nickname);
        }
    }

    public void showBindingNumber(String telephone_number) {
        this.telephone_number = telephone_number;

        if (modify_user_binding_number != null) {
            if (!TextUtils.isEmpty(telephone_number)) {
                if (modify_user_afresh_password != null) {
                    modify_user_afresh_password.setVisibility(View.VISIBLE);
                }
                if (modify_user_binding_number != null) {
                    modify_user_binding_number.setText(telephone_number);
                    modify_user_binding_number.setVisibility(View.VISIBLE);
                }
                if (modify_user_number_prompt != null) {
                    modify_user_number_prompt.setText("重新绑定");
                }
            } else {
                if (modify_user_afresh_password != null) {
                    modify_user_afresh_password.setVisibility(View.GONE);
                }
                if (modify_user_binding_number != null) {
                    modify_user_binding_number.setVisibility(View.GONE);
                }
                if (modify_user_number_prompt != null) {
                    modify_user_number_prompt.setText("绑定手机号，保障账号安全");
                }
            }
        }
    }

    public void showQQNumber(String qq) {
        this.qq = qq;

        if (modify_user_qq != null) {
            if (!TextUtils.isEmpty(qq)) {
                if (modify_user_qq_number != null) {
                    modify_user_qq_number.setText(qq);
                    modify_user_qq_number.setVisibility(View.VISIBLE);
                }
                if (modify_user_qq_prompt != null) {
                    modify_user_qq_prompt.setText("修改QQ号");
                }
            } else {
                if (modify_user_qq_number != null) {
                    modify_user_qq_number.setVisibility(View.GONE);
                }
                if (modify_user_qq_prompt != null) {
                    modify_user_qq_prompt.setText("填写QQ号，实时与编辑沟通");
                }
            }
        }
    }

    @Override
    public void showToast(String message) {
        if (toast == null) {
            toast = Toast.makeText(this.getContext(), message, Toast.LENGTH_SHORT);
        } else {
            toast.setText(message);
        }
        if (toast != null)
            toast.show();
    }

    @Override
    public void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new CustomDialog(getActivity(), R.layout.layout_dialog_progress, Gravity.CENTER);
            TextView progress_prompt = (TextView) progressDialog.findViewById(R.id.progress_prompt);
            progress_prompt.setText("正在处理用户请求...");
        }
        if (!progressDialog.isShowing() && !getActivity().isFinishing()) {
            progressDialog.show();
        }
    }

    @Override
    public void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing() && !getActivity().isFinishing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void startLoginActivity() {
        UserUtil.deleteUser(getContext());
        Intent intent = new Intent();
        intent.putExtra("exit_login", true);
        intent.setClass(getContext(), WXEntryActivity.class);
        startActivity(intent);
    }

    @Override
    public void showLoadingPage() {
        if (loadingPage != null) {
            loadingPage.onSuccess();
        }

        if (loadingPage == null) {
            loadingPage = new LoadingPage(getActivity(), modify_user_content);
        }
        loadingPage.setReloadButton(false);
    }

    @Override
    public void hideLoadingPage() {
        if (loadingPage != null) {
            loadingPage.onSuccess();
        }
    }

    @Override
    public void showLoadingError() {
        if (loadingPage != null) {
            loadingPage.onError();
        }
    }

    @Override
    public void destroyActivity() {
        ((ReviseUserActivity) getActivity()).finishActivity();
    }

    @OnClick({R.id.modify_user_avatar, R.id.modify_user_number, R.id.modify_user_qq, R.id.modify_user_afresh_password, R.id.modify_user_exit_login})
    public void onClickListener(View view) {
        switch (view.getId()) {
            case R.id.modify_user_avatar:
                showAvatarDialog();
                break;
            case R.id.modify_user_number:
                if (reviseUserListener != null) {
                    if (TextUtils.isEmpty(telephone_number)) {
                        reviseUserListener.showModifyBindingFragment();
                    } else {
                        reviseUserListener.showModifyNumberFragment();
                    }
                }
                break;
            case R.id.modify_user_afresh_password:
                if (reviseUserListener != null) {
                    reviseUserListener.showModifyPasswordFragment();
                }
                break;
            case R.id.modify_user_qq:
                if (reviseUserListener != null) {
                    reviseUserListener.showModifyQQFragment();
                }
                break;
            case R.id.modify_user_exit_login:
                if (reviseUserListener != null) {
                    reviseUserListener.exitLogin();
                }
                break;
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

    private void showAvatarDialog() {
        final CustomDialog customDialog = new CustomDialog(getActivity(), R.layout.layout_dialog_user_avatar, Gravity.BOTTOM);

        TextView dialog_avatar_photo_gallery = (TextView) customDialog.findViewById(R.id.dialog_avatar_photo_gallery);

        TextView dialog_avatar_camera = (TextView) customDialog.findViewById(R.id.dialog_avatar_camera);

        TextView dialog_avatar_cancel = (TextView) customDialog.findViewById(R.id.dialog_avatar_cancel);

        dialog_avatar_photo_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cropParams.refreshUri();
                cropParams.enable = true;
                cropParams.compress = false;
                Intent intent = CropHelper.buildGalleryIntent(cropParams);
                startActivityForResult(intent, CropHelper.REQUEST_CROP);
                customDialog.dismiss();
            }
        });

        dialog_avatar_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cropParams.refreshUri();
                cropParams.enable = true;
                cropParams.compress = false;
                Intent intent = CropHelper.buildCameraIntent(cropParams);
                startActivityForResult(intent, CropHelper.REQUEST_CAMERA);
                customDialog.dismiss();
            }
        });

        dialog_avatar_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customDialog.dismiss();
            }
        });


        customDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        CropHelper.handleResult(this, requestCode, resultCode, data);
    }

    @Override
    public void onPhotoCropped(Uri uri) {
        if (!cropParams.compress) {
            Bitmap bitmap = BitmapUtil.decodeUriAsBitmap(getContext(), uri);
            String file_name = "cover_" + System.currentTimeMillis() + ".png";
            if (BitmapUtil.saveBitmapToFile(bitmap, file_name)) {
                File file = new File(Constants.APP_PATH_AVATAR + file_name);
                Glide.with(getContext())
                        .load(file)
                        .transform(new GlideRoundTransform(getContext()))
                        .error(R.drawable.icon_avatar_default)
                        .placeholder(R.drawable.icon_avatar_default)
                        .into(modify_user_avatar_image);
                modifyUserPresenter.changeAvatar(file);
            } else {
                Toast.makeText(getContext(), "存储裁剪文件失败！", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onCompressed(Uri uri) {
        Bitmap bitmap = BitmapUtil.decodeUriAsBitmap(getContext(), uri);
        String file_name = "cover_" + System.currentTimeMillis() + ".png";
        if (BitmapUtil.saveBitmapToFile(bitmap, file_name)) {
            File file = new File(Constants.APP_PATH_AVATAR + file_name);
            Glide.with(getContext())
                    .load(file)
                    .transform(new GlideRoundTransform(getContext()))
                    .error(R.drawable.icon_avatar_default)
                    .placeholder(R.drawable.icon_avatar_default)
                    .into(modify_user_avatar_image);
            modifyUserPresenter.changeAvatar(file);
        } else {
            Toast.makeText(getContext(), "存储裁剪文件失败！", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCancel() {
        Toast.makeText(getContext(), "已取消封面裁剪！", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFailed(String message) {
        Toast.makeText(getContext(), "封面裁剪失败！" + message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void handleIntent(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode);
    }

    @Override
    public CropParams getCropParams() {
        return cropParams;
    }

    public void checkUserPenName() {
        String name = null;
        if (modify_user_penname_input != null) {
            hideSoftInput(modify_user_penname_input);
            name = modify_user_penname_input.getText().toString();
        }
        if (name != null && name.trim().equals("")) {
            Toast.makeText(getContext(), R.string.check_pen_name, Toast.LENGTH_SHORT).show();
        } else {
            if (name != null && !name.equals("") && modifyUserPresenter != null) {
                modifyUserPresenter.changePenName(name, true);
            }
        }
    }

    private boolean nickNameFilter(String content) throws PatternSyntaxException {
        if (pattern == null) {
            pattern  = Pattern.compile(compile);
        }
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            Logger.d(matcher.group());
            return true;
        }
        return false;
    }
}
