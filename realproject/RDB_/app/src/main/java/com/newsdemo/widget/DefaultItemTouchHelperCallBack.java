package com.newsdemo.widget;

import android.graphics.ImageFormat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by jianqiang.hu on 2017/5/26.
 */

public class DefaultItemTouchHelperCallBack extends ItemTouchHelper.Callback{


    /**
     * Item 操作的回调
     */
    OnItemTouchCallbackListener onItemTouchCallbackListener;

    /**
     * 是否可以拖拽
     */
    private boolean isCanDrag=true;

    /**
     * 是否可以被滑动
     */
    private boolean isCanSwip=true;


    public DefaultItemTouchHelperCallBack(OnItemTouchCallbackListener onItemTouchCallbackListener){
        this.onItemTouchCallbackListener=onItemTouchCallbackListener;
    }



    /**
     * 设置是否可以被拖拽
     * @param canDrag true 是 false  否
     */
    public void setDragEnable(boolean canDrag){
        this.isCanDrag=canDrag;
    }

    /**
     * 设置是否可以被滑动
     *
     * @param canSwipe 是true，否false
     */
    public void setSwipeEnable(boolean canSwipe) {
        isCanSwip = canSwipe;
    }
    /**
     * 当Item被长按的时候是否可以被拖拽
     *
     * @return
     */
    @Override
    public boolean isLongPressDragEnabled() {
        return isCanDrag;
    }

    /**
     * Item是否可以被滑动(H：左右滑动，V：上下滑动)
     *
     * @return
     */
    @Override
    public boolean isItemViewSwipeEnabled() {
        return isCanSwip;
    }


    /**
     * 当用户拖拽或滑动Item的时候需要我们告诉系统滑动或者拖拽的方向
     * @param recyclerView
     * @param viewHolder
     * @return
     */
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        RecyclerView.LayoutManager layoutManager=  recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager){//GridLayoutManager
            //flag 如果值是0，相当于这个功能被关闭
            int dragFlag=ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            int swipeFlag=0;
            //create make
            return makeMovementFlags(dragFlag,swipeFlag);
        }else if (layoutManager instanceof LinearLayoutManager){
            //linearLayoutManager
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            int orientation = linearLayoutManager.getOrientation();


            int dragFlag=0;
            int swipFlag=0;

            // 为了方便理解，相当于分为横着的ListView和竖着的ListView
            if (orientation==LinearLayoutManager.HORIZONTAL){// 如果是横向的布局
                swipFlag=ItemTouchHelper.UP|ItemTouchHelper.DOWN;   //滑动方向
                dragFlag=ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT;    //拖拽方向
            }else if(orientation==LinearLayoutManager.VERTICAL){// 如果是竖向的布局，相当于ListView
                dragFlag = ItemTouchHelper.UP | ItemTouchHelper.DOWN;//拖拽方向
                swipFlag = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;//滑动方向
            }
            return makeMovementFlags(dragFlag,swipFlag);
        }
        return 0;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        if (onItemTouchCallbackListener!=null){
            return onItemTouchCallbackListener.onMove(viewHolder.getAdapterPosition(),target.getAdapterPosition());
        }
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if (onItemTouchCallbackListener!=null){
            onItemTouchCallbackListener.onSwipe(viewHolder.getAdapterPosition());
        }
    }



    public interface OnItemTouchCallbackListener{

        /**
         * 当某个Item被滑动删除的时候
         * @param adaperPosition
         */
        void onSwipe(int adaperPosition);

        /**
         * 当两个Item位置互换的时候被调用
         * @param srcPosition   拖拽的item的 position
         * @param targetPosition 目的地的Item的position
         * @return 开发者处理了操作应该返回true 开发者没有处理就返回false
         */
        boolean onMove(int srcPosition,int targetPosition);
    }
}
