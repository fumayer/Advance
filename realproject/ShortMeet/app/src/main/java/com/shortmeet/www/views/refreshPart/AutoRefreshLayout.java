package com.shortmeet.www.views.refreshPart;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

/**
 * Created by Fenglingyue on 2017/10/18.
 */
public class AutoRefreshLayout extends SwipeRefreshLayout {
    public AutoRefreshLayout(Context context) {
        this(context, null);
    }

    public AutoRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void refreshComplete() {
        setRefreshing(false);
    }

    public void autoRefresh(final OnRefreshListener onRefreshListener, long time) {
        if (onRefreshListener != null) {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    setRefreshing(true);
                    onRefreshListener.onRefresh();
                }
            }, time);

        }
    }

    public void autoRefresh(final OnRefreshListener onRefreshListener) {
        if (onRefreshListener != null) {
            post(new Runnable() {
                @Override
                public void run() {
                    //手动调用,通知系统去测量
                    measure(0, 0);
                    setRefreshing(true);
                    onRefreshListener.onRefresh();
                }
            });

        }
    }

}
