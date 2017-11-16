package com.shortmeet.www.ui.Home.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.util.MultiTypeDelegate;
import com.shortmeet.www.R;
import com.shortmeet.www.entity.home.KindRecomanVideoEntity;
import com.shortmeet.www.utilsUsed.GlideLoader;
import com.shortmeet.www.utilsUsed.LogUtils;
import com.shortmeet.www.views.widgetPart.GoodManVideoPlayerPriview;

import cn.jzvd.JZVideoPlayerStandard;

/**
 * Created by Fenglingyue on 2017/9/20.
 */

public class RecomandVideoAdapter extends BaseQuickAdapter<KindRecomanVideoEntity,BaseViewHolder> {
    public RecomandVideoAdapter() {
        super(null);
        //Step.1
        setMultiTypeDelegate(new MultiTypeDelegate<KindRecomanVideoEntity>() {
            @Override
            protected int getItemType(KindRecomanVideoEntity entity) {
                //根据你的实体类来判断布局类型   1横向     2 纵向
                return entity.getType();
            }
        });
        //Step.2
        getMultiTypeDelegate()
                .registerItemType(KindRecomanVideoEntity.HORIZANTAL_ONE, R.layout.item_recycler_recomand_one)//横向1个
                .registerItemType(KindRecomanVideoEntity.VERTICAL_TWO, R.layout.item_recycler_recomand_hroizantaltwo);//横向2个
    }


    @Override
    protected void convert(BaseViewHolder helper,KindRecomanVideoEntity entity) {
      //Step.3
        switch (helper.getItemViewType()) {
            case KindRecomanVideoEntity.HORIZANTAL_ONE:  //横向1个视频
                // do something
                if(entity.getRecomanVideoEntities()==null){
                    LogUtils.e("RecomandVideoAdapter  KindRecomanVideoEntity.HORIZANTAL_ONE","entity.getRecomanVideoEntities()==null");
                    return;
                }
                if(entity.getRecomanVideoEntities().size()==1){
              //标题
                helper.setText(R.id.tv_tile_recomand,entity.getRecomanVideoEntities().get(0).getTitle());
              //tag  标签
                helper.setText(R.id.tv_tag_recomandfrg,entity.getRecomanVideoEntities().get(0).getTag());
                }
                //透明摸监听
                helper.addOnClickListener(R.id.imgv_video_item_recommend_video);
                //头像监听
                helper.addOnClickListener(R.id.linear_head_recomandfrg);

                String coverurl=entity.getRecomanVideoEntities().get(0).getCover_url();
                String playurl=entity.getRecomanVideoEntities().get(0).getPlay_url();
                GoodManVideoPlayerPriview goodManVideoPlayerPriview=helper.getView(R.id.videoplayer_item_recommend_video);
                initVideoPalyer(goodManVideoPlayerPriview,playurl,coverurl);
                break;
            case KindRecomanVideoEntity.VERTICAL_TWO: //横向2个封面
                // do something
                if(entity.getRecomanVideoEntities()==null){
                    LogUtils.e("RecomandVideoAdapter  KindRecomanVideoEntity.VERTICAL_TWO","entity.getRecomanVideoEntities()==null");
                    return;
                }
                int size=entity.getRecomanVideoEntities().size();
                if(size==1){
                    //封面
                    GlideLoader.getInstance().loadImgPlaceAndErrSame(mContext,entity.getRecomanVideoEntities().get(0).getCover_url(), R.mipmap.ic_launcher, (ImageView) helper.getView(R.id.imgv1_video_item_recommend_video));
                    //标题
                    helper.setText(R.id.tv1_tile_recomand,entity.getRecomanVideoEntities().get(0).getTitle());
                    //tag  标签
                    helper.setText(R.id.tv1_tag_recomandfrg,entity.getRecomanVideoEntities().get(0).getTag());
                    //封面监听
                    helper.addOnClickListener(R.id.imgv1_video_item_recommend_video);
                    //头像监听
                    helper.addOnClickListener(R.id.linear_head1_recomandfrag);
                }else if(size==2){
                    //1封面
                    GlideLoader.getInstance().loadImgPlaceAndErrSame(mContext,entity.getRecomanVideoEntities().get(0).getCover_url(), R.mipmap.ic_launcher, (ImageView) helper.getView(R.id.imgv1_video_item_recommend_video));
                    //1标题
                    helper.setText(R.id.tv1_tile_recomand,entity.getRecomanVideoEntities().get(0).getTitle());
                    //tag  1标签
                    helper.setText(R.id.tv1_tag_recomandfrg,entity.getRecomanVideoEntities().get(0).getTag());
                    //封面1监听
                    helper.addOnClickListener(R.id.imgv1_video_item_recommend_video);
                    //头像监听
                    helper.addOnClickListener(R.id.linear_head1_recomandfrag);


                   RelativeLayout  relativeLayout= helper.getView(R.id.relative2_recomandfrg);
                   relativeLayout.setVisibility(View.VISIBLE);
                    //2封面
                    GlideLoader.getInstance().loadImgPlaceAndErrSame(mContext,entity.getRecomanVideoEntities().get(1).getCover_url(), R.mipmap.ic_launcher, (ImageView) helper.getView(R.id.imgv2_video_item_recommend_video));
                    //2标题
                    helper.setText(R.id.tv2_tile_recomand,entity.getRecomanVideoEntities().get(1).getTitle());
                    //tag 2标签
                    helper.setText(R.id.tv2_tag_recomandfrg,entity.getRecomanVideoEntities().get(1).getTag());
                    //封面2监听
                    helper.addOnClickListener(R.id.imgv2_video_item_recommend_video);
                    //头像监听
                    helper.addOnClickListener(R.id.linear_head2_recomandfrag);
                }
                break;
        }
    }
    /*
     *  Fly 注：1横屏的palyer
     */
    boolean a=false;
    public void initVideoPalyer(GoodManVideoPlayerPriview playerVideoPlayer,String playurl,String coverurl){

        playerVideoPlayer.backButton.setVisibility(View.GONE);
        playerVideoPlayer.tinyBackImageView.setVisibility(View.GONE);
        playerVideoPlayer.widthRatio = 2;
        playerVideoPlayer.heightRatio = 1;
        playerVideoPlayer.setSilencePattern(true);
        playerVideoPlayer.setUp(playurl, JZVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "");
        if (!TextUtils.isEmpty(coverurl)) {
            GlideLoader.getInstance().loadImgPlaceAndErrSame(mContext,coverurl,R.drawable.aaaa,playerVideoPlayer.thumbImageView);
        } else {
            Glide.with(mContext).load(R.drawable.p1).centerCrop().crossFade().into(playerVideoPlayer.thumbImageView);
        }
    }

}
