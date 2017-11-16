/*
 * Copyright (C) 2010-2017 Alibaba Group Holding Limited.
 */

package com.shortmeet.www.zTakePai.editt.editor;

import android.graphics.Bitmap;

import com.aliyun.qupai.editor.AliyunPasterController;
import com.shortmeet.www.zTakePai.editt.editor.timeline.TimelineBar;
import com.shortmeet.www.zTakePai.editt.widget.AliyunPasterView;
import com.shortmeet.www.zTakePai.editt.widget.AutoResizingTextView;


public class PasterUITextImpl extends PasterUIGifImpl {

    public PasterUITextImpl(AliyunPasterView pasterView, AliyunPasterController controller, TimelineBar timelineBar){
        this(pasterView, controller, timelineBar, false);
    }

    public PasterUITextImpl(AliyunPasterView pasterView, AliyunPasterController controller, TimelineBar timelineBar, boolean completed) {
        super(pasterView, controller, timelineBar);

        if(mText == null ){
            mText = (AutoResizingTextView) mPasterView.getContentView();
        }

        mText.setVideoSize(controller.getVideoWidth(), controller.getVideoHeight());
        mText.setText(controller.getText());
        mText.setTextOnly(true);
        mText.setFontPath(controller.getPasterTextFont());
        mText.setTextAngle(controller.getPasterTextRotation());
        mText.setTextStrokeColor(controller.getTextStrokeColor());
        mText.setCurrentColor(controller.getTextColor());
        if(completed){
            mText.setTextWidth(controller.getPasterWidth());
            mText.setTextHeight(controller.getPasterHeight());
            mText.setEditCompleted(true);
            pasterView.setEditCompleted(true);
        }else{
            mText.setEditCompleted(false);
            pasterView.setEditCompleted(false);
        }

    }

    @Override
    public void mirrorPaster(boolean mirror) {

    }

    @Override
    protected void playPasterEffect() {

    }

    @Override
    protected void stopPasterEffect() {

    }

    @Override
    public String getText() {
        return mText.getText().toString();
    }

    @Override
    public int getTextColor() {
        return mText.getTextColor();
    }

    @Override
    public String getPasterTextFont() {
        return mText.getFontPath();
    }

    @Override
    public int getTextStrokeColor() {
        return mText.getTextStrokeColor();
    }

    @Override
    public boolean isTextHasStroke() {
        return getTextStrokeColor() == 0;
    }

    @Override
    public boolean isTextHasLabel() {
        return mPasterView.getTextLabel() != null;
    }

    @Override
    public int getTextBgLabelColor() {
        return super.getTextBgLabelColor();
    }

    @Override
    public Bitmap transToImage() {
        return mText.layoutToBitmap();
    }


//    @Override
//    public int getPasterWidth() {
//        return AliYunMathUtils.convertFun(super.getPasterWidth());
//    }
//
//    @Override
//    public int getPasterHeight() {
//        return AliYunMathUtils.convertFun(super.getPasterHeight());
//    }
}
