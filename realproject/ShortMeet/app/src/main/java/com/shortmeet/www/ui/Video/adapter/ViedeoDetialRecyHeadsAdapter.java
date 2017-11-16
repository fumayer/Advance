package com.shortmeet.www.ui.Video.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shortmeet.www.R;
import com.shortmeet.www.entity.video.ShowZanHeadsEntity;
import com.shortmeet.www.utilsUsed.GlideLoader;

import java.util.List;

/**
 * Created by Fenglingyue on 2017/9/22.
 */

public class ViedeoDetialRecyHeadsAdapter extends BaseQuickAdapter<ShowZanHeadsEntity.DataEntity.ListEntity,BaseViewHolder>{
    public ViedeoDetialRecyHeadsAdapter(@LayoutRes int layoutResId, @Nullable List<ShowZanHeadsEntity.DataEntity.ListEntity> data) {
        super(layoutResId, data);
    }
    @Override
    protected void convert(BaseViewHolder helper, ShowZanHeadsEntity.DataEntity.ListEntity item) {
     GlideLoader.getInstance().loadCircleImg(mContext,item.getImg(),R.drawable.p1, (ImageView) helper.getView(R.id.imgv_recyitem_head_video_detail));
   }
}
