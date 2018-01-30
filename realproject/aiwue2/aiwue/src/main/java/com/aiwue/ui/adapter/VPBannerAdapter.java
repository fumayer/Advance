package com.aiwue.ui.adapter;

import android.widget.ImageView;

import com.aiwue.R;
import com.aiwue.base.AiwueConfig;
import com.aiwue.model.Banner;
import com.aiwue.model.Note;
import com.aiwue.model.NoteParagraph;
import com.aiwue.theme.colorUi.util.ColorUiUtil;
import com.aiwue.ui.view.EasyJCVideoPlayer;
import com.aiwue.utils.ImageLoaderUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
/**
 * 主页中的热点页面的banneradapter
 * Created by Chenhui on 2017年4月27日18:35:31
 * Copyright (c) 2017 aiwue.com All rights reserved
 */
class BannerListAdapter extends BaseQuickAdapter<Banner,BaseViewHolder> {
    public BannerListAdapter(List<Banner> data) {
        super(R.layout.item_video_riverlake, data);
    }
    @Override
    protected void convert(BaseViewHolder baseViewHolder, final Banner banner) {
//        ColorUiUtil.changeTheme(baseViewHolder.convertView, mContext.getTheme());
//        //得到视频播放的id
//        final EasyJCVideoPlayer videoPlayer = baseViewHolder.getView(R.id.videoPlayer);
//        //三级缓存    网址+用户信息（id，昵称，头像，性别，），头像的imageView
//        ImageLoaderUtils.displayAvatar(AiwueConfig.AIWUE_API_PIC_URL + note.getAuthorInfo().getHeadPicName(), (ImageView) baseViewHolder.getView(R.id.img_avatar_video_item));
//        //网址+题图，视频播放器
//        ImageLoaderUtils.displayImage(AiwueConfig.AIWUE_API_PIC_URL + note.getHeadPicName(), videoPlayer.thumbImageView);
//        //得到一个list(类型，标题，封面，媒体文件名，每天大小，视频宽高比，宽高比，文本内容)
//        List<NoteParagraph> np = note.getPgraphList();
//        //如果这个list是空的话
//        if (np == null || np.size() == 0) {
//            //网址+题图，视频播放器
//            ImageLoaderUtils.displayImage(AiwueConfig.AIWUE_API_PIC_URL + note.getHeadPicName(), videoPlayer.thumbImageView);
//            //给视频设置放映时长
//            videoPlayer.setDurationText("0");
//        }
//        //给控件设置属性
//        videoPlayer.thumbImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        //给适配器设置
//        baseViewHolder
////                .setText(R.id.tvDuration, np.video_duration_str)
//                //作者名字的id，作者昵称
//                .setText(R.id.author, note.getAuthorInfo().getNickName())
//                //评论控件的id,评论数量
//                .setText(R.id.comment_vedio_txt, note.getCommentNum() + "")
//                .setText(R.id.praise_video_txt, note.getPraiseNum() + "")
//                .setText(R.id.title_video_txt, note.getTitle() )
//                .setText(R.id.readNum_video_txt, note.getReadNum()+"次播放" )
//
//
//        ;
//        //设置标题
//        videoPlayer.titleTextView.setText(note.getTitle());
//        //设置时长
//        videoPlayer.setDurationText("0秒");
//        //设置
//        videoPlayer.setUp(AiwueConfig.AIWUE_API_SHORT_VIDEO_URL.concat(np.get(0).getMedia()), JCVideoPlayer.SCREEN_LAYOUT_LIST, note.getTitle());
    }

//    private void setPlayer(JCVideoPlayerStandard videoPlayer, Note note) {
//       videoPlayer.setUp(note.video.main_url, JCVideoPlayer.SCREEN_LAYOUT_LIST, note.title);
//    }
}
