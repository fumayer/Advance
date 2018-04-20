package com.newsdemo.ui.zhihu.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.newsdemo.R;
import com.newsdemo.component.GlidUtils;
import com.newsdemo.model.bean.HotListBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jianqiang.hu on 2017/5/23.
 */

public class HotAdapter extends RecyclerView.Adapter<HotAdapter.ViewHolder>{

    private List<HotListBean.RecentBean> mList;
    private Context mContext;
    private LayoutInflater inflater;
    private HotAdapter.onItemClickListener onItemClickListener;


    public HotAdapter(Context mContext, List<HotListBean.RecentBean> mList){
        this.mContext=mContext;
        this.mList=mList;
        inflater=LayoutInflater.from(mContext);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_daily,parent,false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.title.setText(mList.get(position).getTitle());
        if (mList.get(position).getReadState()){
            holder.title.setTextColor(ContextCompat.getColor(mContext,R.color.news_read));
        }else{
            holder.title.setTextColor(ContextCompat.getColor(mContext,R.color.news_unread));
        }
        GlidUtils.load(mContext,mList.get(position).getThumbnail(),holder.image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener!=null){
                    ImageView iv = (ImageView) view.findViewById(R.id.iv_daily_item_image);
                    onItemClickListener.onItemClick(holder.getAdapterPosition(),iv);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setOnItemClickListener(HotAdapter.onItemClickListener onItemClickListener){
        this.onItemClickListener=onItemClickListener;
    }

    public interface onItemClickListener{
        void onItemClick(int poition,View view);
    }

    public void setReadState(int position,boolean readState){
        mList.get(position).setReadState(readState);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_daily_item_title)
        TextView title;

        @BindView(R.id.iv_daily_item_image)
        ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

}
