package com.aiwue.widget;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

/**
 * Created by pc on 2014/11/28.
 */
public abstract  class BuilderBase<T> {
    public static final int DIALOG = 1;
    public static final int FLOAT_DIALOG = 2;
    protected int mCurrDialogType;
    protected final MyAlertController.AlertParams P;

    /**
     * Constructor using a context and theme for (T)this BuilderBase and the
     * {@link android.app.AlertDialog} it creates.
     */
    public BuilderBase(Context context, int dialogType) {
        mCurrDialogType = dialogType;
        P = new MyAlertController.AlertParams(context);
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
     * Set the title displayed in the {@link android.app.Dialog}.
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
     * Set the resource id of the {@link android.graphics.drawable.Drawable} to be used in the title.
     *
     * @return (T)this BuilderBase object to allow for chaining of calls to set
     *         methods
     */
//		public BuilderBase setIcon(int iconId) {
//			P.mIconId = iconId;
//			return (T)this;
//		}

    /**
     * Set the {@link android.graphics.drawable.Drawable} to be used in the title.
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
     * {@link android.widget.ListAdapter}, to be displayed in the dialog as the content,
     * you will be notified of the selected item via the supplied listener.
     *
     * @param adapter
     *            The {@link android.widget.ListAdapter} to supply the list of items
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
     * Set a list of items, which are supplied by the given {@link android.database.Cursor},
     * to be displayed in the dialog as the content, you will be notified of
     * the selected item via the supplied listener.
     *
     * @param cursor
     *            The {@link android.database.Cursor} to supply the list of items
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
     *            The {@link android.widget.ListAdapter} to supply the list of items
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
     * @see android.widget.AdapterView#setOnItemSelectedListener(android.widget.AdapterView.OnItemSelectedListener)
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
     * view is an instance of a {@link android.widget.ListView} the light background will
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
     * instance of a {@link android.widget.ListView} the light background will be used.
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
     * Creates a {@link android.app.AlertDialog} with the arguments supplied to (T)this
     * BuilderBase. It does not {@link android.app.Dialog#show()} the dialog. (T)this allows
     * the user to do any extra processing before displaying the dialog. Use
     * {@link #show()} if you don't have any other processing to do and want
     * (T)this to be created and displayed.
     */
    public Object create() {
        return null;
    };

    /**
     * Creates a {@link android.app.AlertDialog} with the arguments supplied to (T)this
     * BuilderBase and {@link android.app.Dialog#show()}'s the dialog.
     */
    public abstract Object show();

    /**
     * show a outside cancelable dialog
     * @return
     */
    public abstract Object showIsOutsideCancelable(boolean isCancel);
}
