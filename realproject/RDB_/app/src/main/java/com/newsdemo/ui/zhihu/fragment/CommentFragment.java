package com.newsdemo.ui.zhihu.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.newsdemo.R;
import com.newsdemo.base.RootFragment;
import com.newsdemo.base.contract.zhihu.CommentConract;
import com.newsdemo.model.bean.CommentBean;
import com.newsdemo.presenter.zhihu.CommentPresent;
import com.newsdemo.ui.zhihu.adapter.CommentAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by jianqiang.hu on 2017/5/15.
 */

public class CommentFragment extends RootFragment<CommentPresent> implements CommentConract.View{

    @BindView(R.id.view_main)
    RecyclerView rvCommentList;

    private List<CommentBean.CommentsBean> mList;

    CommentAdapter mAdapter;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_comment;
    }

    @Override
    protected void initEventAndData() {
        super.initEventAndData();
        Bundle bundle=getArguments();
        stateLoading();
        mPresentter.getCommentData(bundle.getInt("id"),bundle.getInt("kind"));
        rvCommentList.setVisibility(View.INVISIBLE);
        mList=new ArrayList<>();
        mAdapter=new CommentAdapter(mContext,mList);
        rvCommentList.setLayoutManager(new LinearLayoutManager(mContext));
        rvCommentList.setAdapter(mAdapter);
    }

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    public void showContent(CommentBean commentBean) {
        rvCommentList.setVisibility(View.VISIBLE);
        mList.clear();
        mList.addAll(commentBean.getComments());
        mAdapter.notifyDataSetChanged();
    }
}
