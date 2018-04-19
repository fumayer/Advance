package com.newsdemo.ui.gank.fragment;

import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.newsdemo.R;
import com.newsdemo.app.Constants;
import com.newsdemo.base.RootFragment;
import com.newsdemo.base.contract.gank.TechContract;
import com.newsdemo.component.GlidUtils;
import com.newsdemo.model.bean.GankItemBean;
import com.newsdemo.presenter.gank.TechPresenter;
import com.newsdemo.ui.gank.activity.TechDetailActivity;
import com.newsdemo.ui.gank.adapter.TechAdapter;
import com.newsdemo.util.LogUtil;
import com.newsdemo.util.SystemUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import retrofit2.http.POST;

import static android.R.attr.flipInterval;
import static android.R.attr.id;

/**
 * Created by jianqiang.hu on 2017/5/25.
 */

public class TechFragment extends RootFragment<TechPresenter> implements TechContract.View{

    @BindView(R.id.view_main)
    RecyclerView rvTechContent;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;

    @BindView(R.id.tv_tech_blur)
    ImageView ivBlur;

    @BindView(R.id.tv_tech_origin)
    ImageView ivOrigin;

    @BindView(R.id.tv_tech_copyright)
    TextView tvCopyRight;

    @BindView(R.id.tech_appbar)
    AppBarLayout appbar;

    private TechAdapter mAdapter;

    private List<GankItemBean> mList;
    String tech;
    int type;
    boolean isLoadingMore = false;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tech;
    }

    @Override
    protected void initEventAndData() {
        super.initEventAndData();
        mPresentter.getGirlImage();

        mList=new ArrayList<>();
        tech=getArguments().getString(Constants.IT_GANK_TYPE);
        type=getArguments().getInt(Constants.IT_GANK_TYPE_CODE);
        mAdapter=new TechAdapter(mContext,mList,tech);
        rvTechContent.setLayoutManager(new LinearLayoutManager(mContext));
        rvTechContent.setAdapter(mAdapter);

        stateLoading();
        mPresentter.getGankData(tech,type);
        mAdapter.setOnItemClickListener(new TechAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                TechDetailActivity.launcher(new TechDetailActivity.Builder()
                                            .setmContext(mContext)
                                            .setId(mList.get(position).get_id())
                                            .setTitle(mList.get(position).getType())
                                            .setUrl(mList.get(position).getUrl())
                                            .setType(type)
                                            .setAnimConfig(mActivity,view)
                );
            }
        });

        appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {//解决swip滑动冲突
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                LogUtil.d(verticalOffset+"--");
                if (verticalOffset>=0){//下滑到顶端
                    swipeRefresh.setEnabled(true);
                }else{
                    swipeRefresh.setEnabled(false);
                    float rate=((float) SystemUtil.dp2px(mContext,256)+verticalOffset*2)/SystemUtil.dp2px(mContext,256);
                    if (rate>0){
                        ivOrigin.setAlpha(rate);
                    }
                }
            }
        });
        rvTechContent.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItem=((LinearLayoutManager)rvTechContent.getLayoutManager()).findLastVisibleItemPosition();
                int totalItemCount=rvTechContent.getLayoutManager().getItemCount();
                if (lastVisibleItem>=totalItemCount-2 && dy>0){//还剩2个Item时加载更多
                        if (!isLoadingMore){
                            isLoadingMore=true;
                            mPresentter.getMoreGankData(tech);
                        }
                }
            }
        });



        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresentter.getGankData(tech,type);
            }
        });
    }



    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    public void showContent(List<GankItemBean> list) {
        if(swipeRefresh.isRefreshing()) {
            swipeRefresh.setRefreshing(false);
        }
        stateMain();
        mList.clear();
        mList.addAll(list);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showMoreContent(List<GankItemBean> listit) {
        stateMain();
        mList.addAll(listit);
        mAdapter.notifyDataSetChanged();
        isLoadingMore=false;
    }

    @Override
    public void showGirlImage(String url, String copyright) {
        GlidUtils.load(mContext,url,ivOrigin);
        GlidUtils.loadBlur(mContext,url,ivBlur,15);
        tvCopyRight.setText(String.format("by:%s",copyright));
    }


    @Override
    public void stateError() {
        super.stateError();
        if (swipeRefresh.isRefreshing()){
            swipeRefresh.setRefreshing(false);
        }
    }
}
