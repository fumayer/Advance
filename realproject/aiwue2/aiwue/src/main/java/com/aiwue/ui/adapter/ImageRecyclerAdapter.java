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
 *  图片列表适配器
 * Created by Jelly on 2016/9/3.
 */
public class ImageRecyclerAdapter extends BaseQuickAdapter<Note,BaseViewHolder> {
private ImageView image_albumdetile;
public ImageRecyclerAdapter(List<Note> data) {super(R.layout.item_image, data);}
@Override
protected void convert(BaseViewHolder baseViewHolder, final Note note) {
        ColorUiUtil.changeTheme(baseViewHolder.convertView, mContext.getTheme());
        // 找到图片的Id

        image_albumdetile = baseViewHolder.getView(R.id.image_albumdetile);
        ///设置作者头像
        ImageLoaderUtils.displayAvatar(AiwueConfig.AIWUE_API_PIC_URL + note.getAuthorInfo().getHeadPicName(), (ImageView) baseViewHolder.getView(R.id.img_avatar_album_item));
        //给图集赋值
        ImageLoaderUtils.displayImage(AiwueConfig.AIWUE_API_PIC_URL + note.getHeadPicName(), image_albumdetile);
        //得到Note段落list
        List<NoteParagraph> np = note.getPgraphList();
        if (np == null || np.size() == 0) {
        //设置图集的图片
        ImageLoaderUtils.displayImage(AiwueConfig.AIWUE_API_PIC_URL + note.getHeadPicName(), image_albumdetile);
        }
    image_albumdetile.setScaleType(ImageView.ScaleType.CENTER_CROP);
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
