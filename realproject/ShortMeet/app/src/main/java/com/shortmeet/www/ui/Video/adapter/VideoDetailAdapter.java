package com.shortmeet.www.ui.Video.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shortmeet.www.R;
import com.shortmeet.www.entity.video.GetCommenListEntity;
import com.shortmeet.www.utilsUsed.GlideLoader;

import java.util.List;

/**
 * Created by Fenglingyue on 2017/9/21.
 */

public class VideoDetailAdapter extends BaseQuickAdapter<GetCommenListEntity.DataEntity.ListEntity,BaseViewHolder>{
    public VideoDetailAdapter(@LayoutRes int layoutResId, @Nullable List<GetCommenListEntity.DataEntity.ListEntity> data) {
        super(layoutResId, data);
    }
    @Override
    protected void convert(BaseViewHolder helper, GetCommenListEntity.DataEntity.ListEntity item) {
        //评论者头像
       GlideLoader.getInstance().loadCircleImg(mContext,item.getImg(),R.drawable.p1, (ImageView) helper.getView(R.id.imgv_commen_head_video_detail));
        //评论者昵称
        helper.setText(R.id.tv_commen_nickmame_videodetail,item.getNickname());
        //时间
        helper.setText(R.id.tv_xtianqian,item.getComment_time());
        //评论内容
        helper.setText(R.id.tv_commenspeak_videodetail,item.getC_content());
        LinearLayout view=helper.getView(R.id.linear_commen_yuanspeak_videodetail);
        if(item.isType()){
            //说原话的人名字
            helper.setText(R.id.tv_commen_speakauthor,item.getComment_info().getP_nickname()+": ");
            //说原话的人说的话
            helper.setText(R.id.tv_commen_yuanspeak_videodetail,item.getComment_info().getP_content());
            view.setVisibility(View.VISIBLE);
        }else{
            view.setVisibility(View.GONE);
        }
    }
}
