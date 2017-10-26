package com.quduquxie.function.creation.create.view;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.quduquxie.Constants;
import com.quduquxie.R;
import com.quduquxie.application.component.ApplicationComponent;
import com.quduquxie.communal.crop.BitmapUtil;
import com.quduquxie.communal.crop.CropHandler;
import com.quduquxie.communal.crop.CropHelper;
import com.quduquxie.communal.crop.CropParams;
import com.quduquxie.communal.dialog.CustomDialogFragment;
import com.quduquxie.communal.dialog.CustomDialogImageFragment;
import com.quduquxie.communal.dialog.CustomDialogImageListener;
import com.quduquxie.communal.dialog.CustomDialogListener;
import com.quduquxie.communal.utils.UserUtil;
import com.quduquxie.creation.widget.LiteratureCategoryView;
import com.quduquxie.creation.widget.adapter.LiteratureCategoryViewAdapter;
import com.quduquxie.creation.widget.listener.LiteratureCategoryListener;
import com.quduquxie.function.BaseActivity;
import com.quduquxie.function.creation.create.LiteratureCreateInterface;
import com.quduquxie.function.creation.create.component.DaggerLiteratureCreateComponent;
import com.quduquxie.function.creation.create.module.LiteratureCreateModule;
import com.quduquxie.function.creation.create.presenter.LiteratureCreatePresenter;
import com.quduquxie.function.creation.create.util.LiteratureCreateUtil;
import com.quduquxie.function.creation.util.DescTextWatcher;
import com.quduquxie.function.creation.util.NameTextWatcher;
import com.quduquxie.widget.LoadingPage;
import com.quduquxie.wxapi.WXEntryActivity;

import java.io.File;
import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created on 17/4/10.
 * Created by crazylei.
 */

public class LiteratureCreateActivity extends BaseActivity implements LiteratureCreateInterface.View, NameTextWatcher.NameCountChangeListener, DescTextWatcher.DescCountChangeListener, LiteratureCategoryListener, RadioGroup.OnCheckedChangeListener, CropHandler {

    @BindView(R.id.literature_create_back)
    public ImageView literature_create_back;
    @BindView(R.id.literature_create_title)
    public TextView literature_create_title;
    @BindView(R.id.literature_create_establish)
    public TextView literature_create_establish;
    @BindView(R.id.literature_create_content)
    public RelativeLayout literature_create_content;
    @BindView(R.id.literature_create_result)
    public ScrollView literature_create_result;
    @BindView(R.id.literature_create_cover)
    public ImageView literature_create_cover;
    @BindView(R.id.literature_create_title_input)
    public EditText literature_create_title_input;
    @BindView(R.id.literature_create_title_length)
    public TextView literature_create_title_length;
    @BindView(R.id.literature_create_title_limit)
    public TextView literature_create_title_limit;
    @BindView(R.id.literature_create_description_input)
    public EditText literature_create_description_input;
    @BindView(R.id.literature_create_description_length)
    public TextView literature_create_description_length;
    @BindView(R.id.literature_create_description_limit)
    public TextView literature_create_description_limit;
    @BindView(R.id.literature_create_channel)
    public TextView literature_create_channel;
    @BindView(R.id.literature_create_channel_group)
    public RadioGroup literature_create_channel_group;

    @BindView(R.id.literature_category_view)
    public LiteratureCategoryView literature_category_view;

    @BindView(R.id.literature_create_style)
    public TextView literature_create_style;
    @BindView(R.id.literature_create_style_group)
    public RadioGroup literature_create_style_group;
    @BindView(R.id.literature_create_ending)
    public TextView literature_create_ending;
    @BindView(R.id.literature_create_ending_group)
    public RadioGroup literature_create_ending_group;

    @Inject
    LiteratureCreatePresenter literatureCreatePresenter;

    private int textSize26;
    private int textSize34;
    private int buttonWidth;

    private RadioGroup.LayoutParams layoutParams;

    public CropParams cropParams;

    private File cover_file;

    private Toast toast;

    private LoadingPage loadingPage;

    public static final int DEFAULT_ASPECT_X = 154;
    public static final int DEFAULT_ASPECT_Y = 217;
    public static final int DEFAULT_OUTPUT_X = 142;
    public static final int DEFAULT_OUTPUT_Y = 200;

    private CustomDialogImageFragment customDialogImageFragment;

    private CustomDialogFragment customDialogFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.layout_activity_literature_create);

            initParameter();
        } catch (Resources.NotFoundException exception) {
            collectException(exception);
            exception.printStackTrace();
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
    protected void setActivityComponent(ApplicationComponent applicationComponent) {
        DaggerLiteratureCreateComponent.builder()
                .applicationComponent(applicationComponent)
                .literatureCreateModule(new LiteratureCreateModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(LiteratureCreateInterface.Presenter literatureCreatePresenter) {
        this.literatureCreatePresenter = (LiteratureCreatePresenter) literatureCreatePresenter;
    }

    @Override
    public void initParameter() {
        ButterKnife.bind(this);

        this.textSize26 = this.getResources().getDimensionPixelSize(R.dimen.text_size_26);
        this.textSize34 = this.getResources().getDimensionPixelSize(R.dimen.text_size_34);

        this.buttonWidth = this.getResources().getDimensionPixelOffset(R.dimen.width_146);

        int buttonHeight = this.getResources().getDimensionPixelOffset(R.dimen.height_50);

        int viewPadding = this.getResources().getDimensionPixelOffset(R.dimen.width_10);

        layoutParams = new RadioGroup.LayoutParams(buttonWidth, buttonHeight);
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.setMargins(viewPadding, 0, viewPadding, 0);

        cropParams = new CropParams(this, DEFAULT_ASPECT_X, DEFAULT_ASPECT_Y, DEFAULT_OUTPUT_X, DEFAULT_OUTPUT_Y);

        literatureCreatePresenter.loadLiteratureTag();
    }

    @Override
    public void showLoadingPage() {
        if (loadingPage != null) {
            loadingPage.onSuccess();
        }

        if (loadingPage == null) {
            loadingPage = new LoadingPage(this, literature_create_content);
        }
        literatureCreatePresenter.setLoadingPage(loadingPage);
    }

    @Override
    public void hideLoadingPage() {
        if (loadingPage != null) {
            loadingPage.onSuccess();
        }
    }

    @Override
    public void initView(int nameLimit, int descLimit) {

        if (literature_create_title != null) {
            literature_create_title.setTypeface(typeface_song_depict);
            literature_create_title.setText(R.string.create);
        }

        if (literature_create_establish != null) {
            literature_create_establish.setText(R.string.create);
        }

        if (literature_create_title_input != null) {
            literature_create_title_input.setTypeface(typeface_song);
            NameTextWatcher nameTextWatcher = new NameTextWatcher();
            nameTextWatcher.setNameCountChangeListener(this);
            literature_create_title_input.addTextChangedListener(nameTextWatcher);
        }

        if (literature_create_title_length != null && literature_create_title_input != null) {
            literature_create_title_length.setText(String.valueOf(literature_create_title_input.length()));
        }

        if (literature_create_title_limit != null) {
            literature_create_title_limit.setText(String.valueOf(nameLimit));
        }

        if (literature_create_description_input != null) {
            DescTextWatcher descTextWatcher = new DescTextWatcher();
            descTextWatcher.setDescCountChangeListener(this);
            literature_create_description_input.addTextChangedListener(descTextWatcher);
        }

        if (literature_create_description_length != null && literature_create_description_input != null) {
            literature_create_description_length.setText(String.valueOf(literature_create_description_input.length()));
        }

        if (literature_create_description_limit != null) {
            literature_create_description_limit.setText(String.valueOf(descLimit));
        }

        if (literature_create_channel != null) {
            literature_create_channel.setTypeface(typeface_song);
        }

        ArrayList<String> channels = LiteratureCreateUtil.getLiteratureChannel(getQuApplicationContext());
        if (channels != null && channels.size() > 0) {
            for (String channel : channels) {
                RadioButton radioButton = new RadioButton(getQuApplicationContext());
                radioButton.setText(channel);
                radioButton.setTextColor(Color.parseColor("#191919"));
                radioButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize34);
                radioButton.setButtonDrawable(R.drawable.selector_check_view);

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(buttonWidth, LinearLayout.LayoutParams.MATCH_PARENT);
                layoutParams.gravity = Gravity.CENTER;

                radioButton.setLayoutParams(layoutParams);
                radioButton.setGravity(Gravity.CENTER);
                radioButton.setTag(R.id.click_type, "channel");

                literature_create_channel_group.addView(radioButton);
            }
        }


        if (literature_category_view != null) {
            LiteratureCategoryViewAdapter literatureCategoryViewAdapter = new LiteratureCategoryViewAdapter(getSupportFragmentManager(), this);
            literature_category_view.setLiteratureCategoryListener(this);
            literature_category_view.setLiteratureCategoryAdapter(literatureCategoryViewAdapter);
        }

        if (literature_create_style != null) {
            literature_create_style.setTypeface(typeface_song);
        }

        ColorStateList colorStateList = this.getResources().getColorStateList(R.color.selector_literature_label_text);

        ArrayList<String> styles = LiteratureCreateUtil.getLiteratureStyle(getQuApplicationContext());
        if (styles != null && styles.size() > 0) {
            for (String style : styles) {
                RadioButton radioButton = new RadioButton(getQuApplicationContext());
                radioButton.setText(style);
                radioButton.setButtonDrawable(android.R.color.transparent);
                radioButton.setTextColor(colorStateList);
                radioButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize26);
                radioButton.setBackgroundResource(R.drawable.background_literature_label);
                radioButton.setGravity(Gravity.CENTER);
                radioButton.setTag(R.id.click_type, "style");

                literature_create_style_group.addView(radioButton, layoutParams);
            }
        }

        if (literature_create_ending != null) {
            literature_create_ending.setTypeface(typeface_song);
        }

        ArrayList<String> endings = LiteratureCreateUtil.getLiteratureEnding(getQuApplicationContext());
        if (endings != null && endings.size() > 0) {
            for (String ending : endings) {
                RadioButton radioButton = new RadioButton(getQuApplicationContext());
                radioButton.setText(ending);
                radioButton.setButtonDrawable(android.R.color.transparent);
                radioButton.setTextColor(colorStateList);
                radioButton.setBackgroundResource(R.drawable.background_literature_label);
                radioButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize26);
                radioButton.setGravity(Gravity.CENTER);
                radioButton.setTag(R.id.click_type, "ending");

                literature_create_ending_group.addView(radioButton, layoutParams);
            }
        }

        if (literature_create_channel_group != null) {
            literature_create_channel_group.setOnCheckedChangeListener(this);
            literature_create_channel_group.check(literature_create_channel_group.getChildAt(0).getId());
        }

        if (literature_create_style_group != null) {
            literature_create_style_group.setOnCheckedChangeListener(this);
        }

        if (literature_create_ending_group != null) {
            literature_create_ending_group.setOnCheckedChangeListener(this);
        }

        if (literature_create_result != null) {
            literature_create_result.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(literature_create_title_input.getWindowToken(), 0);
                    inputMethodManager.hideSoftInputFromWindow(literature_create_description_input.getWindowToken(), 0);
                    return false;
                }
            });
        }
    }

    @Override
    public void checkLiteratureCreateState(boolean state) {
        literature_create_establish.setText("完成");
    }

    @Override
    public void showToast(String message) {
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

    @Override
    public void finishActivity() {
        finish();
    }

    @Override
    public void startLoginActivity() {
        UserUtil.deleteUser(getApplicationContext());
        Intent intent = new Intent();
        intent.setClass(this, WXEntryActivity.class);
        startActivity(intent);
    }

    @Override
    public void setDescCountChanged(String description) {
        if (literature_create_description_length != null) {
            literature_create_description_length.setText(String.valueOf(description.length()));
        }
        literatureCreatePresenter.checkLiteratureCompleteState("description", description);
    }

    @Override
    public void setNameCountChanged(String name) {
        if (literature_create_title_length != null) {
            literature_create_title_length.setText(String.valueOf(name.length()));
        }
        literatureCreatePresenter.checkLiteratureCompleteState("name", name);
    }

    @Override
    public void checkNameInput() {
        showToast("检测到书名含有非法字符！");
    }

    @Override
    public void setCurrentType(int index) {
        if (literature_create_channel_group != null) {
            literature_create_channel_group.check(literature_create_channel_group.getChildAt(index).getId());
        }
    }

    @Override
    public void onCategoryClicked(String category) {
        literatureCreatePresenter.checkLiteratureCompleteState("category", category);
    }

    @Override
    public void clearClickedCategory() {
        onCategoryClicked("");
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        RadioButton radioButton = (RadioButton) findViewById(checkedId);
        String type = (String) radioButton.getTag(R.id.click_type);
        if ("channel".equals(type)) {
            String text = radioButton.getText().toString();
            literatureCreatePresenter.checkLiteratureCompleteState("fenpin", text);
            if (literature_category_view != null) {
                if ("女频".equals(text)) {
                    literature_category_view.setCurrentItem(1);
                } else if ("男频".equals(text)) {
                    literature_category_view.setCurrentItem(0);
                }
            }
        } else if ("style".equals(type)) {
            String text = radioButton.getText().toString();
            literatureCreatePresenter.checkLiteratureCompleteState("style", text);
        } else if ("ending".equals(type)) {
            String text = radioButton.getText().toString();
            literatureCreatePresenter.checkLiteratureCompleteState("ending", text);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        CropHelper.handleResult(this, requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            showCreatePromptDialog();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onPhotoCropped(Uri uri) {
        if (!cropParams.compress) {
            Bitmap bitmap = BitmapUtil.decodeUriAsBitmap(this, uri);
            literature_create_cover.setImageBitmap(bitmap);
            String file_name = "cover_" + System.currentTimeMillis() + ".png";
            if (BitmapUtil.saveBitmapToFile(bitmap, file_name)) {
                cover_file = new File(Constants.APP_PATH_AVATAR + file_name);
            } else {
                showToast("存储裁剪文件失败！");
            }
        }
    }

    @Override
    public void onCompressed(Uri uri) {
        Bitmap bitmap = BitmapUtil.decodeUriAsBitmap(this, uri);
        literature_create_cover.setImageBitmap(bitmap);
        String file_name = "cover_" + System.currentTimeMillis() + ".png";
        if (BitmapUtil.saveBitmapToFile(bitmap, file_name)) {
            cover_file = new File(Constants.APP_PATH_AVATAR + file_name);
        } else {
            showToast("存储裁剪文件失败！");
        }
    }

    @Override
    public void onCancel() {
        showToast("已取消封面裁剪！");
    }

    @Override
    public void onFailed(String message) {
        showToast("封面裁剪失败！");
    }

    @Override
    public void handleIntent(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode);
    }

    @Override
    public CropParams getCropParams() {
        return cropParams;
    }

    @OnClick({R.id.literature_create_back, R.id.literature_create_cover, R.id.literature_create_establish})
    public void onClickListener(View view) {
        switch (view.getId()) {
            case R.id.literature_create_back:
                finishActivity();
                break;
            case R.id.literature_create_cover:
                showCoverDialog();
                break;
            case R.id.literature_create_establish:
                publishLiteratureChapter();
                break;
        }
    }

    private void showCoverDialog() {
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

        if (!isFinishing()) {
            if (customDialogImageFragment.isAdded()) {
                customDialogImageFragment.setShowsDialog(true);
            } else {
                customDialogImageFragment.show(getSupportFragmentManager(), "CustomDialogImageFragment");
            }
        }
    }

    public void hideCustomDialogImageFragment() {
        if (!isFinishing() && customDialogImageFragment != null && customDialogImageFragment.getShowsDialog()) {
            customDialogImageFragment.dismiss();
        }
    }

    private void showCreatePromptDialog() {
        if (customDialogFragment == null) {
            customDialogFragment = new CustomDialogFragment();
        }

        customDialogFragment.setPrompt("是否需要创建新作品？");
        customDialogFragment.setFirstOption("退出");
        customDialogFragment.setSecondOption("创建");
        customDialogFragment.setCustomDialogListener(new CustomDialogListener() {
            @Override
            public void onFirstOptionClicked() {
                hideCustomDialogFragment();
                finishActivity();
            }

            @Override
            public void onSecondOptionClicked() {
                hideCustomDialogFragment();
                publishLiteratureChapter();
            }
        });

        if (!isFinishing()) {
            if (customDialogFragment.isAdded()) {
                customDialogFragment.setShowsDialog(true);
            } else {
                customDialogFragment.show(getSupportFragmentManager(), "CustomDialogFragment");
            }
        }
    }

    public void hideCustomDialogFragment() {
        if (!isFinishing() && customDialogFragment != null && customDialogFragment.getShowsDialog()) {
            customDialogFragment.dismiss();
        }
    }

    private void publishLiteratureChapter() {
        if (literatureCreatePresenter.verificationInformation()) {
            literatureCreatePresenter.createLiterature(cover_file);
        }
    }
}
