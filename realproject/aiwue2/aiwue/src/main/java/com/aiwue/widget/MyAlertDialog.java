package com.aiwue.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;

import com.aiwue.R;


/**
 * A subclass of Dialog that can display one, two or three buttons. If you only
 * want to display a String in this dialog box, use the setMessage() method. If
 * you want to display a more complex view, look up the FrameLayout called
 * "custom" and add your view to it:
 * 
 * <pre>
 * FrameLayout fl = (FrameLayout) findViewById(android.R.id.custom);
 * fl.addView(myView, new LayoutParams(MATCH_PARENT, WRAP_CONTENT));
 * </pre>
 * 
 * <p>
 * The AlertDialog class takes care of automatically setting
 * {@link WindowManager.LayoutParams#FLAG_ALT_FOCUSABLE_IM
 * WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM} for you based on whether
 * any views in the dialog return true from {@link View#onCheckIsTextEditor()
 * View.onCheckIsTextEditor()}. Generally you want this set for a Dialog without
 * text editors, so that it will be placed on top of the current input method
 * UI. You can modify this behavior by forcing the flag to your desired mode
 * after calling {@link #onCreate}.
 */
public class MyAlertDialog extends Dialog implements DialogInterface {
	private MyAlertController mAlert;
	private Context context;

	protected MyAlertDialog(Context context) {
		this(context, R.style.AliDialog);
		this.context = context;
	}

	protected MyAlertDialog(Context context, int theme) {
		super(context, theme);
		mAlert = new MyAlertController(context, this, getWindow());
		this.context = context;
	}

	protected MyAlertDialog(Context context, boolean cancelable,
							OnCancelListener cancelListener) {
		super(context, R.style.AliDialog);
		setCancelable(cancelable);
		setOnCancelListener(cancelListener);
		mAlert = new MyAlertController(context, this, getWindow());
		this.context = context;
	}

	/**
	 * Gets one of the buttons used in the dialog.
	 * <p>
	 * If a button does not exist in the dialog, null will be returned.
	 * 
	 * @param whichButton
	 *            The identifier of the button that should be returned. For
	 *            example, this can be {@link DialogInterface#BUTTON_POSITIVE}.
	 * @return The button from the dialog, or null if a button does not exist.
	 */
	public Button getButton(int whichButton) {
		return mAlert.getButton(whichButton);
	}

	/**
	 * Gets the list view used in the dialog.
	 * 
	 * @return The {@link ListView} from the dialog.
	 */
//	public ListView getListView() {
//		return mAlert.getListView();
//	}

	@Override
	public void setTitle(CharSequence title) {
		super.setTitle(title);
		mAlert.setTitle(title);
	}

	/**
	 * @see Builder#setCustomTitle(View)
	 */
//	public void setCustomTitle(View customTitleView) {
//		mAlert.setCustomTitle(customTitleView);
//	}

	public void setMessage(CharSequence message) {
		mAlert.setMessage(message);
	}

	/**
	 * Set the view to display in that dialog.
	 */
	public void setView(View view) {
		mAlert.setView(view);
	}
	
	public View getView() {
		return mAlert.getView();
	}

	/**
	 * Set the view to display in that dialog, specifying the spacing to appear
	 * around that view.
	 * 
	 * @param view
	 *            The view to show in the content area of the dialog
	 * @param viewSpacingLeft
	 *            Extra space to appear to the left of {@code view}
	 * @param viewSpacingTop
	 *            Extra space to appear above {@code view}
	 * @param viewSpacingRight
	 *            Extra space to appear to the right of {@code view}
	 * @param viewSpacingBottom
	 *            Extra space to appear below {@code view}
	 */
	public void setView(View view, int viewSpacingLeft, int viewSpacingTop,
						int viewSpacingRight, int viewSpacingBottom) {
		mAlert.setView(view, viewSpacingLeft, viewSpacingTop, viewSpacingRight,
				viewSpacingBottom);
	}
	
	public void setCustomButtonMargin(int margin) {
		mAlert.setCustomButtonMargin(margin);
	}

//    public void setNegativeEnable(boolean enable) {
//        mAlert.setNegativeEnable(enable);
//    }
	
	/**
	 * Set a message to be sent when a button is pressed.
	 * 
	 * @param whichButton
	 *            Which button to set the message for, can be one of
	 *            {@link DialogInterface#BUTTON_POSITIVE},
	 *            {@link DialogInterface#BUTTON_NEGATIVE}, or
	 *            {@link DialogInterface#BUTTON_NEUTRAL}
	 * @param text
	 *            The text to display in positive button.
	 * @param msg
	 *            The {@link Message} to be sent when clicked.
	 */
//	public void setButton(int whichButton, CharSequence text, Message msg) {
//		mAlert.setButton(whichButton, text, null, msg);
//	}

	/**
	 * Set a listener to be invoked when the positive button of the dialog is
	 * pressed.
	 * 
	 * @param whichButton
	 *            Which button to set the listener on, can be one of
	 *            {@link DialogInterface#BUTTON_POSITIVE},
	 *            {@link DialogInterface#BUTTON_NEGATIVE}, or
	 *            {@link DialogInterface#BUTTON_NEUTRAL}
	 * @param text
	 *            The text to display in positive button.
	 * @param listener
	 *            The {@link OnClickListener} to use.
	 */
	public void setButton(int whichButton, CharSequence text,
			OnClickListener listener) {
		mAlert.setButton(whichButton, text, listener, null);
	}

	/**
	 * @deprecated Use {@link #setButton(int, CharSequence, Message)} with
	 *             {@link DialogInterface#BUTTON_POSITIVE}.
	 */
//	@Deprecated
//	public void setButton(CharSequence text, Message msg) {
//		setButton(BUTTON_POSITIVE, text, msg);
//	}

	/**
	 * @deprecated Use {@link #setButton(int, CharSequence, Message)} with
	 *             {@link DialogInterface#BUTTON_NEGATIVE}.
	 */
//	@Deprecated
//	public void setButton2(CharSequence text, Message msg) {
//		setButton(BUTTON_NEGATIVE, text, msg);
//	}

	/**
	 * @deprecated Use {@link #setButton(int, CharSequence, Message)} with
	 *             {@link DialogInterface#BUTTON_NEUTRAL}.
	 */
//	@Deprecated
//	public void setButton3(CharSequence text, Message msg) {
//		setButton(BUTTON_NEUTRAL, text, msg);
//	}

	/**
	 * Set a listener to be invoked when button 1 of the dialog is pressed.
	 *
	 * @param text
	 *            The text to display in button 1.
	 * @param listener
	 *            The {@link OnClickListener} to use.
	 * @deprecated Use
	 *             {@link #setButton(int, CharSequence, OnClickListener)}
	 *             with {@link DialogInterface#BUTTON_POSITIVE}
	 */
//	@Deprecated
//	public void setButton(CharSequence text, final OnClickListener listener) {
//		setButton(BUTTON_POSITIVE, text, listener);
//	}

	/**
	 * Set a listener to be invoked when button 2 of the dialog is pressed.
	 *
	 * @param text
	 *            The text to display in button 2.
	 * @param listener
	 *            The {@link OnClickListener} to use.
	 * @deprecated Use
	 *             {@link #setButton(int, CharSequence, OnClickListener)}
	 *             with {@link DialogInterface#BUTTON_NEGATIVE}
	 */
//	@Deprecated
//	public void setButton2(CharSequence text, final OnClickListener listener) {
//		setButton(BUTTON_NEGATIVE, text, listener);
//	}

	/**
	 * Set a listener to be invoked when button 3 of the dialog is pressed.
	 *
	 * @param text
	 *            The text to display in button 3.
	 * @param listener
	 *            The {@link OnClickListener} to use.
	 * @deprecated Use
	 *             {@link #setButton(int, CharSequence, OnClickListener)}
	 *             with {@link DialogInterface#BUTTON_POSITIVE}
	 */
//	@Deprecated
//	public void setButton3(CharSequence text, final OnClickListener listener) {
//		setButton(BUTTON_NEUTRAL, text, listener);
//	}

//	public void setInverseBackgroundForced(boolean forceInverseBackground) {
//		mAlert.setInverseBackgroundForced(forceInverseBackground);
//	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mAlert.installContent();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (mAlert.onKeyDown(keyCode, event))
			return true;
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (mAlert.onKeyUp(keyCode, event))
			return true;

		if (keyCode == KeyEvent.KEYCODE_BACK ){
			if(context != null && context instanceof Activity){
				if(((Activity)context).isFinishing()){
					return false;
				}
			}
		}
		return super.onKeyUp(keyCode, event);
	}

	@Override
	public void show() {
		if(context != null && context instanceof Activity){
			if(((Activity)context).isFinishing()){
				return ;
			}
		}
//        if(DialogChecker.getIns().checkShow(context,DialogDefine.DialogType.DIALOG)) {
            super.show();
            mAlert.adaptToScreenHeight();
//        }else{
//
//        }
	}

	public static class Builder extends  BuilderBase<Builder> {
        private int mTheme;
        public Builder(Context context) {
            this(context, R.style.AliDialog);
        }

        public Builder(Context context, int theme) {
            super(context,BuilderBase.DIALOG);
            mTheme = theme;
        }

        @Override
        public MyAlertDialog create() {
            if(P.mNegativeButtonListener != null ||
                    P.mPositiveButtonListener != null){
//				theme = R.style.AliDialogWithButton;
            }
            final MyAlertDialog dialog = new MyAlertDialog(P.mContext, mTheme);
            P.apply(dialog.mAlert);
            dialog.setCancelable(P.mCancelable);
            dialog.setOnCancelListener(P.mOnCancelListener);
            if (P.mOnKeyListener != null) {
                dialog.setOnKeyListener(P.mOnKeyListener);
            }
            return dialog;
        }

        public MyAlertDialog show() {
            if (!checkActivityExist()) {
                return null;
            }
            MyAlertDialog dialog = create();
            dialog.show();
            return dialog;
        }

        /**
         * show a outside cancelable dialog
         * @return
         */
        public MyAlertDialog showIsOutsideCancelable(boolean isCancel) {
            if (!checkActivityExist()) {
                return null;
            }
            MyAlertDialog dialog = create();
            dialog.setCanceledOnTouchOutside(isCancel);
            dialog.show();
            return dialog;
        }


        public MyAlertDialog showIsOutsideCancelableInServiceProcess(boolean isCancel) {
            if (!checkActivityExist()) {
                return null;
            }
            MyAlertDialog dialog = create();
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            if(Build.VERSION.SDK_INT >= 23){
                dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
            }
            dialog.setCanceledOnTouchOutside(isCancel);
            dialog.show();
            return dialog;
        }
    }
}
