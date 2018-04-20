package com.newsdemo.presenter.zhihu;

import com.newsdemo.base.RxPresent;
import com.newsdemo.base.contract.zhihu.ThemeChildContract;
import com.newsdemo.model.DataManager;
import com.newsdemo.model.bean.ThemeChildListBean;
import com.newsdemo.util.RxUtil;
import com.newsdemo.widget.CommonSubscriber;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;


/**
 * Created by jianqiang.hu on 2017/5/24.
 */

public class ThemeChildPresent extends RxPresent<ThemeChildContract.View> implements ThemeChildContract.Presenter {

    DataManager mDatamanager;

    @Inject
   public ThemeChildPresent(DataManager mDatamanager){
       this.mDatamanager=mDatamanager;
   }

    @Override
    public void getThemeChildData(int id) {
            addSubscribe(mDatamanager.fetchThemeChildListInfo(id)
                        .compose(RxUtil.<ThemeChildListBean>rxSchedulerHelper())
                        .map(new Function<ThemeChildListBean, ThemeChildListBean>() {
                            @Override
                            public ThemeChildListBean apply(@NonNull ThemeChildListBean themeChildListBean) throws Exception {
                                List<ThemeChildListBean.StoriesBean> list=themeChildListBean.getStories();
                                for (ThemeChildListBean.StoriesBean item:list ){
                                    item.setReadState(mDatamanager.queryNewsId(item.getId()));
                                }
                                return themeChildListBean;
                            }
                        })
                    .subscribeWith(new CommonSubscriber<ThemeChildListBean>(mView) {
                        @Override
                        public void onNext(ThemeChildListBean themeChildListBean) {
                            mView.showContent(themeChildListBean);
                        }
                    })
            );
    }

    @Override
    public void insertReadToDB(int id) {
        mDatamanager.insertNewsId(id);
    }
}
