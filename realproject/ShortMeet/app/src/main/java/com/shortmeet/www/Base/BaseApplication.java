package com.shortmeet.www.Base;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.aliyun.common.crash.CrashHandler;
import com.aliyun.common.httpfinal.QupaiHttpFinal;
import com.aliyun.common.logger.Logger;
import com.aliyun.downloader.DownloaderManager;
import com.shortmeet.www.Api.ApiConstant;
import com.shortmeet.www.Api.RetrofitApi.RetrofitClient;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.cookie.CookieJarImpl;
import com.zhy.http.okhttp.cookie.store.PersistentCookieStore;
import com.zhy.http.okhttp.https.HttpsUtils;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import cn.jzvd.JZVideoPlayerStandard;
import okhttp3.OkHttpClient;

/**
 * Created by Fenglingyue on 2017/8/20.
 */

public class BaseApplication extends Application {
    // Fly 注：阿里oss
    private static OSSClient oss;
    private static BaseApplication mApplicaInstance;
    public JZVideoPlayerStandard VideoPlaying;
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
    public static BaseApplication getInstance() {
        if (mApplicaInstance == null) {
            synchronized (BaseApplication.class) {
                if (mApplicaInstance == null) {
                    mApplicaInstance = new BaseApplication();
                }
            }
        }
        return mApplicaInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApplicaInstance=this;
        // Fly 注：okhttputils 初始化
        initOkhttpUtil();
        // Fly 注：初始化 retrofit
        RetrofitClient.getRetroClientInstance().initRetrofit();
        //阿里拍摄开始
        loadLibs();
        QupaiHttpFinal.getInstance().initOkHttpFinal();
        Logger.setDebug(true);
        initDownLoader();
        //阿里拍摄结束
        //初始化 阿里存储
        initALi();
    }
    // Fly 注：  okhttputils 初始化
    public  void  initOkhttpUtil(){
        /**
         * 配置OkHttpUtils 对应参数
         * 设置log
         * 设置连接超时时间
         * 设置读取超时时间
         * 设置cookie持久化
         * 设置可以访问所有的https网站
         */
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                //.addInterceptor(new MyProjectInterceptor())
                .addInterceptor(new LoggerInterceptor("ShortMeet", true))
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .cookieJar(new CookieJarImpl(new PersistentCookieStore(getApplicationContext())))
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .build();
                OkHttpUtils.initClient(okHttpClient);
    }






    /*
     *  Fly 注： 有关阿里云拍摄开始
     */
    private void loadLibs(){
        System.loadLibrary("openh264");
        System.loadLibrary("encoder");
        System.loadLibrary("QuCore-ThirdParty");
        System.loadLibrary("QuCore");
    }
    HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    private void initDownLoader() {
        DownloaderManager.getInstance().init(this);
    }

    private  void trustAllHosts() {
        // Create a trust manager that does not validate certificate chains
        // Android use X509 cert
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[] {};
            }

            public void checkClientTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }
        } };

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection
                    .setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(DO_NOT_VERIFY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void localCrashHandler() {
        CrashHandler catchHandler = CrashHandler.getInstance();
        catchHandler.init(getApplicationContext());
    }
    // Fly 注： Fly 注： 有关阿里云拍摄结束



    /*
     *  Fly 注： 阿里云初始化   （实现上传功能）
     */
    private void initALi() {
       //  明文设置secret的方式建议只在测试时使用，更多鉴权模式请参考后面的`访问控制`章节
        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(ApiConstant.accessKeyId, ApiConstant.accessKeySecret);
        oss = new OSSClient(getApplicationContext(), ApiConstant.ENDPOINT, credentialProvider);
    }


     //获取阿里Oss对象
    public static OSS getOss() {
        return oss;
    }

}
