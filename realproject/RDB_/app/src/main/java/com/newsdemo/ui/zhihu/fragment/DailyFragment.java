package com.newsdemo.ui.zhihu.fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.newsdemo.R;
import com.newsdemo.base.RootFragment;
import com.newsdemo.base.contract.zhihu.DailyContract;
import com.newsdemo.component.RxBus;
import com.newsdemo.model.bean.DailyBeforeListBean;
import com.newsdemo.model.bean.DailyListBean;
import com.newsdemo.presenter.zhihu.DailyPresenter;
import com.newsdemo.ui.main.activity.MainActivity;
import com.newsdemo.ui.zhihu.activity.CalendarActivity;
import com.newsdemo.ui.zhihu.activity.ZhihuDetailActivity;
import com.newsdemo.ui.zhihu.adapter.DailyAdapter;
import com.newsdemo.util.CircularAnimUtil;
import com.newsdemo.util.DateUtil;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.newsdemo.app.Constants.IT_ZHIHU_DETAIL_ID;

/**
 * Created by jianqiang.hu on 2017/5/15.
 */

public class DailyFragment extends RootFragment<DailyPresenter> implements DailyContract.View{

    @BindView(R.id.fab_calender)
    FloatingActionButton fabCalender;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;

    @BindView(R.id.view_main)
    RecyclerView rvDailyList;



    String currentDate;
    DailyAdapter mAdapter;
    List<DailyListBean.StoriesBean> mList=new ArrayList<>();

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_daily;
    }

    @Override
    protected void initEventAndData() {
        super.initEventAndData();
        currentDate= DateUtil.getTomorrowDate();
        mAdapter=new DailyAdapter(mContext,mList);
        mAdapter.setOnItemClickListener(new DailyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                mPresentter.insertReadToDB(mList.get(position).getId());//存储点击过的
                mAdapter.setReadState(position,true);
                if (mAdapter.getIsBefore()){
                    mAdapter.notifyItemChanged(position+1);
                }else{
                    mAdapter.notifyItemChanged(position+2);
                }

                Intent intent=new Intent();
                intent.setClass(mContext, ZhihuDetailActivity.class);
                intent.putExtra(IT_ZHIHU_DETAIL_ID,mList.get(position).getId());
                ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(mActivity,view,"shareView");
                mContext.startActivity(intent,options.toBundle());
            }
        });

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (currentDate.equals(DateUtil.getTomorrowDate())){
                    mPresentter.getDailyData();
                }else{
                    int year = Integer.valueOf(currentDate.substring(0,4));
                    int month = Integer.valueOf(currentDate.substring(4,6));
                    int day = Integer.valueOf(currentDate.substring(6,8));
                    CalendarDay date = CalendarDay.from(year, month - 1, day);
                    RxBus.getDefault().post(date);
                }
            }
        });

        rvDailyList.setLayoutManager(new LinearLayoutManager(mContext));
        rvDailyList.setAdapter(mAdapter);
        stateLoading();
        mPresentter.getDailyData();
    }

    /**
     * 当天数据
     * @param info
     */
    @Override
    public void showContent(DailyListBean info) {
        if (swipeRefresh.isRefreshing()){
            swipeRefresh.setRefreshing(false);
        }
        stateMain();
        mList=info.getStories();
        currentDate=String.valueOf(Integer.valueOf(info.getDate())+1);
        mAdapter.addDailyDate(info);
        mPresentter.stopInterval();
        mPresentter.startInterval();

    }

    @Override
    public void showMoreContent(String date, DailyBeforeListBean info) {
        if (swipeRefresh.isRefreshing()){
            swipeRefresh.setRefreshing(false);
        }
        stateMain();
        mPresentter.stopInterval();
        mList=info.getStories();
        currentDate=String.valueOf(Integer.valueOf(info.getDate()));
        mAdapter.addDailyDefore(info);
    }

    @Override
    public void doInterval(int currentCount) {
        mAdapter.changeTopPager(currentCount);
    }

    @OnClick(R.id.fab_calender)
    void startCalender(){
        final Intent it=  new Intent();
        it.setClass(mContext, CalendarActivity.class);
        CircularAnimUtil.fullActivity(mActivity, fabCalender)
                .colorOrImageRes(R.color.fab_bg)
                .go(new CircularAnimUtil.OnAnimationEndListener() {
                    @Override
                    public void onAnimationEnd() {
                        startActivity(it);
                    }
                });
    }

    @Override
    public void stateError() {
        super.stateError();
        if (swipeRefresh.isRefreshing()){
            swipeRefresh.setRefreshing(false);
        }
    }



}
