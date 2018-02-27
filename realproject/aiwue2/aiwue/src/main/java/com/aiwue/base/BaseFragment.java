package com.aiwue.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import com.aiwue.model.Notice;
import com.aiwue.utils.RxBus;

import org.greenrobot.eventbus.EventBus;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import timber.log.Timber;

/**
 *  Fragment基类
 * Created by Yibao on 17/4/11
 * Copyright (c) 2017 aiwue.com All rights reserved
 */
public abstract class BaseFragment extends Fragment {
    protected Activity mContext;
    protected boolean isFirst = true;
    protected View rootView;
    protected Subscription mSubscription;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        try {
//            Field mFactorySet = LayoutInflater.class.getDeclaredField("mFactorySet");
//            mFactorySet.setAccessible(true);
//            mFactorySet.set(inflater, false);
//            LayoutInflaterCompat.setFactory(inflater, new SkinFactory((AppCompatActivity) getActivity()));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        return loadViewLayout(inflater, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = getActivity();
        rootView = view;
        initView(view);

//        if (onFragmentInitFinish != null) {
//            onFragmentInitFinish.onInitFinish();
//        }
        mSubscription = toObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Notice>() {
            @Override
            public void call(Notice message) {
//                if (message.type == ConstantValue.MSG_TYPE_CHANGE_THEME)
//                    ColorUiUtil.changeTheme(rootView, getActivity().getTheme());
            }

        });
        try {
            EventBus.getDefault().register(this);
        } catch (Exception e) {
            Timber.e(e);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSubscription != null) mSubscription.unsubscribe();
    }

    /**
     * 获取控件
     *
     * @param id  控件的id
     * @param <E>
     * @return
     */
    protected <E extends View> E get(int id) {
        return (E) rootView.findViewById(id);
    }

//    public static interface OnFragmentInitFinish {
//        void onInitFinish();
//    }
//
//    private OnFragmentInitFinish onFragmentInitFinish;
//
//    public void setOnFragmentInitFinish(OnFragmentInitFinish onFragmentInitFinish) {
//        this.onFragmentInitFinish = onFragmentInitFinish;
//    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            onVisible();
        } else {
            onInVisible();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            onVisible();
        } else {
            onInVisible();
        }
    }

    /**
     * 当界面可见时的操作
     */
    protected void onVisible() {
        if (isFirst) {
            lazyLoad();
            isFirst = false;
        }
    }

    /**
     * 数据懒加载
     */
    protected void lazyLoad() {

    }

    /**
     * 当界面不可见时的操作
     */
    protected void onInVisible() {

    }

    /**
     * 初始化界面
     *
     * @param view
     */
    private void initView(View view) {
        bindViews(view);
        processLogic();
        setListener();
    }

    /**
     * 加载布局
     */
    protected abstract View loadViewLayout(LayoutInflater inflater, ViewGroup container);

    /**
     * find控件
     *
     * @param view
     */
    protected abstract void bindViews(View view);

    /**
     * 处理数据
     */
    protected abstract void processLogic();

    /**
     * 设置监听
     */
    protected abstract void setListener();

    /**
     * 界面跳转
     *
     * @param tarActivity
     */
    protected void intent2Activity(Class<? extends Activity> tarActivity) {
        Intent intent = new Intent(mContext, tarActivity);
        startActivity(intent);
    }

    /**
     * 显示Toast
     *
     * @param msg
     */
    protected void showToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    protected void showLog(String msg) {
        Logger.i(msg);
    }

    /**
     * 注册事件通知
     */
    public Observable<Notice> toObservable() {
        return RxBus.getDefault().toObservable(Notice.class);
    }

    /**
     * 发送消息
     */
    public void post(Notice msg) {
        RxBus.getDefault().post(msg);
    }


}
