package com.quduquxie.bean;

import android.os.Bundle;

public final class ViewPageInfo {

    public final String title;
    public final String tag;
    public final Class<?> className;
    public final Bundle bundle;

    public ViewPageInfo(String title, String tag, Class<?> className, Bundle bundle) {
    	this.title = title;
        this.tag = tag;
        this.className = className;
        this.bundle = bundle;
    }
}