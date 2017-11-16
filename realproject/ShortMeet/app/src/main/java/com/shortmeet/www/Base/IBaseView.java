package com.shortmeet.www.Base;

/**
 * Created by Fenglingyue on 2017/8/26.
 */

public interface IBaseView<T>{
    // Fly 注：展示进度
    void showLoading(String msg);
    //Fly 注： 隐藏进度
    void hideLoading();
    //弹出土司
    void  showMessage(String msg);
    //标识当前View 是否已经初始化
    // boolean isActive();

    //网络请求成功返回的数据
    void setData(T t, int id);
}
