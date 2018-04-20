package com.newsdemo.presenter.zhihu;

import com.newsdemo.base.RxPresent;
import com.newsdemo.base.contract.zhihu.DailyContract;
import com.newsdemo.component.RxBus;
import com.newsdemo.model.DataManager;
import com.newsdemo.model.bean.DailyBeforeListBean;
import com.newsdemo.model.bean.DailyListBean;
import com.newsdemo.util.DateUtil;
import com.newsdemo.util.RxUtil;
import com.newsdemo.widget.CommonSubscriber;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import org.reactivestreams.Publisher;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

import static com.newsdemo.util.DateUtil.getTomorrowDate;

/**
 * Created by jianqiang.hu on 2017/5/16.
 */

public class DailyPresenter extends RxPresent<DailyContract.View> implements DailyContract.Presenter {


    private DataManager mDataManager;
    private Disposable intervalSubscription;


    private static final int INTERVAL_INSTANCE = 6;

    private int topCount = 0;
    private int currentTopCount = 0;


    @Inject
    public DailyPresenter(DataManager mDataManager){
       this.mDataManager=mDataManager;
    }

    @Override
    public void attachView(DailyContract.View view) {
        super.attachView(view);
        registerEvent();
    }
    private void registerEvent(){
        addSubscribe(RxBus.getDefault().toFlowable(CalendarDay.class)
                .subscribeOn(Schedulers.io())
                .map(new Function<CalendarDay, String>() {
                    @Override
                    public String apply(@NonNull CalendarDay calendarDay) throws Exception {
                        StringBuilder date = new StringBuilder();
                        String year=String.valueOf(calendarDay.getYear());
                        String month=String.valueOf(calendarDay.getMonth()+1);
                        String day=String.valueOf(calendarDay.getDay()+1);
                        if (month.length()<2){
                            month="0"+month;
                        }
                        if (day.length()<2){
                            day="0"+day;
                        }
                        return date.append(year).append(month).append(day).toString();
                    }
                })
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(@NonNull String s) throws Exception {
                        String a=DateUtil.getTomorrowDate();
                        if (s.equals(getTomorrowDate())){
                            getDailyData();
                            return  false;
                        }
                        return true;
                    }
                })
                .observeOn(Schedulers.io())//为了网络请求切到io线程
                .flatMap(new Function<String, Flowable<DailyBeforeListBean>>() {
                    @Override
                    public Flowable<DailyBeforeListBean> apply(@NonNull String s) throws Exception {
                        return mDataManager.fetchDailyBeforeListInfo(s);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<DailyBeforeListBean, DailyBeforeListBean>() {
                    @Override
                    public DailyBeforeListBean apply(@NonNull DailyBeforeListBean dailyBeforeListBean) throws Exception {
                        List<DailyListBean.StoriesBean> list=dailyBeforeListBean.getStories();
                        for (DailyListBean.StoriesBean item:list){
                            item.setReadState(mDataManager.queryNewsId(item.getId()));
                        }
                        return dailyBeforeListBean;
                    }
                })
                .subscribeWith(new CommonSubscriber<DailyBeforeListBean>(mView) {
                    @Override
                    public void onNext(DailyBeforeListBean dailyBeforeListBean) {
                        int year = Integer.valueOf(dailyBeforeListBean.getDate().substring(0,4));
                        int month = Integer.valueOf(dailyBeforeListBean.getDate().substring(4,6));
                        int day = Integer.valueOf(dailyBeforeListBean.getDate().substring(6,8));
                        mView.showMoreContent(String.format("%d年%d月%d日",year,month,day), dailyBeforeListBean);
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        registerEvent();
                    }
                })


        );
    }


    @Override
    public void getDailyData() {
        addSubscribe(mDataManager.fetchDailyListInfo()
                        .compose(RxUtil.<DailyListBean>rxSchedulerHelper())
                        .map(new Function<DailyListBean, DailyListBean>() {
                            @Override
                            public DailyListBean apply(@NonNull DailyListBean dailyListBean) throws Exception {
                                List<DailyListBean.StoriesBean> stories = dailyListBean.getStories();
                                for (DailyListBean.StoriesBean item:stories){
                                    item.setReadState(mDataManager.queryNewsId(item.getId()));
                                }
                                return dailyListBean;
                            }
                        })
                    .subscribeWith(new CommonSubscriber<DailyListBean>(mView) {
                        @Override
                        public void onNext(DailyListBean dailyListBean) {
                            topCount=dailyListBean.getTop_stories().size();
                            mView.showContent(dailyListBean);
                        }
                    })
        );
    }

    @Override
    public void getBeforeData(String date) {

    }

    @Override
    public void startInterval() {
        intervalSubscription= Flowable.interval(INTERVAL_INSTANCE, TimeUnit.SECONDS)
                                .compose(RxUtil.<Long>rxSchedulerHelper())
                                .subscribe(new Consumer<Long>() {
                                    @Override
                                    public void accept(@NonNull Long aLong) throws Exception {
                                        if (currentTopCount==topCount){
                                            currentTopCount=0;
                                        }
                                        mView.doInterval(currentTopCount++);
                                    }
                                });
        addSubscribe(intervalSubscription);
    }

    @Override
    public void stopInterval() {
        if (intervalSubscription!=null){
            intervalSubscription.dispose();
        }
    }

    @Override
    public void insertReadToDB(int id) {
        mDataManager.insertNewsId(id);
    }
}
