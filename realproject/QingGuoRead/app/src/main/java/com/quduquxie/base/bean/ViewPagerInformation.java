package com.quduquxie.base.bean;

import android.os.Bundle;

/**
 * Created on 17/7/20.
 * Created by crazylei.
 */

public final class ViewPagerInformation {

    public final String title;
    public final String flag;
    public final Class<?> className;
    public final Bundle bundle;

    public ViewPagerInformation(String title, String flag, Class<?> className, Bundle bundle) {
        this.title = title;
        this.flag = flag;
        this.className = className;
        this.bundle = bundle;
    }
}