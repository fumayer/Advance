package com.quduquxie.base.manager;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import com.quduquxie.creation.modify.view.LiteratureSectionModifyActivity;
import com.quduquxie.creation.revise.view.LiteratureReviseActivity;
import com.quduquxie.function.creation.create.view.LiteratureCreateActivity;
import com.quduquxie.function.creation.draft.view.DraftListActivity;
import com.quduquxie.function.creation.literature.view.LiteratureListActivity;
import com.quduquxie.function.creation.section.view.SectionListActivity;
import com.quduquxie.function.creation.write.view.SectionWriteActivity;
import com.quduquxie.module.read.reading.view.ReadingActivity;
import com.quduquxie.revise.view.ReviseUserActivity;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Created on 17/7/12.
 * Created by crazylei.
 */

public class ActivityStackManager {

    private static Stack<Activity> activityStack;

    private static ActivityStackManager activityStackManager;

    private ActivityStackManager() {

    }

    public synchronized static ActivityStackManager getActivityStackManager() {

        if (null == activityStackManager) {
            activityStackManager = new ActivityStackManager();
        }
        return activityStackManager;
    }

    /**
     * 添加Activity到堆栈
     */
    public synchronized void insertActivity(Activity activity) {
        if (activityStack == null) {
            activityStack  = new Stack<>();
        }
        activityStack.add(activity);
    }

    /**
     * 获取当前Activity
     */
    public Activity currentActivity() {
        return activityStack.lastElement();
    }

    /**
     * 结束当前Activity
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class activityClass) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(activityClass)) {
                finishActivity(activity);
                break;
            }
        }
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
        }
    }

    /**
     * 退出应用程序
     */
    public void exitApplication(Context context) {
        try {
            finishAllActivity();
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            activityManager.killBackgroundProcesses(context.getPackageName());
            System.exit(0);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * 结束所有Activity
     */
    private void finishAllActivity() {
        for (int i = 0; i < activityStack.size(); i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    public void removeActivity(Activity activity) {
        if (activityStack.contains(activity)) {
            activityStack.remove(activity);
        }
    }

    public void exitLogin() {
        ArrayList<Integer> index = new ArrayList<>();
        for (int i  = activityStack.size() - 1; i > -1; i--) {
            Activity activity = activityStack.get(i);
            if (activity instanceof LiteratureListActivity || activity instanceof SectionListActivity || activity instanceof DraftListActivity || activity instanceof LiteratureCreateActivity || activity instanceof SectionWriteActivity
                    || activity instanceof LiteratureReviseActivity || activity instanceof LiteratureSectionModifyActivity || activity instanceof ReviseUserActivity) {

                index.add(i);
            }
        }
        for (Integer integer : index) {
            activityStack.get(integer).finish();
        }
    }

    public void startCoverActivity() {
        int index = activityStack.size() - 1;
        for (int i  = activityStack.size() - 1; i > -1; i--) {
            Activity activity = activityStack.get(i);

            if (activity instanceof ReadingActivity) {
                index = i;
                return;
            }
        }
        for (int i = index; i < activityStack.size(); i++) {
            activityStack.get(i).finish();
        }
    }

    public void getActivity() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Activity activity : activityStack) {
            if (stringBuilder.length() != 0) {
                stringBuilder.append("\n");
            }
            stringBuilder.append(activity.getClass().getSimpleName());
        }
    }

    public int getActivities() {
        return activityStack.size();
    }
}