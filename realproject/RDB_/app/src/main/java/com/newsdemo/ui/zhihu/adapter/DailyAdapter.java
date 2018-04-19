package com.newsdemo.ui.zhihu.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.newsdemo.R;
import com.newsdemo.component.GlidUtils;
import com.newsdemo.model.bean.DailyBeforeListBean;
import com.newsdemo.model.bean.DailyListBean;
import com.newsdemo.util.LogUtil;
import com.newsdemo.widget.ZhihuDiffCallback;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jianqiang.hu on 2017/5/16.
 */

public class DailyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<DailyListBean.StoriesBean> mList;
    private List<DailyListBean.TopStoriesBean> mTopList;
    LayoutInflater inflater;
    private TopPageAdapter mAdapter;
    private ViewPager topViewPager;
    private OnItemClickListener onItemClickListener;
    private boolean isBefore=false;
    private String currentTitle = "今日热闻";
    public enum ITEM_TYPE{
        ITEM_TOP,       //滚动栏
        ITEM_DATE,      //日期
        ITEM_CONTENT   //内容
    }

    public DailyAdapter (Context mContext, List<DailyListBean.StoriesBean> mList){
        this.mList=mList;
        this.mContext=mContext;
        inflater=LayoutInflater.from(mContext);
    }

    @Override
    public int getItemViewType(int position) {
        if (!isBefore){
            if (position==0){
                return ITEM_TYPE.ITEM_TOP.ordinal();
            }else if (position==1){
                return ITEM_TYPE.ITEM_DATE.ordinal();
            }else{
                return ITEM_TYPE.ITEM_CONTENT.ordinal();
            }
        }else{
            if (position==0){
                return ITEM_TYPE.ITEM_DATE.ordinal();
            }else{
                return ITEM_TYPE.ITEM_CONTENT.ordinal();
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType==ITEM_TYPE.ITEM_TOP.ordinal()){
            mAdapter=new TopPageAdapter(mContext,mTopList);
            return new TopViewHolder(inflater.inflate(R.layout.item_top,parent,false));
        }else if (viewType==ITEM_TYPE.ITEM_DATE.ordinal()){
            return new DateViewHolder(inflater.inflate(R.layout.item_date,parent,false));
        }
        return new ContentViewHolder(inflater.inflate(R.layout.item_daily,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ContentViewHolder){
            final int contentPostion;
            if (isBefore){
                contentPostion=position-1;
            }else{
                contentPostion=position-2;
            }
            ((ContentViewHolder) holder).title.setText(mList.get(contentPostion).getTitle());
            if (mList.get(contentPostion).getReadState()){
                ((ContentViewHolder) holder).title.setTextColor(ContextCompat.getColor(mContext,R.color.news_read));
            }else{
                ((ContentViewHolder) holder).title.setTextColor(ContextCompat.getColor(mContext,R.color.news_unread));
            }
            GlidUtils.load(mContext,mList.get(contentPostion).getImages().get(0),((ContentViewHolder) holder).image);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener!=null){
                        ImageView iv= (ImageView) view.findViewById(R.id.iv_daily_item_image);
                        onItemClickListener.onItemClick(contentPostion,iv);
                    }
                }
            });
        }else if(holder instanceof DateViewHolder){
            ((DateViewHolder) holder).tvDate.setText(currentTitle);
        }else{
            ((TopViewHolder)holder).vTop.setAdapter(mAdapter);
            topViewPager=((TopViewHolder) holder).vTop;
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }



    public static class ContentViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_daily_item_title)
        TextView title;

        @BindView(R.id.iv_daily_item_image)
        ImageView image;

        public ContentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public static class DateViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_daily_date)
        TextView tvDate;

        public DateViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public static class TopViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.vp_top)
        ViewPager vTop;

        @BindView(R.id.ll_point_container)
        LinearLayout llContainer;

        public TopViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener=onItemClickListener;
    }


    public void addDailyDate(DailyListBean info){
        currentTitle="今日新闻";
        DiffUtil.DiffResult diffResult=DiffUtil.calculateDiff(new ZhihuDiffCallback(mList,info.getStories()),true);
        mList=info.getStories();
        mTopList=info.getTop_stories();
        isBefore=false;
        diffResult.dispatchUpdatesTo(this);//7.0以后使用此方法代替notifyDataSetChanged，能直接比较2个数据的不同
        //        notifyDataSetChanged();
    }

    public void addDailyDefore(DailyBeforeListBean info){
        DiffUtil.DiffResult diffResult=DiffUtil.calculateDiff(new ZhihuDiffCallback(mList,info.getStories()));
        mList=info.getStories();
        isBefore=true;
        diffResult.dispatchUpdatesTo(this);
    }




    public interface OnItemClickListener{
        void onItemClick(int position,View view);
    }

    public void changeTopPager(int currentCount){
        if (!isBefore && topViewPager!=null){
            topViewPager.setCurrentItem(currentCount);
        }
    }


    public boolean getIsBefore(){
        return isBefore;
    }

    public void setReadState(int position,boolean readState){
        mList.get(position).setReadState(readState);
    }


}
