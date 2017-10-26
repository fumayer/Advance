package com.quduquxie.creation.revise.view;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.StringSignature;
import com.quduquxie.Constants;
import com.quduquxie.R;
import com.quduquxie.base.util.TypefaceUtil;
import com.quduquxie.communal.crop.BitmapUtil;
import com.quduquxie.communal.crop.CropHandler;
import com.quduquxie.communal.crop.CropHelper;
import com.quduquxie.communal.crop.CropParams;
import com.quduquxie.communal.dialog.CustomDialogFragment;
import com.quduquxie.communal.dialog.CustomDialogImageFragment;
import com.quduquxie.communal.dialog.CustomDialogImageListener;
import com.quduquxie.communal.dialog.CustomDialogListener;
import com.quduquxie.communal.utils.UserUtil;
import com.quduquxie.creation.revise.LiteratureReviseInterface;
import com.quduquxie.creation.revise.presenter.LiteratureRevisePresenter;
import com.quduquxie.creation.widget.LiteratureCategoryView;
import com.quduquxie.creation.revise.widget.utils.DescTextWatcher;
import com.quduquxie.creation.widget.adapter.LiteratureCategoryViewAdapter;
import com.quduquxie.creation.widget.listener.LiteratureCategoryListener;
import com.quduquxie.function.creation.create.util.LiteratureCreateUtil;
import com.quduquxie.model.creation.Literature;
import com.quduquxie.util.QGLog;
import com.quduquxie.view.BaseActivity;
import com.quduquxie.wxapi.WXEntryActivity;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created on 16/11/22.
 * Created by crazylei.
 */

public class LiteratureReviseActivity extends BaseActivity implements LiteratureReviseInterface.View, DescTextWatcher.DescCountChangeListener, RadioGroup.OnCheckedChangeListener, LiteratureCategoryListener, CropHandler {

    private static final String TAG = LiteratureReviseActivity.class.getSimpleName();

    private LiteratureReviseInterface.Presenter literatureRevisePresenter;

    @BindView(R.id.literature_create_back)
    public ImageView literature_create_back;
    @BindView(R.id.literature_create_title)
    public TextView literature_create_title;
    @BindView(R.id.literature_create_establish)
    public TextView literature_create_establish;
    @BindView(R.id.literature_revise_content)
    public ScrollView literature_revise_content;
    @BindView(R.id.literature_revise_cover)
    public ImageView literature_revise_cover;
    @BindView(R.id.literature_revise_title)
    public TextView literature_revise_title;
    @BindView(R.id.literature_revise_title_limit)
    public TextView literature_revise_title_limit;
    @BindView(R.id.literature_revise_description_input)
    public EditText literature_revise_description_input;
    @BindView(R.id.literature_revise_description_length)
    public TextView literature_revise_description_length;
    @BindView(R.id.literature_revise_description_limit)
    public TextView literature_revise_description_limit;
    @BindView(R.id.literature_attribute_type)
    public TextView literature_attribute_type;
    @BindView(R.id.literature_attribute_type_serialize)
    public RadioButton literature_attribute_type_serialize;
    @BindView(R.id.literature_attribute_type_finish)
    public RadioButton literature_attribute_type_finish;
    @BindView(R.id.literature_revise_channel)
    public TextView literature_revise_channel;
    @BindView(R.id.literature_revise_channel_group)
    public RadioGroup literature_revise_channel_group;

    @BindView(R.id.literature_category_view)
    public LiteratureCategoryView literature_category_view;

    @BindView(R.id.literature_revise_style)
    public TextView literature_revise_style;
    @BindView(R.id.literature_revise_style_group)
    public RadioGroup literature_revise_style_group;
    @BindView(R.id.literature_revise_ending)
    public TextView literature_revise_ending;
    @BindView(R.id.literature_revise_ending_group)
    public RadioGroup literature_revise_ending_group;

    private static final String[] attribute = new String[]{"serialize", "finish"};

    private Literature literature;

    private File cover_file;

    public CropParams cropParams;

    private int textSize26;
    private int textSize34;
    private int buttonWidth;

    private RadioGroup.LayoutParams layoutParams;

    public static final int DEFAULT_ASPECT_X = 154;
    public static final int DEFAULT_ASPECT_Y = 217;
    public static final int DEFAULT_OUTPUT_X = 142;
    public static final int DEFAULT_OUTPUT_Y = 200;

    private CustomDialogImageFragment customDialogImageFragment;

    private CustomDialogFragment customDialogAttributeFragment;
    private CustomDialogFragment customDialogExitFragment;

    private Typeface typeface_song;
    private Typeface typeface_song_depict;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.layout_activity_literature_revise);
        } catch (Resources.NotFoundException exception) {
            collectException(exception);
            exception.printStackTrace();
        }

        ButterKnife.bind(this);


        this.textSize26 = this.getResources().getDimensionPixelSize(R.dimen.text_size_26);
        this.textSize34 = this.getResources().getDimensionPixelSize(R.dimen.text_size_34);

        this.buttonWidth = this.getResources().getDimensionPixelOffset(R.dimen.width_146);

        int buttonHeight = this.getResources().getDimensionPixelOffset(R.dimen.height_50);

        int viewPadding = this.getResources().getDimensionPixelOffset(R.dimen.width_10);

        layoutParams = new RadioGroup.LayoutParams(buttonWidth, buttonHeight);
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.setMargins(viewPadding, 0, viewPadding, 0);

        Intent intent = getIntent();
        if (intent != null) {
            literature = (Literature) intent.getSerializableExtra("literature");
        }

        typeface_song = TypefaceUtil.loadTypeface(this, TypefaceUtil.TYPEFACE_SONG);
        typeface_song_depict = TypefaceUtil.loadTypeface(this, TypefaceUtil.TYPEFACE_SONG_DEPICT);

        cropParams = new CropParams(this, DEFAULT_ASPECT_X, DEFAULT_ASPECT_Y, DEFAULT_OUTPUT_X, DEFAULT_OUTPUT_Y);

        literatureRevisePresenter = new LiteratureRevisePresenter(this, getApplicationContext());
        literatureRevisePresenter.initParameter(literature);
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
        CropHelper.clearCacheDir();
        super.onDestroy();
    }

    @Override
    public void setPresenter(LiteratureReviseInterface.Presenter literatureRevisePresenter) {
        this.literatureRevisePresenter = literatureRevisePresenter;
    }

    @Override
    public void initView(int desc_limit) {

        if (literature_create_title != null) {
            literature_create_title.setTypeface(typeface_song_depict);
            literature_create_title.setText(R.string.literature_revise);
        }

        if (literature_create_establish != null) {
            literature_create_establish.setText("保存");
            literature_create_establish.setEnabled(false);
        }

        if (literature_revise_cover != null) {
            if (!TextUtils.isEmpty(literature.image_url)) {
                Glide.with(getApplicationContext())
                        .load(literature.image_url)
                        .signature(new StringSignature(literature.image_url))
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .placeholder(R.drawable.icon_literature_create_cover)
                        .error(R.drawable.icon_literature_create_cover)
                        .skipMemoryCache(true)
                        .centerCrop()
                        .into(literature_revise_cover);
            } else {
                literature_revise_cover.setImageResource(R.drawable.icon_literature_create_cover);
            }
        }

        if (literature_revise_title != null) {
            literature_revise_title.setTypeface(typeface_song);
            literature_revise_title.setText(literature.name);
        }

        if (literature_revise_description_input != null) {
            DescTextWatcher descTextWatcher = new DescTextWatcher();
            descTextWatcher.setDescCountChangeListener(this);
            literature_revise_description_input.addTextChangedListener(descTextWatcher);
            literature_revise_description_input.setText(literature.description);
            literature_revise_description_input.setSelection(literature.description.length());
        }

        if (literature_revise_description_length != null && literature_revise_description_input != null) {
            literature_revise_description_length.setText(String.valueOf(literature_revise_description_input.length()));
        }

        if (literature_revise_description_limit != null) {
            literature_revise_description_limit.setText(String.valueOf(desc_limit));
        }

        if (literature_attribute_type != null) {
            literature_attribute_type.setTypeface(typeface_song);
        }

        if (literature.attribute.equals(attribute[1])) {
            literature_attribute_type_finish.setChecked(true);
            literature_attribute_type_finish.setClickable(false);
            literature_attribute_type_serialize.setVisibility(View.GONE);
        } else {
            literature_attribute_type_serialize.setVisibility(View.VISIBLE);
            literature_attribute_type_serialize.setChecked(true);
        }

        if (literature_revise_channel != null) {
            literature_revise_channel.setTypeface(typeface_song);
        }

        ArrayList<String> channels = LiteratureCreateUtil.getLiteratureChannel(getApplicationContext());
        if (channels != null && channels.size() > 0) {
            for (String channel : channels) {
                RadioButton radioButton = new RadioButton(getApplicationContext());
                radioButton.setText(channel);
                radioButton.setTextColor(Color.parseColor("#191919"));
                radioButton.setBackgroundColor(Color.parseColor("#FFFFFF"));
                radioButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize34);
                radioButton.setButtonDrawable(R.drawable.selector_check_view);

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(buttonWidth, LinearLayout.LayoutParams.MATCH_PARENT);
                layoutParams.gravity = Gravity.CENTER;

                radioButton.setLayoutParams(layoutParams);
                radioButton.setGravity(Gravity.CENTER);
                radioButton.setTag(R.id.click_type, "channel");

                literature_revise_channel_group.addView(radioButton);
            }
        }

        if (literature_category_view != null) {
            LiteratureCategoryViewAdapter literatureCategoryViewAdapter = new LiteratureCategoryViewAdapter(getSupportFragmentManager(), this);
            literature_category_view.setLiteratureCategoryListener(this);
            literature_category_view.setLiteratureCategoryAdapter(literatureCategoryViewAdapter);
        }

        if (literature_revise_style != null) {
            literature_revise_style.setTypeface(typeface_song);
        }

        ColorStateList colorStateList = this.getResources().getColorStateList(R.color.selector_literature_label_text);

        ArrayList<String> styles = LiteratureCreateUtil.getLiteratureStyle(getApplicationContext());
        if (styles != null && styles.size() > 0) {
            for (String style : styles) {
                RadioButton radioButton = new RadioButton(getApplicationContext());
                radioButton.setText(style);
                radioButton.setTextColor(colorStateList);
                radioButton.setBackgroundResource(R.drawable.background_literature_label);
                radioButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize26);
                radioButton.setButtonDrawable(android.R.color.transparent);
                radioButton.setGravity(Gravity.CENTER);
                radioButton.setTag(R.id.click_type, "style");

                literature_revise_style_group.addView(radioButton, layoutParams);
            }
        }

        if (literature_revise_ending != null) {
            literature_revise_ending.setTypeface(typeface_song);
        }

        ArrayList<String> endings = LiteratureCreateUtil.getLiteratureEnding(getApplicationContext());
        if (endings != null && endings.size() > 0) {
            for (String ending : endings) {
                RadioButton radioButton = new RadioButton(getApplicationContext());
                radioButton.setText(ending);
                radioButton.setTextColor(colorStateList);
                radioButton.setBackgroundResource(R.drawable.background_literature_label);
                radioButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize26);
                radioButton.setButtonDrawable(android.R.color.transparent);
                radioButton.setGravity(Gravity.CENTER);
                radioButton.setTag(R.id.click_type, "ending");

                literature_revise_ending_group.addView(radioButton, layoutParams);
            }
        }


        if (literature_revise_channel_group != null) {
            literature_revise_channel_group.setOnCheckedChangeListener(this);
        }

        if (literature_revise_style_group != null) {
            literature_revise_style_group.setOnCheckedChangeListener(this);
        }

        if (literature_revise_ending_group != null) {
            literature_revise_ending_group.setOnCheckedChangeListener(this);
        }

        if (channels != null && channels.size() > 0) {
            int index = channels.indexOf(literature.fenpin);
            if (index != -1) {
                literature_revise_channel_group.check(literature_revise_channel_group.getChildAt(index).getId());
            } else {
                literature_revise_channel_group.check(literature_revise_channel_group.getChildAt(0).getId());
            }
        }

        if (styles != null && styles.size() > 0) {
            int index = styles.indexOf(literature.style);
            if (index != -1) {
                literature_revise_style_group.check(literature_revise_style_group.getChildAt(index).getId());
            } else {
                literature_revise_style_group.check(literature_revise_style_group.getChildAt(0).getId());
            }
        }

        if (endings != null && endings.size() > 0) {
            int index = endings.indexOf(literature.ending);
            if (index != -1) {
                literature_revise_ending_group.check(literature_revise_ending_group.getChildAt(index).getId());
            } else {
                literature_revise_ending_group.check(literature_revise_ending_group.getChildAt(0).getId());
            }
        }

        literature_category_view.setLiteratureCategory(literature.category);

        if (literature_revise_content != null) {
            literature_revise_content.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(literature_revise_description_input.getWindowToken(), 0);
                    return false;
                }
            });
        }
    }

    @Override
    public void showErrorView() {

    }

    @Override
    public void checkLiteratureCreateState(boolean state) {
        literature_create_establish.setText("保存");
        literature_create_establish.setEnabled(true);
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void finishActivity() {
        finish();
    }

    @Override
    public void startLoginActivity() {
        UserUtil.deleteUser(getApplicationContext());
        Intent intent = new Intent();
        intent.putExtra("exit_login", true);
        intent.setClass(this, WXEntryActivity.class);
        startActivity(intent);
    }


    @OnClick({R.id.literature_create_back, R.id.literature_revise_cover, R.id.literature_create_establish, R.id.literature_attribute_type_finish, R.id.literature_attribute_type_serialize})
    public void onClickListener(View view) {
        switch (view.getId()) {
            case R.id.literature_create_back:
                finish();
                break;
            case R.id.literature_revise_cover:
                QGLog.e(TAG, "上传图片！");
                showCoverDialog();
                break;
            case R.id.literature_create_establish:
                publishLiteratureChapter();
                break;
            case R.id.literature_attribute_type_finish:
                literature_attribute_type_finish.setChecked(false);
                if (literature.chapter != null) {
                    literatureFinishConfirmDialog();
                } else {
                    Toast.makeText(getApplicationContext(), "当前作品尚未开始创作，无法设置完结状态", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.literature_attribute_type_serialize:
                setAttributeState(0);
                literatureRevisePresenter.checkLiteratureCompleteState("attribute", "serialize");
                break;
        }
    }

    @Override
    public void setDescCountChanged(String description) {
        if (literature_revise_description_length != null) {
            literature_revise_description_length.setText(String.valueOf(description.length()));
        }
        literatureRevisePresenter.checkLiteratureCompleteState("description", description);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        RadioButton radioButton = (RadioButton) findViewById(checkedId);
        String type = (String) radioButton.getTag(R.id.click_type);
        if ("channel".equals(type)) {
            String text = radioButton.getText().toString();
            literatureRevisePresenter.checkLiteratureCompleteState("fenpin", text);
            if (literature_category_view != null) {
                if ("女频".equals(text)) {
                    literature_category_view.setCurrentItem(1);
                } else if ("男频".equals(text)) {
                    literature_category_view.setCurrentItem(0);
                }
            }
        } else if ("style".equals(type)) {
            String text = radioButton.getText().toString();
            literatureRevisePresenter.checkLiteratureCompleteState("style", text);
        } else if ("ending".equals(type)) {
            String text = radioButton.getText().toString();
            literatureRevisePresenter.checkLiteratureCompleteState("ending", text);
        }
    }

    @Override
    public void setCurrentType(int index) {
        if (literature_revise_channel_group != null) {
            literature_revise_channel_group.check(literature_revise_channel_group.getChildAt(index).getId());
        }
    }

    @Override
    public void onCategoryClicked(String category) {
        literatureRevisePresenter.checkLiteratureCompleteState("category", category);
    }

    @Override
    public void clearClickedCategory() {
        onCategoryClicked("");
    }

    public void setAttributeState(int index) {
        if (literature_attribute_type_serialize != null) {
            literature_attribute_type_serialize.setChecked(index == 0);
        }
        if (literature_attribute_type_finish != null) {
            literature_attribute_type_finish.setChecked(index == 1);
        }
    }

    /**
     * 确认修改书籍状态Dialog
     **/
    private void literatureFinishConfirmDialog() {
        if (customDialogAttributeFragment == null) {
            customDialogAttributeFragment = new CustomDialogFragment();
        }

        customDialogAttributeFragment.setPrompt("作品设置为已完结将无法修改回连载状态，是否要完结？");
        customDialogAttributeFragment.setFirstOption("否");
        customDialogAttributeFragment.setSecondOption("是");
        customDialogAttributeFragment.setCustomDialogListener(new CustomDialogListener() {
            @Override
            public void onFirstOptionClicked() {
                hideCustomDialogAttributeFragment();
            }

            @Override
            public void onSecondOptionClicked() {
                setAttributeState(1);
                literatureRevisePresenter.checkLiteratureCompleteState("attribute", "finish");
                hideCustomDialogAttributeFragment();
            }
        });

        if (!isFinishing()) {
            if (customDialogAttributeFragment.isAdded()) {
                customDialogAttributeFragment.setShowsDialog(true);
            } else {
                customDialogAttributeFragment.show(getSupportFragmentManager(), "CustomDialogAttributeFragment");
            }
        }
    }

    public void hideCustomDialogAttributeFragment() {
        if (!isFinishing() && customDialogAttributeFragment != null && customDialogAttributeFragment.getShowsDialog()) {
            customDialogAttributeFragment.dismiss();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        CropHelper.handleResult(this, requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            showCreatePromptDialog();
        }
        return false;
    }

    private void publishLiteratureChapter() {
        if (literatureRevisePresenter == null) {
            literatureRevisePresenter = new LiteratureRevisePresenter(this, getApplicationContext());
        }
        if (literatureRevisePresenter.verificationInformation()) {
            literatureRevisePresenter.reviseLiterature(cover_file);
        }
    }

    private void showCreatePromptDialog() {
        if (customDialogExitFragment == null) {
            customDialogExitFragment = new CustomDialogFragment();
        }

        customDialogExitFragment.setPrompt("是否需要更新作品信息？");
        customDialogExitFragment.setFirstOption("否");
        customDialogExitFragment.setSecondOption("是");
        customDialogExitFragment.setCustomDialogListener(new CustomDialogListener() {
            @Override
            public void onFirstOptionClicked() {
                hideCustomDialogExitFragment();
                finishActivity();
            }

            @Override
            public void onSecondOptionClicked() {
                hideCustomDialogExitFragment();
                publishLiteratureChapter();
            }
        });

        if (!isFinishing()) {
            if (customDialogExitFragment.isAdded()) {
                customDialogExitFragment.setShowsDialog(true);
            } else {
                customDialogExitFragment.show(getSupportFragmentManager(), "CustomDialogExitFragment");
            }
        }
    }

    public void hideCustomDialogExitFragment() {
        if (!isFinishing() && customDialogExitFragment != null && customDialogExitFragment.getShowsDialog()) {
            customDialogExitFragment.dismiss();
        }
    }
    @Override
    public void onPhotoCropped(Uri uri) {
        if (!cropParams.compress) {
            Bitmap bitmap = BitmapUtil.decodeUriAsBitmap(this, uri);
            literature_revise_cover.setImageBitmap(bitmap);
            String file_name = "cover_" + System.currentTimeMillis() + ".png";
            if (BitmapUtil.saveBitmapToFile(bitmap, file_name)) {
                cover_file = new File(Constants.APP_PATH_AVATAR + file_name);
            } else {
                Toast.makeText(getApplicationContext(), "存储裁剪文件失败！", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onCompressed(Uri uri) {
        Bitmap bitmap = BitmapUtil.decodeUriAsBitmap(this, uri);
        literature_revise_cover.setImageBitmap(bitmap);
        String file_name = "cover_" + System.currentTimeMillis() + ".png";
        if (BitmapUtil.saveBitmapToFile(bitmap, file_name)) {
            cover_file = new File(Constants.APP_PATH_AVATAR + file_name);
        } else {
            Toast.makeText(getApplicationContext(), "存储裁剪文件失败！", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCancel() {
        Toast.makeText(this, "已取消封面裁剪！", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFailed(String message) {
        Toast.makeText(this, "封面裁剪失败！" + message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void handleIntent(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode);
    }

    @Override
    public CropParams getCropParams() {
        return cropParams;
    }
}
