package com.shortmeet.www.ui.Attention.fragment;


import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.shortmeet.www.Base.BaseFragment;
import com.shortmeet.www.R;
import com.shortmeet.www.mvp.IMVPView;
import com.shortmeet.www.ui.Attention.adapter.AttentionHeadAdapter;
import com.shortmeet.www.ui.Attention.adapter.AttentionVideoAdapter;
import com.shortmeet.www.utilsUsed.StatusBarUtil;
import com.shortmeet.www.utilsUsed.UiUtils;
import com.shortmeet.www.views.refreshPart.AutoRefreshLayout;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link } subclass.
 */
public class AttentionFragment extends BaseFragment implements IMVPView {
    //标识
    private static final String TAG = "AttentionFragment";
    private View topStusbar;//状态栏
    private RecyclerView attention_push_recycler;//推送Recycler
    private RecyclerView attentionRecyclerList;//关注视屏列表
    private AttentionHeadAdapter attentionHeadAdapter;//推送适配器
    private AttentionVideoAdapter attentionVideoAdapter;//关注列表适配器
    private View headView;//头部view
    private AutoRefreshLayout autorefresh_attentionfrag;//刷新
    private boolean refreshHead=false;
    private boolean refreshVedio=false;
    private List<String> headList;
    private List<String> vedioList;
    @Override
    public int setFragRootView() {
        return R.layout.fragment_attention;
    }

    @Override
    public void initView() {
        topStusbar = (View)contentView.findViewById(R.id.top_stusbar);
        attentionRecyclerList = (RecyclerView)contentView.findViewById(R.id.attention_recycler_list);
        autorefresh_attentionfrag= (AutoRefreshLayout) contentView.findViewById(R.id.autorefresh_attentionfrag);
        autorefresh_attentionfrag.setColorSchemeResources(R.color.black_3);
        initStusBar();
        initHeadView();
        initVideoRecycler();
    }

    public  void  initStusBar(){
        int stusBarHeight= StatusBarUtil.getStatusBarHeight(mActivity);
        LinearLayout.LayoutParams layoutParams= (LinearLayout.LayoutParams) topStusbar.getLayoutParams();
        layoutParams.height=stusBarHeight;
        topStusbar.setBackgroundColor(UiUtils.getColor(R.color.black));
    }

    private void initHeadView() {
        headView=mLayoutInflater.inflate(R.layout.item_attentiongrag_headview,null);
        attention_push_recycler= (RecyclerView) headView.findViewById(R.id.attention_push_recycler);
        initHeadRecycler();
    }

    private void initHeadRecycler() {
        LinearLayoutManager layoutManager=new LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false);
        attention_push_recycler.setLayoutManager(layoutManager);
        attentionHeadAdapter = new AttentionHeadAdapter(R.layout.item_attention_head,null);
        attention_push_recycler.setAdapter(attentionHeadAdapter);
    }

    private void initVideoRecycler() {
        LinearLayoutManager layoutManager=new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false);
        attentionRecyclerList.setLayoutManager(layoutManager);
        attentionVideoAdapter=new AttentionVideoAdapter(R.layout.item_attention_vedio,null);
        attentionVideoAdapter.addHeaderView(headView,0);
        attentionRecyclerList.setAdapter(attentionVideoAdapter);
    }

    @Override
    public void initListener() {
        autorefresh_attentionfrag.setOnRefreshListener(mOnRefreshListener);
    }
    @Override
    public void initData() {
        autorefresh_attentionfrag.autoRefresh(mOnRefreshListener);
    }

  /*  @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_attention, container, false);
    }*/

    @Override
    public void setData(Object o, int id) {
        switch (id){
            case 0:
                attentionHeadAdapter.setNewData(headList);
                refreshHead=false;
                break;
            case 1:
                attentionVideoAdapter.setNewData(vedioList);
                refreshVedio=false;
                break;
        }
        if (!refreshHead &&!refreshVedio ){
            autorefresh_attentionfrag.refreshComplete();
        }

    }

    /*
*  Fly 注：刷新监听
*/
    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener= new AutoRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            refreshHead = true;
            refreshVedio=true;
            prepareData();
        }
    };


    /**
     * 数据获取
     */
    private void prepareData(){
        headList=new ArrayList<>();
        for (int i = 0;i<10;i++){
            headList.add(i+"");
        }
        setData(headList,0);

        vedioList=new ArrayList<>();
        for (int i = 0;i<10;i++){
            vedioList.add(i+"");
        }

        setData(vedioList,1);

    }
}
