package com.aiwue.ui.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.aiwue.R;
import com.aiwue.base.AiwueConfig;
import com.aiwue.iview.IAlbumDetailView;
import com.aiwue.iview.IArticleDetailView;
import com.aiwue.model.Article;
import com.aiwue.presenter.AlbumDetailPresenter;
import com.aiwue.presenter.ArticleDetailPresenter;
import com.aiwue.ui.view.ProgressWebView;
import com.aiwue.utils.ConstantValue;

/**
 *  文章详情页面
 * Created by Yibao on 2017年4月12日14:36:55
 * Copyright (c) 2017 aiwue.com All rights reserved
 */
public class AlbumDetailActivity extends BaseContentActivity<AlbumDetailPresenter> implements IAlbumDetailView {

    private RecyclerView rvcycleView;

    /*  Article mArticle = null;
            private ProgressWebView web;*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        contentId = (Integer) getIntent().getSerializableExtra("articleId");
        contentType = ConstantValue.CONTENT_TYPE_ARTICLE;
        shareContent = this.getResources().getString(R.string.article_share_title);

        headerView = View.inflate(this, R.layout.activity_album_detail_item, null);
        headerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        rvcycleView = (RecyclerView) headerView.findViewById(R.id.recyclerView_albumdetail);


    /*    web = (ProgressWebView) headerView.findViewById(R.id.web);
        web.setOnHtmlEventListener(new ProgressWebView.OnHtmlEventListener() {
            @Override
            public void onFinished(String html) {
            }

            @Override
            public void onUriLoading(Uri uri) {
                onUriLoad(uri);
            }
        });*/
        super.processLogic(savedInstanceState);
      //  mvpPresenter.getArticleDetail(contentId);
    }

    protected void onUriLoad(Uri uri) {
    }

    @Override
    protected AlbumDetailPresenter createPresenter() {
        return new AlbumDetailPresenter(this);
    }




 /*    public void onGetArticleDetailSuccess(Boolean success, String err,Article response) {
          if(success) {
              mArticle = response;
              String url = AiwueConfig.ARTICLE_HOST.concat(mArticle.getpId());
              web.loadUrl(url);
              if (response.getCommentNum() > 0) {
                  actionCommentCount.setVisibility(View.VISIBLE);
                  actionCommentCount.setText(response.getCommentNum() + "");
              }
              pId = mArticle.getpId();
              shareTitle = mArticle.getTitle();
              shareImgName = mArticle.getHeadPicName();


              refreshCommentList();//开始加载评论列表
          }else{
            showToast(err);
        }
    }*/

    @Override
    public void onGetAlbumDetailSuccess(Boolean success, String err, Article response) {

    }
}
