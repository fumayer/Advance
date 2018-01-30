package com.aiwue.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aiwue.R;
import com.aiwue.base.BaseActivity;
import com.aiwue.utils.ImageLoaderUtils;
import com.aiwue.utils.RxCountDown;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action0;
import rx.subscriptions.CompositeSubscription;

import static com.aiwue.R.id.banner_view;

/**
 * Created by Administrator on 2017/2/22 0022.
 */

public class SplashActivity extends BaseActivity {
    final int COUT_DOWN_TIME = 5;
    @BindView(R.id.banner_view)
    ImageView mBannerView;//控件注入使用的Butterknife
    @BindView(R.id.splash_view)
    ImageView mSplashView;
    @BindView(R.id.skip_real)
    TextView mSkipReal;
    @BindView(R.id.guide_fragment)
    FrameLayout mGuideFragment;
    @BindView(R.id.ad_click_small)
    ImageView mAdClickSmall;
    @BindView(R.id.ad_click)
    LinearLayout mAdClick;
    @BindView(R.id.ad_skip_loading)
    ImageView mAdSkipLoading;
    @BindView(R.id.ad_ignore)
    FrameLayout mAdIgnore;
    @BindView(R.id.splash_video_frame)
    FrameLayout mSplashVideoFrame;
    @BindView(R.id.splash_video_layout)
    RelativeLayout mSplashVideoLayout;
    private Subscription mSubscription;

    @Override
    protected void loadViewLayout() {//这个是在基类中使用了模板模式定义了程序的执行流程，初始化了界面
        setContentView(R.layout.splash_activity);
        ButterKnife.bind(this);//初始化然后进行绑定
//        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
//        mContext.startActivity(intent);

//          测试网络接口代码
//        AiwueClient.getChannelList(this, LifeCycleEvent.ON_DESTROY, Schedulers.io(), AndroidSchedulers.mainThread(), new BaseCallBack<getChannelListResponse>() {
//            @Override
//            public void onNext(getChannelListResponse getChannelListResponse) {
//                Timber.d("getChannelList onResponse result: %s", getChannelListResponse.toString());
//            }
//        });
    }

    @Override
    protected void bindViews() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        //加广告代码
        mSubscription = RxCountDown.countDown(COUT_DOWN_TIME)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        ImageLoaderUtils.displayBigImage("http://www.3vsheji.com/uploads/allimg/151222/1F92594D_0.jpg", mSplashView);
                        mAdClickSmall.setVisibility(View.VISIBLE);
                        mSplashView.setVisibility(View.VISIBLE);
                        mAdIgnore.setVisibility(View.VISIBLE);
                    }
                })
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        Logger.i("结束倒计时");
                        goMain();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        Logger.i("正在倒计时：" + integer);
                        mSkipReal.setText(TextUtils.concat(integer.intValue() + "s", getResources().getString(R.string.splash_ad_ignore)));
                    }
                });
//        goMain();
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSubscription != null&&!mSubscription.isUnsubscribed())
            mSubscription.unsubscribe();
    }


    @OnClick(R.id.skip_real)
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.skip_real:
                goMain();
                break;
        }
    }

    private void goMain() {
        if (mSubscription != null&&!mSubscription.isUnsubscribed())
            mSubscription.unsubscribe();//清除掉所有的观察者和被观察者然后释放资源
        intent2Activity(MainActivity.class);
        finish();
    }

    @Subscribe
    public void onEvent(String event){
        //还使用了EventBus
    }
}
