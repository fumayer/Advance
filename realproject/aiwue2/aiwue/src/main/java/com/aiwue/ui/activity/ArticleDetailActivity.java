package com.aiwue.ui.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;

import com.aiwue.R;
import com.aiwue.base.AiwueConfig;
import com.aiwue.iview.IArticleDetailView;
import com.aiwue.model.Article;
import com.aiwue.presenter.ArticleDetailPresenter;
import com.aiwue.ui.view.ProgressWebView;
import com.aiwue.utils.ConstantValue;

import timber.log.Timber;

/**
 *  文章详情页面
 * Created by Yibao on 2017年4月12日14:36:55
 * Copyright (c) 2017 aiwue.com All rights reserved
 */
public class ArticleDetailActivity extends BaseContentActivity<ArticleDetailPresenter> implements IArticleDetailView {
    Article mArticle = null;
    private ProgressWebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    protected void processLogic(Bundle savedInstanceState) {
        contentId = (Integer) getIntent().getSerializableExtra("articleId");
        contentType = ConstantValue.CONTENT_TYPE_ARTICLE;
        shareContent = this.getResources().getString(R.string.article_share_title);

       setTitle("");
        headerView = View.inflate(this, R.layout.layout_webview, null);
        headerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        webView = (ProgressWebView) headerView.findViewById(R.id.web);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);


        String ua = webView.getSettings().getUserAgentString();
        ua = ua + "; aiwue/"+getVersion();
        webView.getSettings().setUserAgentString(ua);

        if (Build.VERSION.SDK_INT >= 21)
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        webView.setOnHtmlEventListener(new ProgressWebView.OnHtmlEventListener() {
            @Override
            public void onFinished(String html) {
                Timber.i(html);
            }

            @Override
            public void onUriLoading(Uri uri) {
                onUriLoad(uri);
            }
        });
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.setFocusable(true);
        webView.setFocusableInTouchMode(true);
        //webView.setWebViewClient(mWebViewClient);

        super.processLogic(savedInstanceState);
        mvpPresenter.getArticleDetail(contentId);
    }

    protected void onUriLoad(Uri uri) {
    }

    @Override
    protected ArticleDetailPresenter createPresenter() {
        return new ArticleDetailPresenter(this);
    }



    @Override
    public void onGetArticleDetailSuccess(Boolean success, String err,Article response) {
          if(success) {
             mArticle = response;
              String url = AiwueConfig.AIWUE_ARTICLE_URL.concat(mArticle.getpId());
              webView.loadUrl("http://www.baidu.com");
              if (response.getCommentNum() > 0) {
                  actionCommentCount.setVisibility(View.VISIBLE);
                  actionCommentCount.setText(response.getCommentNum() + "");
              }
              pId = mArticle.getpId();
              shareTitle = mArticle.getTitle();
              shareImgName = mArticle.getHeadPicName();
              setTitle(mArticle.getAuthor());

              refreshCommentList();//开始加载评论列表
          }else{
            showToast(err);
        }
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public String getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
