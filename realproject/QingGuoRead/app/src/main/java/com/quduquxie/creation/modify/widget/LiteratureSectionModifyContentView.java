package com.quduquxie.creation.modify.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.quduquxie.R;
import com.quduquxie.creation.modify.widget.utils.ContentTextWatcher;
import com.quduquxie.creation.modify.widget.utils.CustomInputFilter;
import com.quduquxie.creation.modify.widget.utils.LiteratureSectionModifyContentListener;
import com.quduquxie.creation.modify.widget.utils.LiteratureSectionModifyListener;
import com.quduquxie.creation.modify.widget.utils.TitleTextWatcher;
import com.quduquxie.creation.write.widget.CustomHorizontalScrollView;
import com.quduquxie.model.v2.Rank;
import com.quduquxie.model.v2.SensitiveWord;
import com.quduquxie.util.QGLog;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created on 16/11/18.
 * Created by crazylei.
 */

public class LiteratureSectionModifyContentView extends RelativeLayout implements LiteratureSectionModifyListener, TitleTextWatcher.TitleCountChangeListener, ContentTextWatcher.ContentCountChangeListener, View.OnClickListener {

    private static final String TAG = LiteratureSectionModifyContentView.class.getSimpleName();

    private EditText literature_write_title_input;
    private TextView literature_write_title_length;
    private TextView literature_write_title_limit;

    private EditText literature_write_content;

    private CustomHorizontalScrollView literature_write_util_punctuation_view;

    private ImageView literature_write_util_punctuation_more;

    private ImageView literature_write_util_keyboard;

    private LiteratureSectionModifyContentListener literatureSectionModifyContentListener;

    private boolean keyword_closed = true;

    public LiteratureSectionModifyContentView(Context context) {
        super(context);
        initView(context);
    }

    public LiteratureSectionModifyContentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public LiteratureSectionModifyContentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public LiteratureSectionModifyContentView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_view_literature_write, this, true);

        final RelativeLayout literature_write_main = (RelativeLayout) findViewById(R.id.literature_write_main);

        literature_write_title_input = (EditText) findViewById(R.id.literature_write_title_input);
        literature_write_title_length = (TextView) findViewById(R.id.literature_write_title_length);
        literature_write_title_limit = (TextView) findViewById(R.id.literature_write_title_limit);

        literature_write_content = (EditText) findViewById(R.id.literature_write_content);

        literature_write_util_punctuation_view = (CustomHorizontalScrollView) findViewById(R.id.literature_write_util_punctuation_view);
        ImageView literature_write_util_punctuation_comma = (ImageView) findViewById(R.id.literature_write_util_punctuation_comma);
        ImageView literature_write_util_punctuation_period = (ImageView) findViewById(R.id.literature_write_util_punctuation_period);
        ImageView literature_write_util_punctuation_question_mark = (ImageView) findViewById(R.id.literature_write_util_punctuation_question_mark);
        ImageView literature_write_util_punctuation_exclamation_mark = (ImageView) findViewById(R.id.literature_write_util_punctuation_exclamation_mark);
        ImageView literature_write_util_punctuation_colon = (ImageView) findViewById(R.id.literature_write_util_punctuation_colon);
        ImageView literature_write_util_punctuation_double_quotation_marks = (ImageView) findViewById(R.id.literature_write_util_punctuation_double_quotation_marks);
        ImageView literature_write_util_punctuation_stop = (ImageView) findViewById(R.id.literature_write_util_punctuation_stop);
        ImageView literature_write_util_punctuation_semicolon = (ImageView) findViewById(R.id.literature_write_util_punctuation_semicolon);

        literature_write_util_punctuation_more = (ImageView) findViewById(R.id.literature_write_util_punctuation_more);

        ImageView literature_write_util_automatic_typesetting = (ImageView) findViewById(R.id.literature_write_util_automatic_typesetting);
        ImageView literature_write_util_cursor_left = (ImageView) findViewById(R.id.literature_write_util_cursor_left);
        ImageView literature_write_util_cursor_right = (ImageView) findViewById(R.id.literature_write_util_cursor_right);
        literature_write_util_keyboard = (ImageView) findViewById(R.id.literature_write_util_keyboard);

        if (literature_write_util_punctuation_comma != null) {
            literature_write_util_punctuation_comma.setOnClickListener(this);
        }

        if (literature_write_util_punctuation_period != null) {
            literature_write_util_punctuation_period.setOnClickListener(this);
        }

        if (literature_write_util_punctuation_question_mark != null) {
            literature_write_util_punctuation_question_mark.setOnClickListener(this);
        }

        if (literature_write_util_punctuation_exclamation_mark != null) {
            literature_write_util_punctuation_exclamation_mark.setOnClickListener(this);
        }

        if (literature_write_util_punctuation_colon != null) {
            literature_write_util_punctuation_colon.setOnClickListener(this);
        }

        if (literature_write_util_punctuation_double_quotation_marks != null) {
            literature_write_util_punctuation_double_quotation_marks.setOnClickListener(this);
        }

        if (literature_write_util_punctuation_stop != null) {
            literature_write_util_punctuation_stop.setOnClickListener(this);
        }

        if (literature_write_util_punctuation_semicolon != null) {
            literature_write_util_punctuation_semicolon.setOnClickListener(this);
        }

        if (literature_write_util_automatic_typesetting != null) {
            literature_write_util_automatic_typesetting.setOnClickListener(this);
        }

        if (literature_write_util_cursor_left != null) {
            literature_write_util_cursor_left.setOnClickListener(this);
        }

        if (literature_write_util_cursor_right != null) {
            literature_write_util_cursor_right.setOnClickListener(this);
        }

        if (literature_write_util_keyboard != null) {
            literature_write_util_keyboard.setOnClickListener(this);
        }

        literature_write_main.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = literature_write_main.getRootView().getHeight() - literature_write_main.getHeight();
                // 大于100像素，是打开的情况
                if (heightDiff > 350) {
                    if (keyword_closed) {
                        resetKeyboardState(false);
                        QGLog.e(TAG, "软键盘打开");
                    }
                    return;
                }
                resetKeyboardState(true);
                QGLog.e(TAG, "软键盘已关闭");
            }
        });
        initParameter();
    }

    private void initParameter() {

        if (literature_write_title_length != null) {
            literature_write_title_length.setText(String.valueOf(0));
        }

        if (literature_write_title_input != null) {
            TitleTextWatcher titleTextWatcher = new TitleTextWatcher();
            titleTextWatcher.setTitleCountChangeListener(this);
            literature_write_title_input.addTextChangedListener(titleTextWatcher);
        }

        if (literature_write_content != null) {
            ContentTextWatcher contentTextWatcher = new ContentTextWatcher();
            contentTextWatcher.setContentCountChangeListener(this);
            literature_write_content.addTextChangedListener(contentTextWatcher);
        }

        if (literature_write_util_punctuation_view != null) {
            literature_write_util_punctuation_view.setHorizontalScrollListener(new CustomHorizontalScrollView.HorizontalScrollListener() {
                @Override
                public void onScrollChanged(CustomHorizontalScrollView customHorizontalScrollView, int x, int y, int old_x, int old_y) {
                    int scrollX = customHorizontalScrollView.getScrollX();
                    if (scrollX == 0) {
                        if (literature_write_util_punctuation_more != null) {
                            literature_write_util_punctuation_more.setVisibility(VISIBLE);
                        }
                    } else {
                        if (literature_write_util_punctuation_more != null) {
                            literature_write_util_punctuation_more.setVisibility(INVISIBLE);
                        }
                    }
                }
            });
        }

        if (literature_write_util_punctuation_more != null) {
            literature_write_util_punctuation_more.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.literature_write_util_punctuation_comma:
                addPunctuation("，");
                break;
            case R.id.literature_write_util_punctuation_period:
                addPunctuation("。");
                break;
            case R.id.literature_write_util_punctuation_exclamation_mark:
               addPunctuation("！");
                break;
            case R.id.literature_write_util_punctuation_question_mark:
                addPunctuation("？");
                break;
            case R.id.literature_write_util_punctuation_colon:
                addPunctuation("：");
                break;
            case R.id.literature_write_util_punctuation_double_quotation_marks:
                addPunctuation("“”");
                break;
            case R.id.literature_write_util_punctuation_stop:
               addPunctuation("、");

                break;
            case R.id.literature_write_util_punctuation_semicolon:
                addPunctuation("；");
                break;
            case R.id.literature_write_util_punctuation_more:
                if (literature_write_util_punctuation_view != null) {
                    if (literature_write_util_punctuation_more != null) {
                        literature_write_util_punctuation_more.setVisibility(INVISIBLE);
                    }
                    literature_write_util_punctuation_view.smoothScrollTo(300, 0);
                }
                break;
            case R.id.literature_write_util_automatic_typesetting:
                clearContentFormat();
                break;
            case R.id.literature_write_util_cursor_left:
                cursorMoveToLeft();
                break;
            case R.id.literature_write_util_cursor_right:
                cursorMoveToRight();
                break;
            case R.id.literature_write_util_keyboard:
                if (keyword_closed) {
                    if (literatureSectionModifyContentListener != null) {
                        if (literature_write_title_input.hasFocus()) {
                            if (literature_write_title_input != null) {
                                literatureSectionModifyContentListener.showKeyboard(literature_write_title_input);
                            }
                        } else if (literature_write_content.hasFocus()) {
                            if (literature_write_content != null) {
                                literatureSectionModifyContentListener.showKeyboard(literature_write_content);
                            }
                        }
                    }
                } else {
                    if (literatureSectionModifyContentListener != null) {
                        if (literature_write_title_input.hasFocus()) {
                            if (literature_write_title_input != null) {
                                literatureSectionModifyContentListener.hideKeyboard(literature_write_title_input);
                            }
                        } else if (literature_write_content.hasFocus()) {
                            if (literature_write_content != null) {
                                literatureSectionModifyContentListener.hideKeyboard(literature_write_content);
                            }
                        }
                    }
                }
                break;
        }
    }

    public void setLiteratureSectionModifyContentListener(LiteratureSectionModifyContentListener literatureSectionModifyContentListener) {
        this.literatureSectionModifyContentListener = literatureSectionModifyContentListener;
        if (literature_write_content != null) {
            setContentCountChanged(literature_write_content.length());
        }
    }

    @Override
    public void setTitleLimit(int length) {
        if (literature_write_title_limit != null) {
            literature_write_title_limit.setText(String.valueOf(length));
        }
    }

    @Override
    public void initChapterInformation(String title, String content) {
        if (literature_write_title_input != null) {
            literature_write_title_input.setText(title);
            literature_write_title_input.setSelection(title.length());
        }

        if (literature_write_content != null) {
            literature_write_content.setText(content);
            literature_write_content.setSelection(content.length());
        }
    }

    @Override
    public String getTitle() {
        if (literature_write_title_input != null) {
            return literature_write_title_input.getText().toString();
        }
        return null;
    }

    @Override
    public String getContent() {
        if (literature_write_content != null) {
            return literature_write_content.getText().toString();
        }
        return null;
    }

    @Override
    public void recycleData() {
        removeAllViews();
    }

    @Override
    public void setTitleCountChanged(int length) {
        if (literature_write_title_length != null) {
            literature_write_title_length.setText(String.valueOf(length));
        }
    }

    @Override
    public void setContentCountChanged(int length) {
        if (literatureSectionModifyContentListener != null) {
            literatureSectionModifyContentListener.setContentLength(length);
        }
    }

    @Override
    public void setSensitiveWord(ArrayList<SensitiveWord> sensitiveWords) {
        if (literature_write_content != null) {
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(literature_write_content.getText());

            for (SensitiveWord sensitiveWord : sensitiveWords) {
                spannableStringBuilder = highlightSensitiveWord(spannableStringBuilder, sensitiveWord.word, sensitiveWord.rank);
            }
            literature_write_content.setText(spannableStringBuilder);
            literature_write_content.setSelection(spannableStringBuilder.length());
        }
    }

    private void clearContentFormat() {
        if (literature_write_content != null) {

            Editable editable = literature_write_content.getEditableText();

            literature_write_content.setSelection(editable.length());

            CustomInputFilter customInputFilter = new CustomInputFilter();
            editable.setFilters(new InputFilter[] {customInputFilter});
            editable.replace(0, literature_write_content.length(), editable);

            editable.setFilters(new InputFilter[0]);
        }
    }

    private void cursorMoveToLeft() {
        if (literature_write_title_input.hasFocus()) {
            if (literature_write_title_input != null) {
                int index = literature_write_title_input.getSelectionEnd();
                if (index > 0) {
                    literature_write_title_input.setSelection(index - 1);
                }
            }
        } else if (literature_write_content.hasFocus()) {
            if (literature_write_content != null) {
                int index = literature_write_content.getSelectionEnd();
                if (index > 0) {
                    literature_write_content.setSelection(index - 1);
                }
            }
        } else {
            Toast.makeText(getContext(), "输入区域无效！", Toast.LENGTH_SHORT).show();
        }
    }

    private void cursorMoveToRight() {
        if (literature_write_title_input.hasFocus()) {
            if (literature_write_title_input != null) {
                int index = literature_write_title_input.getSelectionEnd();
                if (index < literature_write_title_input.length()) {
                    literature_write_title_input.setSelection(index + 1);
                }
            }
        } else if (literature_write_content.hasFocus()) {
            if (literature_write_content != null) {
                int index = literature_write_content.getSelectionEnd();
                if (index < literature_write_content.length()) {
                    literature_write_content.setSelection(index + 1);
                }
            }
        } else {
            Toast.makeText(getContext(), "输入区域无效！", Toast.LENGTH_SHORT).show();
        }
    }

    private void addPunctuation(String punctuation) {
        if (literature_write_title_input.hasFocus()) {
            if (literature_write_title_input != null) {
                int index = literature_write_title_input.getSelectionStart();
                Editable editable = literature_write_title_input.getEditableText();
                if (index < 0 || index >= editable.length() ){
                    editable.append(punctuation);
                } else {
                    editable.insert(index, punctuation);
                }
                if (punctuation.equals("“”")) {
                    literature_write_title_input.setSelection(literature_write_title_input.length() - 1);
                }
            }
        } else if (literature_write_content.hasFocus()) {
            if (literature_write_content != null) {
                int index = literature_write_content.getSelectionStart();
                Editable editable = literature_write_content.getEditableText();
                if (index < 0 || index >= editable.length() ){
                    editable.append(punctuation);
                } else {
                    editable.insert(index, punctuation);
                }

                if (punctuation.equals("“”")) {
                    literature_write_content.setSelection(literature_write_content.getSelectionEnd() - 1);
                }
            }
        } else {
            Toast.makeText(getContext(), "输入区域无效！", Toast.LENGTH_SHORT).show();
        }
    }

    private void resetKeyboardState(boolean close) {
        if (close) {
            keyword_closed = true;
            if (literature_write_util_keyboard != null) {
                literature_write_util_keyboard.setImageResource(R.drawable.icon_literature_write_keyboard_up);
            }
        } else {
            keyword_closed = false;
            if (literature_write_util_keyboard != null) {
                literature_write_util_keyboard.setImageResource(R.drawable.icon_literature_write_keyboard_down);
            }
        }
    }

    public SpannableStringBuilder highlightSensitiveWord(SpannableStringBuilder spannableStringBuilder, String sensitive, Rank rank) {
        String context = spannableStringBuilder.toString();
        Pattern pattern = Pattern.compile(sensitive);
        Matcher matcher = pattern.matcher(context);

        CharacterStyle characterStyle = null;

        while (matcher.find()) {
            if (rank.equals(Rank.C)) {
                characterStyle = new ForegroundColorSpan(Color.parseColor("#f44336"));
            } else if (rank.equals(Rank.B)) {
                characterStyle = new ForegroundColorSpan(Color.parseColor("#f89406"));
            } else if (rank.equals(Rank.A)) {
                characterStyle = new ForegroundColorSpan(Color.parseColor("#6bb9f0"));
            }
            spannableStringBuilder.setSpan(characterStyle, matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannableStringBuilder;
    }
}
