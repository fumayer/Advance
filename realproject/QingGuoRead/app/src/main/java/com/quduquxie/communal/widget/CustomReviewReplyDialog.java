package com.quduquxie.communal.widget;

import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quduquxie.R;
import com.quduquxie.application.QuApplication;
import com.quduquxie.communal.utils.GlobalUtil;
import com.quduquxie.communal.widget.expression.ExpressionEditText;
import com.quduquxie.communal.widget.expression.ExpressionFragment;
import com.quduquxie.communal.widget.expression.ExpressionItem;
import com.quduquxie.communal.widget.expression.ExpressionListener;
import com.quduquxie.model.Comment;

import java.lang.reflect.Field;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created on 17/3/4.
 * Created by crazylei.
 */

public class CustomReviewReplyDialog extends DialogFragment implements ExpressionListener {

    @BindView(R.id.comment_post_root)
    public CoordinatorLayout comment_post_root;
    @BindView(R.id.comment_post_content)
    public RelativeLayout comment_post_content;
    @BindView(R.id.comment_post_empty)
    public RelativeLayout comment_post_empty;
    @BindView(R.id.comment_post_input)
    public ExpressionEditText comment_post_input;
    @BindView(R.id.comment_post_option)
    public ImageView comment_post_option;
    @BindView(R.id.comment_post_sent)
    public TextView comment_post_sent;
    @BindView(R.id.comment_post_expression)
    public FrameLayout comment_post_expression;

    private int screenHeight;

    // 状态栏的高度
    private int statusBarHeight;
    // 软键盘的高度
    private int keyboardHeight;

    private boolean keyboard_state = false;

    private LayoutTransition layoutTransition = new LayoutTransition();

    private ExpressionFragment expressionFragment;

    private Comment comment;
    private OnReviewReplySentClickListener onReviewReplySentClickListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomCommentInputDialog);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.layout_dialog_comment_post, container, false);

        ButterKnife.bind(this, view);


        statusBarHeight = getStatusBarHeight(this.getContext());

        ObjectAnimator animIn = ObjectAnimator.ofFloat(null, "translationY", screenHeight, keyboardHeight).setDuration(layoutTransition.getDuration(LayoutTransition.APPEARING));
        layoutTransition.setAnimator(LayoutTransition.APPEARING, animIn);
        ObjectAnimator animOut = ObjectAnimator.ofFloat(null, "translationY", keyboardHeight, screenHeight).setDuration(layoutTransition.getDuration(LayoutTransition.DISAPPEARING));
        layoutTransition.setAnimator(LayoutTransition.DISAPPEARING, animOut);
        comment_post_root.setLayoutTransition(layoutTransition);

        comment_post_root.getViewTreeObserver().addOnGlobalLayoutListener(globalLayoutListener);

        if (comment_post_input != null) {
            comment_post_input.setFocusable(true);
            comment_post_input.setFocusableInTouchMode(true);
            comment_post_input.requestFocus();
        }

        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        expressionFragment = new ExpressionFragment();
        expressionFragment.setExpressionListener(this);
        getChildFragmentManager().beginTransaction().add(R.id.comment_post_expression, expressionFragment, "ExpressionFragment").commit();

        keyboardHeight = GlobalUtil.getKeyboardHeight();
        resetExpressionViewHeight();

        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onResume() {
        WindowManager.LayoutParams layoutParams = getDialog().getWindow().getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        this.getDialog().getWindow().setAttributes(layoutParams);

        this.getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dismiss();
                    return true;
                }
                return false;
            }
        });

        super.onResume();

        if (comment_post_input != null) {
            comment_post_input.setText("");
        }
    }

    private ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {

        @Override
        public void onGlobalLayout() {
            Rect rect = new Rect();
            comment_post_root.getWindowVisibleDisplayFrame(rect);

            screenHeight = comment_post_root.getRootView().getHeight();
            int heightDiff = Math.abs(screenHeight - (rect.bottom - rect.top));

            if (heightDiff > statusBarHeight) {
                keyboardHeight = heightDiff - statusBarHeight;
                GlobalUtil.saveKeyboardHeight(keyboardHeight);
                resetExpressionViewHeight();
            }

            if (keyboard_state) {
                if (heightDiff <= statusBarHeight) {
                    keyboard_state = false;
                    onKeyboardHide();
                }
            } else {
                if (heightDiff > statusBarHeight) {
                    keyboard_state = true;
                    onKeyboardShow();
                }
            }
        }
    };

    public static int getStatusBarHeight(Context context) {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return context.getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private void onKeyboardShow() {
        if (comment_post_option != null) {
            comment_post_option.setImageResource(R.drawable.icon_comment_input_expression);
        }
    }

    private void onKeyboardHide() {
        if (comment_post_option != null) {
            comment_post_option.setImageResource(R.drawable.icon_comment_input_keyboard);
        }
    }

    private void showInputKeyBoard(View view) {
        if (view != null) {
            view.setFocusable(true);
            view.setFocusableInTouchMode(true);
            view.requestFocus();

            InputMethodManager inputManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    private void hideInputKeyBoard(View view) {
        if (view == null || view.getContext() == null) {
            return;
        }
        InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive()) {
            inputMethodManager.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
        }
    }

    private void resetExpressionViewHeight() {
        comment_post_content.setPadding(0, 0, 0, keyboardHeight);
        comment_post_expression.getLayoutParams().height = keyboardHeight;
        comment_post_expression.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.comment_post_option, R.id.comment_post_sent, R.id.comment_post_empty})
    public void onClickListener(View view) {
        switch (view.getId()) {
            case R.id.comment_post_option:
                if (keyboard_state) {
                    hideInputKeyBoard(comment_post_input);
                } else {
                    showInputKeyBoard(comment_post_input);
                }
                break;
            case R.id.comment_post_sent:
                if (comment != null && onReviewReplySentClickListener != null) {
                    onReviewReplySentClickListener.onReviewReplyClicked(comment, comment_post_input.getText().toString());
                }
                break;
            case R.id.comment_post_empty:
                dismiss();
                break;
        }
    }

    @Override
    public void onExpressionClicked(ExpressionItem expressionItem) {
        if (comment_post_input == null || expressionItem == null) {
            return;
        }

        int start = comment_post_input.getSelectionStart();
        int end = comment_post_input.getSelectionEnd();
        if (start < 0) {
            comment_post_input.append(expressionItem.getExpression());
        } else {
            comment_post_input.getText().replace(Math.min(start, end), Math.max(start, end), expressionItem.getExpression(), 0, expressionItem.getExpression().length());
        }
    }

    public void setReplyCommentData(Comment comment) {
        this.comment = comment;
    }

    public void setOnReviewReplySentClickListener(OnReviewReplySentClickListener onReviewReplySentClickListener) {
        this.onReviewReplySentClickListener = onReviewReplySentClickListener;
    }

    public interface OnReviewReplySentClickListener {
        void onReviewReplyClicked(Comment comment, String content);
    }
}

