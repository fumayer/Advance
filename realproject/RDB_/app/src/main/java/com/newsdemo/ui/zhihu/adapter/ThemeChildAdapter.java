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
import com.newsdemo.model.bean.ThemeChildListBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jianqiang.hu on 2017/5/24.
 */

public class ThemeChildAdapter extends RecyclerView.Adapter<ThemeChildAdapter.ViewHolder> {

    private List<ThemeChildListBean.StoriesBean> mList;
    private Context mContext;
    private LayoutInflater inflater;
    private ThemeChildAdapter.onItemClickListener onItemClickListener;


    public ThemeChildAdapter(Context mContext, List<ThemeChildListBean.StoriesBean> mList){
        this.mContext=mContext;
        this.mList=mList;
        inflater=LayoutInflater.from(mContext);
    }


    @Override
    public ThemeChildAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ThemeChildAdapter.ViewHolder(inflater.inflate(R.layout.item_daily,parent,false));
    }
    @Override
    public void onBindViewHolder(final ThemeChildAdapter.ViewHolder holder, int position) {
        holder.title.setText(mList.get(position).getTitle());


        if (mList.get(position).getImages()!=null&&mList.get(position).getImages().size()>0){
            GlidUtils.load(mContext,mList.get(position).getImages().get(0),holder.image);
        }

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

    public void setOnItemClickListener(ThemeChildAdapter.onItemClickListener onItemClickListener){
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
