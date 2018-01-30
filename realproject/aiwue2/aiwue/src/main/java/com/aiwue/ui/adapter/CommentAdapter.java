package com.aiwue.ui.adapter;

import android.widget.ImageView;

import com.aiwue.R;
import com.aiwue.model.Comment;
import com.aiwue.base.AiwueConfig;
import com.aiwue.utils.ImageLoaderUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;


/**
 * Created by Administrator on 2016/11/22 0022.
 */
public class CommentAdapter extends BaseQuickAdapter<Comment,BaseViewHolder> {
    public CommentAdapter(List<Comment> data) {
        super(R.layout.item_comment, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Comment comment) {
        ImageLoaderUtils.displayAvatar(AiwueConfig.AIWUE_API_PIC_URL.concat(comment.getAuthorInfo().getHeadPicName()), (ImageView) baseViewHolder.getView(R.id.ivAvatar));
        baseViewHolder
                .setText(R.id.ss_user, comment.getAuthorInfo().getNickName())
                .setText(R.id.tvLikeCount, comment.getPraiseNum() + "")
                .setText(R.id.content, comment.getContent());
    }
}
