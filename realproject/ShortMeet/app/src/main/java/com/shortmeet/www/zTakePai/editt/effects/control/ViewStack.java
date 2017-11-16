/*
 * Copyright (C) 2010-2017 Alibaba Group Holding Limited.
 */

package com.shortmeet.www.zTakePai.editt.effects.control;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.shortmeet.www.zTakePai.editt.effectmanager.MoreCaptionActivity;
import com.shortmeet.www.zTakePai.editt.effectmanager.MoreMVActivity;
import com.shortmeet.www.zTakePai.editt.effects.audiomix.AudioMixChooserMediator;
import com.shortmeet.www.zTakePai.editt.effects.caption.CaptionChooserMediator;
import com.shortmeet.www.zTakePai.editt.effects.filter.FilterChooserMediator;
import com.shortmeet.www.zTakePai.editt.effects.imv.ImvChooserMediator;
import com.shortmeet.www.zTakePai.editt.effects.overlay.OverlayChooserMediator;


public class ViewStack {

    private final Context mContext;
    private EditorService mEditorService;
    private OnEffectChangeListener mOnEffectChangeListener;
    private OnDialogButtonClickListener mDialogButtonClickListener;
    private BottomAnimation mBottomAnimation;

    private FilterChooserMediator mFilterChooserMediator;
    private OverlayChooserMediator mOverlayChooserMediator;
    private AudioMixChooserMediator mAudioMixChooserMediator;
    private CaptionChooserMediator mCaptionChooserMediator;
    private ImvChooserMediator mImvChooserMediator;

    public ViewStack(Context context) {
        mContext = context;
    }

    public void setActiveIndex(int value) {

        UIEditorPage index = UIEditorPage.get(value);
        switch (index) {
            case FILTER_EFFECT:
                mFilterChooserMediator = FilterChooserMediator.newInstance();
                mFilterChooserMediator.setmEditorService(mEditorService);
                mFilterChooserMediator.setOnEffectChangeListener(mOnEffectChangeListener);
                mFilterChooserMediator.setBottomAnimation(mBottomAnimation);
                mFilterChooserMediator.show(((FragmentActivity) mContext).getSupportFragmentManager(), "filter");
                break;
            case MV:
                mImvChooserMediator = ImvChooserMediator.newInstance();
                mImvChooserMediator.setmEditorService(mEditorService);
                mImvChooserMediator.setOnEffectChangeListener(mOnEffectChangeListener);
                mImvChooserMediator.setBottomAnimation(mBottomAnimation);
                mImvChooserMediator.show(((FragmentActivity) mContext).getSupportFragmentManager(), "imv");
                break;
            case OVERLAY:
                mOverlayChooserMediator = OverlayChooserMediator.newInstance();
                mOverlayChooserMediator.setmEditorService(mEditorService);
                mOverlayChooserMediator.setBottomAnimation(mBottomAnimation);
                mOverlayChooserMediator.setOnEffectChangeListener(mOnEffectChangeListener);
                mOverlayChooserMediator.setDialogButtonClickListener(mDialogButtonClickListener);
                mOverlayChooserMediator.show(((FragmentActivity) mContext).getSupportFragmentManager(), "overlay");
                break;
            case CAPTION:
                mCaptionChooserMediator = CaptionChooserMediator.newInstance();
                mCaptionChooserMediator.setmEditorService(mEditorService);
                mCaptionChooserMediator.setBottomAnimation(mBottomAnimation);
                mCaptionChooserMediator.setOnEffectChangeListener(mOnEffectChangeListener);
                mCaptionChooserMediator.setDialogButtonClickListener(mDialogButtonClickListener);
                mCaptionChooserMediator.show(((FragmentActivity) mContext).getSupportFragmentManager(), "caption");
                break;
            case AUDIO_MIX:
                mAudioMixChooserMediator = AudioMixChooserMediator.newInstance();
                mAudioMixChooserMediator.setBottomAnimation(mBottomAnimation);
                mAudioMixChooserMediator.setmEditorService(mEditorService);
                mAudioMixChooserMediator.setOnEffectChangeListener(mOnEffectChangeListener);
                mAudioMixChooserMediator.show(((FragmentActivity) mContext).getSupportFragmentManager(), "audioMix");
                break;
            case PAINT:
                EffectInfo effectInfo = new EffectInfo();
                effectInfo.type = UIEditorPage.PAINT;
                mOnEffectChangeListener.onEffectChange(effectInfo);
                break;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case BaseChooser.CAPTION_REQUEST_CODE:
                if(mCaptionChooserMediator == null) {
                    break;
                }
                if (resultCode == Activity.RESULT_OK) {
                    int id = data.getIntExtra(MoreCaptionActivity.SELECTED_ID, 0);
                    mCaptionChooserMediator.initResourceLocalWithSelectId(id);
                }else if(resultCode == Activity.RESULT_CANCELED){
                    mCaptionChooserMediator.initResourceLocalWithSelectId(0);
                }
                break;
            case BaseChooser.IMV_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK && mImvChooserMediator != null) {
                    int id = data.getIntExtra(MoreMVActivity.SELECTD_ID, 0);
                    mImvChooserMediator.initResourceLocalWithSelectId(id);
                }
                break;
            case BaseChooser.PASTER_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK && mOverlayChooserMediator != null) {
                    int id = data.getIntExtra(MoreCaptionActivity.SELECTED_ID, 0);
                    mOverlayChooserMediator.setCurrResourceID(id);
                }
                break;
        }
    }

    public void setEditorService(EditorService editorService) {
        mEditorService = editorService;
    }

    public void setEffectChange(OnEffectChangeListener onEffectChangeListener) {
        mOnEffectChangeListener = onEffectChangeListener;
    }

    public void setBottomAnimation(BottomAnimation bottomAnimation) {
        mBottomAnimation = bottomAnimation;
    }

    public void setDialogButtonClickListener(OnDialogButtonClickListener dialogButtonClickListener) {
        mDialogButtonClickListener = dialogButtonClickListener;
    }
}
