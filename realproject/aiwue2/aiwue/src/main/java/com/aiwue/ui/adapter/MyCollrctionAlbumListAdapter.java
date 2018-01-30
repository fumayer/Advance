package com.aiwue.ui.adapter;

import android.widget.ImageView;

import com.aiwue.R;
import com.aiwue.base.AiwueConfig;
import com.aiwue.model.Note;
import com.aiwue.model.NoteParagraph;
import com.aiwue.theme.colorUi.util.ColorUiUtil;
import com.aiwue.utils.ImageLoaderUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 主页中的视频列表页面的adapter
 * Created by Yibao on 2017年4月19日12:53:31
 * Copyright (c) 2017 aiwue.com All rights reserved
 */

public class MyCollrctionAlbumListAdapter extends BaseQuickAdapter<Note,BaseViewHolder> {
    private ImageView album_img;
    public MyCollrctionAlbumListAdapter(List<Note> data) {super(R.layout.item_album_riverlake, data);}
    @Override
    protected void convert(BaseViewHolder baseViewHolder, final Note note) {
        ColorUiUtil.changeTheme(baseViewHolder.convertView, mContext.getTheme());
        // 找到图片的Id
        album_img = baseViewHolder.getView(R.id.album_img);
        ///设置作者头像
        ImageLoaderUtils.displayAvatar(AiwueConfig.AIWUE_API_PIC_URL + note.getAuthorInfo().getHeadPicName(), (ImageView) baseViewHolder.getView(R.id.img_avatar_album_item));
        //给图集赋值
        ImageLoaderUtils.displayImage(AiwueConfig.AIWUE_API_PIC_URL + note.getHeadPicName(), album_img);
        //得到Note段落list
        List<NoteParagraph> np = note.getPgraphList();
        if (np == null || np.size() == 0) {
            //设置图集的图片
            ImageLoaderUtils.displayImage(AiwueConfig.AIWUE_API_PIC_URL + note.getHeadPicName(), album_img);
        }
        album_img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        baseViewHolder
                .setText(R.id.title_album_txt, note.getTitle())
                //设置作者名称
                .setText(R.id.author_album, note.getAuthorInfo().getNickName())
                //设置评论
                .setText(R.id.comment_album_item, note.getCommentNum() + "")
                .setText(R.id.praise_album_txt,note.getPraised()+"")
                .setText(R.id.photos_txt,note.getPgraphList().size()+"图");
              //  .setText(R.id.praise_album_txt, note.getPraised() + "");

     /*   album_img.titleTextView.setText(note.getTitle());
        videoPlayer.setDurationText("0秒");

        videoPlayer.setUp(AiwueConfig.SHORT_VIDEO_HOST.concat(np.get(0).getMedia()), JCVideoPlayer.SCREEN_LAYOUT_LIST, note.getTitle());*/
    }

//    private void setPlayer(JCVideoPlayerStandard videoPlayer, Note note) {
//       videoPlayer.setUp(note.video.main_url, JCVideoPlayer.SCREEN_LAYOUT_LIST, note.title);
//    }
}
