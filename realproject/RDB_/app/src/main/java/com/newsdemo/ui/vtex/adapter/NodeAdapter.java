package com.newsdemo.ui.vtex.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.newsdemo.R;
import com.newsdemo.app.Constants;
import com.newsdemo.ui.vtex.activity.NodeListActivity;
import com.newsdemo.util.SystemUtil;
import com.newsdemo.widget.FlowLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jianqiang.hu on 2017/5/27.
 */

public class NodeAdapter extends RecyclerView.Adapter<NodeAdapter.ViewHolder>{

    private Context mContext;
    private LayoutInflater inflater;
    private ArrayMap<String, ArrayMap<String,String>> mMap;

    public NodeAdapter(Context context, ArrayMap<String, ArrayMap<String,String>> mMap) {
        this.mContext = context;
        this.mMap = mMap;
        inflater = LayoutInflater.from(mContext);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_node,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvTitle.setText(mMap.keyAt(position));
        holder.flContent.removeAllViews();

        ArrayMap<String,String> mNodeBlock = mMap.valueAt(position);
        for (ArrayMap.Entry<String,String> node:mNodeBlock.entrySet()){
            TextView tvNode=new TextView(mContext);
            tvNode.setText(node.getValue());
            tvNode.setTextColor(ContextCompat.getColor(mContext,R.color.colorText));
            tvNode.setPadding(SystemUtil.dp2px(6f), SystemUtil.dp2px(6f), SystemUtil.dp2px(6f), SystemUtil.dp2px(6f));

           tvNode.setOnClickListener(new OnNodeClickLisener(node.getKey(),node.getValue()));
            holder.flContent.addView(tvNode);
        }
    }


    /**
     * text 点击
     */
    private class OnNodeClickLisener implements View.OnClickListener{
        private String nodeName;
        private String nodeValue;
        public OnNodeClickLisener(String nodeName,String nodeValue){
            this.nodeName=nodeName;
            this.nodeValue=nodeValue;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(mContext, NodeListActivity.class);
            intent.putExtra(Constants.IT_VTEX_NODE_NAME,nodeName);
            intent.putExtra(Constants.IT_VTEX_NODE_VALUE,nodeValue);
            mContext.startActivity(intent);
        }
    }


    @Override
    public int getItemCount() {
        return mMap.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_node_title)
        TextView tvTitle;
        @BindView(R.id.fl_node_content)
        FlowLayout flContent;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
