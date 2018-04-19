package com.newsdemo.presenter.gank;

import com.newsdemo.app.Constants;
import com.newsdemo.base.RxPresent;
import com.newsdemo.base.contract.gank.TechContract;
import com.newsdemo.component.RxBus;
import com.newsdemo.model.DataManager;
import com.newsdemo.model.bean.GankItemBean;
import com.newsdemo.model.bean.GankSearchItemBean;
import com.newsdemo.model.event.SearchEvent;
import com.newsdemo.model.http.response.GankHttpResponse;
import com.newsdemo.ui.gank.fragment.GankMainFragment;
import com.newsdemo.util.RxUtil;
import com.newsdemo.widget.CommonSubscriber;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * Created by jianqiang.hu on 2017/5/25.
 */

public class TechPresenter extends RxPresent<TechContract.View> implements TechContract.Presenter{

    private DataManager mDataManager;
    private String queryStr;
    private int currenType= Constants.TYPE_ANDROID;
    private int currentPage = 1;
    private String currentTech = GankMainFragment.tabTitle[0];
    private static final int NUM_OF_PAGE = 20;
    @Inject
    public TechPresenter(DataManager mDataManager){
        this.mDataManager=mDataManager;
    }

    @Override
    public void attachView(TechContract.View view) {
        super.attachView(view);
        registerEvent();
    }


    private void registerEvent(){
        addSubscribe(RxBus.getDefault().toFlowable(SearchEvent.class)
                    .compose(RxUtil.<SearchEvent>rxSchedulerHelper())
                    .filter(new Predicate<SearchEvent>() {
                        @Override
                        public boolean test(@NonNull SearchEvent searchEvent) throws Exception {
                            return searchEvent.getType()==currenType;
                        }
                    })
                .map(new Function<SearchEvent, String>() {
                    @Override
                    public String apply(@NonNull SearchEvent searchEvent) throws Exception {
                        return searchEvent.getQuery();
                    }
                })
                .subscribeWith(new CommonSubscriber<String>(mView, "搜索失败") {
                    @Override
                    public void onNext(String s) {
                        queryStr=s;
                        getSearchTechData();
                    }
                })

        );
    }

    private void getSearchTechData(){
        currentPage=1;
        addSubscribe(mDataManager.fetchGankSearchList(queryStr,currentTech,NUM_OF_PAGE,currentPage)
                   .compose(RxUtil.<GankHttpResponse<List<GankSearchItemBean>>>rxSchedulerHelper())
                .compose(RxUtil.<List<GankSearchItemBean>>handleResult())
                .map(new Function<List<GankSearchItemBean>, List<GankItemBean>>() {
                    @Override
                    public List<GankItemBean> apply(@NonNull List<GankSearchItemBean> gankSearchItemBeen) throws Exception {
                        List<GankItemBean> list=new ArrayList<GankItemBean>();
                        for (GankSearchItemBean item:gankSearchItemBeen){
                            GankItemBean bean = new GankItemBean();
                            bean.set_id(item.getGanhuo_id());
                            bean.setDesc(item.getDesc());
                            bean.setPublishedAt(item.getPublishedAt());
                            bean.setWho(item.getWho());
                            bean.setUrl(item.getUrl());
                            list.add(bean);
                        }
                        return list;
                    }
                })
                .subscribeWith(new CommonSubscriber<List<GankItemBean>>(mView) {
                    @Override
                    public void onNext(List<GankItemBean> gankItemBeen) {
                        mView.showContent(gankItemBeen);
                    }
                })
        );
    }

    @Override
    public void getGankData(String tech, int type) {
        queryStr=null;
        currentPage=1;
        currentTech=tech;
        currenType=type;

        addSubscribe(mDataManager.fetchTechList(tech,NUM_OF_PAGE,currentPage)
                    .compose(RxUtil.<GankHttpResponse<List<GankItemBean>>>rxSchedulerHelper())
                    .compose(RxUtil.<List<GankItemBean>>handleResult())
                    .subscribeWith(new CommonSubscriber<List<GankItemBean>>(mView) {
                        @Override
                        public void onNext(List<GankItemBean> gankItemBeen) {
                            mView.showContent(gankItemBeen);
                        }
                    })
        );


    }

    @Override
    public void getMoreGankData(String tech) {
        if (queryStr!=null){
            getSearchTechData();
            return;
        }
        addSubscribe(mDataManager.fetchTechList(tech,NUM_OF_PAGE,++currentPage)
                    .compose(RxUtil.<GankHttpResponse<List<GankItemBean>>>rxSchedulerHelper())
                .compose(RxUtil.<List<GankItemBean>>handleResult())
                .subscribeWith(new CommonSubscriber<List<GankItemBean>>(mView) {
                    @Override
                    public void onNext(List<GankItemBean> gankItemBeen) {
                        mView.showMoreContent(gankItemBeen);
                    }
                })
        );

    }

    @Override
    public void getGirlImage() {
        addSubscribe(mDataManager.fetchRandomGirl(1)
                .compose(RxUtil.<GankHttpResponse<List<GankItemBean>>>rxSchedulerHelper())
                .compose(RxUtil.<List<GankItemBean>>handleResult())
                .subscribeWith(new CommonSubscriber<List<GankItemBean>>(mView, "加载封面失败", false) {
                    @Override
                    public void onNext(List<GankItemBean> gankItemBeen) {
                        mView.showGirlImage(gankItemBeen.get(0).getUrl(),gankItemBeen.get(0).getWho());
                    }
                })
        );
    }
}
