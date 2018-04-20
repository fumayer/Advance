package com.newsdemo.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.newsdemo.app.App;
import com.newsdemo.util.SystemUtil;

/**
 * Created by jianqiang.hu on 2017/5/26.
 */

public class GoldItemDection extends RecyclerView.ItemDecoration {

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position=((RecyclerView.LayoutParams)view.getLayoutParams()).getViewAdapterPosition();
        if (position>-1){
            if (position==0){
                outRect.set(0, SystemUtil.dp2px(App.getInstance(), 15),0,0);
            }else if (position==3){
                outRect.set(0, SystemUtil.dp2px(App.getInstance(), 0.5f),0,SystemUtil.dp2px(App.getInstance(), 15));
            }else{
                outRect.set(0, SystemUtil.dp2px(App.getInstance(), 0.5f), 0, 0);
            }
        }
    }
}
