package com.shortmeet.www.ui.PercenalCenter.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.util.MultiTypeDelegate;
import com.shortmeet.www.R;
import com.shortmeet.www.entity.percenalCenter.MyWorkEntity;

import java.util.List;

/**
 * Created by Fnglingyue on 2017/9/22.  其他用户中心
 */
//list<video> mlist
public class OtherPeopleWorkfragRecyAdapter<T>extends BaseQuickAdapter<T,BaseViewHolder>{
    private   int  mkind;
    public void setMkind(int mkind) {
        this.mkind = mkind;
    }
    //作品
    private List<MyWorkEntity.DataEntity.VideoEntity.VideosEntity>mVideoEntityList;
    public List<MyWorkEntity.DataEntity.VideoEntity.VideosEntity> getVideoEntityList() {
    return mVideoEntityList;
    }
    public void setVideoEntityList(List<MyWorkEntity.DataEntity.VideoEntity.VideosEntity> videoEntityList) {
        mVideoEntityList = videoEntityList;
    }
   //点赞作品
   private List<MyWorkEntity.DataEntity.VideoEntity.VideosEntity>mList;
    public List<MyWorkEntity.DataEntity.VideoEntity.VideosEntity> getZanList() {
        return mList;
    }
    public void setZanVideoEntityList(List<MyWorkEntity.DataEntity.VideoEntity.VideosEntity> setZanList) {
        this.mList = setZanList;
    }
    //草稿
    private List<String>msts;
    public List<String> getDrafts() {
        return msts;
    }
    public void setDrafts(List<String>sts) {
        msts = sts;
    }



    public OtherPeopleWorkfragRecyAdapter() {
        super(null);
        //Step.1
        setMultiTypeDelegate(new MultiTypeDelegate<T>() {
            @Override
            protected int getItemType(T entity) {
               return mkind;
            }
        });

        //Step.2
        getMultiTypeDelegate()
                .registerItemType(MyWorkEntity.DataEntity.VideoEntity.VideosEntity.WORK, R.layout.item_recycler_threehorizantal_minefrag)//作品
                .registerItemType(MyWorkEntity.DataEntity.VideoEntity.VideosEntity.DRAFT, R.layout.item_recycler_mydraft_minefrag);//点赞

    }

    @Override
    protected void convert(BaseViewHolder helper, T item) {
   /*     switch (mkind) {
            case 0:
                int  size= getVideoEntityList().size();
                if(size==1){
                    GlideLoader.getInstance().loadImgPlaceAndErrSame(mContext,getVideoEntityList().get(0).getCover_url(), R.mipmap.ic_launcher, (ImageView) helper.getView(R.id.imgv1_video_minefrag));
                    helper.getView(R.id.relav2_minefrag).setVisibility(View.GONE);
                    helper.getView(R.id.relav3_minefrag).setVisibility(View.GONE);
                }else if(size==2){
                    GlideLoader.getInstance().loadImgPlaceAndErrSame(mContext,getVideoEntityList().get(0).getCover_url(), R.mipmap.ic_launcher, (ImageView) helper.getView(R.id.imgv1_video_minefrag));
                    GlideLoader.getInstance().loadImgPlaceAndErrSame(mContext,getVideoEntityList().get(1).getCover_url(), R.mipmap.ic_launcher, (ImageView) helper.getView(R.id.imgv2_video_minefrag));
                    helper.getView(R.id.relav3_minefrag).setVisibility(View.GONE);
                }else if(size==3){
                    GlideLoader.getInstance().loadImgPlaceAndErrSame(mContext,getVideoEntityList().get(0).getCover_url(), R.mipmap.ic_launcher, (ImageView) helper.getView(R.id.imgv1_video_minefrag));
                    GlideLoader.getInstance().loadImgPlaceAndErrSame(mContext,getVideoEntityList().get(1).getCover_url(), R.mipmap.ic_launcher, (ImageView) helper.getView(R.id.imgv2_video_minefrag));
                    GlideLoader.getInstance().loadImgPlaceAndErrSame(mContext,getVideoEntityList().get(2).getCover_url(), R.mipmap.ic_launcher, (ImageView) helper.getView(R.id.imgv3_video_minefrag));
                }
                break;
            case 1:
   //              int  sizeb= getZanList().size();
//                if(sizeb==1){
//                    GlideLoader.getInstance().loadImgPlaceAndErrSame(mContext,getZanList().get(0).getCover_url(), R.mipmap.ic_launcher, (ImageView) helper.getView(R.id.imgv1_video_minefrag));
//                    helper.getView(R.id.relav2_minefrag).setVisibility(View.GONE);
//                    helper.getView(R.id.relav3_minefrag).setVisibility(View.GONE);
//                }else if(sizeb==2){
//                    GlideLoader.getInstance().loadImgPlaceAndErrSame(mContext,getZanList().get(0).getCover_url(), R.mipmap.ic_launcher, (ImageView) helper.getView(R.id.imgv1_video_minefrag));
//                    GlideLoader.getInstance().loadImgPlaceAndErrSame(mContext,getZanList().get(1).getCover_url(), R.mipmap.ic_launcher, (ImageView) helper.getView(R.id.imgv2_video_minefrag));
//                    helper.getView(R.id.relav3_minefrag).setVisibility(View.GONE);
//                }else if(sizeb==3){
//                    GlideLoader.getInstance().loadImgPlaceAndErrSame(mContext,getZanList().get(0).getCover_url(), R.mipmap.ic_launcher, (ImageView) helper.getView(R.id.imgv1_video_minefrag));
//                    GlideLoader.getInstance().loadImgPlaceAndErrSame(mContext,getZanList().get(1).getCover_url(), R.mipmap.ic_launcher, (ImageView) helper.getView(R.id.imgv2_video_minefrag));
//                    GlideLoader.getInstance().loadImgPlaceAndErrSame(mContext,getZanList().get(2).getCover_url(), R.mipmap.ic_launcher, (ImageView) helper.getView(R.id.imgv3_video_minefrag));
//                }
                break;
            case 2:

                break;
        }*/
    }
}
