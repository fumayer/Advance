package com.aiwue.widget;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.CursorAdapter;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.aiwue.R;
import com.aiwue.base.AiwueApplication;

import java.lang.ref.WeakReference;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class MyAlertController {

	private final Context mContext;
	private final DialogInterface mDialogInterface;
	private final Window mWindow;
    private View mRootView;

	private int mGravity;
	
	private LinearLayout mTopPanel;
	
	private CharSequence mTitle;
	private boolean mTitleSupportMulitipleLines;
	private CharSequence mSubTitle;
	
	private boolean mTitleLogoVisiable;
	
	private CharSequence mMessage;

	private ListView mListView;

	private View mView;
	
	private boolean mCustomNoPadding;
	
	private boolean mTitleInContent;
    private boolean mTitleBottomLineExist;//标题下的下划线是否强制存在
	
    private boolean mNegativeEnable;
    
    private boolean mPositiveWarning;

	private int mViewSpacingLeft;

	private int mViewSpacingTop;

	private int mViewSpacingRight;

	private int mViewSpacingBottom;
	
	private int mCustomButtonMargin;

	private boolean mViewSpacingSpecified = false;

	private boolean mbAutoBtnPosDismiss = true;
	private Button mButtonPositive;

	private CharSequence mButtonPositiveText;

	private Message mButtonPositiveMessage;

	private boolean mbAutoBtnNegDismiss = true;
	private Button mButtonNegative;

	private CharSequence mButtonNegativeText;

	private Message mButtonNegativeMessage;

	private boolean mbAutoBtnNeutDismiss = true;
	private Button mButtonNeutral;

	private CharSequence mButtonNeutralText;

	private Message mButtonNeutralMessage;

	private ScrollView mScrollView;
	
	private HorizontalScrollView mHorizontalScrollView;

//	private Drawable mIcon;

	private TextView mTitleView;
	private TextView mSubTitleView;
	private View mLogo1;
	private View mLogo2;

	private TextView mMessageView;

	private View mCustomTitleView;

//	private boolean mForceInverseBackground;

	private ListAdapter mAdapter;

	private int mCheckedItem = -1;

	private Handler mHandler;

//	private boolean useBackground = true;
	
	private boolean transparentBackage = false;
	
	private boolean mUseHorizontalScrollView = false;
	
	public boolean mEnableShowWithSuitableHeight = false;
	
	View.OnClickListener mButtonHandler = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			
			boolean bDismiss = true;
			Message m = null;
			if (v == mButtonPositive && mButtonPositiveMessage != null) {
				m = Message.obtain(mButtonPositiveMessage);
				bDismiss = mbAutoBtnPosDismiss;
			} else if (v == mButtonNegative && mButtonNegativeMessage != null) {
				m = Message.obtain(mButtonNegativeMessage);
				bDismiss = mbAutoBtnNegDismiss;
			} else if (v == mButtonNeutral && mButtonNeutralMessage != null) {
				m = Message.obtain(mButtonNeutralMessage);
				bDismiss = mbAutoBtnNeutDismiss;
			}
			if (m != null) {
				m.sendToTarget();
			}

			// Post a message so we dismiss after the above handlers are
			// executed
			if (bDismiss)
			mHandler.obtainMessage(ButtonHandler.MSG_DISMISS_DIALOG,
					mDialogInterface).sendToTarget();
		}
	};

	private static final class ButtonHandler extends Handler {
		// Button clicks have Message.what as the BUTTON{1,2,3} constant
		private static final int MSG_DISMISS_DIALOG = 1;

		private WeakReference<DialogInterface> mDialog;

		public ButtonHandler(DialogInterface dialog) {
			mDialog = new WeakReference<DialogInterface>(dialog);
		}

		@Override
		public void handleMessage(Message msg) {
			if (null == msg || null == msg.obj) {
				return;
			}
			
			switch (msg.what) {

			case DialogInterface.BUTTON_POSITIVE:
			case DialogInterface.BUTTON_NEGATIVE:
			case DialogInterface.BUTTON_NEUTRAL:
				if (null == mDialog) {
					break;
				}
				
				DialogInterface dialog = mDialog.get();
				
				if (null != dialog) {
					((DialogInterface.OnClickListener) msg.obj).onClick(
							dialog, msg.what); 
				}
				break;

			case MSG_DISMISS_DIALOG:
				try {
					((DialogInterface) msg.obj).dismiss();
                } catch (Exception e) {
                	e.printStackTrace();
                }
			}
		}
	}

	public MyAlertController(Context context, DialogInterface di, Window window) {
		mContext = context;
		mDialogInterface = di;
        mWindow = window;
		mHandler = new ButtonHandler(di);
	}

    public MyAlertController(Context context, DialogInterface di, View rootView) {
        mContext = context;
        mDialogInterface = di;
        mRootView = rootView;
        mWindow = null;
        mHandler = new ButtonHandler(di);
    }

	static boolean canTextInput(View v) {
		if (v.onCheckIsTextEditor()) {
			return true;
		}

		if (!(v instanceof ViewGroup)) {
			return false;
		}

		ViewGroup vg = (ViewGroup) v;
		int i = vg.getChildCount();
		while (i > 0) {
			i--;
			v = vg.getChildAt(i);
			if (canTextInput(v)) {
				return true;
			}
		}

		return false;
	}

	public void installContent() {
		/* We use a custom title so never request a window title */
        if(mWindow != null){    //dialog
            mWindow.requestFeature(Window.FEATURE_NO_TITLE);
            mRootView = mWindow.getDecorView();
            if (mView == null || !canTextInput(mView)) {
                mWindow.setFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM,
                        WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            }
            mWindow.setContentView(R.layout.my_alert_dialog);
            DisplayMetrics metric = new DisplayMetrics();
            mWindow.getWindowManager().getDefaultDisplay().getMetrics(metric);
            float density = metric.density;
            int width = metric.widthPixels; // 屏幕宽度（像素）
            int height = metric.heightPixels; // 屏幕高度（像素）
            WindowManager.LayoutParams para = mWindow.getAttributes();
            if (width <= 240 || height <= 320) {
                para.width = width;
            } else {
                para.width = (int) (315 * density);
            }
            para.gravity = mGravity;
            para.height= LayoutParams.WRAP_CONTENT;
            mWindow.setAttributes(para);
            mWindow.setBackgroundDrawableResource(R.drawable.trans_piece);
        }else{       //floatDialog

            mRootView.setBackgroundResource(R.drawable.float_my_alert_bg);

        }



		setupView();
	}

    public void setGravity(int gravity) {
        this.mGravity = gravity;
    }
    
	public void setTitle(CharSequence title) {
		mTitle = title;
		if (mTitleView != null) {
			mTitleView.setText(title);
		}
	}
	
    public void setTitleSupportMultipleLines(boolean support) {
        mTitleSupportMulitipleLines = support;
        if (mTitleView != null) {
            mTitleView.setSingleLine(!support);
        }
    }

	/**
	 * @see AlertDialog.Builder#setCustomTitle(View)
	 */
	public void setCustomTitle(View customTitleView) {
		mCustomTitleView = customTitleView;
	}

	public void setMessage(CharSequence message) {
		mMessage = message;
		if (mMessageView != null) {
			mMessageView.setText(message);
		}
	}
	
	public void setSubTitle(CharSequence title)
	{
		 mSubTitle = title;
		 if(mSubTitleView!=null)
		 {
			 mSubTitleView.setVisibility(View.VISIBLE);
			 mSubTitleView.setText(title);
		 } 
	}
	
    public void setTitleLogoVisiable(boolean visiable) {
        mTitleLogoVisiable = visiable;
        if (visiable) {
            if (mTopPanel != null) {
                mTopPanel.setBackgroundResource(R.drawable.dialog_title_bg);
            }
            if (mTitleView != null) {
                mTitleView.setVisibility(View.GONE);
            }
            if (mSubTitleView != null) {
                mSubTitleView.setVisibility(View.GONE);
            }
            if (mLogo1 != null) {
                mLogo1.setVisibility(View.VISIBLE);
            }
            if (mLogo2 != null) {
                mLogo2.setVisibility(View.VISIBLE);
            }
        } else {
            if (mTopPanel != null) {
                mTopPanel.setBackgroundDrawable(null);
            }
            if (mTitleView != null) {
                mTitleView.setVisibility(View.VISIBLE);
            }
            if (mSubTitleView != null) {
                mSubTitleView.setVisibility(View.VISIBLE);
            }
            if (mLogo1 != null) {
                mLogo1.setVisibility(View.GONE);
            }
            if (mLogo2 != null) {
                mLogo2.setVisibility(View.GONE);
            }
        }
    }
    
	/**
	 * Set the view to display in the dialog.
	 */
	public void setView(View view) {
		mView = view;
		mViewSpacingSpecified = false;
	}
	
	public View getView() {
		return mView;
	}

    public void setCustomNoPadding(boolean isNoPadding) {
        mCustomNoPadding = isNoPadding;
    }

    public void setTitleInContent(boolean enable) {
        mTitleInContent = enable;
    }
    public void setTitleBottomLineExist(boolean enable){
        mTitleBottomLineExist = enable;
    }
    
    public void setNegativeEnable(boolean enable) {
        mNegativeEnable = enable;
        if (mButtonNegative != null) {
            int resId;
            if (mNegativeEnable) {
                resId = R.color.dialog_text_normal;
            } else {
                resId = R.color.dialog_text_disable;
            }
            mButtonNegative.setTextColor(mContext.getResources().getColor(resId));
        }
    }

    public void setPositiveWarning(boolean enable) {
        mPositiveWarning = enable;
        if (mButtonPositive != null) {
            int resId;
            if (mPositiveWarning) {
                resId = R.drawable.dialog_right_button_warning_bg;
            } else {
                resId = R.drawable.dialog_right_button_bg;
            }
            mButtonPositive.setBackgroundResource(resId);
        }
    }

	/**
	 * Set the view to display in the dialog along with the spacing around that
	 * view
	 */
	public void setView(View view, int viewSpacingLeft, int viewSpacingTop,
						int viewSpacingRight, int viewSpacingBottom) {
		mView = view;
		mViewSpacingSpecified = true;
		mViewSpacingLeft = viewSpacingLeft;
		mViewSpacingTop = viewSpacingTop;
		mViewSpacingRight = viewSpacingRight;
		mViewSpacingBottom = viewSpacingBottom;
	}
	
    public int mLeftRightPadding = 18;
    public int mBottomPadding = 16;
    public void setContentPadding(int leftRightPadding ,int bottomPadding){
        mLeftRightPadding = leftRightPadding;
        mBottomPadding = bottomPadding;
    }
	
	public void setCustomButtonMargin(int margin) {
		if (margin < 0) {
			margin = 0;
		}
		
		mCustomButtonMargin = margin;
	}
	
	/**
	 * Sets a click listener or a message to be sent when the button is clicked.
	 * You only need to pass one of {@code listener} or {@code msg}.
	 * 
	 * @param whichButton
	 *            Which button, can be one of
	 *            {@link DialogInterface#BUTTON_POSITIVE},
	 *            {@link DialogInterface#BUTTON_NEGATIVE}, or
	 *            {@link DialogInterface#BUTTON_NEUTRAL}
	 * @param text
	 *            The text to display in positive button.
	 * @param listener
	 *            The {@link DialogInterface.OnClickListener} to use.
	 * @param msg
	 *            The {@link Message} to be sent when clicked.
	 */
	public void setButton(int whichButton, CharSequence text,
						  DialogInterface.OnClickListener listener, Message msg) {

		if (msg == null && listener != null) {
			msg = mHandler.obtainMessage(whichButton, listener);
		}

		switch (whichButton) {

		case DialogInterface.BUTTON_POSITIVE:
			mButtonPositiveText = text;
			mButtonPositiveMessage = msg;
			break;

		case DialogInterface.BUTTON_NEGATIVE:
			mButtonNegativeText = text;
			mButtonNegativeMessage = msg;
			break;

		case DialogInterface.BUTTON_NEUTRAL:
			mButtonNeutralText = text;
			mButtonNeutralMessage = msg;
			break;

		default:
			throw new IllegalArgumentException("Button does not exist");
		}
	}

	public void setInverseBackgroundForced(boolean forceInverseBackground) {
//		mForceInverseBackground = forceInverseBackground;
	}

//	public ListView getListView() {
//		return mListView;
//	}

	public Button getButton(int whichButton) {
		switch (whichButton) {
		case DialogInterface.BUTTON_POSITIVE:
			return mButtonPositive;
		case DialogInterface.BUTTON_NEGATIVE:
			return mButtonNegative;
		case DialogInterface.BUTTON_NEUTRAL:
			return mButtonNeutral;
		default:
			return null;
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return mScrollView != null && mScrollView.executeKeyEvent(event);
	}

	public boolean onKeyUp(int keyCode, KeyEvent event) {
		return mScrollView != null && mScrollView.executeKeyEvent(event);
	}
	
	public void adaptToScreenHeight() {
		if (!mEnableShowWithSuitableHeight) {
			return;
		}
		
		if (!(mContext instanceof Activity)) {
			return;
		}
		
		int screenHeight = 0;
		WindowManager wm = ((Activity)mContext).getWindowManager();
		if (null != wm) {
			Display d = wm.getDefaultDisplay();
			if (null != d) {
				screenHeight = d.getHeight();
			}
		}

		if (screenHeight > 0) {
			if (null == mDialogInterface || !(mDialogInterface instanceof Dialog)) {
				return;
			}
			
			Window dlgWin = ((Dialog)mDialogInterface).getWindow();
			int newHeight = screenHeight * 6 / 10;
			
			final Handler h = new AdaptScreenHeightHandler() {
				@Override
				public void handleMessage(Message msg) {
                    Dialog dialog = ((Dialog) mDialogInterface);
                    if (dialog == null || !dialog.isShowing()) {
                        return;
                    }

				    if (null != msg.obj) {
				    	
				    	if (((Activity)mContext).isFinishing()) {
				    		return;
				    	}
				    	
						Window dlgWin = (Window)msg.obj;
						View dlgView = dlgWin.findViewById(R.id.parentPanel);
 						if (null != dlgView) {
							if (dlgView.getHeight() > msg.arg1) {
								dlgWin.setLayout(dlgView.getWidth(), msg.arg1);
							}else
							{
								dlgWin.setLayout(dlgView.getWidth(), dlgView.getMeasuredHeight());
							}
						}
					}
				}
			};
			Message msg = h.obtainMessage(0, newHeight, 0, dlgWin);
			h.sendMessage(msg);
		}
	}
	
	private static class AdaptScreenHeightHandler extends Handler {
	}

	private void setupView() {
        LinearLayout contentPanel = (LinearLayout) mRootView.findViewById(R.id.contentPanel);
        setupContent(contentPanel);
        setupButtons();
        
		boolean  hasTitle =  setupTitle();
		
		FrameLayout customPanel = null;
		if (mView != null) {
			customPanel = (FrameLayout) mRootView
					.findViewById(R.id.customPanel);
			customPanel.setVisibility(View.VISIBLE);
			if(hasTitle)
			{
				mRootView.findViewById(R.id.centerPanel_dotted_split_line_color).setVisibility(View.VISIBLE);
			}else
			{
				mRootView.findViewById(R.id.centerPanel_dotted_split_line_color).setVisibility(View.GONE);
	
			}
			
			if(transparentBackage)
			{
				mRootView.findViewById(R.id.parentPanel).setBackgroundColor(mRootView.getContext().getResources().getColor(android.R.color.transparent));
			}
			
			LinearLayout custom = (LinearLayout) mRootView
					.findViewById(R.id.custom_view);
            if (mCustomNoPadding)
            {
            	custom.setPadding(0, 0, 0, 0);
            } 
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
            lp.gravity= Gravity.CENTER;
            if(mView.getParent() == custom){
                custom.removeView(mView);
            }
            custom.addView(mView, lp );
			if (mViewSpacingSpecified) {
				custom.setPadding(mViewSpacingLeft, mViewSpacingTop,
						mViewSpacingRight, mViewSpacingBottom);
			}
			if (mListView != null) {
				((LinearLayout.LayoutParams) customPanel.getLayoutParams()).weight = 0;
			}
		} else {
			mRootView.findViewById(R.id.customPanel)
					.setVisibility(View.GONE);
			if(mRootView.findViewById(R.id.contentPanel).getVisibility()== View.GONE)
				mRootView.findViewById(R.id.centerPanel_dotted_split_line_color).setVisibility(View.GONE);;
		}

        if (mTitleInContent) {
            int h = DimenUtils.dp2px(mContext, mLeftRightPadding);
            int v = DimenUtils.dp2px(mContext, mBottomPadding);
            mRootView.findViewById(R.id.custom_view).setPadding(h, 0, h, v);
            mRootView.findViewById(R.id.contentPanel).setPadding(h, 0, h, v);
            if (mTitleBottomLineExist){
                mRootView.findViewById(R.id.custom_view).setPadding(h, v, h, v);
                mRootView.findViewById(R.id.contentPanel).setPadding(h, v, h, v);
                mRootView.findViewById(R.id.centerPanel_dotted_split_line_color).setPadding(h,0,h,0);
                mRootView.findViewById(R.id.centerPanel_dotted_split_line_color).setVisibility(View.VISIBLE);
            }else {
                mRootView.findViewById(R.id.custom_view).setPadding(h, 0, h, v);
                mRootView.findViewById(R.id.contentPanel).setPadding(h, 0, h, v);
                mRootView.findViewById(R.id.centerPanel_dotted_split_line_color).setVisibility(View.GONE);
            }
        }

		/*
		 * Only display the divider if we have a title and a custom view or a
		 * message.
		 */
		
		/* 不要这个divider
		if (hasTitle && ((mMessage != null) || (mView != null))) {
			View divider = mRootView
					.findViewById(R.id.titleDivider);
			divider.setVisibility(View.VISIBLE);
		}
		*/

		//setBackground(topPanel, contentPanel, customPanel, hasButtons, a,
		//hasTitle, buttonPanel);
        if ((mListView != null) && (mAdapter != null)) {
            mListView.setAdapter(mAdapter);
            if (mCheckedItem > -1) {
                mListView.setItemChecked(mCheckedItem, true);
                mListView.setSelection(mCheckedItem);
            }
        }
	}

    private boolean setupTitle() {
        boolean hasTitle = true;

        mTopPanel = (LinearLayout) mRootView.findViewById(R.id.topPanel);
        // TypedArray a = mContext.obtainStyledAttributes(null,
        // com.android.internal.R.styleable.AlertDialog,
        // com.android.internal.R.attr.alertDialogStyle, 0);
        // a.recycle();

        if (mCustomTitleView != null) {
            // Add the custom title view directly to the topPanel layout
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT);
            mTopPanel.addView(mCustomTitleView, lp);

            // Hide the title template
            View titleTemplate = mRootView.findViewById(R.id.title_template);
            titleTemplate.setVisibility(View.GONE);
        } else {
            final boolean hasTextTitle = !TextUtils.isEmpty(mTitle);

            if (hasTextTitle) {
                /* Display the title if a title is supplied, else hide it */
                mTitleView = (TextView) mRootView.findViewById(R.id.alertTitle);
                mTitleView.getPaint().setFakeBoldText(true);
                mTitleView.setText(mTitle);
                mTitleView.setSingleLine(!mTitleSupportMulitipleLines);
                if (!TextUtils.isEmpty(mSubTitle)) {
                    mSubTitleView = (TextView) mRootView.findViewById(R.id.alertTitle2);
                    mSubTitleView.setVisibility(View.VISIBLE);
                    mSubTitleView.getPaint().setFakeBoldText(true);
                    mSubTitleView.setText(mSubTitle);
                }

                mLogo1 = mRootView.findViewById(R.id.logo1);
                mLogo2 = mRootView.findViewById(R.id.logo2);
                setTitleLogoVisiable(mTitleLogoVisiable);
            } else {
                // Hide the title template
                View titleTemplate = mRootView.findViewById(R.id.title_template);
                titleTemplate.setVisibility(View.GONE);
                mTopPanel.setVisibility(View.GONE);
                hasTitle = false;
            }
        }
        return hasTitle;
    }

	private void setupContent(LinearLayout contentPanel) {

		if (mUseHorizontalScrollView) {
			mRootView.findViewById(
					R.id.scrollView).setVisibility(View.GONE);
			mRootView.findViewById(
					R.id.scrollView2).setVisibility(View.VISIBLE);
		} else {
			mRootView.findViewById(
					R.id.scrollView2).setVisibility(View.GONE);
			mRootView.findViewById(
					R.id.scrollView).setVisibility(View.VISIBLE);
		}

		mScrollView = (ScrollView) mRootView
				.findViewById(mUseHorizontalScrollView ? 
						R.id.scrollView2 : 
							R.id.scrollView);
		mScrollView.setFocusable(false);
        mScrollView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                View child = mScrollView.getChildAt(0);
                if (child != null) {
                    int originHeight = child.getHeight();
                    int maxHeight = (int) (0.6f * DimenUtils.getWindowHeight(mContext) - DimenUtils
                            .dp2px(mContext, 140));
                    int height = originHeight;
                    if (height > maxHeight) {
                        height = maxHeight;
                    }
                    DimenUtils.updateLayout(mScrollView, -3, height);
                }
            }
        });
		
		mHorizontalScrollView = (HorizontalScrollView)mRootView.findViewById(
				R.id.horizontalScrollView);
		mHorizontalScrollView.setFocusable(false);

		// Special case for users that only want to display a String
		mMessageView = (TextView) mRootView
				.findViewById(mUseHorizontalScrollView ?
						R.id.message2 : 
							R.id.message);
		if (mMessageView == null) {
			return;
		}
		
		if (mMessage != null) {
			mMessageView.setText(mMessage);
			if (mUseHorizontalScrollView) {
				mRootView.findViewById(R.id.horizontalScrollView).setBackgroundDrawable(null);
			} else {
				mRootView.findViewById(R.id.scrollView).setBackgroundDrawable(null);
			}
			//mRootView.findViewById(R.id.titleDividerTop).setVisibility(View.VISIBLE);
		} else {
			mMessageView.setVisibility(View.GONE);
			if (mUseHorizontalScrollView) {
				mHorizontalScrollView.removeView(mMessageView);
				mScrollView.removeView(mHorizontalScrollView);
			} else {
				mScrollView.removeView(mMessageView);
			}

			if (mListView != null) {
				if (mUseHorizontalScrollView) {
					mListView.setBackgroundDrawable(mHorizontalScrollView.getBackground());
				} else {
					mListView.setBackgroundDrawable(mScrollView.getBackground());
				}
				contentPanel.removeView(mRootView
						.findViewById(mUseHorizontalScrollView ? 
								R.id.scrollView2 : 
									R.id.scrollView));
				contentPanel.addView(mListView, new LinearLayout.LayoutParams(
						MATCH_PARENT, MATCH_PARENT));
				contentPanel.setLayoutParams(new LinearLayout.LayoutParams(
						MATCH_PARENT, 0, 1.0f));
			} else {
				contentPanel.setVisibility(View.GONE);
				if(mRootView.findViewById(R.id.customPanel).getVisibility()== View.GONE)

					mRootView.findViewById(R.id.centerPanel_dotted_split_line_color).setVisibility(View.GONE);
			}
		}
	}

    private final int BUTTON_MARGIN = DimenUtils.dp2px(AiwueApplication.getAppContext().getApplicationContext(), 6);

    private boolean setupButtons() {
        int selfMargin = mCustomButtonMargin <= 0 ? BUTTON_MARGIN / 2 : mCustomButtonMargin;
        int BIT_BUTTON_POSITIVE = 1;
        int BIT_BUTTON_NEGATIVE = 2;
        int BIT_BUTTON_NEUTRAL = 4;
        int whichButtons = 0;

        mButtonPositive = (Button) mRootView.findViewById(R.id.button1);
        mButtonPositive.setOnClickListener(mButtonHandler);

        if (TextUtils.isEmpty(mButtonPositiveText)) {
            mButtonPositive.setVisibility(View.GONE);
        } else {
            mButtonPositive.setText(mButtonPositiveText);
            mButtonPositive.setVisibility(View.VISIBLE);
            mButtonPositive.getPaint().setFakeBoldText(true);
            int resId;
            if (mPositiveWarning) {
                resId = R.drawable.dialog_right_button_warning_bg;
            } else {
                resId = R.drawable.dialog_right_button_bg;
            }
            mButtonPositive.setBackgroundResource(resId);
            DimenUtils.updateLayoutMargin(mButtonPositive, selfMargin, -3, selfMargin, -3);
            whichButtons = whichButtons | BIT_BUTTON_POSITIVE;
        }

        mButtonNegative = (Button) mRootView.findViewById(R.id.button2);
        mButtonNegative.setOnClickListener(mButtonHandler);

        if (TextUtils.isEmpty(mButtonNegativeText)) {
            mButtonNegative.setVisibility(View.GONE);
            mRootView.findViewById(R.id.btn2_solid_split_line).setVisibility(View.GONE);
        } else {
            mButtonNegative.setText(mButtonNegativeText);
            mButtonNegative.setVisibility(View.VISIBLE);
            mButtonNegative.getPaint().setFakeBoldText(true);
            int resId;
            if (mNegativeEnable) {
                resId = R.color.dialog_text_normal;
            } else {
                resId = R.color.dialog_text_disable;
            }
            mButtonNegative.setTextColor(mContext.getResources().getColor(resId));
            // mRootView.findViewById(R.id.btn2_solid_split_line).setVisibility(View.VISIBLE);
            DimenUtils.updateLayoutMargin(mButtonNegative, selfMargin, -3, selfMargin, -3);
            whichButtons = whichButtons | BIT_BUTTON_NEGATIVE;
        }

        mButtonNeutral = (Button) mRootView.findViewById(R.id.button3);
        mButtonNeutral.setOnClickListener(mButtonHandler);

        if (TextUtils.isEmpty(mButtonNeutralText)) {
            mButtonNeutral.setVisibility(View.GONE);
            mRootView.findViewById(R.id.btn3_solid_split_line).setVisibility(View.GONE);
        } else {
            mButtonNeutral.setText(mButtonNeutralText);
            mButtonNeutral.setVisibility(View.VISIBLE);
            mButtonNeutral.getPaint().setFakeBoldText(true);
            // mRootView.findViewById(R.id.btn3_solid_split_line).setVisibility(View.VISIBLE);
            DimenUtils.updateLayoutMargin(mButtonNeutral, selfMargin, -3, selfMargin, -3);
            whichButtons = whichButtons | BIT_BUTTON_NEUTRAL;
        }

        /*
         * If we only have 1 button it should be centered on the layout and
         * expand to fill 50% of the available space. update fill 100% by wufeng
         * 2013.10.31
         */
        if (whichButtons == BIT_BUTTON_POSITIVE) {
            centerButton(mButtonPositive);
        } else if (whichButtons == BIT_BUTTON_NEGATIVE) {
            centerButton(mButtonNegative);
        } else if (whichButtons == BIT_BUTTON_NEUTRAL) {
            centerButton(mButtonNeutral);
        }

        View buttonPanel = mRootView.findViewById(R.id.buttonPanel);
        buttonPanel.setPadding(selfMargin + DimenUtils.dp2px(mContext, 1), selfMargin, selfMargin
                + DimenUtils.dp2px(mContext, 1), selfMargin + DimenUtils.dp2px(mContext, 3));
        if (whichButtons == 0) {
            buttonPanel.setVisibility(View.GONE);
            mRootView.findViewById(R.id.bottom_solid_split_line).setVisibility(View.GONE);
        }
        return whichButtons != 0;
    }

	private void centerButton(Button button) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) button
                .getLayoutParams();
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.width = LayoutParams.MATCH_PARENT;
        button.setLayoutParams(params);
        if (button == mButtonPositive) {
            int resId;
            if (mPositiveWarning) {
                resId = R.drawable.dialog_right_button_warning_bg;
            } else {
                resId = R.drawable.dialog_right_button_bg;
            }
            button.setBackgroundResource(resId);
        } else if (button == mButtonNegative) {
            button.setBackgroundResource(R.drawable.dialog_left_button_bg);
        }
        
        mRootView.findViewById(R.id.btn2_solid_split_line).setVisibility(View.GONE);
        mRootView.findViewById(R.id.btn3_solid_split_line).setVisibility(View.GONE);
        
//		View leftSpacer = mRootView
//				.findViewById(R.id.leftSpacer);
//		leftSpacer.setVisibility(View.VISIBLE);
//		View rightSpacer = mRootView
//				.findViewById(R.id.rightSpacer);
//		rightSpacer.setVisibility(View.VISIBLE);
	}
	
	// private void setBackground(LinearLayout topPanel,
	// LinearLayout contentPanel, View customPanel, boolean hasButtons,
	// TypedArray a, boolean hasTitle, View buttonPanel) {
	//
	// /* Get all the different background required */
	// int fullDark = a.getResourceId(
	// com.android.internal.R.styleable.AlertDialog_fullDark,
	// com.android.internal.R.drawable.popup_full_dark);
	// int topDark = a.getResourceId(
	// com.android.internal.R.styleable.AlertDialog_topDark,
	// com.android.internal.R.drawable.popup_top_dark);
	// int centerDark = a.getResourceId(
	// com.android.internal.R.styleable.AlertDialog_centerDark,
	// com.android.internal.R.drawable.popup_center_dark);
	// int bottomDark = a.getResourceId(
	// com.android.internal.R.styleable.AlertDialog_bottomDark,
	// com.android.internal.R.drawable.popup_bottom_dark);
	// int fullBright = a.getResourceId(
	// com.android.internal.R.styleable.AlertDialog_fullBright,
	// com.android.internal.R.drawable.popup_full_bright);
	// int topBright = a.getResourceId(
	// com.android.internal.R.styleable.AlertDialog_topBright,
	// com.android.internal.R.drawable.popup_top_bright);
	// int centerBright = a.getResourceId(
	// com.android.internal.R.styleable.AlertDialog_centerBright,
	// com.android.internal.R.drawable.popup_center_bright);
	// int bottomBright = a.getResourceId(
	// com.android.internal.R.styleable.AlertDialog_bottomBright,
	// com.android.internal.R.drawable.popup_bottom_bright);
	// int bottomMedium = a.getResourceId(
	// com.android.internal.R.styleable.AlertDialog_bottomMedium,
	// com.android.internal.R.drawable.popup_bottom_medium);
	//
	// /*
	// * We now set the background of all of the sections of the alert. First
	// * collect together each section that is being displayed along with
	// * whether it is on a light or dark background, then run through them
	// * setting their backgrounds. This is complicated because we need to
	// * correctly use the full, top, middle, and bottom graphics depending on
	// * how many views they are and where they appear.
	// */
	//
	// View[] views = new View[4];
	// boolean[] light = new boolean[4];
	// View lastView = null;
	// boolean lastLight = false;
	//
	// int pos = 0;
	// if (hasTitle) {
	// views[pos] = topPanel;
	// light[pos] = false;
	// pos++;
	// }
	//
	// /*
	// * The contentPanel displays either a custom text message or a ListView.
	// * If it's text we should use the dark background for ListView we should
	// * use the light background. If neither are there the contentPanel will
	// * be hidden so set it as null.
	// */
	// views[pos] = (contentPanel.getVisibility() == View.GONE) ? null
	// : contentPanel;
	// light[pos] = mListView != null;
	// pos++;
	// if (customPanel != null) {
	// views[pos] = customPanel;
	// light[pos] = mForceInverseBackground;
	// pos++;
	// }
	// if (hasButtons) {
	// views[pos] = buttonPanel;
	// light[pos] = true;
	// }
	//
	// boolean setView = false;
	// for (pos = 0; pos < views.length; pos++) {
	// View v = views[pos];
	// if (v == null) {
	// continue;
	// }
	// if (lastView != null) {
	// if (!setView) {
	// lastView.setBackgroundResource(lastLight ? topBright
	// : topDark);
	// } else {
	// lastView.setBackgroundResource(lastLight ? centerBright
	// : centerDark);
	// }
	// setView = true;
	// }
	// lastView = v;
	// lastLight = light[pos];
	// }
	//
	// if (lastView != null) {
	// if (setView) {
	//
	// /*
	// * ListViews will use the Bright background but buttons use the
	// * Medium background.
	// */
	// lastView.setBackgroundResource(lastLight ? (hasButtons ? bottomMedium
	// : bottomBright)
	// : bottomDark);
	// } else {
	// lastView.setBackgroundResource(lastLight ? fullBright
	// : fullDark);
	// }
	// }
	//
	// /*
	// * TODO: uncomment section below. The logic for this should be if it's a
	// * Contextual menu being displayed AND only a Cancel button is shown
	// * then do this.
	// */
	// // if (hasButtons && (mListView != null)) {
	//
	// /*
	// * Yet another *special* case. If there is a ListView with buttons don't
	// * put the buttons on the bottom but instead put them in the footer of
	// * the ListView this will allow more items to be displayed.
	// */
	//
	// /*
	// * contentPanel.setBackgroundResource(bottomBright);
	// * buttonPanel.setBackgroundResource(centerMedium); ViewGroup parent =
	// * (ViewGroup) mRootView.findViewById(R.id.parentPanel);
	// * parent.removeView(buttonPanel); AbsListView.LayoutParams params = new
	// * AbsListView.LayoutParams( AbsListView.LayoutParams.MATCH_PARENT,
	// * AbsListView.LayoutParams.MATCH_PARENT);
	// * buttonPanel.setLayoutParams(params);
	// * mListView.addFooterView(buttonPanel);
	// */
	// // }
	//
	// if ((mListView != null) && (mAdapter != null)) {
	// mListView.setAdapter(mAdapter);
	// if (mCheckedItem > -1) {
	// mListView.setItemChecked(mCheckedItem, true);
	// mListView.setSelection(mCheckedItem);
	// }
	// }
	// }

	public static class RecycleListView extends ListView {
		boolean mRecycleOnMeasure = true;

		public RecycleListView(Context context) {
			super(context);
		}

		public RecycleListView(Context context, AttributeSet attrs) {
			super(context, attrs);
		}

		public RecycleListView(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
		}

		protected boolean recycleOnMeasure() {
			return mRecycleOnMeasure;
		}
	}

	public static class AlertParams {
		public final Context mContext;
		public final LayoutInflater mInflater;

        public int mGravity = Gravity.CENTER;
		public int mIconId = 0;
		public Drawable mIcon;
		public CharSequence mTitle ;
		public boolean mTitleSupportMulitipleLines = false;
		public CharSequence mSubTitle;
		public boolean mTitleLogoVisiable;
		public boolean mTitleInContent;
        public boolean mTitleBottomLineExist;
		public List<View> mAppendCustomViews;
		public View mCustomTitleView;
		public CharSequence mMessage;
		public boolean mbAutoDismissPos = true;
		public boolean mbAutoDismissNeg = true;
		public boolean mbAutoDismissNeu = true;
		public CharSequence mPositiveButtonText;
		public DialogInterface.OnClickListener mPositiveButtonListener;
		public CharSequence mNegativeButtonText;
		public DialogInterface.OnClickListener mNegativeButtonListener;
		public CharSequence mNeutralButtonText;
		public DialogInterface.OnClickListener mNeutralButtonListener;
		public boolean mCancelable;
		public DialogInterface.OnCancelListener mOnCancelListener;
		public DialogInterface.OnKeyListener mOnKeyListener;
		public CharSequence[] mItems;
		public ListAdapter mAdapter;
		public DialogInterface.OnClickListener mOnClickListener;
		public View mView;
		public boolean mCustomNoPadding;
		public int mViewSpacingLeft;
		public int mViewSpacingTop;
		public int mViewSpacingRight;
		public int mViewSpacingBottom;
		public int mCustomButtonMargin;
		public boolean mViewSpacingSpecified = false;
		public boolean[] mCheckedItems;
		public boolean mIsMultiChoice;
		public boolean mIsSingleChoice;
		public int mCheckedItem = -1;
		public DialogInterface.OnMultiChoiceClickListener mOnCheckboxClickListener;
		public Cursor mCursor;
		public String mLabelColumn;
		public String mIsCheckedColumn;
		public boolean mForceInverseBackground;
		public AdapterView.OnItemSelectedListener mOnItemSelectedListener;
		public OnPrepareListViewListener mOnPrepareListViewListener;
		public boolean mRecycleOnMeasure = true;
		public boolean useBackground = true;
		public boolean transparentBackage = false;
		public boolean mUseHorizontalScrollView = false;
		public boolean mEnableShowWithSuitableHeight = false;
		public boolean mNegativeEnable = true;
		public boolean mPositiveWarning;

        public int mTitleLRPadding =18;
        public int mTitleBottomPadding = 16;
        
		/**
		 * Interface definition for a callback to be invoked before the ListView
		 * will be bound to an adapter.
		 */
		public interface OnPrepareListViewListener {

			/**
			 * Called before the ListView is bound to an adapter.
			 * 
			 * @param listView
			 *            The ListView that will be shown in the dialog.
			 */
			void onPrepareListView(ListView listView);
		}

		public AlertParams(Context context) {
			mContext = context;
			mCancelable = true;
			mInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public void apply(MyAlertController dialog) {
			if (mCustomTitleView != null) {
				dialog.setCustomTitle(mCustomTitleView);
			} else {
			    
                dialog.setGravity(mGravity);

                if (mTitle != null) {
                    dialog.setTitle(mTitle);
                }
                
                dialog.setTitleSupportMultipleLines(mTitleSupportMulitipleLines);

                if (mSubTitle != null) {
                    dialog.setSubTitle(mSubTitle);
                }
				
				dialog.setTitleLogoVisiable(mTitleLogoVisiable);
			}
			
			dialog.setTitleInContent(mTitleInContent);
            dialog.setTitleBottomLineExist(mTitleBottomLineExist);
			dialog.setContentPadding(mTitleLRPadding, mTitleBottomPadding);
            dialog.setNegativeEnable(mNegativeEnable);
            dialog.setPositiveWarning(mPositiveWarning);
			
			if (mMessage != null) {
				dialog.setMessage(mMessage);
			}
			if (mPositiveButtonText != null) {
				dialog.setButton(DialogInterface.BUTTON_POSITIVE,
						mPositiveButtonText, mPositiveButtonListener, null);
				
				dialog.mbAutoBtnPosDismiss = mbAutoDismissPos;
			}
			if (mNegativeButtonText != null) {
				dialog.setButton(DialogInterface.BUTTON_NEGATIVE,
						mNegativeButtonText, mNegativeButtonListener, null);
				
				dialog.mbAutoBtnNegDismiss = mbAutoDismissNeg;
			}
			if (mNeutralButtonText != null) {
				dialog.setButton(DialogInterface.BUTTON_NEUTRAL,
						mNeutralButtonText, mNeutralButtonListener, null);
				
				dialog.mbAutoBtnNeutDismiss = mbAutoDismissNeu;
			}
			if (mForceInverseBackground) {
				dialog.setInverseBackgroundForced(true);
			}
			// For a list, the client can either supply an array of items or an
			// adapter or a cursor
			if ((mItems != null) || (mCursor != null) || (mAdapter != null)) {
				createListView(dialog);
			}
			if (mView != null) {
				if (mViewSpacingSpecified) {
					dialog.setView(mView, mViewSpacingLeft, mViewSpacingTop,
							mViewSpacingRight, mViewSpacingBottom);
				} else {
					dialog.setView(mView);
				}
				dialog.setCustomNoPadding(mCustomNoPadding);
			}
			
//			if(!useBackground)
//			{
//				dialog.useBackground = false;
//			}
			
			dialog.transparentBackage = transparentBackage;
 
			dialog.mUseHorizontalScrollView = mUseHorizontalScrollView;
			
			dialog.mEnableShowWithSuitableHeight = mEnableShowWithSuitableHeight;
			
			dialog.mCustomButtonMargin = mCustomButtonMargin;
			
			/*
			 * dialog.setCancelable(mCancelable);
			 * dialog.setOnCancelListener(mOnCancelListener); if (mOnKeyListener
			 * != null) { dialog.setOnKeyListener(mOnKeyListener); }
			 */
		}

		private void createListView(final MyAlertController dialog) {
			final RecycleListView listView = (RecycleListView) mInflater
					.inflate(R.layout.select_dialog, null);
			
			ListAdapter adapter;

			if (mIsMultiChoice) {
				if (mCursor == null) {
					adapter = new ArrayAdapter<CharSequence>(
							mContext,
							R.layout.select_dialog_multichoice,
							R.id.text1, mItems) {
						@Override
						public View getView(int position, View convertView,
											ViewGroup parent) {
							View view = super.getView(position, convertView,
									parent);
							if (mCheckedItems != null) {
								boolean isItemChecked = mCheckedItems[position];
								if (isItemChecked) {
									listView.setItemChecked(position, true);
								}
							}
							return view;
						}
					};
				} else {
					adapter = new CursorAdapter(mContext, mCursor, false) {
						private final int mLabelIndex;
						private final int mIsCheckedIndex;

						{
							final Cursor cursor = getCursor();
							mLabelIndex = cursor
									.getColumnIndexOrThrow(mLabelColumn);
							mIsCheckedIndex = cursor
									.getColumnIndexOrThrow(mIsCheckedColumn);
						}

						@Override
						public void bindView(View view, Context context,
											 Cursor cursor) {
//							CheckedTextView text = (CheckedTextView) view
//									.findViewById(com.android.internal.R.id.text1);
//							liaixiong:编译时无法找到com.android.internel.R.id.XXX
//							网上介绍了两种方法方法
//							以获取com.android.internal.R.id.month为例
//							方法一：反射变量
//							try{
//								Class c = Class.forName("com.android.internal.R$id");
//								Object obj = c.newInstance();
//								Field field = c.getField("month");
//								id = field.getInt(obj);
//							}catch(Exception e){
//							}
//							方法二：通过Resource
//							Resources mResources = Resources.getSystem();  //getResources()测试也可以
//							id = mResources.getIdentifier("month", "id", "android");
//							请实测是否可行
							CheckedTextView text = (CheckedTextView) view
									.findViewById(Resources.getSystem()
											.getIdentifier("text1", "id", "android"));
							text.setText(cursor.getString(mLabelIndex));
							listView.setItemChecked(cursor.getPosition(),
									cursor.getInt(mIsCheckedIndex) == 1);
						}

						@Override
						public View newView(Context context, Cursor cursor,
											ViewGroup parent) {
							return mInflater
									.inflate(
											R.layout.select_dialog_multichoice,
											parent, false);
						}

					};
				}
			} else {
				int layout = mIsSingleChoice ? R.layout.select_dialog_singlechoice
						: R.layout.select_dialog_item;
				if (mCursor == null) {
					adapter = (mAdapter != null) ? mAdapter
							: new ArrayAdapter<CharSequence>(mContext, layout,
									R.id.text1, mItems);
				} else {
//					adapter = new SimpleCursorAdapter(mContext, layout,
//							mCursor, new String[] { mLabelColumn },
//							new int[] { com.android.internal.R.id.text1 });
					//liaixiong:需要验证是否可以得到internal text1
					adapter = new SimpleCursorAdapter(mContext, layout,
							mCursor, new String[] { mLabelColumn },
							new int[] { Resources.getSystem().getIdentifier("text1", "id", "android") });
				}
			}

			if (mOnPrepareListViewListener != null) {
				mOnPrepareListViewListener.onPrepareListView(listView);
			}

			/*
			 * Don't directly set the adapter on the ListView as we might want
			 * to add a footer to the ListView later.
			 */
			dialog.mAdapter = adapter;
			dialog.mCheckedItem = mCheckedItem;

			if (mOnClickListener != null) {
				listView.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View v,
											int position, long id) {
						mOnClickListener.onClick(dialog.mDialogInterface,
								position);
						if (!mIsSingleChoice) {
							dialog.mDialogInterface.dismiss();
						}
					}
				});
			} else if (mOnCheckboxClickListener != null) {
				listView.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View v,
											int position, long id) {
						if (mCheckedItems != null) {
							mCheckedItems[position] = listView
									.isItemChecked(position);
						}
						mOnCheckboxClickListener.onClick(
								dialog.mDialogInterface, position,
								listView.isItemChecked(position));
					}
				});
			}

			// Attach a given OnItemSelectedListener to the ListView
			if (mOnItemSelectedListener != null) {
				listView.setOnItemSelectedListener(mOnItemSelectedListener);
			}

			if (mIsSingleChoice) {
				listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			} else if (mIsMultiChoice) {
				listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
			}
			listView.mRecycleOnMeasure = mRecycleOnMeasure;
			
			dialog.mListView = listView;
		}
	}

	/**
     * Created by pc on 2014/11/28.
     */
    public abstract static class BuilderBase<T> {
        public static final int DIALOG = 1;
        public static final int FLOAT_DIALOG = 2;
        protected int mCurrDialogType;
        protected final AlertParams P;

        /**
         * Constructor using a context and theme for (T)this BuilderBase and the
         * {@link AlertDialog} it creates.
         */
        public BuilderBase(Context context, int dialogType) {
            mCurrDialogType = dialogType;
            P = new AlertParams(context);
        }

    //        public BuilderBase setGravity(int gravity) {
    //            P.mGravity = gravity;
    //            return (T)this;
    //        }

        /**
         * Set the title using the given resource id.
         *
         * @return (T)this BuilderBase object to allow for chaining of calls to set
         *         methods
         */
        public T setTitle(int titleId) {
            P.mTitle = P.mContext.getText(titleId);
            return (T)this;
        }

        /**
         * Set the title displayed in the {@link Dialog}.
         *
         * @return (T)this BuilderBase object to allow for chaining of calls to set
         *         methods
         */
        public T setTitle(CharSequence title) {
            P.mTitle = title;
            return (T)this;
        }

        public T setTitleSupportMultipleLines(boolean  support) {
            P.mTitleSupportMulitipleLines = support;
            return (T)this;
        }

    //		public BuilderBase setSubTitle(CharSequence subTitle)
    //		{
    //			P.mSubTitle = subTitle;
    //			return (T)this;
    //		}

        public T setTitleLogoVisibility(boolean visiable) {
            P.mTitleLogoVisiable = visiable;
            return (T)this;
        }

        public T setCustomTitleView(View customTitleView) {
            P.mCustomTitleView = customTitleView;
            return (T)this;
        }

        public T setTitleInContent(boolean enable) {
            P.mTitleInContent = enable;
            return (T)this;
        }

        /**
         * 当setTitleInContent为true的时候，设置title的下划线强制显示
         * 且设置左右padding
         * @param enable
         * @return
         */
        public T setTitleBottomLineExist(boolean enable) {
            P.mTitleBottomLineExist = enable;
            return (T)this;
        }

        public void setContentPadding(int leftRightPadding ,int bottomPadding){
            P.mTitleLRPadding = leftRightPadding;
            P.mTitleBottomPadding = bottomPadding;
        }
    //        public BuilderBase setNegativeEnable(boolean enable) {
    //            P.mNegativeEnable = enable;
    //            return (T)this;
    //        }

        public T setPositiveWarning(boolean enable) {
            P.mPositiveWarning = enable;
            return (T)this;
        }

    //		public BuilderBase appendViewToCustomPanel(View view){
    //			if(P.mAppendCustomViews == null)
    //			{
    //				P.mAppendCustomViews = new ArrayList<View>();
    //			}
    //			P.mAppendCustomViews.add(view);
    //			return (T)this;
    //		}

        /**
         * Set the title using the custom view {@code customTitleView}. The
         * methods {@link #setTitle(int)} and {@link #setIcon(int)} should be
         * sufficient for most titles, but (T)this is provided if the title needs
         * more customization. Using (T)this will replace the title and icon set
         * via the other methods.
         *
         * @param customTitleView
         *            The custom view to use as the title.
         *
         * @return (T)this BuilderBase object to allow for chaining of calls to set
         *         methods
         */
    //		public BuilderBase setCustomTitle(View customTitleView) {
    //			P.mCustomTitleView = customTitleView;
    //			return (T)this;
    //		}

        /**
         * Set the message to display using the given resource id.
         *
         * @return (T)this BuilderBase object to allow for chaining of calls to set
         *         methods
         */
        public T setMessage(int messageId) {
            P.mMessage = P.mContext.getText(messageId);
            return (T)this;
        }

        /**
         * Set the message to display.
         *
         * @return (T)this BuilderBase object to allow for chaining of calls to set
         *         methods
         */
        public T setMessage(CharSequence message) {
            P.mMessage = message;
            return (T)this;
        }

        /**
         * Set the resource id of the {@link Drawable} to be used in the title.
         *
         * @return (T)this BuilderBase object to allow for chaining of calls to set
         *         methods
         */
    //		public BuilderBase setIcon(int iconId) {
    //			P.mIconId = iconId;
    //			return (T)this;
    //		}

        /**
         * Set the {@link Drawable} to be used in the title.
         *
         * @return (T)this BuilderBase object to allow for chaining of calls to set
         *         methods
         */
    //		public BuilderBase setIcon(Drawable icon) {
    //			P.mIcon = icon;
    //			return (T)this;
    //		}

        /**
         * Set a listener to be invoked when the positive button of the dialog
         * is pressed.
         *
         * @param textId
         *            The resource id of the text to display in the positive
         *            button
         * @param listener
         *            The {@link DialogInterface.OnClickListener} to use.
         *
         * @return (T)this BuilderBase object to allow for chaining of calls to set
         *         methods
         */
        public T setPositiveButton(int textId,final DialogInterface.OnClickListener listener) {
            P.mPositiveButtonText = P.mContext.getText(textId);
            P.mPositiveButtonListener = listener;
            return (T)this;
        }

        /**
         * Set a listener to be invoked when the positive button of the dialog
         * is pressed.
         *
         * @param text
         *            The text to display in the positive button
         * @param listener
         *            The {@link DialogInterface.OnClickListener} to use.
         *
         * @return (T)this BuilderBase object to allow for chaining of calls to set
         *         methods
         */
        public T setPositiveButton(CharSequence text, final DialogInterface.OnClickListener listener) {
            P.mPositiveButtonText = text;
            P.mPositiveButtonListener = listener;
            return (T)this;
        }

        public T setPositiveButtonAutoDismiss(boolean bAutoDiss){
            P.mbAutoDismissPos = bAutoDiss;
            return (T)this;
        }

        /**
         * Set a listener to be invoked when the negative button of the dialog
         * is pressed.
         *
         * @param textId
         *            The resource id of the text to display in the negative
         *            button
         * @param listener
         *            The {@link DialogInterface.OnClickListener} to use.
         *
         * @return (T)this BuilderBase object to allow for chaining of calls to set
         *         methods
         */
        public T setNegativeButton(int textId, final DialogInterface.OnClickListener listener) {
            P.mNegativeButtonText = P.mContext.getText(textId);
            P.mNegativeButtonListener = listener;
            return (T)this;
        }

        public T setNegativeButtonAutoDismiss(boolean bAutoDiss){
            P.mbAutoDismissNeg = bAutoDiss;
            return (T)this;
        }

        /**
         * Set a listener to be invoked when the negative button of the dialog
         * is pressed.
         *
         * @param text
         *            The text to display in the negative button
         * @param listener
         *            The {@link DialogInterface.OnClickListener} to use.
         *
         * @return (T)this BuilderBase object to allow for chaining of calls to set
         *         methods
         */
        public T setNegativeButton(CharSequence text, final DialogInterface.OnClickListener listener) {
            P.mNegativeButtonText = text;
            P.mNegativeButtonListener = listener;
            return (T)this;
        }


        /**
         * Set a listener to be invoked when the neutral button of the dialog is
         * pressed.
         *
         * @param textId
         *            The resource id of the text to display in the neutral
         *            button
         * @param listener
         *            The {@link DialogInterface.OnClickListener} to use.
         *
         * @return (T)this BuilderBase object to allow for chaining of calls to set
         *         methods
         */
    //		public BuilderBase setNeutralButton(int textId,
    //				final OnClickListener listener) {
    //			P.mNeutralButtonText = P.mContext.getText(textId);
    //			P.mNeutralButtonListener = listener;
    //			return (T)this;
    //		}

    //		public BuilderBase setNeutralButtonAutoDismiss(boolean bAutoDiss){
    //			P.mbAutoDismissNeu = bAutoDiss;
    //			return (T)this;
    //		}

        /**
         * Set a listener to be invoked when the neutral button of the dialog is
         * pressed.
         *
         * @param text
         *            The text to display in the neutral button
         * @param listener
         *            The {@link DialogInterface.OnClickListener} to use.
         *
         * @return (T)this BuilderBase object to allow for chaining of calls to set
         *         methods
         */
    //		public BuilderBase setNeutralButton(CharSequence text,
    //				final OnClickListener listener) {
    //			P.mNeutralButtonText = text;
    //			P.mNeutralButtonListener = listener;
    //			return (T)this;
    //		}

        /**
         * Sets whether the dialog is cancelable or not default is true.
         *
         * @return (T)this BuilderBase object to allow for chaining of calls to set
         *         methods
         */
        public T setCancelable(boolean cancelable) {
            P.mCancelable = cancelable;
            return (T)this;
        }

        /**
         * Sets the callback that will be called if the dialog is canceled.
         *
         * @see #setCancelable(boolean)
         *
         * @return (T)this BuilderBase object to allow for chaining of calls to set
         *         methods
         */
        public T setOnCancelListener(DialogInterface.OnCancelListener onCancelListener) {
            P.mOnCancelListener = onCancelListener;
            return (T)this;
        }

        /**
         * Sets the callback that will be called if a key is dispatched to the
         * dialog.
         *
         * @return (T)this BuilderBase object to allow for chaining of calls to set
         *         methods
         */
        public T setOnKeyListener(DialogInterface.OnKeyListener onKeyListener) {
            P.mOnKeyListener = onKeyListener;
            return (T)this;
        }

        /**
         * Set a list of items to be displayed in the dialog as the content, you
         * will be notified of the selected item via the supplied listener. (T)this
         * should be an array type i.e. R.array.foo
         *
         * @return (T)this BuilderBase object to allow for chaining of calls to set
         *         methods
         */
    //		public BuilderBase setItems(int itemsId, final OnClickListener listener) {
    //			P.mItems = P.mContext.getResources().getTextArray(itemsId);
    //			P.mOnClickListener = listener;
    //			return (T)this;
    //		}

        /**
         * Set a list of items to be displayed in the dialog as the content, you
         * will be notified of the selected item via the supplied listener.
         *
         * @return (T)this BuilderBase object to allow for chaining of calls to set
         *         methods
         */
    //		public BuilderBase setItems(CharSequence[] items,
    //				final OnClickListener listener) {
    //			P.mItems = items;
    //			P.mOnClickListener = listener;
    //			return (T)this;
    //		}

        /**
         * Set a list of items, which are supplied by the given
         * {@link ListAdapter}, to be displayed in the dialog as the content,
         * you will be notified of the selected item via the supplied listener.
         *
         * @param adapter
         *            The {@link ListAdapter} to supply the list of items
         * @param listener
         *            The listener that will be called when an item is clicked.
         *
         * @return (T)this BuilderBase object to allow for chaining of calls to set
         *         methods
         */
    //		public BuilderBase setAdapter(final ListAdapter adapter,
    //				final OnClickListener listener) {
    //			P.mAdapter = adapter;
    //			P.mOnClickListener = listener;
    //			return (T)this;
    //		}

        /**
         * Set a list of items, which are supplied by the given {@link Cursor},
         * to be displayed in the dialog as the content, you will be notified of
         * the selected item via the supplied listener.
         *
         * @param cursor
         *            The {@link Cursor} to supply the list of items
         * @param listener
         *            The listener that will be called when an item is clicked.
         * @param labelColumn
         *            The column name on the cursor containing the string to
         *            display in the label.
         *
         * @return (T)this BuilderBase object to allow for chaining of calls to set
         *         methods
         */
    //		public BuilderBase setCursor(final Cursor cursor,
    //				final OnClickListener listener, String labelColumn) {
    //			P.mCursor = cursor;
    //			P.mLabelColumn = labelColumn;
    //			P.mOnClickListener = listener;
    //			return (T)this;
    //		}

        /**
         * Set a list of items to be displayed in the dialog as the content, you
         * will be notified of the selected item via the supplied listener. (T)this
         * should be an array type, e.g. R.array.foo. The list will have a check
         * mark displayed to the right of the text for each checked item.
         * Clicking on an item in the list will not dismiss the dialog. Clicking
         * on a button will dismiss the dialog.
         *
         * @param itemsId
         *            the resource id of an array i.e. R.array.foo
         * @param checkedItems
         *            specifies which items are checked. It should be null in
         *            which case no items are checked. If non null it must be
         *            exactly the same length as the array of items.
         * @param listener
         *            notified when an item on the list is clicked. The dialog
         *            will not be dismissed when an item is clicked. It will
         *            only be dismissed if clicked on a button, if no buttons
         *            are supplied it's up to the user to dismiss the dialog.
         *
         * @return (T)this BuilderBase object to allow for chaining of calls to set
         *         methods
         */
    //		public BuilderBase setMultiChoiceItems(int itemsId, boolean[] checkedItems,
    //				final OnMultiChoiceClickListener listener) {
    //			P.mItems = P.mContext.getResources().getTextArray(itemsId);
    //			P.mOnCheckboxClickListener = listener;
    //			P.mCheckedItems = checkedItems;
    //			P.mIsMultiChoice = true;
    //			return (T)this;
    //		}

        /**
         * Set a list of items to be displayed in the dialog as the content, you
         * will be notified of the selected item via the supplied listener. The
         * list will have a check mark displayed to the right of the text for
         * each checked item. Clicking on an item in the list will not dismiss
         * the dialog. Clicking on a button will dismiss the dialog.
         *
         * @param items
         *            the text of the items to be displayed in the list.
         * @param checkedItems
         *            specifies which items are checked. It should be null in
         *            which case no items are checked. If non null it must be
         *            exactly the same length as the array of items.
         * @param listener
         *            notified when an item on the list is clicked. The dialog
         *            will not be dismissed when an item is clicked. It will
         *            only be dismissed if clicked on a button, if no buttons
         *            are supplied it's up to the user to dismiss the dialog.
         *
         * @return (T)this BuilderBase object to allow for chaining of calls to set
         *         methods
         */
    //		public BuilderBase setMultiChoiceItems(CharSequence[] items,
    //				boolean[] checkedItems,
    //				final OnMultiChoiceClickListener listener) {
    //			P.mItems = items;
    //			P.mOnCheckboxClickListener = listener;
    //			P.mCheckedItems = checkedItems;
    //			P.mIsMultiChoice = true;
    //			return (T)this;
    //		}

        /**
         * Set a list of items to be displayed in the dialog as the content, you
         * will be notified of the selected item via the supplied listener. The
         * list will have a check mark displayed to the right of the text for
         * each checked item. Clicking on an item in the list will not dismiss
         * the dialog. Clicking on a button will dismiss the dialog.
         *
         * @param cursor
         *            the cursor used to provide the items.
         * @param isCheckedColumn
         *            specifies the column name on the cursor to use to
         *            determine whether a checkbox is checked or not. It must
         *            return an integer value where 1 means checked and 0 means
         *            unchecked.
         * @param labelColumn
         *            The column name on the cursor containing the string to
         *            display in the label.
         * @param listener
         *            notified when an item on the list is clicked. The dialog
         *            will not be dismissed when an item is clicked. It will
         *            only be dismissed if clicked on a button, if no buttons
         *            are supplied it's up to the user to dismiss the dialog.
         *
         * @return (T)this BuilderBase object to allow for chaining of calls to set
         *         methods
         */
    //		public BuilderBase setMultiChoiceItems(Cursor cursor,
    //				String isCheckedColumn, String labelColumn,
    //				final OnMultiChoiceClickListener listener) {
    //			P.mCursor = cursor;
    //			P.mOnCheckboxClickListener = listener;
    //			P.mIsCheckedColumn = isCheckedColumn;
    //			P.mLabelColumn = labelColumn;
    //			P.mIsMultiChoice = true;
    //			return (T)this;
    //		}

        /**
         * Set a list of items to be displayed in the dialog as the content, you
         * will be notified of the selected item via the supplied listener. (T)this
         * should be an array type i.e. R.array.foo The list will have a check
         * mark displayed to the right of the text for the checked item.
         * Clicking on an item in the list will not dismiss the dialog. Clicking
         * on a button will dismiss the dialog.
         *
         * @param itemsId
         *            the resource id of an array i.e. R.array.foo
         * @param checkedItem
         *            specifies which item is checked. If -1 no items are
         *            checked.
         * @param listener
         *            notified when an item on the list is clicked. The dialog
         *            will not be dismissed when an item is clicked. It will
         *            only be dismissed if clicked on a button, if no buttons
         *            are supplied it's up to the user to dismiss the dialog.
         *
         * @return (T)this BuilderBase object to allow for chaining of calls to set
         *         methods
         */
    //		public BuilderBase setSingleChoiceItems(int itemsId, int checkedItem,
    //				final OnClickListener listener) {
    //			P.mItems = P.mContext.getResources().getTextArray(itemsId);
    //			P.mOnClickListener = listener;
    //			P.mCheckedItem = checkedItem;
    //			P.mIsSingleChoice = true;
    //			return (T)this;
    //		}

        /**
         * Set a list of items to be displayed in the dialog as the content, you
         * will be notified of the selected item via the supplied listener. The
         * list will have a check mark displayed to the right of the text for
         * the checked item. Clicking on an item in the list will not dismiss
         * the dialog. Clicking on a button will dismiss the dialog.
         *
         * @param cursor
         *            the cursor to retrieve the items from.
         * @param checkedItem
         *            specifies which item is checked. If -1 no items are
         *            checked.
         * @param labelColumn
         *            The column name on the cursor containing the string to
         *            display in the label.
         * @param listener
         *            notified when an item on the list is clicked. The dialog
         *            will not be dismissed when an item is clicked. It will
         *            only be dismissed if clicked on a button, if no buttons
         *            are supplied it's up to the user to dismiss the dialog.
         *
         * @return (T)this BuilderBase object to allow for chaining of calls to set
         *         methods
         */
    //		public BuilderBase setSingleChoiceItems(Cursor cursor, int checkedItem,
    //				String labelColumn, final OnClickListener listener) {
    //			P.mCursor = cursor;
    //			P.mOnClickListener = listener;
    //			P.mCheckedItem = checkedItem;
    //			P.mLabelColumn = labelColumn;
    //			P.mIsSingleChoice = true;
    //			return (T)this;
    //		}

        /**
         * Set a list of items to be displayed in the dialog as the content, you
         * will be notified of the selected item via the supplied listener. The
         * list will have a check mark displayed to the right of the text for
         * the checked item. Clicking on an item in the list will not dismiss
         * the dialog. Clicking on a button will dismiss the dialog.
         *
         * @param items
         *            the items to be displayed.
         * @param checkedItem
         *            specifies which item is checked. If -1 no items are
         *            checked.
         * @param listener
         *            notified when an item on the list is clicked. The dialog
         *            will not be dismissed when an item is clicked. It will
         *            only be dismissed if clicked on a button, if no buttons
         *            are supplied it's up to the user to dismiss the dialog.
         *
         * @return (T)this BuilderBase object to allow for chaining of calls to set
         *         methods
         */
    //		public BuilderBase setSingleChoiceItems(CharSequence[] items,
    //				int checkedItem, final OnClickListener listener) {
    //			P.mItems = items;
    //			P.mOnClickListener = listener;
    //			P.mCheckedItem = checkedItem;
    //			P.mIsSingleChoice = true;
    //			return (T)this;
    //		}

        /**
         * Set a list of items to be displayed in the dialog as the content, you
         * will be notified of the selected item via the supplied listener. The
         * list will have a check mark displayed to the right of the text for
         * the checked item. Clicking on an item in the list will not dismiss
         * the dialog. Clicking on a button will dismiss the dialog.
         *
         * @param adapter
         *            The {@link ListAdapter} to supply the list of items
         * @param checkedItem
         *            specifies which item is checked. If -1 no items are
         *            checked.
         * @param listener
         *            notified when an item on the list is clicked. The dialog
         *            will not be dismissed when an item is clicked. It will
         *            only be dismissed if clicked on a button, if no buttons
         *            are supplied it's up to the user to dismiss the dialog.
         *
         * @return (T)this BuilderBase object to allow for chaining of calls to set
         *         methods
         */
    //		public BuilderBase setSingleChoiceItems(ListAdapter adapter,
    //				int checkedItem, final OnClickListener listener) {
    //			P.mAdapter = adapter;
    //			P.mOnClickListener = listener;
    //			P.mCheckedItem = checkedItem;
    //			P.mIsSingleChoice = true;
    //			return (T)this;
    //		}

        /**
         * Sets a listener to be invoked when an item in the list is selected.
         *
         * @param listener
         *            The listener to be invoked.
         * @see AdapterView#setOnItemSelectedListener(AdapterView.OnItemSelectedListener)
         *
         * @return (T)this BuilderBase object to allow for chaining of calls to set
         *         methods
         */
    //		public BuilderBase setOnItemSelectedListener(
    //				final AdapterView.OnItemSelectedListener listener) {
    //			P.mOnItemSelectedListener = listener;
    //			return (T)this;
    //		}

        /**
         * Set a custom view to be the contents of the Dialog. If the supplied
         * view is an instance of a {@link ListView} the light background will
         * be used.
         *
         * @param view
         *            The view to use as the contents of the Dialog.
         *
         * @return (T)this BuilderBase object to allow for chaining of calls to set
         *         methods
         */
        public T setView(View view) {
            P.mView = view;
            P.mViewSpacingSpecified = false;
            return (T)this;
        }

        public T setCustomNoPadding(boolean isNoPadding) {
            P.mCustomNoPadding = isNoPadding;
            return (T)this;
        }

    //        public BuilderBase setCustomButtonMargin(int margin) {
    //        	if (margin < 0) {
    //        		margin = 0;
    //        	}
    //        	P.mCustomButtonMargin = margin;
    //        	return (T)this;
    //        }

        /**
         *
         * @param view
         * @param useBackground
         * @return
         */
        public T setView(View view, boolean useBackground){
            P.mView = view;
            P.mViewSpacingSpecified = false;
            if(!useBackground)
            {
                P.useBackground = false;
            }
            return (T)this;
        }

    //		public BuilderBase setTransparentBgView(View view, boolean transparentBackage) {
    //			P.mView = view;
    //			P.mViewSpacingSpecified = false;
    //			P.transparentBackage = transparentBackage;
    //			return (T)this;
    //		}

        /**
         * Set a custom view to be the contents of the Dialog, specifying the
         * spacing to appear around that view. If the supplied view is an
         * instance of a {@link ListView} the light background will be used.
         *
         * @param view
         *            The view to use as the contents of the Dialog.
         * @param viewSpacingLeft
         *            Spacing between the left edge of the view and the dialog
         *            frame
         * @param viewSpacingTop
         *            Spacing between the top edge of the view and the dialog
         *            frame
         * @param viewSpacingRight
         *            Spacing between the right edge of the view and the dialog
         *            frame
         * @param viewSpacingBottom
         *            Spacing between the bottom edge of the view and the dialog
         *            frame
         * @return (T)this BuilderBase object to allow for chaining of calls to set
         *         methods
         *
         *
         *         (T)this is currently hidden because it seems like people should
         *         just be able to put padding around the view.
         * @hide
         */
        public T setView(View view, int viewSpacingLeft, int viewSpacingTop, int viewSpacingRight, int viewSpacingBottom) {
            P.mView = view;
            P.mViewSpacingSpecified = true;
            P.mViewSpacingLeft = viewSpacingLeft;
            P.mViewSpacingTop = viewSpacingTop;
            P.mViewSpacingRight = viewSpacingRight;
            P.mViewSpacingBottom = viewSpacingBottom;
            return (T)this;
        }

        /**
         * Sets the Dialog to use the inverse background, regardless of what the
         * contents is.
         *
         * @param useInverseBackground
         *            Whether to use the inverse background
         *
         * @return (T)this BuilderBase object to allow for chaining of calls to set
         *         methods
         */
    //		public BuilderBase setInverseBackgroundForced(boolean useInverseBackground) {
    //			P.mForceInverseBackground = useInverseBackground;
    //			return (T)this;
    //		}

    //		/**
    //		 * @hide
    //		 */
    //		public BuilderBase setRecycleOnMeasureEnabled(boolean enabled) {
    //			P.mRecycleOnMeasure = enabled;
    //			return (T)this;
    //		}

        public T setUseHorizontalScrollViewFlag(boolean use) {
            P.mUseHorizontalScrollView = use;
            return (T)this;
        }

        public T enableShowWithSuitableHeight(boolean enable) {
            P.mEnableShowWithSuitableHeight = enable;
            return (T)this;
        }

        public boolean checkActivityExist() {
            if (P.mContext != null && P.mContext instanceof Activity) {
                Activity activity = (Activity) P.mContext;
                if (activity.isFinishing()) {
                    return false;
                }
            }
            return true;
        }

        public T setTransparentBgView(boolean transparentBackage) {
            P.transparentBackage = transparentBackage;
            return (T)this;
        }

        /**
         * Creates a {@link AlertDialog} with the arguments supplied to (T)this
         * BuilderBase. It does not {@link Dialog#show()} the dialog. (T)this allows
         * the user to do any extra processing before displaying the dialog. Use
         * {@link #show()} if you don't have any other processing to do and want
         * (T)this to be created and displayed.
         */
        public Object create() {
            return null;
        };

        /**
         * Creates a {@link AlertDialog} with the arguments supplied to (T)this
         * BuilderBase and {@link Dialog#show()}'s the dialog.
         */
        public abstract Object show();

        /**
         * show a outside cancelable dialog
         * @return
         */
        public abstract Object showIsOutsideCancelable(boolean isCancel);
    }
}