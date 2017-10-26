package com.quduquxie.common.widget;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;

/**
 * Created on 17/5/23.
 * Created by crazylei.
 */

public class CustomRadioGroup extends LinearLayout {
    private int checkedID = -1;
    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener;
    private boolean change = false;
    private OnCheckedChangeListener checkedChangeListener;
    private PassThroughHierarchyChangeListener onHierarchyChangeListener;

    public CustomRadioGroup(Context context) {
        super(context);
        setOrientation(VERTICAL);
        init();
    }

    public CustomRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        onCheckedChangeListener = new CheckedStateTracker();
        onHierarchyChangeListener = new PassThroughHierarchyChangeListener();
        super.setOnHierarchyChangeListener(onHierarchyChangeListener);
    }

    @Override
    public void setOnHierarchyChangeListener(OnHierarchyChangeListener hierarchyChangeListener) {
        this.onHierarchyChangeListener.hierarchyChangeListener = hierarchyChangeListener;
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        List<RadioButton> radioButtons = getAllRadioButton(child);
        if (radioButtons != null && radioButtons.size() > 0) {
            for (RadioButton radioButton : radioButtons) {
                if (radioButton.isChecked()) {
                    change = true;
                    if (checkedID != -1) {
                        setCheckedStateForView(checkedID, false);
                    }
                    change = false;
                    setCheckedId(radioButton.getId());
                }
            }
        }
        super.addView(child, index, params);
    }

    private List<RadioButton> getAllRadioButton(View child) {
        List<RadioButton> radioButtons = new ArrayList<>();
        if (child instanceof RadioButton) {
            radioButtons.add((RadioButton) child);
        } else if (child instanceof ViewGroup) {
            int counts = ((ViewGroup) child).getChildCount();
            for (int i = 0; i < counts; i++) {
                radioButtons.addAll(getAllRadioButton(((ViewGroup) child).getChildAt(i)));
            }
        }
        return radioButtons;
    }

    public void check(int id) {
        if (id != -1 && (id == checkedID)) {
            return;
        }

        if (checkedID != -1) {
            setCheckedStateForView(checkedID, false);
        }

        if (id != -1) {
            setCheckedStateForView(id, true);
        }

        setCheckedId(id);
    }

    private void setCheckedId(int id) {
        checkedID = id;
        if (checkedChangeListener != null) {
            checkedChangeListener.onCheckedChanged(this, checkedID);
        }
    }

    private void setCheckedStateForView(int viewId, boolean checked) {
        View checkedView = findViewById(viewId);
        if (checkedView != null && checkedView instanceof RadioButton) {
            ((RadioButton) checkedView).setChecked(checked);
        }
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener checkedChangeListener) {
        this.checkedChangeListener = checkedChangeListener;
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new CustomRadioGroup.LayoutParams(getContext(), attrs);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return layoutParams instanceof CustomRadioGroup.LayoutParams;
    }

    @Override
    protected LinearLayout.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        event.setClassName(CustomRadioGroup.class.getName());
    }

    @Override
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setClassName(CustomRadioGroup.class.getName());
    }

    public static class LayoutParams extends LinearLayout.LayoutParams {

        public LayoutParams(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(int width, int height, float initWeight) {
            super(width, height, initWeight);
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
        }

        public LayoutParams(MarginLayoutParams marginLayoutParams) {
            super(marginLayoutParams);
        }

        @Override
        protected void setBaseAttributes(TypedArray typedArray, int widthAttr, int heightAttr) {

            if (typedArray.hasValue(widthAttr)) {
                width = typedArray.getLayoutDimension(widthAttr, "layout_width");
            } else {
                width = WRAP_CONTENT;
            }

            if (typedArray.hasValue(heightAttr)) {
                height = typedArray.getLayoutDimension(heightAttr, "layout_height");
            } else {
                height = WRAP_CONTENT;
            }
        }
    }

    public interface OnCheckedChangeListener {
        void onCheckedChanged(CustomRadioGroup group, int checkedId);
    }

    private class CheckedStateTracker implements CompoundButton.OnCheckedChangeListener {
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (change) {
                return;
            }

            change = true;
            if (checkedID != -1) {
                setCheckedStateForView(checkedID, false);
            }
            change = false;

            int id = buttonView.getId();
            setCheckedId(id);
        }
    }

    private class PassThroughHierarchyChangeListener implements ViewGroup.OnHierarchyChangeListener {
        private ViewGroup.OnHierarchyChangeListener hierarchyChangeListener;

        @SuppressLint("NewApi")
        public void onChildViewAdded(View parent, View child) {
            if (parent == CustomRadioGroup.this) {
                List<RadioButton> radioButtons = getAllRadioButton(child);
                if (radioButtons != null && radioButtons.size() > 0) {
                    for (RadioButton radioButton : radioButtons) {
                        int id = radioButton.getId();
                        if (id == View.NO_ID && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                            id = View.generateViewId();
                            radioButton.setId(id);
                        }
                        radioButton.setOnCheckedChangeListener(
                                onCheckedChangeListener);
                    }
                }
            }

            if (hierarchyChangeListener != null) {
                hierarchyChangeListener.onChildViewAdded(parent, child);
            }
        }

        public void onChildViewRemoved(View parent, View child) {
            if (parent == CustomRadioGroup.this) {
                List<RadioButton> radioButtons = getAllRadioButton(child);
                if (radioButtons != null && radioButtons.size() > 0) {
                    for (RadioButton radioButton : radioButtons) {
                        radioButton.setOnCheckedChangeListener(null);
                    }
                }
            }

            if (hierarchyChangeListener != null) {
                hierarchyChangeListener.onChildViewRemoved(parent, child);
            }
        }
    }


}
