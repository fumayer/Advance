package com.quduquxie.read;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.quduquxie.Constants;
import com.quduquxie.R;
import com.quduquxie.base.bean.Book;
import com.quduquxie.base.config.BaseConfig;
import com.quduquxie.base.util.CommunalUtil;
import com.quduquxie.base.util.NetworkUtil;
import com.quduquxie.base.util.ResourceUtil;
import com.quduquxie.base.util.TypefaceUtil;
import com.quduquxie.base.widget.CustomRadioButton;
import com.quduquxie.module.read.reading.view.ReadingActivity;

import java.text.NumberFormat;

/**
 * 阅读页面 bottom 菜单
 */
public class ReadSettingView extends FrameLayout implements OnClickListener, OnCheckedChangeListener, OnSeekBarChangeListener {

    private View setting_options;
    private LinearLayout setting_options_content;
    private ImageView setting_options_chapter_previous;
    private ImageView setting_options_chapter_next;
    private TextView setting_options_chapter;
    private TextView setting_options_sequence;
    private SeekBar setting_options_progress;
    private ImageView setting_options_category;
    private ImageView setting_options_detail;
    private ImageView setting_options_other;
    private ImageView setting_options_change_mode;


    private View setting_detail_options;
    private LinearLayout setting_detail_options_content;
    private TextView setting_detail_options_fount;
    private ImageView setting_detail_options_fount_decrease;
    private ImageView setting_detail_options_fount_increase;
    private View setting_detail_options_fount_view;
    private RelativeLayout setting_detail_options_fount_10;
    private ImageView setting_detail_options_fount_image_10;
    private RelativeLayout setting_detail_options_fount_15;
    private ImageView setting_detail_options_fount_image_15;
    private RelativeLayout setting_detail_options_fount_20;
    private ImageView setting_detail_options_fount_image_20;
    private RelativeLayout setting_detail_options_fount_25;
    private ImageView setting_detail_options_fount_image_25;
    private RelativeLayout setting_detail_options_fount_30;
    private ImageView setting_detail_options_fount_image_30;
    private TextView setting_detail_options_spacing;
    private RadioGroup setting_detail_options_spacing_group;
    private CustomRadioButton setting_detail_options_spacing_0_2;
    private CustomRadioButton setting_detail_options_spacing_0_5;
    private CustomRadioButton setting_detail_options_spacing_1_0;
    private TextView setting_detail_options_typeface;
    private RadioGroup setting_detail_options_typeface_group;
    private RadioButton setting_detail_options_typeface_song;
    private RadioButton setting_detail_options_typeface_system;
    private TextView setting_detail_options_flip;
    private RadioGroup setting_detail_options_flip_group;
    private RadioButton setting_detail_options_flip_simulation;
    private RadioButton setting_detail_options_flip_translation;
    private RadioButton setting_detail_options_flip_up_down;
    private ImageView setting_detail_options_change_mode;


    private View setting_other_options;
    private LinearLayout setting_other_options_content;
    private ImageView setting_other_options_brightness_down;
    private SeekBar setting_other_options_brightness_progress;
    private ImageView setting_other_options_brightness_up;
    private Button setting_other_options_brightness_follow;
    private RadioGroup setting_other_options_background;
    private RadioButton setting_other_options_background_first;
    private RadioButton setting_other_options_background_second;
    private RadioButton setting_other_options_background_third;
    private RadioButton setting_other_options_background_fourth;
    private RadioButton setting_other_options_background_fifth;
    private RadioButton setting_other_options_background_sixth;
    private ImageView setting_other_options_change_mode;


    private ReadingActivity readingActivity;

    private SharedPreferences sharedPreferences;

    private boolean brightnessFollowSystem;

    private Animation displayAnimation;
    private Animation dismissAnimation;

    private Drawable darkProgressThumb;
    private Drawable lightProgressThumb;

    private int settingView = SETTING_DEFAULT;

    private long intervalTime;

    private ColorStateList darkColorStateList;
    private ColorStateList lightColorStateList;

    private ReadSettingListener readSettingListener;
    private IReadDataFactory dataFactory;
    private ReadStatus readStatus;

    final static int SETTING_DEFAULT = 0x80;
    final static int SETTING_OPTION = 0x81;
    final static int SETTING_DETAIL = 0x82;
    final static int SETTING_OTHER = 0x83;

    private Handler handler = new Handler();

    public ReadSettingView(Context context) {
        this(context, null);
    }

    public ReadSettingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ReadSettingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void initializeView(ReadingActivity readingActivity) {
        this.readingActivity = readingActivity;

        this.sharedPreferences = readingActivity.getSharedPreferences(BaseConfig.FLAG_SHARE_PREFERENCE_NAME, Context.MODE_PRIVATE);

        BaseConfig.READING_BACKGROUND_MODE = sharedPreferences.getInt(BaseConfig.FLAG_READING_BACKGROUND_MODE, 1);

        brightnessFollowSystem = sharedPreferences.getBoolean(BaseConfig.FLAG_BRIGHTNESS_FOLLOW_SYSTEM, true);

        //阅读页设置底部布局
        setting_options = LayoutInflater.from(readingActivity).inflate(R.layout.layout_view_setting_options, null);

        setting_options_content = (LinearLayout) setting_options.findViewById(R.id.setting_options_content);
        setting_options_chapter_previous = (ImageView) setting_options.findViewById(R.id.setting_options_chapter_previous);
        setting_options_chapter_next = (ImageView) setting_options.findViewById(R.id.setting_options_chapter_next);
        setting_options_chapter = (TextView) setting_options.findViewById(R.id.setting_options_chapter);
        setting_options_sequence = (TextView) setting_options.findViewById(R.id.setting_options_sequence);
        setting_options_progress = (SeekBar) setting_options.findViewById(R.id.setting_options_progress);
        setting_options_category = (ImageView) setting_options.findViewById(R.id.setting_options_category);
        setting_options_detail = (ImageView) setting_options.findViewById(R.id.setting_options_detail);
        setting_options_other = (ImageView) setting_options.findViewById(R.id.setting_options_other);
        setting_options_change_mode = (ImageView) setting_options.findViewById(R.id.setting_options_change_mode);


        //阅读页设置详细设置布局
        setting_detail_options = LayoutInflater.from(readingActivity).inflate(R.layout.layout_view_setting_detail_options, null);

        setting_detail_options_content = (LinearLayout) setting_detail_options.findViewById(R.id.setting_detail_options_content);
        setting_detail_options_fount = (TextView) setting_detail_options.findViewById(R.id.setting_detail_options_fount);
        setting_detail_options_fount_decrease = (ImageView) setting_detail_options.findViewById(R.id.setting_detail_options_fount_decrease);
        setting_detail_options_fount_increase = (ImageView) setting_detail_options.findViewById(R.id.setting_detail_options_fount_increase);

        setting_detail_options_fount_view = setting_detail_options.findViewById(R.id.setting_detail_options_fount_view);
        setting_detail_options_fount_10 = (RelativeLayout) setting_detail_options.findViewById(R.id.setting_detail_options_fount_10);
        setting_detail_options_fount_image_10 = (ImageView) setting_detail_options.findViewById(R.id.setting_detail_options_fount_image_10);
        setting_detail_options_fount_15 = (RelativeLayout) setting_detail_options.findViewById(R.id.setting_detail_options_fount_15);
        setting_detail_options_fount_image_15 = (ImageView) setting_detail_options.findViewById(R.id.setting_detail_options_fount_image_15);
        setting_detail_options_fount_20 = (RelativeLayout) setting_detail_options.findViewById(R.id.setting_detail_options_fount_20);
        setting_detail_options_fount_image_20 = (ImageView) setting_detail_options.findViewById(R.id.setting_detail_options_fount_image_20);
        setting_detail_options_fount_25 = (RelativeLayout) setting_detail_options.findViewById(R.id.setting_detail_options_fount_25);
        setting_detail_options_fount_image_25 = (ImageView) setting_detail_options.findViewById(R.id.setting_detail_options_fount_image_25);
        setting_detail_options_fount_30 = (RelativeLayout) setting_detail_options.findViewById(R.id.setting_detail_options_fount_30);
        setting_detail_options_fount_image_30 = (ImageView) setting_detail_options.findViewById(R.id.setting_detail_options_fount_image_30);

        setting_detail_options_spacing = (TextView) setting_detail_options.findViewById(R.id.setting_detail_options_spacing);
        setting_detail_options_spacing_group = (RadioGroup) setting_detail_options.findViewById(R.id.setting_detail_options_spacing_group);
        setting_detail_options_spacing_0_2 = (CustomRadioButton) setting_detail_options.findViewById(R.id.setting_detail_options_spacing_0_2);
        setting_detail_options_spacing_0_5 = (CustomRadioButton) setting_detail_options.findViewById(R.id.setting_detail_options_spacing_0_5);
        setting_detail_options_spacing_1_0 = (CustomRadioButton) setting_detail_options.findViewById(R.id.setting_detail_options_spacing_1_0);
        setting_detail_options_typeface = (TextView) setting_detail_options.findViewById(R.id.setting_detail_options_typeface);
        setting_detail_options_typeface_group = (RadioGroup) setting_detail_options.findViewById(R.id.setting_detail_options_typeface_group);
        setting_detail_options_typeface_song = (RadioButton) setting_detail_options.findViewById(R.id.setting_detail_options_typeface_song);
        setting_detail_options_typeface_system = (RadioButton) setting_detail_options.findViewById(R.id.setting_detail_options_typeface_system);
        setting_detail_options_flip = (TextView) setting_detail_options.findViewById(R.id.setting_detail_options_flip);
        setting_detail_options_flip_group = (RadioGroup) setting_detail_options.findViewById(R.id.setting_detail_options_flip_group);
        setting_detail_options_flip_simulation = (CustomRadioButton) setting_detail_options.findViewById(R.id.setting_detail_options_flip_simulation);
        setting_detail_options_flip_translation = (CustomRadioButton) setting_detail_options.findViewById(R.id.setting_detail_options_flip_translation);
        setting_detail_options_flip_up_down = (CustomRadioButton) setting_detail_options.findViewById(R.id.setting_detail_options_flip_up_down);
        setting_detail_options_change_mode = (ImageView) setting_detail_options.findViewById(R.id.setting_detail_options_change_mode);


        //阅读页设置亮度设置布局
        setting_other_options = LayoutInflater.from(readingActivity).inflate(R.layout.layout_view_setting_other_options, null);
        setting_other_options_content = (LinearLayout) setting_other_options.findViewById(R.id.setting_other_options_content);
        setting_other_options_brightness_down = (ImageView) setting_other_options.findViewById(R.id.setting_other_options_brightness_down);
        setting_other_options_brightness_progress = (SeekBar) setting_other_options.findViewById(R.id.setting_other_options_brightness_progress);
        setting_other_options_brightness_up = (ImageView) setting_other_options.findViewById(R.id.setting_other_options_brightness_up);
        setting_other_options_brightness_follow = (Button) setting_other_options.findViewById(R.id.setting_other_options_brightness_follow);
        setting_other_options_background = (RadioGroup) setting_other_options.findViewById(R.id.setting_other_options_background);
        setting_other_options_background_first = (RadioButton) setting_other_options.findViewById(R.id.setting_other_options_background_first);
        setting_other_options_background_second = (RadioButton) setting_other_options.findViewById(R.id.setting_other_options_background_second);
        setting_other_options_background_third = (RadioButton) setting_other_options.findViewById(R.id.setting_other_options_background_third);
        setting_other_options_background_fourth = (RadioButton) setting_other_options.findViewById(R.id.setting_other_options_background_fourth);
        setting_other_options_background_fifth = (RadioButton) setting_other_options.findViewById(R.id.setting_other_options_background_fifth);
        setting_other_options_background_sixth = (RadioButton) setting_other_options.findViewById(R.id.setting_other_options_background_sixth);
        setting_other_options_change_mode = (ImageView) setting_other_options.findViewById(R.id.setting_other_options_change_mode);


        int resource = ResourceUtil.loadResourceID(readingActivity, Constants.DRAWABLE, "_reading_mode");

        setting_options_change_mode.setBackgroundResource(resource);
        setting_detail_options_change_mode.setBackgroundResource(resource);
        setting_other_options_change_mode.setBackgroundResource(resource);


        darkProgressThumb = readingActivity.getResources().getDrawable(R.drawable.icon_dark_progress_thumb);
        lightProgressThumb = readingActivity.getResources().getDrawable(R.drawable.icon_light_progress_thumb);

        int[] darkColors = new int[]{Color.parseColor("#0094D5"), Color.parseColor("#0094D5"), Color.parseColor("#0094D5"), Color.parseColor("#9B9B9B")};
        int[] lightColors = new int[]{Color.parseColor("#0094D5"), Color.parseColor("#0094D5"), Color.parseColor("#0094D5"), Color.parseColor("#3E3E3E")};

        int[][] states = new int[4][];
        states[0] = new int[]{android.R.attr.state_pressed};
        states[1] = new int[]{android.R.attr.state_checked};
        states[2] = new int[]{android.R.attr.state_selected};
        states[3] = new int[]{};

        darkColorStateList = new ColorStateList(states, darkColors);
        lightColorStateList = new ColorStateList(states, lightColors);

        this.addView(setting_options);
        this.addView(setting_detail_options);
        this.addView(setting_other_options);

        changeHeadViewState();
        changeBottomViewState(SETTING_DEFAULT);

        displayAnimation = AnimationUtils.loadAnimation(readingActivity, R.anim.anim_display);
        dismissAnimation = AnimationUtils.loadAnimation(readingActivity, R.anim.anim_dismiss);

        setting_other_options_brightness_progress.setMax(255);

        if (brightnessFollowSystem) {
            startSystemBrightness();
        } else {
            closeSystemBrightness();
        }

        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMaximumFractionDigits(2);

        BaseConfig.READING_INTERLINEAR_SPACE = sharedPreferences.getInt(BaseConfig.FLAG_READING_INTERLINEAR_SPACE, 3) * 0.1f + 0.2f;
        BaseConfig.READING_TEXT_SIZE = sharedPreferences.getInt(BaseConfig.FLAG_READING_TEXT_SIZE, 15);

        try {
            BaseConfig.READING_INTERLINEAR_SPACE = Float.valueOf(numberFormat.format(BaseConfig.READING_INTERLINEAR_SPACE));
        } catch (NumberFormatException numberFormatException) {
            numberFormatException.printStackTrace();
        }

        initializeInterlinearSpace();
        initializeTypeface();
        initializeFlipMode();
        initializeBackground();
        changeFontSizeState();

        initializeListener();
    }

    private void changeHeadViewState() {
        readingActivity.hideHeadView();
    }

    private void changeBottomViewState(int view) {

        settingView = view;

        initializeSettingView();

        switch (settingView) {
            case SETTING_OPTION:
                setting_options.setVisibility(View.VISIBLE);
                setting_other_options.setVisibility(View.GONE);
                setting_detail_options.setVisibility(View.GONE);
                break;
            case SETTING_DETAIL:
                setting_options.setVisibility(View.GONE);
                setting_other_options.setVisibility(View.GONE);
                setting_detail_options.setVisibility(View.VISIBLE);
                break;
            case SETTING_OTHER:
                setting_options.setVisibility(View.GONE);
                setting_other_options.setVisibility(View.VISIBLE);
                setting_detail_options.setVisibility(View.GONE);
                break;
            default:
                setting_options.setVisibility(View.GONE);
                setting_other_options.setVisibility(View.GONE);
                setting_detail_options.setVisibility(View.GONE);
                break;
        }
    }

    private void startSystemBrightness() {
        resetBrightnessFollowSystem(true);

        saveFollowSystem(true);

        resetScreenBrightness(loadSystemBrightness());
    }

    private void closeSystemBrightness() {
        resetBrightnessFollowSystem(false);

        saveFollowSystem(false);

        int brightness = sharedPreferences.getInt(BaseConfig.FLAG_SCREEN_BRIGHTNESS, -1);

        resetScreenBrightness(brightness);
    }

    private void saveFollowSystem(boolean state) {

        if (sharedPreferences == null) {
            this.sharedPreferences = readingActivity.getSharedPreferences(BaseConfig.FLAG_SHARE_PREFERENCE_NAME, Context.MODE_PRIVATE);
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(BaseConfig.FLAG_BRIGHTNESS_FOLLOW_SYSTEM, state);
        editor.apply();
    }

    private void resetBrightnessFollowSystem(boolean state) {
        this.brightnessFollowSystem = state;
        setting_other_options_brightness_follow.setSelected(state);
    }

    private int loadSystemBrightness() {
        int brightness = -1;
        try {
            brightness = Settings.System.getInt(readingActivity.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException settingNotFoundException) {
            settingNotFoundException.printStackTrace();
        }
        return brightness;
    }

    private void resetScreenBrightness(int parameter) {
        if (parameter >= 0) {
            initializeScreenBrightness(parameter);
        } else {
            initializeScreenBrightness(loadCurrentBrightness());
        }
    }

    public void initializeScreenBrightness(int brightness) {
        Window localWindow = readingActivity.getWindow();
        WindowManager.LayoutParams layoutParams = localWindow.getAttributes();
        layoutParams.screenBrightness = (float) brightness / 255.0F;
        localWindow.setAttributes(layoutParams);

        if (setting_other_options_brightness_progress != null) {
            setting_other_options_brightness_progress.setProgress(brightness);
        }
    }

    private int loadCurrentBrightness() {
        int brightness = 0;
        ContentResolver contentResolver = readingActivity.getContentResolver();
        try {
            brightness = android.provider.Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return brightness;
    }

    private void initializeInterlinearSpace() {
        if (BaseConfig.READING_INTERLINEAR_SPACE == 0.2f) {
            setting_detail_options_spacing_group.check(R.id.setting_detail_options_spacing_0_2);
            saveInterlinearSpace(0);
        } else if (BaseConfig.READING_INTERLINEAR_SPACE == 0.5f) {
            setting_detail_options_spacing_group.check(R.id.setting_detail_options_spacing_0_5);
            saveInterlinearSpace(3);
        } else if (BaseConfig.READING_INTERLINEAR_SPACE == 1.0f) {
            setting_detail_options_spacing_group.check(R.id.setting_detail_options_spacing_1_0);
            saveInterlinearSpace(8);
        }
    }

    private void saveInterlinearSpace(int space) {
        if (sharedPreferences == null) {
            this.sharedPreferences = readingActivity.getSharedPreferences(BaseConfig.FLAG_SHARE_PREFERENCE_NAME, Context.MODE_PRIVATE);
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(BaseConfig.FLAG_READING_INTERLINEAR_SPACE, space);
        editor.apply();
    }

    private void initializeTypeface() {

        BaseConfig.READING_TYPEFACE = sharedPreferences.getInt(BaseConfig.FLAG_READING_TYPEFACE, 0);

        if (BaseConfig.READING_TYPEFACE == TypefaceUtil.TYPEFACE_SONG) {
            setting_detail_options_typeface_group.check(R.id.setting_detail_options_typeface_song);
            saveTypeface(TypefaceUtil.TYPEFACE_SONG);
        } else if (BaseConfig.READING_TYPEFACE == TypefaceUtil.TYPEFACE_SYSTEM) {
            setting_detail_options_typeface_group.check(R.id.setting_detail_options_typeface_system);
            saveTypeface(TypefaceUtil.TYPEFACE_SYSTEM);
        }
    }

    private void saveTypeface(int type) {
        if (sharedPreferences == null) {
            sharedPreferences = readingActivity.getSharedPreferences(BaseConfig.FLAG_SHARE_PREFERENCE_NAME, Context.MODE_PRIVATE);
        }

        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt(BaseConfig.FLAG_READING_TYPEFACE, type);
        edit.apply();
    }

    private void initializeFlipMode() {
        if (BaseConfig.READING_FLIP_MODE == 1) {
            setting_detail_options_flip_group.check(R.id.setting_detail_options_flip_simulation);
        } else if (BaseConfig.READING_FLIP_MODE == 2) {
            setting_detail_options_flip_group.check(R.id.setting_detail_options_flip_translation);
        } else if (BaseConfig.READING_FLIP_MODE == 3) {
            setting_detail_options_flip_group.check(R.id.setting_detail_options_flip_up_down);
        }
    }

    public void initializeBackground() {

        if (sharedPreferences == null) {
            sharedPreferences = readingActivity.getSharedPreferences(BaseConfig.FLAG_SHARE_PREFERENCE_NAME, Context.MODE_PRIVATE);
        }

        int background = sharedPreferences.getInt(BaseConfig.FLAG_READING_BACKGROUND_MODE, 1);

        resetBackgroundMode(background);
    }

    private void initializeListener() {
        setting_options_chapter_previous.setOnClickListener(this);
        setting_options_chapter_next.setOnClickListener(this);
        setting_options_progress.setOnSeekBarChangeListener(this);
        setting_options_category.setOnClickListener(this);
        setting_options_detail.setOnClickListener(this);
        setting_options_other.setOnClickListener(this);
        setting_options_change_mode.setOnClickListener(this);

        setting_detail_options_fount_decrease.setOnClickListener(this);
        setting_detail_options_fount_increase.setOnClickListener(this);

        setting_detail_options_fount_10.setOnClickListener(this);
        setting_detail_options_fount_15.setOnClickListener(this);
        setting_detail_options_fount_20.setOnClickListener(this);
        setting_detail_options_fount_25.setOnClickListener(this);
        setting_detail_options_fount_30.setOnClickListener(this);

        setting_detail_options_spacing_group.setOnCheckedChangeListener(this);
        setting_detail_options_typeface_group.setOnCheckedChangeListener(this);
        setting_detail_options_flip_group.setOnCheckedChangeListener(this);
        setting_detail_options_change_mode.setOnClickListener(this);

        setting_other_options_brightness_progress.setOnSeekBarChangeListener(this);
        setting_other_options_brightness_follow.setOnClickListener(this);
        setting_other_options_background.setOnCheckedChangeListener(this);
        setting_other_options_change_mode.setOnClickListener(this);
    }

    private void resetBackgroundMode(int mode) {

        saveBackgroundMode(mode);

        switch (mode) {
            case 1:
                setting_other_options_background.check(R.id.setting_other_options_background_first);
                break;
            case 2:
                setting_other_options_background.check(R.id.setting_other_options_background_second);
                break;
            case 3:
                setting_other_options_background.check(R.id.setting_other_options_background_third);
                break;
            case 4:
                setting_other_options_background.check(R.id.setting_other_options_background_fourth);
                break;
            case 5:
                setting_other_options_background.check(R.id.setting_other_options_background_fifth);
                break;
            case 6:
                setting_other_options_background.check(R.id.setting_other_options_background_sixth);
                break;
            default:
                break;
        }

        initializeSettingView();
    }

    private void saveBackgroundMode(int mode) {

        if (sharedPreferences == null) {
            sharedPreferences = readingActivity.getSharedPreferences(BaseConfig.FLAG_SHARE_PREFERENCE_NAME, Context.MODE_PRIVATE);
        }

        BaseConfig.READING_BACKGROUND_MODE = mode;

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(BaseConfig.FLAG_READING_BACKGROUND_MODE, BaseConfig.READING_BACKGROUND_MODE);
        editor.apply();

        changeBackgroundMode(mode);
    }

    private void changeBackgroundMode(int mode) {
        if (readSettingListener != null) {
            readSettingListener.onBackgroundModeChanged(mode);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.setting_options_chapter_previous:
                if (readSettingListener != null) {
                    readSettingListener.onTurnToPreviousChapter();
                }
                break;
            case R.id.setting_options_chapter_next:
                if (readSettingListener != null) {
                    readSettingListener.onTurnToNextChapter();
                }
                break;
            case R.id.setting_options_category:
                if (readSettingListener != null) {
                    readSettingListener.startCatalogActivity();
                }
                break;
            case R.id.setting_options_detail:
                changeBottomViewState(SETTING_DETAIL);
                break;
            case R.id.setting_options_other:
                changeBottomViewState(SETTING_OTHER);
                break;
            case R.id.setting_detail_options_fount_decrease:
                decreaseTextSize();
                break;
            case R.id.setting_detail_options_fount_increase:
                increaseTextSize();
                break;
            case R.id.setting_detail_options_fount_10:
                changeFontSize(10);
                break;
            case R.id.setting_detail_options_fount_15:
                changeFontSize(15);
                break;
            case R.id.setting_detail_options_fount_20:
                changeFontSize(20);
                break;
            case R.id.setting_detail_options_fount_25:
                changeFontSize(25);
                break;
            case R.id.setting_detail_options_fount_30:
                changeFontSize(30);
                break;
            case R.id.setting_other_options_brightness_follow:
                changeSystemBrightness();
                break;
            case R.id.setting_options_change_mode:
            case R.id.setting_detail_options_change_mode:
            case R.id.setting_other_options_change_mode:
                changeNightMode();
                break;
            default:
                break;
        }
    }

    private void changeFontSize(int size) {

        BaseConfig.READING_TEXT_SIZE = size;

        changeFontSizeState();

        saveTextSize();

        if (readSettingListener != null) {
            readSettingListener.refreshReadingPage();
        }
    }

    private void changeFontSizeState() {

        setting_detail_options_fount_10.setSelected(BaseConfig.READING_TEXT_SIZE == 10);

        setting_detail_options_fount_15.setSelected(BaseConfig.READING_TEXT_SIZE == 15);

        setting_detail_options_fount_20.setSelected(BaseConfig.READING_TEXT_SIZE == 20);

        setting_detail_options_fount_25.setSelected(BaseConfig.READING_TEXT_SIZE == 25);

        setting_detail_options_fount_30.setSelected(BaseConfig.READING_TEXT_SIZE == 30);
    }

    private void decreaseTextSize() {
        if (BaseConfig.READING_TEXT_SIZE > 10) {

            BaseConfig.READING_TEXT_SIZE -= 5;

            changeFontSizeState();

            saveTextSize();

            if (readSettingListener != null) {
                readSettingListener.refreshReadingPage();
            }
        } else {
            readingActivity.showPromptMessage("已是最小字体！");
        }
    }

    private void saveTextSize() {

        if (sharedPreferences == null) {
            sharedPreferences = readingActivity.getSharedPreferences(BaseConfig.FLAG_SHARE_PREFERENCE_NAME, Context.MODE_PRIVATE);
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(BaseConfig.FLAG_READING_TEXT_SIZE, BaseConfig.READING_TEXT_SIZE);
        editor.apply();
    }

    private void increaseTextSize() {
        if (BaseConfig.READING_TEXT_SIZE < 30) {

            BaseConfig.READING_TEXT_SIZE += 5;

            changeFontSizeState();

            saveTextSize();

            if (readSettingListener != null) {
                readSettingListener.refreshReadingPage();
            }
        } else {
            readingActivity.showPromptMessage("已是最大字体！");
        }
    }

    private void changeSystemBrightness() {
        if (brightnessFollowSystem) {
            closeSystemBrightness();
        } else {
            startSystemBrightness();
        }
    }

    private void changeNightMode() {

        if (sharedPreferences == null) {
            sharedPreferences = readingActivity.getSharedPreferences(BaseConfig.FLAG_SHARE_PREFERENCE_NAME, Context.MODE_PRIVATE);
        }

        int fromMode = sharedPreferences.getInt(BaseConfig.READING_BACKGROUND_MODE + BaseConfig.FLAG_READING_NIGHT_MODE_FROM, 0);

        if (fromMode == 0) {
            fromMode = loadDefaultFromMode();
        }

        saveNightModeFrom(fromMode);

        resetBackgroundMode(fromMode);
    }

    private int loadDefaultFromMode() {
        if (BaseConfig.READING_BACKGROUND_MODE == 6) {
            return 1;
        } else {
            return 6;
        }
    }

    private void saveNightModeFrom(int mode) {

        if (sharedPreferences == null) {
            sharedPreferences = readingActivity.getSharedPreferences(BaseConfig.FLAG_SHARE_PREFERENCE_NAME, Context.MODE_PRIVATE);
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(mode + BaseConfig.FLAG_READING_NIGHT_MODE_FROM, BaseConfig.READING_BACKGROUND_MODE);
        editor.apply();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.setting_detail_options_spacing_0_2:
                if (setting_detail_options_spacing_0_2.isChecked()) {
                    BaseConfig.READING_INTERLINEAR_SPACE = 0.2f;
                    saveInterlinearSpace(0);
                    resetInterlinearSpace();
                }
                break;
            case R.id.setting_detail_options_spacing_0_5:
                if (setting_detail_options_spacing_0_5.isChecked()) {
                    BaseConfig.READING_INTERLINEAR_SPACE = 0.5f;
                    saveInterlinearSpace(3);
                    resetInterlinearSpace();
                }
                break;
            case R.id.setting_detail_options_spacing_1_0:
                if (setting_detail_options_spacing_1_0.isChecked()) {
                    BaseConfig.READING_INTERLINEAR_SPACE = 1.0f;
                    saveInterlinearSpace(8);
                    resetInterlinearSpace();
                }
                break;
            case R.id.setting_detail_options_typeface_song:
                resetTypeface(TypefaceUtil.TYPEFACE_SONG);
                break;
            case R.id.setting_detail_options_typeface_system:
                resetTypeface(TypefaceUtil.TYPEFACE_SYSTEM);
                break;

            case R.id.setting_detail_options_flip_simulation:
                changeFlipMode(1);
                break;
            case R.id.setting_detail_options_flip_translation:
                changeFlipMode(2);
                break;
            case R.id.setting_detail_options_flip_up_down:
                changeFlipMode(3);
                break;
            case R.id.setting_other_options_background_first:
                resetBackgroundMode(1);
                break;
            case R.id.setting_other_options_background_second:
                resetBackgroundMode(2);
                break;
            case R.id.setting_other_options_background_third:
                resetBackgroundMode(3);
                break;
            case R.id.setting_other_options_background_fourth:
                resetBackgroundMode(4);
                break;
            case R.id.setting_other_options_background_fifth:
                resetBackgroundMode(5);
                break;
            case R.id.setting_other_options_background_sixth:
                resetBackgroundMode(6);
                break;
            default:
                break;
        }
    }

    public void resetInterlinearSpace() {
        if (readStatus != null) {
            int interimOffset = readStatus.offset;

            if (readSettingListener != null) {
                readSettingListener.refreshReadingPage();
            }
            readStatus.offset = interimOffset;
        }
    }

    private void resetTypeface(int type) {
        if (readSettingListener != null) {
            saveTypeface(type);

            BaseConfig.READING_TYPEFACE = type;

            Typeface typeface = TypefaceUtil.loadTypeface(readingActivity, type);

            readSettingListener.onTypefaceChanged(typeface);

            readSettingListener.refreshReadingPage();
        }
    }

    private void changeFlipMode(int mode) {

        if (System.currentTimeMillis() - intervalTime < 500) {
            return;
        }

        intervalTime = System.currentTimeMillis();

        saveFlipMode(mode);

        Intent intent = new Intent(readingActivity, ReadingActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("book", readStatus.book);
        bundle.putInt("sequence", readStatus.sequence);
        bundle.putInt("offset", readStatus.offset);
        intent.putExtras(bundle);
        readingActivity.startActivity(intent);

        BaseConfig.READING_FLIP_MODE = mode;
    }

    public void saveFlipMode(int mode) {

        if (sharedPreferences == null) {
            sharedPreferences = readingActivity.getSharedPreferences(BaseConfig.FLAG_SHARE_PREFERENCE_NAME, Context.MODE_PRIVATE);
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(BaseConfig.FLAG_READING_FLIP_MODE, mode);
        editor.apply();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser && (seekBar.getId() == R.id.setting_options_progress)) {

            final int currentProgress = progress * (readStatus.chapter_count - 1) / 100;

            if (dataFactory.chapterList != null && !dataFactory.chapterList.isEmpty() && currentProgress < dataFactory.chapterList.size() && currentProgress >= 0) {

                readStatus.novel_progress = currentProgress;

                setting_options_chapter.setText(dataFactory.chapterList.get(currentProgress).name);
                setting_options_sequence.setText((currentProgress + 1) + "/" + readStatus.chapter_count);
            }

        } else if (fromUser && (seekBar.getId() == R.id.setting_other_options_brightness_progress)) {
            if (brightnessFollowSystem) {

                resetFollowSystemState(false);
                readingActivity.setReaderDisplayBrightness();

                saveFollowSystem(false);

                int brightness = loadSystemBrightness();

                if (brightness >= 0) {
                    initializeScreenBrightness(brightness);
                } else {
                    int current_brightness = loadCurrentBrightness();
                    initializeScreenBrightness(current_brightness);
                }
            }

            int brightness_progress = seekBar.getProgress();
            initializeScreenBrightness(brightness_progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        NumberFormat numFormat = NumberFormat.getNumberInstance();
        numFormat.setMaximumFractionDigits(2);
        if (seekBar.getId() == R.id.setting_options_progress) {

            if (readStatus == null || readStatus.novel_progress == readStatus.sequence) {
                return;
            }

            if (readStatus.book.book_type == Book.TYPE_ONLINE && NetworkUtil.NETWORK_NONE == NetworkUtil.loadNetworkType(readingActivity)) {
                if (!CommunalUtil.checkChapterExist(dataFactory.chapterList.get(readStatus.novel_progress).id, readStatus.book_id)) {
                    readingActivity.showReadToast("重新加载！");
                    return;
                }
            }

            if (readSettingListener != null) {
                readSettingListener.onTurnToChapter();
            }

        } else if (seekBar.getId() == R.id.setting_other_options_brightness_progress) {
            saveScreenBrightness(seekBar.getProgress());
        }
    }

    private void resetFollowSystemState(boolean state) {
        this.brightnessFollowSystem = state;
        resetFollowSystemSelected();
    }

    private void resetFollowSystemSelected() {
        if (brightnessFollowSystem) {
            setting_other_options_brightness_follow.setSelected(true);
        } else {
            setting_other_options_brightness_follow.setSelected(false);
        }
    }

    private void saveScreenBrightness(int brightness) {

        if (sharedPreferences == null) {
            sharedPreferences = readingActivity.getSharedPreferences(BaseConfig.FLAG_SHARE_PREFERENCE_NAME, Context.MODE_PRIVATE);
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (editor != null) {
            editor.putInt(BaseConfig.FLAG_SCREEN_BRIGHTNESS, brightness);
            editor.apply();
        }
    }


    public void showSettingView(boolean show) {
        if (show) {
            if (setting_options != null) {
                setting_options.startAnimation(displayAnimation);
                changeBottomViewState(SETTING_OPTION);
            }

            if (readStatus != null && setting_options_progress != null) {
                if (readStatus.chapter_count - 1 <= 0 || readStatus.chapter_count - 1 < readStatus.sequence) {
                    setting_options_progress.setProgress(0);
                } else {
                    int index = Math.max(readStatus.sequence, 0);
                    setting_options_progress.setProgress(index * 100 / (readStatus.chapter_count - 1));
                }
                changeChapterState();
            }

        } else {
            if (setting_options != null && setting_options.isShown()) {
                setting_options.startAnimation(dismissAnimation);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        changeBottomViewState(SETTING_DEFAULT);
                    }
                }, 500);
            }

            if (setting_detail_options != null && setting_detail_options.isShown()) {
                setting_detail_options.startAnimation(dismissAnimation);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        changeBottomViewState(SETTING_DEFAULT);
                    }
                }, 500);
            }

            if (setting_other_options != null && setting_other_options.isShown()) {
                setting_other_options.startAnimation(dismissAnimation);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        changeBottomViewState(SETTING_DEFAULT);
                    }
                }, 500);
            }
        }
    }

    private void changeChapterState() {
        if (setting_options_chapter != null) {
            setting_options_chapter.setText(TextUtils.isEmpty(readStatus.chapter_name) ? "青果阅读" : readStatus.chapter_name);
        }

        if (setting_options_sequence != null) {
            setting_options_sequence.setText((readStatus.sequence + 1) + "/" + readStatus.chapter_count + "章");
        }
    }

    private void initializeSettingView() {
        switch (settingView) {
            case SETTING_OPTION:
                changeOptionsViewState();
                break;
            case SETTING_DETAIL:
                changeOptionsDetailViewState();
                break;
            case SETTING_OTHER:
                changeOptionsOtherViewState();
                break;
        }
    }

    private void changeOptionsViewState() {

        boolean viewState = (BaseConfig.READING_BACKGROUND_MODE == 5 || BaseConfig.READING_BACKGROUND_MODE == 6);

        if (setting_options_content != null) {
            setting_options_content.setBackgroundColor(viewState ? Color.parseColor("#252424") : Color.parseColor("#FFFFFF"));
        }

        if (setting_options_chapter_previous != null) {
            setting_options_chapter_previous.setImageResource(viewState ? R.drawable.selector_dark_chapter_previous : R.drawable.selector_light_chapter_previous);
        }

        if (setting_options_chapter_next != null) {
            setting_options_chapter_next.setImageResource(viewState ? R.drawable.selector_dark_chapter_next : R.drawable.selector_light_chapter_next);
        }

        if (setting_options_chapter != null) {
            setting_options_chapter.setTextColor(viewState ? Color.parseColor("#9B9B9B") : Color.parseColor("#686868"));
        }

        if (setting_options_sequence != null) {
            setting_options_sequence.setTextColor(viewState ? Color.parseColor("#9B9B9B") : Color.parseColor("#686868"));
        }

        if (setting_options_progress != null) {
            setting_options_progress.setThumb(viewState ? darkProgressThumb : lightProgressThumb);
        }

        if (setting_options_category != null) {
            setting_options_category.setImageResource(viewState ? R.drawable.selector_dark_category : R.drawable.selector_light_category);
        }

        if (setting_options_detail != null) {
            setting_options_detail.setImageResource(viewState ? R.drawable.selector_dark_setting_detail : R.drawable.selector_light_setting_detail);
        }

        if (setting_options_other != null) {
            setting_options_other.setImageResource(viewState ? R.drawable.selector_dark_setting_other : R.drawable.selector_light_setting_other);
        }
    }


    private void changeOptionsDetailViewState() {

        boolean viewState = (BaseConfig.READING_BACKGROUND_MODE == 5 || BaseConfig.READING_BACKGROUND_MODE == 6);

        if (setting_detail_options_content != null) {
            setting_detail_options_content.setBackgroundColor(viewState ? Color.parseColor("#252424") : Color.parseColor("#FFFFFF"));
        }

        if (setting_detail_options_fount != null) {
            setting_detail_options_fount.setTextColor(viewState ? Color.parseColor("#9B9B9B") : Color.parseColor("#686868"));
        }

        if (setting_detail_options_fount_view != null) {
            setting_detail_options_fount_view.setBackgroundColor(viewState ? Color.parseColor("#9B9B9B") : Color.parseColor("#D8D8D8"));
        }

        if (setting_detail_options_fount_image_10 != null) {
            setting_detail_options_fount_image_10.setImageResource(viewState ? R.drawable.selector_dark_font : R.drawable.selector_light_font);
        }

        if (setting_detail_options_fount_image_15 != null) {
            setting_detail_options_fount_image_15.setImageResource(viewState ? R.drawable.selector_dark_font : R.drawable.selector_light_font);
        }

        if (setting_detail_options_fount_image_20 != null) {
            setting_detail_options_fount_image_20.setImageResource(viewState ? R.drawable.selector_dark_font : R.drawable.selector_light_font);
        }

        if (setting_detail_options_fount_image_25 != null) {
            setting_detail_options_fount_image_25.setImageResource(viewState ? R.drawable.selector_dark_font : R.drawable.selector_light_font);
        }

        if (setting_detail_options_fount_image_30 != null) {
            setting_detail_options_fount_image_30.setImageResource(viewState ? R.drawable.selector_dark_font : R.drawable.selector_light_font);
        }

        if (setting_detail_options_spacing != null) {
            setting_detail_options_spacing.setTextColor(viewState ? Color.parseColor("#9B9B9B") : Color.parseColor("#686868"));
        }

        if (setting_detail_options_spacing_0_2 != null) {
            setting_detail_options_spacing_0_2.setBackgroundResource(viewState ? R.drawable.selector_dark_radio_button_left : R.drawable.selector_light_radio_button_left);
        }

        if (setting_detail_options_spacing_0_5 != null) {
            setting_detail_options_spacing_0_5.setBackgroundResource(viewState ? R.drawable.selector_dark_radio_button_middle : R.drawable.selector_light_radio_button_middle);
        }

        if (setting_detail_options_spacing_1_0 != null) {
            setting_detail_options_spacing_1_0.setBackgroundResource(viewState ? R.drawable.selector_dark_radio_button_right : R.drawable.selector_light_radio_button_right);
        }

        if (setting_detail_options_typeface != null) {
            setting_detail_options_typeface.setTextColor(viewState ? Color.parseColor("#9B9B9B") : Color.parseColor("#686868"));
        }

        if (setting_detail_options_typeface_song != null) {
            setting_detail_options_typeface_song.setBackgroundResource(viewState ? R.drawable.selector_dark_radio_button : R.drawable.selector_light_radio_button);
            setting_detail_options_typeface_song.setTextColor(viewState ? darkColorStateList : lightColorStateList);
        }

        if (setting_detail_options_typeface_system != null) {
            setting_detail_options_typeface_system.setBackgroundResource(viewState ? R.drawable.selector_dark_radio_button : R.drawable.selector_light_radio_button);
            setting_detail_options_typeface_system.setTextColor(viewState ? darkColorStateList : lightColorStateList);
        }

        if (setting_detail_options_flip != null) {
            setting_detail_options_flip.setTextColor(viewState ? Color.parseColor("#9B9B9B") : Color.parseColor("#686868"));
        }

        if (setting_detail_options_flip_simulation != null) {
            setting_detail_options_flip_simulation.setBackgroundResource(viewState ? R.drawable.selector_dark_radio_button_left : R.drawable.selector_light_radio_button_left);
            setting_detail_options_flip_simulation.setTextColor(viewState ? darkColorStateList : lightColorStateList);
        }

        if (setting_detail_options_flip_translation != null) {
            setting_detail_options_flip_translation.setBackgroundResource(viewState ? R.drawable.selector_dark_radio_button_middle : R.drawable.selector_light_radio_button_middle);
            setting_detail_options_flip_translation.setTextColor(viewState ? darkColorStateList : lightColorStateList);
        }

        if (setting_detail_options_flip_up_down != null) {
            setting_detail_options_flip_up_down.setBackgroundResource(viewState ? R.drawable.selector_dark_radio_button_right : R.drawable.selector_light_radio_button_right);
            setting_detail_options_flip_up_down.setTextColor(viewState ? darkColorStateList : lightColorStateList);
        }
    }

    private void changeOptionsOtherViewState() {

        boolean viewState = (BaseConfig.READING_BACKGROUND_MODE == 5 || BaseConfig.READING_BACKGROUND_MODE == 6);

        if (setting_other_options_content != null) {
            setting_other_options_content.setBackgroundColor(viewState ? Color.parseColor("#252424") : Color.parseColor("#FFFFFF"));
        }

        if (setting_other_options_brightness_progress != null) {
            setting_other_options_brightness_progress.setThumb(viewState ? darkProgressThumb : lightProgressThumb);
        }

        if (setting_other_options_brightness_follow != null) {
            setting_other_options_brightness_follow.setBackgroundResource(viewState ? R.drawable.selector_dark_radio_button : R.drawable.selector_light_radio_button);
            setting_other_options_brightness_follow.setTextColor(viewState ? darkColorStateList : lightColorStateList);
        }
    }


    public void changeChapter() {
        if (setting_options_progress != null && setting_options_progress.isShown() && readStatus.chapter_count - 1 != 0) {
            int index = Math.max(readStatus.sequence, 0);
            setting_options_progress.setProgress(index * 100 / (readStatus.chapter_count - 1));
        }
        changeChapterState();
    }

    public void resetNightMode() {
        Resources resources = getResources();
        if (resources == null || readingActivity.isFinishing()) {
            return;
        }

        int resource = ResourceUtil.loadResourceID(readingActivity, Constants.DRAWABLE, "_reading_mode");

        setting_options_change_mode.setBackgroundResource(resource);
        setting_detail_options_change_mode.setBackgroundResource(resource);
        setting_other_options_change_mode.setBackgroundResource(resource);
    }

    public void recycleResource() {

        this.detachAllViewsFromParent();

        if (this.readingActivity != null) {
            this.readingActivity = null;
        }

        if (this.dataFactory != null) {
            this.dataFactory = null;
        }

        if (this.readStatus != null) {
            this.readStatus = null;
        }

        if (setting_other_options_background != null) {
            setting_other_options_background.removeAllViews();
            setting_other_options_background = null;
        }

    }

    public void setReadSettingListener(ReadSettingListener readSettingListener) {
        this.readSettingListener = readSettingListener;
    }

    public void setDataFactory(IReadDataFactory factory, ReadStatus readStatus) {
        this.dataFactory = factory;
        this.readStatus = readStatus;
    }

    public interface ReadSettingListener {

        void onBackgroundModeChanged(int mode);

        void onTurnToPreviousChapter();

        void onTurnToNextChapter();

        void onTurnToChapter();

        void startCatalogActivity();

        void refreshReadingPage();

        void onTypefaceChanged(Typeface typeface);
    }
}