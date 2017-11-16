package com.shortmeet.www.Base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shortmeet.www.utilsUsed.DialogUtils;
import com.shortmeet.www.utilsUsed.ToastUtils;

import java.lang.reflect.Field;

/**
 * Created by Fenglingyue on 2017/8/19.
 *  懒加载
 */

public  abstract class BaseLazyFragment extends Fragment {
    //rootView是否初始化标志，防止回调函数在rootView为空的时候触发
    public boolean hasCreateView;
    //当前Fragment是否处于可见状态标志，防止因ViewPager的缓存机制而导致回调函数的触发
    public boolean isFragmentVisible;
    //是否已经初始化了
    public boolean isInit;
    public LayoutInflater mInflater;
    public Context mContext;
    public View rootView;

    // Fly 注：初始化控件
    public abstract  void initView();
    // Fly 注：初始化监听
    public abstract  void  initListener();
    //   获取跟布局
    public abstract  int  getMyLazyRootView();
    //  懒加载 执行的方法  （常做联网操作）
    public abstract  void lazyLoad();

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        this.mContext = context;
//    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (rootView == null) {
            return;
        }
           hasCreateView = true;
        if (isVisibleToUser) {
            onFragmentVisibleChange(true);
            isFragmentVisible = true;
            return;
        }
        if (isFragmentVisible) {
            onFragmentVisibleChange(false);
            isFragmentVisible = false;
        }
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVariable();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.mInflater = inflater;
         mContext=this.getContext();
        if (rootView == null) {
            rootView = getRootView(container);
            initView();
            initListener();
            isInit = true;
        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }
        return rootView;
    }
    public View getRootView(ViewGroup container){
        return mInflater.inflate(getMyLazyRootView(),container,false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!hasCreateView && getUserVisibleHint()) {
            onFragmentVisibleChange(true);
            isFragmentVisible = true;
        }
    }
    public void initVariable() {
        hasCreateView = false;
        isFragmentVisible = false;
    }
    public void onFragmentVisibleChange(boolean isVisible) {
        if (isVisible) {
            if (isInit) {
                lazyLoad();//第一次加载数据的时候调用
                isInit = false;
            }
        } else {
               stopLoad();
        }
    }
    /**
     * 当视图已经对用户不可见并且加载过数据，如果需要在切换到其他页面时停止加载数据，可以覆写此方法
     */
    protected void stopLoad() {
    }
    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    // Fly 注： 显示加载 进度条(带文字)、 不带文字、 取消加载进度
    public void showLoading(String msg){
        DialogUtils.showProgress(mContext, TextUtils.isEmpty(msg)? "加载中":msg);
    }

    public void hideLoading(){
        DialogUtils.closeProgress();
    }

    // Fly 注：弹出土司
    public void  showMessage(String msg){
        if(!TextUtils.isEmpty(msg)){
            ToastUtils.showShort(mContext,msg);
        }
    }
}
