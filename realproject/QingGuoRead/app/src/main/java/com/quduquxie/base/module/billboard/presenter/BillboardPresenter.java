package com.quduquxie.base.module.billboard.presenter;

import android.os.Bundle;
import android.text.TextUtils;

import com.quduquxie.base.RxPresenter;
import com.quduquxie.base.module.billboard.adapter.BillboardAdapter;
import com.quduquxie.base.module.billboard.BillboardInterface;
import com.quduquxie.base.module.billboard.view.BillboardActivity;
import com.quduquxie.base.module.billboard.view.fragment.BillboardContentFragment;

import java.util.HashMap;

/**
 * Created on 17/7/25.
 * Created by crazylei.
 */

public class BillboardPresenter extends RxPresenter implements BillboardInterface.Presenter {

    private BillboardActivity billboardActivity;

    private HashMap<String, String[]> titles = new HashMap<String, String[]>() {
        {
            put("男频榜单", new String[]{"风云榜", "完本榜", "收藏榜", "字数榜", "新书榜"});
            put("女频榜单", new String[]{"风云榜", "完本榜", "收藏榜", "字数榜", "新书榜"});
            put("榜单", new String[]{"男频榜", "女频榜", "完本榜", "收藏榜", "字数榜", "新书榜"});
        }
    };

    private HashMap<String, String> uris = new HashMap<String, String>() {
        {
            put("男频榜", "/v2/ranks/click");
            put("女频榜", "/v2/ranks/click");
            put("风云榜", "/v2/ranks/click");
            put("完本榜", "/v2/ranks/finish");
            put("收藏榜", "/v2/ranks/follows");
            put("字数榜", "/v2/ranks/word");
            put("新书榜", "/v2/ranks/fresh");
        }
    };

    private HashMap<String, String> dates = new HashMap<String, String>() {
        {
            put("/v2/ranks/click/year", "year");
            put("/v2/ranks/click/week", "week");
            put("/v2/ranks/click/month", "month");
        }
    };

    public BillboardPresenter(BillboardActivity billboardActivity) {
        this.billboardActivity = billboardActivity;
    }

    @Override
    public void initializeParameter(BillboardAdapter billboardAdapter, String title, String uri, String date) {

        if (titles.containsKey(title)) {
            if ("男频榜单".equals(title)) {
                initializeFragment(billboardAdapter, titles.get(title), date, "男频");
            } else if ("女频榜单".equals(title)) {
                initializeFragment(billboardAdapter, titles.get(title), date, "女频");
            } else {
                initializeFragment(billboardAdapter, titles.get(title), date, "");
            }
        }
    }

    @Override
    public String initializeDate(String uri) {
        return dates.get(uri);
    }

    private void initializeFragment(BillboardAdapter billboardAdapter, String[] titles, String date, String channel) {

        for (String title : titles) {
            String uri = uris.get(title);
            Bundle bundle = new Bundle();
            bundle.putString("uri", uri);
            bundle.putString("title", title);

            if ("男频榜".equals(title)) {
                bundle.putString("parameter", "男频");
            } else if ("女频榜".equals(title)) {
                bundle.putString("parameter", "女频");
            }

            bundle.putString("channel", channel);
            billboardAdapter.insetTab(title, title, BillboardContentFragment.class, bundle);
        }
    }

    @Override
    public void recycle() {

    }
}
