package com.quduquxie.read.page;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.KeyEvent;

import com.quduquxie.read.IReadDataFactory;
import com.quduquxie.read.NovelHelper;
import com.quduquxie.read.ReadStatus;


public interface PageInterface {

    void init(Activity activity, ReadStatus readStatus, NovelHelper novelHelper);

    void freshTime(CharSequence time);

    void drawNextPage();

    void drawCurrentPage();

    void setTextColor(int color);

    void setBackground();

    void setPageBackColor(int color);

    void refreshCurrentPage();

    void tryTurnPrePage();

    void onAnimationFinish();

    void setCallBack(CallBack callBack);

    void getChapter(boolean needSavePage);

    void getPreChapter();

    void getNextChapter();

    void setReadFactory(IReadDataFactory factory);

    void setTypeFace(Typeface typeFace);

    void recyclerData();

    boolean setKeyEvent(KeyEvent event);
}
