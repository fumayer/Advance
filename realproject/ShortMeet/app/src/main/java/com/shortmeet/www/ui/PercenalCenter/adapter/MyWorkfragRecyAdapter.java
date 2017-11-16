package com.shortmeet.www.ui.PercenalCenter.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shortmeet.www.R;
import com.shortmeet.www.entity.percenalCenter.MyWorkEntity;
import com.shortmeet.www.utilsUsed.GlideLoader;
import com.shortmeet.www.utilsUsed.LogUtils;

import java.util.List;

/**
 * Created by Fnglingyue on 2017/9/22.   作品
 */
//list<video> mlist
public class MyWorkfragRecyAdapter extends BaseQuickAdapter<MyWorkEntity.DataEntity.VideoEntity.VideosEntity,BaseViewHolder>{
    public MyWorkfragRecyAdapter(@LayoutRes int layoutResId, @Nullable List<MyWorkEntity.DataEntity.VideoEntity.VideosEntity> data) {
        super(layoutResId, data);
    }
    //作品
    private List<MyWorkEntity.DataEntity.VideoEntity.VideosEntity>mVideoEntityList;
    public List<MyWorkEntity.DataEntity.VideoEntity.VideosEntity> getVideoEntityList() {
        return mVideoEntityList;
    }
    public void setVideoEntityList(List<MyWorkEntity.DataEntity.VideoEntity.VideosEntity> videoEntityList) {
        mVideoEntityList = videoEntityList;
    }
    @Override
    protected void convert(BaseViewHolder helper, MyWorkEntity.DataEntity.VideoEntity.VideosEntity item) {
                if (getVideoEntityList() == null) {
                    return;
                }
                int size = getVideoEntityList().size();
                LogUtils.e("adapter里的size", "adapter里的size===========>" + getVideoEntityList().size());
                GlideLoader.getInstance().loadImgPlaceAndErrSame(mContext, getVideoEntityList().get(0).getCover_url(), R.mipmap.ic_launcher, (ImageView) helper.getView(R.id.imgv1_video_minefrag));
                helper.getView(R.id.card1).setVisibility(View.VISIBLE);
                helper.getView(R.id.relav1_minefrag).setVisibility(View.VISIBLE);


        /*    helper.getView(R.id.relav2_minefrag).setVisibility(View.GONE);
            helper.getView(R.id.relav3_minefrag).setVisibility(View.GONE);*/
              /*  if (size == 1) {
                    GlideLoader.getInstance().loadImgPlaceAndErrSame(mContext, getVideoEntityList().get(0).getCover_url(), R.mipmap.ic_launcher, (ImageView) helper.getView(R.id.imgv1_video_minefrag));
                    helper.getView(R.id.card1).setVisibility(View.VISIBLE);
                    helper.getView(R.id.relav1_minefrag).setVisibility(View.VISIBLE);
                    helper.getView(R.id.relav2_minefrag).setVisibility(View.GONE);
                    helper.getView(R.id.relav3_minefrag).setVisibility(View.GONE);
                } else if (size == 2) {
                    GlideLoader.getInstance().loadImgPlaceAndErrSame(mContext, getVideoEntityList().get(0).getCover_url(), R.mipmap.ic_launcher, (ImageView) helper.getView(R.id.imgv1_video_minefrag));
                    GlideLoader.getInstance().loadImgPlaceAndErrSame(mContext, getVideoEntityList().get(1).getCover_url(), R.mipmap.ic_launcher, (ImageView) helper.getView(R.id.imgv2_video_minefrag));
                    helper.getView(R.id.card1).setVisibility(View.VISIBLE);
                    helper.getView(R.id.card2).setVisibility(View.VISIBLE);
                    helper.getView(R.id.relav1_minefrag).setVisibility(View.VISIBLE);
                    helper.getView(R.id.relav2_minefrag).setVisibility(View.VISIBLE);
                    helper.getView(R.id.relav3_minefrag).setVisibility(View.GONE);
                } else if (size == 3) {
                    GlideLoader.getInstance().loadImgPlaceAndErrSame(mContext, getVideoEntityList().get(0).getCover_url(), R.mipmap.ic_launcher, (ImageView) helper.getView(R.id.imgv1_video_minefrag));
                    GlideLoader.getInstance().loadImgPlaceAndErrSame(mContext, getVideoEntityList().get(1).getCover_url(), R.mipmap.ic_launcher, (ImageView) helper.getView(R.id.imgv2_video_minefrag));
                    GlideLoader.getInstance().loadImgPlaceAndErrSame(mContext, getVideoEntityList().get(2).getCover_url(), R.mipmap.ic_launcher, (ImageView) helper.getView(R.id.imgv3_video_minefrag));
                    helper.getView(R.id.card1).setVisibility(View.VISIBLE);
                    helper.getView(R.id.card2).setVisibility(View.VISIBLE);
                    helper.getView(R.id.card3).setVisibility(View.VISIBLE);
                    helper.getView(R.id.relav1_minefrag).setVisibility(View.VISIBLE);
                    helper.getView(R.id.relav2_minefrag).setVisibility(View.VISIBLE);
                    helper.getView(R.id.relav3_minefrag).setVisibility(View.VISIBLE);
                } else {
                    helper.getView(R.id.relav1_minefrag).setVisibility(View.GONE);
                    helper.getView(R.id.relav2_minefrag).setVisibility(View.GONE);
                    helper.getView(R.id.relav3_minefrag).setVisibility(View.GONE);
                    helper.getView(R.id.card1).setVisibility(View.GONE);
                    helper.getView(R.id.card2).setVisibility(View.GONE);
                    helper.getView(R.id.card3).setVisibility(View.GONE);
                }*/
    }
}
