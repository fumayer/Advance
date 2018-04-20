package com.newsdemo.presenter.vtex;

import com.newsdemo.base.RxPresent;
import com.newsdemo.base.contract.vtex.VtexContract;
import com.newsdemo.model.bean.TopicListBean;
import com.newsdemo.model.http.api.VtexApis;
import com.newsdemo.util.LogUtil;
import com.newsdemo.widget.CommonSubscriber;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import io.realm.internal.IOException;


/**
 * Created by jianqiang.hu on 2017/5/27.
 */

public class VtexPresenter extends RxPresent<VtexContract.View> implements VtexContract.Presenter{
    @Inject
    public VtexPresenter(){}

    @Override
    public void getContent(String type) {
        addSubscribe(Flowable
                .just(VtexApis.TAB_HOST+type)
                .subscribeOn(Schedulers.io())
                .map(new Function<String, Document>() {
                    @Override
                    public Document apply(@NonNull String s) throws Exception {
                        try {
                            return Jsoup.connect(s).timeout(10000).get();
                        }catch (IOException e){
                            LogUtil.d(e.toString());
                        }
                        return null;
                    }
                })
                .filter(new Predicate<Document>() {
                    @Override
                    public boolean test(@NonNull Document document) throws Exception {
                        return document!=null;
                    }
                })
                .map(new Function<Document, List<TopicListBean>>() {
                    @Override
                    public List<TopicListBean> apply(@NonNull Document document) throws Exception {
                        List<TopicListBean> mList=new ArrayList<TopicListBean>();
                        Elements itemElements=document.select("div.cell.item");
                        int count =itemElements.size();
                        for (int i=0;i<count;i++){
                            Elements titleElements = itemElements.get(i).select("div.cell.item table tr td span.item_title > a");   //标题
                            Elements imgElements = itemElements.get(i).select("div.cell.item table tr td img.avatar");              //头像
                            Elements nodeElements = itemElements.get(i).select("div.cell.item table tr span.small.fade a.node");    //节点
                            Elements commentElements = itemElements.get(i).select("div.cell.item table tr a.count_livid");          //评论数
                            Elements nameElements = itemElements.get(i).select("div.cell.item table tr span.small.fade strong a");  //作者 & 最后回复
                            Elements timeElements = itemElements.get(i).select("div.cell.item table tr span.small.fade");           //更新时间

                            TopicListBean bean=new TopicListBean();

                            if (titleElements.size()>0){
                                bean.setTitle(titleElements.get(0).text());
                                bean.setTopicId(parseId(titleElements.get(0).attr("href")));
                            }

                            if (imgElements.size()>0){
                                bean.setImgUrl(parseImg(imgElements.get(0).attr("src")));
                            }

                            if (nodeElements.size()>0){
                                bean.setNode(nodeElements.get(0).text());
                            }

                            if (nameElements.size()>0){
                                bean.setName(nameElements.get(0).text());
                            }

                            //存在没有 最后回复者、评论数、更新时间的情况
                            if (nameElements.size()>1){
                                bean.setLastUser(nameElements.get(1).text());
                            }
                            if (commentElements.size()>0){
                                bean.setCommentNum(Integer.valueOf(commentElements.get(0).text()));
                            }

                            if (timeElements.size()>1){
                                bean.setUpdateTime(parseTime(timeElements.get(1).text()));
                            }

                            mList.add(bean);
                        }
                        return mList;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new CommonSubscriber<List<TopicListBean>>(mView) {
                    @Override
                    public void onNext(List<TopicListBean> topicListBeen) {
                        mView.showContent(topicListBeen);
                    }
                })
        );

    }

    private String parseId(String str){
        int idEnd=str.indexOf("#");
        return str.substring(3,idEnd);
    }

    private String parseTime(String str) {
        int timeEnd = str.indexOf("  •");
        if (timeEnd == -1) {
            return str;
        }
        return str.substring(0, timeEnd);
    }

    public static String parseImg(String str) {
        return "http:" + str;
    }

}
