package com.aiwue.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.aiwue.R;
import com.aiwue.base.BaseActivity;
import com.aiwue.listener.ItemDragHelperCallBack;
import com.aiwue.listener.OnChannelDragListener;
import com.aiwue.model.Channel;
import com.aiwue.ui.adapter.ChannelAdapter;

public class ChannelActivity extends BaseActivity implements OnChannelDragListener {
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.icon_collapse)
    ImageView icon_collapse;
    private List<Channel> mDatas = new ArrayList<>();
    private ChannelAdapter mAdapter;
    private final String[] titles = new String[]{"推荐", "视频", "热点", "社会", "娱乐", "科技", "汽车", "体育", "财经", "军事", "国际", "时尚", "游戏", "旅游", "历史", "探索", "美食", "育儿", "养生", "故事", "美文"};
    private ItemTouchHelper mHelper;

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_channel);
        ButterKnife.bind(this);
    }

    @Override
    protected void bindViews() {
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        generateDatas();
        mAdapter = new ChannelAdapter(mDatas);
        GridLayoutManager manager = new GridLayoutManager(this, 4);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mAdapter);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int itemViewType = mAdapter.getItemViewType(position);
                return itemViewType == Channel.TYPE_MY_CHANNEL || itemViewType == Channel.TYPE_OTHER_CHANNEL ? 1 : 4;
            }
        });
        ItemDragHelperCallBack callBack = new ItemDragHelperCallBack(this);
        mHelper = new ItemTouchHelper(callBack);
        mAdapter.setOnChannelDragListener(this);
        //attachRecyclerView
        mHelper.attachToRecyclerView(mRecyclerView);
    }

    /**
     * 生成频道数据
     */
    private void generateDatas() {
        mDatas.add(new Channel(Channel.TYPE_MY, "我的频道"));
        for (int i = 0; i < titles.length; i++) {
            String title = titles[i];
            mDatas.add(new Channel(Channel.TYPE_MY_CHANNEL, title));
        }
        mDatas.add(new Channel(Channel.TYPE_OTHER, "频道推荐"));
        for (int i = 0; i < titles.length; i++) {
            String title = titles[i];
            mDatas.add(new Channel(Channel.TYPE_OTHER_CHANNEL, title + "推荐"));
        }
    }

    @Override
    protected void setListener() {
        icon_collapse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onStarDrag(BaseViewHolder baseViewHolder) {
        //开始拖动
        Logger.i("开始拖动");
        mHelper.startDrag(baseViewHolder);
    }

    @Override
    public void onItemMove(int starPos, int endPos) {
//        if (starPos < 0||endPos<0) return;
        Channel startChannel = mDatas.get(starPos);
        //先删除之前的位置
        mDatas.remove(starPos);
        //添加到现在的位置
        mDatas.add(endPos, startChannel);
        mAdapter.notifyItemMoved(starPos, endPos);
    }
}
