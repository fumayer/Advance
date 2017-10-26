package com.quduquxie.wxapi.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.orhanobut.logger.Logger;
import com.quduquxie.BaseFragment;
import com.quduquxie.Constants;
import com.quduquxie.R;
import com.quduquxie.communal.dialog.CustomDialogImageFragment;
import com.quduquxie.communal.dialog.CustomDialogImageListener;
import com.quduquxie.model.User;
import com.quduquxie.communal.crop.BitmapUtil;
import com.quduquxie.communal.crop.CropHandler;
import com.quduquxie.communal.crop.CropHelper;
import com.quduquxie.communal.crop.CropParams;
import com.quduquxie.communal.widget.GlideRoundTransform;
import com.quduquxie.db.UserDao;
import com.quduquxie.wxapi.CompleteUserInterface;
import com.quduquxie.wxapi.listener.LandingListener;
import com.quduquxie.wxapi.presenter.CompleteUserPresenter;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created on 16/11/30.
 * Created by crazylei.
 */

public class CompleteUserFragment extends BaseFragment implements CompleteUserInterface.View, CropHandler {

    private CompleteUserInterface.Presenter completeUserPresenter;

    @BindView(R.id.complete_user_avatar)
    public ImageView complete_user_avatar;
    @BindView(R.id.complete_user_nickname)
    public EditText complete_user_nickname;
    @BindView(R.id.complete_user)
    public TextView complete_user;
    @BindView(R.id.complete_user_prompt)
    public TextView complete_user_prompt;

    private LandingListener landingListener;

    private File avatar_file;

    private UserDao userDao;

    public CropParams cropParams;

    public static final int DEFAULT_ASPECT_X = 1;
    public static final int DEFAULT_ASPECT_Y = 1;
    public static final int DEFAULT_OUTPUT_X = 200;
    public static final int DEFAULT_OUTPUT_Y = 200;

    private String compile = "[^\\u4e00-\\u9fa50-9a-zA-Z_' ']";
    private Pattern pattern;

    private Toast toast;

    private CustomDialogImageFragment customDialogImageFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cropParams = new CropParams(getContext(), DEFAULT_ASPECT_X, DEFAULT_ASPECT_Y, DEFAULT_OUTPUT_X, DEFAULT_OUTPUT_Y);

        completeUserPresenter = new CompleteUserPresenter(this, getContext());
        completeUserPresenter.init();
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
        View view = inflater.inflate(R.layout.layout_fragment_complete_user, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    protected void initView() {
        if (userDao == null) {
            userDao = UserDao.getInstance(getContext());
        }
        User user = userDao.getUser();
        if (user != null && !TextUtils.isEmpty(user.avatar_url)) {
            Glide.with(getContext())
                    .load(user.avatar_url)
                    .transform(new GlideRoundTransform(getContext()))
                    .signature(new StringSignature(user.avatar_url))
                    .error(R.drawable.icon_avatar_default)
                    .placeholder(R.drawable.icon_avatar_default)
                    .into(complete_user_avatar);
        } else {
            complete_user_avatar.setImageResource(R.drawable.icon_avatar_default);
        }

        if (user != null && !user.platform.equals("Qingoo") && !TextUtils.isEmpty(user.penname)) {
            complete_user_nickname.setText(user.penname);
        }

        complete_user_nickname.addTextChangedListener(new TextWatcher() {
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
    }

    @Override
    public void setPresenter(CompleteUserInterface.Presenter completeUserPresenter) {
        this.completeUserPresenter = completeUserPresenter;
    }

    @Override
    public void showToast(String message) {
        if (toast == null) {
            toast = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
        } else {
            toast.setText(message);
        }
        if (toast != null)
            toast.show();
    }

    @OnClick({R.id.complete_user_avatar, R.id.complete_user})
    public void onClickListener(View view) {
        switch (view.getId()) {
            case R.id.complete_user_avatar:
                showAvatarDialog();
                break;
            case R.id.complete_user:
                hideSoftInput(view);
                String nickname = complete_user_nickname.getText().toString();
                if (completeUserPresenter.verificationInformation(nickname, avatar_file)) {
                    if (landingListener != null) {
                        landingListener.completeUserInformation(nickname, avatar_file);
                    }
                }
                break;
        }
    }

    private void showAvatarDialog() {
        if (customDialogImageFragment == null) {
            customDialogImageFragment = new CustomDialogImageFragment();
        }

        customDialogImageFragment.setCustomDialogImageListener(new CustomDialogImageListener() {
            @Override
            public void onGalleryClicked() {
                cropParams.refreshUri();
                cropParams.enable = true;
                cropParams.compress = false;
                Intent intent = CropHelper.buildGalleryIntent(cropParams);
                startActivityForResult(intent, CropHelper.REQUEST_CROP);
                hideCustomDialogImageFragment();
            }

            @Override
            public void onCameraClicked() {
                cropParams.refreshUri();
                cropParams.enable = true;
                cropParams.compress = false;
                Intent intent = CropHelper.buildCameraIntent(cropParams);
                startActivityForResult(intent, CropHelper.REQUEST_CAMERA);
                hideCustomDialogImageFragment();
            }

            @Override
            public void onCancelClicked() {
                hideCustomDialogImageFragment();
            }
        });

        if (getActivity() != null && !getActivity().isFinishing()) {
            if (customDialogImageFragment.isAdded()) {
                customDialogImageFragment.setShowsDialog(true);
            } else {
                customDialogImageFragment.show(getFragmentManager(), "CustomDialogImageFragment");
            }
        }
    }

    public void hideCustomDialogImageFragment() {
        if (getActivity() != null && !getActivity().isFinishing() && customDialogImageFragment != null && customDialogImageFragment.getShowsDialog()) {
            customDialogImageFragment.dismiss();
        }
    }

    public void setLandingListener(LandingListener landingListener) {
        this.landingListener = landingListener;
    }

    public void setCompletePrompt(String message) {
        if (complete_user_prompt != null) {
            complete_user_prompt.setText(message);
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
                avatar_file = new File(Constants.APP_PATH_AVATAR + file_name);
                Glide.with(getContext())
                        .load(avatar_file)
                        .transform(new GlideRoundTransform(getContext()))
                        .error(R.drawable.icon_avatar_default)
                        .placeholder(R.drawable.icon_avatar_default)
                        .into(complete_user_avatar);
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
            avatar_file = new File(Constants.APP_PATH_AVATAR + file_name);
            Glide.with(getContext())
                    .load(avatar_file)
                    .transform(new GlideRoundTransform(getContext()))
                    .error(R.drawable.icon_avatar_default)
                    .placeholder(R.drawable.icon_avatar_default)
                    .into(complete_user_avatar);
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
