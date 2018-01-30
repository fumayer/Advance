package com.aiwue.ui.adapter;

import android.text.TextUtils;
import android.widget.ImageView;

import com.aiwue.R;
import com.aiwue.base.AiwueConfig;
import com.aiwue.model.Article;
import com.aiwue.theme.colorUi.util.ColorUiUtil;
import com.aiwue.utils.ConstantValue;
import com.aiwue.utils.DateUtils;
import com.aiwue.utils.ImageLoaderUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 主页中的文章列表页面的adapter
 * Created by Yibao on 2017年4月19日12:53:31
 * Copyright (c) 2017 aiwue.com All rights reserved
 */
public class MyCollectionArticleListAdapter extends BaseQuickAdapter<Article, BaseViewHolder> {
    public MyCollectionArticleListAdapter(List<Article> data) {
        super(R.layout.item_news, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Article article) {
        //防止复用View没有改变主题，重新设置
        ColorUiUtil.changeTheme(baseViewHolder.convertView, mContext.getTheme());
        setGone(baseViewHolder);
        if (article.getType() == ConstantValue.ARTICLE_TYPE_NORMAL) {
            //文章类型
            if (!TextUtils.isEmpty(article.getHeadPicName())) {
                //单图片文章
                ImageLoaderUtils.displayImage(AiwueConfig.AIWUE_API_PIC_URL + article.getHeadPicName(), (ImageView) baseViewHolder.getView(R.id.ivRightImg1));
                baseViewHolder.setVisible(R.id.rlRightImg, true)
                        .setVisible(R.id.viewFill, true);
            }
//            else {
//                //3张图片
//                baseViewHolder.setVisible(R.id.llCenterImg, true);
//                try {
//                    ImageLoaderUtils.displayImage(article.image_list.get(0).url, (ImageView) baseViewHolder.getView(R.id.ivCenterImg1));
//                    ImageLoaderUtils.displayImage(article.image_list.get(1).url, (ImageView) baseViewHolder.getView(R.id.ivCenterImg2));
//                    ImageLoaderUtils.displayImage(article.image_list.get(2).url, (ImageView) baseViewHolder.getView(R.id.ivCenterImg3));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            }
        } else if (article.getType() == ConstantValue.ARTICLE_TYPE_IMG) {
            //画廊类型
            //if (article.image_list == null) {
                ImageLoaderUtils.displayImage(AiwueConfig.AIWUE_API_PIC_URL + article.getHeadPicName(), (ImageView) baseViewHolder.getView(R.id.ivRightImg1));
                baseViewHolder.setVisible(R.id.rlRightImg, true)
                        .setVisible(R.id.viewFill, true);
//            } else {
//                ImageLoaderUtils.displayImage(article.image_list.get(0).url, (ImageView) baseViewHolder.getView(R.id.ivBigImg));
//                baseViewHolder.setVisible(R.id.rlBigImg, true)
//                        .setText(R.id.tvImgCount, article.image_list.size() + "图");
//            }


        } else if (article.getType() == ConstantValue.ARTICLE_TYPE_VIDEO) {
            //视频类型
            ImageLoaderUtils.displayImage(AiwueConfig.AIWUE_API_PIC_URL + article.getHeadPicName(), (ImageView) baseViewHolder.getView(R.id.ivRightImg1));
            baseViewHolder.setVisible(R.id.rlRightImg, true)
                    .setVisible(R.id.viewFill, true)
                    .setVisible(R.id.llVideo, true);
                    //.setText(R.id.tvDuration, article.video_duration_str);
        }
        baseViewHolder.setText(R.id.tvTitle, article.getTitle())
                .setText(R.id.tvAuthorName, article.getAuthor())
                .setText(R.id.tvCommentCount, article.getCommentNum() + "评论")
                .setText(R.id.tvTime, DateUtils.getShortTime(article.getVisibleTime() * 1000));
    }

    private void setGone(BaseViewHolder baseViewHolder) {
        baseViewHolder.setVisible(R.id.viewFill, false)
                .setVisible(R.id.llCenterImg, false)
                .setVisible(R.id.rlBigImg, false)
                .setVisible(R.id.llVideo, false)
                .setVisible(R.id.rlRightImg, false);

    }
}
