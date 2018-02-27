package com.aiwue.ui.activity;


import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwue.R;
import com.aiwue.iview.IStatusDetailView;
import com.aiwue.model.Note;
import com.aiwue.presenter.StatusDetailPresenter;
import com.aiwue.utils.ConstantValue;

/**
 *  动态详情页面
 * Created by Chenhui on 2017年4月20日
 * Copyright (c) 2017 aiwue.com All rights reserved
 */

public class StatusDetailActivity extends BaseContentActivity<StatusDetailPresenter> implements IStatusDetailView {
    //共用数据列表
    private Note mNote = null;
    private ImageView imageAvatar;
    private ImageView imageGendar;
    private TextView txtNickName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        contentId = (Integer) getIntent().getSerializableExtra("noteId");
        contentType = ConstantValue.CONTENT_TYPE_NOTE;
        shareContent = this.getResources().getString(R.string.status_share_title);
        //导入布局
        headerView = View.inflate(this, R.layout.item_status_details_header, null);
        //给布局设置宽高属性
        headerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        imageAvatar = (ImageView)headerView.findViewById(R.id.img_avatar_dynamic);


        super.processLogic(savedInstanceState);

        //mvpPresenter.getStatusDetail(contentId);
    }

     private void ShowStatusHeader(Note note) {
         //ImageLoaderUtils.displayAvatar(AiwueConfig.AIWUE_API_PIC_URL, imageAvatar);

     }
    @Override
    protected StatusDetailPresenter createPresenter() {
        return new StatusDetailPresenter(this);
    }

    @Override
    public void onGetStatusDetailSuccess(Boolean success, String err, Note response) {

//        if(success) {
//            mNote = response;
//            ShowStatusHeader(response);
//            if (response.getCommentNum() > 0) {
//                actionCommentCount.setVisibility(View.VISIBLE);
//                actionCommentCount.setText(response.getCommentNum() + "");
//            }
//            pId = mArticle.getpId();
//            shareTitle = mArticle.getTitle();
//            shareImgName = mArticle.getHeadPicName();
//
//
//            refreshCommentList();//开始加载评论列表
//        }else{
//            showToast(err);
//        }
    }
}
