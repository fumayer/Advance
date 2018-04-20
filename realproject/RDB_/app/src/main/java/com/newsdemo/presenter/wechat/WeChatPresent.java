package com.newsdemo.presenter.wechat;


import com.newsdemo.app.Constants;
import com.newsdemo.base.RxPresent;
import com.newsdemo.base.contract.wechat.WechatContract;
import com.newsdemo.component.RxBus;
import com.newsdemo.model.DataManager;
import com.newsdemo.model.bean.WXItemBean;
import com.newsdemo.model.event.SearchEvent;
import com.newsdemo.model.http.response.WXHttpResponse;
import com.newsdemo.util.RxUtil;
import com.newsdemo.widget.CommonSubscriber;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.annotations.Experimental;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * Created by jianqiang.hu on 2017/5/24.
 */

public class WeChatPresent extends RxPresent<WechatContract.View> implements  WechatContract.Present{

    DataManager mDataManager;
    private static final int NUM_OF_PAGE = 20;//每页显示多少条

    private int currentPage = 1;//当前页数
    private String queryStr = null;
    @Inject
    public WeChatPresent(DataManager mDataManager){
        this.mDataManager=mDataManager;
    }

    @Override
    public void attachView(WechatContract.View view) {
        super.attachView(view);
        registerEvent();
    }


    private void registerEvent(){
        addSubscribe(RxBus.getDefault().toFlowable(SearchEvent.class)
                    .compose(RxUtil.<SearchEvent>rxSchedulerHelper())
                    .filter(new Predicate<SearchEvent>() {
                        @Override
                        public boolean test(@NonNull SearchEvent searchEvent) throws Exception {
                            return searchEvent.getType()== Constants.TYPE_WECHAT;
                        }
                    })
                .map(new Function<SearchEvent, String>() {
                    @Override
                    public String apply(@NonNull SearchEvent searchEvent) throws Exception {
                        return searchEvent.getQuery();
                    }
                })
                .subscribeWith(new CommonSubscriber<String>(mView, "搜索失败ヽ(≧Д≦)ノ") {
                    @Override
                    public void onNext(String s) {
                        queryStr=s;
                        getSeatchWechatData(s);
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        registerEvent();
                    }
                })
        );
    }

    private void getSeatchWechatData(String query){
        currentPage=1;
        addSubscribe(mDataManager.fetchWechatSearchListInfo(NUM_OF_PAGE,currentPage,query)
                    .compose(RxUtil.<WXHttpResponse<List<WXItemBean>>>rxSchedulerHelper())
                .compose(RxUtil.<List<WXItemBean>>handleWXResult())
                .subscribeWith(new CommonSubscriber<List<WXItemBean>>(mView) {
                    @Override
                    public void onNext(List<WXItemBean> wxItemBeen) {
                        mView.showContent(wxItemBeen);
                    }
                })
        );
    }


    @Override
    public void getWeChatData() {
        queryStr=null;
        currentPage=1;
        addSubscribe(mDataManager.fetchWechatListInfo(NUM_OF_PAGE,currentPage)
                .compose(RxUtil.<WXHttpResponse<List<WXItemBean>>>rxSchedulerHelper())
                .compose(RxUtil.<List<WXItemBean>>handleWXResult())
                .subscribeWith(new CommonSubscriber<List<WXItemBean>>(mView) {
                    @Override
                    public void onNext(List<WXItemBean> wxItemBeen) {
                        mView.showContent(wxItemBeen);
                    }
                })
        );
    }

    @Override
    public void getMoreWechatData() {
        Flowable<WXHttpResponse<List<WXItemBean>>> observable;

        if (queryStr!=null){
            observable=mDataManager.fetchWechatSearchListInfo(NUM_OF_PAGE,++currentPage,queryStr);
        }else{
            observable=mDataManager.fetchWechatListInfo(NUM_OF_PAGE,++currentPage);
        }
        addSubscribe(observable
                    .compose(RxUtil.<WXHttpResponse<List<WXItemBean>>>rxSchedulerHelper())
                .compose(RxUtil.<List<WXItemBean>>handleWXResult())
                .subscribeWith(new CommonSubscriber<List<WXItemBean>>(mView, "没有更多了ヽ(≧Д≦)ノ") {
                    @Override
                    public void onNext(List<WXItemBean> wxItemBeen) {
                        mView.showContent(wxItemBeen);
                    }
                })
        );
    }
}
