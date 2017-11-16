package com.shortmeet.www.Base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shortmeet.www.R;
import com.shortmeet.www.utilsUsed.DialogUtils;
import com.shortmeet.www.utilsUsed.ToastUtils;


/**
 * Created by Fenglingyue on 2017/8/19.
 */

public abstract class BaseFragment extends Fragment{
    public Context  mContext;
    public Activity mActivity;  //宿主activity
    public LayoutInflater mLayoutInflater;
    public View  titleView;  //标题栏
    //标题栏上的控件
    public FrameLayout titleBarLeftframelayout;
    public ImageView imgvLeftbackTitlebar;
    public TextView tvCenterTitleBar;
    public TextView tvRightTitlebar;
    public boolean viewIsAdded;
    public View contentView;


    // Fly 注：设置界面跟布局
    public abstract  int  setFragRootView();

    // Fly 注：初始化控件
    public abstract  void initView();

    // Fly 注：初始化监听
    public abstract  void  initListener();

    // Fly 注：初始化数据
    public abstract  void initData();

    // Fly 注： 是否使用标题栏
    public boolean setUseTitleBar(){
       return  false;
    }

    // Fly 注：获取宿主Activity  返回宿主Activity 实例
    public Activity getHoldingActivity(){
        return mActivity;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!viewIsAdded){
            viewIsAdded=true;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
          mActivity= (BaseActivity) this.getActivity();
          mLayoutInflater=inflater;
          contentView=setMyFragContentView(container);
          mContext= this.getContext();
          initView();
          initListener();
          initData();
          return contentView;
    }

     public View setMyFragContentView(ViewGroup container){
         if(setUseTitleBar()){
         LinearLayout  linearLayout=new LinearLayout(mContext);
         linearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
         linearLayout.setOrientation(LinearLayout.VERTICAL);
         titleView=mLayoutInflater.inflate(R.layout.layout_titlebar,linearLayout,false);
         titleBarLeftframelayout = (FrameLayout) titleView.findViewById(R.id.title_bar_leftframelayout);
         imgvLeftbackTitlebar = (ImageView)titleView. findViewById(R.id.imgv_leftback_titlebar);
         tvCenterTitleBar = (TextView)titleView. findViewById(R.id.tv_center_title_bar);
         tvRightTitlebar = (TextView) titleView.findViewById(R.id.tv_right_titlebar);
         View rootView=mLayoutInflater.inflate(setFragRootView(),linearLayout,false);
         if(viewIsAdded){    //控制 只添加一次和
           linearLayout.addView(titleView);
           linearLayout.addView(rootView);
             viewIsAdded = false;
         }
          return  linearLayout;
         }
         return mLayoutInflater.inflate(setFragRootView(),container,false);
     }

     // Fly 注：向宿主Acyivity  中添加fragment
//    public  void addFragment(int containId,BaseFragment  fragment){
//        if(null!=fragment){
//            getHoldingActivity().addFragment(containId,fragment);
//        }
//    }
//   // Fly 注：移除frag
//    public  void removeFragment(){
//        getHoldingActivity().removeFragment();
//    }

    // Fly 注： 显示加载 进度条(带文字)、 不带文字、 取消加载进度
    public void showLoading(String msg){
        DialogUtils.showProgress(mActivity, TextUtils.isEmpty(msg)? "加载中":msg);
    }

    public void hideLoading(){
        DialogUtils.closeProgress();
    }

    // Fly 注：弹出土司
    public void  showMessage(String msg){
        if(!TextUtils.isEmpty(msg)){
            ToastUtils.showShort(mActivity,msg);
        }
    }
}
