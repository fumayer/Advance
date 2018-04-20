package com.newsdemo.presenter.gold;

import com.newsdemo.base.RxPresent;
import com.newsdemo.base.contract.gold.GoldContract;
import com.newsdemo.model.DataManager;
import com.newsdemo.model.bean.GoldListBean;
import com.newsdemo.model.http.response.GoldHttpResponse;
import com.newsdemo.util.RxUtil;
import com.newsdemo.widget.CommonSubscriber;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;

/**
 * Created by jianqiang.hu on 2017/5/26.
 */

public class GoldPresenter extends RxPresent<GoldContract.View> implements GoldContract.Presenter{

    private static final int NUM_EACH_PAGE = 20;
    private static final int NUM_HOT_LIMIT = 3;
    private List<GoldListBean> totalList = new ArrayList<>();

    private DataManager mDataManager;

    private boolean isHotList = true;
    private String mType;

    private int currentPage = 0;

    @Inject
    public GoldPresenter(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void getGoldData(String type) {
        mType=type;
        currentPage=0;
        totalList.clear();

        Flowable<List<GoldListBean>> list = mDataManager.fetchGoldList(type,NUM_EACH_PAGE,currentPage++)
                .compose(RxUtil.<GoldHttpResponse<List<GoldListBean>>>rxSchedulerHelper())
                .compose(RxUtil.<List<GoldListBean>>handleGoldResult());

        Calendar cal=Calendar.getInstance();
        cal.add(Calendar.DATE,-3);

        Flowable<List<GoldListBean>> hotList=mDataManager.fetchGoldHotList(type,
                new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime()),NUM_HOT_LIMIT)
                .compose(RxUtil.<GoldHttpResponse<List<GoldListBean>>>rxSchedulerHelper())
                .compose(RxUtil.<List<GoldListBean>>handleGoldResult());


        addSubscribe(Flowable.concat(hotList,list)//Observable.concat只接受相同泛型的参数,concat符操作处理多数据源
                .subscribeWith(new CommonSubscriber<List<GoldListBean>>(mView) {
                    @Override
                    public void onNext(List<GoldListBean> goldListBeen) {
                        if (isHotList){
                            isHotList=false;
                            totalList.addAll(goldListBeen);
                        }else{
                            isHotList=true;
                            totalList.addAll(goldListBeen);
                            mView.showContent(totalList);
                        }
                    }
                })
        );
    }

    @Override
    public void getModeGoldData() {
        addSubscribe(mDataManager.fetchGoldList(mType, NUM_EACH_PAGE, currentPage++)
                .compose(RxUtil.<GoldHttpResponse<List<GoldListBean>>>rxSchedulerHelper())
                .compose(RxUtil.<List<GoldListBean>>handleGoldResult())
                .subscribeWith(new CommonSubscriber<List<GoldListBean>>(mView, false) {
                    @Override
                    public void onNext(List<GoldListBean> goldListBeen) {
                        totalList.addAll(goldListBeen);
                        mView.showMoreContent(totalList, totalList.size(), totalList.size() + NUM_EACH_PAGE);
                    }
                })
        );
    }
}
