package com.shortmeet.www.ui.PercenalCenter.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.aliyun.common.media.ShareableBitmap;
import com.aliyun.qupai.editor.AliyunIThumbnailFetcher;
import com.aliyun.qupai.editor.AliyunThumbnailFetcherFactory;
import com.aliyun.struct.common.ScaleMode;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shortmeet.www.Api.ApiConstant;
import com.shortmeet.www.R;
import com.shortmeet.www.utilsUsed.FileUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Fnglingyue on 2017/9/22.   作品
 */
//list<video> mlist
public class DraftWorkfragRecyAdapter extends BaseQuickAdapter<File,BaseViewHolder>{
    //草稿
    private List<File>msts;
    private  AliyunIThumbnailFetcher thumbnailFetcher;//取帧类
    private SimpleDateFormat  formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm");
    public DraftWorkfragRecyAdapter(@LayoutRes int layoutResId, @Nullable List<File> data) {
        super(layoutResId, data);
    }

    public List<File> getDrafts() {
        return msts;
    }
    public void setDrafts(List<File>sts) {
        msts = sts;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final File item) {
        thumbnailFetcher= AliyunThumbnailFetcherFactory.createThumbnailFetcher();
        thumbnailFetcher.addVideoSource(item.getAbsolutePath());
        thumbnailFetcher.setParameters(60, 60, AliyunIThumbnailFetcher.CropMode.Mediate, ScaleMode.LB, 30);
        long [] time={0};
        thumbnailFetcher.requestThumbnailImage(time, new AliyunIThumbnailFetcher.OnThumbnailCompletion(){
            @Override
            public void onThumbnailReady(ShareableBitmap shareableBitmap, long l) {
                helper.setImageBitmap(R.id.draft_verdio_crop,shareableBitmap.getData());
                FileUtil.saveBitmap(shareableBitmap.getData(), ApiConstant.COMPOSE_PATH_COVER+System.currentTimeMillis()+".png");
            }

            @Override
            public void onError(int i) {

            }
        });
        long modifyTime = item.lastModified();
        helper.setText(R.id.tv_time_draftminfrag, formatter.format(modifyTime));
       helper.addOnClickListener(R.id.ll_mine_draft_editor);
    }

}
