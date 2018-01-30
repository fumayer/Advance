package com.aiwue.ui.adapter.hotspotadapter;

import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.aiwue.R;
import com.aiwue.base.AiwueConfig;
import com.aiwue.model.Banner;
import com.aiwue.theme.colorUi.util.ColorUiUtil;
import com.aiwue.utils.ImageLoaderUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/4/28.
 */

public class BannerListAdapter extends BaseQuickAdapter<Banner,BaseViewHolder>{


    private BaseViewHolder gone;

    public BannerListAdapter(List<Banner> data) {
        super(R.layout.item_banner,data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Banner banner) {
        ColorUiUtil.changeTheme(baseViewHolder.convertView, mContext.getTheme());
        setGone(baseViewHolder);
        if (banner.getPicName()!=null) {
            ImageLoaderUtils.displayImage(AiwueConfig.AIWUE_API_PIC_URL + banner.getPicName(), (ImageView) baseViewHolder.getView(R.id.banner_hotspot_fragment));
            System.out.println("BannerListAdapter.convert");
        }


    }

    private void setGone(BaseViewHolder baseViewHolder) {
        baseViewHolder.setVisible(R.id.imgv_banner, false);
    }
}
