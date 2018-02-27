package com.aiwue.ui.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aiwue.R;
import com.aiwue.base.BaseMvpFragment;
import com.aiwue.iview.INoteListView;
import com.aiwue.model.Note;
import com.aiwue.presenter.NoteListPresenter;
import com.aiwue.ui.activity.AlbumDetailActivity;
import com.aiwue.ui.adapter.AlbumListAdapter;
import com.aiwue.utils.ConstantValue;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 主页中的图集列表页面
 * Created by Yibao on 2017年4月19日17:17:15
 * Copyright (c) 2017 aiwue.com All rights reserved
 */

public class AlbumListFragment extends BaseMvpFragment<NoteListPresenter> implements INoteListView {
    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;
    protected List<Note> mDatas = new ArrayList<>();
    protected BaseQuickAdapter mAdapter;

    private  int pIndex = 1;
    private int pSize = 10;
    @Override
    protected NoteListPresenter createPresenter() {
        return new NoteListPresenter(this);
    }

    @Override
    protected View loadViewLayout(LayoutInflater inflater, ViewGroup container) {
        View v = inflater.inflate(R.layout.layout_recyclerview, null);
        ButterKnife.bind(this, v);
        return v;
    }
    @Override
    protected void bindViews(View view) {
    }

    @Override
    protected void processLogic() {
        initCommonRecyclerView(createAdapter(), null);
        //mTitleCode = getArguments().getString(ConstantValue.DATA);
        srl.measure(0, 0);
        srl.setRefreshing(true);
    }

    protected BaseQuickAdapter createAdapter() {
        mAdapter = new AlbumListAdapter(mDatas);
        mAdapter.setEnableLoadMore(true);
        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        return mAdapter;
    }

    @Override
    protected void lazyLoad() {
        super.lazyLoad();
        pIndex = 1;
        mvpPresenter.getNoteList(pIndex, pSize, ConstantValue.NOTE_TYPE_ALBUM);
    }

    @Override
    protected void setListener() {
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pIndex = 1;
                mvpPresenter.getNoteList(pIndex, pSize,ConstantValue.NOTE_TYPE_ALBUM);
            }
        });

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter var1, View var2, int i){
                Note note = mDatas.get(i);
                Intent intent = new Intent(mContext, AlbumDetailActivity.class);
                intent.putExtra("noteId",note.getId());
                startActivity(intent);
            }
        });
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override public void onLoadMoreRequested() {
                mvpPresenter.getNoteList(++pIndex, pSize,ConstantValue.NOTE_TYPE_ALBUM);
            }
        },recyclerView);
    }

    @Override
    public void onGetNoteListSuccess(Boolean success, String err, List<Note> response) {
        srl.setRefreshing(false);
        mAdapter.loadMoreComplete();
        if (success) {
            if (response == null) { //如果返回null，表明到末尾了
                if (pIndex > 1)
                    mAdapter.loadMoreEnd();
                return;
            }
            if (response.size() < pSize) { //如果返回的记录数比期望的小，则表明到末尾了
                mAdapter.loadMoreEnd();
            }
            if (pIndex == 1) {
                mAdapter.getData().clear();
            }

            mAdapter.addData(response);
            mAdapter.notifyDataSetChanged();

        }else {
            mAdapter.loadMoreFail();
        }
    }
}
