package com.aiwue.base;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDexApplication;
import android.view.WindowManager;

import com.aiwue.BuildConfig;
import com.aiwue.utils.SharedPreferencesMgr;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;

import timber.log.Timber;

/**
 *  Application
 * Created by Yibao on 17/4/11
 * Copyright (c) 2017 aiwue.com All rights reserved
 */
public class AiwueApplication extends MultiDexApplication {//一个程序的入口，用来实现一些信息的初始化，继承MultiDexApplcation就是为了实现动态加载然后加载超过65536的dex包

    //private UserInfo userInfo;
    private static AiwueApplication instance;//获取一个示例对象，可以在任意地方进行使用

    public static final int GUIDE_VERSION = 1;

    public static String spName = "AiwueSP";

    private static Context mAppContext = null;
    private static Application mAppEntry = null;

    public static int mScreenHeight, mScreenWidth;

    //提供给所有模块使用，在任何需要使用application context的地方，均可直接
    //使用AiwueApplication.getAppContext()来得到context
    public static Context getAppContext() {
        return mAppContext;
    }

    //提供给所有模块使用，在任何需要使用application的地方，均可直接
    //使用AiwueApplication.getApplication()来得到Application
    public static Application getApplication() {
        return mAppEntry;
    }

    //设置不完整的context，主要为了避免provider中过早使用context导致的崩溃
    //也即在provider中也可直接使用AiwueApplication.getAppContext()来得到
    //Application context
    public void SetAppBaseContext(Context cxt) {
        mAppContext = cxt;
    }

    //设置完整的context
    public void SetApplicationInfo(Application app) {
        mAppEntry = app;
        mAppContext = app.getApplicationContext();
    }

    private void initUmeng(){
        //初始化友盟主要是用来实现三方登陆的时候使用传递key

        //微信 appid appsecret
        PlatformConfig.setWeixin("wxc39271caa516adbf", "38e3ac3689c64a8dd4e290fb039ec631");
        //        PlatformConfig.setWeixin("wx967daebe835fbeac", "5bb696d9ccd75a38c8a0bfe0675559b3");

        //新浪微博 appkey appsecret
        PlatformConfig.setSinaWeibo("77046565", "76fc5e1ad731642416986bb05a2b6008");
        Config.REDIRECT_URL="http://sns.whalecloud.com/sina2/callback";
        //        PlatformConfig.setSinaWeibo("3921700954","04b48b094faeb16683c32669824ebdad");

        // QQ和Qzone appid appkey
        PlatformConfig.setQQZone("1105435721", "1jYLibu5Pa34grUg");

    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        SharedPreferencesMgr.init(this, "aiwue");//初始化管理类的信息
        initImageLoader();//初始化了ImageLoader图片加载框架的配置
        SetApplicationInfo(this);
        Timber.plant(new Timber.DebugTree() {});//用来实现打印日志使用的

        //原则上不应在主线程做耗时初始化操作，为了避免各模块误用，把所有初始化工作直接放在非主线程
        new Runnable() {
            @Override
            public void run() {
                //TODO:初始化流程还需要细化，比如针对主进程或者非进程的判断，同步初始化和异步初始化的
                //的判断（为了更好的并行初始化等).子模块多时很有必要，目前应该够用.

                //???是否应该在这做友盟的Crash Handler的初始化

                //在这里做初始化工作，包括第三方的SDK初始化
//                NetUtil.initInstance(mAppEntry);
                //初始化友盟的各个平台配置
                initUmeng();

//                UpdateManager.getInstance().initialize(mAppContext);

                //需要把这几行代码拿走，主要为了窦佳伟的代码能编译通过
                WindowManager wm = (WindowManager) mAppContext.getSystemService(Context.WINDOW_SERVICE);
                mScreenHeight = wm.getDefaultDisplay().getHeight();
                mScreenWidth = wm.getDefaultDisplay().getWidth();
            }
        }.run();


    }

    private void initImageLoader() {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(
                instance);
        config.memoryCacheExtraOptions(480, 800);
        config.diskCacheExtraOptions(480, 800, null);
        config.diskCacheSize(100 * 1024 * 1024); // 100 MiB
        if (BuildConfig.DEBUG) {
            config.writeDebugLogs(); // Remove for release app
        }
        ImageLoader.getInstance().init(config.build());
    }

    //    public UserInfo getUserInfo() {
//        return userInfo;
//    }
//
//    public void setUserInfo(UserInfo userInfo) {
//        this.userInfo = userInfo;
//    }
    public static AiwueApplication getInstance() {
        return instance;
    }
}
