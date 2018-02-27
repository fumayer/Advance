package com.aiwue.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.aiwue.BuildConfig;
import com.aiwue.R;
import com.aiwue.model.Notice;
import com.aiwue.theme.colorUi.SkinFactory;
import com.aiwue.theme.colorUi.util.ColorUiUtil;
import com.aiwue.utils.SharedPreferencesMgr;
import com.aiwue.utils.ConstantValue;
import com.aiwue.utils.RxBus;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.orhanobut.logger.Logger;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Field;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

/**
  *  acticity 基类
  * Created by Yibao on 17/4/11
  * Copyright (c) 2017 aiwue.com All rights reserved
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected Context mContext;
    private CompositeSubscription mCompositeSubscription;//管理这
    protected Subscription mSubscription;//Rxjava用来实现消息传递和线程切换操作
    private boolean isFullScreen;
    private boolean isDestroyed = false;
    protected boolean isShowing = false;
    static boolean isHomePressed = false;
    //public static int sShowingActivities = 0;

    private CompositeSubscription onStopSubscriptions = new CompositeSubscription();
    private CompositeSubscription onPauseSubscriptions = new CompositeSubscription();
    private CompositeSubscription onDestroySubscriptions = new CompositeSubscription();

    public enum LifeCycleEvent {//生命周期相关
        ON_PAUSE, ON_STOP, ON_DESTROY;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.d("onCreate");
        //友盟统计
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
        mContext = this;
        if (SharedPreferencesMgr.getInt(ConstantValue.SP_THEME, ConstantValue.THEME_LIGHT) == ConstantValue.THEME_LIGHT) {
            setTheme(R.style.Theme_Light);//设置夜间模式还是白天模式
        } else {
            setTheme(R.style.Theme_Night);
        }
        mSubscription = toObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Notice>() {
            @Override
            public void call(Notice message) {
                if (message.type == ConstantValue.MSG_TYPE_CHANGE_THEME)//切换主题
                    ColorUiUtil.changeTheme(getWindow().getDecorView(), getTheme());
            }

        });
        setLayoutInflaterFactory();
        initView(savedInstanceState);

        try {
            EventBus.getDefault().register(this);
        } catch (Exception e) {
            Timber.e(e);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopSubscriptions(LifeCycleEvent.ON_STOP);
        isShowing = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        stopSubscriptions(LifeCycleEvent.ON_PAUSE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        isShowing = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        onUnsubscribe();
        if (mSubscription != null) mSubscription.unsubscribe();
        stopSubscriptions(LifeCycleEvent.ON_DESTROY);
        exitFullscreen();
        isDestroyed = true;

        try {
            if (EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().unregister(this);
            }
        } catch (Exception e) {
            Timber.e(e);
        }
    }

    @Override
    public boolean isFinishing() {
        boolean ret = super.isFinishing();
        if (Build.VERSION.SDK_INT >= 17) {
            ret |= super.isDestroyed();
        } else {
            ret |= isDestroyed;
        }
        return ret;
    }

    public <T extends View> T findView(int id){
        return (T) findViewById(id);
    }

    public void initToolBar(int titleId){
        initToolBar(titleId, -1);
    }
    public void initToolBar(int titleId, int menuId){
        initToolBar(getResources().getString(titleId), menuId);
    }

    public void initToolBar(String title){
        initToolBar(title, -1);
    }

    public void initToolBar(String title,int menuId){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (title != null)
            toolbar.setTitle(title);

        if (menuId > 0) {
            toolbar.inflateMenu(menuId);
        }
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void bindingEvent(Subscription subscription, LifeCycleEvent event) {
        if (event != null && subscription != null) {
            switch (event) {
                case ON_PAUSE:
                    onPauseSubscriptions.add(subscription);
                    break;

                case ON_STOP:
                    onStopSubscriptions.add(subscription);
                    break;

                case ON_DESTROY:
                    onDestroySubscriptions.add(subscription);
            }
        }
    }

    protected void stopSubscriptions(LifeCycleEvent event) {
        if (event == null) {
            return;
        }

        switch (event) {
            case ON_PAUSE:
                onPauseSubscriptions.clear();
                break;
            case ON_STOP:
                onStopSubscriptions.clear();
                break;
            case ON_DESTROY:
                onDestroySubscriptions.clear();
                break;
        }
    }

    public void exitFullscreen() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
    }

    protected void setFullScreen() {
        exitFullscreen();

        int visibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            visibility |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }

        getWindow().getDecorView().setSystemUiVisibility(visibility);
        isFullScreen = true;
    }

    public void setLayoutInflaterFactory() {//这个不懂
        LayoutInflater layoutInflater = getLayoutInflater();
        try {
            Field mFactorySet = LayoutInflater.class.getDeclaredField("mFactorySet");
            mFactorySet.setAccessible(true);
            mFactorySet.set(layoutInflater, false);
            LayoutInflaterCompat.setFactory(layoutInflater, new SkinFactory(this));//皮肤工厂
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public RecyclerView initCommonRecyclerView(BaseQuickAdapter adapter, RecyclerView.ItemDecoration decoration) {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        return initCommonRecyclerView(recyclerView, adapter, decoration);
    }

    public RecyclerView initCommonRecyclerView(RecyclerView recyclerView, BaseQuickAdapter adapter, RecyclerView.ItemDecoration decoration) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if (decoration != null) {
            recyclerView.addItemDecoration(decoration);
        }
        recyclerView.setAdapter(adapter);
        return recyclerView;
    }

    public RecyclerView initHorizontalRecyclerView(BaseQuickAdapter adapter, RecyclerView.ItemDecoration decoration) {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        if (decoration != null) {
            recyclerView.addItemDecoration(decoration);
        }
        recyclerView.setAdapter(adapter);
        return recyclerView;
    }

    public RecyclerView initGridRecyclerView(int resId, BaseQuickAdapter adapter, RecyclerView.ItemDecoration decoration, int spanCount) {
        RecyclerView recyclerView = (RecyclerView) findViewById(resId == 0 ? R.id.recyclerView : resId);
        recyclerView.setLayoutManager(new GridLayoutManager(this, spanCount));
        if (decoration != null) {
            recyclerView.addItemDecoration(decoration);
        }
        recyclerView.setAdapter(adapter);
        return recyclerView;
    }

    public RecyclerView initGridRecyclerView(BaseQuickAdapter adapter, RecyclerView.ItemDecoration decoration, int spanCount) {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, spanCount));
        if (decoration != null) {
            recyclerView.addItemDecoration(decoration);
        }
        recyclerView.setAdapter(adapter);
        return initGridRecyclerView(0, adapter, decoration, spanCount);
    }

    /**
     * 初始化界面
     */
    protected void initView(Bundle savedInstanceState) {
        loadViewLayout();
        bindViews();
        processLogic(savedInstanceState);
        setListener();
    }

    protected void showLog(String log) {
        Logger.i(log);
    }


    /**
     * 获取控件
     *
     * @param id  控件的id
     * @param <E>
     * @return
     */
    protected <E extends View> E get(int id) {
        return (E) findViewById(id);
    }

    /**
     * 加载布局
     */
    protected abstract void loadViewLayout();

    /**
     * find控件
     */
    protected abstract void bindViews();


    /**
     * 处理数据
     */
    protected abstract void processLogic(Bundle savedInstanceState);

    /**
     * 设置监听
     */
    protected abstract void setListener();

    /**
     * 界面跳转
     *
     * @param tarActivity
     */
    protected void intent2Activity(Class<? extends Activity> tarActivity) {//实现快速跳转的方法
        Intent intent = new Intent(mContext, tarActivity);
        startActivity(intent);
    }

    /**
     * 显示Toast
     *
     * @param msg
     */
    protected void showToast(String msg) {
//        ToastUtils.showToast(msg);
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
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

    //RXjava取消注册，以避免内存泄露
    public void onUnsubscribe() {
        if (mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.unsubscribe();
        }
    }

    public void addSubscription(Observable observable, Subscriber subscriber) {//将所有的观察者和被观察者用管理者对象进行管理
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

}
