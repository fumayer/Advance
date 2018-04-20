package com.newsdemo.ui.gold.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.newsdemo.R;
import com.newsdemo.app.Constants;
import com.newsdemo.base.SimpleActivity;
import com.newsdemo.component.RxBus;
import com.newsdemo.model.bean.GoldManagerBean;
import com.newsdemo.model.bean.GoldManagerItemBean;
import com.newsdemo.ui.gold.adapter.GoldManagerAdapter;
import com.newsdemo.widget.DefaultItemTouchHelperCallBack;

import java.util.Collections;

import butterknife.BindView;
import io.realm.RealmList;

/**
 * Created by jianqiang.hu on 2017/5/26.
 */

public class GoldManagerActivity extends SimpleActivity{

    @BindView(R.id.tool_bar)
    Toolbar toolBar;

    @BindView(R.id.iv_gold_manager_list)
    RecyclerView rvGoldManagerList;

    RealmList<GoldManagerItemBean> mList;
    GoldManagerAdapter mAdapter;
    DefaultItemTouchHelperCallBack mCallback;
    @Override
    protected int getLayout() {
        return R.layout.activity_gold_manager;
    }

    @Override
    protected void initEventAndData() {
        setToolBar(toolBar,"首页特别展示");
        mList=((GoldManagerBean)getIntent().getParcelableExtra(Constants.IT_GOLD_MANAGER)).getManagerList();
        mAdapter=new GoldManagerAdapter(mContext,mList);
        rvGoldManagerList.setLayoutManager(new LinearLayoutManager(mContext));
        rvGoldManagerList.setAdapter(mAdapter);
        mCallback=new DefaultItemTouchHelperCallBack(new DefaultItemTouchHelperCallBack.OnItemTouchCallbackListener() {
            @Override
            public void onSwipe(int adaperPosition) {
                //拖拽
            }

            @Override
            public boolean onMove(int srcPosition, int taretPosition) {
                //滑动
                if (mList!=null){
                    Collections.swap(mList,srcPosition,taretPosition);
                    mAdapter.notifyItemMoved(srcPosition,taretPosition);
                    return true;
                }
                return false;
            }
        });
        mCallback.setDragEnable(true);
        mCallback.setSwipeEnable(false);
        ItemTouchHelper itemTouchHelper=new ItemTouchHelper(mCallback);
        itemTouchHelper.attachToRecyclerView(rvGoldManagerList);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.getDefault().post(new GoldManagerBean(mList));
    }
}
